#!/usr/bin/env bash

#
# Starts a basic file-based web server for the web content directory.
#

EXECUTABLE=$0
if [ -L $0 ]; then
    # Dereference symbolic link so that libraries can be found. This is in case
    # the user has a link to this script from somewhere like ~/bin/rsa
    EXECUTABLE=$(readlink -f ${0})
fi
ROOTDIR=$(dirname ${EXECUTABLE})

cd $ROOTDIR/../src/main/webapp/app/ && python -m SimpleHTTPServer

