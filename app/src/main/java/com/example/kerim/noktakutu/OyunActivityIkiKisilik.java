/**
 * Game.java
 */

package com.example.kerim.noktakutu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.kerim.noktakutu.Firebase_Database.EnIyiOyuncular;

public class OyunActivityIkiKisilik extends MainActivity {

  private String oyuncu1;
  private String oyuncu2;
  private static OyunGoruntuIkiKisilik goruntu;
  float yogunluk;

  private void ekranıOlustur() {

    LinearLayout linearLayout = new LinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);
    linearLayout.setBackgroundResource(R.drawable.secim_arkaplan1);

    LinearLayout.LayoutParams mainParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);


    LinearLayout.LayoutParams gameParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    gameParam.gravity = Gravity.BOTTOM;
    gameParam.weight = 0.5f;

    Button btnTemizle = new Button(this);
    btnTemizle.setText("Temizle");
    btnTemizle.setOnClickListener(new Button.OnClickListener() {

      @Override
      public void onClick(View arg0) {
        goruntu.temizle();

      }
    });

    Button kaydet = new Button(this);
    kaydet.setText("Kayıtlar");
    kaydet.setClickable(false);

    kaydet.setOnClickListener(new Button.OnClickListener() {

      @Override
      public void onClick(View arg0) {

        Intent intent=new Intent(getApplicationContext(),EnIyiOyuncular.class);
        startActivity(intent);
      }
    });



    //Buton Görünümü
    LinearLayout buttonView = new LinearLayout(this);
    buttonView.setOrientation(LinearLayout.HORIZONTAL);

    // Create layout parameters for the buttons.
    LinearLayout.LayoutParams buttonsParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    buttonsParam.weight = 0.5f;
    buttonView.setPadding(0, 0, 0, (int) (10 * this.yogunluk));

    // Add the buttons to the inner view.
    buttonView.addView(btnTemizle, buttonsParam);
    buttonView.addView(kaydet, buttonsParam);


    // Create layout parameters for the buttonViewkazandin.
    LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    buttonParam.gravity = Gravity.BOTTOM;

    // Add the views to our main view.
    linearLayout.addView(goruntu, gameParam);
    linearLayout.addView(buttonView, buttonParam);

    // Show the view to the user.
    this.setContentView(linearLayout, mainParam);
  }

  /**
   * When the activity is launched.
   * 
   * @param savedInstanceState
   *          Saved instances being loaded.
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Get values from the settings class.
    this.oyuncu1 = (this.getIntent().getStringExtra("1.Oyuncu"));
    this.oyuncu2 = (this.getIntent().getStringExtra("2.Oyuncu"));
    int renk1 = (this.getIntent().getIntExtra("renk1", Color.BLACK));
    int renk2 = (this.getIntent().getIntExtra("renk2", Color.BLACK));
    boolean usingBot = (this.getIntent().getBooleanExtra("usingBot", false));
    if (usingBot) {
      this.oyuncu2 = "Bilgisayar";
    }
    String ekranBoyutu = (this.getIntent().getStringExtra("ekranBoyutu"));

    // Display setup for later use.
    this.yogunluk = this.getResources().getDisplayMetrics().density;

    // Create the view which contains the game.
    goruntu = new OyunGoruntuIkiKisilik(this, this.oyuncu1, this.oyuncu2, renk1, renk2, ekranBoyutu, usingBot);
    // Create the layout for the main view.
    this.ekranıOlustur();


  }

  /**
   * When the application restores its state we want it to call this method. This method restores the game logic so it is not reset.
   *
   * @param savedInstanceState
   *          The Bundle containing the instance state.
   *
   * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
   */
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    // Set the goruntu instance logic instance.
    OyunActivityIkiKisilik.goruntu.setOyun(savedInstanceState.getSerializable("logic"));
    super.onRestoreInstanceState(savedInstanceState);
  }

  /**
   * When the application saves its state we want it to call this method. This method backs up the game logic so it is not reset.
   *
   * @param outState
   *          The Bundle containing the instance state.
   *
   * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    // Get the OyunKontrol instance from the goruntu.
    outState.putSerializable("logic", OyunActivityIkiKisilik.goruntu.getOyun());
    super.onSaveInstanceState(outState);
  }


  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {

    this.finish();
    Intent intent=new Intent(OyunActivityIkiKisilik.this,OyunSecimActivity.class);
    startActivity(intent);


    return super.onKeyDown(keyCode, event);
  }
}
