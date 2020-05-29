#!/bin/bash

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [[ -z "$1" || -z "$2" || -z "$3" || -z "$4" ]]
then
  echo "Usage: ./deploy.sh path_to_ssh_key registry_host registry_username registry_password"
  exit 1
fi

KEY=$1

REGISTRY_HOST=$2
REGISTRY_USERNAME=$3
REGISTRY_PASSWORD=$4

HOST="simonerps.our.buildo.io"

echo
echo "Deploying rps"

echo
echo Copying docker-compose file...
scp -ri $KEY $DIR/../terraform/docker-compose.yml ubuntu@$HOST:docker-compose.yml

echo
echo Logging into registry $REGISTRY_HOST
ssh -i $KEY ubuntu@$HOST "docker login -u $REGISTRY_USERNAME -p $REGISTRY_PASSWORD $REGISTRY_HOST"

echo
echo Updating docker images...
ssh -i $KEY ubuntu@$HOST "docker-compose pull"

echo
echo Removing old containers...
ssh -i $KEY ubuntu@$HOST "docker-compose down"

echo
echo Restarting all the other containers...
ssh -i $KEY ubuntu@$HOST "docker-compose up -d"

echo
echo Cleaning up unused resources...
ssh -i $KEY ubuntu@$HOST "docker system prune --all -f"

echo
echo "All done :)"
echo "Please allow about 1 minute for the app to restart"
echo "https://$HOST"
echo
