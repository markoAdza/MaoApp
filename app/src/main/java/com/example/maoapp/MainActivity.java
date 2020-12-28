package com.example.maoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView jedi;
    private String url = "https://mao-dev.azurewebsites.net/api/v1/Menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jedi = (TextView) findViewById(R.id.jedi);
    }

    public void prikaziJedi(View view){
        if(view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener);
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();
            data.add("Food,  Food Type,  Menu Type,  Rating");

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    String name = object.getString("foodName");
                    String foodType = object.getString("foodType");
                    String menuType = object.getString("menuType");
                    int rating = object.getInt("avgRating");

                    data.add(name + ",  " + foodType + ",  " + menuType + ",  " + rating);
                }catch (JSONException e){
                    e.printStackTrace();
                    return;
                }
            }

            jedi.setText("");

            for (String row : data){
                String currentText = jedi.getText().toString();
                jedi.setText(currentText + "\n\n" + row);
            }

        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

}
//
//{
//        "foodName": "FriedChocolates",
//        "foodType": "dessert",
//        "menuType": "vegan"
//        }