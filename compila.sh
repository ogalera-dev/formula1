#!/bin/bash

rm -rf compilat/
rm -rf target/
mvn clean compile assembly:single

mkdir compilat
cp target/*.jar compilat/compilat.jar
rm -rf target/

cp -r dades/ compilat/

echo "OK, els resultats estàn a compilat/"