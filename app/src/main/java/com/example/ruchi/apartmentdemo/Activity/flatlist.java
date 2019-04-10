package com.example.ruchi.apartmentdemo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ruchi.apartmentdemo.Helper.CustomAdapter;
import com.example.ruchi.apartmentdemo.R;

public class flatlist extends AppCompatActivity {
    ListView list;
    String[] itemname ={
            "A Block",
            "B Block ",
            "C Block ",
            "D Block ",
            "E Block "
    };
    String[] flatcity ={
            "Anand",
            "Vadodara",
            "Vadodara",
            "Surat",
            "Surat"
    };

    Integer[] imgid={
            R.drawable.person,
            R.drawable.pn,
            R.drawable.hai,
            R.drawable.man,
            R.drawable.model
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatlist);
        getSupportActionBar().setTitle("Your Flat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CustomAdapter adapter=new CustomAdapter(this, itemname, imgid,flatcity);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}