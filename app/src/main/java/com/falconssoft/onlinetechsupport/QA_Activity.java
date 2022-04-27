package com.falconssoft.onlinetechsupport;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QA_Activity extends AppCompatActivity {

    adapterGridQA adapter;
    adapterGridQAInfo adapterInfo;
    GridView gridView, gridViewInfo;
    ManagerImport managerImport;
Timer timer;
ImageButton refresh;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qa_activity);
        initialization();
//        List<CustomerOnline>list=new ArrayList<>();
//        CustomerOnline onlineCenter=new CustomerOnline();
//        onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);
//        onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);
//        onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);
//        onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);
//        onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);onlineCenter.setCompanyName("aa");
//        onlineCenter.setCallCenterName("kjhkj");
//        list.add(onlineCenter);
//
//        fillAdapter(list);


        managerImport.startGetQA();


    }

    private void initialization() {
        managerImport = new ManagerImport(QA_Activity.this);

        gridView = findViewById(R.id.gridView);
refresh=findViewById(R.id.refresh);
refresh.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        managerImport.startGetQA();
    }
});

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                managerImport.startGetQA();

            }

        }, 0, 60000);

    }

    void fillAdapter(List<CustomerOnline> list) {

        adapter = new adapterGridQA(QA_Activity.this, list);
        gridView.setAdapter(adapter);


    }

    void clickLinear(CustomerOnline customerOnline) {
//        managerImport.

        final Dialog dialog = new Dialog(QA_Activity.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.qa_activity_dialog);
        GridView gridView = dialog.findViewById(R.id.gridView);
        final TextView inCount,OutCount,waitCount;

        TextView textEngName=dialog.findViewById(R.id.engName);
        gridViewInfo = gridView;
        managerImport.startGetQA_info(customerOnline.getEngineerID());
        textEngName.setText(customerOnline.getEngineerName());
        waitCount = dialog.findViewById(R.id.waitCount);
        inCount = dialog.findViewById(R.id.inCount);
        OutCount = dialog.findViewById(R.id.OutCount);

        waitCount.setText(""+customerOnline.getWaitCount());
        inCount.setText(""+customerOnline.getChekInCount());
        OutCount.setText(""+customerOnline.getChekOutCount());

        dialog.show();

    }

    void fillAdapter_info(List<CustomerOnline> list) {

        adapterInfo = new adapterGridQAInfo(QA_Activity.this, list);
        gridViewInfo.setAdapter(adapterInfo);


    }


}
