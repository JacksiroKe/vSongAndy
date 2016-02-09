package com.jackson_siro.vsongbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

public class AaaDemo extends FragmentActivity{
	private Button SingNow;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView();
        setUpFragment();
        SingNow = ((Button)findViewById(R.id.proceed));
    }
    void setUpView(){
    	 setContentView(R.layout.sliding);
    }
    void setUpFragment(){
    	 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
         Sliding fragment = new Sliding();
         transaction.replace(R.id.sample_content_fragment, fragment);
         transaction.commit();
    }
    
    public void SingNow(View view)
    {
      startActivity(new Intent(this, SongSearch.class));
      finish();
    }
}
