package com.caucaragp.worldskills.emparejapp.controllers;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.caucaragp.worldskills.emparejapp.R;

public class Menu extends AppCompatActivity implements View.OnClickListener{
    Button btnJugar, btnPuntuacion, btnConfiguracion;
    public static int nivel=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        inicializar();
        escucharBotones();
    }

    //Método para inicializar las vistas
    private void inicializar() {
        btnJugar = findViewById(R.id.btnJugar);
        btnPuntuacion = findViewById(R.id.btnPuntuacion);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);
    }

    //Método para ingresar el setOnClickListener a los botones
    private void escucharBotones() {
        btnJugar.setOnClickListener(this);
        btnPuntuacion.setOnClickListener(this);
        btnConfiguracion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnJugar:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.item_nivel);
                dialog.setCancelable(true);
                final RadioButton rbtnFacil = dialog.findViewById(R.id.rbtnFacil);
                final RadioButton rbtnMedio = dialog.findViewById(R.id.rbtnMedio);
                final RadioButton rbtnDificil = dialog.findViewById(R.id.rbtnDificil);
                Button btnEmpezar = dialog.findViewById(R.id.btnEmpezar);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btnEmpezar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Menu.this,Juego.class);
                        if (rbtnFacil.isChecked()){
                            nivel=4;
                            startActivity(intent);
                        }

                        if (rbtnMedio.isChecked()){
                            nivel=6;
                            startActivity(intent);
                        }

                        if (rbtnDificil.isChecked()){
                            nivel=8;
                            startActivity(intent);
                        }

                        dialog.cancel();



                    }
                });

                dialog.show();

                break;

            case R.id.btnPuntuacion:
                break;

            case R.id.btnConfiguracion:
                break;

        }
    }
}
