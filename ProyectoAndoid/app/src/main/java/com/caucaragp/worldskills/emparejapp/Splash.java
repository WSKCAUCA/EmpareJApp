package com.caucaragp.worldskills.emparejapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import com.caucaragp.worldskills.emparejapp.controllers.Menu;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.caucaragp.worldskills.emparejapp.controllers.Juego;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);
       // inicializar();
       // keyHash();
       // compartirFacebook();


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Menu.class);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask,2000);

    }

    private void compartirFacebook() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setQuote("Prueba texto")
                .setContentUrl(Uri.parse("https://www.google.ca/")).build();

        if (shareDialog.canShow(ShareLinkContent.class)){

            shareDialog.show(content);
        }

    }

    //Referenciamos los campos que vamos a utilizar
    private void inicializar() {

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

    }

    //Mentodo el cual nos ayuda a obtener la KeyHash para la api de Facebook
    private void keyHash() {

        try {

            PackageInfo info = getPackageManager().getPackageInfo("com.caucaragp.worldskills.emparejapp", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
