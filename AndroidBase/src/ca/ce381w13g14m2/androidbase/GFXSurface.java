package ca.ce381w13g14m2.androidbase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GFXSurface extends Activity implements OnTouchListener{

	MySurface ourSurfaceView;
	float x , y;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ourSurfaceView = new MySurface(this);
		ourSurfaceView.setOnTouchListener(this);
		x = 0;
		y = 0;
		setContentView(ourSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gfxsurface, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSurfaceView.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ourSurfaceView.resume();
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		x = event.getX();
		y = event.getY();
		return true;
	}

	public class MySurface extends SurfaceView implements Runnable{

		SurfaceHolder ourHolder;
		Thread ourThread = null;
		boolean isRunning = false;
		
		public MySurface(Context context) {
			// TODO Auto-generated constructor stub
			super(context);
			ourHolder = getHolder();
			
		}
		
		public void pause(){
			isRunning = false;
			while(true){
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}
		
		public void resume(){
			isRunning = true;
			ourThread = new Thread(this);
			ourThread.start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRunning){
				if(!ourHolder.getSurface().isValid())
					continue;
				
				Canvas canvas = ourHolder.lockCanvas();
				//canvas.drawRGB(02, 02, 150);
				if(x != 0 && y != 0){
					Bitmap test = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
					//Toast t = Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG); 
					//t.show(); 
					canvas.drawBitmap(test, x-(test.getWidth()/2), y-(test.getHeight()/2), null);

				}
					
					
				ourHolder.unlockCanvasAndPost(canvas);
				
			}
		}

	}

}
