package ca.ce381w13g14m2.androidbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
private static final String TAG = "DrawView";
private static String send_data = null;
public static boolean clear = false;
public static int color = Color.RED;
public static int i = 0;
public instr_type cmd=instr_type.NONE;
private  boolean line_start=true;


    List<Point> points = new ArrayList<Point>();
    //Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //Paint paint_border = new Paint(Paint.ANTI_ALIAS_FLAG);
    //Path path = new Path();
    //MyPath path = new MyPath();
    List<MyPath> paths = new ArrayList<MyPath>();

    public DrawView(Context context, AttributeSet attribute_set) {
        super(context, attribute_set );
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        
        
        
        Timer my_timer = new Timer(); 
        MyTimerTask my_task = new MyTimerTask(); 
        my_timer.schedule(my_task, 1000, 100);
        
    }
	//MyPath path = new MyPath();


    public void onDraw(Canvas canvas) {
    	MyPath path = new MyPath();
    	path.paint.setStyle(Paint.Style.STROKE);
        path.paint_border.setStyle(Paint.Style.STROKE);
        path.paint.setAntiAlias(true);
        path.paint_border.setAntiAlias(true);
    	
    	canvas.drawRect(0, 0, getWidth(), getHeight(), path.paint_border);
    	boolean first = true;
    	
        path.paint.setStrokeWidth(MainActivity.brushWidth);
    	if(clear)
    	{
    		clear = false;
    		points.clear();
    		path.onePath.reset();
    	}
    	path.paint.setColor(color);
    	for(Point point : points){
            if(first){
                first = false;
                path.onePath.moveTo(point.x, point.y);
            }
            else{
                path.onePath.lineTo(point.x, point.y);
            }
        }
    	paths.add(path);
    	
    	for (int i = 0;i<paths.size();i++)
    		canvas.drawPath(paths.get(i).onePath, paths.get(i).paint);
    }

    public boolean onTouch(View view, MotionEvent event) {
        
        
    	Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        if(point.x>600)
        	point.x=600;
        if(point.x<0)
        	point.x=0;
        if(point.y>440)
        	point.y=440;
        if(point.y<0)
        	point.y=0;
        points.add(point);
        invalidate();
        Log.d(TAG, "point: " + point);
        
        i = ((int)point.y * 600/320) + ((int)point.x * 320/600);
        if(event.getAction()==android.view.MotionEvent.ACTION_DOWN)
        {
        	cmd=instr_type.LINE_START;
        }
        else if(event.getAction()==android.view.MotionEvent.ACTION_UP)
        {
        	cmd=instr_type.LINE_END;
        }
        else
        {
          	cmd=instr_type.LINE_PT;
        }
        
        send_data = Integer.toString(i);
        Log.d("TimerTask", "Cmd:"+cmd+" X:"+Float.toString(point.x)+" Y:"+Float.toString(point.y));
        
        if (event.getAction() == android.view.MotionEvent.ACTION_UP)
        {
        	points.clear();
        }
    	
    	
    	
    	/*
    	final MotionEvent fevent = event;
        new Thread(new Runnable() {
            public void run() {
            	Point point = new Point();
                point.x = fevent.getX();
                point.y = fevent.getY();
                points.add(point);
                invalidate();
                Log.d(TAG, "point: " + point);
                
                i = ((int)point.y * 600) + (int)point.x;
                send_data = Integer.toString(i);
                Log.d(TAG, send_data);
                
                if (fevent.getAction() == android.view.MotionEvent.ACTION_UP)
                	points.clear();
            }
        }).start();
    	*/
    	
    	
        //new SavePointTask().execute(event);
        return true;
    }



   
    
/*
    private class SavePointTask extends AsyncTask<MotionEvent, Void, Point> {
        //* The system calls this to perform work in a worker thread and
          //* delivers it the parameters given to AsyncTask.execute() 
        protected Point doInBackground(MotionEvent... event) {
        	Point point = new Point();
            point.x = event[0].getX();
            point.y = event[0].getY();
            points.add(point);
        	postInvalidate();

            if (event[0].getAction() == android.view.MotionEvent.ACTION_UP)
            	points.clear();
            return point;
        }
        
        //* The system calls this to perform work in the UI thread and delivers
          // the result from doInBackground() 
        protected void onPostExecute(Point point) {
        	//invalidate();

        	i = ((int)point.y * 600) + (int)point.x;
            send_data = Integer.toString(i);
            Log.d(TAG, send_data);
        }
    }*/
    
    
    
    public class MyTimerTask extends TimerTask { 
    	 public void run() { 
    		 if(clear == true)
    		 {
    			 postInvalidate();
    		 }
    	 } 
    } 

 
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}

class MyPath {
	Path onePath = new Path();
	int color;
	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint paint_border  = new Paint(Paint.ANTI_ALIAS_FLAG);
/*	
	public MyPath (){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_border = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	public MyPath(MyPath m)
	{
		this.color = m.color;
		Path path = new Path();
		this.path = m.path;
	}
*/
}