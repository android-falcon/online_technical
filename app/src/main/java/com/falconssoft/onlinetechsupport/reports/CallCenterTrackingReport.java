package com.falconssoft.onlinetechsupport.reports;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.falconssoft.onlinetechsupport.ManagerImport;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.falconssoft.onlinetechsupport.PresenterClass;
import com.falconssoft.onlinetechsupport.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.falconssoft.onlinetechsupport.GClass.engList;
import static com.falconssoft.onlinetechsupport.GClass.systemList;

public class CallCenterTrackingReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private CallCenterTrackingAdapter adapter;
    private PresenterClass presenterClass;
    public static List<ManagerLayout> callCenterList = new ArrayList<>();
    private ArrayAdapter<String> callCenterAdapter,dateAdapter,engAdapter,sysAdapter;
    private Spinner callCenterSpinner,engSpinner,sysSpinner;//dateSpinner
    private String engineerText = "All", DateText = "All",engText="All",systemText="All";
    private List<String> engineerList = new ArrayList<>();
    private List<ManagerLayout> tempList = new ArrayList<>();
    TextView count;
    public static List<String> DateList=new ArrayList<>();
    List<String> dateListReal=new ArrayList<String>();
//    public static List<String> systemList=new ArrayList<String>(),engList=new ArrayList<String>();
//    public static List<Systems> systemMList=new ArrayList<Systems>();
//    public static List<EngineerInfo> engMList=new ArrayList<EngineerInfo>();
    int inEng=0;
    int inSys=0;
    TextView infoTableReport;

    TextView fromDate,toDate;
    private int timeFlag = 0;// 0=> from, 1=> to
    EditText customerEText,phoneEText,companyEText;
    LinearLayout search;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, dfReport;
    private Date date;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center_tracking_report);

        presenterClass = new PresenterClass(this);
        presenterClass.getCallCenterData(this);
        infoTableReport= findViewById(R.id.infoTableReport);
        infoTableReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfoColor();
            }
        });
        recyclerView = findViewById(R.id.callCenter_report_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        callCenterSpinner = findViewById(R.id.callCenter_report_engSpinner);
        engSpinner= findViewById(R.id.eng_report_dateSpinner);
        sysSpinner=findViewById(R.id.sys_report_dateSpinner);
        customerEText=findViewById(R.id.callCenter_report_Customer);
        phoneEText=findViewById(R.id.callCenter_report_phone);
        companyEText=findViewById(R.id.eng_report_company);
        search=findViewById(R.id.search);
//        dateSpinner= findViewById(R.id.callCenter_report_dateSpinner);
        adapter = new CallCenterTrackingAdapter(this, callCenterList);
        recyclerView.setAdapter(adapter);
        count=findViewById(R.id.count);
        engineerList.add(0, "All");
        engineerList.add("Sarah");
        engineerList.add("Manal");
        callCenterAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, engineerList);
        callCenterAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        callCenterSpinner.setAdapter(callCenterAdapter);
        callCenterSpinner.setOnItemSelectedListener(this);

        fromDate=findViewById(R.id.fromDate);
        toDate=findViewById(R.id.toDate);

        ManagerImport managerImport=new ManagerImport(CallCenterTrackingReport.this);
        managerImport.startSendingEngSys(CallCenterTrackingReport.this,0);

