package com.example.nicole_weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //les champs
    TextView mDate, mVille, mTemp, mDescription;
    ImageView imgIcon;
    String maVille = "Toronto";

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDate = findViewById(R.id.mDate);
        mVille = findViewById(R.id.mVille);
        mTemp = findViewById(R.id.mTemp);
        mDescription = findViewById(R.id.mDescription);

        requestQueue = Volley.newRequestQueue(this);
        afficher();

    }

    public void afficher(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=3ddc142c3a3b1aed34e0a885a5af9119&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //afficher dans le logcat
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    //afficher weather
                    Log.d("Tag", "resultat =" + array.toString());
                    //afficher main : temperature (feels like, min, max), pression, humidite.
                    Log.d("Tag", "resultat =" + main_object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        }

    }



