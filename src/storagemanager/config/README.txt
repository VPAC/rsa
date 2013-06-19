This directory contains configuration files that the user is expected to modify.
These files must be on the classpath when the RSA is started. E.g.

    java -classpath "config:lib/StorageManager.jar:/usr/local/lib/gdal.jar" ...

rsa.xml
    The core configuration file of the RSA. Includes settings for the National
    Nested Grid, storage locations, etc.

datasource.xml.SAMPLE
    Connection parameters for the database where metadata is stored. This must
    be renamed to datasource.xml before the RSA will work; this is to allow the
    connection parameters to be changed locally without interfering with the
    version-controlled file.

logback.xml
    Logging parameters, i.e. which messages to report and where to write them
    to.

blank_seed.nc:
    Used to generate blank tiles when creating bands. You should not need to
    modify this file.
