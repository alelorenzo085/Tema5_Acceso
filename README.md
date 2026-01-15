# Herramientas ORM (TEMA 5)

```ACCESO A DATOS```

## Comenzando 🚀

_Este repositorio es una prueba realizada en clase utilizando herramientas de ORM._

## Construido con 🛠️

* [Visual Studio Code](http://www.code.visualstudio.com/) - IDE utilizado
* [Maven](https://maven.apache.org/) - Manejador de dependencias
* [JDBC](https://www.oracle.com/es/database/technologies/appdev/jdbc.html) - Usado para manejar BBDD
* [SQLite](https://sqlite.org) - Base de Datos implementada en el código
* [Hibernate](https://hibernate.org) - Herramientas de ORM

## Dependencias

```
<dependencies>
        <!-- Dependencia logback -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.5.18</version>
        </dependency>

        <!-- Dependencia SLF4J -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.1.0-alpha1</version>
        </dependency>

        <!-- Driver JDBC para SQLite -->
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.50.3.0</version>
        </dependency>

        <!-- Hibernate (Implementación de JPA) -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>${hibernate.version}</version>
        </dependency>

        <!-- Soporte en Hibernate para SQLite -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-community-dialects</artifactId>
            <version>${hibernate.version}</version>
        </dependency>
        
        <!-- Implementación para Hibernate de la especificación anterior -->
        <dependency>
            <groupId>org.hibernate.validator</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>9.1.0.Final</version>
        </dependency>

        <!-- Opcional para evaluación dinámica de cadenas de texto (puede necesitarlo Hibernate Validator) -->
        <dependency>
            <groupId>org.glassfish.expressly</groupId>
            <artifactId>expressly</artifactId>
            <version>6.0.0</version>
        </dependency>
    </dependencies>
```

## Autores ✒️

**Alejandro Lorenzo** - *Estudiante* - [alelorenzo085](https://github.com/alelorenzo085)


## Licencia 📄

Este proyecto está bajo la Licencia [MIT](https://mit-license.org) - mira el archivo [LICENSE](LICENSE) para detalles
