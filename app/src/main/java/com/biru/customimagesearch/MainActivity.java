package com.biru.customimagesearch;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    EditText eText;
    Button btn;
    GridView gridViewImageDisplay;
    private static final String TAG = "searchApp";
    static String result = null;
    Integer responseCode = null;
    String responseMessage = "";
    ArrayList<ImageResults> imageResults = new ArrayList<ImageResults>();
    ImageResultArrayAdapter imageAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eText = (EditText) findViewById(R.id.edittext);
        btn = (Button) findViewById(R.id.button);
        gridViewImageDisplay = (GridView) findViewById(R.id.gridViewImageDisplay);
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        gridViewImageDisplay.setAdapter(imageAdapter);


        //layo

//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                final String searchString = eText.getText().toString();
//                Log.d(TAG, "Searching for : " + searchString);
//             //   resultTextView.setText("Searching for : " + searchString);
//
//                // hide keyboard
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//                // looking for
//                String searchStringNoSpaces = searchString.replace(" ", "+");
//
//                // Your API key
//                // TODO replace with your value
//                String key="AIzaSyBeWavTCsnjDI2MARvutp0bRyhy6uYT-64";
//
//                // Your Search Engine ID
//                // TODO replace with your value
//                String cx = "013537537227831329346:pppbjtqtvi0";
//
//                String urlString = "https://www.googleapis.com/customsearch/v1?q=" + searchStringNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json";
//                URL url = null;
//                try {
//                    url = new URL(urlString);
//                } catch (MalformedURLException e) {
//                    Log.e(TAG, "ERROR converting String to URL " + e.toString());
//                }
//                Log.d(TAG, "Url = "+  urlString);
//
//
//                // start AsyncTask
//                GoogleSearchAsyncTask searchTask = new GoogleSearchAsyncTask();
//                searchTask.execute(url);
//
//            }
//        });

    }

    public void onSearch(View view) {
        String query = eText.getText().toString();
        String key = "AIzaSyBeWavTCsnjDI2MARvutp0bRyhy6uYT-64";
        String cx = "013537537227831329346:pppbjtqtvi0";
        Toast.makeText(this, "Showing Images of " + query, Toast.LENGTH_SHORT).show();
        String searchStringNoSpaces = query.replace(" ", "+");
//https://www.googleapis.com/customsearch/v1?key=YOUR-KEY&cx=013036536707430787589:_pqjad5hr1a&q=flowers&alt=json
        //https://www.googleapis.com/customsearch/v1?key=<YOUR API KEY>&cx=<>&q=java&as_filetype=png
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://www.googleapis.com/customsearch/v1?q="+Uri.encode(query)+"&key="+key+"&cx="+cx+"&alt=json",
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray imageJsonResults = null;
                        try {
                            imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                            imageResults.clear();
                            imageAdapter.addAll(ImageResults.fromJsonArray(imageJsonResults));
                            imageAdapter.notifyDataSetChanged();
                            Log.d("DEBUG", imageResults.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


        String urlString = "https://www.googleapis.com/customsearch/v1?q=" +
                searchStringNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(TAG, "ERROR converting String to URL " + e.toString());
        }
        Log.d(TAG, "Url = "+  urlString);

        GoogleSearchAsyncTask searchTask = new GoogleSearchAsyncTask();
        searchTask.execute(url);
    }


    private class GoogleSearchAsyncTask extends AsyncTask<URL, Integer, String>{

        protected void onPreExecute(){
            Log.d(TAG, "AsyncTask - onPreExecute");
            // show progressbar
           // progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            Log.d(TAG, "AsyncTask - doInBackground, url=" + url);

            // Http connection
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                Log.e(TAG, "Http connection ERROR " + e.toString());
            }


            try {
                responseCode = conn.getResponseCode();
                responseMessage = conn.getResponseMessage();
            } catch (IOException e) {
                Log.e(TAG, "Http getting response code ERROR " + e.toString());
            }

            Log.d(TAG, "Http response code =" + responseCode + " message=" + responseMessage);

            try {

                if(responseCode == 200) {

                    // response OK

                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = rd.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    rd.close();

                    conn.disconnect();

                    result = sb.toString();

                    Log.d(TAG, "result=" + result);

                    return result;

                }
                else{

                    // response problem

                    String errorMsg = "Http ERROR response " + responseMessage + "\n" + "Make sure to replace in code your own Google API key and Search Engine ID";
                    Log.e(TAG, errorMsg);
                    result = errorMsg;
                    return  result;

                }
            } catch (IOException e) {
                Log.e(TAG, "Http Response ERROR " + e.toString());
            }


            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.d(TAG, "AsyncTask - onProgressUpdate, progress=" + progress);

        }

        protected void onPostExecute(String result) {

            Log.d(TAG, "AsyncTask - onPostExecute, result=" + result);

            // hide progressbar
          //  progressBar.setVisibility(View.GONE);

            // make TextView scrollable
           // resultTextView.setMovementMethod(new ScrollingMovementMethod());
            // show result


        }


    }

}