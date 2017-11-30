

package com.example.kerim.noktakutu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

import static com.example.kerim.noktakutu.OyunKontrol.degeri;


public class OyunGoruntu extends View {

    public int kazanilan_puan;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private Paint nokta;
    private Paint kutu;
    private Paint txtSkor;
    private Paint baskiliCizgi;
    private Context icerik;
    private final float yogunluk;
    public OyunKontrol oyun;

    private Bilgisayar bot;
    private boolean bilgisayar;

    private AsyncTask<Void, Void, Void> task;
    private int width;
    private int height;
    private String oyuncu1;
    private String oyuncu2;
    public static String textOyuncu1;
    public static String textOyuncu2;
    public static String textOyuncu;
    OyunActivityTekKisilik gameParam;
    /**
     * The GameView object which is an extension of View. Creates what the player will see when playing the game. Controls all user
     * interaction with the game and passes information through to the game OyunKontrol.
     *
     * @param icerik      The current state of the application.
     * @param oyuncu1     The name of the player 1.
     * @param oyuncu2     The name of the player 2.
     * @param renk1       The colour chosen by player 1.
     * @param renk2       The colour chosen by player 2.
     * @param ekranBoyutu The size of the grid used for the game.
     * @param bilgisayar  If we are playing against the computer or not.
     */
    public OyunGoruntu(Context icerik, String oyuncu1, String oyuncu2, int renk1, int renk2, String ekranBoyutu, boolean bilgisayar) {
        super(icerik);

        // Create oyun controller using width and height of the view.
        WindowManager wm = (WindowManager) icerik.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        this.width = d.getWidth();
        this.height = d.getHeight();
        this.oyun = new OyunKontrol(ekranBoyutu, this.width, this.height, renk1, renk2);
        this.oyuncu1 = oyuncu1;
        this.oyuncu2 = oyuncu2;
        this.icerik = icerik;

        this.yogunluk = this.getResources().getDisplayMetrics().density;

        this.bilgisayar = bilgisayar;
        if (this.bilgisayar == true) {
            this.bot = new Bilgisayar(this.oyun);
        }
        //Set up the task running the Bilgisayar.
        this.setupTask();
        // Initialise paint objects.
        this.baskiliCizgi = new Paint();
        this.baskiliCizgi.setStrokeWidth(5 * this.yogunluk);
        this.kutu = new Paint();
        this.txtSkor = new Paint();
        this.txtSkor.setColor(Color.parseColor("#FFFFFF"));
        this.txtSkor.setTextSize(20 * this.yogunluk);
        this.txtSkor.setAntiAlias(true);
        this.nokta = new Paint();
        this.nokta.setColor(Color.WHITE);
        this.nokta.setAntiAlias(true);

    }

