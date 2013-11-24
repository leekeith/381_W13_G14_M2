package ca.ce381w13g14m2.androidbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
 
public class SplashScreen extends Activity {
 
    private static int DISPLAY_TIME = 4000; //milliseconds
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide statusbar of Android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        
        //Show main_activity after timer times out and kill splash screen
        new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
 
                finish();
            }
        }, DISPLAY_TIME);
    }
 
}