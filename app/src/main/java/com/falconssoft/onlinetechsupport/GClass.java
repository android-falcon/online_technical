package com.falconssoft.onlinetechsupport;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.falconssoft.onlinetechsupport.reports.CallCenterTrackingReport;
import com.falconssoft.onlinetechsupport.reports.EngineersTrackingReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.falconssoft.onlinetechsupport.OnlineCenter.hold_List;
import static com.falconssoft.onlinetechsupport.OnlineCenter.isShow;
import static com.falconssoft.onlinetechsupport.OnlineCenter.recyclerView;

public class GClass {
    private Calendar calendar;
    private SimpleDateFormat dateFormat, dfReport;
    private Date date;
    private EngineersTrackingReport engineersTrackingReport;
    private CallCenterTrackingReport callCenterTrackingReport;

    public static List<String> systemList=new ArrayList<String>(),engList=new ArrayList<String>();
    public static List<Systems> systemMList=new ArrayList<Systems>();
    public static List<EngineerInfo> engMList=new ArrayList<EngineerInfo>();
    public static List<String> engineerInfoList=new ArrayList<>();

    public static List<ManagerLayout> customerPhoneNo=new ArrayList<>();
    ArrayAdapter<String>spinnerPhoneAdapter;

    public GClass( EngineersTrackingReport engineersTrackingReport,CallCenterTrackingReport callCenterTrackingReport) {

        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       this. engineersTrackingReport=engineersTrackingReport;
       this. callCenterTrackingReport=callCenterTrackingReport;

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



    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag, final TextView fromDate, final TextView toDate, final int type) {
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
                    if(type==1) {
                        engineersTrackingReport.fillAdapter();
                    }else {
                        callCenterTrackingReport.fillAdapter();
                    }

                }
            }
        };
        return date;
    }


  @SuppressLint("WrongConstant")
  void  fillSearchCustomerPhoneNo(RecyclerView spinnerPhoneNo , String contentPhNo , String contentCustomer , String contentCompany , Context context, EditText editText){
        List<ManagerLayout>spinnerPhoneListTemp = new ArrayList<>();
      spinnerPhoneListTemp.clear();

        for(int i=0;i<customerPhoneNo.size();i++){
            if(customerPhoneNo.get(i).getPhoneNo().contains(contentPhNo)&&customerPhoneNo.get(i).getCustomerName().contains(contentCustomer)
                    &&customerPhoneNo.get(i).getCompanyName().contains(contentCompany)){

                ManagerLayout phoneNo=customerPhoneNo.get(i);
                spinnerPhoneListTemp.add(phoneNo);

            }

        }

        if(spinnerPhoneListTemp.size()!=0) {
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            searchCustomerAdapter searchCustomerAdapt = new searchCustomerAdapter((OnlineCenter) context, spinnerPhoneListTemp, editText);
            spinnerPhoneNo.setLayoutManager(llm);
            spinnerPhoneNo.setAdapter(searchCustomerAdapt);
            isShow=0;
        }else {
            spinnerPhoneNo.setVisibility(View.GONE);
        }
  }

}
