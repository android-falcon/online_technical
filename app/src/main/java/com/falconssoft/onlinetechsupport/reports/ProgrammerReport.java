package com.falconssoft.onlinetechsupport.reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.DatabaseHandler;
import com.falconssoft.onlinetechsupport.ManagerImport;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.PresenterClass;
import com.falconssoft.onlinetechsupport.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.falconssoft.onlinetechsupport.GClass.callCenterList;
import static com.falconssoft.onlinetechsupport.GClass.engList;
import static com.falconssoft.onlinetechsupport.GClass.engineerInfoList;
import static com.falconssoft.onlinetechsupport.GClass.systemList;

public class ProgrammerReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHandler databaseHandler;
    private RecyclerView recyclerView;
    private CallCenterTrackingAdapter adapter;
    private PresenterClass presenterClass;

    private ArrayAdapter<String> callCenterAdapter,dateAdapter,engAdapter,sysAdapter;
    private Spinner callCenterSpinner,engSpinner,sysSpinner;//dateSpinner
    private String engineerText = "All", DateText = "All",engText="All",systemText="All";
    private List<String> engineerList = new ArrayList<>();
    private List<ManagerLayout> tempList = new ArrayList<>();
    TextView count,CheckInTimeId,CheckOutTimeId,HoldTimeId,problemOrder;
    public static List<String> DateList=new ArrayList<>();
    List<String> dateListReal=new ArrayList<String>();


    private static final int REQ_CODE_SPEECH_INPUT_Company = 200;
    private static final int REQ_CODE_SPEECH_INPUT_CUSTOMER = 300;
//    public static List<String> systemList=new ArrayList<String>(),engList=new ArrayList<String>();
//    public static List<Systems> systemMList=new ArrayList<Systems>();
//    public static List<EngineerInfo> engMList=new ArrayList<EngineerInfo>();
    int inEng=0;
    int inSys=0;
    TextView infoTableReport;

    TextView reportName,fromDate,toDate,btnSpeakCompany,btnSpeakCustomer,dateTransOrder,companyOrder,phoneOrder,customerFilter,engOrder;
    private int timeFlag = 0;// 0=> from, 1=> to
    EditText customerEText,phoneEText,companyEText;
    LinearLayout search;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, dfReport;
    private Date date;
    boolean upDownFlagCheInTime =false;
    boolean upDownFlagCheOutTime =true;
    boolean upDownFlagHoldTime =true;
    boolean upDownFlagProblem =false;
    boolean upDownFlagDateTrance =false;
    boolean upDownFlagEng =false;
    boolean upDownFlagCustomer =false;
    boolean upDownFlagPhone =false;
    boolean upDownFlagCompany =false;
    int timeFlagOrder =0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center_tracking_report);

        presenterClass = new PresenterClass(this);
        presenterClass.getCallCenterData(null,null,this,"6");
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
        adapter = new CallCenterTrackingAdapter(this, callCenterList,"6");
        recyclerView.setAdapter(adapter);
        count=findViewById(R.id.count);
        CheckInTimeId=findViewById(R.id.CheckInTimeId);
        CheckOutTimeId=findViewById(R.id.CheckOutTimeId);
        HoldTimeId=findViewById(R.id.HoldTimeId);
        problemOrder=findViewById(R.id.problemOrder);
        dateTransOrder=findViewById(R.id.dateTransOrder);
        companyOrder=findViewById(R.id.companyOrder);
        phoneOrder=findViewById(R.id.phoneOrder);
        customerFilter=findViewById(R.id.customerFilter);
        engOrder=findViewById(R.id.engOrder);
        reportName=findViewById(R.id.reportName);
        reportName.setText(ProgrammerReport.this.getResources().getString(R.string.Programmer_Report));

