package com.disVaadin.Practica2Ext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class Practica2ExtApplicationTests {

	//Aqui vamos a realziar las diferentes pruebas

	@Autowired
	ISuperheroe iSuperheroe;

	@Autowired
	IBatallas iBatallas;

	@Autowired
	IHabilidades iHabilidades;


	//Test para ver si existe el repositorio
	@Test
	void existeRepositorio(){
		assertNotNull(iSuperheroe);
	}

	@Test
	void buscarHeroe(){
		Optional<Superheroe> oSuperheroe = iSuperheroe.findById(Long.valueOf("2"));
		if(oSuperheroe.isPresent()){
			assertEquals("Thor",oSuperheroe.get().getIdentidadSecreta());
		}
	}

	@Test
	void buscarPorNombre(){
		List<Superheroe> lSuperheroe = iSuperheroe.findByNombreStartsWithIgnoreCase("Dr. Strange");
		if (!lSuperheroe.isEmpty()){
			assertEquals("Dr. Strange",lSuperheroe.get(0).getIdentidadSecreta());
		}
	}

	@Test
	void idDiferente()
	{
		//comprobamos que no hay dos superheroes con el mismo ID
		List<Superheroe> lSuperheroe = iSuperheroe.findAll();

		for (int i=0; i < lSuperheroe.size(); i++)
		{
			for (int j=0; j < lSuperheroe.size(); j++)
			{
				if (i != j)
				{
					assertNotEquals(lSuperheroe.get(i).getId(), lSuperheroe.get(j).getId());
				}
			}
		}
	}

	@Test
	void numBatallasCoinciden()
	{
		//comprobamos que hay tantas batallas como la variable numeroBatallas del superheroe
		List<Superheroe> lSuperheroe = iSuperheroe.findByNombreStartsWithIgnoreCase("Thor");
		List<Batalla> lBatallas = iBatallas.findByidSuperheroe(lSuperheroe.get(0).getId());

		assertEquals(lSuperheroe.get(0).getNumeroBatallas(), lBatallas.size());

	}

	@Test
	void numHabilidadesCoinciden()
	{
		//comprobamos que hay tantas habilidades como la variable numeroHabilidades del superheroe
		List<Superheroe> lSuperheroe = iSuperheroe.findByNombreStartsWithIgnoreCase("Thor");
		List<Habilidad> lHabilidades = iHabilidades.findByidSuperheroe(lSuperheroe.get(0).getId());
		System.out.println(lSuperheroe.get(0).getNumeroHabilidades());
		System.out.println(lHabilidades.size());
		assertEquals(lSuperheroe.get(0).getNumeroHabilidades(), lHabilidades.size());

	}






}
