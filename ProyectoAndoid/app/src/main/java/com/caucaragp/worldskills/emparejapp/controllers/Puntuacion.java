package com.caucaragp.worldskills.emparejapp.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.caucaragp.worldskills.emparejapp.R;
import com.caucaragp.worldskills.emparejapp.models.GestorDB;
import com.caucaragp.worldskills.emparejapp.models.Score;

import java.util.ArrayList;
import java.util.List;

public class Puntuacion extends AppCompatActivity implements View.OnClickListener {
    //Declaración de variables
    TextView txtPrimero, txtSegundo, txtTercero, txtCuarto;
    RadioButton rbtnFacil, rbtnMedio, rbtnDificil;
    GestorDB gestorDB;
    int nivel=4;
    List<Score> scores = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);
        inizialite();
        escucharBotones();
        consultScore();
        inputList();
    }


    //Método para inicializar las vistas y la clase GestorDB
    private void inizialite() {
        txtPrimero = findViewById(R.id.txtPrimero);
        txtSegundo = findViewById(R.id.txtSegundo);
        txtTercero = findViewById(R.id.txtTercero);
        txtCuarto = findViewById(R.id.txtCuarto);

        rbtnFacil = findViewById(R.id.rbtnFacil);
        rbtnMedio = findViewById(R.id.rbtnMedio);
        rbtnDificil = findViewById(R.id.rbtnDificil);

        gestorDB = new GestorDB(this);
    }

    //Método que permite ingresar el setOnClickListener a los RadioButtons
    private void escucharBotones() {
        rbtnFacil.setOnClickListener(this);
        rbtnMedio.setOnClickListener(this);
        rbtnDificil.setOnClickListener(this);
    }

    //Métpdo para consultar la punutación dependiendo del RadioButton seleccionado
    private void consultScore() {

        if (rbtnFacil.isChecked()){
            nivel =4;
            scores = gestorDB.listScore(nivel);

        }

        if (rbtnMedio.isChecked()){
            nivel =6;
            scores = gestorDB.listScore(nivel);

        }

        if (rbtnDificil.isChecked()){
            nivel =8;
            scores = gestorDB.listScore(nivel);

        }

    }

    //Método para ingresar la lista a las vistas
    private void inputList(){
        if (scores.size()>0){
            Score score = scores.get(0);
            txtPrimero.setText(score.getJugador()+ " "+ score.getPuntaje());
        }else {
            cleanV();
            Toast.makeText(this, "No hay punutaciones disponibles", Toast.LENGTH_SHORT).show();
        }

        if (scores.size()>1){
            Score score = scores.get(1);
            txtSegundo.setText(score.getJugador()+ " "+ score.getPuntaje());
        }else {
            txtSegundo.setText("");
            txtTercero.setText("");
            txtCuarto.setText("");
        }

        if (scores.size()>2){
            Score score = scores.get(2);
            txtTercero.setText(score.getJugador()+ " "+ score.getPuntaje());
        }else {
            txtTercero.setText("");
            txtCuarto.setText("");
        }

        if (scores.size()>3){
            Score score = scores.get(3);
            txtCuarto.setText(score.getJugador()+ " "+ score.getPuntaje());
        }else {
            txtCuarto.setText("");
        }

    }

    //Método que limpia los TextView: txtPrimero, txtSegundo, txtTercero, txtCuarto
    private void cleanV() {
        txtPrimero.setText("");
        txtSegundo.setText("");
        txtTercero.setText("");
        txtCuarto.setText("");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rbtnFacil:
                nivel=4;
                consultScore();
                inputList();
                break;

            case R.id.rbtnMedio:
                nivel=6;
                consultScore();
                inputList();
                break;

            case R.id.rbtnDificil:
                nivel=8;
                consultScore();
                inputList();
                break;
        }
    }
}
