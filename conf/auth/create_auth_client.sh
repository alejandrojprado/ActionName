#!/bin/sh
docker cp conf/auth/insert_client.sh ms-auth-db:/insert_client.sh
docker exec -it ms-auth-db bash /insert_client.sh