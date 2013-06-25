# Query Engine Design

A common use case for the RSA is to export a section of the data and perform
some operations on it, such as filtering for pixels that meet certain criteria.
For example:

 - Find all pixels that represent water over a given time period.
 - Find all pixels that represent vegetation above 1000m.

Rsaquery is a programmable image processing system that allows the creation of
such queries. The query engine is specialised for the type of data stored in the
RSA. The program allows users to define:

 - *Filters* that transform image data.
 - *Queries* that link filters to specific datasets.

Filters are implemented as Java classes using a special API, and queries are
defined using XML. The program has no dependency on the RSA, so it can be used
to run queries on local files as well.

The query engine can handle images with any number of heterogeneous bands and
(currently) up to 4 dimensions. Optionally, the number of dimensions can be
reduced during processing, e.g. summarising data over time.

Queries in rsaquery are constructed as directed acyclic graphs (DAGs) of
filters, like pipelines or workflows in other applications. An illustration of
a filter pipeline is shown below.

![Diagram of a filter graph](images/query-graph-simple.png)

> Filter pipeline with one input (I), one output (O) and two filters (f and g).
> Data flows from left to right.

Rsaquery hides many of the error-prone parts of image processing by
encapsulating pixel iteration. Filters are effectively written as kernel
functions that take coordinates and return pixel values; the query engine will
call the kernel for each pixel in the image and write the result to the output.
The algorithm is summarised below.

    for pixel in output:
	    for band in output:
		    invoke filter for current pixel
		    transfer filtered value of this band to output

A filter with multiple output connections may be invoked several times per
pixel; however, it will not run again unless the pixel coordinates have changed.
All user-defined filter code runs in the invoke filter step.

## Filters

From the perspective of a filter writer, the design of the query engine is quite
simple, as shown below.

![Class diagram of a filter](images/query-filter-class.png)

> Filter class diagram.

The main goal of a filter is to populate a dataset; however, it does not iterate
over the output array to do so. Instead, the framework will call the filter's
kernel function once (roughly) for every cell in the output array.

The filter may return values from any source: it could read it from an input,
perform some arithmetic operation on multiple inputs, or generate a value based
only on the coordinates (as a procedural texture). If it reads input from a
`PixelSource`, that input may be a source dataset (e.g. a NetCDF file) or
another filter.

The code of a basic filter is shown below. It simply adds a constant (but
configurable) value to every pixel.

```java
@InheritDimensions(from = "input")
public class AddConst implements Filter {

	public int value = 1;

	public PixelSource input;

	@CellType("input")
	public Cell output;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		Element<?> currentCell = input.getPixel(coords);
		currentCell.add(value);
		output.set(currentCell);
	}
}
```

The element type is not specified in this filter: it could be a byte, float or
any other basic numeric type. It could even be a vector, in which case the value
would be added to each of the components. The actual type is resolved at runtime
based on the configuration supplied by the user.

### Input, Output and Configuration

A filter should take zero or more inputs, and produce one or more outputs. It
does this by:

 - Reading configuration from primitive fields.
 - Taking input from `PixelSources`, which may be source datasets (e.g. a NetCDF
   file) or other filters.
 - Writing output to `Cells`, which may then be written to a target dataset or
   read by other filters.

`PixelSources` and `Cells` can be seen as circular white sockets in the filter
graph shown above.

A filter should simply declare these fields and use them for I/O. The framework
will assign values to them during query initialisation. The life-cycle of a
`Filter` instance is:

 - *Construction*: Default values for parameters should be assigned now.
 - *Configuration*: The query engine will assign values to the public members as
   required by the query being run. It is up to the user to define how these
   should be instantiated (see below).
 - *Initialisation*: The initialise method is called once after configuration.
   The `PixelSources`, `Cells` and other parameters have already been set, so
   this is where the filter should instantiate private fields that it will use
   during execution.
 - *Execution*: The kernel method is called roughly once per output pixel.
   Ideally, each filter would be called once for every unique output coordinate.
   Often this will be the case; however it is not guaranteed.

Filters have a couple of properties that may not be obvious from reading their
source: their dimensionality and input/output data types. Often these are
extrinsic to the filter, and are determined at runtime. However, the filter
author must guide the query engine so that they can be determined.

### Dimensionality

The dimensionality of a filter is inherited from one of its inputs. In the
`AddConst` example given above, the input field determines the dimensionality
as specified by the `@InheritDimensions` annotation. For example, if input is a
3D dataset, the filter will be expected to produce 3D output - so the coords
argument of the kernel method will be of rank 3.

