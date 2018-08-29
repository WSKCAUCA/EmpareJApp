package com.caucaragp.worldskills.emparejapp.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GestorDB extends SQLiteOpenHelper {
    public GestorDB(Context context) {
        super(context, Constants.DATABASE_NAME, null,Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.TABLE_SCORE);

    }

    //Método para ingresar los datos a la tabla SCORE  de la base de datos empareja.db
    public void insertScore(Score score){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("JUGADOR",score.getJugador());
        values.put("PUNTAJE",score.getPuntaje());
        values.put("NIVEL",score.getNivel());
        db.close();
        db.insert("SCORE",null,values);
    }

    //Método para listar los puntajes más altos dependiendo del valor de entrada del nivel que están registrados en la base de datos
    public List<Score> listScore(int nivel){
        List<Score> results = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SCORE WHERE NIVEL="+nivel+" ORDER BY PUNTAJE DESC",null);
        if (cursor.moveToFirst()){
            do {
                Score score = new Score();
                score.setJugador(cursor.getString(0));
                score.setPuntaje(cursor.getInt(1));
                score.setNivel(cursor.getInt(2));
                results.add(score);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return results;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
