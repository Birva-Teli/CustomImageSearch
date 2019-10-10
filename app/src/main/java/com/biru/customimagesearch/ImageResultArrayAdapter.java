package com.biru.customimagesearch;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResults> {
    public ImageResultArrayAdapter( Context context, ArrayList<ImageResults> images) {
        super(context, R.layout.item_img_result,images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResults imageResults=this.getItem(position);
        ImageView imageView;
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(getContext());
            imageView=(ImageView) layoutInflater.inflate(R.layout.item_img_result,parent,false);
        }
        else {
            imageView=(ImageView) convertView;
            imageView.setImageResource(android.R.color.transparent);
        }
        imageView.setImageURI(Uri.parse(imageResults.getThumbURL()));
        //imageResults.getThumblURL()
        return imageView;
    }
}
