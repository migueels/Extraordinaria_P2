package com.disVaadin.Practica2Ext;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Habilidad {

    @Id
    @GeneratedValue
    public Long idHabilidad;

    private String tipo;

    @Column(length = 200) //establecemos una longitud de 200 para la columna de la deficion
    private String definicion;

    private Long idSuperheroe;

    protected Habilidad(String tipo, String definicion, Long idSuperheroe){
        this.tipo = tipo;
        this.definicion = definicion;
        this.idSuperheroe = idSuperheroe;


    }

    //constructor
    public Habilidad(){

    }

    //getter y setters

    //del identificador solo neceitamos el getter ay que los genera


    public Long getIdSuperheroe() {
        return idSuperheroe;
    }

    public Long getIdHabilidad() {
        return idHabilidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDefinicion() {
        return definicion;
    }

    public void setDefinicion(String definicion) {
        this.definicion = definicion;
    }
}
