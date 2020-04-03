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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class holdCompanyAdapter extends  RecyclerView.Adapter<holdCompanyAdapter.ViewHolder> {
    //    RecyclerView.Adapter<engineer_adapter.ViewHolder>
    Context context;
    List<CompaneyInfo> companey;


//public static List<Cheque> chequeListall;

    public holdCompanyAdapter(Context context, List<CompaneyInfo> companeyInfo) {
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
//
//        if (i != 0) {
            // viewHolder.linear_companey.setBackgroundColor(Color.parseColor("#F5F1F5F5"));

            viewHolder.hold_company_name.setText(companey.get(i).getCompanyName());
            viewHolder.companyTel.setText(companey.get(i).getPhoneNo());
//        }


    }

    @Override
    public int getItemCount() {
        return companey.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView green, textView_Nmae,hold_company_name,companyTel;
        CircleImageView profile_image;
        LinearLayout linear_companey;

        public ViewHolder(View itemView) {
            super(itemView);
            //linear_companey = itemView.findViewById(R.id.linear_companey);
            hold_company_name=itemView.findViewById(R.id.hold_company_name);
            companyTel=itemView.findViewById(R.id.hold_company_tel);


        }
    }
}
