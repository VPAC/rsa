The command line client provides a convenient way to interact with
the RSA from a text shell. Data can be imported, exported, and queried using
rsaquery. This makes it ideal for administrative tasks, or for processing data
on a high-performance computer (HPC).

The most common commands are:

    $ rsa -h 									# see a complete list of usage

    $ rsa dataset list 							# list all available datasets
    
    $ rsa dataset show [IDENTIFIER] 			# show all details on the dataset

    $ rsa dataset create <NAME> <RESOLUTION> 	# create a new dataset as specified

    $ rsa band list <DATASET_ID> 				# list all the bands on the dataset

    $ rsa band create <DATASET_ID> <BAND_NAME>	# create a new band for the dataset

    $ rsa timeslice list <DATASET_ID> 			# list all the timeslices on the dataset

    $ rsa timeslice create <DATASET_ID> <DATE>	# create a new timeslice for the dataset
    
    $ rsa data import <TIMESLICE_ID> <BAND_ID> <FILE> # import data into the timeslice & band
    
    $ rsa data export <DATASET_ID>				# export the dataset

    $ rsa data query <QUERY_DEF_FILE>			# run a query as defined in QUERY_DEF_FILE

## Command completion

Bash completion is available for some RSA commands - including completion of metadata such as dataset names. To enable it, run:

    $ . _rsacli_bash_completion.sh
    
from the rsacli installation directory. This runs the RSA to query the database, therefore it is best to use this feature in conjunction with Nailgun to ensure responsiveness.

