package com.example.ruchi.apartmentdemo.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

public class pgView extends AppCompatActivity {

    int pgId;
    QueryUtility myQuery;
    TextView viewpgName, viewpgCity, viewpgAddress;
    ImageView viewpgAvatar;
    //Button deleteflat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_view);
        getSupportActionBar().setTitle("PG Details");
        //flatId = getIntent().getStringExtra("flatID");
        pgId = getIntent().getIntExtra("PGID",0);
        viewpgCity = findViewById(R.id.viewpgCity);
        viewpgAvatar = findViewById(R.id.viewpgAvatar);
        viewpgName = findViewById(R.id.viewpgName);
        viewpgAddress = findViewById(R.id.viewpgAddress);
        //deleteflat = (Button)findViewById(R.id.delete);

        pgView.GetDatabaseTask getDatabaseTask = new pgView.GetDatabaseTask();
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

//    public void delete(View view) {
//        // call this method
//        QueryUtility.getInstance().deleteOneFlat(pgId);
//        startActivity(new Intent(this, OwnerDashboardActivity.class));
//        finish();
//
//    }




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
            Cursor cursor = myQuery.getpgFlatData(pgId);
            cursor.moveToLast();
            byte[] byteArray = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            viewpgAvatar.setImageBitmap(bm);
            viewpgName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            viewpgCity.setText(cursor.getString(cursor.getColumnIndexOrThrow("city")));
            viewpgAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("address")));

            cursor.close();


        }
    }




}
