# E-Commerce Backend test

## Software engineer test - Asunción Paterna

Este proyecto es un sistema backend para un e-commerce diseñado para gestionar carritos de compra y productos sin necesidad de almacenamiento persistente. Este proyecto se ha realizado desde la IDE InelliJ, el código de desarrollo se encuentra en la rama 'Dev', y toda la información se almacena en memoria. Entre sus funcionalidades se encuentra la eliminación de carritos inactivos tras 10 minutos, lo que garantiza que los datos no persistan más allá de la ejecución de la aplicación.

### Tecnologías Utilizadas

- Java 17
- Spring Boot 3.4.3
- Maven
- Lombok
- Mockito
- JUnit 5 para pruebas unitarias e integración

### Funcionalidades
 - Crear, obtener y eliminar productos.
 - Crear, obtener y eliminar carritos.
 - Añadir y eliminar productos de un carrito.
 - Eliminación automática de carritos tras 10 minutos de inactividad.

### Endpoints

**PRODUCTOS**
- GET /api/products → Obtener todos los productos.
- POST /api/products → Crear un producto.
- DELETE /api/products → Eliminar todos los productos.
- GET /api/products/{productId} → Obtener un producto por ID.
- DELETE /api/products/{productId} → Eliminar un producto por ID.
  
**CARRITOS**
- GET /api/carts → Obtener todos los carritos.
- POST /api/carts → Crear un carrito.
- DELETE /api/carts → Eliminar todos los carritos.
- POST /api/carts/{cartId} → Obtener un carrito por ID.
- DELETE /api/carts/{cartId} → Eliminar un carrito por ID.
- POST /api/carts/{cartId}/product → Agregar un producto al carrito.
- DELETE /api/carts/{cartId}/{productId} → Eliminar un producto del carrito.

### Ejecución
1. Cambio a la rama dev:
   ```sh
   git checkout dev

3. Construir el proyecto con Maven:
   ```sh
   mvn clean install

4. Ejecutar la aplicación:
   ```sh
   mvn spring-boot:run
   
### Pruebas
Se ha desarrollado pruebas unitarias y de integración. Se encuentran en *test/java/org/ecommerce/cart*. Clases de prueba implementadas:
- CartServiceTest
- ProductServiceTest
- CartControllerTest
- ProductControllerTest

Para ejecutar las pruebas:
```sh
mvn clean test
