package com.falconssoft.onlinetechsupport.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(@NonNull CallCenterTrackingViewHolder holder, int i) {
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
}
