package com.biru.customimagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageResults {

    private String fullURL;
    private String thumbURL;

    public ImageResults(JSONObject jsonObject) {
        try {
            this.fullURL = jsonObject.getString("url");
            this.thumbURL = jsonObject.getString("tbUrl");
        } catch (JSONException e) {
            this.fullURL = null;
            this.thumbURL = null;

        }
    }

    public static ArrayList<ImageResults> fromJsonArray(JSONArray array) {
        ArrayList<ImageResults> results=new ArrayList<ImageResults>();
        for(int i=0;i<array.length();i++){
            try {
                results.add(new ImageResults(array.getJSONObject(i)));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return "ImageResults{" +
                "fullURL='" + fullURL + '\'' +
                ", thumbURL='" + thumbURL + '\'' +
                '}';
    }

    public String getFullURL() {
        return fullURL;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }
}
