package com.example.ruchi.apartmentdemo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

public class TenantView extends AppCompatActivity {

    String tenantId;
    Button paymentButton;
    Button contactButton;
    QueryUtility myQuery;
    TextView viewTenantName, viewTenantContact, viewTenantEmail, viewTenantRent, viewTenantCharges, viewTenantFlat, billAmount;
    ImageView viewTenantAvatar;

    private class GetDatabaseTask extends AsyncTask<Void, Void, QueryUtility> {

        @Override
        protected QueryUtility doInBackground(Void... voids) {
            return QueryUtility.getInstance(getApplicationContext());
        }

        @Override
        protected void onPostExecute(QueryUtility queryUtility) {
            myQuery = queryUtility;
            Cursor cursor = myQuery.getTenantData(tenantId);
            cursor.moveToFirst();
            byte[] byteArray = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
            viewTenantAvatar.setImageBitmap(bm);
            viewTenantName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            viewTenantContact.setText(cursor.getString(cursor.getColumnIndexOrThrow("contact")));
            viewTenantEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            viewTenantFlat.setText(cursor.getString(cursor.getColumnIndexOrThrow("flat")));
            viewTenantRent.setText(Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow("rent"))));
            viewTenantCharges.setText(Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow("charges"))));
            billAmount.setText(Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow("rent")) + cursor.getInt(cursor.getColumnIndexOrThrow("charges"))));
            cursor.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_view);
        getSupportActionBar().setTitle("Tenant Details");
        tenantId = getIntent().getStringExtra("tenantID");
        contactButton = findViewById(R.id.contactButton);
        paymentButton = findViewById(R.id.paymentButton);
        boolean isOwner = getIntent().getBooleanExtra("isOwner",false);
        if(isOwner) {
            contactButton.setVisibility(View.GONE);
            paymentButton.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Tenant Details");
        } else {
            getSupportActionBar().setTitle("Your Details");
        }
        viewTenantAvatar = findViewById(R.id.viewtenantAvatar);
        viewTenantName = findViewById(R.id.viewTenantName);
        viewTenantContact = findViewById(R.id.viewTenantContact);
        viewTenantEmail = findViewById(R.id.viewTenantEmail);
        viewTenantFlat = findViewById(R.id.viewTenantFlat);
        viewTenantRent = findViewById(R.id.viewTenantRent);
        viewTenantCharges = findViewById(R.id.viewTenantCharges);
        billAmount = findViewById(R.id.billAmount);

        GetDatabaseTask getDatabaseTask = new GetDatabaseTask();
        getDatabaseTask.execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void otp(View view) {
        startActivity(new Intent(this, OTP_Main.class));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            logOut();
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void logOut() {
        QueryUtility.getInstance(getApplicationContext()).setSessionTable("null", "null");
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void makePayment(View view) {
        Toast.makeText(this, "Please verify your number", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, payment.class));
        billAmount = findViewById(R.id.billAmount);
        billAmount.setText("0");
        view.setEnabled(false);
    }

    public void addContact(View view) {
        final EditText contactEditText = new EditText(this);
        contactEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        contactEditText.setMaxLines(1);
        new AlertDialog.Builder(this)
                .setTitle("Add new contact number")
                .setMessage("Enter a valid number")
                .setView(contactEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QueryUtility.getInstance(TenantView.this).addNewContact(contactEditText.getText().toString().trim(), "vin27");
                        recreate();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}
