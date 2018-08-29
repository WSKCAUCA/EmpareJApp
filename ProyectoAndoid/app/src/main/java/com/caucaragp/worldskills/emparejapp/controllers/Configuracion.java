package com.caucaragp.worldskills.emparejapp.controllers;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.caucaragp.worldskills.emparejapp.R;

public class Configuracion extends AppCompatActivity implements View.OnClickListener{
    RadioButton rbtnConTiempo, rbtnSinTiempo;
    EditText txtTiempo;
    Button btnContinuar;
    SharedPreferences juegoC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        inizialite();
        inputData();
        escucharBotones();

    }

    //Método para inizializar las vistas y el SharedPreferences

    private void inizialite() {
        rbtnConTiempo = findViewById(R.id.btnConTiempo);
        rbtnSinTiempo = findViewById(R.id.btnSinTiempo);
        txtTiempo = findViewById(R.id.txtTiempo);
        btnContinuar = findViewById(R.id.btnContinuar);
        juegoC = getSharedPreferences("juegoC",MODE_PRIVATE);
    }

    //Método para poder ingresar en el setOnClickListener el escuchador
    private void escucharBotones() {
        rbtnSinTiempo.setOnClickListener(this);
        rbtnConTiempo.setOnClickListener(this);
        btnContinuar.setOnClickListener(this);
    }

    //Método para cargar las preferencias
    private void inputData() {
        int modo = juegoC.getInt("modo",1);
        int tiempo = juegoC.getInt("tiempo",30);
        if (modo==1){
            rbtnSinTiempo.setChecked(true);
            txtTiempo.setEnabled(false);
        }else {
            rbtnConTiempo.setChecked(true);
            txtTiempo.setEnabled(true);
        }
        txtTiempo.setText(Integer.toString(tiempo));
    }

    //Método para guardar las preferencias de juego
    private void savePreferences(){
        SharedPreferences.Editor editor = juegoC.edit();
        if (rbtnSinTiempo.isChecked()){
            editor.putInt("modo",1);
            editor.commit();
        }else {
            editor.putInt("modo",2);
            try{
                int tiempo = Integer.parseInt(txtTiempo.getText().toString());
                if (tiempo>9 && tiempo<121){
                    editor.putInt("tiempo",tiempo);
                }else {
                    Toast.makeText(this, "Por favor ingrese un número mayor a 9 segundos o menor 121 segundos. \n"+ "No se guardará el tiempo", Toast.LENGTH_SHORT).show();
                }


            }catch (Exception e){
                Toast.makeText(this, "Por favor no deje el campo vacio", Toast.LENGTH_SHORT).show();
                txtTiempo.setText("Por favor no deje este campo vacio." + "No se guardará el tiempo");

            }
            editor.commit();


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSinTiempo:
                txtTiempo.setEnabled(false);
                break;

            case R.id.btnConTiempo:
                txtTiempo.setEnabled(true);
                break;

            case R.id.btnContinuar:
                savePreferences();
                finish();
                break;
        }
    }
}
