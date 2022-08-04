package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private EditText user_field;
    private Button main_btn;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result = findViewById(R.id.result);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = user_field.getText().toString().trim();
                String keyAPI = "c0fcbb513aebe8ebd4d46ffb84ab8467";
                String url = "https://api.openweathermap.org/geo/1.0/direct?q=" + city + "&appid=" + keyAPI;
                if(city.equals(""))
                    Toast.makeText(MainActivity.this, R.string.empty_input, Toast.LENGTH_LONG).show();
                else{
                    new GetURLData().execute(url);
                }
            }
        });
    }

    private class GetURLData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            result.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append(("\n"));

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String resultJSON) {
            super.onPostExecute(resultJSON);

            result.setText("Получено: " + resultJSON);
            try {
                JSONArray arr = new JSONArray(resultJSON);
                result.setText("lat: " + arr.getJSONObject(0).getDouble("lat"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}