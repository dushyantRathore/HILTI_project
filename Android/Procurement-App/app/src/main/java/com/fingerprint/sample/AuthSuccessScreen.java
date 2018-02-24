package com.fingerprint.sample;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AuthSuccessScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_success_screen);

        Intent intent = new Intent(this, DecoderActivity.class);
        this.startActivity(intent);

    }
}
