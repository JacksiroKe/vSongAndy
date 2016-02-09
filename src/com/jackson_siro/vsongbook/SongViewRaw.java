package com.jackson_siro.vsongbook;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class SongViewRaw extends ActionBarActivity {
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_raw);
        
        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null) {
            finish();
        } else {
            cursor.moveToFirst();

            EditText content = (EditText) findViewById(R.id.content);

            int wIndex = cursor.getColumnIndexOrThrow(SongbookDatabase.KEY_SONGCONT);
            int dIndex = cursor.getColumnIndexOrThrow(SongbookDatabase.KEY_SONG);

            setTitle("Copying Song: "+cursor.getString(wIndex));
            content.setText(cursor.getString(dIndex));
        }
        
    }
    
}