//        engineerList.add("Sarah");
//        engineerList.add("Manal");

        fromDate=findViewById(R.id.fromDate);
        toDate=findViewById(R.id.toDate);
        btnSpeakCompany=findViewById(R.id.btnSpeakCompany);
        btnSpeakCustomer=findViewById(R.id.btnSpeakCustomer);
        btnSpeakCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput(1);
            }
        });
        btnSpeakCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput(2);
            }
        });

        ManagerImport managerImport=new ManagerImport(ProgrammerReport.this);
        managerImport.startSendingEngSys(ProgrammerReport.this,5);

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
                new DatePickerDialog(ProgrammerReport.this, openDatePickerDialog(timeFlag), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 1;
                new DatePickerDialog(ProgrammerReport.this, openDatePickerDialog(timeFlag), calendar
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

        CheckInTimeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=0;
                if(upDownFlagCheInTime) {
                    upDownFlagCheInTime =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparatorIn());
                    filter();
//                    CheckInTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagCheInTime =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorIn()));
                    filter();
//                    CheckInTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });


//        dateTransOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                timeFlagOrder=1;
//                if(upDownFlagDateTrance) {
//                    upDownFlagDateTrance =false;
//                    //ic_arrow_upward_black_24dp
//                    Collections.sort(callCenterList, new StringDateComparatorDateTrance());
//                    filter();
////                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
//                }else {
//                    upDownFlagDateTrance =true;
//                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorDateTrance()));
//                    filter();
////                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);
//
//                }
//
//            }
//        });


        engOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=4;
                if(upDownFlagEng) {
                    upDownFlagEng =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparatorString());
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagEng =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorString()));
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });


        companyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=5;
                if(upDownFlagCompany) {
                    upDownFlagCompany =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparatorString());
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagCompany =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorString()));
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });

        phoneOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=6;
                if(upDownFlagPhone) {
                    upDownFlagPhone =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparatorString());
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagPhone =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorString()));
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });


        customerFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=7;
                if(upDownFlagCustomer) {
                    upDownFlagCustomer =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparatorString());
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagCustomer =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorString()));
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });

        CheckOutTimeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=1;
                if(upDownFlagCheOutTime) {
                    upDownFlagCheOutTime =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparatorOut());
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagCheOutTime =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorOut()));
                    filter();
//                    CheckOutTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });




        HoldTimeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=2;
                if(upDownFlagHoldTime) {
                    upDownFlagHoldTime =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparatorHold());
                    filter();
//                    HoldTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagHoldTime =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparatorHold()));
                    filter();
//                    HoldTimeId.setCompoundDrawables(null, null, CallCenterTrackingReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });


        problemOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFlagOrder=3;
                if(upDownFlagProblem) {
                    upDownFlagProblem =false;
                    //ic_arrow_upward_black_24dp
                    Collections.sort(callCenterList, new StringDateComparator());
                    filter();
                    problemOrder.setCompoundDrawables(null, null, ProgrammerReport.this.getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp), null);
                }else {
                    upDownFlagProblem =true;
                    Collections.sort(callCenterList,Collections.reverseOrder(new StringDateComparator()));
                    filter();
                    problemOrder.setCompoundDrawables(null, null, ProgrammerReport.this.getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp), null);

                }

            }
        });




    }
    private void startVoiceInput(int flag) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            if(flag==1)// search company
            {
                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_Company);
            }
            else
            if(flag==2)
            {

                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_CUSTOMER);
            }


        } catch (ActivityNotFoundException a) {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT_Company: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    companyEText.setText(result.get(0));
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_CUSTOMER: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    customerEText.setText(result.get(0));
                }
                break;
            }



        }
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
        Dialog dialog = new Dialog(ProgrammerReport.this,R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.information_dialog_table_color);
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
                Log.e("engineer1", "onItemSelected");
                engineerText = parent.getSelectedItem().toString();

                Log.e("engineer1", engineerText+position);
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
//            String engineer = (Integer.parseInt(tempList.get(i).getCallCenterId()) == 2 ? "Sarah" : "Manal");

            String engineer = tempList.get(i).getCallCenterName();
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

        adapter = new CallCenterTrackingAdapter(this, filtered,"6");
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


