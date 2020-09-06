package com.falconssoft.onlinetechsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;
import static com.falconssoft.onlinetechsupport.OnlineActivity.isTimerWork;

public class PresenterClass {
    private String getDataState = "1";
    private String urlImportCustomer, urlLogin, urlState, urlPushProblem;
    private RequestQueue requestQueue;
    private JsonObjectRequest loginRequest, objectRequest;
    //    private JsonArrayRequest  pushProblemRequest;
    private StringRequest stateRequest, pushProblemRequest;
    private Context context;
    private DatabaseHandler databaseHandler;
    private List<EngineerInfo> list;
    private List<CustomerOnline> onlineList;
    private OnlineActivity onlineActivity;
    private CustomerOnline customerOnline;
    private String value;
    private String ipAddres;

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
        ipAddres = databaseHandler.getIp();
        urlLogin = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";
        loginRequest = new JsonObjectRequest(Request.Method.GET,urlLogin, null, new LoginDataClass(), new LoginDataClass());
        requestQueue.add(loginRequest);
    }

    class LoginDataClass implements Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
//            Log.e("presenter/e", "LoginDataClass/ " + error.getMessage());

        }

        @Override
        public void onResponse(JSONObject response) {
            Log.e("presenter", "LoginDataClass/ " + response.toString());
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
                    Log.e("EmployList", "LoginDataClass/ " + engineerInfo.getName() + "  " + engineerInfo.getPassword());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            databaseHandler.addLoginInfo(list);
        }
    }// ststus 2 ///////// cusomer