Some filters change the number of dimensions: for example, it is common for a
filter to take a 3D input that varies in *(time, y, x)* and reduce it to a 2D
output that varies in *(y, x)*. To do that, the filter should be annotated with
`@InheritDimensions(value = "input", reduceBy = 1)`. In that case, the filter
will receive 2D coordinates from the query engine; it is the filter's
responsibility to promote them to 3D for sampling the input pixel source. The
`Reduction` class allows for easy iteration over the outer dimension.

![Diagram of a filter graph](images/query-graph-reduction.png)

> Illustration of a reduction filter. The numbers indicate how many dimensions
> each socket has.

All outputs of a given filter have the same dimensionality as the filter itself.

### Data Types

Filters in rsaquery are intended to be reused in various queries and on
different input and output datasets. The query engine provides a generic way to
manipulate numeric data to allow filters to operate in a wide variety of
situations. The numeric types are found in the `org.vpac.ndg.query.math`
package; the classes are shown and summarised below.

![Class diagram of the numerical Element types](images/query-element-class.png)

> Data types class diagram.

 - `Element`: This is the most generic data type, and the super type of all other numerics. It supports all basic arithmetic operations such as add and multiply, and bounding operations such as finding the minimum and maximum of two values.

    In addition to storing a numeric value, the Element type has the concept of validity, which is a basic kind of metadata. An element will be tainted (marked as invalid) if it is read from a nodata pixel from the source image, or if an arithmetic operation failed (e.g. divide by zero). The taint is optionally passed to other elements through arithmetic operations. For example, adding one to an invalid element results in an invalid element. When writing to the output, invalid elements will be recorded using the nodata value declared for the target band (which is specified using the `_FillValue` or `valid_range` attributes in NetCDF).

 - `ScalarElement`: Like `Element`, but has a natural ordering and so may be compared with other numbers. Scalar elements can be converted to primitive types.
 - `VectorElement`: A heterogeneous collection of `ScalarElements`. It can be used to perform arithmetic on multiple values, e.g. the add method performs component-wise addition. This type has no natural ordering.

Filters should be written to use the most generic type possible, as this will make them easier to apply to different datasets. If the filter needs to use a specific numeric type (e.g. float), the *element* type should be converted into float instead of using the primitive `float` type - this ensures nodata values are preserved, and allows vectors to be used if the user requires them. The correct way to coerce values is as follows:

```java
Element<?> val = input.getPrototype().asFloat();
val.set(input.getPixel(coords));
```

For speed, the first line should actually be done in the filter's `initialise` method, with `val` stored as a field in the class.

Output fields may be explicitly declared as scalar or vector by using the `CellScalar` and `CellVector` classes. The numeric type can either be inherited from the inputs, or specified by the filter. This is done by placing the `@CellType` annotation above a `Cell` field. If the value of that annotation is a primitive type, e.g. "float", then the cell with have that type. Otherwise, its type will be inherited from an input field with the same name. For example:

```java
@CellType("input")
@CellType("input,float")
@CellType(value = "input", as = "float")
```

From top to bottom: inherit type from input field; create a vector type as the concatenation of the type of the input field followed by a float; create a type with the same rank as input (may be scalar or vector), but ensure all components are floats.

The types of pixel sources can not be specified - except that they may be declared as scalar or vector using the `PixelSourceScalar` and `PixelSourceVector` classes.

### Coordinates

Coordinates in rsaquery are embodied by the `VectorReal` class. Like `Element`s, these vectors support basic arithmetic. Unlike `VectorElement`, these are homogeneous collections of floats - a distinction that was made to increase performance and simplify pixel addressing.

Filters operate in a global coordinate system, which is defined as the pixel space of the output dataset.  Where the output has fewer dimensions than the input, the global coordinate system is constructed as though the output has the same number of dimensions. By default, the bounds of the coordinate system will be the union of the bounds of the input datasets, as shown below. However the user can define different bounds when creating a query.

![Diagram of coordinate system and image alignment. Image shows two rectangular prisms intersecting, encompassed by an axis-aligned bounding box.](images/query-coordinates.png)

> Coordinate spaces. Grey: inputs. Blue: global. The dashed lines represent a
> dimension (time) that is part of the global coordinate system but not part
> of the output dataset.

The `kernel` method of a filter takes a `VectorReal` as an argument; this object represents the coordinates of the current pixel in the global coordinate system (i.e. the output coordinates). The "first" pixel in a 2D image covers the coordinates *(0.0, 0.0)* to *(1.0, 1.0)*; the first coordinate passed to the filter will be the centre of that pixel at *(0.5, 0.5)*.

