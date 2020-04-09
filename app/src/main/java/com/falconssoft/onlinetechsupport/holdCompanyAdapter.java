package com.falconssoft.onlinetechsupport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.CompaneyInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.falconssoft.onlinetechsupport.OnlineCenter.companey_name;
import static com.falconssoft.onlinetechsupport.OnlineCenter.customer_name;
import static com.falconssoft.onlinetechsupport.OnlineCenter.telephone_no;

public class holdCompanyAdapter extends  RecyclerView.Adapter<holdCompanyAdapter.ViewHolder> {
    //    RecyclerView.Adapter<engineer_adapter.ViewHolder>
    Context context;
    List<ManagerLayout> companey;
    CompaneyInfo clickedcom;
    List<CompaneyInfo> companeyInfos=new ArrayList<>();

    public holdCompanyAdapter(Context context, List<ManagerLayout> companeyInfo) {
        this.context = context;
        this.companey = companeyInfo;
        Log.e("holdCompanyAdapter",""+companey.size());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hold_row, viewGroup, false);
        return new holdCompanyAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

            viewHolder.hold_company_name.setText(companey.get(i).getCompanyName());
            viewHolder.companyTel.setText(companey.get(i).getPhoneNo());
            viewHolder.hold_company_time.setText(companey.get(i).getCheakInTime());
            viewHolder.linear_companey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedcom=new CompaneyInfo();
                    clickedcom.setCompanyName(viewHolder.hold_company_name.getText().toString());
                    clickedcom.setPhoneNo(viewHolder.companyTel.getText().toString());
                    companey_name.setText(viewHolder.hold_company_name.getText().toString());
                    telephone_no.setText(viewHolder.companyTel.getText().toString());
                    customer_name.setText("mohemmed");
                    companeyInfos.add(clickedcom);
                }
            });
    }

    @Override
    public int getItemCount() {
        return companey.size();
    }
    public List<CompaneyInfo> getCheckedItems() {
        return companeyInfos;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView green, hold_company_time,hold_company_name,companyTel;
        CircleImageView profile_image;
        LinearLayout linear_companey;

        public ViewHolder(View itemView) {
            super(itemView);
            //linear_companey = itemView.findViewById(R.id.linear_companey);
            hold_company_name=itemView.findViewById(R.id.hold_company_name);
            companyTel=itemView.findViewById(R.id.hold_company_tel);
            hold_company_time=itemView.findViewById(R.id.hold_company_time);
            linear_companey=itemView.findViewById(R.id.linear_companey);


        }
    }
}
