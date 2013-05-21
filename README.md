#The Raster Storage Archive

##What is it
The Raster Storage Archive (RSA) is a platform built to store and distribute large geospatial raster datasets.  Datasets can have multiple bands and a temporal component.

Datasets are imported into the RSA via the command line interface or web-services. RSA will process these uploaded datasets into manageable sized files sharing a common projection and alignment. It will also handle the merging process with existing geospatial data. RSA simply manages the uploaded data; third party tools can always access the stored geospatial from RSAs hierarchical file system and read meta-data from the database directly.

One of the fundamental concepts behind the RSA is the *Data Cube*. The RSA can export a *Data Cube' given appropriate spatial and temporal extents.

The RSA provides an easily extensible method to perform complex spatial-temporal queries through the query engine.

##Dependencies
- The Geospatial Data Abstraction Layer (GDAL), as used for geospatial data transformations ([www.gdal.org](www.gdal.org))
- PostgreSQL, to maintain meta-data and geospatial tile relationships ([www.postgresql.org](www.postgresql.org))

##Licensing
The RSA is licensed under the GPL v3, please see [LICENSE.txt](LICENSE.txt).

##Acknowledgements
The RSA was developed under the Unlocking the Landsat Archive (ULA) project as funded by the Australian Space Research Program (ASRP).

##Copyright
2013 Cooperative Research Centre for Spatial Information (CRCSI)