#### Component Order

Vector components are specified in NetCDF canonical order, with the most slowly-varying dimension first: *(w, z, y, x)*. Components can also be addressed using letters A-J, with A as the fastest-varying dimension. X, Y, Z and W are synonymous with A, B, C and D, respectively. T (time) is special: it is always the first component. For example, given the vector *(0.0, 1.0, 2.0)*, *x = 2.0*, *y = 1.0*, and *z = t = 0.0*.

#### Bounds

The `PixelSources` (inputs) of a filter are also in the global coordinate system. Where an input dataset has a different origin, scale or projection than the output dataset, the sampler will automatically transform the coordinates when `getPixel` is called. Therefore filters do not need to alter the coordinates when reading from an input - although there are several cases where it is desirable to do so, such as when the filter wishes to warp an image or read from multiple pixels.

Each `PixelSource` has a bounding box that encompasses the useful extents of the data; these are shown as the grey boxes in the figure above. The bounding box can be retrieved using the `getBounds` method. It can be used when traversing input data to avoid needlessly processing non-existent pixels. Note that it is not an error to access a pixel outside the bounds of an image; the behaviour in this case is configurable by the user. By default, the nearest in-bounds pixel will be returned. Also note that, due to the coordinate transforms, the data may not actually fill the box.

#### Vector Swizzling

[Swizzling][swiz] is an operation that transforms a vector by rearranging its components. The operation is specified as a format string, which gets compiled to a `Swizzle` object that can be applied to vectors. Their use is similar to that of regular expressions.

Swizzles has a number of advantages over manual manipulation of the components. They:

 - Are concise and expressive.
 - Are easy to build programmatically, e.g. when the number of components is not known in advance.
 - Can be compiled once and used on different vectors.

[swiz]: http://en.wikipedia.org/wiki/Swizzling_(computer_graphics)

#### Pixel Iterators

The query engine calls filter `kernel` methods once per pixel - so there is often no need for a filter to do its own iteration. However there are cases where it is required; for example, when performing an operation on neighbouring pixels, such as a blur.

Iteration helper classes are available in the `org.vpac.ndg.query.iteration` package. They are:

 - `Reduction`: This allows filters to sample an n+1D input image from nD output coordinates. For example, given an input image with three time coordinates and a *(y, x)* output coordinate of *(5, 4)*, the iterator would generate the input coordinates *(0, 5, 4)*, *(1, 5, 4)*, *(2, 5, 4)*. The filter should initialise it with the bounding box of the region to traverse, and the output coordinates.
 - `Rectangle`: Iterates over coordinates within an nD rectangular window of pixels. This iterator returns a `CoordinatePair` object, which contains the integer index within the window and the real coordinates in the image. The filter should initialise it with the shape of the window, and the output coordinates to centre the window on.
 - `Kernel`: Like `Rectangle`, but returns values from a convolution kernel during iteration. Returns a `KernelPair` object, which contains input image coordinates as well as the corresponding convolution kernel value. The filter should initialise it with the shape of the kernel, an array of kernel values, and the coordinates to centre the window on.

The following code shows how to use a `Reduction` iterator.

```java
public void initialise(BoxReal bounds) throws QueryConfigurationException {
	val = input.getPrototype().copy();
	reduction = new Reduction(input.getBounds());
}

public void kernel(VectorReal coords) throws IOException {
	val.set(0);
	for (VectorReal varcoords : reduction.getIterator(coords)) {
		val.add(input.getPixel(varcoords));
	}
	output.set(val);
}
```

> Excerpt of a reduction filter: finds the sum of the values at the current
> coordinates across the outer dimension.

### Multiple Inputs and Outputs
This section presents a more complicated filter that operates on three different inputs, and produces two outputs. This filter performs a reduction, searching across time to find the pixel with the highest value. When it finds the maximum value, two values are written to the output dataset:

 1. The value of the highest pixel at the current coordinates.
 1. The time that corresponds to that value.

The first half of the class (initialisation) is shown below.

```java
@InheritDimensions(from = "toMaximise", reduceBy = 1)
public class MaximiseForTime implements Filter {

	public PixelSourceScalar toMaximise;
	public PixelSource toKeep;

	@Constraint(dimensions=1)
	public PixelSource intime;

	@CellType("toKeep")
	public Cell output;
	@CellType("intime")
	public Cell outtime;

	Reduction reduction;
	VectorReal tco = VectorReal.createEmpty(1);

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		reduction = new Reduction(toMaximise.getBounds());
	}
```

> Construction and initialisation code for the `MaximiseForTime` filter.

