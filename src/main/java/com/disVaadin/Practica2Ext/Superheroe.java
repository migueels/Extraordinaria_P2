package com.disVaadin.Practica2Ext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Superheroe {
    //clase de los superheroes
    @Id
    @GeneratedValue
    private Long id; //variable para el identidicador


    private String nombre; //variable para el nombre del superhereos
    private String identidadSecreta;
    private String procedencia;
    private String genero;
    private int numeroBatallas;
    private int numeroHabilidades;

    public Superheroe() {
    }

    public Superheroe(String nombre, String identidadSecreta, String procedencia, String genero, int numeroBatallas, int numeroHabilidades) {
       this.nombre = nombre;
       this.identidadSecreta = identidadSecreta;
       this.procedencia = procedencia;
       this.genero = genero;
       this.numeroHabilidades = numeroHabilidades;
       this.numeroBatallas = numeroBatallas;

    }


    //GETTER y SETTER


    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentidadSecreta() {
        return identidadSecreta;
    }

    public void setIdentidadSecreta(String identidadSecreta) {
        this.identidadSecreta = identidadSecreta;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public int getNumeroBatallas() {
        return numeroBatallas;
    }

    public void setNumeroBatallas(int numeroBatallas) {
        this.numeroBatallas = numeroBatallas;
    }

    public int getNumeroHabilidades() {
        return numeroHabilidades;
    }

    public void setNumeroHabilidades(int numeroHabilidades) {
        this.numeroHabilidades = numeroHabilidades;
    }
}
