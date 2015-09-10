#!/bin/bash
now=$(date +"%F-%T")
./createModel.sh > w2vFromWiki-$now.log 2>&1 &
