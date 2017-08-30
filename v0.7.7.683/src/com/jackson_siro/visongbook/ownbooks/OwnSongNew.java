package com.jackson_siro.visongbook.ownbooks;

import com.jackson_siro.visongbook.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class OwnSongNew extends ActionBarActivity {
	
	public static final String VSB_SETTINGS = "vSONG_BOOKs";
	public static final String FONT_SIZE = "font_size";
	
	String songTitle;
	String songContent;
	//OwnSong selectedSong;
	//SQLiteHelper db;
	SQLiteHelper db = new SQLiteHelper(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ob_edit);
		
	}
	
	public void AddNewSong() {
		
		EditText title = (EditText) findViewById(R.id.title);
		EditText content = (EditText) findViewById(R.id.content);
		
		songTitle = title.getText().toString();
		songContent = content.getText().toString();
		
		//db.onUpgrade(db.getWritableDatabase(), 1, 2);

		db.createSong(new OwnSong(songTitle, songContent)); 
		Toast.makeText(getApplicationContext(), "A new song has been added.", Toast.LENGTH_SHORT).show();
		
		finish();
	}
	
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.own_save, menu);
         
         return true;
     }
     

 	@Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.savethis:
            	 AddNewSong();
                 return true;
                 
             case R.id.cancel:
             	finish();
                 return true;

                 
             default:
                 return false;
         }
     } 

	  public static class LineEditText extends EditText{
			// we need this constructor for LayoutInflater
			public LineEditText(Context context, AttributeSet attrs) {
				super(context, attrs);
					mRect = new Rect();
			        mPaint = new Paint();
			        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			        mPaint.setColor(Color.BLUE);
			}

			private Rect mRect;
		    private Paint mPaint;	    
		    
		    @Override
		    protected void onDraw(Canvas canvas) {
		  
		        int height = getHeight();
		        int line_height = getLineHeight();

		        int count = height / line_height;

		        if (getLineCount() > count)
		            count = getLineCount();

		        Rect r = mRect;
		        Paint paint = mPaint;
		        int baseline = getLineBounds(0, r);

		        for (int i = 0; i < count; i++) {

		            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
		            baseline += getLineHeight();

		        super.onDraw(canvas);
		    }

		}
	  }
	  
	
}
