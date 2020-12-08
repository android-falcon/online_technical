package com.falconssoft.onlinetechsupport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.falconssoft.onlinetechsupport.reports.CallCenterTrackingReport;
import com.falconssoft.onlinetechsupport.reports.EngineersTrackingReport;
import com.falconssoft.onlinetechsupport.reports.TechnicalTrackingReport;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.falconssoft.onlinetechsupport.GClass.customerPhoneNo;
import static com.falconssoft.onlinetechsupport.GClass.engList;
import static com.falconssoft.onlinetechsupport.GClass.engMList;
import static com.falconssoft.onlinetechsupport.GClass.engPerHourList;
import static com.falconssoft.onlinetechsupport.GClass.engPerSysList;
import static com.falconssoft.onlinetechsupport.GClass.engineerInfoList;
import static com.falconssoft.onlinetechsupport.GClass.holdCounts;
import static com.falconssoft.onlinetechsupport.GClass.inCount;
import static com.falconssoft.onlinetechsupport.GClass.listOfCallHour;
import static com.falconssoft.onlinetechsupport.GClass.listOfCallHourByEng;
import static com.falconssoft.onlinetechsupport.GClass.managerDashBord;
import static com.falconssoft.onlinetechsupport.GClass.outCount;
import static com.falconssoft.onlinetechsupport.GClass.sizeProgress;
import static com.falconssoft.onlinetechsupport.GClass.systemDashBordListByEng;
import static com.falconssoft.onlinetechsupport.GClass.systemList;
import static com.falconssoft.onlinetechsupport.GClass.systemListDashBoardSystem;
import static com.falconssoft.onlinetechsupport.GClass.systemListDashOnlyMax;
import static com.falconssoft.onlinetechsupport.GClass.systemMList;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.MainActivity.cheakIn;
import static com.falconssoft.onlinetechsupport.MainActivity.cheakout;
import static com.falconssoft.onlinetechsupport.MainActivity.countChickHold;
import static com.falconssoft.onlinetechsupport.MainActivity.countChickIn;
import static com.falconssoft.onlinetechsupport.MainActivity.countChickOut;
import static com.falconssoft.onlinetechsupport.MainActivity.countOfScheduleS;
import static com.falconssoft.onlinetechsupport.MainActivity.countSchedule;
import static com.falconssoft.onlinetechsupport.MainActivity.hold;
import static com.falconssoft.onlinetechsupport.MainActivity.refresh;
import static com.falconssoft.onlinetechsupport.MainActivity.schaeduale;
import static com.falconssoft.onlinetechsupport.OnlineCenter.EngId;
import static com.falconssoft.onlinetechsupport.OnlineCenter.checkInList;
import static com.falconssoft.onlinetechsupport.OnlineCenter.hold_List;
import static com.falconssoft.onlinetechsupport.OnlineCenter.recyclerView;
import static com.falconssoft.onlinetechsupport.OnlineCenter.recyclerViewCheckIn;
import static com.falconssoft.onlinetechsupport.OnlineCenter.textState;
import static com.falconssoft.onlinetechsupport.OnlineCenter.text_finish;

public class ManagerImport {

    private Context context;
    boolean holdin = false;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogSave;
    private JSONObject obj;

    String itemCode;
    String JsonResponseSave;
    String JsonResponseSaveSwitch;
    JSONObject datatoSend = null, upDateHold = null;
    public static boolean sendSucsses = false;
    public static String ipAddres = "";
    DatabaseHandler databaseHandler;
    public static TextView countOfCall = null;
    CallCenterTrackingReport callCenterTrackingReport;
    EngineersTrackingReport engineersTrackingReport;
    TechnicalTrackingReport technicalTrackingReport;
    DashBoard dashBoard;
    String EngIdDashBord, sysIdDashboard;
    ManagerLayout managerLayoutTrans;
    int flag = 0;
    Dialog dialogs;
    String fDateDash, sDateDash, dateTime;


    public ManagerImport(Context context) {//, JSONObject obj
//        this.obj = obj;
        this.context = context;
        databaseHandler = new DatabaseHandler(context);
        ipAddres = databaseHandler.getIp();


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
        if (flag.equals("CountCallWork"))
            new CountCallWork().execute();
        if (flag.equals("CustomerPhone"))
            new ImportPhoneNo().execute();


//http://10.0.0.214/onlineTechnicalSupport/export.php?CUSTOMER_INFO=[{CUST_NAME:%22fALCONS%22,COMPANY_NAME:%22MASTER%22,SYSTEM_NAME:%22rESTURANT%22,PHONE_NO:%220784555545%22,CHECH_IN_TIME:%2202:25%22,STATE:%221%22,ENG_NAME:%22ENG.RAWAN%22}]

    }

    public void refreshHold(String flag) {
//        Log.e("check",flag);
        if (flag.equals("GetHold"))
            new SyncHoldLayout().execute();


//http://10.0.0.214/onlineTechnicalSupport/export.php?CUSTOMER_INFO=[{CUST_NAME:%22fALCONS%22,COMPANY_NAME:%22MASTER%22,SYSTEM_NAME:%22rESTURANT%22,PHONE_NO:%220784555545%22,CHECH_IN_TIME:%2202:25%22,STATE:%221%22,ENG_NAME:%22ENG.RAWAN%22}]

    }

    public void startSendingEngSys(Object object, int flag) {

        new SystemEngineer(object, flag).execute();
    }

