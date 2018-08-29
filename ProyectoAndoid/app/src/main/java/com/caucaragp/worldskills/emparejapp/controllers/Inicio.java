package com.caucaragp.worldskills.emparejapp.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.caucaragp.worldskills.emparejapp.R;

public class Inicio extends AppCompatActivity {

    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        inicializar();
        cambiarActividad();
    }

    private void cambiarActividad() {

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Inicio.this, Menu.class);
                startActivity(intent);

            }
        });

    }

    //Referenciamos
    private void inicializar() {

        btnContinuar = findViewById(R.id.btnContinuar);
    }
}
