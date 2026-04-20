# Sistema de Pedidos con Clean Architecture

## Información del proyecto

**Asignatura:** Patrones de Diseño de Software  
**Unidad:** Unidad 8 - Arquitectura Limpia  
**Actividad:** Post-Contenido 1  
**Estudiante:** Carlos Vega  
**Repositorio:** Vega-post1-u8  

## Descripción

Este proyecto implementa un sistema de gestión de pedidos utilizando **Clean Architecture**, organizando el sistema en capas concéntricas que separan claramente la lógica de negocio de los detalles técnicos.

El objetivo principal es garantizar independencia del dominio respecto a frameworks como Spring Boot, permitiendo que la lógica central sea fácilmente testeable, mantenible y extensible.

La aplicación expone una API REST para crear pedidos y consultarlos por identificador.

## Tecnologías utilizadas

- Java 17  
- Spring Boot 3.3.5  
- Spring Web  
- Spring Data JPA  
- H2 Database  
- Maven  
- JUnit 5  

## Arquitectura (Clean Architecture)

El sistema se organiza en círculos:

```text
domain      -> Entidades y Value Objects
usecase     -> Casos de uso
adapter     -> Controladores y persistencia
config      -> Configuración de dependencias
````

Esta estructura sigue el principio de inversión de dependencias, donde las capas externas dependen de las internas, pero nunca al contrario.

## Estructura del proyecto

```text
com.example.pedidos
├── domain
│   ├── model
│   │   ├── Pedido.java
│   │   ├── PedidoId.java
│   │   ├── Dinero.java
│   │   ├── LineaPedido.java
│   │   └── EstadoPedido.java
├── usecase
│   ├── CrearPedidoUseCase.java
│   └── ConsultarPedidoUseCase.java
├── adapter
│   ├── in
│   │   └── web
│   │       ├── PedidoController.java
│   │       └── dto
│   └── out
│       └── persistence
│           ├── PedidoJpaEntity.java
│           ├── PedidoJpaRepository.java
│           └── PedidoRepositoryAdapter.java
├── config
│   └── BeanConfiguration.java
└── PedidosApplication.java
```

## Dominio

El dominio es completamente independiente de frameworks.

### Entidad principal

* Pedido

### Value Objects

* PedidoId
* Dinero
* LineaPedido
* EstadoPedido

Estos encapsulan reglas del negocio y garantizan consistencia interna del sistema.

## Casos de uso

* CrearPedidoUseCase
* ConsultarPedidoUseCase

Los casos de uso contienen la lógica de aplicación y orquestan el flujo entre dominio y puertos.

## Puerto de salida

```java
Pedido guardar(Pedido pedido);
Optional<Pedido> buscarPorId(PedidoId id);
```

Este puerto define el contrato que debe cumplir la infraestructura sin acoplar el dominio a JPA.

## Adaptadores

### Adaptador REST

Expone los endpoints:

```text
POST /api/pedidos
GET  /api/pedidos/{id}
```

### Adaptador de persistencia

Encapsula el acceso a datos con JPA:

* PedidoJpaEntity
* PedidoJpaRepository
* PedidoRepositoryAdapter

## Base de datos

Se utiliza H2 en memoria.

Consola:

```text
http://localhost:8080/h2-console
```

Datos de conexión:

* JDBC URL: `jdbc:h2:mem:pedidosdb`
* User Name: `sa`
* Password: *(vacío)*

## Manejo de errores

Se implementa manejo global de excepciones para validar reglas del dominio.

Ejemplos:

```json
{
  "error": "El cliente es obligatorio"
}
```

```json
{
  "error": "La cantidad debe ser mayor a cero"
}
```

## Ejecución del proyecto

```bash
mvn spring-boot:run
```

La aplicación queda disponible en:

```text
http://localhost:8080
```

## Pruebas de endpoints

### Crear pedido

```json
POST /api/pedidos
{
  "clienteNombre": "Ana Garcia",
  "lineas": [...]
}
```

Respuesta:

```json
{
  "pedidoId": "a82f2f67-e67e-422c-b066-5f7007775cc5"
}
```

### Consultar pedido

```json
GET /api/pedidos/{id}
```

Respuesta:

```json
{
  "id": "...",
  "clienteNombre": "Ana Garcia",
  "estado": "CONFIRMADO",
  "lineas": [...],
  "total": 1500.00
}
```

## Validaciones

* Cliente obligatorio → 400
* Cantidad inválida → 400

## Pruebas unitarias

El proyecto incluye pruebas con JUnit que validan:

* Lógica del dominio
* Casos de uso

Resultados:

```text
Tests run: 3, Failures: 0, Errors: 0
```

Esto demuestra que el dominio es independiente de Spring.

## Verificación

* Compilación exitosa con Maven
* Empaquetado correcto
* API funcional
* Validaciones operativas
* Arquitectura desacoplada
* Dominio testeable sin framework

## Commits sugeridos

```bash
git add .
git commit -m "feat: crear proyecto Spring Boot con Clean Architecture"

git add .
git commit -m "feat: implementar dominio y casos de uso de pedidos"

git add .
git commit -m "feat: agregar adaptadores REST y JPA"

git add .
git commit -m "docs: agregar README con arquitectura y pruebas"
```

## Conclusión

La implementación de Clean Architecture permite separar completamente la lógica de negocio de la infraestructura, garantizando un sistema desacoplado, mantenible y fácilmente testeable. Esta aproximación mejora la calidad del software y facilita su evolución a largo plazo.

