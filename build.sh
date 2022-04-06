#!/usr/bin/env bash
set -euo pipefail

git submodule update --init

cd ./work/Determiner
mvn clean install
cd ../../

./gradlew applyPatches

cd ./patched-spigot-fernflower
mvn clean package
cp ./target/fernflower-*.jar ../fernflower.jar
