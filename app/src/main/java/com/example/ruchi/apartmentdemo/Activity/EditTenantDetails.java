package com.example.ruchi.apartmentdemo.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditTenantDetails extends AppCompatActivity {

    String[] randomList = {"AZ", "Z3", "4R", "K2", "FU", "7E", "3T", "5Z", "SX", "XS", "AA", "JF"};

    public static int getRandom() {
        return (int)(Math.ceil(Math.random() * 10));
    }

    ImageView avatar;
    ArrayList<String> flatNameList;
    Cursor flatDataCursor;
    Spinner flatChoiceSpinner;

    private int PICK_IMAGE_REQUEST = 1;

    public void getImageFromUser(View view) {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView tenantAvatar = findViewById(R.id.editTenantAvatar);
                tenantAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateID(View view) {
        EditText idField = findViewById(R.id.editTenantId);
        EditText editTenantNameField = findViewById(R.id.editTenantName);
        String name = editTenantNameField.getText().toString();
        if(name.isEmpty()) {
            idField.setText("tenant" + getRandom() + getRandom());
        } else {
            idField.setText(name.replaceAll("\\s","") + getRandom() + getRandom());
        }
    }

    public void generatePassword(View view) {
        EditText passwordField = findViewById(R.id.editPassword);
        String password = randomList[getRandom()] + randomList[getRandom()] + randomList[getRandom()];
        passwordField.setText(password);
    }


    private class GetDatabaseTask extends AsyncTask<Void, Void, QueryUtility> {

        @Override
        protected QueryUtility doInBackground(Void... voids) {
            return QueryUtility.getInstance(getApplicationContext());
        }

        @Override
        protected void onPostExecute(QueryUtility queryUtility) {
            myQuery = queryUtility;
            flatNameList = new ArrayList<>();
            flatDataCursor = myQuery.getFlatData(null);
            flatDataCursor.moveToFirst();
            while(!flatDataCursor.isAfterLast()) {
                flatNameList.add(flatDataCursor.getString(flatDataCursor.getColumnIndexOrThrow("name")));
                flatDataCursor.moveToNext();
            }
            flatChoiceSpinner = findViewById(R.id.flatSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditTenantDetails.this, android.R.layout.simple_spinner_item, flatNameList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            flatChoiceSpinner.setAdapter(adapter);
        }
    }

    QueryUtility myQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tenant_details);
        getSupportActionBar().setTitle("Edit Tenant Details");
        avatar = findViewById(R.id.editTenantAvatar);
        avatar.setImageDrawable(getDrawable(R.drawable.man));
        GetDatabaseTask getDatabaseTask = new GetDatabaseTask();
        getDatabaseTask.execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.saveButton) {
            // This should handle SAVE action
            String tenantName, tenantContact, tenantEmail, tenantId, tenantPassword, tenantFlat;
            int tenantRent, tenantCharges;
            EditText tenantNameField = findViewById(R.id.editTenantName);
            EditText tenantContactField = findViewById(R.id.editTenantContact);
            EditText tenantEmailField = findViewById(R.id.editTenantEmail);
            EditText tenantIdField = findViewById(R.id.editTenantId);
            EditText tenantPasswordField = findViewById(R.id.editPassword);
            Spinner tenantFlatField = findViewById(R.id.flatSpinner);
            String tenantFlatSelection = tenantFlatField.getSelectedItem().toString();
            EditText tenantRentField = findViewById(R.id.editTenantRent);
            EditText tenantChargesField = findViewById(R.id.editTenantCharges);
            ImageView editTenantAvatar = findViewById(R.id.editTenantAvatar);
            editTenantAvatar.setDrawingCacheEnabled(true);
            editTenantAvatar.buildDrawingCache();
            Bitmap bitmapAvatar = Bitmap.createBitmap(editTenantAvatar.getDrawingCache());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] avatarBlob = bos.toByteArray();
            if(tenantNameField.getText().toString().isEmpty()) {
                tenantNameField.setError("Please enter tenant name");
                tenantNameField.requestFocus();
                return false;
            }
            if(tenantContactField.getText().toString().isEmpty()) {
                tenantContactField.setError("Please enter tenant contact");
                tenantContactField.requestFocus();
                return false;
            }
            if(tenantEmailField.getText().toString().isEmpty()) {
                tenantEmailField.setError("Please enter tenant email");
                tenantEmailField.requestFocus();
                return false;
            }
            if(tenantIdField.getText().toString().isEmpty()) {
                tenantIdField.setError("Please enter tenant id");
                tenantIdField.requestFocus();
                return false;
            }
            if(tenantPasswordField.getText().toString().isEmpty()) {
                tenantPasswordField.setError("Please enter tenant password");
                tenantPasswordField.requestFocus();
                return false;
            }
            if(tenantRentField.getText().toString().isEmpty()) {
                tenantRentField.setError("Please enter tenant rent");
                tenantRentField.requestFocus();
                return false;
            }
            if(tenantChargesField.getText().toString().isEmpty()) {
                tenantChargesField.setError("Please enter tenant charges");
                tenantChargesField.requestFocus();
                return false;
            }
            tenantName = tenantNameField.getText().toString().trim();
            tenantContact = tenantContactField.getText().toString().trim();
            tenantEmail = tenantEmailField.getText().toString().trim();
            tenantPassword = tenantPasswordField.getText().toString().trim();
            tenantRent = Integer.parseInt(tenantRentField.getText().toString().trim());
            tenantCharges = Integer.parseInt(tenantChargesField.getText().toString().trim());
            tenantId = tenantIdField.getText().toString().trim();



            myQuery.insertTenant(tenantId, tenantName, tenantFlatSelection, tenantContact, tenantEmail, tenantPassword, tenantRent, tenantCharges, avatarBlob);

            Toast.makeText(this, "Successfully inserted new tenant", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, OwnerDashboardActivity.class));
            //S (_id text PRIMARY KEY, name text, flat text, FOREIGNKEY flat references flats(name), contact text, email text, password text, rent int, charges int, image blob)";


        }
        return super.onOptionsItemSelected(item);
    }

}

