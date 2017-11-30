/**
 * Bilgisayar.java
 */

package com.example.kerim.noktakutu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Bilgisayar {

  private OyunKontrol kontrol;
      private List<Cizgi> normalLines; // Lines which are normal priority.
    private List<Cizgi> goodLines;   // Lines which are top priority.
  private List<Cizgi> badLines;    // Lines which are bottom priority.
  private int        botLevel;    // Bot seviyesi

  /**
   * Constructor of the Bilgisayar. It takes a logic object and sets up its Lists
   * .
   * @param oyun The logic from the game to be processed.
   */
  public Bilgisayar(OyunKontrol oyun) {
    // The Bot knows the same as the user in terms of the game logic.
    this.kontrol = oyun;
    // Set up array lists and the bots level.
    this.normalLines = new ArrayList<Cizgi>();
    this.goodLines = new ArrayList<Cizgi>();
    this.badLines = new ArrayList<Cizgi>();
    this.botLevel = MainActivity.botLevel;
  }

  /**
   * This method will find the lines available from the game logic and work out the priority in which the bot should press them. 
   * It then puts these lines in the correct Lists. It does this by getting a box, seeing how many of the lines inside it are selected
   * and then based on the fact that if 3 lines are selected already, this box will only need one more press to score a point.
   */
  private void findLinesToPress() {
    // Loop through each box.
    for (Kutu[] boxRow : this.kontrol.getKutular()) {
      for (Kutu box : boxRow) {
        // If the box is not full.
          if (!box.Secili_mi()) {
          List<Cizgi> tempLines = new ArrayList<Cizgi>();
          // Get the non-selected lines in the box.
          for (Cizgi tempLine : box.getLines()) {
            if (!tempLine.isSecim()) {
              tempLines.add(tempLine);
            }
          }
          //Now go through each line found.
          for (Cizgi freeLine : tempLines) {
            // Bot is easy
            if (this.botLevel == 0) {
              //Just add all the lines to be normal.
              switch (tempLines.size()) {
                case 1:
                  this.goodLines.add(freeLine);
                  break;
                case 2:
                  this.badLines.add(freeLine);
                  break;
                case 3:
                  this.normalLines.add(freeLine);
                  break;
                case 4:
                  this.normalLines.add(freeLine);
                  break;
            }

            }
          }
        }
      }
    }
  }

  /**
   * The main logic within the Bilgisayar class. This is passed a logic object and will call the findLinesToPress() method to create a the lists of priority.
   * This method will then chose random numbers from those lists starting with good lines first, then normal lines, and lastly bad lines. This creates
   * the perception of intelligence when playing against the computer.
   * 
   * @param logic The logic from the game.
   * @return The new logic after it has been modified by the bot.
   */
  public OyunKontrol think(OyunKontrol logic) {
    // See if the botLevel has changed.
    this.botLevel = MainActivity.botLevel;
    // Replace logic with most current version.
    this.kontrol = logic;
    // Reset lines to press.
    this.normalLines = new ArrayList<Cizgi>();
    this.goodLines = new ArrayList<Cizgi>();
    this.badLines = new ArrayList<Cizgi>();
    // Create the list of lines we can press.
    this.findLinesToPress();
    // Pick a random number in the range of length of the linesToPress.
    Random rand = new Random();
    // If there are good lines to press.
    if (this.goodLines.size() != 0) {
      // Chose a random line from the list.
      Cizgi lineToPress = this.goodLines.get(rand.nextInt(this.goodLines.size()));
      // If the line is a horizontal line.
      if (lineToPress.getYon() == "horizontal") {
        // Press the line in our local version of logic.
        this.kontrol.horizontalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      // Else if the line is a vertical line.
      else if (lineToPress.getYon() == "vertical") {
        // Press the line in our local version of logic.
        this.kontrol.yataycizgi(lineToPress.getI(), lineToPress.getJ());
      }
      //If a box was created then go again.
      if (this.kontrol.seciliKutu()) {
        this.think(this.kontrol);
      }
    }
    // If there are good lines to press.
    else if (this.normalLines.size() != 0) {
      // Chose a random line from the list.
      Cizgi lineToPress = this.normalLines.get(rand.nextInt(this.normalLines.size()));
      // If the line is a horizontal line.
      if (lineToPress.getYon() == "horizontal") {
        // Press the line in our local version of logic.
        this.kontrol.horizontalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      // Else if the line is a vertical line.
      else if (lineToPress.getYon() == "vertical") {
        // Press the line in our local version of logic.
        this.kontrol.yataycizgi(lineToPress.getI(), lineToPress.getJ());
      }
      //If a box was created then go again.
      if (this.kontrol.seciliKutu()) {
        this.think(this.kontrol);
      }
    }
    // Finally if there are bad lines to press.
    else if (this.badLines.size() != 0) {
      // Chose a random line from the list.
      Cizgi lineToPress = this.badLines.get(rand.nextInt(this.badLines.size()));
      // If the line is a horizontal line.
      if (lineToPress.getYon() == "horizontal") {
        // Press the line in our local version of logic.
        this.kontrol.horizontalPressed(lineToPress.getI(), lineToPress.getJ());
      }
      // Else if the line is a vertical line.
      else if (lineToPress.getYon() == "vertical") {
        // Press the line in our local version of logic.
        this.kontrol.yataycizgi(lineToPress.getI(), lineToPress.getJ());
      }
      //If a box was created then go again.
      if (this.kontrol.seciliKutu()) {
        this.think(this.kontrol);
      }
    }
    return this.kontrol;
  }
}
