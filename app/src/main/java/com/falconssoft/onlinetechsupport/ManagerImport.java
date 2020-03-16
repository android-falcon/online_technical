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


public class ManagerImport {

    private Context context;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogSave;
    private JSONObject obj;

    String itemCode;
    String JsonResponseSave;
    String JsonResponseSaveSwitch;


    public ManagerImport(Context context) {//, JSONObject obj
//        this.obj = obj;
        this.context = context;

    }

    public void startSending(String flag) {
//        Log.e("check",flag);

        if (flag.equals("Manager"))
            new SyncManagerLayout().execute();
        if (flag.equals("ManageriN"))
            new SyncManagerLayoutIN().execute();
//http://10.0.0.214/onlineTechnicalSupport/export.php?CUSTOMER_INFO=[{CUST_NAME:%22fALCONS%22,COMPANY_NAME:%22MASTER%22,SYSTEM_NAME:%22rESTURANT%22,PHONE_NO:%220784555545%22,CHECH_IN_TIME:%2202:25%22,STATE:%221%22,ENG_NAME:%22ENG.RAWAN%22}]

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

                String ip = "";

//
                String link ="http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG=1";
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

                    JSONObject parentArrayS = new JSONObject(JsonResponse);
                    JSONArray parentArray = parentArrayS.getJSONArray("CUSTOMER_INFO");

                    int cheakInCount=cheakIn.size();
                    int cheakoutCount=cheakout.size();
                    int holdCount=hold.size();

                    cheakIn.clear();
                    cheakout.clear();
                    hold.clear();

//                    INSERT INTO ONLINE_CUSTOMER_INFO (
//                            "CUST_NAME, COMPANY_NAME, SYSTEM_NAME, PHONE_NO, CHECH_IN_TIME,
//                            "STATE, ENG_NAME

                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);

                        ManagerLayout obj = new ManagerLayout();
                        obj.setCompanyName(finalObject.getString("COMPANY_NAME"));
                        obj.setCustomerName(finalObject.getString("CUST_NAME"));
                        obj.setCheakInTime(finalObject.getString("CHECH_IN_TIME"));
//                        obj.setCheakOutTime(finalObject.getString("ItemOCode"));
                        obj.setEnginerName(finalObject.getString("ENG_NAME"));
                        obj.setPhoneNo(finalObject.getString("PHONE_NO"));
                        obj.setState(finalObject.getString("STATE"));
                        obj.setSystemName(finalObject.getString("SYSTEM_NAME"));


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

    private class SyncManagerLayoutIN extends AsyncTask<String, String, String> {
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

                String ip = "";

//
                String link ="http://10.0.0.214/onlineTechnicalSupport/export.php";
                // ITEM_CARD

                JSONObject obj = new JSONObject();
                JSONArray NEWI=new JSONArray();

                try {
                    obj.put("CUST_NAME", "'Eng Tahani'");
                    obj.put("COMPANY_NAME", "'Falcons'");
                    obj.put("SYSTEM_NAME", "'Accounting'");
                    obj.put("PHONE_NO", "'015454'");
                    obj.put("CHECH_IN_TIME", "'03:30'");
                    obj.put("STATE", "'1'");
                    obj.put("ENG_NAME", "'ENG.RAWAN'");

                    NEWI.put(obj);
                } catch (JSONException e) {
                    Log.e("Tag", "JSONException");
                }


                String data = "CUSTOMER_INFO=" + URLEncoder.encode(NEWI.toString(), "UTF-8");
////

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


            }  else {
                Log.e("tag_itemCard", "****Failed to export data");

//                }


            }

        }
    }



}