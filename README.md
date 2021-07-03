PRACTICAS 2 EXTRAORDIANRIA 

*******************************************************
* AUROTES: Miguel Salas Arias / Liam Kristian Roncero *
*******************************************************

Para poder ejecutar la aplicacion lo podrá hacer de dos maneras: 

1) Creando un archivo ejecutable jar y ejecutarlo desde la linea de comandos

		./mvnm package 
		java -jar target/*.jar

2) Puede ejecutarlo directamente desde Maven usando Spring Boot Maven. 

		./mvnw spring-boot:run



Para poder arrancar la aplicacio via ssh se ha usado heroku, con una aplicacion llamada hereosext. Para ello se ha debido se usar un Procfile. Para arrancarlo solo se necesita copiar y pegar el siguiente enlace en el navegador.
AVISO --> (Puede que a veces tarde un poco mas de lo normal)


ENLACE HEROKU: https://heroesext.herokuapp.com


* BASE DE DATOS

La aplicacion usa una base de datos en memoria H2 que se llena al inicio con datos. La consola h2 se expone directamente en la direccion "http://localhost:8080" pero con poner en el buscador localhost:8080 seria valido.


* REQUISITOS IDE 

Para la ejecucion y buen funcionameinto de la aplicacion seán necesarios los siguientes elementos:

-Java 8 o recientes

- Vaadin version 14.5.2 (La 8.6.2 es otra opcion pero no funciona bien con spring )

- IDE que pueda ser Eclipse o Intellij. En este caso se ha usado Intellij IDE y Visual Studio Code como editor de texto.


*******************
* Jenkins
*******************



