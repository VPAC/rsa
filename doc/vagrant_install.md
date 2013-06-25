# Building RSA and its environment using Vagrant:

 1. Install vagrant as shown [here](http://docs.vagrantup.com/v2/installation/index.html) or using the following instructions.
 
	Debian/Ubuntu:
	
	```
	$ sudo apt-get install virtualbox
	$ sudo apt-get install vagrant
	```
 	
 2. Get rsabuild vagrant and puppet scripts from GitHub repository:

		$ git clone git@github.com:VPAC/rsabuild.git
		$ cd rsabuild					# Go into rsabuild directory
	
 3. Load up a guest VM using Vagrant by running:
		
		$ vagrant up
	

	Once the above command finished running, you'll will have a fully running Centos 6.3 VM with RSA installed.
	
	Once you ssh into the guest VM, you should see this usage below:

	```
	$ vagrant ssh
	
	Welcome to your newly built CentOS 6.3 guest VM with RSA installed!
                 Produced by VPAC

	To get started with rsacli:
		$ rsa -h					# see a complete list of usage
		$ rsa dataset list			# list all available datasets

	To get started with spatialcubeservice:
		$ curl http://localhost:8080/spatialcubeservice/Dataset.xml

    ...
	```
	As port `8080` is being forwarded to host port `8181`, you can access below URL from host machine directly using port 8181:
	
		http://localhost:8181/spatialcubeservice/Dataset.xml
		
	To get started with query from host machine:
	
		http://localhost:8181/spatialcubeservice/app/index.html

### Commonly used vagrant commands:

	$ vagrant ssh		# SSH into the guest VM.

	$ vagrant up		# Install all dependencies and load the guest VM.

	$ vagrant provision	# Apply changes to the guest VM.

	$ vagrant reload	# Reload the guest VM.

	$ vagrant destroy	# Destroy the guest VM.

	$ vagrant suspend	# Suspend the guest VM rather than fully destroying it.

	$ vagrant resume	# Resume a Vagrant managed VM that was previously suspended.

	$ vagrant status	# Show the state of the VMs Vagrant is managing.

	$ vagrant init centos-63-x64 http://vpac.org/pub/centos-63-x64.box # Create the VM from the specified box

### How do I commit changes to this rsabuild project?

To contribute, file bug reports or issues, please visit [rsabuild github repository](http://github.com/VPAC/rsabuild).