    public void startSendingData(JSONObject data, boolean holds, int flagT, ManagerLayout managerLayout, Dialog dialog) {
        sendSucsses = false;
        datatoSend = data;
        holdin = holds;
        managerLayoutTrans = managerLayout;
        flag = flagT;
        dialogs = dialog;

        new SyncManagerLayoutIN().execute();
//http://10.0.0.214/onlineTechnicalSupport/export.php?CUSTOMER_INFO=[{CUST_NAME:%22fALCONS%22,COMPANY_NAME:%22MASTER%22,SYSTEM_NAME:%22rESTURANT%22,PHONE_NO:%220784555545%22,CHECH_IN_TIME:%2202:25%22,STATE:%221%22,ENG_NAME:%22ENG.RAWAN%22}]

    }

    public void startUpdateHold(JSONObject data) {
        upDateHold = data;
        new updateHoldLayout().execute();
    }


    public void dashBoardData(Object object, String date) {
        dashBoard = (DashBoard) object;
        dateTime = date;
        new importDataDashBord().execute();
        new importDataDashBordEng().execute();
        new getDataSystem().execute();
        new getInOutHoldDataByDate().execute();

    }

    public void dashBoardDataByEngId(Object object, String engId, String date) {
        dashBoard = (DashBoard) object;
        EngIdDashBord = engId;
        dateTime = date;
        new getDataByEngId().execute();
        new getDataSystemByEngId().execute();

    }

    public void dashBoardDataBySysId(Object object, String sysId, String date) {
        dashBoard = (DashBoard) object;
        sysIdDashboard = sysId;
        dateTime = date;
        new getDataBySysId().execute();

    }


    public void dashBoardDataByTwoDate(Object object, String fDate, String sDate) {
        dashBoard = (DashBoard) object;
        fDateDash = fDate;
        sDateDash = sDate;

        new getDataByTowDate().execute();

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
                ipAddres = databaseHandler.getIp();
                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=1";
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

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.N)
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

                    String curentTime = CURRENT_TIME.getString("CURRENT_TIME");
                    Log.e("CURRENT_TIME", "" + CURRENT_TIME.getString("CURRENT_TIME"));

                    JSONObject parentArrayD = parentArrayS.getJSONObject(1);
                    JSONArray parentArray = parentArrayD.getJSONArray("CUSTOMER_INFO");

                    int cheakInCount = cheakIn.size();
                    int cheakoutCount = cheakout.size();
                    int holdCount = hold.size();

                    cheakIn.clear();
                    cheakout.clear();
                    hold.clear();
                    schaeduale.clear();

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
                        obj.setCallCenterName(finalObject.getString("CALL_CENTER_NAME"));
                        obj.setOriginalSerial(finalObject.getString("ORGINAL_SERIAL"));
                        obj.setTransactionDate(finalObject.getString("DATE_OF_TRANSACTION"));
                        obj.setHoldReason(finalObject.getString("HOLD_REASON"));
                       obj.setTecType(finalObject.getString("TEC_TYPE"));
                       obj.setCancelState(finalObject.getString("ROW_STATUS"));
                        Log.e("finalObjectConvert", "" + obj.getConvertFlag());

                        obj.setCurrentTime(curentTime);

                        if (obj.getState().equals("1")) {
                            cheakIn.add(obj);
                        } else if (obj.getState().equals("2")) {
                            cheakout.add(obj);
                        } else if (obj.getState().equals("0")&&obj.getCancelState().equals("0")) {
                            hold.add(obj);
                        }else if (obj.getState().equals("3")&&obj.getCancelState().equals("0")) {
                            schaeduale.add(obj);
                        }


                    }

                    countChickHold.setText("" + hold.size());
                    countChickOut.setText("" + cheakout.size());
                    countChickIn.setText("" + cheakIn.size());
                    countSchedule.setText("" + schaeduale.size());
                    countOfScheduleS.setText("" + schaeduale.size());
//                    inCount=cheakIn.size();
                    refresh.setText("1");


                    int cheakInCount1 = cheakIn.size();
                    int cheakoutCount1 = cheakout.size();
                    int holdCount1 = hold.size();


                    if (cheakInCount1 > cheakInCount) {
                        refresh.setText("2");
                    }

                    Log.e("tag_itemCard", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
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
                ipAddres = databaseHandler.getIp();
                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=1";
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

                    String curentTime = CURRENT_TIME.getString("CURRENT_TIME");
                    Log.e("CURRENT_TIME", "" + CURRENT_TIME.getString("CURRENT_TIME"));

                    JSONObject parentArrayD = parentArrayS.getJSONObject(1);
                    JSONArray parentArray = parentArrayD.getJSONArray("CUSTOMER_INFO");

                    int cheakInCount = checkInList.size();

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
                        obj.setOriginalSerial(finalObject.getString("ORGINAL_SERIAL"));
                        obj.setCompanyId(finalObject.getString("COMPANY_ID"));
                        obj.setCurrentTime(curentTime);


                        if (obj.getState().equals("1") && finalObject.getString("CALL_CENTER_ID").equals(LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1"))) {
                            checkInList.add(obj);
                        }
//                        }else if(obj.getState().equals("2")){
//                            cheakout.add(obj);
//                        }else if(obj.getState().equals("0")){
//                            hold.add(obj);

//                        }


                    }
//                    refresh.setText("1");

                    int cheakInCount1 = checkInList.size();
//
//                    if(cheakInCount1>cheakInCount){
//                        refresh.setText("2");
//                    }

                    setCheckInList();

                    Log.e("check_in", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("check_in", "****Failed to export data");

            }

        }
    }

    private class SyncManagerLayoutIN extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        boolean isHold = false;
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
                ipAddres = databaseHandler.getIp();
                String link = "http://" + ipAddres + "/onlineTechnicalSupport/export.php";

                JSONObject obj = new JSONObject();
                JSONArray NEWI = new JSONArray();

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
                sendSucsses = true;

                Log.e("tag_ItemOCodeSS", "****Success");
