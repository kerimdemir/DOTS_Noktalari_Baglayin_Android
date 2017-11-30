/**
 * OyunKontrol.java
 */

package com.example.kerim.noktakutu;

import java.io.Serializable;
import java.util.Stack;


public class OyunKontrol implements Serializable {

 public static int degeri=3;
  
  /* Objects fields. */
  private Nokta[][] ekranBoyutu;
  private Cizgi[][] dikeyCizgi;
  private Cizgi[][] yatayCizgi;
  private Kutu[][] kutular;
  private Stack<Cizgi> kalinCizgi;
  /* Display fields. */
  private int               displayWidth;
  private int               displayHeight;
  /* Player 1 fields. */ 
  private int puan1 = 0;
  private int renk1;
  private int puan2 = 0;
  private int renk2;
  /* Game info fields. */
  private int               currentPlayer    = 1;
  private boolean kutusecimi;

  
  /**
   * The logic constructor which creates a logic object.
   * 
   * @param gridSize The size of the ekranBoyutu.
   * @param width    The width of the view.
   * @param height   The height of the view.

   * @param renk1  The colour of player 1.
   * @param renk2  The colour of player 2.
   */
  public OyunKontrol(String gridSize, int width, int height, int renk1, int renk2) {
    //Set the display diameters.
    this.displayWidth = width;
    this.displayHeight = height;
    // Set up our stack.
    this.kalinCizgi = new Stack<Cizgi>();
    // Set the player info.
    this.renk1 = renk1;
    this.renk2 = renk2;


      this.noktaCiz(degeri);


    //Create lines based on dots.
    this.CizgiCiz();
    //Create kutular based on lines.
    this.KutuCiz();
  }

  /**
   * Method which checks if a box had been selected.
   * 
   * @return the kutusecimi
   */
  public boolean seciliKutu() {
    return this.kutusecimi;
  }

  /**
   * The method which creates Kutu objects.
   */
  private void KutuCiz() {
    // Initialise the ekranBoyutu of dots.
    this.kutular = new Kutu[this.ekranBoyutu.length - 1][this.ekranBoyutu[0].length - 1];
    // Fill the ekranBoyutu with dots.
    for (int i = 0; i < this.kutular.length; i++) {
      for (int j = 0; j < this.kutular[0].length; j++) {
        this.kutular[i][j] = new Kutu(this.ekranBoyutu[i][j].getxDegeri(), this.ekranBoyutu[i][j].getyDegeri(),
        // Top Cizgi.
            this.dikeyCizgi[i][j],
            // Bottom Cizgi.
            this.dikeyCizgi[i][j + 1],
            // Left Cizgi.
            this.yatayCizgi[i][j],
            // Right Cizgi.
            this.yatayCizgi[i + 1][j]);
      }
    }
  }


  public void noktaCiz(int degeri) {

    // Set each dot to take up about a 6th of the screen.
    int xOffset = this.displayWidth / (degeri + 1);
    int yOffset = this.displayHeight / (degeri + 5);

    // Initialise the ekranBoyutu of dots.
    this.ekranBoyutu = new Nokta[degeri][degeri];
    // Fill the ekranBoyutu with dots.
    for (int i = 0; i < this.ekranBoyutu.length; i++) {
      for (int j = 0; j < this.ekranBoyutu[0].length; j++) {
        // Top left dot
        if (i == 0 && j == 0) {
          this.ekranBoyutu[i][j] = new Nokta((xOffset), (yOffset * 2));
        }
        else {
          // First column
          if (i == 0) {
            this.ekranBoyutu[i][j] = new Nokta(this.ekranBoyutu[i][j - 1].getxDegeri(), this.ekranBoyutu[i][j - 1].getyDegeri() + yOffset);
          }
          // First row
          else if (j == 0) {
            this.ekranBoyutu[i][j] = new Nokta(this.ekranBoyutu[i - 1][j].getxDegeri() + xOffset, this.ekranBoyutu[i - 1][j].getyDegeri());
          }
          // Everything else
          else if (i != 0 && j != 0) {
            this.ekranBoyutu[i][j] = new Nokta(this.ekranBoyutu[i - 1][j - 1].getxDegeri() + xOffset, this.ekranBoyutu[i - 1][j - 1].getyDegeri() + yOffset);
          }

        }
      }
    }
  }


  private void CizgiCiz() {

    //Set up fields.
    this.dikeyCizgi = new Cizgi[this.ekranBoyutu.length - 1][this.ekranBoyutu[0].length];
    this.yatayCizgi = new Cizgi[this.ekranBoyutu.length][this.ekranBoyutu[0].length - 1];

    // We want a line between every dot horizontally and vertically.
    // Going through a column.
    for (int i = 0; i < this.dikeyCizgi.length; i++) {
      // Going through each row.
      for (int j = 0; j < this.dikeyCizgi[i].length; j++) {
        // Create horizontal line across.
        this.dikeyCizgi[i][j] = (new Cizgi(this.ekranBoyutu[i][j], this.ekranBoyutu[i + 1][j], "horizontal", i, j));
      }
    }
    for (int i = 0; i < this.yatayCizgi.length; i++) {
      // Going through each row.
      for (int j = 0; j < this.yatayCizgi[i].length; j++) {
        // Create vertical line down.
        this.yatayCizgi[i][j] = (new Cizgi(this.ekranBoyutu[i][j], this.ekranBoyutu[i][j + 1], "vertical", i, j));
      }
    }
  }

