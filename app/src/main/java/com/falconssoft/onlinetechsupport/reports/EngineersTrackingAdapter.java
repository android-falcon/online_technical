package com.falconssoft.onlinetechsupport.reports;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.R;

import java.util.List;


class EngineersTrackingAdapter extends RecyclerView.Adapter<EngineersTrackingAdapter.EngineersTrackingViewHolder> {

    private List<ManagerLayout> childList;
    private EngineersTrackingReport context;
    private List<ManagerLayout> engTrackingList;
    public EngineersTrackingAdapter(EngineersTrackingReport context, List<ManagerLayout> engTrackingList) {
        this.engTrackingList = engTrackingList;
        this.context = context;
    }

//    public EngineersTrackingAdapter(EngineersTrackingReport context, List<ManagerLayout> childList) {
//        this.childList = childList;
//        this.context = context;
//    }

    @NonNull
    @Override
    public EngineersTrackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.engineers_tracking_row, parent, false);
        return new EngineersTrackingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EngineersTrackingViewHolder holder, final int i) {

        holder.eng.setText(engTrackingList.get(i).getEnginerName());
        holder.system.setText(engTrackingList.get(i).getSystemName());
        holder.problem.setText(engTrackingList.get(i).getProplem());
        holder.customer.setText(engTrackingList.get(i).getCustomerName());
        holder.phone.setText(engTrackingList.get(i).getPhoneNo());
        holder.company.setText(engTrackingList.get(i).getCompanyName());
        holder.checkIn.setText(engTrackingList.get(i).getCheakInTime());
        holder.checkOut.setText(engTrackingList.get(i).getCheakOutTime());
        holder.hold.setText(engTrackingList.get(i).getHoldTime());
        holder.transactionDate.setText(engTrackingList.get(i).getTransactionDate());

//        if (engineerTrackingList.get(i).isShowDetails()){
//            fillChildData(i,holder.childrenLinear);
//        }

        if (engTrackingList.get(i).getState().equals("1")) {// in service/ check in
            holder.tableRow.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));

        } else if (engTrackingList.get(i).getState().equals("2")) {// check out
            holder.tableRow.setBackgroundColor(context.getResources().getColor(R.color.light_blue_over));

        }


        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getChildData(Integer.parseInt(engTrackingList.get(i).getSerial()));
//            if (engineerTrackingList.get(i).isShowDetails()){
//                engineerTrackingList.get(i).setShowDetails(false);
//                holder.showDetails.setImageResource(R.drawable.ic_arrow_drop_down);
//                holder.headerLinear.setVisibility(View.GONE);
//            }else {
//                engineerTrackingList.get(i).setShowDetails(true);
//                holder.showDetails.setImageResource(R.drawable.ic_arrow_drop_up);
//                holder.headerLinear.setVisibility(View.VISIBLE);
//                context.getChildData(i);
//
//            }

            }
        });
    }

    @Override
    public int getItemCount() {
        return engTrackingList.size();
    }

    class EngineersTrackingViewHolder extends RecyclerView.ViewHolder{

        TextView eng, system, problem, customer, phone, company, checkIn, checkOut, hold, transactionDate;
        ImageView showDetails;
        TableRow tableRow;
        LinearLayout  headerLinear;
        TableLayout childrenLinear;

        public EngineersTrackingViewHolder(@NonNull View itemView) {
            super(itemView);

            eng = itemView.findViewById(R.id.engineersTracking_row_eng_name);
            system = itemView.findViewById(R.id.engineersTracking_row_system);
            problem = itemView.findViewById(R.id.engineersTracking_row_problem);
            customer = itemView.findViewById(R.id.engineersTracking_row_customer);
            phone = itemView.findViewById(R.id.engineersTracking_row_phone);
            company = itemView.findViewById(R.id.engineersTracking_row_company);
            checkIn = itemView.findViewById(R.id.engineersTracking_row_checkIn);
            checkOut = itemView.findViewById(R.id.engineersTracking_row_checkOut);
            hold = itemView.findViewById(R.id.engineersTracking_row_hold);
            transactionDate = itemView.findViewById(R.id.engineersTracking_row_transTime);
            showDetails = itemView.findViewById(R.id.engineersTracking_row_show_details);
            tableRow = itemView.findViewById(R.id.engineersTracking_row_tableRow);
            childrenLinear = itemView.findViewById(R.id.engineersTracking_row_children_table);
            headerLinear = itemView.findViewById(R.id.engineersTracking_row_children_header);

        }
    }

    public void fillChildData(int index,TableLayout tableLayout) {
//        TableLayout tableLayout = new TableLayout(this);
        TextView eng, system, problem, customer, phone, company, checkIn, checkOut, hold, transactionDate;

        Log.e("size", "" + childList.size());
        for (int i = 0; i < childList.size(); i++) {
            TableRow tableRow = new TableRow(context);
            eng = new TextView(context);
            system = new TextView(context);
            problem = new TextView(context);
            customer = new TextView(context);
            phone = new TextView(context);
            company = new TextView(context);
            checkIn = new TextView(context);
            checkOut = new TextView(context);
            hold = new TextView(context);
            transactionDate = new TextView(context);

            eng.setText(childList.get(i).getEnginerName());
            system.setText(childList.get(i).getSystemName());
            problem.setText(childList.get(i).getProplem());
            customer.setText(childList.get(i).getCustomerName());
            phone.setText(childList.get(i).getPhoneNo());
            company.setText(childList.get(i).getCompanyName());
            checkIn.setText(childList.get(i).getCheakInTime());
            checkOut.setText(childList.get(i).getCheakOutTime());
            hold.setText(childList.get(i).getHoldTime());
            transactionDate.setText(childList.get(i).getTransactionDate());

            if (childList.get(i).getState().equals("1")) {// in service/ check in
                tableRow.setBackgroundColor(context.getResources().getColor(R.color.greenColor2));

            } else if (childList.get(i).getState().equals("2")) {// check out
                tableRow.setBackgroundColor(context.getResources().getColor(R.color.light_blue_over));

            }
            tableLayout.addView(tableRow);
        }

//        adapter.notifyDataSetChanged();
    }
}
