package com.falconssoft.onlinetechsupport.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.falconssoft.onlinetechsupport.GClass;
import com.falconssoft.onlinetechsupport.ManagerImport;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.falconssoft.onlinetechsupport.PresenterClass;
import com.falconssoft.onlinetechsupport.PresenterInterface;
import com.falconssoft.onlinetechsupport.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.falconssoft.onlinetechsupport.GClass.engList;
import static com.falconssoft.onlinetechsupport.GClass.engineerInfoList;
import static com.falconssoft.onlinetechsupport.GClass.systemList;

public class EngineersTrackingReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private EngineersTrackingAdapter adapter;
    private TextView listSize;
    public static List<ManagerLayout> engineerTrackingList = new ArrayList<>();
    public static List<ManagerLayout> childList = new ArrayList<>();

    private PresenterClass presenterClass;
    private List<ManagerLayout> tempList = new ArrayList<>();
    EditText customerEText,phoneEText,companyEText;
    TextView fromDate,toDate;
    GClass gClass=new GClass(EngineersTrackingReport.this,null,null);
    private Calendar calendar;
    private String engineerText = "All", DateText = "All",engText="All",systemText="All";
    int inEng=0;
    int inSys=0;
    private Date date;
   int  timeFlag=0;
    private SimpleDateFormat dateFormat, dfReport;
    LinearLayout search;
    private Spinner callCenterSpinner,engSpinner,sysSpinner;//dateSpinner
    List<String>engineerList;
    private ArrayAdapter<String> callCenterAdapter,dateAdapter,engAdapter,sysAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineers_tracking_report);

        listSize = findViewById(R.id.tracking_report_count);
        recyclerView = findViewById(R.id.tracking_report_recyclerView);
        customerEText=findViewById(R.id.callCenter_report_Customer);
        phoneEText=findViewById(R.id.callCenter_report_phone);
        companyEText=findViewById(R.id.eng_report_company);
        callCenterSpinner = findViewById(R.id.callCenter_report_engSpinner);
        engSpinner= findViewById(R.id.eng_report_dateSpinner);
        sysSpinner=findViewById(R.id.sys_report_dateSpinner);
        fromDate=findViewById(R.id.fromDate);
        toDate=findViewById(R.id.toDate);
        search=findViewById(R.id.search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenterClass = new PresenterClass(this);
        presenterClass.getTrackingEngineerReportData(this, -1);// when child
        engineerList=new ArrayList<>();



        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fromDate.setText(dateFormat.format(date));
        toDate.setText(dateFormat.format(date));

        ManagerImport managerImport=new ManagerImport(EngineersTrackingReport.this);
        managerImport.startSendingEngSys(EngineersTrackingReport.this,1);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 0;
                new DatePickerDialog(EngineersTrackingReport.this, gClass.openDatePickerDialog(timeFlag,fromDate,toDate,1), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 1;
                new DatePickerDialog(EngineersTrackingReport.this, gClass.openDatePickerDialog(timeFlag,fromDate,toDate,1), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filter();

            }
        });




        fillCallCenterSpinner();
        fillSpinners();

    }


    public void fillAdapter() {

        filter();
    }


    void fillCallCenterSpinner(){
//        engineerList.add(0, "All");
//        engineerList.add("Sarah");
//        engineerList.add("Manal");
//        callCenterAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, engineerInfoList);
//        callCenterAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
//        callCenterSpinner.setAdapter(callCenterAdapter);
//        callCenterSpinner.setOnItemSelectedListener(this);
    }

    void filter() {
        tempList.clear();
        for (int i = 0; i < engineerTrackingList.size(); i++) {
            ManagerLayout object = new ManagerLayout();
            object = engineerTrackingList.get(i);
            tempList.add(object);
        }

        List<ManagerLayout> filtered = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
//            String engineer = (Integer.parseInt(tempList.get(i).getCallCenterId()) == 2 ? "Sarah" : "Manal");
            String engineer = tempList.get(i).getCallCenterName();
            String customer,phone,company;

            if(!customerEText.getText().toString().equals("")){
                customer=customerEText.getText().toString();
            }else {
                customer="All";
            }

            if(!phoneEText.getText().toString().equals("")){
                phone=phoneEText.getText().toString();
            }else {
                phone="All";
            }

            if(!companyEText.getText().toString().equals("")){
                company=companyEText.getText().toString();
            }else {
                company="All";
            }

            String  fromDateLocal="",toDateLocal="";
            fromDateLocal=fromDate.getText().toString();
            toDateLocal=toDate.getText().toString();

            if ((engineerText.equals("All") || engineerText.equals(engineer))
                    &&((gClass.formatDate(tempList.get(i).getTransactionDate()).after(gClass.formatDate(fromDateLocal))
                    || gClass.formatDate(tempList.get(i).getTransactionDate()).equals(gClass.formatDate(fromDateLocal)))
                    && (gClass.formatDate(tempList.get(i).getTransactionDate()).before(gClass.formatDate(toDateLocal))
                    || gClass.formatDate(tempList.get(i).getTransactionDate()).equals(gClass.formatDate(toDateLocal))))
                    &&(engText.equals("All") || tempList.get(i).getEnginerName().equals(engText))
                    &&(systemText.equals("All") || tempList.get(i).getSystemName().equals(systemText))
                    &&(company.equals("All")||tempList.get(i).getCompanyName().contains(company))
                    &&(phone.equals("All")||tempList.get(i).getPhoneNo().contains(phone))
                    &&(customer.equals("All")||tempList.get(i).getCustomerName().contains(customer))) {

                filtered.add(tempList.get(i));
            }
        }

        adapter = new EngineersTrackingAdapter(this,filtered);
        recyclerView.setAdapter(adapter);
        listSize.setText(""+filtered.size());
    }

