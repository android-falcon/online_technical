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

import java.util.List;

public class ManagerLayOutAdapter3 extends BaseAdapter {

        private Context context;
//
    int col;
        private static List<ManagerLayout> itemsList;
//

        public ManagerLayOutAdapter3(Context context, List<ManagerLayout> itemsList, int col) {
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
            TextView companyName, EngName, timeLoading;
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

            holder.companyName.setText("" + itemsList.get(i).getCompanyName());
            holder.EngName.setText("" + itemsList.get(i).getEnginerName());
            holder.timeLoading.setText("" + itemsList.get(i).getCheakInTime());
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
            }


            holder.mainLiner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 Dialog   dialog = new Dialog(context,R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.information_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

                    TextView customer,company,phone,system,eng,time,states,titel;
                    LinearLayout bac1,bac2;


                    bac1=dialog.findViewById(R.id.bac1);
                    bac2=dialog.findViewById(R.id.bac2);

                    titel =dialog.findViewById(R.id.tital);
                    customer=dialog.findViewById(R.id.customerName);
                    company=dialog.findViewById(R.id.company);
                    phone=dialog.findViewById(R.id.phone);
                    system=dialog.findViewById(R.id.system);
                    eng=dialog.findViewById(R.id.engName);
                    time=dialog.findViewById(R.id.time);
                    states=dialog.findViewById(R.id.state);
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
                    }



                    customer.setText("" + itemsList.get(i).getCustomerName());
                    company.setText("" + itemsList.get(i).getCompanyName());
                    phone.setText("" + itemsList.get(i).getPhoneNo());
                    system.setText("" + itemsList.get(i).getSystemName());
                    eng.setText("" + itemsList.get(i).getEnginerName());
                    time.setText("" + itemsList.get(i).getCheakInTime());
                    states.setText(statesString);

//                    Toast.makeText(context, "main "+ holder.EngName.getText().toString(), Toast.LENGTH_SHORT).show();

                    dialog.show();
                }
            });



            return view;
        }
}