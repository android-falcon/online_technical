package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class OnlineCenter extends AppCompatActivity {
    GridView gridView;
    RecyclerView recyclerView;
    List<EngineerInfo> engineerInfoList;
    List<String> chequeListitems;
    TextView customer_name,companey_name,telephone_no;
    Spinner  spenner_systems;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_center);
        initialView();
        engineerInfoList=new ArrayList<>();
        fillEngineerInfoList();




        chequeListitems= new ArrayList<>();
        chequeListitems.add("tahani");
        chequeListitems.add("tahani");
        chequeListitems.add("tahani");
        chequeListitems.add("tahani");
        chequeListitems.add("tahani");
        chequeListitems.add("tahani");
        chequeListitems.add("tahani");
        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(OnlineCenter.this);
        layoutManager.setOrientation(VERTICAL);


        adapterGridEngineer engineerAdapter = new adapterGridEngineer(this, chequeListitems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new holdCompanyAdapter(OnlineCenter.this,chequeListitems));
        gridView.setAdapter(engineerAdapter);
    }

    private void fillEngineerInfoList() {



    }

    private void initialView() {
        gridView = (GridView)findViewById(R.id.grid);
        recyclerView=findViewById(R.id.recycler);
        customer_name=findViewById(R.id.customer_name);
        companey_name=findViewById(R.id.companey_name);
        telephone_no=findViewById(R.id.telephone_no);
        spenner_systems=findViewById(R.id.spenner_systems);

    }

    public void sendCompaneyInfo(View view)
    {
        if(view.getId()==R.id.checkIn_btn)
        {
            getData();
            ManagerImport managerImport = new ManagerImport(OnlineCenter.this);
            managerImport.startSending("ManageriN");
        }

    }

    private void getData() {
        String companName;
//        if()

    }
}
