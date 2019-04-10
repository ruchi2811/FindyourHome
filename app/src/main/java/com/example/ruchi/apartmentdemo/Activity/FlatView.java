package com.example.ruchi.apartmentdemo.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

public class FlatView extends AppCompatActivity {

    int flatId;
    QueryUtility myQuery;
    TextView viewFlatName, viewFlatCity, viewFlatAddress;
    ImageView viewFlatAvatar;
    //Button deleteflat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_view);
        getSupportActionBar().setTitle("Flat Details");
        //flatId = getIntent().getStringExtra("flatID");
        flatId = getIntent().getIntExtra("flatID",0);
        viewFlatCity = findViewById(R.id.viewFlatCity);
        viewFlatAvatar = findViewById(R.id.viewflatAvatar);
        viewFlatName = findViewById(R.id.viewFlatName);
        viewFlatAddress = findViewById(R.id.viewFlatAddress);
        //deleteflat = (Button)findViewById(R.id.delete);

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

    public void delete(View view) {
        // call this method
        QueryUtility.getInstance().deleteOneFlat(flatId);
        startActivity(new Intent(this, OwnerDashboardActivity.class));
        finish();

    }




    //public void delete(View view) {
       // myQuery.deleteFlat(flatId), viewFlatName.getText().toString(), viewFlatAddress.getText().toString(), viewFlatCity.getText().toString(),viewFlatAvatar.toString().getBytes());
       // Toast.makeText(this, "Successfully deleted a flat", Toast.LENGTH_SHORT).show();
       // startActivity(new Intent(this, OwnerDashboardActivity.class));
    //}



    private class GetDatabaseTask extends AsyncTask<Void, Void, QueryUtility> {

        @Override
        protected QueryUtility doInBackground(Void... voids) {
            return QueryUtility.getInstance(getApplicationContext());
        }

        @Override
        protected void onPostExecute(QueryUtility queryUtility) {
            myQuery = queryUtility;
            Cursor cursor = myQuery.getFlatData(flatId);
            cursor.moveToLast();
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
