package com.jackson_siro.visongbook;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Settings2 extends ActionBarActivity {
    public static final String FONT_SIZE = "font_size";
    public static final String SET_THEME = "set_theme";
    public static final String VSB_SETTINGS = "vSONG_BOOKs";
    private TextView preview;
    private SeekBar seekBar;
    private TextView textView;

    /* renamed from: com.jackson_siro.visongbook.Settings2.1 */
    class C01711 implements OnSeekBarChangeListener {
        int progress;

        C01711(int i) {
            this.progress = i;
        }

        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
            this.progress = progresValue;
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            Settings2.this.textView.setText("Font Size: " + this.progress);
            Settings2.this.preview.setTextSize((float) this.progress);
            Settings2.this.getSharedPreferences(Settings2.VSB_SETTINGS, 25).edit().putString(Settings2.FONT_SIZE, Integer.toString(this.progress)).commit();
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_i);
        String font_size = getSharedPreferences(VSB_SETTINGS, 0).getString(FONT_SIZE, "25");
        this.seekBar = (SeekBar) findViewById(R.id.seekBar1);
        this.preview = (TextView) findViewById(R.id.textView2);
        this.textView = (TextView) findViewById(R.id.textView1);
        int myFonts = Integer.parseInt(font_size);
        this.preview.setTextSize((float) myFonts);
        this.textView.setText("Font Size: " + myFonts);
        this.seekBar.setProgress(myFonts);
        this.seekBar.setOnSeekBarChangeListener(new C01711(myFonts));
    }
}
