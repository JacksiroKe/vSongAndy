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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jackson_siro.visongbook.tools.*;

public class BbRegister extends AppFunctions implements LoaderCallbacks<Cursor> {
	JSONParser jParser = new JSONParser();
	SongBookSQLite sbDB = new SongBookSQLite(this, SongBookDatabase.DATABASE, null, SongBookDatabase.VERSION);
		
	public EditText mFirstnameView, mLastnameView, mChurchView, mCityView;
	public TextView mCountryView, mMobileView;
	public View mProgressView;
	public Button mSignupNow;
	public ScrollView  mSignupForm;
	SharedPreferences vSettings;
	SharedPreferences.Editor localEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cc_register);
		vSettings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		
		mSignupForm = (ScrollView) findViewById(R.id.signup_form);

		mFirstnameView = (EditText) findViewById(R.id.firstname);
		mLastnameView = (EditText) findViewById(R.id.lastname);
		mCityView = (EditText) findViewById(R.id.city);
		mChurchView = (EditText) findViewById(R.id.church);
		mCountryView = (TextView) findViewById(R.id.country);
		mMobileView = (TextView) findViewById(R.id.mobile);
		
		mSignupNow = (Button) findViewById(R.id.signmeup);
		
		mProgressView = findViewById(R.id.signing_progress);
		
		mCountryView.setText(vSettings.getString("as_vsb_country_name", "NA"));
		mMobileView.setText(vSettings.getString("as_vsb_mobile_phone", "NA"));
		
		mCityView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.signin || id == EditorInfo.IME_NULL) {
					if (isInternetOn()){
						SignMeUp();
						return true;
					} else {
						showProgress(false);
						mCityView.setError(getString(R.string.cant_connect));
						return false;
					}
				}
				return false;
			}
		});
		changeStatusBarColor();
	}

	public void RegisterMeNow(View view)   {		
		if (isInternetOn())SignMeUp();
	    else {
	    	showProgress(false);
	    	mCityView.setError(getString(R.string.cant_connect));
	    }
	}

	public void SignMeUp() {
		String fname = mFirstnameView.getText().toString();
		String lname = mLastnameView.getText().toString();
		String country = mCountryView.getText().toString();
		String city = mCityView.getText().toString();
		String church = mChurchView.getText().toString();
		String mobile = mMobileView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(fname)) {
			mFirstnameView.setError(getString(R.string.error_field_required));
			focusView = mFirstnameView;
			cancel = true;
		}
		
		if (fname.length() < 4) {
			mFirstnameView.setError(getString(R.string.error_field_required));
			focusView = mFirstnameView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(lname)) {
			mLastnameView.setError(getString(R.string.error_field_required));
			focusView = mLastnameView;
			cancel = true;
		}

		if (lname.length() < 4) {
			mLastnameView.setError(getString(R.string.error_field_required));
			focusView = mLastnameView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(country)) {
			mCountryView.setError(getString(R.string.error_field_required));
			focusView = mCountryView;
			cancel = true;
		}

		if (country.length() < 3) {
			mCountryView.setError(getString(R.string.error_field_required));
			focusView = mCountryView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(city)) {
			mCityView.setError(getString(R.string.error_field_required));
			focusView = mCityView;
			cancel = true;
		}

		if (city.length() < 5) {
			mCityView.setError(getString(R.string.error_field_required));
			focusView = mCityView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(church)) {
			mChurchView.setError(getString(R.string.error_field_required));
			focusView = mChurchView;
			cancel = true;
		}

		if (church.length() < 5) {
			mChurchView.setError(getString(R.string.error_field_required));
			focusView = mChurchView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mobile)) {
			mMobileView.setError(getString(R.string.error_invalid_mobile));
			focusView = mMobileView;
			cancel = true;
		}
		
		if (mobile.contains("+")) {
			mMobileView.setError(getString(R.string.error_invalid_mobile));
			focusView = mMobileView;
			cancel = true;
		}

		if (mobile.length() < 10) {
			mMobileView.setError(getString(R.string.error_invalid_mobile));
			focusView = mMobileView;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			new UserSignupTask().execute();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
			mSignupForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
			mSignupForm.setVisibility(show ? View.GONE : View.VISIBLE);
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

	public class UserSignupTask extends AsyncTask<String, Void, Integer> {
		String siteurl = vSettings.getString("as_vsb_siteurl", "NA") + "as_mobile/as_users_signup.php";

		String firstname = mFirstnameView.getText().toString();
		String lastname = mLastnameView.getText().toString();
		String country = mCountryView.getText().toString();
		String city = mCityView.getText().toString();
		String church = mChurchView.getText().toString();
		String mobile = mMobileView.getText().toString();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(true);
		}
		
		protected Integer doInBackground(String ...args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("mobile", mobile));
			params.add(new BasicNameValuePair("firstname", firstname));
			params.add(new BasicNameValuePair("lastname", lastname));
			params.add(new BasicNameValuePair("country", country));
			params.add(new BasicNameValuePair("city", city));
			params.add(new BasicNameValuePair("church", church));
			params.add(new BasicNameValuePair("haspaid", "0"));
			params.add(new BasicNameValuePair("sex", "1"));
			
			int mSuccessful = 0;
			try {
				JSONObject json = jParser.makeHttpRequest(siteurl, "POST", params);
				Log.d("Signing up", json.toString());
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
	
	public void couldNotValidate() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle(R.string.just_a_min);
		 builder.setMessage(R.string.could_not_register);
		 builder.setNegativeButton(R.string.retry_this, new DialogInterface.OnClickListener() {					
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				ErrorShowNow();
			}
		});
       builder.show();
	}
	
	public void nextActivity(){
		setLoggedIn(); 
		localEditor.putString("as_vsb_userid", sbDB.getOption("userid"));
		localEditor.putString("as_vsb_mobile_phone", sbDB.getOption("user_mobile"));
	    localEditor.commit();
	    
		if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("as_vsb_finished_loading", false))
			startActivity(new Intent(this, CcFirstLoad.class));
		else startActivity(new Intent(this, CcSongBook.class));
		finish();
	}

	public void ErrorShowNow(){
		mFirstnameView.setError(null);
		mLastnameView.setError(null);
		mCountryView.setError(null);
		mChurchView.setError(null);
		mCityView.setError(null);
		mMobileView.setError(null);
		mFirstnameView.requestFocus();
	}
}
