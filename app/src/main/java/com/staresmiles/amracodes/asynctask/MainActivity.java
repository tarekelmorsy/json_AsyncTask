package com.staresmiles.amracodes.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String apiUrl = "https://api.thecatapi.com/v1/images/search?breed_ids=beng";
    String name, imageUrl, description, origin,id;
    TextView nameTextView, descriptionTextView, originTextView,idTextViwe;
    ProgressDialog progressDialog;
    Button displayData;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.categoryTextView);
        displayData = (Button) findViewById(R.id.displayData);
        imageView = (ImageView) findViewById(R.id.imageView);
        originTextView = (TextView)findViewById(R.id.origin);
        idTextViwe = (TextView) findViewById(R.id.id);
        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new MyAsyncTasks().execute();
            }
        });
    }

    class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "" ;
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);

                    urlConnection = (HttpURLConnection) url.openConnection();




                    InputStream in = urlConnection.getInputStream();



                    InputStreamReader isw = new InputStreamReader(in);

                    int charcter = isw.read();
                    while (charcter != -1) {
                        current = current+ (char) charcter;
                        charcter = isw.read();

                    }
                    isw.close();
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            // dismiss the progress dialog after receiving data from API
           progressDialog.dismiss();
            try {
                // JSON Parsing of data
                JSONArray jsonArray = new JSONArray(s);

                JSONObject oneObject = jsonArray.getJSONObject(0);
                // Pulling items from the array
                imageUrl = oneObject.getString("url");
                JSONArray breeds = oneObject.getJSONArray("breeds");
                JSONObject firstBreed = breeds.getJSONObject(0);
                
               name = firstBreed.getString("name");

                id = firstBreed.getString("id");

                description = firstBreed.getString("description");

                origin = firstBreed.getString("origin");
                
                idTextViwe.setText("Id :"+id);
                nameTextView.setText("Name: " + name);
                descriptionTextView.setText("Description: " + description);
                originTextView.setText("Origin : "+origin);
                // Picasso library to display the imageUrl from URL
                Picasso.with(MainActivity.this)
                        .load(imageUrl)
                        .into(imageView);



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
