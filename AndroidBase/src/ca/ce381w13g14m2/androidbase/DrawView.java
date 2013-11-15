package ca.ce381w13g14m2.androidbase;

import java.util.ArrayList;
import java.util.List;

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

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paint_border = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();

    public DrawView(Context context, AttributeSet attribute_set) {
        super(context, attribute_set );
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint_border.setStrokeWidth(2);
        paint_border.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint_border.setColor(Color.BLUE);
        paint_border.setAntiAlias(true);
        
    }


    public void onDraw(Canvas canvas) {
    	canvas.drawRect(0, 0, getWidth(), getHeight(), paint_border);
    	boolean first = true;
        paint.setStrokeWidth(MainActivity.brushWidth);
    	if(clear)
    	{
    		clear = false;
    		points.clear();
    		path.reset();
    	}
    	paint.setColor(color);
    	for(Point point : points){
            if(first){
                first = false;
                path.moveTo(point.x, point.y);
            }
            else{
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
        
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        invalidate();
        //Log.d(TAG, "point: " + point);
        
        i = ((int)point.y * 600) + (int)point.x;
        send_data = Integer.toString(i);
        Log.d(TAG, send_data);
        
        if (event.getAction() == android.view.MotionEvent.ACTION_UP)
        	points.clear();
        return true;
    }
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}