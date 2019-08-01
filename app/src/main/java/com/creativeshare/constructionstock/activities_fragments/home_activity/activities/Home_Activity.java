package com.creativeshare.constructionstock.activities_fragments.home_activity.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.sign_in_sign_up_activity.activity.Login_Activity;

public class Home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }



    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, Login_Activity.class);
        intent.putExtra("sign_up",isSignIn);
        startActivity(intent);
        finish();

    }

}
