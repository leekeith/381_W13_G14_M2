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
public static boolean clear = false;

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();

    public DrawView(Context context, AttributeSet attribute_set) {
        super(context, attribute_set );
        setFocusable(true);
        setFocusableInTouchMode(true);
        
        this.setOnTouchListener(this);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(MainActivity.brushWidth);
    }

    @Override
/*    public void onDraw(Canvas canvas) {
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 5, paint);
            // Log.d(TAG, "Painting: "+point);
        }
    }*/
    
    public void onDraw(Canvas canvas) {
 
        boolean first = true;
        paint.setStrokeWidth(MainActivity.brushWidth);
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
        points.clear(); //THIS ENDS THE LINE?
    }

    public boolean onTouch(View view, MotionEvent event) {
        // if(event.getAction() != MotionEvent.ACTION_DOWN)
        // return super.onTouchEvent(event);
        if (clear){
        	clear = false;
        	points.clear();
        	path.reset();
        }
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);      
        invalidate();
        Log.d(TAG, "point: " + point);
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