package com.jackson_siro.vsongbook.notepad;

import java.util.ArrayList;
import java.util.List;

import com.jackson_siro.vsongbook.*;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class OwnSongList extends ListActivity implements OnItemClickListener {
	SQLiteHelper db = new SQLiteHelper(this);
	List<OwnSong> list;
	ArrayAdapter<String> myAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notepad_main);
		
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("vsb_notepad_first_use", false)) {
			db.onUpgrade(db.getWritableDatabase(), 1, 2);

			db.createSong(new OwnSong("Thank You Lord", "What a privilege to know you  \n Oh Lord my God  \n You came down to save me  \n You washed my sins away  \n Deliver me from darkness  \n My heart is so grateful  \n I give you Thanks \n \n CHORUS  \n Thank you Lord  \n God of my life  \n You made me to know  \n The Love of Your Grace  \n Thank you Lord  \n God of my life  \n My heart is so grateful  \n I give you thanks (chorus x 2) \n \n What a privilege to worship  \n The essence of days  \n Who came down to save me  \n Wash my sins away  \n Deliver us from darkness  \n Now Lord we are free  \n Our hearts are so grateful  \n To give you thanks. \n \n CHORUS  \n Thank you Lord  \n God of my life  \n You made me to know  \n The Love of Your Grace  \n Thank you Lord  \n God of my life  \n My heart is so grateful  \n I give you thanks (chorus x 2)")); 
			db.createSong(new OwnSong("Bibi Arusi Amekaribia Kwenda", "Mtu mmoja mikononi mwa Mungu \n Mara saba dunia kazunguka \n sauti iliyojaa mapenzi \n Bwana akimwita Bibi Arusi \n Bwana akimwita Bibi Arusi \n  \n PAMBIO \n Bibi arusi amekaribia kwenda \n Amesikia mlio wa tai \n Wa Malaki 4 Ufunuo 10 \n Ule mwaliko Bwana akishuka \n Ule mwaliko Bwana akishuka. \n  \n Mihuri 7 na siri zingine  \n Ndio mavazi ya Bibi arusi \n Nguzo ya moto ishara maajabu \n Kuthibithisha ukweli wa Mungu \n Kuthibithisha ukweli wa Mungu \n  \n PAMBIO \n Bibi arusi amekaribia kwenda \n Amesikia mlio wa tai \n Wa Malaki 4 Ufunuo 10 \n Ule mwaliko Bwana akishuka \n Ule mwaliko Bwana akishuka. \n  \n Nabii katuletea ujumbe  \n Unao nguvu ya kutunyakua \n Pia imani ya kutuhamisha \n Ndio mavazi ya Bibi Arusi \n Ndio mavazi ya Bibi Arusi \n  \n PAMBIO \n Bibi arusi amekaribia kwenda \n Amesikia mlio wa tai \n Wa Malaki 4 Ufunuo 10 \n Ule mwaliko Bwana akishuka \n Ule mwaliko Bwana akishuka. \n  \n Ahadi za Mungu zimetimia \n Kutayarisha Bibi Arusi \n Na ukweli maajabu tumesikia \n Kote Ujumbe umeaminika \n Kote Ujumbe umeaminika \n  \n PAMBIO \n Bibi arusi amekaribia kwenda \n Amesikia mlio wa tai \n Wa Malaki 4 Ufunuo 10 \n Ule mwaliko Bwana akishuka \n Ule mwaliko Bwana akishuka. \n  \n Ndugu na dada yangu hebu jiulize \n mmefanana na na neno la Mungu \n Je? Maisha yenu yamestahili \n Ule ujumbe wa Bibi Arusi \n Ndio mavazi ya Bibi Arusi \n  \n PAMBIO \n Bibi arusi amekaribia kwenda \n Amesikia mlio wa tai \n Wa Malaki 4 Ufunuo 10 \n Ule mwaliko Bwana akishuka \n Ule mwaliko Bwana akishuka."));
			db.createSong(new OwnSong("Ujumbe ni Barua ya Upendo", "Siku za mwisho Mungu ametupenda \n Kwa kutumia neno la Uzima \n Ameonekana miongoni mwetu \n Kupitia mjumbe wetu wa saba. \n  \n PAMBIO \n Ujumbe ni barua ya upendo \n Amemtumia bibi arusi wake \n Ameituma hata picha angani \n Yamaanisha kwamba yuaja tena. \n  \n Nguzo ya moto tayari imerejeshwa \n Kama ilivyokuwa siku za Musa \n Tunaiona hata miongoni mwetu \n Udhihirisho wa ahadi zake Mungu \n  \n PAMBIO \n Ujumbe ni barua ya upendo \n Amemtumia bibi arusi wake \n Ameituma hata picha angani \n Yamaanisha kwamba yuaja tena. \n  \n Kama wajua wale wapasambio \n Hakuna siri kati yao wawili \n Na kwa upendo wa Bwana Yesu \n Ametufunulia siri za neno. \n  \n PAMBIO \n Ujumbe ni barua ya upendo \n Amemtumia bibi arusi wake \n Ameituma hata picha angani \n Yamaanisha kwamba yuaja tena. \n  \n Tuna ufunuo wa mihuri saba \n Ukitimiza siri zote za Neno \n Yaliyobaki na hayakufunuliwa \n Malaika wa saba ameyafunua. \n  \n PAMBIO \n Ujumbe ni barua ya upendo \n Amemtumia bibi arusi wake \n Ameituma hata picha angani \n Yamaanisha kwamba yuaja tena. \n  \n Je! Umewahi kukiona kizazi \n Ambacho Bwana wamempiga picha \n Ama wakati malaika wa Bwana  \n Ameshuka na kujichora angani. \n  \n PAMBIO \n Ujumbe ni barua ya upendo \n Amemtumia bibi arusi wake \n Ameituma hata picha angani \n Yamaanisha kwamba yuaja tena. \n  \n Sasa twaelekea mvuto wa tatu \n Nguvu kamilifu za Bibi Arusi \n Hapo madhehebu yatanyamazishwa \n Luka kumi na saba thelathini. \n  \n PAMBIO \n Ujumbe ni barua ya upendo \n Amemtumia bibi arusi wake \n Ameituma hata picha angani \n Yamaanisha kwamba yuaja tena. \n  \n Sa wapenzi mmejaliwa neema \n Ya kumjua Yesu Kristo ni nani? \n Liishi Neno na umche Mungu \n Maisha yako yahubirie wengine. \n  \n PAMBIO \n Ujumbe ni barua ya upendo \n Amemtumia bibi arusi wake \n Ameituma hata picha angani \n Yamaanisha kwamba yuaja tena."));

			SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		    localEditor.putBoolean("vsb_notepad_first_use", true);
		    localEditor.commit();
		} 
				
		// get all songs
		list = db.getAllSongs();
		List<String> listTitle = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {
			listTitle.add(i, list.get(i).getTitle());
		}

		myAdapter = new ArrayAdapter<String>(this, R.layout.notepad_row_layout, R.id.listText, listTitle);
		getListView().setOnItemClickListener(this);
		setListAdapter(myAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// start SongActivity with extras the song id
		Intent intent = new Intent(this, OwnSongView.class);
		intent.putExtra("song", list.get(arg2).getId());
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// get all songs again, because something changed
		list = db.getAllSongs();

		List<String> listTitle = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {
			listTitle.add(i, list.get(i).getTitle());
		}

		myAdapter = new ArrayAdapter<String>(this, R.layout.notepad_row_layout, R.id.listText, listTitle);
		getListView().setOnItemClickListener(this);
		setListAdapter(myAdapter);
	}
	
}