//    public void engineersTrackingReportFilter() {
//        listSize.setText("" + engineerTrackingList.size());
//        adapter = new EngineersTrackingAdapter(this);
//        recyclerView.setAdapter(adapter);
//    }

    public void getChildData(int serial ) {
        presenterClass.getTrackingEngineerReportData(this,serial);
    }

    public void fillChildData(List<ManagerLayout> list) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.tracking_dialog);

        Log.e("size", "" + list.size());
        TableLayout tableLayout = dialog.findViewById(R.id.engineersTrackingDialog_children_table);

        for (int i = 0; i < list.size(); i++) {
            TableRow tableRow = new TableRow(this);

            if (list.get(i).getState().equals("1")) {// in service/ check in
                tableRow.setBackgroundColor(getResources().getColor(R.color.greenColor2));

            } else if (list.get(i).getState().equals("2")) {// check out
                tableRow.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
            }

            for (int k = 0; k < 10; k++) {
                TextView textView = new TextView(this);
                textView.setTextSize(14);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(ContextCompat.getColor(this, R.color.black));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

                TableRow.LayoutParams param1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1f);
                textView.setPadding(0, 5, 0, 5);

                TableRow.LayoutParams param2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,2f);
                textView.setPadding(0, 5, 0, 5);

                switch (k) {
                    case 0:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getEnginerName());
                        break;
                    case 1:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getSystemName());
                        break;
                    case 2:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param2);
                        textView.setText(list.get(i).getProplem());
                        break;
                    case 3:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCustomerName());
                        break;
                    case 4:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getPhoneNo());
                        break;
                    case 5:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCompanyName());
                        break;
                    case 6:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCheakInTime());
                        break;
                    case 7:
                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCheakOutTime());
                        break;
                    case 8:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getHoldTime());
                        break;
                    case 9:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getTransactionDate());
                        break;
                }

                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
            TableRow emptyTableRow = new TableRow(this);
            TableRow.LayoutParams tableRowParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
            TextView emptyTextView = new TextView(this);
            emptyTableRow.setLayoutParams(tableRowParam);
            emptyTableRow.addView(emptyTextView);
            tableLayout.addView(emptyTableRow);
        }
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public  void fillSpinners(){

        callCenterAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, engineerInfoList);
        callCenterAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        callCenterSpinner.setAdapter(callCenterAdapter);
        callCenterSpinner.setOnItemSelectedListener(this);

        engAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, engList);
        engAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        engSpinner.setAdapter(engAdapter);
        engSpinner.setOnItemSelectedListener(this);



        sysAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, systemList);
        sysAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        sysSpinner.setAdapter(sysAdapter);
        sysSpinner.setOnItemSelectedListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.callCenter_report_engSpinner:
                engineerText = parent.getSelectedItem().toString();
                Log.e("engineer1", engineerText);
                filter();
                break;
//            case R.id.callCenter_report_dateSpinner:
//                DateText = parent.getSelectedItem().toString();
//                Log.e("DateText", DateText);
//                filter();
//                break;


            case R.id.eng_report_dateSpinner:
                engText = parent.getSelectedItem().toString();
                Log.e("engText", engText);
                inEng=position;
                filter();
                break;



            case R.id.sys_report_dateSpinner:
                systemText = parent.getSelectedItem().toString();
                inSys=position;
                Log.e("systemText", systemText);
                filter();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}