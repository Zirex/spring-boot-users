# Documentación del Proyecto: API RESTful para Creación de Usuarios

## Descripción

Este proyecto es una **API RESTful desarrollada con Spring Boot** que permite la creación de usuarios, con manejo de autenticación usando JWT (JSON Web Tokens). La API acepta y retorna datos en formato JSON para todos los endpoints, incluyendo los mensajes de error. La persistencia de los datos se realiza en una base de datos en memoria y la configuración de seguridad y validación de datos se implementa mediante Spring Security y validadores personalizados.

## Características
El objetivo principal de esta API es registrar usuarios y permitir su autenticación. A continuación, se describen las principales funcionalidades y requisitos del proyecto:

### 1. **Registro de Usuario**
- El endpoint de registro permite registrar un usuario con los siguientes campos:
    - `name`: Nombre completo del usuario.
    - `email`: Dirección de correo electrónico (validada con una expresión regular configurable).
    - `password`: Contraseña (también validada con una expresión regular configurable).
    - `phones`: Lista de números de teléfono, cada uno con su número, código de ciudad y código de país.

**Ejemplo de solicitud:**
  ```json
  {
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.cl",
    "password": "hunter2",
    "phones": [
      {
        "number": "1234567",
        "citycode": "1",
        "contrycode": "57"
      }
    ]
  }
```
- **Respuesta de éxito:**

    - Si el registro es exitoso, la API responde con el objeto del usuario creado, que incluye:
        - `id`: ID único del usuario (UUID).
        - `created`: Fecha y hora de creación del usuario.
        - `modified`: Fecha y hora de la última actualización del usuario.
        - `last_login`: Fecha y hora del último inicio de sesión (igual a created para nuevos usuarios).
        - `token`: Token de acceso generado para la autenticación (JWT).
        - `isActive`: Indica si el usuario está activo

**Ejemplo de la respuesta:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "created": "2024-09-16T10:00:00",
  "modified": "2024-09-16T10:00:00",
  "last_login": "2024-09-16T10:00:00",
  "token": "jwtTokenHere",
  "isActive": true
}
```

- **Errores:**

    - Si el correo electrónico ya existe en la base de datos, se devuelve el error:
    ```json
{ "mensaje": "El correo ya está registrado" }
```
    - Si el correo no cumple con el formato requerido, se devuelve un mensaje de error:
    ```json
{ "mensaje": "El correo no tiene un formato válido" }
```

### 2. **Validaciones**
Validación de correo electrónico: Se utiliza una expresión regular configurable para validar que el correo tenga un formato válido (por ejemplo, usuario@dominio.cl).
Validación de contraseña: La contraseña también se valida utilizando una expresión regular configurable, asegurando que cumpla con los requisitos de seguridad necesarios.
### 3. **Base de Datos**
La persistencia de datos se realiza en una base de datos en memoria utilizando H2.
Se utiliza JPA con Hibernate para la persistencia de las entidades, y las tablas se generan automáticamente a partir de las entidades definidas en el proyecto.
### 4. **Autenticación**
La API utiliza JWT (JSON Web Tokens) para la autenticación de usuarios. El token se genera durante el registro y se incluye en la respuesta.
El token es persistido junto con el usuario para validar futuras solicitudes.
### 5. **Documentación con Swagger**
La API está documentada utilizando Swagger. Puedes acceder a la documentación interactiva de la API en:
http://localhost:8080/api/v1/swagger-ui/index.html
### 6. **Pruebas Unitarias**
Las pruebas unitarias están implementadas utilizando JUnit y Mockito para asegurar que los servicios funcionan correctamente bajo diferentes escenarios.

**Ejecutar las Pruebas**
Usando Maven:

```bash
./mvnw test
```
Requisitos Técnicos
- **Java 8+**
- **Spring Boot**
- **JPA/Hibernate para persistencia**
- **H2 como base de datos en memoria**
- **JWT para autenticación**
- **Gradle o Maven para el build**
- **Swagger para la documentación**
- **JUnit para pruebas unitarias**
- **Configuración**
- **Configuración de la base de datos**
- **La aplicación utiliza H2, una base de datos en memoria, que se configura automáticamente al ejecutar la aplicación.**

**Propiedades de aplicación**
Asegúrate de configurar las siguientes propiedades en application.yml:
```properties
    password:
        regex: ^(?=.*[A-Z])(?=.*\d).{8,}$  # Al menos 1 mayúscula y 1 número
```

**Ejecución del proyecto**
Para ejecutar la aplicación, usa Maven o Gradle:

Con Maven:

```bash
mvn spring-boot:run
```

**Scripts de Base de Datos**
No es necesario ejecutar scripts manualmente, ya que H2 crea las tablas automáticamente al iniciar la aplicación.

**Cómo Probar**
1. Ejecuta la aplicación.
2. Usa Postman o Swagger UI para hacer una solicitud POST a:
http://localhost:8080/api/v1/auth/register
3. Prueba registrando un nuevo usuario con el formato JSON especificado.
4. Verifica la respuesta de éxito o los mensajes de error, según corresponda.

## **Buenas Prácticas y Patrones de Diseño**
El proyecto sigue las mejores prácticas de desarrollo de software, incluyendo:

- Principio de Responsabilidad Única: Cada clase y método tiene una única responsabilidad clara.
- Inyección de Dependencias: Uso de Spring para manejar las dependencias, facilitando las pruebas y la mantenibilidad.
- DTOs y Proyecciones: Separación entre las entidades de persistencia y los objetos que se exponen a través de la API.
- Manejo Centralizado de Excepciones: Uso de @ControllerAdvice para manejar excepciones de manera uniforme.
- Mapeo con ModelMapper: Simplificación de la conversión entre entidades y DTOs.

## **Conclusión**
Esta API ofrece un sistema básico pero robusto de registro y autenticación de usuarios utilizando Spring Boot, JWT y validación de datos. Se sigue un enfoque de buenas prácticas para el desarrollo de software, como el uso de inyección de dependencias, pruebas unitarias, y documentación con Swagger.