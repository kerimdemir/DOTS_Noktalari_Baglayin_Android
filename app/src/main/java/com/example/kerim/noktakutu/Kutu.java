

package com.example.kerim.noktakutu;

import android.graphics.Color;

import java.io.Serializable;

public class  Kutu implements Serializable {

  private int xDegeri;
  private int yDegeri;
  private int renk = Color.BLUE;
  private Cizgi[] cizgi;

  public Kutu(int xDegeri, int yDegeri, Cizgi ustCizgi, Cizgi altCizgi, Cizgi solCizgi, Cizgi sagCizgi) {
    // Set up the Cizgi array.
    this.cizgi = new Cizgi[4];
    // Set the fields.
    this.xDegeri = xDegeri;
    this.yDegeri = yDegeri;
    // Fill the Cizgi array with the objects.
    this.cizgi[0] = ustCizgi;
    this.cizgi[1] = altCizgi;
    this.cizgi[2] = solCizgi;
    this.cizgi[3] = sagCizgi;
  }

    public boolean satirKontrol(Cizgi c) {
    // satır kontrol et
    for (Cizgi tempLine : this.cizgi) {
      if (tempLine.equals(c)) {
        return true;
      }
    }
    return false;
  }

  public int getRenk() {
    return this.renk;
  }

  public Cizgi[] getLines() {
    return this.cizgi;
  }


  public int getxDegeri() {

    return this.xDegeri;
  }

  public int getyDegeri() {
    return this.yDegeri;
  }


  public boolean Secili_mi() {

    if (this.cizgi[0].isSecim() && this.cizgi[1].isSecim() && this.cizgi[2].isSecim() && this.cizgi[3].isSecim()) {
      return true;

    }
    return false;
  }


  public void tiklandi(int renk) {
    this.renk = renk;
  }
}