    /**
     * A method that is called when a user selects a line that has already been selected. They will be presented with a toast
     * notification and the screen will titresim.
     */
    private void kullanılmısAlan() {
        Toast toast = Toast.makeText(getContext(), "  Kullanılmış alan.  ", Toast.LENGTH_SHORT);

        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast);
        toast.show();
    }

    /**
     * Check the touch event which happened, determine which line was pressed.
     *
     * @param x The X coordinate pressed.
     * @param y The Y coordinate pressed.
     * @return
     */
    private void calculateLine(int x, int y) {

        //OyunKontrol variables.
        boolean dikeyBasiliCizgi = false;
        boolean yatayBasiliCizgi = false;
        Cizgi basiliCizgi = null;

        // If we are playing against the bot and it is the bots turn.
        if (this.bilgisayar == true && this.oyun.getPlayer() == 2) {
            this.rakipSira();
        } else {
            // Go through each vertical line.
            for (Cizgi[] dikeyCizgiler : this.oyun.getDikeyCizgi()) {
                for (Cizgi dikeyCizgi : dikeyCizgiler) {
                    // If the y value touched was between this lines Y values.
                    if (y > Math.abs(dikeyCizgi.getBaslangicNoktasi().getyDegeri()) && y < Math.abs(dikeyCizgi.getBitisNoktasi().getyDegeri())) {
                        // If the touch was between 10px left or right of the line.
                        if (Math.abs(dikeyCizgi.getBaslangicNoktasi().getxDegeri() - x) < 10 * this.yogunluk) {
                            dikeyBasiliCizgi = true;
                            basiliCizgi = dikeyCizgi;
                        }
                    }
                }
            }
            // Some optimisation, if we have already found a vertical line no point searching for a horizontal one.
            if (dikeyBasiliCizgi != true) {
                // Go through each horizontal line.
                for (Cizgi[] dikeyCizgiler : this.oyun.getDikeyCizgiler()) {
                    for (Cizgi dikeyCizgi : dikeyCizgiler) {
                        // If the x value touched was between this lines X values.
                        if (x > Math.abs(dikeyCizgi.getBaslangicNoktasi().getxDegeri()) && x < Math.abs(dikeyCizgi.getBitisNoktasi().getxDegeri())) {
                            // If the touch was between 10px below or above of the line.
                            if (Math.abs(dikeyCizgi.getBaslangicNoktasi().getyDegeri() - y) < 10 * this.yogunluk) {
                                yatayBasiliCizgi = true;
                                basiliCizgi = dikeyCizgi;
                            }
                        }
                    }
                }
            }

            //If a vertical Cizgi was pressed then do the veritcalPressed method.
            if (dikeyBasiliCizgi == true) {
                this.yatayBaskiliCizgi(basiliCizgi.getI(), basiliCizgi.getJ());
            }
            //If a horizontal Cizgi was pressed then do the dikeyBaskiliCizgi method.
            else if (yatayBasiliCizgi == true) {
                this.dikeyBaskiliCizgi(basiliCizgi.getI(), basiliCizgi.getJ());
            }
            //If either was pressed then make a sound.
            if (dikeyBasiliCizgi == true || yatayBasiliCizgi == true) {
                // If sound is on then make a sound.


            }
        }

    }

    /**
     * This method is called from the onDraw method and manages the drawing of the Boxes.
     *
     * @param canvas The canvas to draw on.
     */
    public void kutulariCiz(Canvas canvas) {
        // Go through each kutu.
        for (Kutu[] boxRow : this.oyun.getKutular()) {
            for (Kutu tempBox : boxRow) {
                //If the kutu is selected.
                if (tempBox.Secili_mi()) {
                    //Set the colour of the kutu.
                    this.kutu.setColor(tempBox.getRenk());
                    this.kutu.setAlpha(70);
                    //Get the width and height of the kutu.
                    int boxWidth = this.oyun.getEkranBoyutu()[1][0].getxDegeri() - this.oyun.getEkranBoyutu()[0][0].getxDegeri();
                    int boxHeight = this.oyun.getEkranBoyutu()[0][1].getyDegeri() - this.oyun.getEkranBoyutu()[0][0].getyDegeri();
                    //Draw the kutu onto the canvas.
                    canvas.drawRect(tempBox.getxDegeri(), tempBox.getyDegeri(), tempBox.getxDegeri() + boxWidth,
                            tempBox.getyDegeri() + boxHeight, this.kutu);
                }
            }
        }
    }

    /**
     * This method is called from the onDraw method and manages the drawing of the dots which make up the grid.
     *
     * @param canvas The canvas to draw on.
     */
    public void NoktalariCiz(Canvas canvas) {
        // Go through each nokta.
        for (int i = 0; i < this.oyun.getEkranBoyutu().length; i++) {
            for (int j = 0; j < this.oyun.getEkranBoyutu()[0].length; j++) {
                //Get its x and y location and draw it onto the canvas.
                canvas.drawCircle(this.oyun.getEkranBoyutu()[i][j].getxDegeri(), this.oyun.getEkranBoyutu()[i][j].getyDegeri(), 7 * this.yogunluk, this.nokta);
            }
        }
    }

    /**
     * This method is called from the onDraw method and manages the drawing of the horizontalLines between the dots on the grid.
     *
     * @param canvas The canvas to draw on.
     */
    public void DikeyCizgileriCiz(Canvas canvas) {
        // Go through each horizontal line.
        for (int i = 0; i < this.oyun.getDikeyCizgiler().length; i++) {
            for (int j = 0; j < this.oyun.getDikeyCizgiler()[0].length; j++) {
                //If the line is selected.
                if (this.oyun.getDikeyCizgiler()[i][j].isSecim()) {
                    //Set the colour of the line.
                    this.baskiliCizgi.setColor(this.oyun.getDikeyCizgiler()[i][j].getRenk());
                    //Draw the line onto the canvas using the values of its end dots.
                    canvas.drawLine(this.oyun.getDikeyCizgiler()[i][j].getBaslangicNoktasi().getxDegeri(), this.oyun.getDikeyCizgiler()[i][j]
                            .getBaslangicNoktasi().getyDegeri(), this.oyun.getDikeyCizgiler()[i][j].getBitisNoktasi().getxDegeri(), this.oyun
                            .getDikeyCizgiler()[i][j].getBitisNoktasi().getyDegeri(), this.baskiliCizgi);
                }
            }
        }
    }

    /**
     * This method is called from the onDraw method and manages the drawing of the players name and current score onto the screen.
     *
     * @param canvas The canvas to draw on.
     */
    public void isimleriCiz(Canvas canvas) {

        // Get strings and width/heights for formatting.
        textOyuncu1 = (this.oyuncu1 + "   Puan: " + this.oyun.getOyuncu1Puan());
        textOyuncu2 = (this.oyuncu2 + "   Puan: " + this.oyun.getOyuncu2Puan());
        Rect rect = new Rect();
        this.txtSkor.setTextAlign(Align.CENTER);
        this.txtSkor.getTextBounds(textOyuncu1, 0, textOyuncu1.length(), rect);
        this.height = Math.abs(rect.bottom - rect.top);
        Paint boldText = new Paint();
        boldText.set(this.txtSkor);
        boldText.setTypeface(Typeface.DEFAULT_BOLD);

        // Depending on whose turn it is bold their text.
        switch (this.oyun.getPlayer()) {
            case 1:
                canvas.drawText(textOyuncu1, this.width / 2, this.height + (10 * this.yogunluk), boldText);
                canvas.drawText(textOyuncu2, this.width / 2, this.height * 2 + (15 * this.yogunluk), this.txtSkor);
                break;
            case 2:
                canvas.drawText(textOyuncu1, this.width / 2, this.height + (10 * this.yogunluk), this.txtSkor);
                canvas.drawText(textOyuncu2, this.width / 2, this.height * 2 + (15 * this.yogunluk), boldText);
                break;
        }
    }

    /**
     * This method is called from the onDraw method and manages the drawing of the verticalLines between the dots on the grid.
     *
     * @param canvas The canvas to draw on.
     */
    public void yatayCizgileriCiz(Canvas canvas) {
        // Go through each vertical line.
        for (int i = 0; i < this.oyun.getDikeyCizgi().length; i++) {
            for (int j = 0; j < this.oyun.getDikeyCizgi()[0].length; j++) {
                //If the line is selected.
                if (this.oyun.getDikeyCizgi()[i][j].isSecim()) {
                    //Set the colour of the line.
                    this.baskiliCizgi.setColor(this.oyun.getDikeyCizgi()[i][j].getRenk());
                    //Draw the line onto the canvas using the values of its end dots.
                    canvas.drawLine(this.oyun.getDikeyCizgi()[i][j].getBaslangicNoktasi().getxDegeri(), this.oyun.getDikeyCizgi()[i][j]
                            .getBaslangicNoktasi().getyDegeri(), this.oyun.getDikeyCizgi()[i][j].getBitisNoktasi().getxDegeri(), this.oyun
                            .getDikeyCizgi()[i][j].getBitisNoktasi().getyDegeri(), this.baskiliCizgi);
                }
            }
        }
    }

    /**
     * Gets the current oyun of the game so it can be saved by the activity.
     *
     * @return The oyun.
     */
    public Serializable getOyun() {
        return this.oyun;
    }

    /**
     * The method which is called when a horizontal line has been pressed.
     *
     * @param i The i value of the line across the grid.
     * @param j The j value of the line down the grid.
     */
    private void dikeyBaskiliCizgi(int i, int j) {
        // Send the press to game oyun.
        if (!this.oyun.horizontalPressed(i, j)) {
            // If it returns false then it was already selected.
            this.kullanılmısAlan();
        }
        // Else, player 1 has been pressed, and if we are using the bot.
        else if (this.bilgisayar == true) {
            // Set up our task again.
            this.setupTask();
            // Execute the task.
            this.task.execute((Void[]) null);
        }
        // If the game has ended show the alert kutu.
        if (this.oyun.oyunBitti()) {
            this.kazananOyuncu();
        }
        // Redraw the canvas.
        this.invalidate();
    }

    private void rakipSira() {
        // Show a toast message.
        Toast toast = Toast.makeText(getContext(), "  Sıra Rakibinde.  ", Toast.LENGTH_SHORT);

        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toast);
        toast.show();
        // Shake the screen to get user attention.

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //Draw the names of the players.
        this.isimleriCiz(canvas);
        //First draw the boxes so everything else appears on top of it.
        this.kutulariCiz(canvas);
        //Draw both sets of lines.
        this.DikeyCizgileriCiz(canvas);
        this.yatayCizgileriCiz(canvas);
        //Finally draw the dots (grid) on top of the lines and boxes.
        this.NoktalariCiz(canvas);
    }

    /**
     * When a touch is detected, work out which MotionEvent it was and send the co-ordinates pressed to the check_touch() method for
     * processing.
     *
     * @param event
     * @return Telling super there was a touchEvent.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //If the event was a touch down event then calculate the line with those coordinates pressed.
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            this.calculateLine((int) event.getX(), (int) event.getY());
        return super.onTouchEvent(event);
    }

    /**
     * When the reset Button is clicked we want to present an alert kutu to the user for final confirmation before we reset the oyun.
     */
    protected void temizle() {
        //Present the user with an alert asking if they are sure they want to reset the game.

                        OyunGoruntu.this.ekranSifirla();


    }

    /**
     * Resets the game.
     */
    public void ekranSifirla() {
        //Reset the game oyun.
        this.oyun.oyunuSifirla();
        //Show these changes.
        this.invalidate();
    }

    /**
     * Sets the oyun of the game so we can restore its state.
     *
     * @param serializable The oyun we want to set.
     * @return
     */
    public void setOyun(Serializable serializable) {
        //Set the oyun.
        this.oyun = (OyunKontrol) serializable;
    }

    /**
     * Set up the task which takes the turns of the Bilgisayar. Needs to be set up every time it is called because an AsyncTask can only be run once.
     * Lets us process in the background without lagging the UI thread and it allows us to give the impression of the computer thinking and
     * give a small delay in the press being made rather than it happening instantly. This was mainly due to the user not being able to see where the Bilgisayar
     * had gone after they made their move.
     */
    private void setupTask() {
        //Set up the task.
        this.task = new AsyncTask<Void, Void, Void>() {
            //In the background we want to do this.
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //Sleep for half a second to give impression of thinking time.
                    Thread.sleep(500);
                    //If a kutu was not created.
                    if (!OyunGoruntu.this.oyun.seciliKutu()) {
                        //Set the oyun of the GameView to the result of the Bilgisayar thinking.
                        OyunGoruntu.this.setOyun(OyunGoruntu.this.bot.think(OyunGoruntu.this.oyun));
                    }
                } catch (InterruptedException e) {
                }
                return null;
            }

            /**
             * After it has been executed.
             *
             * @param result The result of doInBackground
             *
             * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
             */
            @Override
            protected void onPostExecute(Void result) {
                //Invalidate the view.
                OyunGoruntu.this.invalidate();
                //If the game has ended after the Bilgisayar's turn then show the winner.
                if (OyunGoruntu.this.oyun.oyunBitti() && OyunGoruntu.this.oyun.getPlayer() == 2) {
                    OyunGoruntu.this.kazananOyuncu();
                }
            }
        };
    }

    /**
     * A method which shows the winner of the game through an alertbox and is called at the end of the game.
     */
    private void kazananOyuncu() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogCustom);


        builder.setTitle("Oyun Tamamlandı");


        builder.setIcon(android.R.drawable.btn_star_big_on);



        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        // If there is a draw.
        if (this.oyun.getOyuncu1Puan() == this.oyun.getOyuncu2Puan()) {
            builder.setMessage(this.icerik.getString(R.string.berabere));
            OyunActivityTekKisilik.buttonViewberabere.setVisibility(View.VISIBLE);
            OyunActivityTekKisilik.buttonViewkazandin.setVisibility(View.INVISIBLE);
            OyunActivityTekKisilik.buttonViewkaybettin.setVisibility(View.INVISIBLE);
        }
        // Else someone must have won.
        else {
            // Create an empty string.
            String winnerString = null;
            // Set string to whoever won.
            if (this.oyun.getOyuncu1Puan() > this.oyun.getOyuncu2Puan()) {
                winnerString = this.oyuncu1;
                OyunActivityTekKisilik.buttonViewkazandin.setVisibility(View.VISIBLE);
                OyunActivityTekKisilik.buttonViewberabere.setVisibility(View.INVISIBLE);
                OyunActivityTekKisilik.buttonViewkaybettin.setVisibility(View.INVISIBLE);
                OyunKontrol.degeri = ++degeri;
                kazanilan_puan=(this.oyun.getOyuncu1Puan()*10);
                OyunSecimActivity.kullannicipuan=kazanilan_puan+OyunSecimActivity.kullannicipuan;
                SkoruKaydet(OyunSecimActivity.kullannicipuan);



            } else {
                winnerString = this.oyuncu2;
            }
            builder.setMessage(this.icerik.getString(R.string.tebrikler) + " " + winnerString
                    + this.icerik.getString(R.string.kazandı));
            if (this.bilgisayar == true && (this.oyun.getOyuncu1Puan() < this.oyun.getOyuncu2Puan())) {
                builder.setMessage(this.icerik.getString(R.string.kaybettin));
                OyunActivityTekKisilik.buttonViewkaybettin.setVisibility(View.VISIBLE);
                OyunActivityTekKisilik.buttonViewkazandin.setVisibility(View.INVISIBLE);
                OyunActivityTekKisilik.buttonViewberabere.setVisibility(View.INVISIBLE);

                OyunKontrol.degeri=3;
            }
        }
        builder.show();
    }

    /**
     * When the undo button has been pressed. This is called and acts as a way of the activity talking to the oyun.
     */


    /**
     * The method which is called when a vertical line has been pressed.
     *
     * @param i The i value of the line across the grid.
     * @param j The j value of the line down the grid.
     */
    private void yatayBaskiliCizgi(int i, int j) {

        // Send the press to game oyun.
        if (!this.oyun.yataycizgi(i, j)) {
            // If it returns false then it was already selected.
            this.kullanılmısAlan();
        }
        // Else, player 1 has been pressed, and if we are using the bot.
        else if (this.bilgisayar == true) {
            // Set up our task again.
            this.setupTask();
            // Execute the task.
            this.task.execute((Void[]) null);
        }
        // If the game has ended show the alert kutu.
        if (this.oyun.oyunBitti()) {
            this.kazananOyuncu();
        }
        // Redraw the canvas.
        this.invalidate();

    }


    public void SkoruKaydet(int puan) {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("inventors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("puan").setValue(puan);


    }
}
