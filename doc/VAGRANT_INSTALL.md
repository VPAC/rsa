# Building rsa and its environment using vagrant:

 1. Install vagrant using the following [instructions](http://docs.vagrantup.com/v2/installation/index.html).
    
 2. Get `rsabuild` vagrant and puppet scripts from GitHub repository:
 
 		$ git clone git@code.vpac.org:rsabuild/rsabuild.git

 3. Go into `rsabuild` directory:
 
 		$ cd rsabuild
 		
 4. Load up a VM using Vagrant by running:
 
		vagrant up
		

After running the above command, you'll will have a fully running Centos VM with `rsa` installed.
    
**Note:** User could easily configure vagrant to load up other type of VM such as Ubuntu with minimum changes to the scripts in rsabuild project.

### Commonly used vagrant commands:

	$ vagrant ssh		# SSH into the VM.

	$ vagrant up		# Install all dependencies and load the VM. 

	$ vagrant provision	# Apply changes to the VM.

	$ vagrant reload	# Reload the VM.

	$ vagrant destroy	# Destroy the VM.

	$ vagrant init centos-63-x64 http://vpac.org/pub/centos-63-x64.box # Create the VM from the specified box


### How do I commit changes to this rsabuild project?

To contribute, file bug reports or issues, please visit `rsabuild` [GitHub repository](https://github.com/VPAC/rsabuild).

