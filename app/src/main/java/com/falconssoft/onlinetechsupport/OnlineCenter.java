package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbb20.CountryCodePicker;


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
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_TYPE;
import static com.falconssoft.onlinetechsupport.LoginActivity.sharedPreferences;
import static com.falconssoft.onlinetechsupport.MainActivity.hold;
import static com.falconssoft.onlinetechsupport.ManagerImport.countOfCall;
import static com.falconssoft.onlinetechsupport.ManagerImport.sendSucsses;

public class OnlineCenter extends AppCompatActivity {
    GridView gridView;
    public static RecyclerView recyclerView, recyclerViewCheckIn;
    public static List<EngineerInfo> engineerInfoList, listEngforAdapter, engInfoTra;
    List<ManagerLayout> holdCompaney;
    public static TextView customer_name, companey_name, telephone_no, text_delet_id, text_finish, textState, systype, mSpeakBtn, btnSpeakPhone, btnSpeekCompany;
    TextView callCenterName, LogInTime, deletaAllText, secandCall;
    //    Spinner spenner_systems;
    //    String ipAddres = "10.0.0.214";
    DatabaseHandler databaseHandler;

    String ipAddres = "";
    public static List<Systems> systemsList;
    LinearLayoutManager layoutManager;
    public static int stateCompaney = -1, selectedEngId = 0, EngId = -1;
    String holdReasonText = "";
    String selectedEngineer = "";
    adapterGridEngineer engineerAdapter;
    Timer timer;
    public static JSONObject updateHold;
    public static List<ManagerLayout> hold_List;
    int idForDelete = 0;
    private PresenterClass presenterClass;
    public static boolean isInHold = false;
    AlphaAnimation buttonClick;
    public static List<ManagerLayout> checkInList;
    List<String> spinnerPhoneList;
    ArrayAdapter<String> spinnerPhoneAdapter;
    TextView countOfCallWork;
    public static List<String> engStringName = new ArrayList<>();

    public static RecyclerView recyclerk;
    CountryCodePicker countryCodePicker;
    GClass gClass = new GClass(null, null, null);
    public static int isShow = 0;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int REQ_CODE_SPEECH_INPUT_Company = 200;
    private static final int REQ_CODE_SPEECH_INPUT_Phone = 300;
    RelativeLayout relative;
    TextView holdScheduale;
    int callType = 1;
    Button btn_sch, btn_hold;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private Date date;
    String dateOfTrance="00/00/00";
    public static String companyId="-1";

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_online_center);
        } catch (Exception e) {
            Log.e("setContentView", "" + e.getMessage());
        }


        initialView();
        engInfoTra = new ArrayList<>();

//        fillPhoneSpinner();

        checkInList = new ArrayList<>();
        checkInList.clear();
        systype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemGridDialog(systemsList);
            }
        });


        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        callCenterName.setText(sharedPreferences.getString(LOGIN_NAME, null));
        callType = sharedPreferences.getInt(LOGIN_TYPE, -1);
        Log.e("callType", "" + callType);

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy   hh:mm:ss");
        String time = df.format(currentTimeAndDate);
        LogInTime.setText("" + time);
        presenterClass = new PresenterClass(this);
        databaseHandler = new DatabaseHandler(OnlineCenter.this);
        ipAddres = databaseHandler.getIp();
        fillEngineerInfoList(0);

        btn_sch.setVisibility(View.GONE);
        btn_hold.setVisibility(View.GONE);
        if (callType == 1 ||callType == 5) {
            btn_hold.setVisibility(View.VISIBLE);
            btn_sch.setVisibility(View.GONE);
            holdScheduale.setText(getResources().getString(R.string.hold_customer_list));

        } else if (callType == 3 ) {
            btn_hold.setVisibility(View.GONE);
            btn_sch.setVisibility(View.VISIBLE);
            holdScheduale.setText(getResources().getString(R.string.sch_customer_list));
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                fillEngineerInfoList(1);

            }

        }, 0, 4000);


        callCountCalling();


        fillHoldList();
        //fillListTest();
        //fillSpennerSystem(systemsList);

        FillCheckIn();

        countOfCallWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callCountCalling();

            }
        });


        secandCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isfaull = checkRequiredData();
                if (isfaull) {
                    dialogEngineering(OnlineCenter.this);
                }

            }
        });


