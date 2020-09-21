package com.falconssoft.onlinetechsupport.reports;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.R;

import java.util.List;

class CallCenterTrackingAdapter extends RecyclerView.Adapter<CallCenterTrackingAdapter.CallCenterTrackingViewHolder> {

    private List<ManagerLayout> list;
    private Context context;

    public CallCenterTrackingAdapter(Context context, List<ManagerLayout> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CallCenterTrackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.call_center_report_row, parent, false);
        return new CallCenterTrackingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallCenterTrackingViewHolder holder, final int i) {
        holder.eng.setText(list.get(i).getEnginerName());
        holder.system.setText(list.get(i).getSystemName());
        holder.problem.setText(list.get(i).getProplem());
        holder.customer.setText(list.get(i).getCustomerName());
        holder.phone.setText(list.get(i).getPhoneNo());
        holder.company.setText(list.get(i).getCompanyName());
        holder.checkIn.setText(list.get(i).getCheakInTime());
        holder.checkOut.setText(list.get(i).getCheakOutTime());
        holder.hold.setText(list.get(i).getHoldTime());
        holder.transactionDate.setText(list.get(i).getTransactionDate());



        if (list.get(i).getState().equals("1")) {// in service
            holder.row_mainLayout.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));

        } else if (list.get(i).getState().equals("2")) {// check out
            holder.row_mainLayout.setBackgroundColor(context.getResources().getColor(R.color.light_blue_over));

        } else if (list.get(i).getState().equals("0")) {// hold
            holder.row_mainLayout.setBackgroundColor(context.getResources().getColor(R.color.goldf2));

        }


        holder.row_mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                informationDialog(Integer.parseInt(list.get(i).getState()),list.get(i));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CallCenterTrackingViewHolder extends RecyclerView.ViewHolder{
        TextView eng, system, problem, customer, phone, company, checkIn, checkOut, hold, transactionDate;
        TableRow row_mainLayout;

        public CallCenterTrackingViewHolder(@NonNull View itemView) {
            super(itemView);

            eng = itemView.findViewById(R.id.callCenter_report_eng_name);
            system = itemView.findViewById(R.id.callCenter_report_system);
            problem = itemView.findViewById(R.id.callCenter_report_problem);
            customer = itemView.findViewById(R.id.callCenter_report_customer);
            phone = itemView.findViewById(R.id.callCenter_report_phone);
            company = itemView.findViewById(R.id.callCenter_report_company);
            checkIn = itemView.findViewById(R.id.callCenter_report_checkIn);
            checkOut = itemView.findViewById(R.id.callCenter_report_checkOut);
            hold = itemView.findViewById(R.id.callCenter_report_hold);
            transactionDate = itemView.findViewById(R.id.callCenter_report_transTime);
            row_mainLayout=itemView.findViewById(R.id.row_mainLayout);

        }
    }

    void informationDialog (int col ,ManagerLayout itemsList){

        Dialog dialog = new Dialog(context,R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.information_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        TextView customer,company,phone,system,eng,time,states,titel,problem,callCenterName;
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
        String statesString ="";
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.top_bottom);
//                    dialog.ge.startAnimation(animation);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog;
        switch (col){
            case 1:
                titel.setBackground(context.getResources().getDrawable(R.drawable.bac_list_1));
                titel.setTextColor(context.getResources().getColor(R.color.greenColor));
                bac1.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));
                bac2.setBackground(context.getResources().getDrawable(R.drawable.bac_list_1));
                statesString="Check In";
                states.setTextColor(context.getResources().getColor(R.color.greenColor));

                break;
            case 2:
                titel.setBackground(context.getResources().getDrawable(R.drawable.bac_list_2));
                titel.setTextColor(context.getResources().getColor(R.color.darkblue_2));
                bac1.setBackgroundColor(context.getResources().getColor(R.color.light_blue_over));
                bac2.setBackground(context.getResources().getDrawable(R.drawable.bac_list_2));
                statesString="Check Out";
                states.setTextColor(context.getResources().getColor(R.color.darkblue_2));

                break;
            case 0:
                titel.setBackground(context.getResources().getDrawable(R.drawable.bac_list_3));
                titel.setTextColor(context.getResources().getColor(R.color.falconColor));
                bac1.setBackgroundColor(context.getResources().getColor(R.color.goldf2));
                bac2.setBackground(context.getResources().getDrawable(R.drawable.bac_list_3));
                statesString="In Hold ";
                states.setTextColor(context.getResources().getColor(R.color.falconColor));
                break;
        }



        customer.setText("" + itemsList.getCustomerName());
        company.setText("" + itemsList.getCompanyName());
        phone.setText("" + itemsList.getPhoneNo());
        system.setText("" + itemsList.getSystemName());
        eng.setText("" + itemsList.getEnginerName());
        time.setText("" + itemsList.getCheakInTime());
        states.setText(statesString);
        problem.setText("" + itemsList.getProplem());
        callCenterName.setText(""+itemsList.getCallCenterName());

//                    Toast.makeText(context, "main "+ holder.EngName.getText().toString(), Toast.LENGTH_SHORT).show();

        dialog.show();


    }
}
