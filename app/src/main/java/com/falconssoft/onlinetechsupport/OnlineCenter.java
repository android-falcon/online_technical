package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class OnlineCenter extends AppCompatActivity {
    GridView gridView;
    RecyclerView recyclerView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_center);

        gridView = (GridView)findViewById(R.id.grid);
        recyclerView=findViewById(R.id.recycler);
        List<String> chequeListitems= new ArrayList<>();
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
}
