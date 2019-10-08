#!/bin/sh

cd ..
echo "Starting to generate javadoc.."

javadoc -protected -splitindex -d JavaDoc -sourcepath KahIT/app/src/main/java -subpackages com.god.kahit -Xdoclint:none >/dev/null 2>&1 # Suppresses normal logs and error log 

echo "JavaDoc completed, forcing exit with 0"
exit 0

# pause