The filter explicitly declares the `toMaximise` field as a scalar input; this is because it needs to compare values from that pixel source. The values in `toKeep` will just be written to the output field, so it doesn't matter what their type is. In practice, these two fields will often be configured to point to the same scalar input band, but there is no need to restrict the filter to that use case.

The intime field is a coordinate axis in the input dataset that represents time. It should be 1D; if not, the `@Constraint` annotation will cause an exception to be thrown during query initialisation. Time values will be written to `outtime` - but `outtime` may not be 1D. This is because the outputs have the same dimensions as the filter itself, and the filter declares itself as a reduction on `toMaximise`. Often, `toMaximise` and `toKeep` will be 3D, `intime` will be 1D and `output` and `outtime` will be 2D.

The second half of the filter is shown below.

```java
	@Override
	public void kernel(VectorReal coords) throws IOException {
		double besttime = 0;
		ScalarElement max = null;

		// Search over all times.
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			ScalarElement val = toMaximise.getScalarPixel(varcoords);
			if (!val.isValid())
				continue;
			if (max == null || val.compareTo(max) > 0) {
				besttime = varcoords.getT();
				max = val;
			}
		}

		// Store result.
		if (max == null) {
			output.unset();
			outtime.unset();
		} else {
			VectorReal co = reduction.getSingle(coords, besttime);
			output.set(toKeep.getPixel(co));
			tco.setT(besttime);
			outtime.set(intime.getPixel(tco));
		}
	}
}
```

> Kernel method of the `MaximiseForTime` filter.


## Queries

A query defines the parameters of each filter and how they relate to each other. A query must define:

 - Zero or more datasets to read input from.
 - One or more datasets to write output to.
 - One or more filters to write to the outputs.

Queries are defined using a memento - this allows the definition to exist without references to open datasets. The structure of a query definition is shown below.

![Class diagram of the query definition classes](images/query-querydef-class.png)

> `QueryDefinition` class diagram.

While a programmatic interface exists, the easiest way to define a query is with XML - the framework will handle de-serialisation.

### Query Example: Maximise for Time

This section presents an example query definition. This follows on from the filter example given in [Multiple Inputs and Outputs](#multiple-inputs-and-outputs) above; see that section for the corresponding filter code in Java.

The goal of this query is to transform a 3D dataset *(time, y, x)* of data into a 2D dataset *(y, x)*. The filter graph is shown below.

![Class diagram of the query definition classes](images/query-example-maximise.png)

> Filter graph for the maximise query. Left: input dataset, featuring one 2D
> and one 1D band. Middle: filter. Right: output dataset, featuring two 2D
> bands. The grey sockets are scalar; the blue sockets could be any type -
> although in this example, all data is scalar.

The query definition will now be presented as XML.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<query xmlns="http://www.vpac.org/namespaces/rsaquery-0.2">
	<input id="infile" href="../input/abstract.nc" />
```

A single tag is all that is required to define the input dataset. It consists of a URI - in this case the file name - and an ID, so it can be referred to later in the document.

Next, the filter is declared to connect the input dataset to the output.

```xml
	<filter id="max" cls="org.vpac.ndg.query.MaximiseForTime">
		<sampler name="toMaximise" ref="#infile/Band1"  />
		<sampler name="toKeep" ref="#infile/Band1"  />
		<sampler name="intime" ref="#infile/time"  />
	</filter>
</query>
```

Here, the filter has been configured to use the `MaximiseForTime` class presented earlier. The class name must be fully-qualified, and the `.class` file must be on the Java classpath at runtime. The input bands are bound to the `PixelSource` fields by name. Note that `Band1` is connected to both `toMaximise` and `toKeep`.

The `ref` attribute of the sampler tag may refer to any pixel source. In this example the samplers are connected to the input dataset; however, they could instead refer to another filter. For example, a second filter could be added to this query that uses the output of `MaximiseForTime` by declaring a sampler with the attribute `ref="#max/output"` - referring to the first filter's ID. The order in which the filters are specified is not important, but there can be no circular dependencies.

```xml
	<output id="outfile" >
		<grid ref="#infile" />
		<variable name="Band1" ref="#max/output" />
		<variable name="time" ref="#max/outtime" />
	</output>
```

The output dataset inherits its grid (and coordinate system) from the input dataset. Variables are defined using child tags, and connected to the filter using the `ref` attribute. There is no need to declare dimensions; they will be determined automatically by the query engine. In this case, because of the reduction filter, `Band1` will be demoted from 3D to 2D, and `time` will be promoted from 1D to 2D. Coordinate axes `x` and `y` will be created to match the dimensions.

