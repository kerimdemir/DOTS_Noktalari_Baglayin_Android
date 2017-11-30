package com.example.kerim.noktakutu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;

public class IkiKisilikOyun extends Activity {

    private EditText oyuncu1;
    private EditText oyuncu2;

    private Spinner spOyuncu1;
    private Spinner spOyuncu2;
    private ArrayList<String> renkler;
    private Button btnBasla;

    public void onButtonClick(View view) {


        String oyuncu1 = this.oyuncu1.getText().toString();
        String oyuncu2 = this.oyuncu2.getText().toString();
        String spOyuncu1 = this.spOyuncu1.getSelectedItem().toString();
        String spOyuncu2 = this.spOyuncu2.getSelectedItem().toString();


        //Ikinci renk ile bir aynıysa değiştir
        if (spOyuncu1.equalsIgnoreCase(spOyuncu2)) {
            //Show an error message and don't do any more.
            new AlertDialog.Builder(this).setTitle("Renk Seçiniz").setMessage("Renk Seçili Başka renk seçiniz.")
                    .setPositiveButton("Tamam", null).show();
        } else {
            //Some data validation on the name entered.
            if (oyuncu1.toString().length() == 0) oyuncu1 = "1.Oyuncu";
            if (oyuncu2.toString().length() == 0) oyuncu2 = "2.Oyuncu";
            if (oyuncu1.length() > 15) oyuncu1 = oyuncu1.substring(0, 15);
            if (oyuncu2.length() > 15) oyuncu2 = oyuncu2.substring(0, 15);

            // Create the new intent to load the game interface.
            Intent intent = new Intent(this, OyunActivityIkiKisilik.class);
            // Put our values into the Intent
            intent.putExtra("1.Oyuncu", oyuncu1);
            intent.putExtra("2.Oyuncu", oyuncu2);
            intent.putExtra("renk1", Color.parseColor(spOyuncu1));
            intent.putExtra("renk2", Color.parseColor(spOyuncu2));

            // Start the intent
            this.startActivity(intent);
        }
    }

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     * @see MainActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.ikikisilikoyun);
        this.spinneriDoldur();

        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/d-puntillas-E-Tiptoes-squid.ttf");
        TextView txtView1=(TextView)findViewById(R.id.textview1);
        TextView txtView2=(TextView)findViewById(R.id.textview2);
        btnBasla= (Button) findViewById(R.id.btnYeniOyun);
        txtView1.setTypeface(face1);
        txtView2.setTypeface(face1);
        btnBasla.setTypeface(face1);


    }
    /**
     * Launched when the play against computer tick box is pressed.
     *
     * @param view The view that was pressed.
     */


    /**
     * This method sets up the spinners and fills them with colours.
     */
    private void spinneriDoldur() {
        // Colours we want to show in the spinners.
        String colours[] = {this.getString(R.string.red), this.getString(R.string.blue)};
        // Link the fields with the XML files.
        this.oyuncu1 = (EditText) this.findViewById(R.id.edit_oyuncu1);
        this.oyuncu2 = (EditText) this.findViewById(R.id.edit_oyuncu2);
        this.spOyuncu1 = (Spinner) this.findViewById(R.id.spinner_oyuncu1);
        this.spOyuncu2 = (Spinner) this.findViewById(R.id.spinner_oyuncu2);
        // Clear the renkler
        this.renkler = new ArrayList<String>();
        this.renkler.clear();
        // Fill the list with colours but make sure we dont get multiples.
        for (int i = 0; i < colours.length; i++) {
            if (!this.renkler.contains(colours[i])) {
                this.renkler.add(colours[i]);
            }
            // Create an adapter using the renkler.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.renkler);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Set both spinners to use the colour array as its list.
            this.spOyuncu1.setAdapter(adapter);
            this.spOyuncu2.setAdapter(adapter);
            // Set the default selection for second spinner to blue.
            this.spOyuncu2.setSelection(1);
        }
    }

}
