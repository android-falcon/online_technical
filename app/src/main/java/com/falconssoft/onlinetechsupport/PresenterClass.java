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
import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;

public class PresenterClass {

    private String urlImportCustomer, urlLogin, urlState, urlPushProblem;
    private RequestQueue requestQueue;
    private JsonObjectRequest loginRequest, objectRequest, pushProblemRequest;
    private StringRequest stateRequest;
    private Context context;
    private DatabaseHandler databaseHandler;
    private List<EngineerInfo> list;
    private List<CustomerOnline> onlineList;
    private OnlineActivity onlineActivity;
    private CustomerOnline customerOnline;

    public PresenterClass(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        databaseHandler = new DatabaseHandler(context);
        list = new ArrayList<>();
    }

    public PresenterClass(OnlineActivity onlineActivity, int flag) {
        this.onlineActivity = onlineActivity;
        this.requestQueue = Volley.newRequestQueue(onlineActivity);
        databaseHandler = new DatabaseHandler(onlineActivity);
        onlineList = new ArrayList<>();
    }

// -1 log out

    //****************************************** Login **************************************

    public void getLoginData() {
        urlLogin = "http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG=0";
        loginRequest = new JsonObjectRequest(Request.Method.GET, urlLogin, new LoginDataClass(), new LoginDataClass());
        requestQueue.add(loginRequest);
    }

    class LoginDataClass implements Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
//            Log.e("presenter/e", "LoginDataClass/ " + error.getMessage());

        }

        @Override
        public void onResponse(JSONObject response) {
//            Log.e("presenter", "LoginDataClass/ " + response.toString());
            try {
                databaseHandler.deleteLoginData();
                list.clear();
                JSONArray jsonArray = response.getJSONArray("ENGINEER_INFO");
                int i = 0;
                while (i < jsonArray.length()) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    EngineerInfo engineerInfo = new EngineerInfo();
                    engineerInfo.setId(jsonObject.getString("ENG_ID"));
                    engineerInfo.setName(jsonObject.getString("ENG_NAME"));
                    engineerInfo.setState(jsonObject.getInt("STATE"));
                    engineerInfo.setPassword(jsonObject.getString("PASSWORD"));
                    engineerInfo.setEng_type(jsonObject.getInt("ENG_TYPE"));
                    list.add(engineerInfo);
                    i++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            databaseHandler.addLoginInfo(list);
        }
    }// ststus 2 ///////// cusomer

//    json object problem_solved
    //****************************************** Customers Data **************************************

    public void getCustomersData() {
        //http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG="2"&ENG_ID="2"
        urlImportCustomer = "http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG=2&ENG_ID=2";
        objectRequest = new JsonObjectRequest(Request.Method.GET, urlImportCustomer, new CustomersDataClass(), new CustomersDataClass());
        requestQueue.add(objectRequest);
    }

    class CustomersDataClass implements Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("presenter/e", "getCustomersData/ " + error.getMessage());

        }

        @Override
        public void onResponse(JSONObject response) {
//            Log.e("presenter", "getCustomersData/ " + response.toString());
            try {
                boolean found = false;
                String engId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
                Log.e("ppppppp",  engId);
                JSONArray jsonArray = response.getJSONArray("CUSTOMER_INFO");
                for (int m = 0; m < jsonArray.length(); m++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(m);
                    Log.e("ppppppppppppp", jsonObject.getString("ENG_ID"));

//                    engId.toLowerCase().equals(jsonObject.getString("ENG_ID"))
                    if (engId.toLowerCase().equals("3")) {
                        found = true;
                        customerOnline = new CustomerOnline();
                        customerOnline.setCheakInTime(jsonObject.getString("CHECH_IN_TIME"));
                        customerOnline.setCompanyName(jsonObject.getString("COMPANY_NAME"));
                        customerOnline.setCustomerName(jsonObject.getString("CUST_NAME"));
                        customerOnline.setPhoneNo(jsonObject.getString("PHONE_NO"));
                        customerOnline.setSystemName(jsonObject.getString("SYSTEM_NAME"));
                        customerOnline.setSystemId(jsonObject.getString("SYS_ID"));
                        Log.e("name", customerOnline.getCustomerName());

                        break;
                    }

                }
                if (found)
                    onlineActivity.showCustomerLinear(customerOnline);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //****************************************** Push Customer Problem **************************************

    public void pushCustomerProblem(CustomerOnline customerOnline,int state) {
//    "http://10.0.0.214/onlineTechnicalSupport/import.php?LOG_IN_OUT=0&ENG_ID=&STATE="
        urlPushProblem = "http://10.0.0.214/onlineTechnicalSupport/export.php";//?LOG_IN_OUT=0&ENG_ID="
//        + LoginActivity.sharedPreferences.getString(LOGIN_ID, "null")+"&STATE=" + state;
        Log.e("push", urlPushProblem);
        JSONObject object = new JSONObject();
        try {
            object.put("CHECH_OUT_TIME", customerOnline.getCheakOutTime());
            object.put("PROBLEM", customerOnline.getProblem());
            object.put("CUST_NAME", customerOnline.getCustomerName());
            object.put("CHECH_IN_TIME", customerOnline.getCheakInTime());
            object.put("COMPANY_NAME", customerOnline.getCompanyName());
            object.put("PHONE_NO",customerOnline.getPhoneNo() );
            object.put("SYSTEM_NAME", customerOnline.getSystemName());
            object.put("SYS_ID", customerOnline.getSystemId());
            object.put("ENG_ID", customerOnline.getEngineerID());
            object.put("ENG_NAME", customerOnline.getEngineerName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pushProblemRequest = new JsonObjectRequest(Request.Method.POST, urlPushProblem, object, new PushProblemClass(), new PushProblemClass());
        requestQueue.add(pushProblemRequest);
    }

    class PushProblemClass implements Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("presenter/e", "PushProblemClass/ " + error.getMessage());

        }

        @Override
        public void onResponse(JSONObject response) {
            Log.e("presenter", "PushProblemClass/ " + response.toString());
            onlineActivity.resetFields();
        }
    }

    //****************************************** State **************************************

    public void setState(String engId, int state) {
        urlState = "http://10.0.0.214/onlineTechnicalSupport/export.php?LOGIN_OUT=0&ENG_ID=" + engId + "&STATE=" + state;
        stateRequest = new StringRequest(Request.Method.GET, urlState, new StateClass(), new StateClass());
        requestQueue.add(stateRequest);
    }

    class StateClass implements Response.Listener<String>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
//            Log.e("presenter/e", "StateClass/ " + error.getMessage());

        }

        @Override
        public void onResponse(String response) {
//            Log.e("presenter", "StateClass/ " + response);

        }
    }// ststus 2 ///////// cusomer


}