//        customerEText.addTextChangedListener(textWatcher);
//        phoneEText.addTextChangedListener(textWatcher);
//        companyEText.addTextChangedListener(textWatcher);

        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fromDate.setText(dateFormat.format(date));
        toDate.setText(dateFormat.format(date));


        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 0;
                new DatePickerDialog(CallCenterTrackingReport.this, openDatePickerDialog(timeFlag), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 1;
                new DatePickerDialog(CallCenterTrackingReport.this, openDatePickerDialog(timeFlag), calendar
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

    }


    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                if (flag == 0)
                    fromDate.setText(sdf.format(calendar.getTime()));
                else
                    toDate.setText(sdf.format(calendar.getTime()));

                if (!fromDate.getText().toString().equals("") && !toDate.getText().toString().equals("")) {
                    filter();
                }
            }
        };
        return date;
    }

    private void dialogInfoColor() {
        Dialog dialog = new Dialog(CallCenterTrackingReport.this,R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.information_dialog_table_color);
        dialog.show();
    }


    public  void fillSpinners(){



        engAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, engList);
        engAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        engSpinner.setAdapter(engAdapter);
        engSpinner.setOnItemSelectedListener(this);



        sysAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, systemList);
        sysAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        sysSpinner.setAdapter(sysAdapter);
        sysSpinner.setOnItemSelectedListener(this);


    }

    public void fillAdapter() {
//        adapter = new CallCenterTrackingAdapter(this, callCenterList);
//        recyclerView.setAdapter(adapter);
        filter();
//        count.setText(""+callCenterList.size());
    }


    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            filter();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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




    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void fillDateSpinner(){
//
//        Set<String> set = new HashSet<>(DateList);
//        dateListReal.clear();
//        dateListReal.addAll(set);
//
//        dateListReal.add(0,"All");
//
//        List<String> dateDayList=new ArrayList<>();
//        dateDayList.add(0,"All");
//
//        for (int i=1;i<dateListReal.size();i++) {
//    String input_date = dateListReal.get(i);
//    String day=getDate(input_date);
//    dateDayList.add(i,input_date+"   ("+day+")");
//
//}
//        dateAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, dateDayList);
//        dateAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
//        dateSpinner.setAdapter(dateAdapter);
//        dateSpinner.setOnItemSelectedListener(this);
//
//    }


    String getDate(String input_date){

        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("EEEE");
        String finalDay=format2.format(dt1);

        Log.e("getNameOfDay",""+finalDay);

        return finalDay;

    }

    void filter() {
        tempList.clear();
        for (int i = 0; i < callCenterList.size(); i++) {
            ManagerLayout object = new ManagerLayout();
            object = callCenterList.get(i);
            tempList.add(object);
        }

        List<ManagerLayout> filtered = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            String engineer = (Integer.parseInt(tempList.get(i).getCallCenterId()) == 2 ? "Sarah" : "Manal");
//            int indexEng= engMList.indexOf(engText);
//            String IdEng="-1";
//            if(indexEng!=-1){
//                IdEng =engMList.get(indexEng).getId();
//            }else {
//                IdEng="-1";
//            }
//            Log.e("indexEng",""+indexEng);
//
//            int indexSys= systemMList.indexOf(systemText);
//            String IdSys="-1";
//            if(indexEng!=-1){
//                IdSys =systemMList.get(indexSys).getSystemNo();
//            }else {
//                IdSys="-1";
//            }
//            Log.e("indexSys",""+indexEng);

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
                    &&((formatDate(tempList.get(i).getTransactionDate()).after(formatDate(fromDateLocal))
                    || formatDate(tempList.get(i).getTransactionDate()).equals(formatDate(fromDateLocal)))
                    && (formatDate(tempList.get(i).getTransactionDate()).before(formatDate(toDateLocal))
                    || formatDate(tempList.get(i).getTransactionDate()).equals(formatDate(toDateLocal))))
                    &&(engText.equals("All") || tempList.get(i).getEnginerName().equals(engText))
                    &&(systemText.equals("All") || tempList.get(i).getSystemName().equals(systemText))
                    &&(company.equals("All")||tempList.get(i).getCompanyName().contains(company))
                    &&(phone.equals("All")||tempList.get(i).getPhoneNo().contains(phone))
                    &&(customer.equals("All")||tempList.get(i).getCustomerName().contains(customer))) {

                filtered.add(tempList.get(i));
            }
        }

        adapter = new CallCenterTrackingAdapter(this, filtered);
        recyclerView.setAdapter(adapter);
        count.setText(""+filtered.size());
    }


    public Date formatDate(String date) {

//        Log.e("date", date);
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        Date d = null;
        try {
            d = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

}