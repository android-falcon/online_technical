package com.falconssoft.onlinetechsupport;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ManagerLayOutAdapter4 extends BaseAdapter {

        private Context context;
//
    int col;
        private static List<ManagerLayout> itemsList;
//

        public ManagerLayOutAdapter4(Context context, List<ManagerLayout> itemsList, int col) {
            this.context = context;
            this.itemsList = itemsList;
              this.col=col;
        }//che


        public void setItemsList(List<ManagerLayout> itemsList) {
            this.itemsList = itemsList;
        }

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        private class ViewHolder {
            TextView companyName, EngName, timeLoading,CheckInTime,CheckOutTime,tecType;
            LinearLayout time,mainLiner;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            final ViewHolder holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_manager_row, null);


            holder.companyName = (TextView) view.findViewById(R.id.companyName);
            holder.EngName = (TextView) view.findViewById(R.id.EngName);
            holder.timeLoading = (TextView) view.findViewById(R.id.timeLoading);
            holder.time=(LinearLayout) view.findViewById(R.id.time);
            holder.mainLiner=(LinearLayout) view.findViewById(R.id.mainLiner);


            holder.CheckOutTime= view.findViewById(R.id.CheckOutTime);
            holder.CheckInTime= view.findViewById(R.id.CheckInTime);
            holder.tecType=view.findViewById(R.id.tecType);
            if(itemsList.get(i).getTecType().equals("2")){
                holder.tecType.setText("On-Tec");
            }else  if(itemsList.get(i).getTecType().equals("4")){
                holder.tecType.setText("Tec");
            }

            holder.CheckInTime.setTextColor(context.getResources().getColor(R.color.falconColor));
            holder.CheckOutTime.setVisibility(View.GONE);
            holder.CheckInTime.setText("" + itemsList.get(i).getHoldTime());



            holder.companyName.setText("" + itemsList.get(i).getCompanyName());
            holder.EngName.setText("" + itemsList.get(i).getEnginerName());
            holder.timeLoading.setText(clock(itemsList.get(i).getHoldTime(),itemsList.get(i).getCurrentTime()));
            switch (col){
                case 0:
                    holder.time.setBackground(context.getResources().getDrawable(R.drawable.bac_list_0));
                    break;
                case 1:
                    holder.time.setBackground(context.getResources().getDrawable(R.drawable.bac_list_0_1));
                    break;
                case 2:
                    holder.time.setBackground(context.getResources().getDrawable(R.drawable.bac_list_0_2));
                    break;
                case 3:
                    holder.time.setBackground(context.getResources().getDrawable(R.drawable.bac_list_0_3));
                    break;
            }


            holder.mainLiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 Dialog   dialog = new Dialog(context,R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.information_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

                    TextView customer,company,phone,system,eng,time,states,titel,problem,callCenterName,dateOfTran,hold_reason;
                    LinearLayout bac1,bac2;


                    bac1=dialog.findViewById(R.id.bac1);
                    bac2=dialog.findViewById(R.id.bac2);
                    problem=dialog.findViewById(R.id.problem);
                    titel =dialog.findViewById(R.id.tital);
                    customer=dialog.findViewById(R.id.customerName);
                    company=dialog.findViewById(R.id.company);
                    phone=dialog.findViewById(R.id.phone);
                    system=dialog.findViewById(R.id.system);
                    eng=dialog.findViewById(R.id.engName);
                    time=dialog.findViewById(R.id.time);
                    states=dialog.findViewById(R.id.state);
                    callCenterName=dialog.findViewById(R.id.call_center_name);
                    dateOfTran=dialog.findViewById(R.id.dateOfTran);
                    hold_reason=dialog.findViewById(R.id.hold_reason);
                    String statesString ="";
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.top_bottom);
//                    dialog.ge.startAnimation(animation);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog;
                    switch (col){
                        case 0:
                            titel.setBackground(context.getResources().getDrawable(R.drawable.bac_list_1));
                            titel.setTextColor(context.getResources().getColor(R.color.greenColor));
                            bac1.setBackgroundColor(context.getResources().getColor(R.color.greenColor));
                            bac2.setBackground(context.getResources().getDrawable(R.drawable.bac_list_1));
                            statesString="Check In";
                            states.setTextColor(context.getResources().getColor(R.color.greenColor));

                            break;
                        case 1:
                            titel.setBackground(context.getResources().getDrawable(R.drawable.bac_list_2));
                            titel.setTextColor(context.getResources().getColor(R.color.darkblue_2));
                            bac1.setBackgroundColor(context.getResources().getColor(R.color.darkblue_2));
                            bac2.setBackground(context.getResources().getDrawable(R.drawable.bac_list_2));
                            statesString="Check Out";
                            states.setTextColor(context.getResources().getColor(R.color.darkblue_2));

                            break;
                        case 2:
                            titel.setBackground(context.getResources().getDrawable(R.drawable.bac_list_3));
                            titel.setTextColor(context.getResources().getColor(R.color.falconColor));
                            bac1.setBackgroundColor(context.getResources().getColor(R.color.falconColor));
                            bac2.setBackground(context.getResources().getDrawable(R.drawable.bac_list_3));
                            statesString="In Hold ";
                            states.setTextColor(context.getResources().getColor(R.color.falconColor));
                            break;

                        case 3:
                            titel.setBackground(context.getResources().getDrawable(R.drawable.bac_list_4));
                            titel.setTextColor(context.getResources().getColor(R.color.blueColor));
                            bac1.setBackgroundColor(context.getResources().getColor(R.color.blueColor));
                            bac2.setBackground(context.getResources().getDrawable(R.drawable.bac_list_4));
                            statesString="In Hold ";
                            states.setTextColor(context.getResources().getColor(R.color.blueColor));
                            break;
                    }



                    customer.setText("" + itemsList.get(i).getCustomerName());
                    company.setText("" + itemsList.get(i).getCompanyName());
                    phone.setText("" + itemsList.get(i).getPhoneNo());
                    system.setText("" + itemsList.get(i).getSystemName());
                    eng.setText("" + itemsList.get(i).getEnginerName());
                    time.setText("" + itemsList.get(i).getHoldTime());
                    states.setText(statesString);
                    problem.setText("" + itemsList.get(i).getProplem());
                    callCenterName.setText(""+itemsList.get(i).getCallCenterName());
                    dateOfTran.setText(""+itemsList.get(i).getTransactionDate());
                    hold_reason.setText(""+itemsList.get(i).getHoldReason());
//                    Toast.makeText(context, "main "+ holder.EngName.getText().toString(), Toast.LENGTH_SHORT).show();

                    dialog.show();
                }
            });



            return view;
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