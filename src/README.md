## Project Structure

The Raster Storage Archive is comprised of the following modules. For more
detailed documentation, refer to the [../doc](../doc) directory.

### Storage Manager

The [Storage Manager](storagemanager) is the heart of the RSA. It can import
spatial data into the storage pool, performing the required transforms along the
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

## Building

To compile the RSA, simply run `ant` from this directory. Build artefacts will
be placed in the `dist` directory; these can be deployed to run the RSA.

The command line client can be run without deploying. The build process creates
a portable install in `cmdclient/dist/rsa`. To use it:

 1. Set up the database, dependencies and storage pool as described in the [main
    project documentation][1].
 2. Build the RSA as described above, or run `ant` from the
    [cmdclient](cmdclient) directory if you only need the command line client.
 3. Configure it by editing the files in `cmdclient/dist/rsa/config`. At a
    minimum, `datasource.xml.SAMPLE` will need to be renamed to `datasource.xml`.
 4. Execute the `cmdclient/dist/rsa/rsa` script. Try `cmdclient/dist/rsa/rsa -h`.

[1]: ../doc/manual_install.md

