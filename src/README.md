## Project Structure

The Raster Storage Archive is comprised of the following modules. For more
detailed documentation, refer to the [../doc](../doc) directory.

### Storage Manager

The [Storage Manager](storagemanager) is the heart of the RSA. It can import
spatial data into the storage pool, performing the require transforms along the
way (such as reprojection and splitting into tiles). It also provides access to
the data, either by exporting chunks of the data cube or by direct views of the
live data.

The Storage Manager only provides access via its API. For user interfaces, see
the Command Line Client and Spatial Cube Service modules.

### Query Engine

[Rsaquery](rsaquery) is the query engine of the RSA. It is essentially an image
processing system. It can operate on data cubes with multiple dimensions and
multiple bands, such as Landsat data. Image processing pipelines can be created
in XML, and new filters can be written in Java - allowing algorithms to be
encapsulated. Rsaquery can operate directly on images in the RSA via the Storage
Manager's API. However it also functions as a standalone library for processing
NetCDF files.

### Command Line Client

The [command line client](cmdclient) provides a convenient way to interact with
the RSA from a text shell. Data can be imported, exported, and queried using
rsaquery. This makes it ideal for administrative tasks, or for processing data
on a high-performance computer (HPC).

### Web Services

The [Spatial Cube Service](spatialcubeservice) provides web services to interact
with the RSA. Its functionality is similar to that of the command line client.
It also has a Web-based user interface for querying data.

