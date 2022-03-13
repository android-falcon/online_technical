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

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ManagerControlUser extends BaseAdapter {

        private Context context;
//
    int col;
        private static List<EngineerInfo> itemsList;
//

        public ManagerControlUser(Context context, List<EngineerInfo> itemsList) {
            this.context = context;
            this.itemsList = itemsList;
              this.col=col;
        }//che


        public void setItemsList(List<EngineerInfo> itemsList) {
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
            TextView userName, userType, available,unavailable;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            final ViewHolder holder = new ViewHolder();
            view = View.inflate(context, R.layout.user_control_row, null);


            holder.userName =  view.findViewById(R.id.userName);
            holder.userType =  view.findViewById(R.id.userType);
            holder.available =  view.findViewById(R.id.available);
            holder.unavailable= view.findViewById(R.id.UnAvailable);

            holder.userName.setText(itemsList.get(i).getName());

            switch (itemsList.get(i).getEng_type()){
                case 0:
                    holder.userType.setText("Manager");
                    break;
                case 1:
                    holder.userType.setText("Online");
                    break;
                case 2:
                    holder.userType.setText("Call Center");
                    break;
                case 3:
                    holder.userType.setText("Call Tec ");
                    break;
                case 4:
                    holder.userType.setText("Technical");
                    break;
                case 5:
                    holder.userType.setText("Call center prog");
                    break;
                case 6:
                    holder.userType.setText("Programmer");
                    break;

            }


            holder.available.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ManagerExport managerExport= new ManagerExport(context,null);
                   managerExport.updateEmployeesFun(itemsList.get(i).getId(),itemsList.get(i).getName(),"0");

                }
            });

            holder.unavailable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ManagerExport managerExport= new ManagerExport(context,null);
                    managerExport.updateEmployeesFun(itemsList.get(i).getId(),itemsList.get(i).getName(),"1");

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