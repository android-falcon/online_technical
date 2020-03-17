package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import static android.widget.LinearLayout.VERTICAL;
import static com.falconssoft.onlinetechsupport.ManagerImport.sendSucsses;

public class OnlineCenter extends AppCompatActivity {
    GridView gridView;
    RecyclerView recyclerView;
    List<EngineerInfo> engineerInfoList, listEngforAdapter;
    List<String> chequeListitems;
    TextView customer_name, companey_name, telephone_no;
    Spinner spenner_systems;
    String ipAddres = "10.0.0.214";
    List<String> systemsList;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_center);
        initialView();
        engineerInfoList = new ArrayList<>();
        listEngforAdapter = new ArrayList<>();
        systemsList = new ArrayList<>();
        chequeListitems=new ArrayList<>();
        chequeListitems.add("Falcons");
        chequeListitems.add("Falcons");
        chequeListitems.add("Falcons");
        chequeListitems.add("Falcons");
        chequeListitems.add("Falcons");
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(OnlineCenter.this);
        layoutManager.setOrientation(VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new holdCompanyAdapter(OnlineCenter.this,chequeListitems));
        fillEngineerInfoList();

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
        adapterGridEngineer engineerAdapter = new adapterGridEngineer(this, listEngforAdapter);
        gridView.setAdapter(engineerAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.setBackgroundColor(getResources().getColor(R.color.layer4));
                String name=  ((TextView) view).getText().toString();
                Log.e("selectedPostionName",""+name);

            }
        } );



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
                            for (int i = 0; i < info.length(); i++) {
                                JSONObject engineerInfoObject = info.getJSONObject(i);
                                EngineerInfo engineerInfo = new EngineerInfo();
                                engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                                engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                                engineerInfo.setEng_type(Integer.parseInt(engineerInfoObject.getString("ENG_TYPE")));
                                engineerInfoList.add(engineerInfo);
                            }
                            JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
                            for (int i = 0; i < systemInfoArray.length(); i++) {
                                JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
                                Systems systemInfo = new Systems();
                                systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
                                systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));
                                systemsList.add(systemInfo.getSystemName());
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

    private void fillSpennerSystem(List<String> systemsList) {
        spenner_systems.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, systemsList));

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
            boolean isfaull =checkRequiredData();
            if(isfaull) {

                JSONObject data =getData();
                Log.e("data",""+data);
                ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                managerImport.startSendingData(data);
                clearData();

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
    }

    private JSONObject getData() throws JSONException {
        String time="";

        String customerName="",companeyName="",tele="";
        customerName=customer_name.getText().toString();
        companeyName=companey_name.getText().toString();
        tele=telephone_no.getText().toString();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        time = df.format(currentTimeAndDate);
        Log.e("time",""+time);


        JSONObject obj = new JSONObject();


            if(engineerInfoList.size()!=0)
            {
                obj.put("CUST_NAME", "'"+customerName+"'");
                obj.put("COMPANY_NAME", "'"+companeyName+"'");
                obj.put("SYSTEM_NAME", "'Accounting'");
                obj.put("PHONE_NO", "'"+tele+"'");
                obj.put("CHECH_IN_TIME", "'"+time+"'");
                obj.put("STATE", "'1'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
                obj.put("ENG_NAME", "'Tahani'");
                obj.put("ENG_ID", "'3'");
                obj.put("SYS_ID", "'2'");
                obj.put("CHECH_OUT_TIME", "'00:00:00'");
                obj.put("PROBLEM", "'problem'");
//               String engineer= selectedPostionName;
        }
        return  obj;


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
}
