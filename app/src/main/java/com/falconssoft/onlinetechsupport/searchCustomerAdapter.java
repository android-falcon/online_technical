package com.falconssoft.onlinetechsupport;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.CompaneyInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;
import static com.falconssoft.onlinetechsupport.OnlineCenter.EngId;
import static com.falconssoft.onlinetechsupport.OnlineCenter.companey_name;
import static com.falconssoft.onlinetechsupport.OnlineCenter.customer_name;
import static com.falconssoft.onlinetechsupport.OnlineCenter.engInfoTra;
import static com.falconssoft.onlinetechsupport.OnlineCenter.engStringName;
import static com.falconssoft.onlinetechsupport.OnlineCenter.isShow;
import static com.falconssoft.onlinetechsupport.OnlineCenter.recyclerk;

public class searchCustomerAdapter extends  RecyclerView.Adapter<searchCustomerAdapter.ViewHolder> {
    //    RecyclerView.Adapter<engineer_adapter.ViewHolder>
    OnlineCenter context;
    List<ManagerLayout> companey;
    List<CompaneyInfo> companeyInfos=new ArrayList<>();
     int row_index=-1;

     EditText editText;
     ManagerLayout managerLayoutTransfer=new ManagerLayout();
     DatabaseHandler databaseHandler;


    public searchCustomerAdapter(OnlineCenter context, List<ManagerLayout> companeyInfo,EditText editText) {
        this.context = context;
        this.companey = companeyInfo;
        databaseHandler=new DatabaseHandler(context);
        this.editText=editText;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hold_row, viewGroup, false);
        return new searchCustomerAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.cancelButton.setVisibility(View.GONE);
            viewHolder.hold_company_name.setText(companey.get(i).getCompanyName());
            viewHolder.companyTel.setText(companey.get(i).getPhoneNo());
            viewHolder.hold_company_time.setText(companey.get(i).getCustomerName());
            viewHolder.linear_companey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   isShow=1;
                    editText.setText(companey.get(i).getPhoneNo());
                    customer_name.setText(companey.get(i).getCustomerName());
                    companey_name.setText(companey.get(i).getCompanyName());
                    recyclerk.setVisibility(View.GONE);

                }

            });
            if(row_index==i)
            {
                viewHolder.linear_companey.setBackgroundColor(Color.parseColor("#e5e4e2"));

            }
            else {
                viewHolder.linear_companey.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }


    }


    @Override
    public int getItemCount() {
        return companey.size();
    }
    public List<CompaneyInfo> getCheckedItems() {
        return companeyInfos;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView green, hold_company_time,hold_company_name,companyTel,cancelButton;
        LinearLayout linear_companey;

        public ViewHolder(View itemView) {
            super(itemView);

            hold_company_name=itemView.findViewById(R.id.hold_company_name);
            companyTel=itemView.findViewById(R.id.hold_company_tel);
            hold_company_time=itemView.findViewById(R.id.hold_company_time);
            linear_companey=itemView.findViewById(R.id.linear_companey);
            cancelButton=itemView.findViewById(R.id.cancelButton);


        }
    }



}