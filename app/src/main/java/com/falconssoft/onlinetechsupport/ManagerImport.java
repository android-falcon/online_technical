package com.falconssoft.onlinetechsupport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.MainActivity.cheakIn;
import static com.falconssoft.onlinetechsupport.MainActivity.cheakout;
import static com.falconssoft.onlinetechsupport.MainActivity.hold;
import static com.falconssoft.onlinetechsupport.MainActivity.refresh;
import static com.falconssoft.onlinetechsupport.OnlineCenter.checkInList;
import static com.falconssoft.onlinetechsupport.OnlineCenter.hold_List;
import static com.falconssoft.onlinetechsupport.OnlineCenter.recyclerView;
import static com.falconssoft.onlinetechsupport.OnlineCenter.recyclerViewCheckIn;
import static com.falconssoft.onlinetechsupport.OnlineCenter.textState;
import static com.falconssoft.onlinetechsupport.OnlineCenter.text_finish;


public class ManagerImport {

    private Context context;
    boolean holdin=false;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogSave;
    private JSONObject obj;

    String itemCode;
    String JsonResponseSave;
    String JsonResponseSaveSwitch;
    JSONObject datatoSend=null,upDateHold=null;
    public  static boolean sendSucsses=false;
    public  static String ipAddres ="";
    DatabaseHandler databaseHandler;


    public ManagerImport(Context context) {//, JSONObject obj
//        this.obj = obj;
        this.context = context;
        databaseHandler=new DatabaseHandler(context);
        ipAddres=databaseHandler.getIp();


    }

    public void startSending(String flag) {
//        Log.e("check",flag);

        if (flag.equals("Manager"))
            new SyncManagerLayout().execute();
        if (flag.equals("ManageriN"))
            new SyncManagerLayoutIN().execute();
        if (flag.equals("GetHold"))
            new SyncHoldLayout().execute();
        if (flag.equals("GetCheckInList"))
            new GetCheckInList().execute();




//http://10.0.0.214/onlineTechnicalSupport/export.php?CUSTOMER_INFO=[{CUST_NAME:%22fALCONS%22,COMPANY_NAME:%22MASTER%22,SYSTEM_NAME:%22rESTURANT%22,PHONE_NO:%220784555545%22,CHECH_IN_TIME:%2202:25%22,STATE:%221%22,ENG_NAME:%22ENG.RAWAN%22}]

    }
    public void startSendingData(JSONObject data,boolean holds) {
        sendSucsses=false;
         datatoSend=data;
       holdin=holds;

            new SyncManagerLayoutIN().execute();
//http://10.0.0.214/onlineTechnicalSupport/export.php?CUSTOMER_INFO=[{CUST_NAME:%22fALCONS%22,COMPANY_NAME:%22MASTER%22,SYSTEM_NAME:%22rESTURANT%22,PHONE_NO:%220784555545%22,CHECH_IN_TIME:%2202:25%22,STATE:%221%22,ENG_NAME:%22ENG.RAWAN%22}]

    }

    public void startUpdateHold(JSONObject data) {
        upDateHold=data;
        new updateHoldLayout().execute();
    }


    private class SyncManagerLayout extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
//
                ipAddres=databaseHandler.getIp();
                String link ="http://"+ipAddres+"/onlineTechnicalSupport/import.php?FLAG=1";
                // ITEM_CARD


//                String data = "FLAG=" + URLEncoder.encode("0", "UTF-8");
////


