# Manual Install

Contents:

1. [Dependencies](#1-dependencies) - What are the RSA's requirements?
2. [Configure Database](#2-configure-database) - How to set up and configure RSA's database
3. [Configure Filesystem](#3-configure-filesystem) - Creating directories, setting permissions
4. [Build RSA](#4-build-rsa-from-source) - Building RSA from source

## 1. Dependencies

The RSA requires: 

| Software   | Version      | Notes |
|:-----------|:-------------|:-------------|
| GDAL       | 1.9.2+ (svn) | [GDAL][1] - used for geospatial data transformation. Latest svn checkout is recommended due to improvements with its netCDF driver. |
| netCDF     | 4.3.0+       | [netCDF][2] - high-performance array-based data processing (including rasters). |
| HDF5       | 1.8.6+       | [HDF5][3] - advanced, scalable storage backend used by netCDF. |
| zlib       | 1.2.5+       | Compression library used by HDF5. |
| swig       | 2.0+         | Its compiler allows creation of Java bindings to GDAL. |
| Java 7 JRE | 7 (1.7)      | [OpenJDK][4] is suitable. If you also wish to compile/develop, you'll need a Java 7 JDK as well.|
| PostgreSQL | 8.4+         | [PostgreSQL][5] - deployed as core database backend. |

[1]: 	http://www.gdal.org/
[2]: 	http://www.unidata.ucar.edu/software/netcdf/
[3]:	http://www.hdfgroup.org/HDF5/
[4]:	http://openjdk.java.net/
[5]:	http://www.postgresql.org/
[6]:	http://tomcat.apache.org/ 

Additional web server dependencies required for `spatialcubeservice` war deployment:

| Software   | Version | Notes |
|:-----------|:--------|:-------------|
|Tomcat      | 6.0.x   | An [Apache Tomcat][6] server 6 stack| 

Several other packages relied upon by the RSA application, such as the
Hibernate ORM framework, are distributed as JAR archives as part of the RSA itself.

This document doesn't include advice on Tomcat installation and configuration,
as this differs considerably for development versus server setups. Developers
may wish to run the web services through a Tomcat instance managed by Eclipse.
For server deployment, see [deploy.md](deploy.md).

### Installing Dependencies (on Debian/Ubuntu or Centos/RedHat)

#### Installing PostgreSQL

Debian/Ubuntu 11.10:

	$ sudo apt-get install postgresql-8.4

Debian/Ubuntu 12.04:

	$ sudo apt-get install postgresql-9.1

Centos/RedHat:

	$ sudo yum install postgresql
	$ sudo service postgresql initdb
	$ sudo service postgresql start

#### Installing a JDK

Debian/Ubuntu 12.04:

	$ sudo apt-get install openjdk-7-jdk

Centos/RedHat:

	$ sudo yum install java-1.7.0-openjdk
	
**Note:** The OpenJDK distribution has been tested to work successfully
as well as Sun's Java.	

#### Installing zlib (required for netCDF)   

	$ ./configure
	$ make
	$ sudo make install
 	   
#### Installing HDF5 (required for netCDF)
		
	$ ./configure --prefix=/usr/local --with-zlib=/usr/local
	$ make
	$ sudo make install

#### Installing netCDF

	$ CPPFLAGS=-I/usr/local/include LDFLAGS=-L/usr/local/lib ./configure
	$ make
	$ sudo make install
       
#### Installing GDAL and required drivers

##### Installing build dependencies

Compiling GDAL requires a general C build environment, SWIG bindings
library, as well as ant to build the Java bindings.

Debian/Ubuntu:

	$ sudo apt-get install build-essential ant swig

Centos/RedHat:

	$ sudo yum groupinstall "Development Tools"
	$ sudo yum install swig ant

##### Installing driver dependencies

Core reprojection capabilities of gdal rely on the Proj4 project.

Debian/Ubuntu:

	$ sudo apt-get install libproj0

Centos/RedHat:

	$ sudo yum install proj-devel

On Centos, the public key for these packages may not be
installed. The error can be ignored by passing `--nogpgcheck` to
yum, however this constitutes a security risk, and should be
cleared with the system admin team first.

As specified above, we also require NetCDF and HDF5 software to be
installed to use the NetCDF driver via GDAL.

##### Installing GDAL from source

As listed above, we currently require a 'trunk' version of GDAL to be checked out and
installed (recommended version 1.9.2+), as it contains recent improvements to the GDAL netCDF capabilities.
 
If you don't yet have subversion installed yet:

Debian/Ubuntu:

	$ sudo apt-get install subversion

Centos/RedHat:

	$ sudo yum install subversion

Check out the latest svn source of GDAL:

	$ svn checkout https://svn.osgeo.org/gdal/trunk  gdal-trunk

An alternative to an svn checkout would be to get a [nightly build][gdn]. Then configure:

	$ cd gdal-trunk/gdal
	$ ./configure --with-netcdf=/usr/local --with-hdf5=/usr/local
  
**Important:** At the end of the configure, it's important that in the status
  report printed, HDF5 and netCDF support are both listed as 'yes'.

To be doubly sure, open the config.log file:

	$ less config.log

... and check that the `NETCDF_HAS_NC4` flag is set to 'yes'.

You can specify different prefix paths than the one suggested. The
default would be `/usr/local`.

Before compiling and installing GDAL on Centos/RedHat, edit GDALmake.opt (after ./configure stage), 
and manually add -lstdc++ on the "LIBS = xxxx" line as it is required by the compilation
This step shouldn't be necessary on Debian/Ubuntu.

Compile and install:

	$ make
	$ sudo make install

If you want to run tests of GDAL on the command-line, you can now
update your system path etc to be able to find this version of GDAL,
e.g. via creating an updatePath.sh script to source containing:

	GDAL_DIR=/usr/local
	export PATH=$GDAL_DIR/bin:$PATH
	export LD_LIBRARY_PATH=$GDAL_DIR/lib:$LD_LIBRARY_PATH

That script could be configured to run from your login script
(e.g. *~/.bashrc*).

[gdn]: http://trac.osgeo.org/gdal/wiki/DownloadSource

##### Installing GDAL Java libraries

The GDAL Java bindings aren't installed automatically as part of make
install. To set them up manually, follow the instructions below.

First:

 * edit the file *swig/java/java.opt* (from your GDAL source directory)
 * change the JAVA_HOME variable listed to suit your install.

   Debian/Ubuntu:

    JAVA_HOME=/usr/lib/jvm/java-7-openjdk

   Centos/RedHat (on NCI):

    JAVA_HOME=/usr/java/latest

And just for good measure, set your `JAVA_HOME` environment variable (on the
command line).

Debian/Ubuntu:

	$ export JAVA_HOME=/usr/lib/jvm/java-7-openjdk

Centos/RedHat (on NCI):

	$ export JAVA_HOME=/usr/java/latest

Then go to your GDAL source root dir, and run the build:

	$ cd swig/java
	$ make
	$ sudo cp *.jar *.so /usr/local/lib/
	$ pushd /usr/loca/lib
	$ sudo ln -s libgdalconstjni.so libgdalconstjni.so.1
	$ sudo ln -s libgdaljni.so libgdaljni.so.1	   
	$ sudo ln -s libogrjni.so libogrjni.so.1
	$ sudo ln -s libosrjni.so libosrjni.so.1
	$ popd

If `make` fails, double-check that the Java JDK (not JRE) is installed.

To run GDAL via Java bindings and Apache, you may need to create a symlink to libproj:

	$ pushd /usr/lib
	$ sudo ln -s libproj.so.0 libproj.so

You should also do a dynamic library update to be safe:

	$ sudo ldconfig

## 2. Set Up Database

### Configuring PostgreSQL

The RSA connects to PostgreSQL databases via JDBC on the localhost using
passwords (MD5). 

By default the RSA accesses the database on port 5432 (though this can
be configured in the web application's datasource.xml). You should
double-check these values.

Debian/Ubuntu 10.04:

	vi /etc/postgresql/8.4/main/postgresql.conf

Debian/Ubuntu 12.04:

	vi /etc/postgresql/9.1/main/postgresql.conf

Centos/RedHat:

	vi /var/lib/pgsql/data/postgresql.conf

Navigate to the `port =` line, and make sure it is `5432` if you wish to
leave RSA's configuration as default.

The Ubuntu Server Postgres documentation suggests removing
the '#' at the start of the line `listen_addresses = 'localhost'`
to enable TCP/IP access, but this hasn't proved necessary for jdbc
access in the RSA so far.

### Setting up new user roles and empty database

Choose a database name, and user/password for use throughout the
RSA. We then need to:

 * Create a new postgres user;
 * Create an empty database owned by the user and specify appropriate permissions;
 * (later) Save the appropriate configurations to the *rsacli* and *spatialcubeservice* *datasource.xml* file.

The RSA will automatically initialise the database with required tables 
when it's first deployed.

The default connection role the RSA will use is:

 * Username: *ula*
 * Password: *password*
 * Database: *uladb*

If you want to choose a different name and credential, you
will need to modify the *rsacli* and *spatialcubeservice* *datasource.xml* file.

### Creating database (from the command line)

To create RSA's database from the command-line, run the command below -
substituting values for RSA database name and accessing user as appropriate:

	$ sudo -u postgres createuser -D -A -P ula
	$ sudo -u postgres createdb -O ula uladb

You'll be prompted to enter a password for the new user after the first command. When asked *Shall the new role be allowed to create more new roles?*, answer *n*.

**Important:** Remember to record the values chosen for database name and user, as you'll have to configure the RSA's connection to them via the *datasource.xml*.

It is possible to set up the database graphically using [pgAdmin][pga] - but you may need to change the database access rules in *postgresql.conf* to allow it to connect.

[pga]: http://www.pgadmin.org/

## 3. Setup Filesystem

The RSA requires four directories on the filesystem:

 * *upload*: Stores uploaded data before it is tiled and moved to the storage pool.
 * *tmp*: Stores temporary raster processing files in intermediate state.
 * *pickup*: Processed files are kept here, to be downloaded later by a user.
 * *storagepool*: Persistent storage for rsa dataset. A design intention is that
    actual storagepool directories here may be NFS mounts to other
    file-systems.

###Creating directories

Create directories the RSA to write to:

	$ sudo mkdir -p /var/lib/ndg/storagepool
	$ sudo mkdir -p /var/spool/ndg/upload
	$ sudo mkdir -p /var/spool/ndg/pickup
	$ sudo mkdir -p /var/tmp/ndg

These directories should be configured to be writable by the Tomcat
user. For a development system, it would be your own user
name.

At this stage, for a production server you may wish to mount NFS locations
within the storagepool directory if you intend to store large datasets there.

## 4. Build RSA from source

To build RSA from source using [ant](http://ant.apache.org/):

	$ git clone https://github.com/VPAC/rsa.git
	$ cd rsa/src
	$ ant

Once the build process completes, user can find all RSA components inside dist directory.
	
Optionally, user can build each RSA components separately, if they prefer to do so.

Building storagemanager individually:

	$ cd rsa/src/storagemanager
	$ ant

Building rsaquery individually:

	$ cd rsa/src/rsaquery
	$ ant

Building cmdclient individually:

	$ cd rsa/src/cmdclient
	$ ant

Building spatialcubeservice individually:

	$ cd rsa/src/spatialcubeservice
	$ ant

