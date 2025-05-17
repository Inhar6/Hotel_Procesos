[![Java CI with Maven](https://github.com/Inhar6/Hotel_Procesos/actions/workflows/maven.yml/badge.svg)](https://github.com/Inhar6/Hotel_Procesos/actions/workflows/maven.yml)

[![Doxygen Documentation](https://github.com/Inhar6/Hotel_Procesos/actions/workflows/doxygen.yml/badge.svg)](https://github.com/Inhar6/Hotel_Procesos/actions/workflows/doxygen.yml)

HOTEL PROCESOS
=============================

Este proyecto va encarado a la asignatura de Procesos del Software y Calidad. Principalmente esta creado para la gestion de un hotel desde distintos puntos de vista.
Para el desarrollo de dicho proyecto se han utilizado las tecnologias de SpringBoot, SQL y Maven.

La estructura básica de un proyecto SpringBoot se puede inicializar utilizando *Spring initializr* en https://start.spring.io/.

Para el lanzamiento del proyecto
-------------------------

Primero, verifique todas las dependencias requeridas especificadas por el archivo pom.xml y la configuración de la base de datos contenida en el archivo *src/main/resources/application.properties*.

Ahora, ejecute el siguiente comando para verificar la descarga de todas las dependencias y verificar que todo se compile correctamente.

      mvn compile

Asegúrese de que la base de datos MySQL esté configurada correctamente antes de conectar la aplicación para usarla.
Utilice el contenido del archivo *src/main/resources/dbsetup.sql* para crear la base de datos, un usuario específico 'spq' (contraseña: 'spq') para la aplicación y otórguele privilegios. Si utiliza el cliente de línea de comandos para MySQL, puede ejecutar el siguiente comando.

      mysql –uroot -p < src/main/resources/dbsetup.sql

de lo contrario, podría utilizar el contenido del archivo en cualquier otro cliente MySQL que esté utilizando.

Ahora, inicie el servidor usando el siguiente comando

    mvn spring-boot:run


Si no hay errores, debería obtener una aplicación web ejecutándose y sirviendo su contenido en http://localhost:8080/. Puede presionar Ctrl+C para detener la aplicación.

REST API
--------

La aplicación expone una API REST, utilizada por la aplicación web e implementada en las clases HabitacionController y ClienteController. Por ejemplo, algunos métodos son

Muestra todas las habitaciones registradas

    GET http://localhost:8080/api/personal/habitaciones

Para ver la lista completa de métodos de la API REST, puede visitar la interfaz de Swagger en: http://localhost:8080/swagger-ui.html. Consulte las anotaciones en las clases *Habitacion Controller* y *ClienteController*, las dependencias requeridas en el archivo *pom.xml* y el archivo *application.properties* para su configuración.

Empaquetando la aplicación
-------------------------

La aplicación se puede empaquetar ejecutando el siguiente comando

    mvn package

incluidas todas las bibliotecas necesarias para SpringBoot dentro de *target/rest-api-0.0.1-SNAPSHOT.jar*, que se pueden distribuir.

Una vez empaquetado, el servidor se puede iniciar con

    java -jar rest-api-0.0.1-SNAPSHOT.jar

y el cliente de muestra ejecutándose, ya que SpringBoot cambia la forma en que se ejecuta el cargador de Java predeterminado.

    java -cp rest-api-0.0.1-SNAPSHOT.jar -Dloader.main=com.example.restapi.client.BookManager org.springframework.boot.loader.launch.PropertiesLauncher localhost 8080

Por lo tanto, en un desarrollo real, sería recomendable crear diferentes proyectos Maven para aplicaciones servidor y cliente, facilitando la distribución y mantenimiento de cada aplicación por separado.

Estrategia de Pruebas
---------------------

En este proyecto, hemos implementado una estrategia de pruebas que incluye tanto tests unitarios como tests de integración para asegurar la calidad y el correcto funcionamiento de la aplicación.

### Tests Unitarios

Los tests unitarios se han utilizado principalmente para verificar la lógica de negocio en las clases de modelos y servicios. Estos tests se ejecutan en aislamiento utilizando mocks y Junit para simular las dependencias externas.

Para ejecutar los tests unitarios, utiliza el siguiente comando:

    mvn test

### Tests de Integración

Los tests de integración se han diseñado para verificar la interacción entre diferentes componentes del sistema. En este proyecto, los tests de integración se centran en los repositorios y los controladores, asegurando que las operaciones de base de datos y las rutas HTTP funcionen correctamente.

Para ejecutar los tests de integración, utiliza el siguiente comando:

    mvn verify

Este comando ejecutará los tests de integración y generará un informe de cobertura de código si tienes configurado el plugin JaCoCo.

### Tests de Integración

Para ejecutar los tests de rendimiento, utiliza el siguiente comando:

    mvn -Pperformance integration-test

Documentación y GitHub Pages
----------------------------

Este proyecto utiliza Doxygen para generar documentación y GitHub Pages para alojarla. La documentación incluye detalles sobre pruebas unitarias, pruebas de integración y pruebas de rendimiento.

### Generación de Informes de Doxygen

Para generar informes de Doxygen, puedes usar los siguientes comandos de Maven:

- Generar informes de Doxygen: `mvn doxygen:report` o `mvn site`

### Mover la Documentación a la Carpeta `docs`

Si deseas generar toda la documentación y moverla a la carpeta `docs` de tu proyecto, sigue estos pasos:

1. Ejecuta las pruebas unitarias y de cobertura:
   ```bash
   mvn test jacoco:report
   ```

2. Ejecuta las pruebas de rendimiento:
   ```bash
   mvn -Pperformance integration-test
   ```

3. Asegúrate de que los informes de rendimiento también se muevan a la carpeta `target/site/reports`:
   ```bash
   mvn -Pperformance resources:copy-resources@copy-perf-report
   ```

4. Mueve todo el contenido a la carpeta `docs`:
   ```bash
   mvn post-site
   ```

### GitHub Pages

El proyecto está configurado para usar GitHub Pages para alojar la documentación. La rama `gh-pages` se utiliza para este propósito. Puedes ver la documentación en la siguiente URL:

- [GitHub Pages para Hotel_Procesos](https://inhar6.github.io/Hotel_Procesos)


Referencias
----------

* Very good explaination of the project: https://medium.com/@pratik.941/building-rest-api-using-spring-boot-a-comprehensive-guide-3e9b6d7a8951 
* Building REST services with Spring: https://spring.io/guides/tutorials/rest
* Good example documenting how to generate Swagger APIs in Spring Boot: https://bell-sw.com/blog/documenting-rest-api-with-swagger-in-spring-boot-3/#mcetoc_1heq9ft3o1v 
* Docker example with Spring: https://medium.com/@yunuseulucay/end-to-end-spring-boot-with-mysql-and-docker-2c42a6e036c0
* Repositorio "SpringBoot + REST API + MySQL" utilizado en clase



