/**
 * Game.java
 */

package com.example.kerim.noktakutu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.IntentFilter;
import com.example.kerim.noktakutu.Firebase_Database.InventorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OyunActivityTekKisilik extends MainActivity {
    private static final String LOG_TAG = "Otomatik internet Kontrol¸";
    private NetworkChangeReceiver receiver;//Network dinleyen receiver objemizin referans˝

    private final String TAG = getClass().getSimpleName();
    private DatabaseReference myRef;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    //gameparam oyun noktalarının geldiği layout
    //mainparam ana ekran default görünüm


     String oyuncu1,oyuncu1isim;
     String oyuncu2;
    public static OyunGoruntu goruntu;
    float yogunluk;
    public static int sayac = 1;
    public Button kazandin, kaybettin,berabere;
    public static LinearLayout buttonViewkazandin,buttonViewkaybettin,buttonViewberabere;
    LinearLayout.LayoutParams buttonParam;
    LinearLayout.LayoutParams buttonsParam;
    public LinearLayout linearLayout;
   public LinearLayout.LayoutParams gameParam;




    private void ekranıOlustur() {

        kazandin = new Button(this);
        kaybettin = new Button(this);
        berabere = new Button(this);
        buttonViewkazandin = new LinearLayout(this);
        buttonViewkaybettin = new LinearLayout(this);
        buttonViewberabere = new LinearLayout(this);
        linearLayout = new LinearLayout(this);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.secim_arkaplan1);
        LinearLayout.LayoutParams mainParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        gameParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        gameParam.gravity = Gravity.BOTTOM;

        gameParam.weight = 0.5f;


        kazandin.setText("Sonraki Tur");
        kazandin.setClickable(false);
        kazandin.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sayac = sayac + 1;
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                startActivity(intent);
            }
        });

        berabere.setText("Tekrar Oyna");
        berabere.setClickable(false);
        berabere.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                OyunActivityTekKisilik.goruntu.temizle();
                OyunActivityTekKisilik.goruntu.ekranSifirla();
                OyunActivityTekKisilik.buttonViewberabere.setVisibility(View.INVISIBLE);
            }
        });


        kaybettin.setText("Ana Menu");
        kaybettin.setClickable(false);
        kaybettin.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sayac=1;
                Intent intent = new Intent(getApplicationContext(), OyunSecimActivity.class);
                startActivity(intent);
            }
        });

        //Buton Görünümü

        buttonViewkazandin.setOrientation(LinearLayout.HORIZONTAL);
        buttonViewkaybettin.setOrientation(LinearLayout.HORIZONTAL);
        buttonViewberabere.setOrientation(LinearLayout.HORIZONTAL);
        // Create layout parameters for the buttons.
        buttonsParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        buttonsParam.weight = 0.5f;
        buttonViewkazandin.setPadding(0, 0, 0, (int) (10 * this.yogunluk));
        buttonViewkaybettin.setPadding(0, 0, 0, (int) (10 * this.yogunluk));
       buttonViewberabere.setPadding(0, 0, 0, (int) (10 * this.yogunluk));

        buttonViewkazandin.addView(kazandin, buttonsParam);
        buttonViewkazandin.setVisibility(View.INVISIBLE);

        buttonViewkaybettin.addView(kaybettin, buttonsParam);
        buttonViewkaybettin.setVisibility(View.INVISIBLE);

        buttonViewberabere.addView(berabere, buttonsParam);
        buttonViewberabere.setVisibility(View.INVISIBLE);


        // Create layout parameters for the buttonViewkazandin.
        buttonParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        buttonParam.gravity = Gravity.BOTTOM;

        // Add the views to our main view.
        linearLayout.addView(goruntu, gameParam);
        linearLayout.addView(buttonViewberabere, buttonParam);
        linearLayout.addView(buttonViewkazandin, buttonParam);
        linearLayout.addView(buttonViewkaybettin, buttonParam);


        // Show the view to the user.
        this.setContentView(linearLayout, mainParam);

    }

    /**
     * When the activity is launched.
     *
     * @param savedInstanceState Saved instances being loaded.
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Receiverımızı register ediyoruz
        //Yani Çalıştırıyoruz


        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/d-puntillas-A-Lace.ttf");
        // Get values from the settings class.
        this.oyuncu1 = (this.getIntent().getStringExtra("1.Oyuncu"));
        this.oyuncu2 = (this.getIntent().getStringExtra("Bilgisayar"));
        int renk1 = (this.getIntent().getIntExtra("renk1", Color.BLUE));
        int renk2 = (this.getIntent().getIntExtra("renk2", Color.RED));
        boolean usingBot = (this.getIntent().getBooleanExtra("usingBot", true));
        if (usingBot) {

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            this.oyuncu1 =OyunSecimActivity.kullaniciadi;
            this.oyuncu2 = "Bilgisayar";

        }
        String ekranBoyutu = (this.getIntent().getStringExtra("ekranBoyutu"));

        // Display setup for later use.
        this.yogunluk = this.getResources().getDisplayMetrics().density;

        // Create the view which contains the game.
        goruntu = new OyunGoruntu(this, this.oyuncu1, this.oyuncu2, renk1, renk2, ekranBoyutu, usingBot);
        // Create the layout for the main view.
        this.ekranıOlustur();



        kazandin.setTextSize(20);
        kazandin.setTypeface(face1);

        kaybettin.setTextSize(20);
        kaybettin.setTypeface(face1);

        berabere.setTextSize(20);
        berabere.setTypeface(face1);
       //gameParam=new LinearLayout.LayoutParams(0, 0);
       //linearLayout.addView(goruntu, gameParam);




    }



    /**
     * When the application restores its state we want it to call this method. This method restores the game logic so it is not reset.
     *
     * @param savedInstanceState The Bundle containing the instance state.
     * @see android.app.Activity#onRestoreInstanceState(Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Set the goruntu instance logic instance.
        OyunActivityTekKisilik.goruntu.setOyun(savedInstanceState.getSerializable("logic"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * When the application saves its state we want it to call this method. This method backs up the game logic so it is not reset.
     *
     * @param outState The Bundle containing the instance state.
     * @see android.app.Activity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Get the OyunKontrol instance from the goruntu.
        outState.putSerializable("logic", OyunActivityTekKisilik.goruntu.getOyun());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        this.finish();
        Intent intent=new Intent(OyunActivityTekKisilik.this,OyunSecimActivity.class);
        startActivity(intent);


        return super.onKeyDown(keyCode, event);
    }


}