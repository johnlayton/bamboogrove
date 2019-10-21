#!/usr/bin/env sh

#
#
#
grpcurl --insecure -d '{ }' localhost:8443 org.demo.armeria.UserService/List

#
#
#
grpcurl --insecure -d '{ "name": "Armeria" }' localhost:8443 org.demo.armeria.HelloService/Hello

#
#
#
curl --insecure -X POST -H 'content-type: application/json; charset=utf-8; protocol=gRPC' 'https://localhost:8443/org.demo.armeria.HelloService/Hello' -d '{
  "name": "Armeria"
}'