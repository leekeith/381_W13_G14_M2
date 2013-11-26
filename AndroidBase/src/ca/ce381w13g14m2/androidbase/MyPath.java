package ca.ce381w13g14m2.androidbase;

import android.graphics.Paint;
import android.graphics.Path;
/*
 * DoIP Android
 * ============
 * MyPath:	Path plus context to separate sequences of
 * 			points.  Allows paths to retain their color
 * Author: 	Syed R
 */
public class MyPath {
	Path onePath;
	int color;
	Paint paint;
	Paint paint_border;
	
	// Default constructor	
	public MyPath (){
		onePath = new Path();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint_border = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	
	//Copy Constructor
	public MyPath(MyPath c)
	{
		color = c.color;
		onePath = c.onePath;
		paint = c.paint;
		paint_border = c.paint_border;
	}

/*	
	public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
        	ByteArrayOutputStream fbos = new ByteArrayOutputStream ();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in. 
            ObjectInputStream in = new ObjectInputStream((fbos).getInputStream());
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }*/
}



