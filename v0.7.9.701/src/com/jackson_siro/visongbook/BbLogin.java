package com.jackson_siro.visongbook;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jackson_siro.visongbook.tools.*;

public class BbLogin extends AppFunctions implements LoaderCallbacks<Cursor> {
	JSONParser jParser = new JSONParser();
	SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
	
	public Spinner mCountryView;
	public TextView mCodeView;
	public EditText mMobileView;
	public View mProgressView;
	public Button mSignInNow;
	public ScrollView mSignInForm;
	public SharedPreferences vSettings;
	public SharedPreferences.Editor localEditor;
	String country_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cc_login);
		vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		mCountryView = (Spinner)findViewById(R.id.countrylist);
		mCodeView = (TextView) findViewById(R.id.country_code);
		mSignInForm = (ScrollView) findViewById(R.id.signin_form);
		mMobileView = (EditText) findViewById(R.id.mobile_phone);
		mSignInNow = (Button) findViewById(R.id.signme_innow);
		
		mProgressView = findViewById(R.id.signing_progress);
		
		String[] countrytitles = getResources().getStringArray(R.array.country_titles);
		final String[] countrycodes = getResources().getStringArray(R.array.country_codes);
		
		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, countrytitles);
		mCountryView.setAdapter(spAdapter);
		mCountryView.setSelection(108);
		country_title = "Kenya";
		mCodeView.setText("254");
		
		mCountryView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
				mCodeView.setText(countrycodes[position].toString());
				country_title = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				//sometimes you need nothing here
			}
		});		

		mMobileView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.signin || id == EditorInfo.IME_NULL) {
					if (isInternetOn()){
						SignMeIn();
						return true;
					} else {
						showProgress(false);
						mMobileView.setError(getString(R.string.cant_connect));
						return false;
					}
				}
				return false;
			}
		});
		changeStatusBarColor();	
	}
	
	public void LoginMeNow(View view)   {		
		if (isInternetOn())SignMeIn();
	    else {
	    	showProgress(false);
			mMobileView.setError(getString(R.string.cant_connect));
	    }
	}
	
	public void SignMeIn() {		
		mMobileView.setError(null);
		String mobile_phone = mMobileView.getText().toString();
		String country_code = mCodeView.getText().toString();
		localEditor.putString("as_vsb_mobile_phone", country_code + mobile_phone);
		localEditor.putString("as_vsb_country_name", country_title);
	    localEditor.commit();
	    
		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mobile_phone)) {
			mMobileView.setError(getString(R.string.error_field_required));
			focusView = mMobileView;
			cancel = true;
		} else if (!is_Signin_nameValid(mobile_phone)) {
			mMobileView.setError(getString(R.string.error_invalid_sign_name));
			focusView = mMobileView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			new UserSigninTask().execute();
		}
	}

	private boolean is_Signin_nameValid(String signin_name) {
		// TODO: Replace this with new own logic
		return signin_name.length() > 2;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mSignInForm.setVisibility(show ? View.GONE : View.VISIBLE);
			mSignInForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mSignInForm.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});
		} else {
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mSignInForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		
		return null;		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {

	}

	public class UserSigninTask extends AsyncTask<String, Void, Integer> {
		String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_users_signin.php";
		String mobile_phone = mCodeView.getText().toString() + mMobileView.getText().toString();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(true);
		}
		
		protected Integer doInBackground(String ...args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mobile", mobile_phone));
			
			int mSuccessful = 0;
			try {
				JSONObject json = jParser.makeHttpRequest(siteurl, "GET", params);
				Log.d("Signing in", json.toString());
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					JSONArray userObj = json.getJSONArray(TAG_USER); 
					JSONObject user = userObj.getJSONObject(0);
										
					sbDB.updateOption("userid", user.getString(TAG_USERID)); 
					sbDB.updateOption("user_name", user.getString(TAG_USERNAME));
					sbDB.updateOption("user_fname", user.getString(TAG_FNAME)); 
					sbDB.updateOption("user_lname", user.getString(TAG_LNAME)); 
					sbDB.updateOption("user_email", user.getString(TAG_EMAIL)); 
					sbDB.updateOption("user_level", user.getString(TAG_LEVEL)); 
					sbDB.updateOption("user_mobile", user.getString(TAG_MOBILE)); 
					sbDB.updateOption("user_church", user.getString(TAG_CHURCH)); 
					sbDB.updateOption("user_country", user.getString(TAG_COUNTRY)); 
					sbDB.updateOption("user_haspaid", user.getString(TAG_HASPAID)); 
					sbDB.updateOption("user_joined", user.getString(TAG_JOINED));
					sbDB.updateOption("user_haspaid", user.getString(TAG_HASPAID));
					sbDB.updateOption("user_amountpaid", user.getString(TAG_AMOUNT));
					sbDB.updateOption("user_device", user.getString(TAG_DEVICE)); 
					sbDB.updateOption("user_sex", user.getString(TAG_SEX)); 
					sbDB.updateOption("user_about", user.getString(TAG_ABOUT));  
					sbDB.updateOption("user_scount", user.getString(TAG_SCOUNT));  
					sbDB.updateOption("user_avatar", user.getString(TAG_AVATAR));
					setLoggedIn();
					mSuccessful = 2;
				} else mSuccessful = 1;
			} catch (JSONException e) {
				e.printStackTrace();
				mSuccessful = 0;
			}
			return mSuccessful;
		}

		protected void onPostExecute(Integer mSuccessful) {
			showProgress(false);
			if (mSuccessful == 0) ErrorShowNow();
			else if (mSuccessful == 1) couldNotValidate();
			else if (mSuccessful == 2) nextActivity();
		}
	}
	public void userWasLogged(boolean mSuccessful) {
		if (mSuccessful){
			if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_finished_loading", false))
				startActivity(new Intent(this, CcFirstLoad.class));
			else startActivity(new Intent(this, CcSongBook.class));
			finish();
		} else {
			couldNotValidate();
		}
	}
	
	public void nextActivity(){
		setLoggedIn(); 
		localEditor.putString("as_vsb_userid", sbDB.getOption("userid"));
		localEditor.commit();
	    
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_finished_loading", false))
			startActivity(new Intent(this, CcFirstLoad.class));
		else startActivity(new Intent(this, CcSongBook.class));
		finish();
	}
	
	public void couldNotValidate() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle(R.string.just_a_min);
		 builder.setMessage(R.string.could_not_login);
		 builder.setPositiveButton(R.string.register_btn, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				GotoRegistration();
			}
		 });
        builder.setNegativeButton(R.string.try_btn, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ErrorShowNow();
			}
		});
        builder.show();
	 }

	public void GotoRegistration(){
		startActivity(new Intent(this, BbRegister.class));
	}
	public void ErrorShowNow(){
		mMobileView.setError(getString(R.string.error_incorrect_mobile));
		mMobileView.requestFocus();
	}
	

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.proceed, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donehere:
            	SignMeIn();
                return true;
            
            default:
                return false;
        }
    }
    
}
