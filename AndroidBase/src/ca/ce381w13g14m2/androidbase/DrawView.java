package ca.ce381w13g14m2.androidbase;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RadioGroup;
/*
 * DoIP Android
 * ============
 * DrawView: 	A View class used to interpret touches as brush strokes
 * 				and draws them to the screen. Infers commands to be transmitted
 * 				and holds the values to be sent.
 * Author:		Syed R & Jon M
 */
public class DrawView extends View implements OnTouchListener {
	
	
	private final String TAG = "DrawView";
	private String send_data = null;
	public static boolean clear = false;
	public static int color = Color.RED;
	public int i = 0;
	public instr_type cmd=instr_type.NONE;
	private  boolean line_start=true;
	public RadioGroup radio;
	public int oldRadioId;

	
    List<Point> points = new ArrayList<Point>();
    List<MyPath> paths = new ArrayList<MyPath>();
    
    //Constructor
    public DrawView(Context context, AttributeSet attribute_set) {
        super(context, attribute_set );
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        

    }
	
    //MyPath path = new MyPath();
    
    // Draws brush strokes to the view
    public void onDraw(Canvas canvas) {
    	MyPath path = new MyPath();  //Will be more efficient when copy constructor is done
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
    		for (int i = 0;i<paths.size();i++)
        		paths.get(i).onePath.reset();
    	}
    	path.paint.setColor(color);
        path.paint.setAlpha(170);
    	for(Point point : points){
            if(first){
                first = false;
                path.onePath.moveTo(point.x, point.y);
            }
            else{
                path.onePath.lineTo(point.x, point.y);
            }
        }
    	//paths.add(new MyPath(path));
    	paths.add(path);
    	for (int i = 0;i<paths.size();i++)
    		canvas.drawPath(paths.get(i).onePath, paths.get(i).paint);
    }
    
    // Called by main thread when view is "touched" by the user
    // Generates or extends a path of points to be drawn/connected on screen
    // Converts touch coordinates into pixel offset to be transmitted
    public boolean onTouch(View view, MotionEvent event) {
    	int maxX=view.getWidth();
    	int maxY=view.getHeight();

        
    	Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        
        
        //Restrict (x,y) to within edges of view
        if(point.x>maxX-1)
        	point.x=maxX-1;
   
        if(point.x<0)
        	point.x=0;

        if(point.y>maxY-1)
        	point.y=maxY-1;

        if(point.y<0)
        	point.y=0;
        points.add(point);
        invalidate();
        Log.d(TAG, "point: " + point);
        
        //Make pixel offset
        i = ((int)point.y * 240/maxY)*320 + ((int)point.x * 320/maxX);
        
        //Set instruction type
        if(radio.getCheckedRadioButtonId()==R.id.radio_draw_line)
        {
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
        }
        else if(radio.getCheckedRadioButtonId()==R.id.radio_draw_pixel)
        {
        	cmd=instr_type.FILL_PIXEL;
        }
        else //if(radio.getCheckedRadioButtonId()==R.id.radio_fill_color)
        {
        	cmd=instr_type.FILL_COLOR;
        	radio.check(oldRadioId);
        }
	      
        // Debug message
        send_data = Integer.toString(i);
        Log.d("TimerTask", "Cmd:"+cmd+" X:"+Float.toString(point.x)+" Y:"+Float.toString(point.y));
        
        //Terminate line
        if (event.getAction() == android.view.MotionEvent.ACTION_UP)
        {
        	points.clear();
        }
    	
        return true;
    }
 
}
// Data sturcture to hold x-y coordinate of a "touch"
class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}

