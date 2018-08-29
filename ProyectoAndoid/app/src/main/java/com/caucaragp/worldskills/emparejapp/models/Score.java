package com.caucaragp.worldskills.emparejapp.models;

public class Score {
    //Declaración de variables

    private String jugador;
    private int puntaje;
    private int nivel;


    public Score() {
    }

    //Encapsulamiento de variables de la clase Score
    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