//    class SorterClass implements Comparator<BundleInfo> {
//        @Override
//        public int compare(BundleInfo one, BundleInfo another) {
//            int returnVal = 0;
//            switch (sortFlag) {
//                case 0: // thickness
//                    if (one.getThickness() < another.getThickness()) {
//                        returnVal = -1;
//                    } else if (one.getThickness() > another.getThickness()) {
//                        returnVal = 1;
//                    } else if (one.getThickness() == another.getThickness()) {
//                        returnVal = 0;
//                    }
//                    break;
//
//                case 1: // width
//                    if (one.getWidth() < another.getWidth()) {
//                        returnVal = -1;
//                    } else if (one.getWidth() > another.getWidth()) {
//                        returnVal = 1;
//                    } else if (one.getWidth() == another.getWidth()) {
//                        returnVal = 0;
//                    }
//                    break;
//
//                case 2: // length
//                    if (one.getLength() < another.getLength()) {
//                        returnVal = -1;
//                    } else if (one.getLength() > another.getLength()) {
//                        returnVal = 1;
//                    } else if (one.getLength() == another.getLength()) {
//                        returnVal = 0;
//                    }
//                    break;
//            }
//            return returnVal;
//        }
//
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callCenterList.clear();
    }

    class StringDateComparator implements Comparator<ManagerLayout>
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        int dateTime=0;
        public int compare(ManagerLayout lhs, ManagerLayout rhs)
        {

            dateTime= lhs.getProplem().compareTo(rhs.getProplem());

            return dateTime;
        }
    }



    class StringDateComparatorIn implements Comparator<ManagerLayout>
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        public int compare(ManagerLayout lhs, ManagerLayout rhs)
        {
            //
            if (lhs.getCheakInTime() == null || rhs.getCheakInTime() == null)
                return 0;
            return lhs.getCheakInTime().compareTo(rhs.getCheakInTime());

        }
    }

    class StringDateComparatorOut implements Comparator<ManagerLayout>
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        public int compare(ManagerLayout lhs, ManagerLayout rhs)
        {
            //
            if (lhs.getCheakOutTime() == null || rhs.getCheakOutTime() == null)
                return 0;
            return lhs.getCheakOutTime().compareTo(rhs.getCheakOutTime());

        }
    }

    class StringDateComparatorDateTrance implements Comparator<ManagerLayout>
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        public int compare(ManagerLayout lhs, ManagerLayout rhs)
        {
            //
            if (lhs.getTransactionDate() == null || rhs.getTransactionDate() == null)
                return 0;
            return lhs.getTransactionDate().compareTo(rhs.getTransactionDate());

        }
    }


    class StringDateComparatorString implements Comparator<ManagerLayout>
    {
        public int compare(ManagerLayout lhs, ManagerLayout rhs)
        {
            //
            switch (timeFlagOrder) {
                case 4:
                if (lhs.getEnginerName() == null || rhs.getEnginerName() == null)
                    return 0;
                return lhs.getEnginerName().compareTo(rhs.getEnginerName());


                case 5:
                    if (lhs.getCompanyName() == null || rhs.getCompanyName() == null)
                        return 0;
                    return lhs.getCompanyName().compareTo(rhs.getCompanyName());


                case 6:
                    if (lhs.getPhoneNo() == null || rhs.getPhoneNo() == null)
                        return 0;
                    return lhs.getPhoneNo().compareTo(rhs.getPhoneNo());


                case 7:
                    if (lhs.getCustomerName() == null || rhs.getCustomerName() == null)
                        return 0;
                    return lhs.getCustomerName().compareTo(rhs.getCustomerName());


            }
            return 0;
        }
    }

    class StringDateComparatorHold implements Comparator<ManagerLayout>
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        public int compare(ManagerLayout lhs, ManagerLayout rhs)
        {
            //
            if (lhs.getHoldTime() == null || rhs.getHoldTime() == null)
                return 0;
            return lhs.getHoldTime().compareTo(rhs.getHoldTime());

        }
    }

}
