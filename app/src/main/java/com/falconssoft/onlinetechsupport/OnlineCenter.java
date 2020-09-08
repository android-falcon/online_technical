package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.falconssoft.onlinetechsupport.Modle.CompaneyInfo;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


import static android.widget.LinearLayout.VERTICAL;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;
import static com.falconssoft.onlinetechsupport.LoginActivity.sharedPreferences;
import static com.falconssoft.onlinetechsupport.MainActivity.hold;
import static com.falconssoft.onlinetechsupport.ManagerImport.sendSucsses;

public class OnlineCenter extends AppCompatActivity {
    GridView gridView;
     public static RecyclerView recyclerView;
    List<EngineerInfo> engineerInfoList, listEngforAdapter;
    List<ManagerLayout> holdCompaney;
    public  static TextView customer_name, companey_name, telephone_no,text_delet_id,text_finish,textState,systype;
    TextView callCenterName,LogInTime;
//    Spinner spenner_systems;
    //    String ipAddres = "10.0.0.214";
    DatabaseHandler databaseHandler;

    String ipAddres = "";
    List<Systems> systemsList;
    LinearLayoutManager layoutManager;
    int stateCompaney = -1, selectedEngId = 0,EngId=-1;
    String selectedEngineer = "";
    adapterGridEngineer engineerAdapter;
    Timer timer;
    public static JSONObject updateHold;
    public static List<ManagerLayout> hold_List;
    int idForDelete=0;
    private PresenterClass presenterClass;
    public  static boolean isInHold=false;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_center);
        initialView();
        systype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemGridDialog(systemsList);
            }
        });
        callCenterName.setText(sharedPreferences.getString(LOGIN_NAME, null));
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy   hh:mm:ss");
        String time = df.format(currentTimeAndDate);
        LogInTime.setText(""+time);
        presenterClass = new PresenterClass(this);
        databaseHandler=new DatabaseHandler(OnlineCenter.this);
        ipAddres=databaseHandler.getIp();
       fillEngineerInfoList(0);
        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {

//                fillEngineerInfoList(1);

//            }
//
//        }, 0, 2000);
        fillHoldList();
        //fillListTest();
        //fillSpennerSystem(systemsList);



    }

    @SuppressLint("WrongConstant")
    private void fillHoldList() {

        ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
        managerImport.startSending("GetHold");

    }

    @SuppressLint("WrongConstant")
    private void fillListTest() {
        EngineerInfo engineerInfo = new EngineerInfo();
        engineerInfo.setName("Tahani ghanoum");
        engineerInfo.setId("1");
        EngineerInfo enginee2 = new EngineerInfo();
        enginee2.setName("Rawan ryad");
        engineerInfoList.add(engineerInfo);
        engineerInfoList.add(enginee2);
        engineerInfoList.add(engineerInfo);
        engineerInfoList.add(engineerInfo);
        engineerInfoList.add(enginee2);
        engineerInfoList.add(enginee2);
        layoutManager = new LinearLayoutManager(OnlineCenter.this);
        layoutManager.setOrientation(VERTICAL);
        final adapterGridEngineer engineerAdapter = new adapterGridEngineer(this, engineerInfoList);
        gridView.setAdapter(engineerAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                selectedEngineer = engineerInfoList.get(arg2).getName();
                selectedEngId = arg2;
                EngId= Integer.parseInt(engineerInfoList.get(arg2).getId());
                for (int i = 0; i < gridView.getChildCount(); i++) {
                    if (i != arg2) {
                        gridView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.layer1));

                    }
                }
                new AlertDialog.Builder(OnlineCenter.this)
                        .setTitle("Confirm")
                        .setMessage("Do you want to check in this company to eng " + selectedEngineer + " ?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sendCompaneyInfo();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Continue with delete operation
                            }
                        })

                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < gridView.getChildCount(); i++) {

                                            gridView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.layer1));
                                        }
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .setIcon(android.R.drawable.ic_input_add)
                        .show();

                if (gridView.isItemChecked(arg2)) {
//                    arg1= gridView.getChildAt(arg2);
//                    arg1.setBackgroundColor(getResources().getColor(R.color.layer4));
                } else {
//
                    arg1.setBackgroundColor(getResources().getColor(R.color.layer3)); //the color code is the background color of GridView
                }
            }
        });


    }

    @SuppressLint("WrongConstant")
    private void sendEngineerToAdapter() {
        int engType = 0;
        if(engineerInfoList.size()!=0)
        {
            listEngforAdapter.clear();
            for (int i = 0; i < engineerInfoList.size(); i++) {
                engType = Integer.parseInt(String.valueOf(engineerInfoList.get(i).getEng_type()));

                if (engType == 2)// available  Engeneering
                {
                    listEngforAdapter.add(engineerInfoList.get(i));

                }
            }
            final LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(OnlineCenter.this);
            layoutManager.setOrientation(VERTICAL);
            engineerAdapter = new adapterGridEngineer(this, listEngforAdapter);
            gridView.setAdapter(engineerAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        final int arg2, long arg3) {
                    selectedEngineer = engineerInfoList.get(arg2).getName();
                    selectedEngId = arg2;
                    EngId= Integer.parseInt(engineerInfoList.get(arg2).getId());

                    for (int i = 0; i < gridView.getChildCount(); i++) {
                        if (i != arg2) {
                            gridView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.layer1));

                        }
                    }
                    new AlertDialog.Builder(OnlineCenter.this)
                            .setTitle("Confirm")
                            .setMessage("Do you want to check in this company to eng " + selectedEngineer + " ?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        sendCompaneyInfo();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // Continue with delete operation
                                }
                            })

                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            for (int i = 0; i < gridView.getChildCount(); i++) {

                                                gridView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.layer1));
                                            }
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .setIcon(android.R.drawable.ic_input_add)
                            .show();

                    if (gridView.isItemChecked(arg2)) {
//                    arg1= gridView.getChildAt(arg2);
//                    arg1.setBackgroundColor(getResources().getColor(R.color.layer4));
                    } else {
//
                        arg1.setBackgroundColor(getResources().getColor(R.color.layer3)); //the color code is the background color of GridView
                    }
                }
            });
        }else{
            engineerAdapter = new adapterGridEngineer(this, engineerInfoList);
            gridView.setAdapter(engineerAdapter);
        }
    }

    public void  systemGridDialog(final List<Systems>listOfsystem){

        final Dialog fillSysDialog = new Dialog(OnlineCenter.this,R.style.Theme_Dialog);
        fillSysDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fillSysDialog.setCancelable(true);
        fillSysDialog.setContentView(R.layout.sys_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        final GridView SysGrid;

        SysGrid=fillSysDialog.findViewById(R.id.Sysgrid);
//        SysGrid

        adapterGridSystem adapterSystem = new adapterGridSystem(this, listOfsystem);
        SysGrid.setAdapter(adapterSystem);
        systype.setMovementMethod(new ScrollingMovementMethod());

        SysGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    systype.setText(listOfsystem.get(position).getSystemName());

                fillSysDialog.dismiss();


            }
        });

        fillSysDialog.show();
    }

    private void fillEngineerInfoList(final int flag) {
//        if(TextUtils.isEmpty(ipAddres)){
//            Toast.makeText(this, "ip Not Found,Please Add Ip", Toast.LENGTH_SHORT).show();
//        }
        databaseHandler=new DatabaseHandler(OnlineCenter.this);
        ipAddres=databaseHandler.getIp();
        final String url = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if(flag!=0)// 0 ---->  just first time
                            {
                                engineerInfoList.clear();

                                JSONArray info = jsonObject.getJSONArray("ENGINEER_INFO");
                                Log.e("info", "" + info);
                                for (int i = 0; i < info.length(); i++) {
                                    JSONObject engineerInfoObject = info.getJSONObject(i);
                                    EngineerInfo engineerInfo = new EngineerInfo();
                                    engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                                    engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                                    engineerInfo.setEng_type(engineerInfoObject.getInt("ENG_TYPE"));
                                    engineerInfo.setState(engineerInfoObject.getInt("STATE"));

                                    Log.e("ENG_TYPE",""+engineerInfo.getName()+"-->"+engineerInfo.getEng_type());
                                    if( engineerInfo.getEng_type()==2&& engineerInfo.getState()==0)
                                    {
                                        engineerInfoList.add(engineerInfo);
                                        Log.e("ENG_TYPE_in",""+engineerInfo.getName()+"-->"+engineerInfo.getEng_type());

                                    }

                                }
                                sendEngineerToAdapter();

                            }
                            else {
                                engineerInfoList.clear();

                                try {
                                    JSONArray info = jsonObject.getJSONArray("ENGINEER_INFO");
                                    Log.e("info", "" + info);
                                    for (int i = 0; i < info.length(); i++) {
                                        JSONObject engineerInfoObject = info.getJSONObject(i);
                                        EngineerInfo engineerInfo = new EngineerInfo();
                                        engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                                        engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                                        engineerInfo.setState(engineerInfoObject.getInt("STATE"));
                                        engineerInfo.setEng_type(Integer.parseInt(engineerInfoObject.getString("ENG_TYPE")));
                                        if (engineerInfo.getEng_type() == 2 && engineerInfo.getState() == 0) {
                                            engineerInfoList.add(engineerInfo);

                                        }

                                    }
                                } catch (Exception e) {
                                    Log.e("No_Eng", "Exception");

                                }
                                try{
                                    systemsList.clear();
                                    systemsList.add(new Systems ("system Not Known","-1"));
                                JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
                                Log.e("systemInfoArray", "" + systemInfoArray);
                                for (int i = 0; i < systemInfoArray.length(); i++) {
                                    JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
                                    Systems systemInfo = new Systems();
                                    systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
                                    systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));
                                    systemsList.add(systemInfo);
                                }
                            }catch (Exception e){
                                Log.e("No_Sys       ","Exception");

                            }
