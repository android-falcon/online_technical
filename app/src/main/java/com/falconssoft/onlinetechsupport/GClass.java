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
import com.falconssoft.onlinetechsupport.reports.TechnicalTrackingReport;

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
    TechnicalTrackingReport technicalTrackingReport;
    private DashBoard dashBoard;

    public static List<String> systemList=new ArrayList<String>(),engList=new ArrayList<String>();
    public static List<Systems> systemMList=new ArrayList<Systems>();
    public static List<EngineerInfo> engMList=new ArrayList<EngineerInfo>();
    public static List<String> engineerInfoList=new ArrayList<>();
    public static List<List<ManagerLayout>> listOfCallHour=new ArrayList<List<ManagerLayout>>();
    public static List<List<ManagerLayout>> listOfCallHourByEng=new ArrayList<List<ManagerLayout>>();
    public static List<EngineerInfo> engPerHourList=new ArrayList<EngineerInfo>();
    public static List<Systems> systemDashBordListByEng=new ArrayList<Systems>();
    public static List<Systems> systemListDashBoardSystem=new ArrayList<Systems>();
    public static List<Systems> systemListDashOnlyMax=new ArrayList<Systems>();
    public static List<EngineerInfo> engPerSysList=new ArrayList<EngineerInfo>();
    public static  List<Integer> sizeProgress=new ArrayList<>();
    public static int inCount=0;
    public static int outCount=0;
    public static int holdCounts=0;
    public static List<ManagerLayout> callCenterList = new ArrayList<>();

    public static  List<ManagerLayout> managerDashBord=new ArrayList<>();

    public static List<ManagerLayout> customerPhoneNo=new ArrayList<>();
    ArrayAdapter<String>spinnerPhoneAdapter;

    public void filllistOfCallHour(){

        for(int i=0;i<3;i++){
            List<ManagerLayout>temp=new ArrayList<>();
            for(int q=0;q<19;q++){

                ManagerLayout managerLayout=new ManagerLayout();
                managerLayout.setHour(q);
                managerLayout.setCount(0);

                temp.add(managerLayout);


            }
            listOfCallHour.add(temp);
            Log.e("filllistOfCallHour44",""+listOfCallHour.size());

        }

    }


    public void filllistOfCallHourByEng(){

        for(int i=0;i<3;i++){
            List<ManagerLayout>temp=new ArrayList<>();
            for(int q=0;q<19;q++){

                ManagerLayout managerLayout=new ManagerLayout();
                managerLayout.setHour(q);
                managerLayout.setCount(0);

                temp.add(managerLayout);


            }
            listOfCallHourByEng.add(temp);
            Log.e("listOfCallHourByEng",""+listOfCallHourByEng.size());

        }

    }


    public void filllistOfEngList(){

        for(int q=0;q<engMList.size();q++){

                EngineerInfo managerLayout=new EngineerInfo();
                managerLayout=engMList.get(q);
                managerLayout.setNoOfCountCall(0);
                managerLayout.setPercCall(0.0);

                engPerHourList.add(managerLayout);
            }

            Log.e("filllistOfEngList",""+engPerHourList.size());



    }


    public void filllistOfSystemList(){

        for(int q=0;q<systemMList.size();q++){

            Systems managerLayout=new Systems();
            managerLayout=systemMList.get(q);
            managerLayout.setSystemCount(0);

            systemListDashBoardSystem.add(managerLayout);
        }

        Log.e("filllistOfSystemList",""+systemListDashBoardSystem.size());



    }

    public GClass( EngineersTrackingReport engineersTrackingReport,CallCenterTrackingReport callCenterTrackingReport,DashBoard dashBoardActivity) {

        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       this. engineersTrackingReport=engineersTrackingReport;
       this. callCenterTrackingReport=callCenterTrackingReport;
        this. dashBoard=dashBoardActivity;
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
                    }else  if(type==2) {
                        callCenterTrackingReport.fillAdapter();
                    }else if(type==3) {
                        technicalTrackingReport.fillAdapter();
                    }else if(type==4) {

                        dashBoard.CallDataToFillDashBord();
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


    @SuppressLint("WrongConstant")
    public void setHoldList(Context context) {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        Log.e("fillHoldList",""+hold_List.size());
        if( hold_List.size()!=0)
        {
            final holdCompanyAdapter companyAdapter = new holdCompanyAdapter(context, hold_List);

            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(companyAdapter);
        }
    }

}
