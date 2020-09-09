package com.falconssoft.onlinetechsupport;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.CompaneyInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.falconssoft.onlinetechsupport.OnlineCenter.companey_name;
import static com.falconssoft.onlinetechsupport.OnlineCenter.customer_name;
import static com.falconssoft.onlinetechsupport.OnlineCenter.engineerInfoList;
import static com.falconssoft.onlinetechsupport.OnlineCenter.isInHold;
import static com.falconssoft.onlinetechsupport.OnlineCenter.systype;
import static com.falconssoft.onlinetechsupport.OnlineCenter.telephone_no;
import static com.falconssoft.onlinetechsupport.OnlineCenter.text_delet_id;

public class holdCompanyAdapter extends  RecyclerView.Adapter<holdCompanyAdapter.ViewHolder> {
    //    RecyclerView.Adapter<engineer_adapter.ViewHolder>
    Context context;
    List<ManagerLayout> companey;
    CompaneyInfo clickedcom;
    List<CompaneyInfo> companeyInfos=new ArrayList<>();
     int row_index=-1,rowEng=-1;
     String selectedEngineer="";
     String selectedId="";

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
                    row_index=i;
                    notifyDataSetChanged();


                    clickedcom=new CompaneyInfo();
                    dialogEngineering(context);
//                    clickedcom.setCompanyName(viewHolder.hold_company_name.getText().toString());
//                    clickedcom.setPhoneNo(viewHolder.companyTel.getText().toString());
//                    companey_name.setText(viewHolder.hold_company_name.getText().toString());
//                    telephone_no.setText(viewHolder.companyTel.getText().toString());
//                    customer_name.setText(companey.get(i).getCustomerName());
//                    systype.setText(companey.get(i).getSystemName());
                    Log.e("systemSelected",""+companey.get(i).getSystemName());
                    Log.e("text_delet_id",""+i);
                    text_delet_id.setText(i+"");
                    //companey.remove(i);
                   // Log.e("companysize",""+companey.size());
                    isInHold=true;
                    companeyInfos.add(clickedcom);
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


    private void dialogEngineering(final Context context1) {

       final Dialog dialog = new Dialog(context1);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.engeneering_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        ArrayList<String> nameOfEngi=new ArrayList<>();
        final ListView listEngennering = dialog.findViewById(R.id.listViewEngineering);
        if(engineerInfoList.size()!=0)
        {
            for(int i=0;i<engineerInfoList.size();i++)
            {
//                nameOfEngi.add("tahani");
                nameOfEngi.add(engineerInfoList.get(i).getName());
            }
            Log.e("nameOfEngi",""+nameOfEngi.size());

//                    simple_list_item_1 simple_list_item_activated_1
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, nameOfEngi);
            listEngennering.setAdapter(itemsAdapter);
        }
        listEngennering.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rowEng=position;
                listEngennering.requestFocusFromTouch();
                listEngennering.setSelection(position);
                selectedId=engineerInfoList.get(position).getId();
                selectedEngineer=engineerInfoList.get(position).getName();

//               Log.e( "getSelectedItem",""+listEngennering.getSelectedItem().toString());
                Log.e( "getItemsCanFocus",""+listEngennering.getItemsCanFocus());
                Log.e("position",""+position+"\t"+selectedId);

            }
        });


        Button btn_send = dialog.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rowEng==-1)
                {
                    Toast.makeText(context, "Select Engineer", Toast.LENGTH_SHORT).show();

                }
                else {
                    OnlineCenter onlineCenter=new OnlineCenter();

                    CompaneyInfo companeyInfo = new CompaneyInfo();

                    String sys_Id = onlineCenter.getSystemId(companey.get(row_index).getSystemName());

                    companeyInfo.setCustomerName(companey.get(row_index).getCustomerName());
                    companeyInfo.setCompanyName(companey.get(row_index).getCompanyName());
                    companeyInfo.setSystemName(companey.get(row_index).getSystemName());
                    companeyInfo.setPhoneNo(companey.get(row_index).getPhoneNo());
                    companeyInfo.setCheakInTime(companey.get(row_index).getCheakInTime());
                    companeyInfo.setState_company("1");
                    companeyInfo.setEngName(selectedEngineer);
                    companeyInfo.setEngId(String.valueOf(selectedId));
                    companeyInfo.setSystemId(sys_Id);
                    companeyInfo.setChechout(companey.get(row_index).getCheakOutTime());
                    companeyInfo.setproblem(companey.get(row_index).getProplem());
                    companeyInfo.setSerial(companey.get(row_index).getSerial());

                    Log.e("HOLDp", "UPDATE=-->" + companeyInfo.getSystemName().toString()+"\t"+companey.get(row_index).getCustomerName());
                    Log.e("HOLDp1", "UPDATE=-->" + companeyInfo.getSerial().toString());

                    JSONObject data = null;
                    try {
                        data = companeyInfo.getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("HOLDp", "" + data);
                    ManagerImport managerImport = new ManagerImport(context1);
                    managerImport.startUpdateHold(data);


                    isInHold=false;

                    notifyDataSetChanged();
                    dialog.dismiss();

                }







            }
        });
        dialog.show();

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
/*       viewHolder.linear_companey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index=i;
                    notifyDataSetChanged();


                    clickedcom=new CompaneyInfo();
                    clickedcom.setCompanyName(viewHolder.hold_company_name.getText().toString());
                    clickedcom.setPhoneNo(viewHolder.companyTel.getText().toString());
                    companey_name.setText(viewHolder.hold_company_name.getText().toString());
                    telephone_no.setText(viewHolder.companyTel.getText().toString());
                    customer_name.setText(companey.get(i).getCustomerName());
                    systype.setText(companey.get(i).getSystemName());
                    Log.e("systemSelected",""+companey.get(i).getSystemName());
                    Log.e("text_delet_id",""+i);
                    text_delet_id.setText(i+"");
                    //companey.remove(i);
                   // Log.e("companysize",""+companey.size());
                    isInHold=true;
                    companeyInfos.add(clickedcom);
                }

            });
            */
