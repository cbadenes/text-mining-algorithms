#!/bin/bash
export MAVEN_OPTS="-Xmx112g"
now=$(date+"%Y%m%d-%H%M%S")
mvn exec:java -Dexec.mainClass="w2v.ModelBuilder" -Dexec.args="8 11g /home/cbadenes/test/wikipedia-dump/articles_body.csv text/model/w2v-wiki" -Dexec.classpathScope="test" > log-$now 2>&1 &
