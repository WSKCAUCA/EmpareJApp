package com.caucaragp.worldskills.emparejapp.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caucaragp.worldskills.emparejapp.R;

public class Inicio extends AppCompatActivity {

    Button btnContinuar;
    TextView txtPlayer1, txtPlayer2;
    public static String jugador1, jugador2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        inicializar();
        cambiarActividad();
    }

    //Referenciamos
    private void inicializar() {
        txtPlayer1 = findViewById(R.id.txtPlayer1);
        txtPlayer2 = findViewById(R.id.txtPlayer2);
        btnContinuar = findViewById(R.id.btnContinuar);
    }

    //Método que valida si los dos campos de texto no estan vacios
    private void cambiarActividad() {

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jugador1= txtPlayer1.getText().toString();
                jugador2=txtPlayer2.getText().toString();
                if (jugador1.length()>0 && jugador2.length()>0){
                    Intent intent = new Intent(Inicio.this, Menu.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(Inicio.this, "No puedes entrar al menu porque alguno de los campos esta vacio", Toast.LENGTH_SHORT).show();
                    if (jugador1.length()<0){
                        txtPlayer1.setError("Por favor no deje el campo vacio");
                    }

                    if (jugador2.length()<0){
                        txtPlayer1.setError("Por favor no deje el campo vacio");
                    }
                }

            }
        });


    }




}
