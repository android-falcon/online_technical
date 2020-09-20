package com.falconssoft.onlinetechsupport.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.PresenterClass;
import com.falconssoft.onlinetechsupport.R;

import java.util.ArrayList;
import java.util.List;

public class CallCenterTrackingReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private CallCenterTrackingAdapter adapter;
    private PresenterClass presenterClass;
    public static List<ManagerLayout> callCenterList = new ArrayList<>();
    private ArrayAdapter<String> engineerAdapter;
    private Spinner engineerSpinner;
    private String engineerText = "All";
    private List<String> engineerList = new ArrayList<>();
    private List<ManagerLayout> tempList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center_tracking_report);

        presenterClass = new PresenterClass(this);
        presenterClass.getCallCenterData(this);
        recyclerView = findViewById(R.id.callCenter_report_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        engineerSpinner = findViewById(R.id.callCenter_report_engSpinner);
        adapter = new CallCenterTrackingAdapter(this, callCenterList);
        recyclerView.setAdapter(adapter);

        engineerList.add(0, "All");
        engineerList.add("Sara");
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
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.callCenter_report_engSpinner:
                engineerText = parent.getSelectedItem().toString();
                Log.e("engineer1", engineerText);
                filter();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            String engineer = (Integer.parseInt(tempList.get(i).getCallCenterId()) == 2 ? "Sara" : "Manal");
            Log.e("compare",engineerText +  engineer + (engineerText.equals(engineer)));
            if (engineerText.equals("All") || engineerText.equals(engineer)) {
                filtered.add(tempList.get(i));
            }
        }

        adapter = new CallCenterTrackingAdapter(this, filtered);
        recyclerView.setAdapter(adapter);
    }
}