package com.example.ruchi.apartmentdemo.Helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruchi.apartmentdemo.R;

public class CustomPgAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] pgname;
    private final String[] pgcity;
    private final Integer[] imgid;

    public CustomPgAdapter(Activity context, String[] itemname, Integer[] imgid,String[] flatcity) {
        super(context, R.layout.custom_pg, itemname);
        this.context=context;
        this.pgname=itemname;
        this.pgcity=flatcity;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_pg, null,true);

        TextView txtTitle = rowView.findViewById(R.id.pgname);
        ImageView imageView = rowView.findViewById(R.id.pgimage);
        TextView extratxt = rowView.findViewById(R.id.pgcity);

        txtTitle.setText(pgname[position]);
        imageView.setImageResource(imgid[position]);
        extratxt.setText(pgcity[position]);
        return rowView;

    }
}