//    json object problem_solved
    //****************************************** Customers Data **************************************

    public void getCustomersData(String dataState) {
        if (isTimerWork || dataState.equals("get")) {
            Log.e("getCustomer/e", "data " + dataState + " isWork  " + isTimerWork);

            ipAddres = databaseHandler.getIp();
            getDataState = dataState;
//        List<EngineerInfo>userDat=databaseHandler.getLoginData();
//        String engID="-2";
//        if(userDat.size()!=0){
//            engID=userDat.get(0).getId();
//        }

            String engId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
            //http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG="2"&ENG_ID="2"
            urlImportCustomer = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=2&ENG_ID=" + engId;
            objectRequest = new JsonObjectRequest(Request.Method.GET, urlImportCustomer, null, new CustomersDataClass(), new CustomersDataClass());
            Log.e("presenter/e", "getCustomersData/urllll" + urlImportCustomer);

            requestQueue.add(objectRequest);
        }
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

                //'SUCCESS{"CUSTOMER_INFO":[{"CUST_NAME":"rawanjjj","COMPANY_NAME":"gggg","SYSTEM_NAME":"Pharmacy",
                // "PHONE_NO":"55888","CHECH_IN_TIME":"12:08:59","STATE":"1","ENG_NAME":"ahmad","ENG_ID":"5","SYS_ID":"2",
                // "CHECH_OUT_TIME":"00:00","PROBLEM":"null","HOLD_TIME":"11:47:47",
                // "DATE_OF_TRANSACTION":"16\/06\/2020","SERIAL":"1","CALL_CENTER_ID":"2"}]}

                boolean found = false;
                String engId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
                Log.e("ppppppp", engId);
                JSONArray jsonArray = response.getJSONArray("CUSTOMER_INFO");
                for (int m = 0; m < jsonArray.length(); m++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(m);
                    Log.e("ppppppppppppp", jsonObject.getString("ENG_ID"));

//                    engId.toLowerCase().equals(jsonObject.getString("ENG_ID"))
                    if (engId.toLowerCase().equals(jsonObject.getString("ENG_ID")) && jsonObject.getString("STATE").toString().equals("1")) {
                        found = true;
                        customerOnline = new CustomerOnline();
                        customerOnline.setCheakInTime(jsonObject.getString("CHECH_IN_TIME"));
                        customerOnline.setCompanyName(jsonObject.getString("COMPANY_NAME"));
                        customerOnline.setCustomerName(jsonObject.getString("CUST_NAME"));
                        customerOnline.setPhoneNo(jsonObject.getString("PHONE_NO"));
                        customerOnline.setSystemName(jsonObject.getString("SYSTEM_NAME"));
                        customerOnline.setSystemId(jsonObject.getString("SYS_ID"));
                        customerOnline.setSerial(jsonObject.getString("SERIAL"));
                        Log.e("name", customerOnline.getCustomerName()+"    ==>"+ customerOnline.getSerial());

                        break;
                    }

                }
                if (found && getDataState.equals("get")) {
                    onlineActivity.showCustomerLinear(customerOnline);
                } else if (found && getDataState.equals("ifFound")) {
                    onlineActivity.ShowNotification();
                } else if (!found) {
                    isTimerWork = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //****************************************** Push Customer Problem **************************************

    public void pushCustomerProblem(CustomerOnline customerOnline, int state) {
//        object.put("CHECH_OUT_TIME", "'" + customerOnline.getCheakOutTime() + "'");
//        object.put("PROBLEM", "'" + customerOnline.getProblem() + "'");
//        object.put("CUST_NAME", "'" + customerOnline.getCustomerName() + "'");
//        object.put("CHECH_IN_TIME", "'" + customerOnline.getCheakInTime() + "'");
//        object.put("COMPANY_NAME", "'" + customerOnline.getCompanyName() + "'");
//        object.put("PHONE_NO", "'" + customerOnline.getPhoneNo() + "'");
//        object.put("SYSTEM_NAME", "'" + customerOnline.getSystemName() + "'");
//        object.put("SYS_ID", "'" + customerOnline.getSystemId() + "'");
//        object.put("ENG_ID", "'" + customerOnline.getEngineerID() + "'");
//        object.put("ENG_NAME", "'" + customerOnline.getEngineerName() + "'");

//    "http://10.0.0.214/onlineTechnicalSupport/import.php?LOG_IN_OUT=0&ENG_ID=&STATE="
        ipAddres = databaseHandler.getIp();
        urlPushProblem = "http://" + ipAddres + "/onlineTechnicalSupport/export.php";//?LOG_IN_OUT=0&ENG_ID="
//        + LoginActivity.sharedPreferences.getString(LOGIN_ID, "null")+"&STATE=" + state;
        Log.e("push", urlPushProblem);

        final JSONObject object = new JSONObject();
        try {
            object.put("CHECH_OUT_TIME", customerOnline.getCheakOutTime());
            object.put("PROBLEM", customerOnline.getProblem());
            object.put("CUST_NAME", customerOnline.getCustomerName());//customerOnline.getCustomerName()
            object.put("CHECH_IN_TIME", customerOnline.getCheakInTime());
            object.put("COMPANY_NAME", customerOnline.getCompanyName());
            object.put("PHONE_NO", customerOnline.getPhoneNo());
            object.put("SYSTEM_NAME", customerOnline.getSystemName());
            object.put("SYS_ID", customerOnline.getSystemId());
            object.put("ENG_ID", customerOnline.getEngineerID());
            object.put("ENG_NAME", customerOnline.getEngineerName());
            object.put("STATE", "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        value = "PROBLEM_SOLVED=" + new JSONArray().put(object).toString();
        pushProblemRequest = new StringRequest(Request.Method.POST
                , urlPushProblem
                , new PushProblemClass()
                , new PushProblemClass()) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return value == null ? null : value.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", value, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                    Log.e("respons", "is" + response.toString());
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        }
        ;
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//            final Map<String, String> headers = new HashMap<>();
//            headers.put("PROBLEM_SOLVED","PROBLEM_SOLVED=" + new JSONArray().put(object).toString());
//            return headers;
//        }
//        }
        requestQueue.add(pushProblemRequest);
    }

    class PushProblemClass implements Response.Listener<String>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("presenter/e", "PushProblemClass/ " + error.getMessage());

        }

        @Override
        public void onResponse(String response) {
            Log.e("presenter", "PushProblemClass/ " + response);
            onlineActivity.hideCustomerLinear();
        }
    }

    //****************************************** State **************************************

    public void setState(String engId, int state) {
        ipAddres = databaseHandler.getIp();
        urlState = "http://" + ipAddres + "/onlineTechnicalSupport/export.php?LOG_IN_OUT=0&ENG_ID=" + engId + "&STATE=" + state;
        stateRequest = new StringRequest(Request.Method.GET, urlState, new StateClass(), new StateClass());
        Log.e("setStateGGG///", "engId" + engId + "state" + state + "url" + urlState);

        requestQueue.add(stateRequest);
    }

    class StateClass implements Response.Listener<String>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("presenter/e", "StateClassError/ " + error.getMessage());

        }

        @Override
        public void onResponse(String response) {
            Log.e("presenter", "StateClass/ " + response);

        }
    }// ststus 2 ///////// cusomer


}
