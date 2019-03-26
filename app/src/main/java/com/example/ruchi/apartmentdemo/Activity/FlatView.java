package com.example.ruchi.apartmentdemo.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

public class FlatView extends AppCompatActivity {

    String flatId;
    QueryUtility myQuery;
    TextView viewFlatName, viewFlatCity, viewFlatAddress;
    ImageView viewFlatAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_view);
        getSupportActionBar().setTitle("Flat Details");
        flatId = getIntent().getStringExtra("flatID");
        viewFlatAddress = findViewById(R.id.viewFlatAddress);
        viewFlatAvatar = findViewById(R.id.viewflatAvatar);
        viewFlatName = findViewById(R.id.viewFlatName);
        viewFlatCity = findViewById(R.id.viewFlatCity);
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

    private class GetDatabaseTask extends AsyncTask<Void, Void, QueryUtility> {

        @Override
        protected QueryUtility doInBackground(Void... voids) {
            return QueryUtility.getInstance(getApplicationContext());
        }

        @Override
        protected void onPostExecute(QueryUtility queryUtility) {
            myQuery = queryUtility;
            Cursor cursor = myQuery.getFlatData(Integer.parseInt(flatId));
            cursor.moveToFirst();
            byte[] byteArray = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            viewFlatAvatar.setImageBitmap(bm);
            viewFlatName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            viewFlatCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city")));
            viewFlatAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            cursor.close();
        }
    }
}
