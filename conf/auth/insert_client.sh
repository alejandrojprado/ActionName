#!/bin/sh
echo "[client]" > tmp/mysql.cnf
echo "user=root" >> tmp/mysql.cnf
echo "password=mahisoft-dev" >> tmp/mysql.cnf
echo "host=localhost" >> tmp/mysql.cnf
mysql --defaults-extra-file=tmp/mysql.cnf  -e "USE auth; INSERT IGNORE INTO oauth_client_details (client_id, client_secret, scope, authorities, authorized_grant_types, access_token_validity, refresh_token_validity) VALUES (\"platform-services\", \"{bcrypt}\$2a\$10\$yuZGQXj8tHZu/hN9SJQ/duwOj1nqm8hXU7tn7DSMNAs6QKPzGGOlu\", \"read,trust,write\", \"ADMIN\", \"refresh_token,password,authorization_code,implicit,client_credentials\", 1000, 7200);"