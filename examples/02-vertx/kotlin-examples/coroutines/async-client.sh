#!/bin/bash

RUN_COUNT=1000
URL=http://localhost:8080/movie/starwars

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
AB=`which ab`
echo "AB=$AB"
if [ ! -z "$AB" ]; 
then
  ab -n $RUN_COUNT -c 51 $URL
else
  run-async $RUN_COUNT
fi

