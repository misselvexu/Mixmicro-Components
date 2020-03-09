#!/usr/bin/env bash

CURRENT_FULL_PATH=$(cd `dirname $0`; pwd)
cd ${CURRENT_FULL_PATH}
cd ..
java -jar libs/encrypt-tool.jar $* $JAVA_OPTS