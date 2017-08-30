package com.jackson_siro.visongbook;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Settings4 extends Activity {
    public static final String BELIEVERS_SONGS = "believers_songs";
    public static final String CIA_KUINIRA = "cia_kuinira";
    public static final String MY_OWN_SONGS = "my_own_songs";
    public static final String SHOW_SPLASH = "show_splash";
    public static final String SHOW_TUTORIAL = "show_tutorial";
    public static final String TENZI_ZA_ROHONI = "tenzi_za_rohoni";
    public static final String VSB_SETTINGS = "vSONG_BOOKs";
    private CheckBox animation;
    private CheckBox believers;
    private CheckBox ciakuinira;
    private CheckBox ownsongs;
    private CheckBox tutorial;
    private CheckBox zarohoni;

    /* renamed from: com.jackson_siro.visongbook.Settings4.1 */
    class C01721 implements OnCheckedChangeListener {
        C01721() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String anim_choice;
            if (Settings4.this.animation.isChecked()) {
                anim_choice = "1";
            } else {
                anim_choice = "0";
            }
            Settings4.this.getSharedPreferences(Settings4.VSB_SETTINGS, 0).edit().putString(Settings4.SHOW_SPLASH, anim_choice).commit();
        }
    }

    /* renamed from: com.jackson_siro.visongbook.Settings4.2 */
    class C01732 implements OnCheckedChangeListener {
        C01732() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String tuto_choice;
            if (Settings4.this.tutorial.isChecked()) {
                tuto_choice = "1";
            } else {
                tuto_choice = "0";
            }
            Settings4.this.getSharedPreferences(Settings4.VSB_SETTINGS, 0).edit().putString(Settings4.SHOW_TUTORIAL, tuto_choice).commit();
        }
    }

    /* renamed from: com.jackson_siro.visongbook.Settings4.3 */
    class C01743 implements OnCheckedChangeListener {
        C01743() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String tuto_choice;
            if (Settings4.this.believers.isChecked()) {
                tuto_choice = "1";
            } else {
                tuto_choice = "0";
            }
            Settings4.this.getSharedPreferences(Settings4.VSB_SETTINGS, 0).edit().putString(Settings4.BELIEVERS_SONGS, tuto_choice).commit();
        }
    }

    /* renamed from: com.jackson_siro.visongbook.Settings4.4 */
    class C01754 implements OnCheckedChangeListener {
        C01754() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String tuto_choice;
            if (Settings4.this.zarohoni.isChecked()) {
                tuto_choice = "1";
            } else {
                tuto_choice = "0";
            }
            Settings4.this.getSharedPreferences(Settings4.VSB_SETTINGS, 0).edit().putString(Settings4.TENZI_ZA_ROHONI, tuto_choice).commit();
        }
    }

    /* renamed from: com.jackson_siro.visongbook.Settings4.5 */
    class C01765 implements OnCheckedChangeListener {
        C01765() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String tuto_choice;
            if (Settings4.this.ciakuinira.isChecked()) {
                tuto_choice = "1";
            } else {
                tuto_choice = "0";
            }
            Settings4.this.getSharedPreferences(Settings4.VSB_SETTINGS, 0).edit().putString(Settings4.CIA_KUINIRA, tuto_choice).commit();
        }
    }

    /* renamed from: com.jackson_siro.visongbook.Settings4.6 */
    class C01776 implements OnCheckedChangeListener {
        C01776() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String tuto_choice;
            if (Settings4.this.ownsongs.isChecked()) {
                tuto_choice = "1";
            } else {
                tuto_choice = "0";
            }
            Settings4.this.getSharedPreferences(Settings4.VSB_SETTINGS, 0).edit().putString(Settings4.MY_OWN_SONGS, tuto_choice).commit();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_iii);
        SharedPreferences settings = getSharedPreferences(VSB_SETTINGS, 0);
        String show_splash = settings.getString(SHOW_SPLASH, "0");
        String show_tutorial = settings.getString(SHOW_TUTORIAL, "0");
        String believers_songs = settings.getString(BELIEVERS_SONGS, "0");
        String tenzi_za_rohoni = settings.getString(TENZI_ZA_ROHONI, "0");
        String cia_kuinira = settings.getString(CIA_KUINIRA, "0");
        String my_own_songs = settings.getString(MY_OWN_SONGS, "0");
        this.animation = (CheckBox) findViewById(R.id.checkBox1);
        this.tutorial = (CheckBox) findViewById(R.id.checkBox2);
        this.believers = (CheckBox) findViewById(R.id.checkBox3);
        this.zarohoni = (CheckBox) findViewById(R.id.checkBox4);
        this.ciakuinira = (CheckBox) findViewById(R.id.checkBox5);
        this.ownsongs = (CheckBox) findViewById(R.id.checkBox6);
        if (show_splash == "1") {
            this.animation.setChecked(true);
        } else if (show_splash == "0") {
            this.animation.setChecked(false);
        }
        if (show_tutorial == "1") {
            this.tutorial.setChecked(true);
        } else if (show_tutorial == "0") {
            this.tutorial.setChecked(false);
        }
        if (believers_songs == "1") {
            this.believers.setChecked(true);
        } else if (believers_songs == "0") {
            this.believers.setChecked(false);
        }
        if (tenzi_za_rohoni == "1") {
            this.zarohoni.setChecked(true);
        } else if (tenzi_za_rohoni == "0") {
            this.zarohoni.setChecked(false);
        }
        if (cia_kuinira == "1") {
            this.ciakuinira.setChecked(true);
        } else if (cia_kuinira == "0") {
            this.ciakuinira.setChecked(false);
        }
        if (my_own_songs == "1") {
            this.ownsongs.setChecked(true);
        } else if (my_own_songs == "0") {
            this.ownsongs.setChecked(false);
        }
        this.animation.setOnCheckedChangeListener(new C01721());
        this.tutorial.setOnCheckedChangeListener(new C01732());
        this.believers.setOnCheckedChangeListener(new C01743());
        this.zarohoni.setOnCheckedChangeListener(new C01754());
        this.ciakuinira.setOnCheckedChangeListener(new C01765());
        this.ownsongs.setOnCheckedChangeListener(new C01776());
    }
}
