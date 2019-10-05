package com.jackson_siro.visongbook.ui.notification;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import com.jackson_siro.visongbook.retrofitconfig.BaseUrlConfig;
import com.jackson_siro.visongbook.ui.MyApplication;

public class FcmInstanceIdService extends FirebaseInstanceIdService {


    SharedPreferences prefget = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

//        WHEN YOU WANT USE LOGIN AND STORE TOKEN TO DB, YOU CAN USE SHAREDPREFERENCES SUCH AS BELOW
//        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(getString(R.string.FCM_TOKEN), token);
//        editor.commit();
        storeToken(token);
    }

    private void storeToken(final String token) {
        StringRequest request = new StringRequest(Request.Method.POST, prefget.getString("app_base_url",
                BaseUrlConfig.BaseUrl) + BaseUrlConfig.AppToken, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("fcm_token", token);
                Log.d("TOKENNNNNNNN8 8888", token);
                return parameters;
            }
        };
        MySingleton.getmInstance(FcmInstanceIdService.this).addToRequest(request);
    }
}
