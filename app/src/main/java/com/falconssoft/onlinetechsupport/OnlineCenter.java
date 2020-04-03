package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.widget.LinearLayout.VERTICAL;
import static com.falconssoft.onlinetechsupport.ManagerImport.sendSucsses;

public class OnlineCenter extends AppCompatActivity {
    GridView gridView;
    RecyclerView recyclerView;
    List<EngineerInfo> engineerInfoList, listEngforAdapter;
    List<CompaneyInfo> holdCompaney;
    TextView customer_name, companey_name, telephone_no;
    Spinner spenner_systems;
//    String ipAddres = "10.0.0.214";
    String ipAddres = "127.0.0.1";
//    List<String> systemsList;
    List<Systems> systemsList;
     LinearLayoutManager layoutManager;
     int stateCompaney=-1,selectedEngId=0;
     String selectedEngineer="";
    adapterGridEngineer engineerAdapter;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_center);
        initialView();
        engineerInfoList = new ArrayList<>();
        listEngforAdapter = new ArrayList<>();
        holdCompaney=new ArrayList<>();
        systemsList = new ArrayList<>();
        systemsList.add(new Systems("Falcons1","1"));
        systemsList.add(new Systems("Falcons2","2"));
        systemsList.add(new Systems("Falcons3","3"));
        systemsList.add(new Systems("Falcons4","4"));
        systemsList.add(new Systems("Falcon5","5"));

        fillSpennerSystem(systemsList);

        //fillEngineerInfoList();
        fillHoldList();

        fillListTest();


    }

    @SuppressLint("WrongConstant")
    private void fillHoldList() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        CompaneyInfo info=new CompaneyInfo();
        info.setCompanyName("aljunidi company");
        info.setPhoneNo("079731999999");
        info.setState_company("0");
        CompaneyInfo info2=new CompaneyInfo();
        info2.setCompanyName("ejabi company");
        info2.setPhoneNo("07973190000");
        info2.setState_company("0");
        holdCompaney.add(info);
        holdCompaney.add(info2);
        holdCompaney.add(info);
        holdCompaney.add(info2);


        final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, holdCompaney);

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(companyAdapter);
    }

    @SuppressLint("WrongConstant")
    private void fillListTest() {
        EngineerInfo engineerInfo = new EngineerInfo();

        engineerInfo.setName("tahani");
        engineerInfo.setId("1");
        EngineerInfo enginee2= new EngineerInfo();
        enginee2.setName("tahoneh");


        engineerInfoList.add(engineerInfo);
        engineerInfoList.add(enginee2);
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
                engineerAdapter.notifyDataSetChanged();
//                gridView.setBackgroundColor(getResources().getColor(R.color.layer4));
                selectedEngineer=engineerInfoList.get(arg2).getName();
                selectedEngId=arg2;
              //  engineerInfoList.remove(arg2);
                engineerAdapter.notifyDataSetChanged();

                if(gridView.isItemChecked(arg2)) {
                    arg1= gridView.getChildAt(arg2);
                    arg1.setBackgroundColor(getResources().getColor(R.color.layer4));
                }else {
                    arg1 = gridView.getChildAt(arg2);
                    arg1.setBackgroundColor(getResources().getColor(R.color.layer3)); //the color code is the background color of GridView
                }
            }
        });


    }

    @SuppressLint("WrongConstant")
    private void sendEngineerToAdapter() {
        int engType = 0;
        for (int i = 0; i < engineerInfoList.size(); i++) {
            engType = Integer.parseInt(String.valueOf(engineerInfoList.get(i).getEng_type()));

            if (engType == 0)// online Engeneering
            {
                listEngforAdapter.add(engineerInfoList.get(i));

            }
        }

        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(OnlineCenter.this);
        layoutManager.setOrientation(VERTICAL);
        engineerAdapter = new adapterGridEngineer(this, listEngforAdapter);
        gridView.setAdapter(engineerAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                gridView.setBackgroundColor(getResources().getColor(R.color.layer4));
//                String name=  ((TextView) view).getText().toString();
//                Log.e("selectedPostionName",""+name);
//
//            }
//        } );



    }

    private void fillEngineerInfoList() {
        // http://10.0.0.214/onlineTechnicalSupport/import.php?FLAG=0

        final String url = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            JSONArray info = jsonObject.getJSONArray("ENGINEER_INFO");
                            Log.e("info",""+info);
                            for (int i = 0; i < info.length(); i++) {
                                JSONObject engineerInfoObject = info.getJSONObject(i);
                                EngineerInfo engineerInfo = new EngineerInfo();
                                engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                                engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                                engineerInfo.setEng_type(Integer.parseInt(engineerInfoObject.getString("ENG_TYPE")));
                                engineerInfoList.add(engineerInfo);
                            }
                            JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
                            Log.e("systemInfoArray",""+systemInfoArray);
                            for (int i = 0; i < systemInfoArray.length(); i++) {
                                JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
                                Systems systemInfo = new Systems();
                                systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
                                systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));
                                systemsList.add(systemInfo);
                            }
                            fillSpennerSystem(systemsList);
                            sendEngineerToAdapter();

                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("onErrorResponse: ", "" + error);
            }

        });
        MySingeltone.getmInstance(OnlineCenter.this).addToRequestQueue(stringRequest);


    }

    private void fillSpennerSystem(List<Systems> systemsList) {
        List<String> listNameSys=new ArrayList<>();
        for(int j=0;j<systemsList.size();j++)
        {
            listNameSys.add(systemsList.get(j).getSystemName());
        }

        spenner_systems.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listNameSys));

    }

    private void initialView() {
        gridView = (GridView) findViewById(R.id.grid);
        recyclerView = findViewById(R.id.recycler);
        customer_name = findViewById(R.id.customer_name);
        companey_name = findViewById(R.id.companey_name);
        telephone_no = findViewById(R.id.telephone_no);
        spenner_systems = findViewById(R.id.spenner_systems);


    }

    public void sendCompaneyInfo(View view) throws JSONException {
        if (view.getId() == R.id.checkIn_btn) {
            //check in if list of engineer not empty


            boolean isfaull =checkRequiredData();
            if(isfaull) {
                if(engineerInfoList.size()!=0)
                {
                    JSONObject data =getData();
                    Log.e("data",""+data);
                    ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                    managerImport.startSendingData(data);
                    clearData();
                }
                else{
                    new SweetAlertDialog(OnlineCenter.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("WARNING")
                            .setContentText("does not exist engineering  available  do you want to add  this customer to hold list ?")
                            .setConfirmText("Yes,add it")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    CompaneyInfo info=new CompaneyInfo();
                                    info.setCompanyName(companey_name.getText().toString());
                                    info.setPhoneNo(telephone_no.getText().toString());
                                    holdCompaney.add(info);
                                    layoutManager = new LinearLayoutManager(OnlineCenter.this);
                                    layoutManager.setOrientation(VERTICAL);
                                    final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, holdCompaney);
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
                    Toast.makeText(this, "you can't check in Hold companey", Toast.LENGTH_SHORT).show();
                }



                if(sendSucsses)
                {
                    Log.e("sendSucsses",""+sendSucsses);
                }
            }

        }

    }

    private void clearData() {
       customer_name.setText("");
        companey_name.setText("");
        telephone_no.setText("");
        engineerInfoList.remove(selectedEngId);
        engineerAdapter = new adapterGridEngineer(this, engineerInfoList);
        gridView.setAdapter(engineerAdapter);
        engineerAdapter.notifyDataSetChanged();;
    }

    private JSONObject getData() throws JSONException {
        String time="",sys_name="",sys_Id="";



        String customerName="",companeyName="",tele="";
        customerName=customer_name.getText().toString();
        companeyName=companey_name.getText().toString();
        tele=telephone_no.getText().toString();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        time = df.format(currentTimeAndDate);
        Log.e("time",""+time);
        sys_name= spenner_systems.getSelectedItem().toString();
        sys_Id= getSystemId(sys_name);
        if(engineerInfoList.size()==0)
        {
            stateCompaney=0;// hold
        }
        else {
            stateCompaney=1;
        }




        JSONObject obj = new JSONObject();


            if(engineerInfoList.size()!=0)
            {
                obj.put("CUST_NAME", "'"+customerName+"'");
                obj.put("COMPANY_NAME", "'"+companeyName+"'");
                obj.put("SYSTEM_NAME", "'"+sys_name+"'");
                obj.put("PHONE_NO", "'"+tele+"'");
                obj.put("CHECH_IN_TIME", "'"+time+"'");
                obj.put("STATE", "'"+stateCompaney+"'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
                obj.put("ENG_NAME", "'"+selectedEngineer+"'");
                obj.put("ENG_ID", "'"+selectedEngId+"'");
                obj.put("SYS_ID", "'"+sys_Id+"'");
                obj.put("CHECH_OUT_TIME", "'00:00:00'");
                obj.put("PROBLEM", "'problem'");
//               String engineer= selectedPostionName;
        }
        return  obj;


    }

    private String getSystemId(String name) {
        String sys_id="";
        for(int i=0;i<systemsList.size();i++)
        {
          if(systemsList.get(i).getSystemName().equals(name))
          {
              sys_id=systemsList.get(i).getSystemNo();
              Log.e("sys_id",""+sys_id);
          }
        }


        return sys_id;
    }

    private boolean checkRequiredData() {
        String customerName="",companeyName="",tele="";
        customerName=customer_name.getText().toString();
        companeyName=companey_name.getText().toString();
        tele=telephone_no.getText().toString();

        if ((!TextUtils.isEmpty(customerName)) && (!TextUtils.isEmpty(companeyName))&& (!TextUtils.isEmpty(tele)))
        {
            return  true;

        }
        else {

            if(TextUtils.isEmpty(customerName))
            {
                customer_name.setError("Required");
            }
            else
            if(TextUtils.isEmpty(companeyName))
            {
                companey_name.setError("Required");
            }
            else
            if(TextUtils.isEmpty(tele))
            {
                telephone_no.setError("Required");
            }
            return false;
        }


    }

    @SuppressLint("WrongConstant")
    public void addToHoldList(View view) {
        if(view.getId()==R.id.btn_hold)
        {
            if(checkRequiredData())
            {
                if(engineerInfoList.size()==0)
                {
                    LinearLayoutManager llm = new LinearLayoutManager(this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    CompaneyInfo info=new CompaneyInfo();
                    info.setCompanyName(companey_name.getText().toString());
                    info.setPhoneNo(telephone_no.getText().toString());
                    info.setState_company("0");
                    holdCompaney.add(info);

                    final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, holdCompaney);

                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(companyAdapter);

                }
                else {
                    new AlertDialog.Builder(OnlineCenter.this)
                            .setTitle("warning")
                            .setMessage("there is engineer available !!!")
//
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // Continue with delete operation
//                                }
//                            })
//
//                            // A null listener allows the button to dismiss the dialog and take no further action.
//                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    // alert dialog there is engineer available

                }

            }
        }
    }
}
