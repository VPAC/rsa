# Deploying an RSA spatialcubeservice on Tomcat

This guide is for setting up an RSA spatialcubeservice in a server environment.
The specific instructions are customised to a Debian-based Linux distribution.

**Important:**
	If you intend to use this machine in production, or deploy in a cloud, then 
	of course the default password listed below needs to be updated. 

**Note:**
	This guide assumes that the required RSA components and dependencies have been built and 	installed successfully according to installation instructions.
  
## Configure Tomcat Webserver

See: https://help.ubuntu.com/10.04/serverguide/tomcat.html

 1. Uncomment the *AJP 8009* port in */etc/tomcat6/server.xml*
 2. Modify */var/lib/tomcat6/conf/tomcat-users.xml* with a web-manager
    account by adding at the end before the final *</tomcat-users>*
    tag:
	    
	```    
	<role rolename="manager-gui"/>
	<role username="manager" password="password" roles="manager-gui"/>
	```

 3. Change permissions of /etc/tomcat6 dir to allow webapp functions
    needed permissions:
    
	```
     sudo chgrp -R tomcat6 /etc/tomcat6
     sudo chmod -R g+w /etc/tomcat6 
     sudo chgrp -R tomcat6 /usr/share/tomcat6
     sudo chmod -R g+w /usr/share/tomcat6 
	```
 4. The *spatialcubeservice* webapp to be deployed using Tomcat includes GDAL Java bindings
    that require access to the main compiled C++ GDAL libraries on the
    system.  Thus, it is required to provide the Tomcat
    on startup with a modified *LD_LIBRARY_PATH*. So edit
    the */usr/share/tomcat6/bin/setenv.sh* file and add as the last line:
    
    ```
    export LD_LIBRARY_PATH=/usr/local/lib:$LD_LIBRARY_PATH
	export CLASSPATH=/usr/local/lib/gdal.jar
	```
**Note:** Assuming you installed the GDAL libraries to */usr/local/lib*.	

## Restart Tomcat

Restart with:

     sudo /etc/init.d/tomcat6 restart

Then you should be able to browse to the machine's IP address and login via the manager
application, e.g. http://localhost:8080/manager/html, and login.

If not, double-check your *tomcat-users.xml* file, and look at the log
message output in */var/log/tomcat6*. If you are not using the default
system install of Tomcat, the log files may be in *<Tomcat install
directory>/logs/catalina.out*.

Tomcat should be started automatically whenever the machine restarts, including the deployed webapp.

## Obtain and deploy the RSA spatialcubeservice webapp

While it's possible to build the RSA *spatialcubeservice* webapp directly on your
new RSA node, the easiest option is to deploy pre-build spatialcubeservice WAR file.


 1. Configure the data directories to be writable by Tomcat:
 
	```
      sudo chown -R tomcat6:tomcat6 /var/lib/ndg/storagepool
      sudo chown -R tomcat6:tomcat6 /var/spool/ndg/upload
      sudo chown -R tomcat6:tomcat6 /var/spool/ndg/pickup
      sudo chown -R tomcat6:tomcat6 /var/tmp/ndg
	```
	**Note:** Assuming *tomcat6* is the Tomcat user.

 2. Deploy WAR file onto the server (e.g. either by copying them into
    the appropriate */var/lib/tomcat6/webapps* folder with *scp*, or
    using Tomcat's web admin interface)
 3. Update the *datasource.xml* file in *spatialcubeservice* to make sure
    database configuration is correct.
 4. Browse to the Tomcat manager page, and double-check both are
    started correctly.

Once deployed, the main URL will look something like as follows:

  http://localhost:8080/spatialcubeservice/Dataset.xml

## Configure the RSA spatialcubeservice webapp

Update configuration options in RSA spatialcubeservice instance by:

 1. Browse to the deployed *spatialcubeservice* app. The first time you deploy the webapp, 
    it will initialise necessary tables.
 2. Update *rsa.xml* if need to update configuration options (They should
    already be set to sensible defaults).
 2. Update *datasource.xml* if need to update database credential (They should
    already be set to sensible defaults).


