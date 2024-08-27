curl -X 'PUT' \
'http://localhost:9000/admin/v1/lra-coordinator/6e734de6-97e6-4002-b080-f2c496a8dade/close' \
-H 'accept: application/json'

docker build --tag ottm-offer:1.0 .
docker run -it -p 8081:8081 --rm --name ottm-offer ottm-offer:1.0