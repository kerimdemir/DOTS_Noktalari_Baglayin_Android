package com.example.kerim.noktakutu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends Activity
{
	TextView tv,tv1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{	super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		tv= (TextView) findViewById(R.id.splashtext);
		tv1=(TextView) findViewById(R.id.splashtexttur);
		tv.setText(OyunActivityTekKisilik.sayac+"");
		tv.setTextSize(108);
		Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
		tv.setTypeface(face1);
		tv1.setTypeface(face1);
		Thread mSplashThread;//thread classdan obje olustrduk uygulamann 4 saniye uyutulmasi icin
		mSplashThread = new Thread(){
			@Override public void run(){
				try {

					synchronized(this){
						wait(2000);
					}
				}catch(InterruptedException ex){

				}
				finally{

					Intent i=new Intent(getApplicationContext(),OyunActivityTekKisilik.class);
					startActivity(i);
					finish();
				}

			}
		};//thread objesini olustrduk ve istedmz sekilde sekillendrdik
		mSplashThread.start();// thread objesini calistriyoruz

    }


}