//                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("")
//                        .setContentText(" Send Sucsseful")
////                        .hideConfirmButton()
//                        .show();

                //[{"STATE_DESC":"CUSTOMER_INFO SUCCESS","SERIAL":176}]
                String Serial = "";
                try {
                    JSONArray jsonArray = new JSONArray(JsonResponse);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Serial = jsonObject.getString("SERIAL");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (flag == 8) {
                    int id = EngId;
                    EngId = Integer.parseInt(managerLayoutTrans.getEngId());
                    managerLayoutTrans.setTransferToSerial(Serial);
                    managerLayoutTrans.setEngId("" + id);
                    textState.setText("Success");
                    text_finish.setText("AddFinish");
                    Log.e("UpdateTransferSolved", EngId + "     " + id);
                    new UpdateTransferSolved(managerLayoutTrans, dialogs).execute();
                } else {
                    text_finish.setText("finish");
                }

                if (holdin) {
                    textState.setText("Success");
                }


            } else {
                sendSucsses = false;
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
                ipAddres = databaseHandler.getIp();
                String link = "http://" + ipAddres + "/onlineTechnicalSupport/export.php";

                JSONObject obj = new JSONObject();
                JSONArray NEWI = new JSONArray();

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
                sendSucsses = true;

                startSending("GetHold");
                text_finish.setText("finish");
                Log.e("updatehold", "****Success");
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("send hold data Success")
//                        .hideConfirmButton()
                        .show();


                textState.setText("Success");


            } else {
                sendSucsses = false;
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
                ipAddres = databaseHandler.getIp();
                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=1";
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

                    String curentTime = CURRENT_TIME.getString("CURRENT_TIME");
                    Log.e("CURRENT_TIME", "" + CURRENT_TIME.getString("CURRENT_TIME"));

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
                        obj.setOriginalSerial(finalObject.getString("ORGINAL_SERIAL"));
                        obj.setCallCenterId(finalObject.getString("CALL_CENTER_ID"));
                        obj.setHoldTime(finalObject.getString("HOLD_TIME"));
                        obj.setCancelState(finalObject.getString("ROW_STATUS"));

                        if ((obj.getState().equals("0")|| obj.getState().equals("3") )&& finalObject.getString("CALL_CENTER_ID").equals(LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1"))) {
                            if (obj.getCancelState().equals("0")) {
                                hold_List.add(obj);
                                Log.e("hold_List", "" + hold_List.size());
                            }
                        }
                        setHoldList();


                    }

                    Log.e("tag_itemCard", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
            }

        }
    }

    private class CountCallWork extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();
                final String callId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1");
                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=4&CALL_ID=" + callId;
                // ITEM_CARD


