package com.example.carlos.proyecto_series;

import android.graphics.Bitmap;
import android.widget.ImageView;

//Clase Molde peli la cual nos sirve para que el adaptador especial identifique que elementos contiene

public class Molde_peli {

    private Bitmap cartel;
    private String nombre;
    private Integer idSerie;

    public Molde_peli(Bitmap cartel, String nombre,Integer Serie) {
        this.cartel = cartel;
        this.nombre = nombre;
        this.idSerie=Serie;
    }

    public Bitmap getCartel() {
        return cartel;
    }

    public void setCartel(Bitmap cartel) {
        this.cartel = cartel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(Integer idSerie) {
        this.idSerie = idSerie;
    }
}
