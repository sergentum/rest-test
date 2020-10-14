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


Appropriate HTTP codes returned, like 404 if product not found and 201 when product created for example.

This project has 3 tier architecture:
- Presentation layer: controller.
- Logic layer: service, used only for mapping from entity to dto in order to avoid lazyInitializationException, which is possible when objects become complex.
- Model layer: Spring Data JPA.

RestExceptionHandler allows to catch any exception and response with correct HTTP code according expectations. Custom exception tree can be implemented and processed in handler.
 
Controller has few unit tests, just to check response status and body in some cases. Uses Mockito, so it doesn't require full springboot app startup. 