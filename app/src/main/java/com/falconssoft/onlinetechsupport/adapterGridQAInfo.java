package com.falconssoft.onlinetechsupport;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class adapterGridQAInfo extends BaseAdapter {
    Context context;
    List<CustomerOnline> listofSystem;

    adapterGridQAInfo(Context context, List<CustomerOnline> listOfSystem){
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
        View myView = View.inflate(context, R.layout.data_qa_layout,null);
        final TextView companyName,status,customerName,checkInTime,CheckOutTime,delay,problem;
        TableRow StatusTable;
LinearLayout time;

        companyName = myView.findViewById(R.id.companyName);
        status = myView.findViewById(R.id.status);
        customerName = myView.findViewById(R.id.customerName);
        checkInTime = myView.findViewById(R.id.CIT);
        CheckOutTime = myView.findViewById(R.id.COT);
        delay = myView.findViewById(R.id.del);
        problem = myView.findViewById(R.id.problem);
        StatusTable=myView.findViewById(R.id.StatusTable);
        time=myView.findViewById(R.id.time);
        int stat=listofSystem.get(i).getCustomerState();
        int DStatus=listofSystem.get(i).getDangerStatus();




        if(stat==2){
            String timeDel=clock(listofSystem.get(i).getCheakInTime(),listofSystem.get(i).getCheakOutTime());
            delay.setText(""+timeDel);
            status.setText(" Finish Complete ");
            StatusTable.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));
            time.setBackground(context.getResources().getDrawable(R.drawable.background_checkin));

        }else if(stat==1){

            String timeDel=clock(listofSystem.get(i).getCheakInTime(),listofSystem.get(i).getTIME());
            delay.setText(""+timeDel);

            if(DStatus==0){
                //normal wait but not checkin
                status.setText("Wait to Service(غير متعطل)");
                time.setBackground(context.getResources().getDrawable(R.drawable.button_f_));
                StatusTable.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));
            }else if(DStatus==1){
                status.setText("Wait to Service (متعطل)");
                time.setBackground(context.getResources().getDrawable(R.drawable.button_f_));
                StatusTable.setBackgroundColor(context.getResources().getColor(R.color.exit_hover));

            }else if(DStatus==2){
                status.setText("in Service (متعطل)");
                StatusTable.setBackgroundColor(context.getResources().getColor(R.color.exit_hover));

                time.setBackground(context.getResources().getDrawable(R.drawable.button_f_g));

            }else if(DStatus==3){
                status.setText("in Service ( غير متعطل)");
                StatusTable.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));

                time.setBackground(context.getResources().getDrawable(R.drawable.button_f_g));

            }else if(DStatus==4){
                status.setText("Wait to Service( متعطل)");
                StatusTable.setBackgroundColor(context.getResources().getColor(R.color.exit_hover));

                time.setBackground(context.getResources().getDrawable(R.drawable.button_f_));

            }else if(DStatus==5){
                status.setText("Wait to Service(غير متعطل)");
                StatusTable.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));

                time.setBackground(context.getResources().getDrawable(R.drawable.button_f_));

            }


        }

        companyName.setText(listofSystem.get(i).getCompanyName());
       // status.setText(""+listofSystem.get(i).getDangerStatus());
        customerName.setText(listofSystem.get(i).getCustomerName());
        checkInTime.setText(listofSystem.get(i).getCheakInTime());
        CheckOutTime.setText(listofSystem.get(i).getCheakOutTime());
       // delay.setText(listofSystem.get(i).getCheakInTime());
        problem.setText(listofSystem.get(i).getProblem());

        return myView;
    }

    public  String clock(String inTime ,String outTime){
        String timing ="";

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        long diffSeconds=0,diffMinutes=0,diffHours = 0;
        try {
            d1 = format.parse(inTime);
            d2 = format.parse(outTime);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
//                long diffDays = diff / (24 * 60 * 60 * 1000);

            timing="" +diffHours+":"+diffMinutes+":"+diffSeconds;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return timing;
    }

}


