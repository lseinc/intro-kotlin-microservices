#!/bin/bash

RUN_COUNT=10
URL=http://localhost:8080/hello

run-async() {
  typeset -i CNT=0
  MAX=$1
  while [ $CNT -lt $MAX ];
  do
    CNT=CNT+1
    time curl -v $URL  &
  done
}

set -x
# Use Apache Bench if you have it
AB=`which abx`
echo "AB=$AB"
if [ ! -z "$AB" ]; 
then
  ab -n $RUN_COUNT -c 10 $URL
else
  run-async $RUN_COUNT
fi

