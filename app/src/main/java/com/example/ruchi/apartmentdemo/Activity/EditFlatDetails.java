package com.example.ruchi.apartmentdemo.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class EditFlatDetails extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView avatar;
    Cursor flatDataCursor;
    int flatId;

    private class GetDatabaseTask extends AsyncTask<Void, Void, QueryUtility> {

        @Override
        protected QueryUtility doInBackground(Void... voids) {
            return QueryUtility.getInstance(getApplicationContext());
        }

        @Override
        protected void onPostExecute(QueryUtility queryUtility) {
            myQuery = queryUtility;
            flatDataCursor = myQuery.getFlatData(null);
            flatDataCursor.moveToLast();
            flatId = flatDataCursor.getInt(flatDataCursor.getColumnIndexOrThrow("_id"));
//            Toast.makeText(EditFlatDetails.this, ""+flatId, Toast.LENGTH_SHORT).show();
            flatId = flatId + 2;
        }
    }


    QueryUtility myQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flat_details);
        getSupportActionBar().setTitle("Edit Flat Details");
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


            EditText editFlatName = findViewById(R.id.editFlatName);
            EditText editFlatCity = findViewById(R.id.editFlatCity);
            EditText editFlatAddress = findViewById(R.id.editFlatAddress);
            EditText editPgAvailability = findViewById(R.id.pgAvailable);
            ImageView editFlatAvatar = findViewById(R.id.editFlatAvatar);
            editFlatAvatar.setDrawingCacheEnabled(true);
            editFlatAvatar.buildDrawingCache();
            Bitmap bitmapAvatar = Bitmap.createBitmap(editFlatAvatar.getDrawingCache());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] avatarBlob = bos.toByteArray();
            String flatName = editFlatName.getText().toString().trim();
            String flatCity = editFlatCity.getText().toString().trim();
            String pgAvailability = editPgAvailability.getText().toString().trim();
            String flatAddress = editFlatAddress.getText().toString().trim();
            if(flatName.isEmpty()) {
                editFlatName.setError("Please enter the flat name");
                editFlatName.requestFocus();
                return false;
            }
            if(flatCity.isEmpty()) {
                editFlatCity.setError("Please enter the flat city");
                editFlatCity.requestFocus();
                return false;
            }
            if(flatAddress.isEmpty()) {
                editFlatAddress.setError("Please enter the flat address");
                editFlatAddress.requestFocus();
                return false;
            }

            if(pgAvailability.isEmpty()) {
                editPgAvailability.setError("Please enter the availability");
                editPgAvailability.requestFocus();
                return false;
            }



            myQuery.insertFlat(flatId, flatName, flatAddress, flatCity,avatarBlob);
            //Toast.makeText(this, "Successfully inserted new flat", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Flat ID : " + flatId, Toast.LENGTH_SHORT).show();

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
                ImageView flatAvatar = findViewById(R.id.editFlatAvatar);
                flatAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
