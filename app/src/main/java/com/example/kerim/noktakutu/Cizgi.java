/**
 * Cizgi.java
 */

package com.example.kerim.noktakutu;

import java.io.Serializable;

public class Cizgi implements Serializable {



  private Nokta baslangicNoktasi;
  private Nokta bitisNoktasi;
  private boolean secim;
  private int renk;
  private String yon;
  private int i;
  private int j;

  public Cizgi(Nokta baslangicNoktasi, Nokta bitisNoktasi, String yon, int i, int j) {
    this.baslangicNoktasi = baslangicNoktasi;
    this.bitisNoktasi = bitisNoktasi;
    this.secim = false;
    this.yon = yon;
    this.i = i;
    this.j = j;
  }


  public void seciliDegilse() {
    this.secim = false;
  }


  public int getRenk() {
    return this.renk;
  }

  public Nokta getBitisNoktasi() {
    return this.bitisNoktasi;
  }


  public int getI() {
    return this.i;
  }


  public int getJ() {
    return this.j;
  }


  public String getYon() {
    return this.yon;
  }


  public Nokta getBaslangicNoktasi() {
    return this.baslangicNoktasi;
  }


  public boolean isSecim() {
    return this.secim;
  }

  public void tiklandi(int renk) {
    this.renk = renk;
    this.secim = true;
  }

}