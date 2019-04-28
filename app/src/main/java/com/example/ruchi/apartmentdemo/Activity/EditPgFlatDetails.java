package com.example.ruchi.apartmentdemo.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditPgFlatDetails extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView avatar;
    Cursor pgflatDataCursor;
    int pgId;

    private class GetDatabaseTask extends AsyncTask<Void, Void, QueryUtility> {

        @Override
        protected QueryUtility doInBackground(Void... voids) {
            return QueryUtility.getInstance(getApplicationContext());
        }

        @Override
        protected void onPostExecute(QueryUtility queryUtility) {
            myQuery = queryUtility;
            pgflatDataCursor = myQuery.getpgFlatData(null);
            pgflatDataCursor.moveToLast();
            pgId = pgflatDataCursor.getInt(pgflatDataCursor.getColumnIndexOrThrow("_id"));
//            Toast.makeText(EditFlatDetails.this, ""+flatId, Toast.LENGTH_SHORT).show();
            pgId = pgId + 2;
        }
    }


    QueryUtility myQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pg_flat_details);
        getSupportActionBar().setTitle("Edit PG Flat Details");
        avatar = findViewById(R.id.editFlatAvatar);
        avatar.setImageDrawable(getDrawable(R.drawable.flat));
        GetDatabaseTask getDatabaseTask = new GetDatabaseTask();
        getDatabaseTask.execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.saveButton) {
            // This should handle SAVE action


            EditText editpgFlatName = findViewById(R.id.editpgFlatName);
            EditText editpgFlatCity = findViewById(R.id.editpgFlatCity);
            EditText editpgFlatAddress = findViewById(R.id.editpgFlatAddress);
            ImageView editpgFlatAvatar = findViewById(R.id.editpgFlatAvatar);
            editpgFlatAvatar.setDrawingCacheEnabled(true);
            editpgFlatAvatar.buildDrawingCache();
            Bitmap bitmapAvatar = Bitmap.createBitmap(editpgFlatAvatar.getDrawingCache());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] avatarBlob = bos.toByteArray();
            String pgName = editpgFlatName.getText().toString().trim();
            String pgCity = editpgFlatCity.getText().toString().trim();
            String pgAddress = editpgFlatAddress.getText().toString().trim();
            if(pgName.isEmpty()) {
                editpgFlatName.setError("Please enter the pg flat name");
                editpgFlatName.requestFocus();
                return false;
            }
            if(pgCity.isEmpty()) {
                editpgFlatCity.setError("Please enter the pg flat city");
                editpgFlatCity.requestFocus();
                return false;
            }
            if(pgAddress.isEmpty()) {
                editpgFlatAddress.setError("Please enter the pg flat address");
                editpgFlatAddress.requestFocus();
                return false;
            }


            myQuery.insertpgFlat(pgId, pgName, pgAddress, pgCity, avatarBlob);
            //Toast.makeText(this, "Successfully inserted new flat", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "PG FLAT ID : " + pgId, Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, OwnerDashboardActivity.class));




        }
        return super.onOptionsItemSelected(item);
    }

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
                ImageView flatAvatar = findViewById(R.id.editpgFlatAvatar);
                flatAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}