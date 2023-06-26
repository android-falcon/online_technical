package com.falconssoft.onlinetechsupport;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
import com.falconssoft.onlinetechsupport.Modle.Systems;

import java.util.ArrayList;
import java.util.List;

public class adapterGridQA extends BaseAdapter {
    Context context;
    List<CustomerOnline> listofSystem;

    adapterGridQA(Context context, List<CustomerOnline> listOfSystem){
        this.context=context;
        listofSystem =new ArrayList();
        this.listofSystem =listOfSystem;
        Log.e("list_system",""+ listofSystem.size());


    }
    @Override
    public int getCount() {
        return listofSystem.size();
    }

    @Override
    public Object getItem(int i) {
        return listofSystem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View myView = View.inflate(context, R.layout.data_qa_layout_main,null);
        final TextView engName,inCount,OutCount,waitCount,total,note;
        LinearLayout mainLayout;

        mainLayout=myView.findViewById(R.id.linearMain);
        waitCount = myView.findViewById(R.id.waitCount);
        engName = myView.findViewById(R.id.engName);
        inCount = myView.findViewById(R.id.inCount);
        note = myView.findViewById(R.id.note);
        OutCount = myView.findViewById(R.id.OutCount);
        total = myView.findViewById(R.id.total);
        note.setVisibility(View.GONE);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QA_Activity qa=(QA_Activity) context;
                qa.clickLinear(listofSystem.get(i));
            }
        });

        if(listofSystem.get(i).getWaitCount()>0&&listofSystem.get(i).getChekInCount()==0){
            note.setVisibility(View.VISIBLE);
        }

        engName.setText(listofSystem.get(i).getEngineerName());
        waitCount.setText(""+listofSystem.get(i).getWaitCount());
        inCount.setText(""+listofSystem.get(i).getChekInCount());
        OutCount.setText(""+listofSystem.get(i).getChekOutCount());
        total.setText(""+(listofSystem.get(i).getChekInCount()+listofSystem.get(i).getChekOutCount()+listofSystem.get(i).getWaitCount()));
        return myView;
    }
}
