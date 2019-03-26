package com.example.ruchi.apartmentdemo.Helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruchi.apartmentdemo.R;

public class CustomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] flatname;
    private final String[] flatcity;
    private final Integer[] imgid;

    public CustomAdapter(Activity context, String[] itemname, Integer[] imgid,String[] flatcity) {
        super(context, R.layout.custom, itemname);
        this.context=context;
        this.flatname=itemname;
        this.flatcity=flatcity;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom, null,true);

        TextView txtTitle = rowView.findViewById(R.id.name);
        ImageView imageView = rowView.findViewById(R.id.image);
        TextView extratxt = rowView.findViewById(R.id.city);

        txtTitle.setText(flatname[position]);
        imageView.setImageResource(imgid[position]);
        extratxt.setText(flatcity[position]);
        return rowView;

    }
}
