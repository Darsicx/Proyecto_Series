package com.example.carlos.proyecto_series;

import android.widget.TextView;

//Clase Molde episodios la cual nos sirve para que el adaptador especial identifique que elementos contiene

public class Molde_episodios {

    private String episodio;
    private String contenido;

    public Molde_episodios(String episodio, String contenido) {
        this.episodio = episodio;
        this.contenido = contenido;
    }

    public String getEpisodio() {
        return episodio;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
