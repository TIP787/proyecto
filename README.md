# TomyPaz

## Descripción

TomyPaz es un microservicio desarrollado en Spring Boot que permite la creación y consulta de usuarios. El proyecto incluye autenticación mediante JWT, manejo de excepciones, y validaciones de entrada. Utiliza una base de datos MySQL para almacenar la información de los usuarios.

## Funcionalidades

- Registro de usuarios
- Inicio de sesión con autenticación JWT
- Validaciones de datos de entrada
- Manejo de excepciones personalizadas
- Interacción con la base de datos utilizando JPA

## Tecnologías Utilizadas

- Java 19.0.2
- Spring Boot
- Maven
- MySQL
- JPA
- JWT

## Estructura del Proyecto

TomyPaz/
├── .gitignore
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           ├── TomyPazApplication.java         # Clase principal de Spring Boot
        │           ├── controllers/                     # Controladores para manejar las solicitudes
        │           │   └── UsuarioController.java 
        │           │   └── Controlador_Login.Java
        │           │   └── Controlador_Registro.Java
        │           ├── models/                          # Modelos de datos
        │           │   ├── Usuario.java
        │           │   └── Telefono.java
        │           ├── repositories/                    # Repositorios JPA
        │           │   └── base_De_datos.java
        │           ├── Paz/                        # Servicios para la lógica de negocio
        │           │   └── UserService.java
        │           │   └── Registro.Java
        │           ├── Configuracion/                        # Configuraciones de seguridad
        │           │   ├── JwtAuthenticationFilter.java
        │           │   ├── GlobalExceptionHandler.java
        │           │   └── SecurityConfig.java
        │           │── respuesta/                            # Objetos de transferencia de datos
        │           │   └── UsuarioResponse.java
        │           ├── util/                            # Utilidades del proyecto
        │               └── JwtUtil.java                 # Clase para manejar JWT
        └── resources/
            ├── application.properties                    # Configuraciones de la aplicación



## Configuración

1. **Clona el repositorio**:
   ```bash
   git clone https://github.com/tuusuario/tomypaz.git

