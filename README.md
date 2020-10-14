# rest-test
This is the test project with simple rest operations for one entity - product.
Easiest way to run it:
```
git clone https://github.com/sergentum/rest-test.git
cd rest-test/docker/
docker-compose up -d
```
Git and docker required, ofc.

When it started up, you can make next http requests:
host = localhost:8765 (port is defined in docker-compose.yml)
- To get list of all products:
```GET localhost:8765/test/products```
- To get specific product:
```GET /test/products/1```
- To add new product:
```POST /test/products/ ```
with body: 
```
{
    "name": "Bread",
    "price": 123
}
```
- To update existing product:
```PUT /test/products/1 ```
with body: 
```
{
    "name": "Bread",
    "price": 125
}
```
- To delete specific product:
```DELETE /test/products/1```