//        spinnerPhoneSS.setVisibility(View.GONE);

        gClass.fillSearchCustomerPhoneNo(recyclerk, "", "", "", OnlineCenter.this, (EditText) telephone_no);

        telephone_no.addTextChangedListener(textWatcher);
        companey_name.addTextChangedListener(textWatcher);
        customer_name.addTextChangedListener(textWatcher);
        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerk.setVisibility(View.GONE);
                isShow = 0;
            }
        });

    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                spinnerPhoneSS.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!TextUtils.isEmpty(s.toString())) {
                if (isShow == 0) {
                    recyclerk.setVisibility(View.VISIBLE);
                    isShow = 1;//for search dialog is open or not
                } else if (isShow == 1) {
                    recyclerk.setVisibility(View.GONE);
                    isShow = 1;
                }


                gClass.fillSearchCustomerPhoneNo(recyclerk, telephone_no.getText().toString(), customer_name.getText().toString(), companey_name.getText().toString(), OnlineCenter.this, (EditText) telephone_no);
            } else {
                recyclerk.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    customer_name.setText(result.get(0));
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_Company: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    companey_name.setText(result.get(0));
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_Phone: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    telephone_no.setText(result.get(0));
                }
                break;
            }


        }
    }

    private void dialogEngineering(final Context context1) {

        final Dialog dialog = new Dialog(context1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.engeneering_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        ArrayList<String> nameOfEngi = new ArrayList<>();
        final ListView listEngennering = dialog.findViewById(R.id.listViewEngineering);

        final int[] rowEng = new int[1];
        final String[] selectedId = new String[1];
        if (engInfoTra.size() != 0) {
            for (int i = 0; i < engInfoTra.size(); i++) {
//                nameOfEngi.add("tahani");
                nameOfEngi.add(engInfoTra.get(i).getName());
            }
            Log.e("nameOfEngi", "" + nameOfEngi.size());

//                    simple_list_item_1 simple_list_item_activated_1
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(OnlineCenter.this, android.R.layout.simple_list_item_1, nameOfEngi);
            listEngennering.setAdapter(itemsAdapter);
        }
        listEngennering.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rowEng[0] = position;
                listEngennering.requestFocusFromTouch();
                listEngennering.setSelection(position);
                selectedId[0] = engInfoTra.get(position).getId();
                EngId = Integer.parseInt(selectedId[0]);
                selectedEngineer = engInfoTra.get(position).getName();

//               Log.e( "getSelectedItem",""+listEngennering.getSelectedItem().toString());
                Log.e("getItemsCanFocus", "" + listEngennering.getItemsCanFocus());
                Log.e("position", "" + position + "\t" + selectedId[0]);

            }
        });


        Button btn_send = dialog.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rowEng[0] == -1) {
                    Toast.makeText(OnlineCenter.this, "Select Engineer", Toast.LENGTH_SHORT).show();

                } else {
                    JSONObject data = null;
                    try {
                        data = getData("0", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("data", "" + data);
                    ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                    managerImport.startSendingData(data, true, 0, null, null);

                    dialog.dismiss();


                }

            }
        });
        dialog.show();

    }

    void callCountCalling() {

        countOfCall = countOfCallWork;

        ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
        managerImport.startSending("CountCallWork");


    }

