package ca.ce381w13g14m2.androidbase;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

public class Draw extends Activity {
    DrawView drawView;
    //ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    //ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(1000, 900);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view (FOR NOW)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

//        drawView = new DrawView(this);
        setContentView(drawView);
        drawView.requestFocus();
    }
}