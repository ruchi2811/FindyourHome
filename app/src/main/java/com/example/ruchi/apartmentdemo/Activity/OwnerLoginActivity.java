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

public class OwnerLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);
        getSupportActionBar().setTitle("Owner Login");
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


        /* Dummy Sign-in procedure - Used only for development purposes
        *
        * Signs in the user if: id = owner; password = owner123
        *
        * */

        if(userID.getText().toString().trim().equals("owner")) {
            if(password.getText().toString().trim().equals("owner123")) {
                Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                QueryUtility queryUtility = QueryUtility.getInstance(getApplicationContext());
                queryUtility.setSessionTable("owner", "owner123");
                startActivity(new Intent(this, OwnerDashboardActivity.class));
                finish();
            } else {
                password.setError("Incorrect password");
            }
        } else {
            userID.setError("Incorrect ID");
        }
    }

    public void handleTroubleSigningIn(View view) {
        new AlertDialog.Builder(this).setMessage("If you have forgotten your password, you can generate a new one using an OTP.\n\nIf you have forgotten your user ID, please contact the administrator.")
                .setTitle("Trouble signing in")
                .setPositiveButton(
                        "Forgot Password",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(OwnerLoginActivity.this, "Assume that OTP is sent to your registered mobile number", Toast.LENGTH_LONG).show();
                            }
                        }
                )
                .setNegativeButton(
                        "Forgot ID",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(OwnerLoginActivity.this, "Please contact your administrator for help.", Toast.LENGTH_LONG).show();
                            }
                        }
                )
                .setNeutralButton(
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

