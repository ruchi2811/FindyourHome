package com.example.ruchi.apartmentdemo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ruchi.apartmentdemo.R;

public class aboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    public void feedback(View view) {
        sendMail();

    }
    private void sendMail()
    {
        Intent y = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","findyourhome@gmail.com",null));
        y.putExtra(Intent.EXTRA_SUBJECT,"Send your feedback about my application");
        startActivity(y.createChooser(y,"Send feedback"));

    }
    public void back(View view) {
        startActivity(new Intent(this, MainActivity.class));

    }
}
