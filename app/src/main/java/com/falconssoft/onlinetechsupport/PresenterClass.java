package com.falconssoft.onlinetechsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresenterClass {

    private String urlImportCustomer, urlLogin;
    private RequestQueue requestQueue;
    private JsonObjectRequest  loginRequest;
    private StringRequest objectRequest;
    private Context context;

    public PresenterClass(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    //****************************************** Login **************************************

    public void getLoginData() {
        urlLogin = "http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG=0";
        loginRequest = new JsonObjectRequest(Request.Method.GET, urlLogin, new LoginDataClass(), new LoginDataClass());
        requestQueue.add(loginRequest);
    }

    class LoginDataClass implements Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("presenter", "LoginDataClass/ " + error.getMessage());

        }

        @Override
        public void onResponse(JSONObject response) {
            Log.e("presenter", "LoginDataClass/ " + response.toString());
            try {
                JSONArray jsonArray = response.getJSONArray("ENGINEER_INFO");
                int i = 0;
                while (i<jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }// ststus 2 ///////// cusomer

    //****************************************** Customers Data **************************************

    public void getCustomersData() {
        urlImportCustomer = urlImportCustomer = "http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG=\"2\"&ENG_ID=\"2\"";
        objectRequest = new StringRequest(Request.Method.GET, urlImportCustomer, new CustomersDataClass(), new CustomersDataClass());
        requestQueue.add(objectRequest);
    }

    class CustomersDataClass implements Response.Listener<String>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("presenter", "getCustomersData/ " + error.getMessage());

        }

        @Override
        public void onResponse(String response) {
            Log.e("presenter", "getCustomersData/ " + response.toString());

        }
    }
}
