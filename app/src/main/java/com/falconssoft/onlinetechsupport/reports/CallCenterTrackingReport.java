package com.falconssoft.onlinetechsupport.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.PresenterClass;
import com.falconssoft.onlinetechsupport.R;

import java.util.ArrayList;
import java.util.List;

public class CallCenterTrackingReport extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CallCenterTrackingAdapter adapter;
    private PresenterClass presenterClass;
    private List<ManagerLayout> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center_tracking_report);

        presenterClass = new PresenterClass(this);
        presenterClass.getCallCenterData(this);
        recyclerView = findViewById(R.id.callCenter_report_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CallCenterTrackingAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    public void fillAdapter(List<ManagerLayout> list){
        adapter = new CallCenterTrackingAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }
}