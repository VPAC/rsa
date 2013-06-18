This is the Raster Storage Archive, a system for storing geospatial raster data.

'rsa' is a script that eases launching of the command line client.
Usage:
    rsa -[ckdh] [--restart]
    rsa -[1] <command> <action> [options] <...>
Options:
    -c --compile-filters Compile the filters (i.e. filter-src/**/*.java)
    -d --start      Stop a daemon to speed up commands.
    -k --stop       Stop the daemon.
    --restart       Restart the daemon.
    -1 --no-daemon  Run in a new Java process, and exit when done.
    -h --help       Display help for the internal commands and actions.

Note that by default a daemon will be started the first time the client is run.
To prevent that from happening, pass in --no-daemon as the first argument.

Example commands:

    rsa dataset list
        List datasets in the RSA (output is paged).

    rsa data query add_binary.xml --output sum.nc
        Run a query as defined in 'add_binary.xml', and store the result in
        sum.nc.

    rsa data task --status FINISHED
        Show tasks that have finished.

For more details, run `rsa -h`.

The query engine uses filters that are in the filter/ directory. To define a new
filter:
 - Create a new class that implements the org.vpac.ndg.query.Filter interface.
 - Place the .java file in filter-src.
 - Run `rsa --compile-filters`.
The filter can then be referred to by name in a query definition.

Bash completion is available for some of rsa's commands. To enable it, run

    . _rsacli_bash_completion.sh