//                                fillSpennerSystem(systemsList);
//                                systemGridDialog(systemsList);
                                sendEngineerToAdapter();
                            }




                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((error instanceof NoConnectionError)) {
                    Toast.makeText(OnlineCenter.this,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                }

                Log.e("onErrorResponse: ", "" + error);
            }

        });
        MySingeltone.getmInstance(OnlineCenter.this).addToRequestQueue(stringRequest);


    }

//    private void fillSpennerSystem(List<Systems> systemsList) {
//        List<String> listNameSys = new ArrayList<>();
//        for (int j = 0; j < systemsList.size(); j++) {
//            listNameSys.add(systemsList.get(j).getSystemName());
//        }
//
//        spenner_systems.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listNameSys));
//
//    }

    private void initialView() {
        gridView = (GridView) findViewById(R.id.grid);
        recyclerView = findViewById(R.id.recycler);
        customer_name = findViewById(R.id.customer_name);
        companey_name = findViewById(R.id.companey_name);
        telephone_no = findViewById(R.id.telephone_no);
//        spenner_systems = findViewById(R.id.spenner_systems);
        engineerInfoList = new ArrayList<>();
        listEngforAdapter = new ArrayList<>();
        holdCompaney = new ArrayList<>();
        systemsList = new ArrayList<>();
        hold_List=new ArrayList<>();
        text_delet_id=findViewById(R.id.text_delet_id);
        text_finish=findViewById(R.id.text_finish);
        systype=findViewById(R.id.systype);
        textState=findViewById(R.id.textState);
        callCenterName=findViewById(R.id.callCenterName);
        LogInTime=findViewById(R.id.LogInTime);
        textState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(textState.getText().toString().equals("Success")){
                    presenterClass.setState(String.valueOf(EngId), 1);// checkin
                    Log.e("textStateEngid",""+EngId);
                    textState.setText("1");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        text_finish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(text_finish.getText().toString().equals("finish"))
                {
                    clearData();// after sucsess
                    deleteFromHoldList();
                    hold_List.clear();
                    fillHoldList();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void sendCompaneyInfo() throws JSONException {
        boolean isfaull = checkRequiredData();
        if (isfaull) {
            if (engineerInfoList.size() != 0) {
                if(!isInHold) {
                    JSONObject data = getData();
                    Log.e("data", "" + data);
                    ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                    managerImport.startSendingData(data, true);
                }else{
                    if(!text_delet_id.getText().toString().equals("")) {
                        CompaneyInfo companeyInfo = new CompaneyInfo();

                        String sys_Id = getSystemId(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getSystemName());

                        companeyInfo.setCustomerName(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getCustomerName());
                        companeyInfo.setCompanyName(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getCompanyName());
                        companeyInfo.setSystemName(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getSystemName());
                        companeyInfo.setPhoneNo(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getPhoneNo());
                        companeyInfo.setCheakInTime(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getCheakInTime());
                        companeyInfo.setState_company("1");
                        companeyInfo.setEngName(selectedEngineer);
                        companeyInfo.setEngId(String.valueOf(EngId));
                        companeyInfo.setSystemId(sys_Id);
                        companeyInfo.setChechout(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getCheakOutTime());
                        companeyInfo.setproblem(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getProplem());
                        companeyInfo.setSerial(hold_List.get(Integer.parseInt(text_delet_id.getText().toString())).getSerial());

                        Log.e("HOLDp", "UPDATE=-->" + companeyInfo.getSystemName().toString());
                        Log.e("HOLDp1", "UPDATE=-->" + companeyInfo.getSerial().toString());

                        JSONObject data = companeyInfo.getData();
                        Log.e("HOLDp", "" + data);
                        ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                        managerImport.startUpdateHold(data);

                    }
                    isInHold=false;


                }
            } else {
                new SweetAlertDialog(OnlineCenter.this,R.style.alert_dialog_dark)
                        .setTitleText("WARNING")
                        .setContentText("does not exist engineering  available  do you want to add  this customer to hold list ?")
                        .setConfirmText("Yes,add it")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @SuppressLint("WrongConstant")
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                ManagerLayout info = new ManagerLayout();
                                info.setCompanyName(companey_name.getText().toString());
                                info.setPhoneNo(telephone_no.getText().toString());
                                holdCompaney.add(info);
                                hold_List.add(info);
                                layoutManager = new LinearLayoutManager(OnlineCenter.this);
                                layoutManager.setOrientation(VERTICAL);
                                final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, hold_List);
                                recyclerView.setAdapter(companyAdapter);
                                sDialog.dismissWithAnimation();
                            }
                        }).setCancelText("No").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })
                        .show();
                Toast.makeText(this, "you can't check in Hold company", Toast.LENGTH_SHORT).show();
            }
        }