//    void fillPhoneSpinner() {
//        spinnerPhoneList = new ArrayList<>();
//        spinnerPhoneList.clear();
//        spinnerPhoneList.add("06");
//        spinnerPhoneList.add("078");
//        spinnerPhoneList.add("079");
//        spinnerPhoneList.add("077");
//        spinnerPhoneList.add("+966");
//        spinnerPhoneList.add("+964");
//
//        spinnerPhoneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerPhoneList);
//        spinnerPhoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerPhone.setAdapter(spinnerPhoneAdapter);
//    }

    void FillCheckIn() {

        ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
        managerImport.startSending("GetCheckInList");

    }

    @SuppressLint("WrongConstant")
    public void fillHoldList() {

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
                EngId = Integer.parseInt(engineerInfoList.get(arg2).getId());
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
        if (engineerInfoList.size() != 0) {
            listEngforAdapter.clear();
            for (int i = 0; i < engineerInfoList.size(); i++) {
                engType = Integer.parseInt(String.valueOf(engineerInfoList.get(i).getEng_type()));

                if (callType == 1) {
                    if (engType == 2)// available  Engeneering
                    {
                        listEngforAdapter.add(engineerInfoList.get(i));

                    }
                } else if (callType == 3) {
                    if (engType == 4)// available  tec
                    {
                        listEngforAdapter.add(engineerInfoList.get(i));

                    }
                } else if (callType == 5) {
                    if (engType == 6)// available  tec
                    {
                        listEngforAdapter.add(engineerInfoList.get(i));

                    }
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
                    EngId = Integer.parseInt(engineerInfoList.get(arg2).getId());

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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        if (gridView.isItemChecked(arg2)) {
                            //                    arg1= gridView.getChildAt(arg2);
                            //                    arg1.setBackgroundColor(getResources().getColor(R.color.layer4));
                        } else {
                            //
                            arg1.setBackgroundColor(getResources().getColor(R.color.layer3)); //the color code is the background color of GridView
                        }
                    }
                }
            });
        } else {
            engineerAdapter = new adapterGridEngineer(this, engineerInfoList);
            gridView.setAdapter(engineerAdapter);
        }
    }

    public void systemGridDialog(final List<Systems> listOfsystem) {

        final Dialog fillSysDialog = new Dialog(OnlineCenter.this, R.style.Theme_Dialog);
        fillSysDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fillSysDialog.setCancelable(true);
        fillSysDialog.setContentView(R.layout.sys_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        final GridView SysGrid;

        SysGrid = fillSysDialog.findViewById(R.id.Sysgrid);
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

    public void fillEngineerInfoList(final int flag) {
//        if(TextUtils.isEmpty(ipAddres)){
//            Toast.makeText(this, "ip Not Found,Please Add Ip", Toast.LENGTH_SHORT).show();
//        }
        databaseHandler = new DatabaseHandler(OnlineCenter.this);
        ipAddres = databaseHandler.getIp();
        final String url = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (flag != 0)// 0 ---->  just first time
                            {
                                engineerInfoList.clear();

                                JSONArray info = jsonObject.getJSONArray("ENGINEER_INFO");
                                Log.e("info", "" + info);
                                engInfoTra.clear();
                                engStringName.clear();
                                for (int i = 0; i < info.length(); i++) {
                                    JSONObject engineerInfoObject = info.getJSONObject(i);
                                    EngineerInfo engineerInfo = new EngineerInfo();
                                    engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                                    engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                                    engineerInfo.setEng_type(engineerInfoObject.getInt("ENG_TYPE"));
                                    engineerInfo.setState(engineerInfoObject.getInt("STATE"));

                                    Log.e("ENG_TYPE", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                    if (callType == 1) {
                                        if (engineerInfo.getEng_type() == 2) {
                                            engInfoTra.add(engineerInfo);
                                            engStringName.add(engineerInfo.getName());

                                        }

                                        if (engineerInfo.getEng_type() == 2 && engineerInfo.getState() == 0) {
                                            engineerInfoList.add(engineerInfo);
                                            Log.e("ENG_TYPE_in", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                        }

                                    } else if (callType == 3) {


                                        if (engineerInfo.getEng_type() == 4) {
                                            engInfoTra.add(engineerInfo);
                                            engStringName.add(engineerInfo.getName());

                                        }

                                        if (engineerInfo.getEng_type() == 4 && engineerInfo.getState() == 0) {
                                            engineerInfoList.add(engineerInfo);
                                            Log.e("ENG_TYPE_in", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                        }

                                    }else if (callType == 5) {


                                        if (engineerInfo.getEng_type() == 6) {
                                            engInfoTra.add(engineerInfo);
                                            engStringName.add(engineerInfo.getName());

                                        }

                                        if (engineerInfo.getEng_type() == 6 && engineerInfo.getState() == 0) {
                                            engineerInfoList.add(engineerInfo);
                                            Log.e("ENG_TYPE_in", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                        }

                                    }

                                }
                                sendEngineerToAdapter();

                            } else {
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
                                        if (callType == 1) {
                                            if (engineerInfo.getEng_type() == 2 && engineerInfo.getState() == 0) {
                                                engineerInfoList.add(engineerInfo);

                                            }

                                        } else if (callType == 3) {
                                            if (engineerInfo.getEng_type() == 4 && engineerInfo.getState() == 0) {
                                                engineerInfoList.add(engineerInfo);

                                            }
                                        }else if (callType == 5) {
                                            if (engineerInfo.getEng_type() == 6 && engineerInfo.getState() == 0) {
                                                engineerInfoList.add(engineerInfo);

                                            }
                                        }
//                                        if (engineerInfo.getEng_type() == 2 && engineerInfo.getState() != 0 ) {
//                                            engineerNotAvil.add(engineerInfo);
//                                        }

                                    }
                                } catch (Exception e) {
                                    Log.e("No_Eng", "Exception");

                                }
                                try {
                                    systemsList.clear();
                                    systemsList.add(new Systems("system Not Known", "-1"));
                                    JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
                                    Log.e("systemInfoArray", "" + systemInfoArray);
                                    for (int i = 0; i < systemInfoArray.length(); i++) {
                                        JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
                                        Systems systemInfo = new Systems();
                                        systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
                                        systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));
                                        systemsList.add(systemInfo);
                                    }
                                } catch (Exception e) {
                                    Log.e("No_Sys       ", "Exception");

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
        buttonClick = new AlphaAnimation(1F, 0.1F);
        gridView = (GridView) findViewById(R.id.grid);
        recyclerView = findViewById(R.id.recycler);
        recyclerViewCheckIn = findViewById(R.id.recyclerChechIn);
        customer_name = findViewById(R.id.customer_name);
        companey_name = findViewById(R.id.companey_name);
        telephone_no = findViewById(R.id.telephone_no);
//        spenner_systems = findViewById(R.id.spenner_systems);
        countryCodePicker = findViewById(R.id.spinnerPhone);
        engineerInfoList = new ArrayList<>();
//        engineerNotAvil= new ArrayList<>();
        listEngforAdapter = new ArrayList<>();
        secandCall = findViewById(R.id.secandCall);
        holdCompaney = new ArrayList<>();
        systemsList = new ArrayList<>();
        hold_List = new ArrayList<>();
        text_delet_id = findViewById(R.id.text_delet_id);
        text_finish = findViewById(R.id.text_finish);
        systype = findViewById(R.id.systype);
        textState = findViewById(R.id.textState);
        callCenterName = findViewById(R.id.callCenterName);
        relative = findViewById(R.id.relative);
        deletaAllText = findViewById(R.id.deletaAllText);
        deletaAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                clearText();
            }
        });

        countryCodePicker.setVisibility(View.GONE);
        recyclerk = findViewById(R.id.recyclerk);
        recyclerk.setVisibility(View.GONE);
        countOfCallWork = findViewById(R.id.countOfCallWork);
        LogInTime = findViewById(R.id.LogInTime);
        mSpeakBtn = findViewById(R.id.btnSpeak);
        btnSpeakPhone = findViewById(R.id.btnSpeakPhone);
        btnSpeekCompany = findViewById(R.id.btnSpeakCompany);
        holdScheduale = findViewById(R.id.holdScheaduale);
        btn_sch = findViewById(R.id.btn_sch);
        btn_hold = findViewById(R.id.btn_hold);

        textState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (textState.getText().toString().equals("Success")) {
                    presenterClass.setState(String.valueOf(EngId), 1);// checkin
                    Log.e("textStateEngid", "" + EngId);
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
                if (text_finish.getText().toString().equals("finish")) {
                    clearData();// after sucsess
                    deleteFromHoldList();
                    hold_List.clear();
                    FillCheckIn();
                    fillHoldList();

                    callCountCalling();

                } else if (text_finish.getText().toString().equals("AddFinish")) {
                    deleteFromHoldList();
                    hold_List.clear();
                    FillCheckIn();
                    fillHoldList();
                    callCountCalling();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput(1);
            }
        });
        btnSpeakPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput(3);
            }
        });
        btnSpeekCompany.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput(2);
            }
        });
    }

    private void startVoiceInput(int flag) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            if (flag == 1) {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
            } else if (flag == 2) {

                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_Company);
            } else if (flag == 3) {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_Phone);
            }

        } catch (ActivityNotFoundException a) {

        }
    }

    public void sendCompaneyInfo() throws JSONException {
        boolean isfaull = checkRequiredData();
        if (isfaull) {
            if (engineerInfoList.size() != 0) {
                if (!isInHold) {
                    JSONObject data = getData("0", 1);
                    Log.e("data", "" + data);
                    ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                    managerImport.startSendingData(data, true, 0, null, null);
                } else {
                    if (!text_delet_id.getText().toString().equals("")) {
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
                    isInHold = false;


                }
            } else {
                new SweetAlertDialog(OnlineCenter.this)
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
        if (!text_delet_id.getText().toString().equals("")) {
            idForDelete = Integer.parseInt(text_delet_id.getText().toString());
            Log.e("idForDelete", "" + idForDelete);
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
        clearText();

        try {
            if (engineerInfoList.size() != 0) {
                engineerInfoList.remove(selectedEngId);
                engineerAdapter = new adapterGridEngineer(this, engineerInfoList);
                gridView.setAdapter(engineerAdapter);
                engineerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("error ", "clearData " + e);
        }


    }

    private void clearText() {
        customer_name.setText("");
        customer_name.requestFocus();
        companey_name.setText("");
        telephone_no.setText("");
        systype.setText("");
        companyId="-1";
        companey_name.setEnabled(true);
        telephone_no.setEnabled(true);
    }

    private JSONObject getData(String transferFlag, int isHold) throws JSONException {
//        isHold ======> for hold work without eng list
        String time = "", sys_name = "", sys_Id = "";
        String customerName = "", companeyName = "", tele = "";

        customerName = customer_name.getText().toString().replace("'", "");
        companeyName = companey_name.getText().toString().replace("'", "");

        String phoneFirst = "";

        try {
//            phoneFirst = spinnerPhone.getSelectedItem().toString();
            phoneFirst = countryCodePicker.getSelectedCountryCode().toString();
        } catch (Exception e) {
            phoneFirst = "06";
        }

        tele = telephone_no.getText().toString();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        time = df.format(currentTimeAndDate);
//        sys_name = spenner_systems.getSelectedItem().toString();
//        if(spenner_systems.getCount()!=0){
//            sys_name = spenner_systems.getSelectedItem().toString();
//            Log.e("sys_name",""+sys_name);
//        }
        sys_name = systype.getText().toString();
        sys_Id = getSystemId(sys_name.replace(",", ""));
//        if (engineerInfoList.size() == 0) {
//            stateCompaney = 0;// hold
//        } else {
//            stateCompaney = 1;
//        }

        if (isHold == 0) {
            stateCompaney = 0;// hold
        } else if (isHold == 1) {
            stateCompaney = 1;
        } else if (isHold == 3) {//schaduel
            stateCompaney = 3;
        }


        final String CallId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1");
        final String CallName = LoginActivity.sharedPreferences.getString(LOGIN_NAME, "-1");
        Log.e("call_id1", "" + CallId + "    " + sys_Id + "    " + CallName);

        JSONObject obj = new JSONObject();

//engineerInfoList.size() != 0 ||
        if (isHold == 1) {
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
            obj.put("CALL_CENTER_ID", "'" + CallId + "'");
            obj.put("HOLD_TIME", "'" + "00:00:00" + "'");
            obj.put("DATE_OF_TRANSACTION", "'"+dateOfTrance+"'");
            obj.put("SERIAL", "'" + "222" + "'");
            obj.put("CALL_CENTER_NAME", "'" + CallName + "'");
            obj.put("TRANSFER_FLAG", "'" + transferFlag + "'");
            obj.put("ORGINAL_SERIAL", "'-2'");
            obj.put("HOLD_REASON", "''");
            if (callType == 1) {
                obj.put("TEC_TYPE", "'" + 2 + "'");
            } else if (callType == 3) {
                obj.put("TEC_TYPE", "'" + 4 + "'");
            }else if (callType == 5) {
                obj.put("TEC_TYPE", "'" + 6 + "'");
            }

            obj.put("COMPANY_ID","'"+companyId+"'");
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
            obj.put("CALL_CENTER_ID", "'" + CallId + "'");
            obj.put("HOLD_TIME", "'" + "00:00:00" + "'");
            obj.put("DATE_OF_TRANSACTION", "'"+dateOfTrance+"'");
            obj.put("SERIAL", "'" + "222" + "'");
            obj.put("CALL_CENTER_NAME", "'" + CallName + "'");
            obj.put("TRANSFER_FLAG", "'" + transferFlag + "'");
            obj.put("ORGINAL_SERIAL", "'-2'");
            obj.put("HOLD_REASON", "'" + holdReasonText + "'");
            if (callType == 1) {
                obj.put("TEC_TYPE", "'" + 2 + "'");
            } else if (callType == 3) {
                obj.put("TEC_TYPE", "'" + 4 + "'");
            }else if (callType == 5) {
                obj.put("TEC_TYPE", "'" + 6 + "'");
            }

            obj.put("COMPANY_ID","'"+companyId+"'");
            Log.e("holdReasonText", "reason " + holdReasonText);
        }
        return obj;


    }

    public String getSystemId(String name) {
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
        String customerName = "", companeyName = "", tele = "", systemName = "";
        customerName = customer_name.getText().toString();
        companeyName = companey_name.getText().toString();
        tele = telephone_no.getText().toString();
        systemName = systype.getText().toString();

        if ((!TextUtils.isEmpty(customerName)) && (!TextUtils.isEmpty(companeyName)) && (!TextUtils.isEmpty(tele)) && (!TextUtils.isEmpty(systemName))) {
            return true;

        } else {

            if (TextUtils.isEmpty(customerName)) {
                customer_name.setError("Required");
            } else if (TextUtils.isEmpty(companeyName)) {
                companey_name.setError("Required");
            } else if (TextUtils.isEmpty(tele)) {
                telephone_no.setError("Required");
            } else if (TextUtils.isEmpty(systemName)) {
                systype.setError("Required");
            }
            return false;
        }


    }

    @SuppressLint("WrongConstant")
    public void addToHoldList(View view) {
        if (view.getId() == R.id.btn_hold) {
            view.startAnimation(buttonClick);
            if (checkRequiredData()) {
//                if (engineerInfoList.size() == 0) {

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("warning!!")
                        .setContentText("Add To Hold List !!!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                holdListDialog();
                                sweetAlertDialog.dismissWithAnimation();


                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismissWithAnimation();

                            }
                        })
                        .show();


//                } else {
//                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("warning!!")
//                            .setContentText("there is engineer available !!!")
////                            .hideConfirmButton()
//                            .show();
//
//                }

            }
        } else if (view.getId() == R.id.btn_sch) {
            view.startAnimation(buttonClick);
            if (checkRequiredData()) {
//                if (engineerInfoList.size() == 0) {

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("warning!!")
                        .setContentText("Add To SchSchedule List !!!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                schaduelListDialog();
                                sweetAlertDialog.dismissWithAnimation();


                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismissWithAnimation();

                            }
                        })
                        .show();


//                } else {
//                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("warning!!")
//                            .setContentText("there is engineer available !!!")
////                            .hideConfirmButton()
//                            .show();
//
//                }

            }
        }
    }

    @SuppressLint("WrongConstant")
    void holdListDialog() {

        final Dialog holdDialog = new Dialog(OnlineCenter.this, R.style.Theme_Dialog);
        holdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        holdDialog.setCancelable(false);
        holdDialog.setContentView(R.layout.hold_dialog);
        holdDialog.setCanceledOnTouchOutside(false);

        FloatingActionButton addList = holdDialog.findViewById(R.id.hold_reason);
        final EditText holdReason = holdDialog.findViewById(R.id.holdEdit_reason);


        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(holdReason.getText().toString())) {
                    Date currentTimeAndDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                    String time = df.format(currentTimeAndDate);
                    Log.e("timeHold", "" + time);
                    LinearLayoutManager llm = new LinearLayoutManager(OnlineCenter.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    ManagerLayout info = new ManagerLayout();
                    info.setCompanyName(companey_name.getText().toString());
                    info.setPhoneNo(telephone_no.getText().toString());
                    info.setCustomerName(customer_name.getText().toString());
                    info.setSystemName(systype.getText().toString());
                    info.setHoldReason(holdReason.getText().toString());
                    holdReasonText = holdReason.getText().toString();
                    info.setState("0");
                    info.setCheakInTime(time);
                    JSONObject data = null;
                    try {
                        data = getData("0", 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("data", "" + data);
                    ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                    managerImport.startSendingData(data, false, 0, null, null);

                    holdCompaney.add(info);
//                    hold_List.add(info);
//                    final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, hold_List);
//
//                    recyclerView.setLayoutManager(llm);
//                    recyclerView.setAdapter(companyAdapter);
                    clearData();
                    holdDialog.dismiss();
                } else {

                    Toast.makeText(OnlineCenter.this, "Please add Reason ", Toast.LENGTH_SHORT).show();

                }

            }
        });


        holdDialog.show();


    }


    void schaduelListDialog() {

        final Dialog holdDialog = new Dialog(OnlineCenter.this, R.style.Theme_Dialog);
        holdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        holdDialog.setCancelable(false);
        holdDialog.setContentView(R.layout.schadu_dialog);
        holdDialog.setCanceledOnTouchOutside(false);

        FloatingActionButton addList = holdDialog.findViewById(R.id.schad_reason);
        final EditText holdReason = holdDialog.findViewById(R.id.holdEdit_reason);
        final TextView dateSch = holdDialog.findViewById(R.id.dateSch);
        dateSch.setText(""+gClass.DateInToday());
        dateSch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(OnlineCenter.this, gClass.openDatePickerDialog(0,dateSch,dateSch,0), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addList.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(holdReason.getText().toString())) {
                    Date currentTimeAndDate = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm");
                    String time = df.format(currentTimeAndDate);
                    Log.e("timeHold", "" + time);
                    LinearLayoutManager llm = new LinearLayoutManager(OnlineCenter.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    ManagerLayout info = new ManagerLayout();
                    info.setCompanyName(companey_name.getText().toString());
                    info.setPhoneNo(telephone_no.getText().toString());
                    info.setCustomerName(customer_name.getText().toString());
                    info.setSystemName(systype.getText().toString());
                    dateOfTrance=dateSch.getText().toString();
                    info.setTransactionDate(dateSch.getText().toString());
                    info.setHoldReason(holdReason.getText().toString());
                    holdReasonText = holdReason.getText().toString();
                    info.setState("0");
                    info.setCheakInTime(time);
                    JSONObject data = null;
                    try {
                        data = getData("0", 3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("data", "" + data);
                    ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
                    managerImport.startSendingData(data, false, 0, null, null);

                    holdCompaney.add(info);
//                    hold_List.add(info);
//                    final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(OnlineCenter.this, hold_List);
//
//                    recyclerView.setLayoutManager(llm);
//                    recyclerView.setAdapter(companyAdapter);
                    clearData();
                    holdDialog.dismiss();
                } else {

                    Toast.makeText(OnlineCenter.this, "Please add Reason ", Toast.LENGTH_SHORT).show();

                }

            }
        });


        holdDialog.show();


    }


    @Override
    public void onBackPressed() {
        new SweetAlertDialog(OnlineCenter.this)
                .setTitleText(getResources().getString(R.string.warning))
                .setContentText(getResources().getString(R.string.areyouSureExit))
                .setConfirmText(getResources().getString(R.string.dialog_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        timer.cancel();
                        finish();
                        sDialog.dismissWithAnimation();
                    }
                }).setCancelText(getResources().getString(R.string.no)).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();

            }
        })
                .show();

    }
}
