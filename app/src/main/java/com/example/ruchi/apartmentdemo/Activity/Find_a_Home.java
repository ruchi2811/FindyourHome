package com.example.ruchi.apartmentdemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ruchi.apartmentdemo.Database.QueryUtility;
import com.example.ruchi.apartmentdemo.R;

public class Find_a_Home extends AppCompatActivity {

    ListView list, flatListView;
    OwnerDashboardActivity.FlatCursorAdapter flatCursorAdapter;
    String[] flatNames = {"Sunshine Colony", "City Avenue", "Ganesha", "Green Villa"};

    QueryUtility myQuery;


    public void flats(View view) {
        Intent intent = new Intent(Find_a_Home.this,flatlist.class);
        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_a__home);
        getSupportActionBar().setTitle("Dashboard");

        GetDatabaseTask getDatabaseTask = new GetDatabaseTask();
        getDatabaseTask.execute();
    }

    private class GetDatabaseTask extends AsyncTask<Void, Void, QueryUtility> {

        @Override
        protected QueryUtility doInBackground(Void... voids) {
            return QueryUtility.getInstance(getApplicationContext());
        }



        @Override
        protected void onPostExecute(QueryUtility queryUtility) {
            myQuery = queryUtility;
            final Cursor tenantDataCursor = myQuery.getTenantData(null);
            Cursor flatDataCursor = myQuery.getFlatData(null);
            flatListView = findViewById(R.id.flatList);
            flatCursorAdapter = new OwnerDashboardActivity.FlatCursorAdapter(Find_a_Home.this, flatDataCursor);
            flatListView.setAdapter(flatCursorAdapter);

            flatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //listener for flat list
                    Intent flatDetailsIntent = new Intent(Find_a_Home.this, FlatView.class);
                    flatDetailsIntent.putExtra("flatID", view.getTag().toString());
                    startActivity(flatDetailsIntent);
                }
            });


        }
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

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        QueryUtility.getInstance(getApplicationContext()).setSessionTable("null", "null");
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    public class FlatCursorAdapter extends CursorAdapter {

        public FlatCursorAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.custom, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView flatName = view.findViewById(R.id.name);
            TextView flatCity = view.findViewById(R.id.city);
            ImageView avatar = view.findViewById(R.id.image);
            String flatNameString = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String flatCityString = cursor.getString(cursor.getColumnIndexOrThrow("city"));
            String flatId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            byte[] byteArray = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            flatName.setText(flatNameString);
            flatCity.setText(flatCityString);
            avatar.setImageBitmap(bm);
            view.setTag(flatId);
        }
    }

}