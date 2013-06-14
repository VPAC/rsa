#Manual Install

This section describe step by step instructions on:

* Installing Core Requirements
* Configuring database access
* Configuring the filesystem
* Building `rsa` from source


##1. Core Requirements:

The `rsa` requires: 

| Software   | Version      | Notes |
|:-----------|:-------------|:-------------|
| GDAL       | 1.9.2+ (svn) | [GDAL](http://www.gdal.org) is used for geospatial data transformation. Latest svn checkout is recommended due to improvements with its netCDF driver. |
| netCDF     | 4.2.1+       | [netCDF](http://www.unidata.ucar.edu/software/netcdf/) is used for high-performance array-based data processing (including rasters). |
| HDF5       | 1.8.9+       | Hierarchical Data Format library. Advanced and scalable storage backend used by netCDF. |
| zlib       | 1.2.5+       | Compression library used by HDF5. |
| swig       | 2.0+         | Its compiler allows creation of Java bindings to GDAL. |
| Java 7 JRE | 7 (1.7)      | [OpenJDK](http://openjdk.java.net/) is suitable. If you also wish to compile/develop, you'll need a Java 7 JDK as well.|
| PostgreSQL | 8.4+         | [PostgreSQL](http://www.postgresql.org/) deployed as core database backend. |

Additional web server dependencies required for `spatialcubeservice` war deployment:

| Software   | Version | Notes |
|:-----------|:--------|:-------------|
|Tomcat      | 6.0.x   | An [Apache Tomcat](http://tomcat.apache.org/) server 6 stack| 

Several other packages relied upon by the `rsa` application, such as the
Hibernate ORM framework, are distributed as JAR archives as part of the `rsa` itself.

**Note:** This document doesn't include advice on the Tomcat
   installation and configuration, as this differs considerably for
   Development versus Server setups.


Details for installing GDAL and the dependencies listed above are
available in the section `Installing latest GDAL and required
drivers`.

### Installing Core Dependencies (on Debian/Ubuntu or Centos/RedHat)

**Important:** If you are connected to AARNET, read the section
   `AARNET downloads of Ubuntu packages` before installing packages.

#### Installing a JDK

Debian/Ubuntu 12.04:

	$ sudo apt-get install openjdk-7-jdk

Centos/RedHat:

	$ sudo yum install java-1.7.0-openjdk
	
**Note:** The OpenJDK distribution has been tested to work successfully
as well as Sun's Java.	

#### Installing PostgreSQL

Debian/Ubuntu 11.10:

	$ sudo apt-get install postgresql-8.4

Debian/Ubuntu 12.04:

	$ sudo apt-get install postgresql-9.1

Centos/RedHat:

	$ sudo yum install postgresql
	$ sudo service postgresql initdb
	$ sudo service postgresql start

#### Installing latest GDAL and required drivers

##### Installing build dependencies

Compiling GDAL requires a general C build environment, SWIG bindings
library, as well as ant to build the Java bindings.

Debian/Ubuntu:

	$ sudo apt-get install build-essential ant swig

Centos/RedHat:

	$ sudo yum groupinstall "Development Tools"
	$ sudo yum install swig ant

**Note:** *ant* may depend on a old version of Java; in this case, it will
install the other version, but should not replace Java 1.7.

##### Installing driver dependencies

Core reprojection capabilities of gdal rely on the Proj4 project.
As specified above, we also require netCDF and HDF5 software to be
installed to use the netCDF driver via GDAL.  You are free to install other GDAL driver
software for image conversion, this is the core list needed currently
though.

Debian/Ubuntu:

	$ sudo apt-get install libproj0

Centos/RedHat:

	$ sudo yum install proj-devel

**Note:** On Centos, the public key for these packages may not be
   installed. The error can be ignored by passing *--nogpgcheck* to
   yum, however this constitutes a security risk, and should be
   cleared with the system admin team first.

The version of NetCDF installed needs to be 4.2.1 or above.
Build with configure flags similar to:

	PREFIX=/usr/local
	LDFLAGS=-L$PREFIX/lib CPPFLAGS=-I$PREFIX/include ./configure --enable-netcdf4 --prefix=$PREFIX

LDFLAGS and CPPFLAGS are necessary when using a custom build of libhdf5.

##### Installing GDAL Development version

As listed above, we currently require a 'trunk' version of GDAL to be checked out and
installed (recommended version 1.9.2+), as it contains recent improvements to the GDAL netCDF capabilities.

Included below are instructions for a manual build of GDAL from source.
 
If you don't yet have subversion installed yet:

Debian/Ubuntu:

	$ sudo apt-get install subversion

Centos/RedHat:

	$ sudo yum install subversion

Make a directory and check out the latest svn source of GDAL::

	$ mkdir ~/local
	$ cd ~/local
	$ svn checkout https://svn.osgeo.org/gdal/trunk  gdal-trunk

An alternative to an svn checkout would be to get a nightly build from Nov 2011 onwards from [here](http://trac.osgeo.org/gdal/wiki/DownloadSource).

Then configure gdal:

	$ cd gdal-trunk/gdal
	$ ./configure --prefix=/usr/local --with-java --enable-netcdf4
  
If need to specify netcdf or hdf5 installation:

	$ ./configure --prefix=/usr/local --enable-netcdf4

**Important:** At the end of the configure, it's important that in the status
  report printed, HDF5 and netCDF support are both listed as 'yes'.

To be doubly sure, open the config.log file:

	$ less config.log
and check the NETCDF_HAS_NC4 flag is set to 'yes'.

You can specify different prefix paths than the one suggested. The
default would be */usr/local*.

Note: Before compiling and installing GDAL on Centos/RedHat, edit GDALmake.opt (after ./configure stage), 
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

That script should be configured to run from your login script
(e.g. *~/.bashrc*). You can also perform some GDAL tests via the
'autotest' directory (optional).

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

And just for good measure, set your JAVA_HOME environment variable (on the
command line).

Debian/Ubuntu:

	$ export JAVA_HOME=/usr/lib/jvm/java-7-openjdk

Centos/RedHat (on NCI):

	$ export JAVA_HOME=/usr/java/latest

Then go to your GDAL source root dir, and run the build::

	$ cd swig/java
	$ make
	$ sudo cp *.jar *.so /usr/local/lib/
	$ pushd /usr/loca/lib
	$ sudo ln -s libgdalconstjni.so libgdalconstjni.so.1
	$ sudo ln -s libgdaljni.so libgdaljni.so.1	   
	$ sudo ln -s libogrjni.so libogrjni.so.1
	$ sudo ln -s libosrjni.so libosrjni.so.1
	$ popd

**Note:** If this fails, double-check that the Java JDK (not JRE) is
   installed.

To run GDAL via Java bindings and Apache, you may need to create a symlink to libproj:

	$ pushd /usr/lib
	$ sudo ln -s libproj.so.0 libproj.so

You should also do a dynamic library update to be safe:

	$ sudo ldconfig

## 2. Configuring database access

### Access protocol

The `rsa` connects to PostgreSQL databases via JDBC on the localhost using
passwords (MD5).
No external access is required, though as a user you way wish to enable
remote administration. 

By default the `rsa` accesses the database on port 5432 (though this can
be configured in the web application's datasource.xml). You should
double-check these values.

Debian/Ubuntu 10.04:

	vi /etc/postgresql/8.4/main/postgresql.conf

Debian/Ubuntu 12.04:

	vi /etc/postgresql/9.1/main/postgresql.conf

Centos/RedHat:

	vi /var/lib/pgsql/data/postgresql.conf

Navigate to the 'port =' line, and make sure it is 5432 if you wish to
leave `rsa`'s as default.

**Note:** The Ubuntu Server Postgres documentation suggests removing
   the '#' at the start of the line *listen_addresses = 'localhost'*
   to enable TCP/IP access, but this hasn't proved necessary for jdbc
   access in the `rsa` thus far.

See also: [https://help.ubuntu.com/11.10/serverguide/C/postgresql.html](https://help.ubuntu.com/11.10/serverguide/C/postgresql.html)

###Setting up new user roles and empty database

Choose a database name, and user/password for use throughout the
`rsa`. We then need to:

 * Create a new postgres user;
 * Create an empty database owned by the user and specify appropriate permissions;
 * (later) Save the appropriate configurations to the `rsacli` and `spatialcubeservice` datasource.xml file.

The `rsa` will automatically initialise the database with required tables 
when it's first deployed.

The default connection role the `rsa` will use is:

 * Username: ula
 * Password: password
 * Database: uladb 

**Note:** However, if you want to choose a different name and credential - you
will just need to modify the `rsacli` and `spatialcubeservice`'s datasource.xml file.

###Creating database (from the command line)

To create `rsa` database from the command-line, run a command such as that below -
substituting values for `rsa` database name and accessing user as appropriate:

	$ sudo -u postgres createuser -D -A -P ula
	$ sudo -u postgres createdb -O ula uladb

You'll be prompted to enter a password for the new user after the first command. When asked *Shall the new role be allowed to create more new
roles?*, answer *n*.

**Important:** Remember to record the values chosen for database name and user, as you'll have to configure the `rsa`'s connection to them via the datasource.xml file in `rsacli` and `spatialcubeservice` later.

**Note:** It is possible to set up the database graphically using [pgAdmin](http://www.pgadmin.org/) - but you may need to change the database access rules in *postgresql.conf* to allow it to connect.

## 3. Configuring the filesystem

The `rsa` requires four directories on the filesystem:

	upload
   		Stores uploaded data before it is tiled and moved to the storage pool.

	tmp
   		Stores temporary raster processing files in intermediate state.

	pickup
		Processed files are kept here, to be downloaded later by a user.
		
	storagepool
   		Persistent storage for rsa dataset. A design intention is that
   		actual storagepool directories here may be NFS mounts to other
   		file-systems.

###Creating directories

Create directories the `rsa` to write to:

	$ sudo mkdir -p /var/lib/ndg/storagepool
	$ sudo mkdir -p /var/spool/ndg/upload
	$ sudo mkdir -p /var/spool/ndg/pickup
	$ sudo mkdir -p /var/tmp/ndg

These directories should be configured to be writable by the Tomcat
user. When configuring for a server, this would be the user
*tomcat*. For a development system, it would be your own user
name.

At this stage, for a production server you may wish to mount NFS locations
within the storagepool directory if you intend to store large contiguous
datasets there.

## 4. Building rsa from source

To build `rsa` from source using [`ant`](http://ant.apache.org/):



