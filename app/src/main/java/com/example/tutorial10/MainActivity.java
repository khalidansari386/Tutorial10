package com.example.tutorial10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    CustomAdapter adapter;
    ListView lstData;
   // JSONArray itemsJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstData = findViewById(R.id.lstData);

        new MyAsyncTask().execute();

        lstData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(MainActivity.this,UserData.class);
                intent.putExtra("userdata",i);
                startActivity(intent);

            }
        });

    }

    class MyAsyncTask extends AsyncTask{
        ProgressDialog dialog;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

           StringBuffer response = new StringBuffer();
           try {
               URL url = new URL(MyUtil.URL_USERS);
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
               InputStreamReader ir = new InputStreamReader(urlConnection.getInputStream());
               BufferedReader br = new BufferedReader(ir);
               String inputLine = null;

               while ((inputLine = br.readLine()) != null){
                   response.append(inputLine);
               }
               br.close();
               ir.close();

               MyUtil.userdata = new JSONArray(response.toString());

           } catch (JSONException | IOException e) {
               e.printStackTrace();
           }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            adapter = new CustomAdapter(MainActivity.this,MyUtil.userdata);
            lstData.setAdapter(adapter);

            if (dialog.isShowing())dialog.dismiss();
        }
    }
}