//                String data = "FLAG=" + URLEncoder.encode("0", "UTF-8");
////
                Log.e("CountCallWork", "" + callId + "   " + link);

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

            if (JsonResponse != null && JsonResponse.contains("CALL_COUNT")) {
                Log.e("CALL_COUNT", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject COUNT = new JSONObject(JsonResponse);

                    JSONArray parentArrayS = COUNT.getJSONArray("CALL_COUNT");

                    JSONObject CURRENT_Count = parentArrayS.getJSONObject(0);

                    String count = CURRENT_Count.getString("COUNT");
                    Log.e("CURRENT_TIME", "" + CURRENT_Count.getString("COUNT"));
                    countOfCall.setText("" + count);


                    Log.e("tag_itemCard", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
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


    private class SystemEngineer extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        Object object;
        int sysEngFlag = 0;

        //EngineersTrackingReport engineersTrackingReport;
        public SystemEngineer(Object object, int flag) {
            this.sysEngFlag = flag;
            if (sysEngFlag == 0) {

                callCenterTrackingReport = (CallCenterTrackingReport) object;
            } else if (sysEngFlag == 1) {
                engineersTrackingReport = (EngineersTrackingReport) object;
            } else if (sysEngFlag == 2) {
                dashBoard = (DashBoard) object;
            } else if (sysEngFlag == 3) {
                technicalTrackingReport = (TechnicalTrackingReport) object;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {///GetModifer?compno=736&compyear=2019
            try {
//
                ipAddres = databaseHandler.getIp();

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";


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

            if (JsonResponse != null && (JsonResponse.contains("ENGINEER_INFO") || JsonResponse.contains("SYSTEMS"))) {
                Log.e("CALL_COUNT", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(JsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception ex) {

                }
                try {


                    JSONArray info = jsonObject.getJSONArray("ENGINEER_INFO");
                    Log.e("info", "" + info);
                    engMList.clear();
                    engineerInfoList.clear();
                    engineerInfoList.add(0, "All");
                    engList.clear();
                    engMList.add(0, new EngineerInfo());
                    engList.add(0, "All");
                    for (int i = 0; i < info.length(); i++) {
                        JSONObject engineerInfoObject = info.getJSONObject(i);
                        EngineerInfo engineerInfo = new EngineerInfo();
                        engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                        engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                        engineerInfo.setEng_type(engineerInfoObject.getInt("ENG_TYPE"));
                        engineerInfo.setState(engineerInfoObject.getInt("STATE"));

                        Log.e("ENG_TYPE", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                        if (sysEngFlag == 3) {

                            if (engineerInfo.getEng_type() == 4) {

                                //arr
                                engMList.add(engineerInfo);
                                engList.add(engineerInfo.getName());


                            } else {
                                if (engineerInfo.getEng_type() == 3)

                                    engineerInfoList.add(engineerInfo.getName());
                            }

                        } else {
                            if (engineerInfo.getEng_type() == 2) {

                                //arr
                                engMList.add(engineerInfo);
                                engList.add(engineerInfo.getName());


                            } else {
                                if (engineerInfo.getEng_type() == 1)

                                    engineerInfoList.add(engineerInfo.getName());
                            }

                        }


                    }


                    Log.e("tag_itemCard", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception ex) {

                }


                try {
                    JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
                    systemMList.clear();
                    systemList.clear();
                    systemMList.add(0, new Systems());
                    systemList.add("All");
                    for (int i = 0; i < systemInfoArray.length(); i++) {
                        JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
                        Systems systemInfo = new Systems();
                        systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
                        systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));

                        systemMList.add(systemInfo);
                        systemList.add(systemInfo.getSystemName());
                    }
                } catch (Exception e) {
                    Log.e("No_Sys       ", "Exception");

                }

                if (sysEngFlag == 0) {

                    callCenterTrackingReport.fillSpinners();
                } else if (sysEngFlag == 1) {
                    engineersTrackingReport.fillSpinners();
                } else if (sysEngFlag == 2) {
//                    dashBoard.fillChart();
                    GClass gClass = new GClass(null, null, null);
                    gClass.filllistOfEngList();
                    gClass.filllistOfSystemList();

                } else if (sysEngFlag == 3) {
                    technicalTrackingReport.fillSpinners();
                }


            } else {
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

    @SuppressLint("WrongConstant")
    private void setHoldList() {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        Log.e("fillHoldList", "" + hold_List.size());
        if (hold_List.size() != 0) {
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
        if (checkInList.size() != 0) {
            final CheckInCompanyAdapter companyAdapter = new CheckInCompanyAdapter((OnlineCenter) context, checkInList);

            recyclerViewCheckIn.setLayoutManager(llm);
            recyclerViewCheckIn.setAdapter(companyAdapter);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getDateTime(Date date) {

        Date d2 = new Date(date.getTime() + 1 * 60 * 60 * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String dateTime = dateFormat.format(d2);
//        Log.e("getDateTime",date+"       -->"+dateTime+"\n");
        return dateTime;
    }

    private class UpdateTransferSolved extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        Dialog dialog;
        ManagerLayout managerLayout;

        public UpdateTransferSolved(ManagerLayout managerLayout, Dialog dialog) {
            this.managerLayout = managerLayout;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ipAddres = databaseHandler.getIp();
                String link = "http://" + ipAddres + "//onlineTechnicalSupport/export.php";

                JSONObject object = new JSONObject();
                try {

                    Log.e("problemDataurlString = ", "" + managerLayout.getSerial());

                    object.put("CHECH_OUT_TIME", "00:00:00");
                    object.put("PROBLEM", managerLayout.getProplem());
                    object.put("CUST_NAME", managerLayout.getCustomerName());
                    object.put("CHECH_IN_TIME", managerLayout.getCheakInTime());
                    object.put("COMPANY_NAME", managerLayout.getCompanyName());
                    object.put("PHONE_NO", managerLayout.getPhoneNo());
                    object.put("SYSTEM_NAME", managerLayout.getSystemName());
                    object.put("SYS_ID", managerLayout.getSystemId());
                    object.put("ENG_ID", managerLayout.getEngId());
                    object.put("ENG_NAME", managerLayout.getEnginerName());
                    object.put("STATE", 0);
                    object.put("SERIAL", managerLayout.getSerial());
                    object.put("CONVERT_STATE", managerLayout.getConvertFlag());

                    object.put("TRANSFER_FLAG", managerLayout.getTransferFlag());
                    object.put("TRANSFER_TO_ENG_ID", managerLayout.getTransferToEngId());
                    object.put("TRANSFER_TO_ENG_NAME", managerLayout.getTransferToEngName());
                    object.put("TRANSFER_RESON", managerLayout.getTransferReason());
                    object.put("TRANSFER_TO_SERIAL", managerLayout.getTransferToSerial());

                    if (managerLayout.getOriginalSerial().equals("-2")) {
                        object.put("ORGINAL_SERIAL", "-1");
                        Log.e("ORGINAL_SERIALI", "getSerial " + managerLayout.getOriginalSerial());
                    } else {
                        object.put("ORGINAL_SERIAL", managerLayout.getOriginalSerial());
                        Log.e("ORGINAL_SERIALI", "getOriginalSerial " + managerLayout.getOriginalSerial());
                    }


//                    object.put("CALL_CENTER_ID", "'"+customerOnlineGlobel.getCallId()+"'");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String data = "TRANSFER_SOLVED=" + URLEncoder.encode(object.toString(), "UTF-8");

                URL url = new URL(link);
                Log.e("TRANSFER_SOLVED= ", "" + url.toString());
                Log.e("TRANSFER_SOLVED= ", "" + data);
//                Log.e("serial12344 = ", "" + customerOnlineGlobel.getSerial());
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

                Log.e("tag", "ItemOCodegggppp -->" + stringBuffer.toString());

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

            if (JsonResponse != null && JsonResponse.contains("PROBLEM_SOLVED SUCCESS")) {
                Log.e("PROBLEM_SOLVED_", "****Success" + JsonResponse.toString());
//                hideCustomerLinear();

                dialog.dismiss();
//                context.FillCheckIn();
                text_finish.setText("finish");

            } else {

                Log.e("PROBLEM_SOLVED_", "****Failed to export data");

            }


        }

    }

    private class ImportPhoneNo extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();
                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=77";
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

                Log.e("CUSTOMER_PHONE_tag", "stringBuffer -->" + stringBuffer.toString());

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
                        Log.e("CUSTOMER_PHONE_tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("CUSTOMER_PHONE")) {
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject jsonObject = new JSONObject(JsonResponse);
                    JSONArray parentArrayS = jsonObject.getJSONArray("CUSTOMER_PHONE");

                    customerPhoneNo.clear();

                    for (int i = 0; i < parentArrayS.length(); i++) {
                        JSONObject finalObject = parentArrayS.getJSONObject(i);

                        ManagerLayout customerInfo = new ManagerLayout();
                        customerInfo.setPhoneNo(finalObject.getString("PHONE_NO"));
                        customerInfo.setCustomerName(finalObject.getString("CUST_NAME"));
                        customerInfo.setCompanyName(finalObject.getString("COMPANY_NAME"));
                        customerInfo.setCompanyId(finalObject.getString("COMPANY_ID"));
                        customerPhoneNo.add(customerInfo);
//                        Log.e("customerPhoneNo", "****  "+customerPhoneNo.get(i));
                    }

                    Log.e("customerPhoneNo", "****saveSuccess");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("customerPhoneNo", "****Failed to export data");


            }

        }
    }

    private class importDataDashBord extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();
                //{"CHECH_IN_HOUR":[{"HOUR":"13","COUNT(*)":"9"},{"HOUR":"11","COUNT(*)":"11"},{"HOUR":"12","COUNT(*)":"20"},{"HOUR":"10","COUNT(*)":"8"}],"HOLD_HOUR":[{"HOUR2":"13","COUNT(*)":"9"},{"HOUR2":"11","COUNT(*)":"11"},{"HOUR2":"12","COUNT(*)":"20"},{"HOUR2":"10","COUNT(*)":"8"}],"CHECH_OUT_HOUR":[{"HOUR3":"00","COUNT(*)":"6"},
                // {"HOUR3":"13","COUNT(*)":"9"},{"HOUR3":"11","COUNT(*)":"11"},{"HOUR3":"12","COUNT(*)":"18"},{"HOUR3":"10","COUNT(*)":"4"}]}

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=8&DATE='" + dateTime + "'";
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

                Log.e("tag", "MDashBoard -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);
// //{"CHECH_IN_HOUR":[{"HOUR":"13","COUNT(*)":"9"},{"HOUR":"11","COUNT(*)":"11"},{"HOUR":"12","COUNT(*)":"20"},
// {"HOUR":"10","COUNT(*)":"8"}],"HOLD_HOUR":[{"HOUR2":"13","COUNT(*)":"9"},
// {"HOUR2":"11","COUNT(*)":"11"},{"HOUR2":"12","COUNT(*)":"20"},{"HOUR2":"10","COUNT(*)":"8"}],"CHECH_OUT_HOUR":[{"HOUR3":"00","COUNT(*)":"6"},
//                // {"HOUR3":"13","COUNT(*)":"9"},{"HOUR3":"11","COUNT(*)":"11"},{"HOUR3":"12","COUNT(*)":"18"},{"HOUR3":"10","COUNT(*)":"4"}]}
//
            if (JsonResponse != null && JsonResponse.contains("CHECH_IN_HOUR")) {
                Log.e("tag_ItemOCode", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    listOfCallHour.clear();
                    GClass gClass = new GClass(null, null, null);
                    gClass.filllistOfCallHour();
                    List<ManagerLayout> temp = new ArrayList<>();
                    List<ManagerLayout> temp2 = new ArrayList<>();
                    List<ManagerLayout> temp3 = new ArrayList<>();

                    try {


                        JSONArray CHECH_IN_HOUR = parentArrayS.getJSONArray("CHECH_IN_HOUR");

                        inCount = 0;
                        outCount = 0;
                        holdCounts = 0;

                        for (int i = 0; i < CHECH_IN_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_IN_HOUR.getJSONObject(i);

                            ManagerLayout obj = new ManagerLayout();
                            obj.setHour(finalObject.getDouble("HOUR"));
                            obj.setCount(finalObject.getDouble("COUNT(*)"));

//                        inCount= (int) obj.getCount();
                            listOfCallHour.get(0).get((int) obj.getHour()).setCount(obj.getCount());


                        }

                    } catch (Exception e) {
//                        ManagerLayout managerLayout=new ManagerLayout();
//                        managerLayout.setHour(0);
//                        managerLayout.setCount(0);
//
//                        temp.add(managerLayout);
//                        listOfCallHour.add(temp);
                    }

                    try {
                        JSONArray HOLD_HOUR = parentArrayS.getJSONArray("HOLD_HOUR");


                        for (int i = 0; i < HOLD_HOUR.length(); i++) {
                            JSONObject finalObject = HOLD_HOUR.getJSONObject(i);

                            ManagerLayout obj = new ManagerLayout();
                            obj.setHour(finalObject.getDouble("HOUR2"));
                            obj.setCount(finalObject.getDouble("COUNT(*)"));
//                        holdCounts= (int) obj.getCount();
//                        temp2.add(obj);
                            listOfCallHour.get(1).get((int) obj.getHour()).setCount(obj.getCount());

                        }
//                    listOfCallHour.add(temp2);
                    } catch (Exception e) {
//                        ManagerLayout managerLayout=new ManagerLayout();
//                        managerLayout.setHour(0);
//                        managerLayout.setCount(0);
//
//                        temp2.add(managerLayout);
//                        listOfCallHour.add(temp2);
                    }

                    try {
                        JSONArray CHECH_OUT_HOUR = parentArrayS.getJSONArray("CHECH_OUT_HOUR");
                        for (int i = 0; i < CHECH_OUT_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_OUT_HOUR.getJSONObject(i);

                            ManagerLayout obj = new ManagerLayout();
                            obj.setHour(finalObject.getDouble("HOUR3"));
                            obj.setCount(finalObject.getDouble("COUNT(*)"));
//                        temp3.add(obj);
//                        outCount= (int) (outCount+obj.getCount());

                            listOfCallHour.get(2).get((int) obj.getHour()).setCount(obj.getCount());

//                        dashBoard.dataCall();
                        }
//                    listOfCallHour.add(temp3);
                    } catch (Exception e) {

//                          ManagerLayout managerLayout=new ManagerLayout();
//                          managerLayout.setHour(0);
//                          managerLayout.setCount(0);
//
//                          temp3.add(managerLayout);
//                    listOfCallHour.add(temp3);
                    }

                    Log.e("MDashBoard2", "****saveSuccess");

                    dashBoard.fillCallCountPerHour();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
//                Toast.makeText(context, "Failed to Get data", Toast.LENGTH_SHORT).show();
//                if (pd != null) {
//                    pd.dismiss();
//                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText(context.getResources().getString(R.string.ops))
//                            .setContentText(context.getResources().getString(R.string.fildtoimportitemswitch))
//                            .show();
//                }

                listOfCallHour.clear();
                GClass gClass = new GClass(null, null, null);
                gClass.filllistOfCallHour();
                dashBoard.fillCallCountPerHour();

            }

        }
    }

    private class importDataDashBordEng extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();
                //{"CHECH_IN_HOUR":[{"HOUR":"13","COUNT(*)":"9"},{"HOUR":"11","COUNT(*)":"11"},{"HOUR":"12","COUNT(*)":"20"},{"HOUR":"10","COUNT(*)":"8"}],"HOLD_HOUR":[{"HOUR2":"13","COUNT(*)":"9"},{"HOUR2":"11","COUNT(*)":"11"},{"HOUR2":"12","COUNT(*)":"20"},{"HOUR2":"10","COUNT(*)":"8"}],"CHECH_OUT_HOUR":[{"HOUR3":"00","COUNT(*)":"6"},
                // {"HOUR3":"13","COUNT(*)":"9"},{"HOUR3":"11","COUNT(*)":"11"},{"HOUR3":"12","COUNT(*)":"18"},{"HOUR3":"10","COUNT(*)":"4"}]}

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=9&DATE='" + dateTime + "'";
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

                Log.e("tag", "MDashBoardHOUR_PER_ENG -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);
// //{"CHECH_IN_HOUR":[{"HOUR":"13","COUNT(*)":"9"},{"HOUR":"11","COUNT(*)":"11"},{"HOUR":"12","COUNT(*)":"20"},
// {"HOUR":"10","COUNT(*)":"8"}],"HOLD_HOUR":[{"HOUR2":"13","COUNT(*)":"9"},
// {"HOUR2":"11","COUNT(*)":"11"},{"HOUR2":"12","COUNT(*)":"20"},{"HOUR2":"10","COUNT(*)":"8"}],"CHECH_OUT_HOUR":[{"HOUR3":"00","COUNT(*)":"6"},
//                // {"HOUR3":"13","COUNT(*)":"9"},{"HOUR3":"11","COUNT(*)":"11"},{"HOUR3":"12","COUNT(*)":"18"},{"HOUR3":"10","COUNT(*)":"4"}]}
//

            if (JsonResponse != null && JsonResponse.contains("HOUR_PER_ENG")) {
                Log.e("tag_ItemOCode", "****Success");
//                progressDialog.dismiss();
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    try {


                        //{"HOUR_PER_ENG":[{"ENG_ID":"4","ENG_NAME":"نور حماد ","CALL_COUNT":"1","PERC":"3.33"},{"ENG_ID":"9","ENG_NAME":"ايمان عريقات","CALL_COUNT":"2","PERC":"6.67"},{"ENG_ID":"6","ENG_NAME":"مي صوان ","CALL_COUNT":"4","PERC":"13.33"},{"ENG_ID":"11","ENG_NAME":"كوثر صخرية","CALL_COUNT":"5","PERC":"16.67"},{"ENG_ID":"12","ENG_NAME":"ايمان خنفر","CALL_COUNT":"5","PERC":"16.67"},{"ENG_ID":"8","ENG_NAME":"لمى الشوبكي","CALL_COUNT":"3","PERC":"10"},{"ENG_ID":"7","ENG_NAME":"تسنيم الشعيبي","CALL_COUNT":"4","PERC":"13.33"},{"ENG_ID":"10","ENG_NAME":"سجى بشير ","CALL_COUNT":"5",
                        // "PERC":"16.67"},{"ENG_ID":"13","ENG_NAME":"بلقيس شنانير","CALL_COUNT":"1","PERC":"3.33"}]}
                        engPerHourList.clear();
                        GClass gClass = new GClass(null, null, null);
                        gClass.filllistOfEngList();

                        JSONArray HOUR_PER_ENG = parentArrayS.getJSONArray("HOUR_PER_ENG");

                        for (int i = 0; i < HOUR_PER_ENG.length(); i++) {
                            JSONObject finalObject = HOUR_PER_ENG.getJSONObject(i);

                            EngineerInfo obj = new EngineerInfo();
                            obj.setId(finalObject.getString("ENG_ID"));
                            obj.setName(finalObject.getString("ENG_NAME"));
                            obj.setNoOfCountCall(finalObject.getDouble("CALL_COUNT"));
                            obj.setPercCall(finalObject.getDouble("PERC"));

                            int index = getIndexOf(engPerHourList, obj.getId());
                            if (index != -1) {
                                Log.e("MaEngPerHourList", "engPerHourList  " + engPerHourList.get(index).getNoOfCountCall());
                                engPerHourList.get(index).setNoOfCountCall(obj.getNoOfCountCall());
                                engPerHourList.get(index).setPercCall(obj.getPercCall());
                                Log.e("MaEngPerHourList2", "engPerHourList  " + engPerHourList.get(index).getNoOfCountCall());


                            }
                        }

                    } catch (Exception e) {
//
                    }

                    Log.e("MDashBoard2", "****saveSuccess");

                    dashBoard.fillChart();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
//                Toast.makeText(context, "Failed to Get data", Toast.LENGTH_SHORT).show();
//                if (pd != null) {
//                    pd.dismiss();
//                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText(context.getResources().getString(R.string.ops))
//                            .setContentText(context.getResources().getString(R.string.fildtoimportitemswitch))
//                            .show();
//                }
                engPerHourList.clear();
                GClass gClass = new GClass(null, null, null);
                gClass.filllistOfEngList();
                dashBoard.fillChart();

            }

        }
    }


    private class getDataByEngId extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=10&ENG_ID=" + EngIdDashBord + "&DATE='" + dateTime + "'";
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

                Log.e("tag", "listOfCallHourByEng -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("CALLS_CHECK_IN")) {
                Log.e("tag_ItemOCode", "****Success");
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    listOfCallHourByEng.clear();
                    GClass gClass = new GClass(null, null, null);
                    gClass.filllistOfCallHourByEng();
                    List<ManagerLayout> temp = new ArrayList<>();
                    List<ManagerLayout> temp2 = new ArrayList<>();
                    List<ManagerLayout> temp3 = new ArrayList<>();

                    try {


                        JSONArray CHECH_IN_HOUR = parentArrayS.getJSONArray("CALLS_CHECK_IN");

                        for (int i = 0; i < CHECH_IN_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_IN_HOUR.getJSONObject(i);

                            ManagerLayout obj = new ManagerLayout();
                            obj.setHour(finalObject.getDouble("CHECK_IN_HOUR"));
                            obj.setCount(finalObject.getDouble("CHECK_IN_COUNT"));


                            listOfCallHourByEng.get(0).get((int) obj.getHour()).setCount(obj.getCount());


                        }

                    } catch (Exception e) {
                    }

                    try {
                        JSONArray HOLD_HOUR = parentArrayS.getJSONArray("CALLS_CHECK_HOLD");


                        for (int i = 0; i < HOLD_HOUR.length(); i++) {
                            JSONObject finalObject = HOLD_HOUR.getJSONObject(i);

                            ManagerLayout obj = new ManagerLayout();
                            obj.setHour(finalObject.getDouble("HOLD_HOUR"));
                            obj.setCount(finalObject.getDouble("HOLD_COUNT"));
                            listOfCallHourByEng.get(2).get((int) obj.getHour()).setCount(obj.getCount());

                        }
                    } catch (Exception e) {
                    }

                    try {
                        JSONArray CHECH_OUT_HOUR = parentArrayS.getJSONArray("CALLS_CHECK_OUT");
                        for (int i = 0; i < CHECH_OUT_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_OUT_HOUR.getJSONObject(i);

                            ManagerLayout obj = new ManagerLayout();
                            obj.setHour(finalObject.getDouble("CHECK_OUT_HOUR"));
                            obj.setCount(finalObject.getDouble("CHECK_OUT_COUNT"));
                            listOfCallHourByEng.get(1).get((int) obj.getHour()).setCount(obj.getCount());

                        }
                    } catch (Exception e) {
                    }

                    Log.e("listOfCallHourByEng", "****saveSuccess");

                    dashBoard.fill();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
            }

        }
    }

    private class getDataSystemByEngId extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=11&ENG_ID=" + EngIdDashBord + "&DATE='" + dateTime + "'";
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

                Log.e("tag", "listOfCallHourByEng -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("ENG_SYSTEM_COUNT")) {
                Log.e("tag_ItemOCode", "****Success");
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    systemDashBordListByEng.clear();
//                    GClass gClass=new GClass(null,null);
//                    gClass.filllistOfCallHourByEng();

                    try {


                        JSONArray CHECH_IN_HOUR = parentArrayS.getJSONArray("ENG_SYSTEM_COUNT");

                        for (int i = 0; i < CHECH_IN_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_IN_HOUR.getJSONObject(i);

                            Systems obj = new Systems();

                            obj.setSystemName(finalObject.getString("SYSTEM_NAME"));
                            obj.setSystemCount(finalObject.getDouble("COUNT(SYSTEM_NAME)"));

                            systemDashBordListByEng.add(obj);

                        }

                    } catch (Exception e) {
                    }

                    Log.e("listOfCallHourByEng", "****saveSuccess");

                    dashBoard.fill2();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
            }

        }
    }


    private class getDataSystem extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=12&DATE='" + dateTime + "'";
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

                Log.e("tag", "listOfCallHourByEng -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("SYSTEM_COUNT")) {
                Log.e("tag_ItemOCode", "****Success");
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    systemListDashBoardSystem.clear();
                    systemListDashOnlyMax.clear();
                    GClass gClass = new GClass(null, null, null);
                    gClass.filllistOfSystemList();

                    try {


                        JSONArray CHECH_IN_HOUR = parentArrayS.getJSONArray("SYSTEM_COUNT");

                        for (int i = 0; i < CHECH_IN_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_IN_HOUR.getJSONObject(i);

                            Systems obj = new Systems();

                            obj.setSystemName(finalObject.getString("SYSTEM_NAME"));
                            obj.setSystemCount(finalObject.getDouble("COUNT(*)"));
                            obj.setSystemNo(finalObject.getString("SYS_ID"));
                            systemListDashOnlyMax.add(obj);
                            int index = getIndexOfSystem(systemListDashBoardSystem, obj.getSystemNo());
                            if (index != -1) {
                                Log.e("MaEngPerHourList", "engPerHourList  " + systemListDashBoardSystem.get(index).getSystemName());
                                systemListDashBoardSystem.get(index).setSystemCount(obj.getSystemCount());
                                Log.e("MaEngPerHourList2", "engPerHourList  " + systemListDashBoardSystem.get(index).getSystemName());
                            }


                        }

                    } catch (Exception e) {
                    }

                    Log.e("listOfCallHourByEng", "****saveSuccess");

                    dashBoard.fill3();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");

                systemListDashBoardSystem.clear();
                systemListDashOnlyMax.clear();
                GClass gClass = new GClass(null, null, null);
                gClass.filllistOfSystemList();
                dashBoard.fill3();
            }

        }
    }


    private class getDataBySysId extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=13&SYSTEM_ID=" + sysIdDashboard + "&DATE='" + dateTime + "'";
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

                Log.e("tag", "listOfCallHourByEng -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("SYSTEM_ENG")) {
                Log.e("tag_ItemOCode", "****Success");
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    engPerSysList.clear();
//                    GClass gClass=new GClass(null,null);
//                    gClass.filllistOfCallHourByEng();


                    try {


                        JSONArray CHECH_IN_HOUR = parentArrayS.getJSONArray("SYSTEM_ENG");

                        for (int i = 0; i < CHECH_IN_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_IN_HOUR.getJSONObject(i);

                            EngineerInfo obj = new EngineerInfo();
                            obj.setName(finalObject.getString("ENG_NAME"));
                            obj.setNoOfCountCall(finalObject.getDouble("COUNT"));

                            engPerSysList.add(obj);
                        }

                    } catch (Exception e) {
                    }


                    Log.e("listOfCallHourByEng", "****saveSuccess");

                    dashBoard.fill4();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
                engPerSysList.clear();
                dashBoard.fill4();
            }

        }
    }

    private class getInOutHoldDataByDate extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=15&DATE='" + dateTime + "'";
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

                Log.e("tag", "listOfCallHourByEng -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("TOTAL_CALLS")) {
                Log.e("tag_ItemOCode", "****Success");
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    sizeProgress.clear();
//                    GClass gClass=new GClass(null,null);
//                    gClass.filllistOfCallHourByEng();


                    try {


                        JSONArray CHECH_IN_HOUR = parentArrayS.getJSONArray("TOTAL_CALLS");

                        for (int i = 0; i < CHECH_IN_HOUR.length(); i++) {
                            JSONObject finalObject = CHECH_IN_HOUR.getJSONObject(i);

//                            EngineerInfo obj = new EngineerInfo();
//                            obj.setName(finalObject.getString("ENG_NAME"));
//                            obj.setNoOfCountCall(finalObject.getDouble("COUNT"));
                            sizeProgress.add(finalObject.getInt("CHECH_IN"));
                            sizeProgress.add(finalObject.getInt("CHECH_OUT"));
                            sizeProgress.add(finalObject.getInt("HOLD"));

                        }

                    } catch (Exception e) {
                    }


                    Log.e("listOfCallHourByEng", "****saveSuccess");

                    dashBoard.dataCall();
                    dashBoard.fillChartPipePros();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
                sizeProgress.clear();
                dashBoard.dataCall();
                dashBoard.fillChartPipePros();
            }

        }
    }

    private class getDataByTowDate extends AsyncTask<String, String, String> {
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
                ipAddres = databaseHandler.getIp();

                String link = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=14&DATE=\"" + fDateDash + "\"&DATE2=\"" + sDateDash + "\"";
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

                Log.e("tag", "listOfCallHourByEng -->" + stringBuffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("FIRST_DAY")) {
                Log.e("tag_ItemOCode", "****Success");
                JsonResponseSave = JsonResponse;

                try {

                    JSONObject parentArrayS = new JSONObject(JsonResponse);

                    managerDashBord.clear();
//                    GClass gClass=new GClass(null,null);
//                    gClass.filllistOfCallHourByEng();


                    try {


                        JSONArray FIRST_DAY = parentArrayS.getJSONArray("FIRST_DAY");

                        for (int i = 0; i < FIRST_DAY.length(); i++) {
                            JSONObject finalObject = FIRST_DAY.getJSONObject(i);

                            ManagerLayout obj = new ManagerLayout();
                            obj.setFirstHour(finalObject.getString("DATE1"));
                            obj.setFirstHourCount(finalObject.getDouble("COUNT1"));

                            obj.setSecondHour(finalObject.getString("DATE2"));
                            obj.setSecondHourCount(finalObject.getDouble("COUNT2"));

                            managerDashBord.add(obj);
                        }

                    } catch (Exception e) {
                    }


                    Log.e("listOfCallHourByEng", "****saveSuccess");

                    dashBoard.fill5();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("tag_itemCard", "****Failed to export data");
                managerDashBord.clear();
                dashBoard.fill5();
            }

        }
    }

    public static int getIndexOf(List<EngineerInfo> list, String name) {
        int pos = 0;

        for (EngineerInfo myObj : list) {
            if (name.equalsIgnoreCase(myObj.getId()))
                return pos;
            pos++;
        }

        return -1;
    }

    public static int getIndexOfSystem(List<Systems> list, String name) {
        int pos = 0;

        for (Systems myObj : list) {
            if (name.equalsIgnoreCase(myObj.getSystemNo()))
                return pos;
            pos++;
        }

        return -1;
    }
}