  /**
   * The method which checks to see if the game has ended.
   * 
   * @return If the game has ended or not.
   */
  public boolean oyunBitti() {
    //Go through each box.
    for (int i = 0; i < this.kutular.length; i++) {
      for (int j = 0; j < this.kutular[0].length; j++) {
        //If the is a box which is not selected then the game hasnt ended.
        if (!this.kutular[i][j].Secili_mi()) {
          return false;
        }
      }
    }
      //If it has got through to here it means all kutular were selected.
      return true;
  }

  /**
   * The method which returns the Kutu objects.
   * 
   * @return The kutular contained in logic.
   */
  public Kutu[][] getKutular() {
    return this.kutular;
  }

  /**
   * The method which returns the Nokta objects.
   * 
   * @return The Nokta objects contained in logic.
   */
  public Nokta[][] getEkranBoyutu() {
    return this.ekranBoyutu;
  }

  /**
   * The method which returns the Cizgi objects in dikeyCizgi.
   * 
   * @return The horizontal Lines.
   */
  public Cizgi[][] getDikeyCizgiler() {
    return this.dikeyCizgi;
  }

  /**
   * The method which gets the current player.
   * 
   * @return The current player number.
   */
  public int getPlayer() {
    return this.currentPlayer;
  }

  /**
   * The method which gets the player 1 score.
   * 
   * @return Player 1 score.
   */
  public int getOyuncu1Puan() {
    return this.puan1;
  }

  /**
   * The method which gets the player 2 score.
   * 
   * @return Player 2 score.
   */
  public int getOyuncu2Puan() {
    return this.puan2;
  }

  /**
   * The method which returns the Cizgi objects in yatayCizgi.
   * 
   * @return The vertical Lines.
   */
  public Cizgi[][] getDikeyCizgi() {
    return this.yatayCizgi;
  }

  /**
   * Called from GameView when a horizontal line has been pressed. It tells linePressed() that a line has been pressed.
   * It also acts as an indicator to whoever called it if this line has already been pressed or not.
   * 
   * @param i The i value of the line pressed.
   * @param j The j value of the line pressed.
   * @return If the line has already been selected or not.
   */
  public boolean horizontalPressed(int i, int j) {
    // Check to see if the line has already been pressed.
    if (this.dikeyCizgi[i][j].isSecim()) {
      return false;
    }
    else {
      this.linePressed(this.dikeyCizgi[i][j]);
      return true;
    }
  }

  /**
   * This method deals with pressing the line and making it selected.
   * It also deals with the selection of a box.
   * 
   * @param cizgi The line to be pressed.
   */
  public void linePressed(Cizgi cizgi) {
    //Empty colour.
    int colour = 0;
    //Push the line to the stack so we can undo later.
    this.kalinCizgi.push(cizgi);
    //Get the current player.
    switch (this.currentPlayer) {
      //If its player one.
      case 1:
        colour = this.renk1;
        cizgi.tiklandi(this.renk1);
        break;
      //If its player two.
      case 2:
        colour = this.renk2;
        cizgi.tiklandi(this.renk2);
        break;
    }
    //Switch the player.
    this.switchPlayer();
    //Have we made a box.
    boolean boxMade = false;
    //Go through each box.
    for (Kutu[] boxRow : this.kutular) {
      for (Kutu tempBox : boxRow) {
        //If the box contains the line we just pressed.
        if (tempBox.satirKontrol(cizgi)) {
          //Click the box with the colour of the player who pressed the line.
          tempBox.tiklandi(colour);
          //If the box is selected.
          if (tempBox.Secili_mi()) {
            //We made a box!
            boxMade = true;
            //Get the colour of the box.
            if (tempBox.getRenk() == this.renk1) {
              //Increment the score of the player 1.
              this.puan1++;
              //Give player 1 another go.
              this.currentPlayer = 1;
            }
            else {
              //Increment the score of the player 2.
              this.puan2++;
              //Give player 2 another go.
              this.currentPlayer = 2;
            }
          }
        }
      }
    }
    //If a box was made set kutusecimi to true.
    if (boxMade == true) this.kutusecimi = true;
    else this.kutusecimi = false;
  }

  /**
   * This method resets the game.
   */
  public void oyunuSifirla() {
    // Reset score and turn.
    puan1=0; puan2 = 0;
    currentPlayer = 1;
    kutusecimi = false;
    this.kalinCizgi.clear();
    // Clear selections.
    for (int i = 0; i < this.dikeyCizgi.length; i++) {
      for (int j = 0; j < this.dikeyCizgi[0].length; j++) {
        this.dikeyCizgi[i][j].seciliDegilse();
      }
    }
    for (int i = 0; i < this.yatayCizgi.length; i++) {
      for (int j = 0; j < this.yatayCizgi[0].length; j++) {
        this.yatayCizgi[i][j].seciliDegilse();
      }
    }
  }

  /**
   * This method switches the player.
   */
  public void switchPlayer() {
    if (this.currentPlayer == 1) {
      this.currentPlayer = 2;
    }
    else {
      this.currentPlayer = 1;
    }
  }


  public boolean yataycizgi(int i, int j) {
    // Check to see if the line has already been pressed.
    if (this.yatayCizgi[i][j].isSecim()) {
      return false;
    }
    else {
      this.linePressed(this.yatayCizgi[i][j]);
      return true;
    }
  }


}

