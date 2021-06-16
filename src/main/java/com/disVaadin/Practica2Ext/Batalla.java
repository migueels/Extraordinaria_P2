package com.disVaadin.Practica2Ext;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Batalla {

    //Para la clase de Batalla vamo a necesitar los objetos del json
    //ademas de su identificador y el del superheroe para hacer busquedas
    @Id
    @GeneratedValue
    private Long idBatalla;

    private String fecha_inicio;
    private String fecha_fin;
    private String lugar;

    private Long idSuperheroe;



    public Batalla(){

    }

    public Batalla(String lugar, String fecha_inicio, String fecha_fin, Long idSuperheroe) {
        this.lugar = lugar;
        this.fecha_fin = fecha_fin;
        this.fecha_inicio = fecha_inicio;
        this.idSuperheroe = idSuperheroe;
    }

    public Long getIdSuperheroe() {
        return idSuperheroe;
    }

    public Long getIdBatalla() {
        return idBatalla;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
