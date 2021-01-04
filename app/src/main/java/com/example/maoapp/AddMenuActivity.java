package com.example.maoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class AddMenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText foodName;
    private TextView status;
    private RequestQueue requestQueue;
    private String url = "https://mao-dev.azurewebsites.net/api/v1/Menu";

    private static final String[] foodTypes = {"main", "soup", "dessert"};
    private String selectedFoodType = "main";

    private static final String[] menuTypes = { "meat", "vegan", "no sugar", "no alergens"};
    private String selectedMenuType = "meat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        foodName = (EditText) findViewById(R.id.foodNameInput);
//        status = (TextView) findViewById(R.id.statusText);
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        // Initialize dropdowns

        ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(AddMenuActivity.this,
                R.layout.color_spinner_layout,foodTypes) {
        };
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        Spinner foodTypeDropdown = (Spinner) findViewById(R.id.foodTypeDropdown);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        foodTypeDropdown.setAdapter(adapter1);
        foodTypeDropdown.setOnItemSelectedListener(this);




        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(AddMenuActivity.this,
                R.layout.color_spinner_layout,menuTypes) {
        };
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        Spinner menuTypeDropdown = (Spinner) findViewById(R.id.menuTypeDropdown);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        menuTypeDropdown.setAdapter(adapter2);
        menuTypeDropdown.setOnItemSelectedListener(this);

        // Configure 'back' button

        configureSwitchScreenButton();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch(parent.getId()){
            case R.id.foodTypeDropdown:
                this.selectedFoodType = foodTypes[position];
                break;
            case R.id.menuTypeDropdown:
                this.selectedMenuType = menuTypes[position];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }



    public void addMenu(View view) {

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("foodName", foodName.getText());
            jsonBody.put("foodType", selectedFoodType);
            jsonBody.put("menuType", selectedMenuType);

            final String mRequestBody = jsonBody.toString();

//            status.setText(mRequestBody);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("LOG_VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
//                        status.setText(responseString);

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("ApiKey", "maomaomao");
                    return params;
                }
            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void configureSwitchScreenButton() {
        Button btn = (Button) findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMenuActivity.this, MainActivity.class ));
            }
        });
    }


}