                URL url = new URL(link);
                Log.e("urlString = ", "" + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ItemOCode -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("CUST_NAME")) {
                Log.e("tag_ItemOCode", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;

                try {

                    JSONArray parentArrayS = new JSONArray(JsonResponse);
                    JSONObject CURRENT_TIME = parentArrayS.getJSONObject(0);

                    String curentTime=CURRENT_TIME.getString("CURRENT_TIME");
                    Log.e("CURRENT_TIME",""+CURRENT_TIME.getString("CURRENT_TIME"));

                    JSONObject parentArrayD = parentArrayS.getJSONObject(1);
                    JSONArray parentArray = parentArrayD.getJSONArray("CUSTOMER_INFO");

                    int cheakInCount=cheakIn.size();
                    int cheakoutCount=cheakout.size();
                    int holdCount=hold.size();

                    cheakIn.clear();
                    cheakout.clear();
                    hold.clear();

//                    {"CUST_NAME":"daaa","COMPANY_NAME":"MASTER","SYSTEM_NAME":"rrrr","PHONE_NO":"0154545465","CHECH_IN_TIME":"0000","STATE":"1"
//                            ,"ENG_NAME":"ENG.RAWAN","ENG_ID":"2","SYS_ID":"1","CHECH_OUT_TIME":"03:15","PROBLEM":"sefwuysagdh jyeuv "},

                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        ManagerLayout obj = new ManagerLayout();
                        obj.setCompanyName(finalObject.getString("COMPANY_NAME"));
                        obj.setCustomerName(finalObject.getString("CUST_NAME"));
                        obj.setCheakInTime(finalObject.getString("CHECH_IN_TIME"));
                        obj.setCheakOutTime(finalObject.getString("CHECH_OUT_TIME"));
                        obj.setEnginerName(finalObject.getString("ENG_NAME"));
                        obj.setPhoneNo(finalObject.getString("PHONE_NO"));
                        obj.setState(finalObject.getString("STATE"));
                        obj.setProplem(finalObject.getString("PROBLEM"));
                        obj.setSystemName(finalObject.getString("SYSTEM_NAME"));
                        obj.setSystemId(finalObject.getString("SYS_ID"));
                        obj.setHoldTime(finalObject.getString("HOLD_TIME"));
                        obj.setCallCenterId(finalObject.getString("CALL_CENTER_ID"));
                        obj.setEngId(finalObject.getString("ENG_ID"));
                        obj.setSerial(finalObject.getString("SERIAL"));
                        obj.setConvertFlag(finalObject.getString("CONVERT_STATE"));
                        Log.e("finalObjectConvert",""+obj.getConvertFlag());

                        obj.setCurrentTime(curentTime);

                        if(obj.getState().equals("1")){
                            cheakIn.add(obj);
                        }else if(obj.getState().equals("2")){
                            cheakout.add(obj);
                        }else if(obj.getState().equals("0")){
                            hold.add(obj);
                        }


                    }
                    refresh.setText("1");


                    int cheakInCount1=cheakIn.size();
                    int cheakoutCount1=cheakout.size();
                    int holdCount1=hold.size();

                    if(cheakInCount1>cheakInCount){
                        refresh.setText("2");
                    }

Log.e("tag_itemCard", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }  else {
                Log.e("tag_itemCard", "****Failed to export data");
//                Toast.makeText(context, "Failed to Get data", Toast.LENGTH_SHORT).show();
//                if (pd != null) {
//                    pd.dismiss();
//                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText(context.getResources().getString(R.string.ops))
//                            .setContentText(context.getResources().getString(R.string.fildtoimportitemswitch))
//                            .show();
//                }


            }

        }
    }

    private class GetCheckInList extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
//
                ipAddres=databaseHandler.getIp();
                String link ="http://"+ipAddres+"/onlineTechnicalSupport/import.php?FLAG=1";
                // ITEM_CARD


//                String data = "FLAG=" + URLEncoder.encode("0", "UTF-8");
////


