package com.falconssoft.onlinetechsupport;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import static com.falconssoft.onlinetechsupport.MainActivity.cheakIn;
import static com.falconssoft.onlinetechsupport.MainActivity.cheakout;
import static com.falconssoft.onlinetechsupport.MainActivity.hold;
import static com.falconssoft.onlinetechsupport.MainActivity.refresh;


public class ManagerExport {

    private Context context;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogSave;
    private JSONObject obj;

    String itemCode;
    String JsonResponseSave;
    String JsonResponseSaveSwitch;
    JSONObject datatoSend=null;

    public  static boolean sendSucsses=false;
DatabaseHandler databaseHandler;
String ipAddres="";

    public ManagerExport(Context context,JSONObject obj) {//, JSONObject obj
        this.obj = obj;
        this.context = context;
        databaseHandler=new DatabaseHandler(context);
        ipAddres=databaseHandler.getIp();

    }

    public void startSending(String flag) {
        if (flag.equals("AddEmp"))
            new AddEmployees().execute();
        if (flag.equals("AddSystem"))
            new AddSystem().execute();
//http://10.0.0.214/onlineTechnicalSupport/export.php?CUSTOMER_INFO=[{CUST_NAME:%22fALCONS%22,COMPANY_NAME:%22MASTER%22,SYSTEM_NAME:%22rESTURANT%22,PHONE_NO:%220784555545%22,CHECH_IN_TIME:%2202:25%22,STATE:%221%22,ENG_NAME:%22ENG.RAWAN%22}]

    }


    private class AddEmployees extends AsyncTask<String, String, String> {
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

                String ip = "";

//
                String link ="http://"+ipAddres+"/onlineTechnicalSupport/export.php";
                // ITEM_CARD

                JSONArray jsonArray=new JSONArray();
                jsonArray.put(obj);

                String data = "ADD_USER=" + URLEncoder.encode("'1'", "UTF-8")+"&"+
                        "USER_INFO=" + URLEncoder.encode(jsonArray.toString(), "UTF-8");
////

                URL url = new URL(link);
                Log.e("urlStringoo001234 = ", "" + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

//
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
            Log.e("tag_itemCard000", "****saveSuccess"+JsonResponse);
            if (JsonResponse != null && JsonResponse.contains("CUST_NAME")) {
                Log.e("tag_itemCard", "****saveSuccess");

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

    private class AddSystem extends AsyncTask<String, String, String> {
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

                String ip = "";

//
                String link ="http://"+ipAddres+"/onlineTechnicalSupport/export.php";
                // ITEM_CARD
                JSONArray jsonArray=new JSONArray();
                jsonArray.put(obj);

                String data = "ADD_SYSTEM=" + URLEncoder.encode("'1'", "UTF-8")+"&"+
                        "SYSTEM_INFO=" + URLEncoder.encode(jsonArray.toString(), "UTF-8");
//                String data = "FLAG=" + URLEncoder.encode("0", "UTF-8");
////

                URL url = new URL(link);
                Log.e("urlString = ", "" + url.toString());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

//
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

            if (JsonResponse != null && JsonResponse.contains("SYSTEM_INFO SUCCESS")) {
                Log.e("tag_ItemOCode", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;

//                try {
//
//                    JSONArray parentArrayS = new JSONArray(JsonResponse);
//                    JSONObject CURRENT_TIME = parentArrayS.getJSONObject(0);
//
//                    String curentTime=CURRENT_TIME.getString("CURRENT_TIME");
//                    Log.e("CURRENT_TIME",""+CURRENT_TIME.getString("CURRENT_TIME"));
//
//                    JSONObject parentArrayD = parentArrayS.getJSONObject(1);
//                    JSONArray parentArray = parentArrayD.getJSONArray("CUSTOMER_INFO");
//
//                    int cheakInCount=cheakIn.size();
//                    int cheakoutCount=cheakout.size();
//                    int holdCount=hold.size();
//
//                    cheakIn.clear();
//                    cheakout.clear();
//                    hold.clear();
//
////                    {"CUST_NAME":"daaa","COMPANY_NAME":"MASTER","SYSTEM_NAME":"rrrr","PHONE_NO":"0154545465","CHECH_IN_TIME":"0000","STATE":"1"
////                            ,"ENG_NAME":"ENG.RAWAN","ENG_ID":"2","SYS_ID":"1","CHECH_OUT_TIME":"03:15","PROBLEM":"sefwuysagdh jyeuv "},
//
//                    for (int i = 0; i < parentArray.length(); i++) {
//                        JSONObject finalObject = parentArray.getJSONObject(i);
//
//                        ManagerLayout obj = new ManagerLayout();
//                        obj.setCompanyName(finalObject.getString("COMPANY_NAME"));
//                        obj.setCustomerName(finalObject.getString("CUST_NAME"));
//                        obj.setCheakInTime(finalObject.getString("CHECH_IN_TIME"));
//                        obj.setCheakOutTime(finalObject.getString("CHECH_OUT_TIME"));
//                        obj.setEnginerName(finalObject.getString("ENG_NAME"));
//                        obj.setPhoneNo(finalObject.getString("PHONE_NO"));
//                        obj.setState(finalObject.getString("STATE"));
//                        obj.setProplem(finalObject.getString("PROBLEM"));
//                        obj.setSystemName(finalObject.getString("SYSTEM_NAME"));
//                        obj.setSystemId(finalObject.getString("SYS_ID"));
//                        obj.setCurrentTime(curentTime);
//
//                        if(obj.getState().equals("1")){
//                            cheakIn.add(obj);
//                        }else if(obj.getState().equals("2")){
//                            cheakout.add(obj);
//                        }else if(obj.getState().equals("0")){
//                            hold.add(obj);
//                        }
//
//
//                    }
//                    refresh.setText("1");
//
//
//                    int cheakInCount1=cheakIn.size();
//                    int cheakoutCount1=cheakout.size();
//                    int holdCount1=hold.size();
//
//                    if(cheakInCount1>cheakInCount){
//                        refresh.setText("2");
//                    }
//
//                    Log.e("tag_itemCard", "****saveSuccess");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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


}