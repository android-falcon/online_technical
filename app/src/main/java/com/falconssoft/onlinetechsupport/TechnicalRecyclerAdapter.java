package com.falconssoft.onlinetechsupport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.CompaneyInfo;
import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;

import java.util.List;

public class TechnicalRecyclerAdapter extends RecyclerView.Adapter<TechnicalRecyclerAdapter.TechnicalViewHolder> {

    private List<CustomerOnline> list;
    private TechnicalActivity technicalActivity;

    public TechnicalRecyclerAdapter(List<CustomerOnline> list, TechnicalActivity technicalActivity) {
        this.list = list;
        this.technicalActivity = technicalActivity;
    }

    @NonNull
    @Override
    public TechnicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.technical_customers_row, parent, false);
        return new TechnicalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicalViewHolder holder, final int i) {
        holder.company.setText(list.get(i).getCompanyName());
        holder.customer.setText(list.get(i).getCustomerName());
        holder.phone.setText(list.get(i).getPhoneNo());
        holder.problem.setText(list.get(i).getProblem());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                technicalActivity.getLocation(list.get(i), true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TechnicalViewHolder extends RecyclerView.ViewHolder{
        TextView company, customer, phone, problem;

        public TechnicalViewHolder(@NonNull View itemView) {
            super(itemView);

            company = itemView.findViewById(R.id.technicalRow_company);
            customer = itemView.findViewById(R.id.technicalRow_customer);
            phone = itemView.findViewById(R.id.technicalRow_phone);
            problem = itemView.findViewById(R.id.technicalRow_problem);

        }
    }
}