                URL url = new URL(link);
                Log.e("urlString = ", "" + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ItemOCode -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("CUST_NAME")) {
                Log.e("GetCheckInList", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;

                try {

                    JSONArray parentArrayS = new JSONArray(JsonResponse);
                    JSONObject CURRENT_TIME = parentArrayS.getJSONObject(0);

                    String curentTime=CURRENT_TIME.getString("CURRENT_TIME");
                    Log.e("CURRENT_TIME",""+CURRENT_TIME.getString("CURRENT_TIME"));

                    JSONObject parentArrayD = parentArrayS.getJSONObject(1);
                    JSONArray parentArray = parentArrayD.getJSONArray("CUSTOMER_INFO");

                    int cheakInCount=checkInList.size();

                    checkInList.clear();


                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        ManagerLayout obj = new ManagerLayout();
                        obj.setCompanyName(finalObject.getString("COMPANY_NAME"));
                        obj.setCustomerName(finalObject.getString("CUST_NAME"));
                        obj.setCheakInTime(finalObject.getString("CHECH_IN_TIME"));
                        obj.setCheakOutTime(finalObject.getString("CHECH_OUT_TIME"));
                        obj.setEnginerName(finalObject.getString("ENG_NAME"));
                        obj.setPhoneNo(finalObject.getString("PHONE_NO"));
                        obj.setState(finalObject.getString("STATE"));
                        obj.setProplem(finalObject.getString("PROBLEM"));
                        obj.setSystemName(finalObject.getString("SYSTEM_NAME"));
                        obj.setSystemId(finalObject.getString("SYS_ID"));
                        obj.setHoldTime(finalObject.getString("HOLD_TIME"));
                        obj.setCallCenterId(finalObject.getString("CALL_CENTER_ID"));
                        obj.setEngId(finalObject.getString("ENG_ID"));
                        obj.setSerial(finalObject.getString("SERIAL"));
                        obj.setCurrentTime(curentTime);


                        if(obj.getState().equals("1")&& finalObject.getString("CALL_CENTER_ID").equals( LoginActivity.sharedPreferences.getString(LOGIN_ID,"-1"))) {
                            checkInList.add(obj);
                        }
//                        }else if(obj.getState().equals("2")){
//                            cheakout.add(obj);
//                        }else if(obj.getState().equals("0")){
//                            hold.add(obj);

//                        }


                    }
//                    refresh.setText("1");

                     int cheakInCount1=checkInList.size();
//
//                    if(cheakInCount1>cheakInCount){
//                        refresh.setText("2");
//                    }

                    setCheckInList();

                    Log.e("check_in", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }  else {
                Log.e("check_in", "****Failed to export data");

            }

        }
    }

    private class SyncManagerLayoutIN extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
       boolean isHold=false;
        SweetAlertDialog pdaSweet;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdaSweet = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pdaSweet.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            pdaSweet.setTitleText("Process...");
            pdaSweet.setCancelable(false);
            pdaSweet.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
              ipAddres=databaseHandler.getIp();
            String link ="http://"+ipAddres+"/onlineTechnicalSupport/export.php";

                JSONObject obj = new JSONObject();
                JSONArray NEWI=new JSONArray();

                NEWI.put(datatoSend);

