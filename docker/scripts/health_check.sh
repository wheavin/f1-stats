#!/bin/bash

status_code=$(curl --write-out %{http_code} --silent --output /dev/null curl 'http://localhost:8080/f1-stats-war/rest/v1/system')

if [[ "$status_code" -ne 200 ]] ; then
  echo "Application is not started yet"
  exit 1
else
  echo "Application is started"
  exit 0
fi
