package com.example.ruchi.apartmentdemo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

public class TenantLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_login);
        getSupportActionBar().setTitle("Tenant Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void performSignIn(View view) {
        EditText userID = (EditText) findViewById(R.id.idEditText);
        EditText password = (EditText) findViewById(R.id.passwordEditText);
        if(userID.getText().toString().isEmpty()) {
            userID.setError("Please enter your ID");
            return;
        }
        if(password.getText().toString().isEmpty()) {
            password.setError("Please enter your password");
            return;
        }
        if(QueryUtility.getInstance(this).allowPassage(userID.getText().toString().trim(), password.getText().toString().trim())) {
            Intent tenantIntent = new Intent(TenantLoginActivity.this, TenantView.class);
            QueryUtility queryUtility = QueryUtility.getInstance(getApplicationContext());
            tenantIntent.putExtra("tenantID", userID.getText().toString().trim());
            tenantIntent.putExtra("isOwner", false);
            startActivity(tenantIntent);
        } else {
            Toast.makeText(this, "Oops, try again!", Toast.LENGTH_SHORT).show();
        }

    }

    public void handleTroubleSigningIn(View view) {
        new AlertDialog.Builder(this).setMessage("Please contact the flat owner who has registered your tenant account for your login credentials.\n\nYou can request new ID from the owner if you want.")
                .setTitle("Trouble signing in")
                .setPositiveButton(
                        "Request new ID",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(TenantLoginActivity.this, "Request sent to your flat owner", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                ).setCancelable(false).create().show();

    }
}

