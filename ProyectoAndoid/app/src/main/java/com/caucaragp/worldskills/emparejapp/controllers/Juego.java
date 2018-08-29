package com.caucaragp.worldskills.emparejapp.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caucaragp.worldskills.emparejapp.R;
import com.caucaragp.worldskills.emparejapp.models.AdapterJ;
import com.caucaragp.worldskills.emparejapp.models.GestorDB;
import com.caucaragp.worldskills.emparejapp.models.Score;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;

public class Juego extends AppCompatActivity {
    //Declaración de variables
    private int fondoJuego = R.drawable.fondoboton;
    private int [] imagenesJuego ={R.drawable.butters, R.drawable.cartman, R.drawable.craig,
            R.drawable.jimmy, R.drawable.kenny, R.drawable.kyle, R.drawable.stan, R.drawable.wendy,
    };
    private int [] imagenesFondo, imagenesAleatorias;
    private List<Integer> imagenesSelect = new ArrayList<>();

    int movimientos, pos1=-1, pos2=-1, canselect, nivel, salir, item, columnas, tiempo, inicioJuego, modoJuego, puntuacion1, puntuacion2, ab=0;
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

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_juego);
        inizialite();
        inputValues();
        turns();
        goGame();
        inputAdapter();
    }

    //Método para inicializar las vistas e inizializar los MediaPlayer
    private void inizialite() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

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
        nivel = Menu.nivel;
        modoJuego = juegoC.getInt("modo",1);
        ab=0;

        if (modoJuego==1) {
            tiempo=30;
            segundos= new int[]{0,0};
            txtTiempo.setText("Tiempo gastado: "+segundos[0]);
            pTiempo.setMax(tiempo);
            pTiempo.setProgress(segundos[0]);
        }else {
            tiempo = juegoC.getInt("tiempo",30);
            segundos= new int[]{tiempo,0};
            txtTiempo.setText("Tiempo restante: "+segundos[0]);
            pTiempo.setMax(tiempo);
            pTiempo.setProgress(segundos[0]);
        }
        bandera = true;
        txtJugador1.setText(Inicio.jugador1);
        txtJugador2.setText(Inicio.jugador2);
        salir = nivel;
        puntuacion1 = 0;
        puntuacion2 = 0;



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
            colorearAJugador1();
            Toast.makeText(this, "Empieza Jugador 1", Toast.LENGTH_SHORT).show();//Por ahora
        }else {
            colorearAJugador2();
            Toast.makeText(this, "Empieza Jugador 2", Toast.LENGTH_SHORT).show();//Por ahora
        }
    }

    //Método para colorear de negro al jugador 1 cuando es su turno y descolorear al otro porque no es su turno
    private void colorearAJugador1(){
        txtJugador1.setTextColor(getColor(R.color.colorNegro));
        txtJugador2.setTextColor(getColor(R.color.colorGris));
        txtPuntaje1.setTextColor(getColor(R.color.colorNegro));
        txtPuntaje2.setTextColor(getColor(R.color.colorGris));
    }

    //Método para colorear de negro al jugador 2 cuando es su turno y descolorear al otro porque no es su turno
    private void colorearAJugador2(){
        txtJugador2.setTextColor(getColor(R.color.colorNegro));
        txtJugador1.setTextColor(getColor(R.color.colorGris));
        txtPuntaje2.setTextColor(getColor(R.color.colorNegro));
        txtPuntaje1.setTextColor(getColor(R.color.colorGris));
    }

    //Método para ingresar los puntajes a su respectivas vistas
    private void inputPoints(){
        txtPuntaje1.setText(Integer.toString(puntuacion1));
        txtPuntaje2.setText(Integer.toString(puntuacion2));
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

            final Dialog dialog = new Dialog(Juego.this);
            dialog.setContentView(R.layout.item_resumen);
            TextView txtNombreJ1 = dialog.findViewById(R.id.txtJugador1R);
            TextView txtNombreJ2 = dialog.findViewById(R.id.txtJugador2R);
            TextView txtPuntajeJ1 = dialog.findViewById(R.id.txtPuntaje1R);
            TextView txtPuntajeJ2 = dialog.findViewById(R.id.txtPuntaje2R);
            TextView txtTiempoR = dialog.findViewById(R.id.txtTiempoR);
            Button btnContinuar = dialog.findViewById(R.id.btnContinuar);

            txtNombreJ1.setText(txtJugador1.getText().toString());
            txtNombreJ2.setText(txtJugador2.getText().toString());
            txtPuntajeJ1.setText(txtPuntaje1.getText().toString());
            txtPuntajeJ2.setText(txtPuntaje2.getText().toString());
            txtTiempoR.setText(txtTiempo.getText().toString());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            btnContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    dialog.cancel();
                }
            });

            dialog.show();

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
                if (pos1==position || pos2==position){
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
                Thread.sleep(1000);
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

                animator1.start();animator2.start();
                win.start();
                salir--;

                if (inicioJuego==1){
                    puntuacion1+=100;
                    inputPoints();
                }else {
                    puntuacion2+=100;
                    inputPoints();
                }

                //Validación que nos permite mostrar resumen y guardar en base de datos los datos del juego si está en modo sin tiempo
                if (salir==0){
                    win.stop();
                    end.start();
                    bandera=false;
                    bandera1=false;

                    if (modoJuego==1){
                        Score score1 = new Score();
                        Score score2 = new Score();

                        
                        score1.setPuntaje(puntuacion1);
                        score1.setNivel(nivel);
                        score2.setPuntaje(puntuacion2);
                        score2.setNivel(nivel);

                        GestorDB gestorDB = new GestorDB(Juego.this);
                        gestorDB.insertScore(score1);
                        gestorDB.insertScore(score2);
                    }

                    final Dialog dialog = new Dialog(Juego.this);
                    dialog.setContentView(R.layout.item_resumen);
                    TextView txtNombreJ1 = dialog.findViewById(R.id.txtJugador1R);
                    TextView txtNombreJ2 = dialog.findViewById(R.id.txtJugador2R);
                    TextView txtPuntajeJ1 = dialog.findViewById(R.id.txtPuntaje1R);
                    TextView txtPuntajeJ2 = dialog.findViewById(R.id.txtPuntaje2R);
                    TextView txtTiempoR = dialog.findViewById(R.id.txtTiempoR);
                    Button btnContinuar = dialog.findViewById(R.id.btnContinuar);
                    Button btnFace = dialog.findViewById(R.id.btnFace);
                    Button btnTwi = dialog.findViewById(R.id.btnTwi);

                    String dificultad="";

                    if (nivel==4){
                        dificultad= getString(R.string.facil);
                    }

                    if (nivel==6){
                        dificultad= getString(R.string.medio);
                    }

                    if (nivel==8){
                        dificultad= getString(R.string.dificil);
                    }

                    final String messege = Inicio.jugador1+" puntaje: "+puntuacion1+"\n"+
                                    Inicio.jugador2+" puntaje: "+puntuacion2+"\n"+
                            "Dificultad: "+dificultad+ "\n"+
                            txtTiempo.getText();
                    txtNombreJ1.setText(txtJugador1.getText().toString());
                    txtNombreJ2.setText(txtJugador2.getText().toString());
                    txtPuntajeJ1.setText(txtPuntaje1.getText().toString());
                    txtPuntajeJ2.setText(txtPuntaje2.getText().toString());
                    txtTiempoR.setText(txtTiempo.getText().toString());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btnContinuar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            dialog.cancel();
                        }
                    });

                    btnFace.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShareLinkContent content = new ShareLinkContent.Builder()
                                    .setQuote(messege)
                                    .setContentUrl(Uri.parse("https://www.google.ca/")).build();

                            if (shareDialog.canShow(ShareLinkContent.class)){

                                shareDialog.show(content);
                            }
                        }
                    });

                    btnTwi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            
                        }
                    });

                    dialog.show();


                }




            }else {
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inSampleSize=2;
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),fondoJuego,op);
                animator1 = ViewAnimationUtils.createCircularReveal(imagen1,imagen1.getHeight()/2,imagen1.getHeight()/2,imagen1.getHeight(),0);
                animator1.setDuration(300);
                animator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imagen1.setImageBitmap(bitmap);
                        Animator animator = ViewAnimationUtils.createCircularReveal(imagen1,0,imagen1.getHeight(), 0,imagen1.getHeight()*1.5f);
                        animator.setDuration(400);
                        animator.start();
                    }
                });


                animator2 = ViewAnimationUtils.createCircularReveal(imagen2,imagen2.getHeight()/2,imagen2.getHeight()/2,imagen2.getHeight(),0);
                animator2.setDuration(300);
                animator2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imagen2.setImageBitmap(bitmap);

                        Animator animator = ViewAnimationUtils.createCircularReveal(imagen2,0,imagen2.getHeight(), 0,imagen2.getHeight()*1.5f);
                        animator.setDuration(400);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                item1.setEnabled(true);
                                item2.setEnabled(true);
                                canselect=0;
                                pos1=-1;
                                pos2=-1;
                                AdapterJ.bandera=true;
                            }
                        });
                        animator.start();
                    }
                });

                animator1.start();animator2.start();

                lose.start();

                if (inicioJuego==1){
                    if (puntuacion1>0) {
                        puntuacion1 -= 2;
                        inputPoints();
                    }
                    inicioJuego=2;
                    colorearAJugador2();
                }else {
                    if (puntuacion2>0) {
                        puntuacion2 -= 2;
                        inputPoints();
                    }
                    inicioJuego=1;
                    colorearAJugador1();
                }






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
