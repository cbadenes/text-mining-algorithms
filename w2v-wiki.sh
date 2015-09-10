#!/bin/bash
export MAVEN_OPTS="-Xmx64g"
mvn exec:java -Dexec.mainClass="es.upm.oeg.lab.tma.Word2VecFromWikipedia" -Dexec.classpathScope="test"