                String data = "CUSTOMER_INFO=" + URLEncoder.encode(NEWI.toString(), "UTF-8");
                URL url = new URL(link);
                Log.e("urlString = ", "" + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");


                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("httpURLConnection", "stringBuffer" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);
            pdaSweet.dismissWithAnimation();
            //&& JsonResponse.contains("CUST_NAME")
            if (JsonResponse != null && JsonResponse.contains("CUSTOMER_INFO SUCCESS")) {
                sendSucsses=true;
                text_finish.setText("finish");
                Log.e("tag_ItemOCodeSS", "****Success");
//                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("")
//                        .setContentText(" Send Sucsseful")
////                        .hideConfirmButton()
//                        .show();

                if(holdin){
                    textState.setText("Success");
                }


            }  else {
                sendSucsses=false;
                Log.e("tag_itemCard", "****Failed to export data");

//                }


            }

        }
    }

    private class updateHoldLayout extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            try {
                ipAddres=databaseHandler.getIp();
                String link ="http://"+ipAddres+"/onlineTechnicalSupport/export.php";

                JSONObject obj = new JSONObject();
                JSONArray NEWI=new JSONArray();

//                NEWI.put(upDateHold);

                String data = "UPDATE_CUSTOMER_INFO=" + URLEncoder.encode(upDateHold.toString(), "UTF-8");
                URL url = new URL(link);
                Log.e("UPDATE_CUSTOMER_INFO = ", "" + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                Log.e("UPDATE_HOLD", "Data=-->" + data);


                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("UPDATE_HOLD", "UPDATE=-->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);


            if (JsonResponse != null && JsonResponse.contains("CUST_NAME")) {
                sendSucsses=true;

                startSending("GetHold");
                text_finish.setText("finish");
                Log.e("updatehold", "****Success");
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("send hold data Success")
//                        .hideConfirmButton()
                        .show();

                    textState.setText("Success");



            }  else {
                sendSucsses=false;
                Log.e("tag_itemCard", "****Failed to export data");

//                }


            }

        }
    }

    private class SyncHoldLayout extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
//
                ipAddres=databaseHandler.getIp();
                String link ="http://"+ipAddres+"/onlineTechnicalSupport/import.php?FLAG=1";
                // ITEM_CARD


//                String data = "FLAG=" + URLEncoder.encode("0", "UTF-8");
////

                URL url = new URL(link);
                Log.e("urlString = ", "" + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ItemOCode -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("CUST_NAME")) {
                Log.e("tag_ItemOCode", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;

                try {

                    JSONArray parentArrayS = new JSONArray(JsonResponse);
                    JSONObject CURRENT_TIME = parentArrayS.getJSONObject(0);

                    String curentTime=CURRENT_TIME.getString("CURRENT_TIME");
                    Log.e("CURRENT_TIME",""+CURRENT_TIME.getString("CURRENT_TIME"));

                    JSONObject parentArrayD = parentArrayS.getJSONObject(1);
                    JSONArray parentArray = parentArrayD.getJSONArray("CUSTOMER_INFO");

                    hold_List.clear();
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        ManagerLayout obj = new ManagerLayout();
                        obj.setCompanyName(finalObject.getString("COMPANY_NAME"));
                        obj.setCustomerName(finalObject.getString("CUST_NAME"));
                        obj.setCheakInTime(finalObject.getString("CHECH_IN_TIME"));
                        obj.setCheakOutTime(finalObject.getString("CHECH_OUT_TIME"));
                        obj.setEnginerName(finalObject.getString("ENG_NAME"));
                        obj.setPhoneNo(finalObject.getString("PHONE_NO"));
                        obj.setState(finalObject.getString("STATE"));
                        obj.setProplem(finalObject.getString("PROBLEM"));
                        obj.setSystemName(finalObject.getString("SYSTEM_NAME"));
                        obj.setSystemId(finalObject.getString("SYS_ID"));
                        obj.setCurrentTime(curentTime);
                        obj.setSerial(finalObject.getString("SERIAL"));

                        if(obj.getState().equals("0")&& finalObject.getString("CALL_CENTER_ID").equals( LoginActivity.sharedPreferences.getString(LOGIN_ID,"-1"))){
                            hold_List.add(obj);
                            Log.e("hold_List",""+hold_List.size());
                        }
                        setHoldList();


                    }

                    Log.e("tag_itemCard", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }  else {
                Log.e("tag_itemCard", "****Failed to export data");
            }

        }
    }

    @SuppressLint("WrongConstant")
    private void setHoldList() {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        Log.e("fillHoldList",""+hold_List.size());
        if( hold_List.size()!=0)
        {
            final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(context, hold_List);

            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(companyAdapter);
        }
    }


    @SuppressLint("WrongConstant")
    private void setCheckInList() {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        Log.e("fillHoldList",""+hold_List.size());
        if( checkInList.size()!=0)
        {
            final CheckInCompanyAdapter companyAdapter = new CheckInCompanyAdapter((OnlineCenter) context, checkInList);

            recyclerViewCheckIn.setLayoutManager(llm);
            recyclerViewCheckIn.setAdapter(companyAdapter);
        }
    }


}