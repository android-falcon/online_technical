package com.falconssoft.onlinetechsupport.reports;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.PresenterClass;
import com.falconssoft.onlinetechsupport.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class CallCenterTrackingReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private CallCenterTrackingAdapter adapter;
    private PresenterClass presenterClass;
    public static List<ManagerLayout> callCenterList = new ArrayList<>();
    private ArrayAdapter<String> engineerAdapter,dateAdapter;
    private Spinner engineerSpinner,dateSpinner;
    private String engineerText = "All", DateText = "All";
    private List<String> engineerList = new ArrayList<>();
    private List<ManagerLayout> tempList = new ArrayList<>();
    TextView count;
    public static List<String> DateList=new ArrayList<>();
    List<String> dateListReal=new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center_tracking_report);

        presenterClass = new PresenterClass(this);
        presenterClass.getCallCenterData(this);
        recyclerView = findViewById(R.id.callCenter_report_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        engineerSpinner = findViewById(R.id.callCenter_report_engSpinner);
        dateSpinner= findViewById(R.id.callCenter_report_dateSpinner);
        adapter = new CallCenterTrackingAdapter(this, callCenterList);
        recyclerView.setAdapter(adapter);
        count=findViewById(R.id.count);
        engineerList.add(0, "All");
        engineerList.add("Sarah");
        engineerList.add("Manal");
        engineerAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, engineerList);
        engineerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        engineerSpinner.setAdapter(engineerAdapter);
        engineerSpinner.setOnItemSelectedListener(this);


    }

    public void fillAdapter() {
        adapter = new CallCenterTrackingAdapter(this, callCenterList);
        recyclerView.setAdapter(adapter);
//        filter();
        count.setText(""+callCenterList.size());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.callCenter_report_engSpinner:
                engineerText = parent.getSelectedItem().toString();
                Log.e("engineer1", engineerText);
                filter();
                break;
            case R.id.callCenter_report_dateSpinner:
                DateText = parent.getSelectedItem().toString();
                Log.e("DateText", DateText);
                filter();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }






    public void fillDateSpinner(){

        Set<String> set = new HashSet<>(DateList);
        dateListReal.clear();
        dateListReal.addAll(set);

        dateListReal.add(0,"All");

        dateAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, dateListReal);
        dateAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(this);

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
            if ((engineerText.equals("All") || engineerText.equals(engineer))&&((formatDate(tempList.get(i).getTransactionDate()).equals(formatDate(DateText)))||DateText.equals("All"))) {
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