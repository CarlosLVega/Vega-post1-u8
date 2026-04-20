# Sistema de Pedidos con Clean Architecture

## Informacion del proyecto

**Asignatura:** Patrones de Diseno de Software  
**Unidad:** Unidad 8 - Patrones Arquitectonicos II  
**Actividad:** Post-Contenido 1  
**Repositorio:** Vega-post1-u8  

## Descripcion

Este proyecto implementa un sistema de pedidos aplicando **Clean Architecture**. La solucion organiza el codigo en circulos concentricos: entidades de dominio, casos de uso, adaptadores de interfaz y frameworks/drivers.

La aplicacion expone una API REST para crear pedidos, consultar un pedido por ID y listar todos los pedidos. Los datos se persisten usando Spring Data JPA sobre una base H2 en memoria.

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- H2 Database
- Bean Validation
- Maven
- JUnit 5

## Base de datos

El proyecto usa H2 en memoria.

```text
http://localhost:8080/h2-console
```

Datos de conexion:

```text
JDBC URL: jdbc:h2:mem:pedidosdb
User Name: sa
Password:
```

La contrasena se deja vacia.

## Clean Architecture

La dependencia del codigo apunta hacia adentro. El dominio no depende de Spring, JPA ni HTTP. Los casos de uso dependen del dominio y de puertos. Los adaptadores conectan la aplicacion con REST y persistencia.

```text
Frameworks & Drivers  -> Spring Boot, JPA, H2
Interface Adapters    -> Controller, DTOs, Repository Adapter
Use Cases             -> CrearPedidoService, ConsultarPedidoService, puertos
Entities              -> Pedido, PedidoId, Dinero, LineaPedido, EstadoPedido
```

## Estructura del proyecto

```text
com.example.cleanpedidos
├── domain
│   ├── entity
│   │   └── Pedido.java
│   └── valueobject
│       ├── PedidoId.java
│       ├── LineaPedido.java
│       ├── Dinero.java
│       └── EstadoPedido.java
├── usecase
│   ├── CrearPedidoUseCase.java
│   ├── ConsultarPedidoUseCase.java
│   ├── dto
│   │   └── LineaPedidoDto.java
│   ├── port
│   │   └── PedidoRepositoryPort.java
│   └── impl
│       ├── CrearPedidoService.java
│       ├── ConsultarPedidoService.java
│       └── PedidoNotFoundException.java
├── adapter
│   ├── in/web
│   │   ├── PedidoController.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── dto
│   │       ├── CrearPedidoRequest.java
│   │       ├── LineaPedidoResponse.java
│   │       └── PedidoResponse.java
│   └── out/persistence
│       ├── PedidoJpaEntity.java
│       ├── LineaPedidoJpaEmbeddable.java
│       ├── PedidoJpaRepository.java
│       └── PedidoRepositoryAdapter.java
├── config
│   └── PedidoConfiguration.java
└── CleanPedidosApplication.java
```

## Diagrama de flujo

```text
HTTP Request
    |
    v
PedidoController
    |
    v
CrearPedidoUseCase / ConsultarPedidoUseCase
    |
    v
CrearPedidoService / ConsultarPedidoService
    |
    v
PedidoRepositoryPort
    |
    v
PedidoRepositoryAdapter
    |
    v
PedidoJpaRepository + H2
```

## Dominio

`Pedido` es el Aggregate Root. Controla la modificacion de sus lineas y el cambio de estado.

Reglas principales:

- Un pedido debe tener cliente.
- Solo se pueden agregar lineas en estado `BORRADOR`.
- No se puede confirmar un pedido sin lineas.
- El total se calcula desde las lineas del pedido.

Value Objects:

- `PedidoId`: identidad tipada basada en UUID.
- `Dinero`: representa montos monetarios y no permite valores negativos.
- `LineaPedido`: representa una linea inmutable del pedido.
- `EstadoPedido`: enum de estados del pedido.

## Casos de uso

`CrearPedidoService` crea un pedido, agrega sus lineas, lo confirma y lo persiste usando `PedidoRepositoryPort`.

`ConsultarPedidoService` permite buscar un pedido por ID y listar todos los pedidos.

## Adaptadores

`PedidoController` traduce HTTP a llamadas a los casos de uso.

`PedidoRepositoryAdapter` traduce entre el modelo de dominio y las entidades JPA. Las entidades JPA viven solo en `adapter/out/persistence`.

## Endpoints

```text
POST /api/pedidos
GET  /api/pedidos/{id}
GET  /api/pedidos
```

## Como ejecutar

Compilar:

```bash
mvn clean compile
```

Compilar y probar:

```bash
mvn package
```

Ejecutar:

```bash
mvn spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

## Pruebas con PowerShell

Crear pedido:

```powershell
curl -Method POST http://localhost:8080/api/pedidos -ContentType "application/json" -Body '{"clienteNombre":"Ana Garcia","lineas":[{"productoNombre":"Laptop","cantidad":1,"precioUnitario":1500.00}]}'
```

Consultar pedido:

```powershell
curl http://localhost:8080/api/pedidos/{pedidoId}
```

Listar pedidos:

```powershell
curl http://localhost:8080/api/pedidos
```

Probar cliente vacio:

```powershell
curl -Method POST http://localhost:8080/api/pedidos -ContentType "application/json" -Body '{"clienteNombre":"","lineas":[{"productoNombre":"Laptop","cantidad":1,"precioUnitario":1500.00}]}'
```

Probar cantidad invalida:

```powershell
curl -Method POST http://localhost:8080/api/pedidos -ContentType "application/json" -Body '{"clienteNombre":"Ana Garcia","lineas":[{"productoNombre":"Laptop","cantidad":0,"precioUnitario":1500.00}]}'
```

## Capturas sugeridas

Agregar capturas de:

```text
capturas/post-pedido.png
capturas/get-pedido-id.png
capturas/get-pedidos.png
capturas/error-cliente-vacio.png
capturas/error-cantidad-invalida.png
capturas/h2-console.png
```

## Checkpoints

- El proyecto compila con `mvn clean compile`.
- `domain/` no importa Spring ni JPA.
- `usecase/` no importa Spring.
- `POST /api/pedidos` retorna `201 Created` con un UUID.
- `GET /api/pedidos/{id}` retorna el pedido con lineas y total calculado.
- Cliente vacio retorna `400`.
- Cantidad menor o igual a cero retorna `400`.
- `Pedido` se prueba en JUnit sin `@SpringBootTest`.
- `CrearPedidoService` se prueba usando un repositorio en memoria sin Spring.

## Commits sugeridos

```text
feat: crear proyecto Spring Boot con estructura Clean Architecture
feat: implementar dominio y casos de uso de pedidos
feat: agregar adaptadores REST y JPA para pedidos
feat: agregar pruebas y documentacion de Clean Architecture
```

## Conclusion

La implementacion aplica Clean Architecture separando el dominio, los casos de uso, los adaptadores y los frameworks. Esta organizacion protege la logica de negocio de detalles tecnicos, facilita las pruebas unitarias y permite cambiar la infraestructura sin afectar el nucleo del sistema.