//        }

    }

    @SuppressLint("WrongConstant")
    private void deleteFromHoldList() {
        if(!text_delet_id.getText().toString().equals(""))
        {
            idForDelete=Integer.parseInt(text_delet_id.getText().toString());
            Log.e("idForDelete",""+idForDelete);
            hold_List.remove(idForDelete);
//            recyclerView.removeViewAt(idForDelete);
            LinearLayoutManager llm = new LinearLayoutManager(OnlineCenter.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, hold_List);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(companyAdapter);
            text_delet_id.setText("");


        }
    }

    public void clearData() {
        customer_name.setText("");
        customer_name.requestFocus();
        companey_name.setText("");
        telephone_no.setText("");
        systype.setText("");
        if(engineerInfoList.size()!=0)
        {
            engineerInfoList.remove(selectedEngId);
            engineerAdapter = new adapterGridEngineer(this, engineerInfoList);
            gridView.setAdapter(engineerAdapter);
            engineerAdapter.notifyDataSetChanged();
        }


    }

    private JSONObject getData() throws JSONException {
        String time = "", sys_name = "", sys_Id = "";
        String customerName = "", companeyName = "", tele = "";

        customerName = customer_name.getText().toString();
        companeyName = companey_name.getText().toString();
        tele = telephone_no.getText().toString();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        time = df.format(currentTimeAndDate);
//        sys_name = spenner_systems.getSelectedItem().toString();
//        if(spenner_systems.getCount()!=0){
//            sys_name = spenner_systems.getSelectedItem().toString();
//            Log.e("sys_name",""+sys_name);
//        }
        sys_name=systype.getText().toString();
        sys_Id = getSystemId(sys_name.replace(",",""));
        if (engineerInfoList.size() == 0) {
            stateCompaney = 0;// hold
        } else {
            stateCompaney = 1;
        }
        final String CallId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1");
        Log.e("call_id1",""+CallId+"    "+sys_Id);

        JSONObject obj = new JSONObject();
        if (engineerInfoList.size() != 0) {
            obj.put("CUST_NAME", "'" + customerName + "'");
            obj.put("COMPANY_NAME", "'" + companeyName + "'");
            obj.put("SYSTEM_NAME", "'" + sys_name + "'");
            obj.put("PHONE_NO", "'" + tele + "'");
            obj.put("CHECH_IN_TIME", "'" + time + "'");
            obj.put("STATE", "'" + stateCompaney + "'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
            obj.put("ENG_NAME", "'" + selectedEngineer + "'");
            obj.put("ENG_ID", "'" + EngId + "'");
            obj.put("SYS_ID", "'" + sys_Id + "'");
            obj.put("CHECH_OUT_TIME", "'00:00:00'");
            obj.put("PROBLEM", "'problem'");
            obj.put("CALL_CENTER_ID", "'"+CallId+"'");
            obj.put("HOLD_TIME", "'"+"00:00:00"+"'");
            obj.put("DATE_OF_TRANSACTION", "'00/00/00'");
            obj.put("SERIAL", "'"+"222"+"'");
        } else {
            // hold company data
            obj.put("CUST_NAME", "'" + customerName + "'");
            obj.put("COMPANY_NAME", "'" + companeyName + "'");
            obj.put("SYSTEM_NAME", "'" + sys_name + "'");
            obj.put("PHONE_NO", "'" + tele + "'");
            obj.put("CHECH_IN_TIME", "'" + time + "'");
            obj.put("STATE", "'" + stateCompaney + "'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
            obj.put("ENG_NAME", "''");
            obj.put("ENG_ID", "''");
            obj.put("SYS_ID", "'" + sys_Id + "'");
            obj.put("CHECH_OUT_TIME", "'00:00:00'");
            obj.put("PROBLEM", "'problem'");
            obj.put("CALL_CENTER_ID", "'"+CallId+"'");
        }
        return obj;


    }

    private String getSystemId(String name) {
        String sys_id = "";
        for (int i = 0; i < systemsList.size(); i++) {
            if (systemsList.get(i).getSystemName().equals(name)) {
                sys_id = systemsList.get(i).getSystemNo();
                Log.e("sys_id", "" + sys_id);
            }
        }


        return sys_id;
    }

    private boolean checkRequiredData() {
        String customerName = "", companeyName = "", tele = "",systemName="";
        customerName = customer_name.getText().toString();
        companeyName = companey_name.getText().toString();
        tele = telephone_no.getText().toString();
        systemName=systype.getText().toString();

        if ((!TextUtils.isEmpty(customerName)) && (!TextUtils.isEmpty(companeyName)) && (!TextUtils.isEmpty(tele)) &&  (!TextUtils.isEmpty(systemName))) {
            return true;

        } else {

            if (TextUtils.isEmpty(customerName)) {
                customer_name.setError("Required");
            } else if (TextUtils.isEmpty(companeyName)) {
                companey_name.setError("Required");
            } else if (TextUtils.isEmpty(tele)) {
                telephone_no.setError("Required");
            }
            else if (TextUtils.isEmpty(systemName)) {
                systype.setError("Required");
            }
            return false;
        }


    }

    @SuppressLint("WrongConstant")
    public void addToHoldList(View view) {
        if (view.getId() == R.id.btn_hold) {
            if (checkRequiredData()) {
                if (engineerInfoList.size() == 0) {
                    Date currentTimeAndDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                    String time = df.format(currentTimeAndDate);
                    Log.e("timeHold", "" + time);
                    LinearLayoutManager llm = new LinearLayoutManager(this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    ManagerLayout info = new ManagerLayout();
                    info.setCompanyName(companey_name.getText().toString());
                    info.setPhoneNo(telephone_no.getText().toString());
                    info.setCustomerName(customer_name.getText().toString());
                    info.setSystemName(systype.getText().toString());
                    info.setState("0");
                    info.setCheakInTime(time);
                    JSONObject data = null;
                    try {
                        data = getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("data", "" + data);
                    ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                    managerImport.startSendingData(data,false);

                    holdCompaney.add(info);
                    hold_List.add(info);
                    final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, hold_List);

                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(companyAdapter);
                    clearData();

                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("warning!!")
                            .setContentText("there is engineer available !!!")
//                            .hideConfirmButton()
                            .show();

                }

            }
        }
    }
}
