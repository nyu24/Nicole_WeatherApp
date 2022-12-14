package com.example.nicole_weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //les champs
    TextView mDate, mVille, mTemp, mDescription, mVent, mTempF;
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
        mVent = findViewById(R.id.mVent);
        mTempF = findViewById(R.id.mTempF);

        afficher();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Ecrire le nom de la ville");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                maVille = query;
                afficher();
                //gestion du clavier
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void afficher(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + maVille + "&appid=3ddc142c3a3b1aed34e0a885a5af9119&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONObject wind_object = response.getJSONObject("wind");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);

                    //afficher dans le logcat
                    //afficher weather
                    //Log.d("Tag", "resultat =" + array.toString());
                    //afficher main : temperature (feels like, min, max), pression, humidite.
                    //Log.d("Tag", "resultat =" + main_object);
                    //noter la temperature

                    int tempC = (int) Math.round(main_object.getDouble("temp"));
                    String temp = String.valueOf(tempC);

                    int tempFeels = (int) Math.round(main_object.getDouble("feels_like"));
                    String tempF = String.valueOf(tempFeels);

                    int spdV = (int) Math.round(wind_object.getDouble("speed"));
                    String spd = String.valueOf(spdV);

                    String description = object.getString("description");
                    String city = response.getString("name");
                    String icon = object.getString("icon");

                    //mettre dans les champs
                    mVille.setText(city);
                    mTemp.setText(temp);
                    mDescription.setText(description);
                    mVent.setText(spd);
                    mTempF.setText(tempF);

                    //formattage du temps
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd");
                    String formatted_date = simpleDateFormat.format(calendar.getTime());
                    mDate.setText(formatted_date);

                    //l'image
                    String imageUri = "http://openweathermap.org/img/w/" + icon + ".png";
                    imgIcon = findViewById(R.id.imgIcon);
                    Uri myUri = Uri.parse(imageUri);
                    Picasso.with(MainActivity.this).load(myUri).resize(200,200).into(imgIcon);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //ajouter les elements dans une queue
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        }

    }



