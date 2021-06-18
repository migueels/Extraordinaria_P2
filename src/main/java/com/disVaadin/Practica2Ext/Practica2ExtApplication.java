package com.disVaadin.Practica2Ext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;
import java.io.FileReader;

@SpringBootApplication
public class Practica2ExtApplication {

	private static final Logger log = LoggerFactory.getLogger(Practica2ExtApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(Practica2ExtApplication.class);

	}



	@Bean
	public CommandLineRunner cargaJson(ISuperheroe repository, IBatallas bat, IHabilidades hab) throws FileNotFoundException{

		//Lo que vamos a hacer aqui va a ser leer el json para poder obtener los objetos
		//Para ello vamos a usar el json realizado en la practica 1

		JsonParser parser = new JsonParser();
		Object objeto = parser.parse(new FileReader("Superhereoes_JSON.json"));
		JsonObject gsonObjeto = (JsonObject) objeto;

		//Pasamos a acceder a los superheroes
		//debemos ir pasando de objeto en objeto teniendo en cuenta los arrays
		gsonObjeto = gsonObjeto.getAsJsonObject("Heroe");
		gsonObjeto = gsonObjeto.getAsJsonObject("Superheroes");
		JsonArray dem = gsonObjeto.get("Superheroe").getAsJsonArray();

		return (args) -> {
			 //Una vez accedido a lo que va ser el array de Superheroes donde tenemos todos almacenados
			//vamos a ir sacando la informacion
			for (JsonElement i : dem){
				String nombre = ((JsonObject) i).get("Nombre").getAsString();
				String identidaD = ((JsonObject) i).get("IdentidadS").getAsString();
				String procedencia = ((JsonObject) i).get("procedencia").getAsString();
				String genero = ((JsonObject) i).get("genero").getAsString();

				//Tenemos en cuenta que el superhereo no tenga habilidades pero vamos a darle un numemero de 4, aunque puede varias
				int numHabilidades = 0; //inicializamos a 0 en numero de habilidades
				String[] tipoHab = new String[4];
				String[] definicionHab = new String[4];

				try{
					JsonObject habilidad = i.getAsJsonObject();
					habilidad = habilidad.getAsJsonObject("Habilidades");

					//Try para tener en cuanta que tenemos mas de una habilidad por cada superheroe
					//el cath sera para el caso de solo tener uno
					try{
						JsonArray habilidades = habilidad.get("Habilidad").getAsJsonArray();
						for(JsonElement j : habilidades){
							String tipo = ((JsonObject) j).get("Tipo").getAsString();
							String definicion = ((JsonObject) j).get("Definicion").getAsString();

							tipoHab[numHabilidades] = tipo;
							definicionHab[numHabilidades] = definicion;
							numHabilidades += 1; //incrementamos uno el numero de habilidades

						}

					}catch (Exception ex1){ //Esta excepcion para el caso de tener solamente una habilidad
						habilidad = habilidad.getAsJsonObject("Habilidad");
						numHabilidades = 1;
						String tipo = habilidad.get("Tipo").getAsString();
						String deficinicion = habilidad.get("Definicion").getAsString();
						tipoHab[0] = tipo;
						definicionHab[0] = deficinicion;

					}
				}catch (Exception ex1){

				}

				//THacemos lo mismo que con las habilidades pero en este caso con las batallas
				//debemos de tener en cuenta que el superheroes puede que participe en varias o que no lo haga en ninguna
				int numBatallas = 0; //inicializamos a 0 en numero de habilidades
				String[] fechaInicio = new String[4];
				String[] fechaFin = new String[4];
				String[] Lugar = new String[4];

				try{
					JsonObject batalla = i.getAsJsonObject();
					batalla = batalla.getAsJsonObject("Batallas");

					//Try para tener en cuanta que tenemos mas de una habilidad por cada superheroe
					//el cath sera para el caso de solo tener uno
					try{
						JsonArray batallas = batalla.get("Batalla").getAsJsonArray();
						for(JsonElement j : batallas){
							String inicio = ((JsonObject) j).get("Fecha_comienzo").getAsString();
							String fin = ((JsonObject) j).get("Fecha_fin").getAsString();
							String lugar = ((JsonObject) j).get("Lugar").getAsString();

							fechaInicio[numBatallas] = inicio;
							fechaFin[numBatallas] = fin;
							Lugar[numBatallas] = lugar;
							numBatallas += 1; //incrementamos uno el numero de habilidades

						}

					}catch (Exception ex2){ //Esta excepcion para el caso de tener solamente una habilidad
						batalla = batalla.getAsJsonObject("Batalla");
						numBatallas = 1;
						String inicio = batalla.get("Fecha_comienzo").getAsString();
						String fin = batalla.get("Fecha_fin").getAsString();
						String lugar = batalla.get("Lugar").getAsString();
						fechaFin[0] = fin;
						fechaInicio[0] = inicio;
						Lugar[0] = lugar;

					}
				}catch (Exception ex2){

				}

				//AQUI TENEMMOS QUE METER PARA AÑADIR NUEVOS SUPERHERES

				//Tednremos que tener en cuanta el ide del superheroe
				Superheroe heroe = new Superheroe(nombre, identidaD, genero, procedencia, numBatallas, numHabilidades);
				repository.save(heroe);
				//establecemos el identificador
				Long idHeroe = heroe.getId();
				//aqui recorrer los arrays de batallas y habilidaes y añadirloc con el id del hereo
				for(int k = 0; k<numBatallas; k++){
					Batalla war = new Batalla(fechaFin[k],fechaInicio[k], Lugar[k],idHeroe);
					bat.save(war);
				}

				//recorremos las habilidades y vamos añadiendo por cada heroe
				for(int j = 0; j<numHabilidades; j++){
					Habilidad hability = new Habilidad(tipoHab[j],definicionHab[j],idHeroe);
					hab.save(hability);
				}

			}
		};
	}


}
