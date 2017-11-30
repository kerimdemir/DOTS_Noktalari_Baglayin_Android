/**
 * Nokta.java
 */

package com.example.kerim.noktakutu;

import java.io.Serializable;

public class Nokta implements Serializable {

  private int xDegeri;
  private int yDegeri;


  public Nokta(int xDegeri, int yDegeri) {
    this.xDegeri = xDegeri;
    this.yDegeri = yDegeri;
  }

  public int getxDegeri() {

    return this.xDegeri;
  }

  public int getyDegeri()
  {
    return this.yDegeri;
  }

}
