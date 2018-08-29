package com.caucaragp.worldskills.emparejapp.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caucaragp.worldskills.emparejapp.R;
import com.caucaragp.worldskills.emparejapp.models.AdapterJ;

import java.util.ArrayList;
import java.util.List;

public class Juego extends AppCompatActivity {
    //Declaración de variables
    private int fondoJuego = R.drawable.cartel;
    private int [] imagenesJuego ={R.drawable.butters, R.drawable.cartman, R.drawable.craig,
            R.drawable.jimmy, R.drawable.kenny, R.drawable.kyle, R.drawable.stan, R.drawable.wendy,
    };
    private int [] imagenesFondo, imagenesAleatorias;
    private List<Integer> imagenesSelect = new ArrayList<>();
    int movimientos, pos1=-1, pos2=-1, canselect, nivel, salir, item, columnas, tiempo;
    int inicioJuego, modoJuego, puntacion1, puntuacion2, ab=0;
    boolean bandera = true, bandera1=true;
    TextView txtJugador1, txtJugador2, txtPuntaje1, txtPuntaje2, txtTiempo;
    ProgressBar pTiempo;
    RecyclerView contenedorJuego;
    ImageView imagen1, imagen2;
    View item1, item2;
    int [] segundos;
    SharedPreferences juegoC;
    Animator animator1, animator2;
    MediaPlayer win,lose,end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        inizialite();
        inputValues();
        turns();
        goGame();
        inputAdapter();
    }

    //Método para inicializar las vistas e inizializar los MediaPlayer
    private void inizialite() {
        txtJugador1 = findViewById(R.id.txtJugador1);
        txtJugador2 = findViewById(R.id.txtJugador2);
        txtPuntaje1 = findViewById(R.id.txtPuntaje1);
        txtPuntaje2 = findViewById(R.id.txtPuntaje2);
        txtTiempo = findViewById(R.id.txtTiempo);

        pTiempo = findViewById(R.id.pTiempo);
        contenedorJuego = findViewById(R.id.contenedorJuego);

        win= MediaPlayer.create(this,R.raw.win);
        lose= MediaPlayer.create(this,R.raw.lose);
        end= MediaPlayer.create(this,R.raw.end);
    }

    //Método para ingresar variables a los campos de texto, definir el nivel, definir el modo de juego y otros
    private void inputValues() {
        juegoC = getSharedPreferences("juegoC",MODE_PRIVATE);
        nivel = 8;//por ahora
        modoJuego = juegoC.getInt("modo",1);
        ab=0;

        if (modoJuego==1) {
            tiempo=30;
            segundos= new int[]{0};
            txtTiempo.setText("Tiempo gastado: "+segundos[0]);
            pTiempo.setMax(tiempo);
            pTiempo.setProgress(segundos[0]);
        }else {
            tiempo = juegoC.getInt("tiempo",30);
            segundos= new int[]{tiempo};
            txtTiempo.setText("Tiempo restante: "+segundos[0]);
            pTiempo.setMax(tiempo);
            pTiempo.setProgress(segundos[0]);
        }
        bandera = true;
        txtJugador1.setText("");//por ahora
        txtJugador2.setText("");//por ahora
        salir = nivel;



        if (nivel==4){
            item = R.layout.item_1;
            columnas=2;
        }


        if (nivel==6){
            item = R.layout.item_2;
            columnas=3;
        }

        if (nivel==8){
            item = R.layout.item_3;
            columnas=4;
        }

    }

    //Método para definir el turno aleatoriamente
    private void turns() {
        inicioJuego  = (int) (Math.random() * 2)+1;
        if (inicioJuego==1){
            txtJugador1.setTextColor(getColor(R.color.colorNegro));
            txtJugador2.setTextColor(getColor(R.color.colorGris));
            Toast.makeText(this, "Empieza Jugador 1", Toast.LENGTH_SHORT).show();//Por ahora
        }else {
            txtJugador1.setTextColor(getColor(R.color.colorNegro));
            txtJugador2.setTextColor(getColor(R.color.colorGris));
            Toast.makeText(this, "Empieza Jugador 2", Toast.LENGTH_SHORT).show();//Por ahora
        }
    }

    //Método que da inicio a otros métodos para iniciar el juego
    private void goGame() {
        generarFondo();
        generarSelect();
        generarAleatorias();
        chronometer();
    }


    //Método para ingresar al vector imagenesFondo la variable fondoJuego para el reverso de los items
    private void generarFondo() {
        imagenesFondo = new int[nivel*2];
        for (int i=0; i<imagenesFondo.length;i++){
            imagenesFondo[i] = fondoJuego;
        }
    }

    //Método para generar las imagenes que se mostrar en la seleción, esto se hará aleatoriamente
    private void generarSelect() {
        imagenesSelect = new ArrayList<>();
        for (int i=0; i<nivel;i++){
            int tmp = (int) (Math.random() * imagenesJuego.length);
            if (!imagenesSelect.contains(imagenesJuego[tmp])){
                imagenesSelect.add(imagenesJuego[tmp]);
            }else {
                i--;
            }
        }

    }

    //Método para generar las posicisiones aleatorias para cada pareja de imagenes
    private void generarAleatorias() {
        imagenesAleatorias = new int[nivel*2];
        for (int i=0; i<nivel;i++){
            int tmp =0;
            do {
                int valor = (int) (Math.random() * nivel*2);
                if (imagenesAleatorias[valor]==0){
                    imagenesAleatorias[valor] = imagenesSelect.get(i);
                    tmp++;
                }

            }while (tmp<2);
        }
    }

    //Método para generar el thread y correrlo
    private void chronometer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (bandera){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bandera1){
                                if (modoJuego==1){
                                    segundos[0]++;
                                    txtTiempo.setText("Tiempo gastado: "+segundos[0]);
                                    pTiempo.setMax(tiempo);
                                    pTiempo.setProgress(segundos[0]);
                                }else {
                                    segundos[0]--;
                                    txtTiempo.setText("Tiempo restante: "+segundos[0]);
                                    pTiempo.setMax(tiempo);
                                    pTiempo.setProgress(segundos[0]);
                                    endGame();
                                }
                            }
                        }
                    });
                }
            }
        });
        thread.start();
    }

    //Método para finalizar el juego cuando se acaba el tiempo (en modo con tiempo)
    private void endGame() {
        if (ab==0 && segundos[0]==0){
            bandera=false;
            bandera1=false;


        }
    }

    //Método para ingresar el adaptador al contenedor llamado conetedorJuego
    private void inputAdapter() {
        AdapterJ adapterJ = new AdapterJ(imagenesFondo,item,this);
        contenedorJuego.setAdapter(adapterJ);
        contenedorJuego.setLayoutManager(new GridLayoutManager(this,columnas,GridLayoutManager.VERTICAL,false));
        contenedorJuego.setHasFixedSize(true);

        adapterJ.setOnItemClickListener(new AdapterJ.OnItemClickListener() {
            @Override
            public void itemClick(int position, ImageView imageView, View itemView) {
                canselect++;
                if (pos1!=position || pos2!=position){
                    canselect--;
                }

                if (canselect==1){
                    pos1=position;
                    imagen1 = imageView;
                    item1 = itemView;
                    item1.setEnabled(false);
                    animator1 = ViewAnimationUtils.createCircularReveal(imagen1, 0, imagen1.getHeight(),0, imagen1.getHeight()*1.5f);
                    animator1.setDuration(400);
                    animator1.start();

                }

                if (canselect==2){
                    pos2=position;
                    imagen2 = imageView;
                    item2 = itemView;
                    item2.setEnabled(false);
                    animator2 = ViewAnimationUtils.createCircularReveal(imagen2, 0, imagen2.getHeight(),0, imagen2.getHeight()*1.5f);
                    animator2.setDuration(400);
                    animator2.start();

                }

                mostrarImagen(position,imageView);

            }
        });
    }

    //Método para visualizar la imagen cuando el item es seleccionado
    private void mostrarImagen(int position, ImageView imageView) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize=4;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imagenesAleatorias[position],op);
        imageView.setImageBitmap(bitmap);
        if (canselect==2){
            movimientos++;
            new ValidarJuego().execute();
            AdapterJ.bandera=false;
        }

    }

    public class ValidarJuego extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            AdapterJ.bandera=false;
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (imagenesAleatorias[pos1]==imagenesAleatorias[pos2]){
                animator1 = ViewAnimationUtils.createCircularReveal(imagen1,imagen1.getHeight()/2,imagen1.getHeight()/2,imagen1.getHeight(),0);
                animator1.setDuration(300);
                animator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imagen1.setVisibility(View.INVISIBLE);
                        imagen1=null;
                        item1=null;
                    }
                });

                animator2 = ViewAnimationUtils.createCircularReveal(imagen2,imagen2.getHeight()/2,imagen2.getHeight()/2,imagen2.getHeight(),0);
                animator2.setDuration(300);
                animator2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imagen2.setVisibility(View.INVISIBLE);
                        imagen2=null;
                        item2=null;
                        canselect=0;
                        pos1=-1;
                        pos2=-1;
                        AdapterJ.bandera=true;

                    }
                });
                win.start();
                salir--;


            }else {

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bandera=false;
        bandera1 = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        bandera1=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        bandera1=false;
    }
}
