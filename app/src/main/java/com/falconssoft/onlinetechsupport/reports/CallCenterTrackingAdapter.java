package com.falconssoft.onlinetechsupport.reports;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.ManagerExport;
import com.falconssoft.onlinetechsupport.ManagerImport;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.OnlineCenter;
import com.falconssoft.onlinetechsupport.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class CallCenterTrackingAdapter extends RecyclerView.Adapter<CallCenterTrackingAdapter.CallCenterTrackingViewHolder> {

    private List<ManagerLayout> list;
    private Context context;
    private String tecType;

    public CallCenterTrackingAdapter(Context context, List<ManagerLayout> list,String tecType) {
        this.list = list;
        this.context = context;
        this.tecType=tecType;
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


        holder.problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.get(i).getState().equals("2")){

                    final Dialog updateDialog = new Dialog(context,R.style.Theme_Dialog);
                    updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    updateDialog.setCancelable(true);
                    updateDialog.setContentView(R.layout.problem_dialog);
                    updateDialog.setCanceledOnTouchOutside(true);

                    FloatingActionButton addList = updateDialog.findViewById(R.id.hold_reason);
                    final EditText newProblem = updateDialog.findViewById(R.id.holdEdit_reason);
                    newProblem.setText(list.get(i).getProplem()+"");

                    addList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!TextUtils.isEmpty(newProblem.getText().toString())) {
                                list.get(i).setProplem(newProblem.getText().toString());
                                ManagerExport managerExport=new ManagerExport(context,null);
                                if(tecType.equals("2")) {
                                    managerExport.UpdateProblemFun(list.get(i), newProblem.getText().toString(), "2");
                                }else  if(tecType.equals("4")) {
                                    managerExport.UpdateProblemFun(list.get(i), newProblem.getText().toString(), "4");

                                }else  if(tecType.equals("6")) {
                                    managerExport.UpdateProblemFun(list.get(i), newProblem.getText().toString(), "6");

                                }
                                updateDialog.dismiss();
                            } else {

                                Toast.makeText(context, "Please add Reason ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                    updateDialog.show();
//                    Toast.makeText(context, i+"problem "+list.get(i).getProplem(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, i+"Can Not Change Problem", Toast.LENGTH_SHORT).show();
                }

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

        TextView customer,company,phone,system,eng,time,states,titel,problem,callCenterName,stateOfSolve,dateOfTran,hold_reason;
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
        stateOfSolve=dialog.findViewById(R.id.stateOfSolve);
        dateOfTran=dialog.findViewById(R.id.dateOfTran);
        hold_reason=dialog.findViewById(R.id.hold_reason);
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
        String sys=itemsList.getSystemName().replace("\\n"," ");
        system.setText(sys);
        eng.setText("" + itemsList.getEnginerName());
        time.setText("" + itemsList.getCheakInTime());
        states.setText(statesString);
        problem.setText("" + itemsList.getProplem());
        callCenterName.setText(""+itemsList.getCallCenterName());
        dateOfTran.setText(""+itemsList.getTransactionDate());
        hold_reason.setText(""+itemsList.getHoldReason());

        String solveState="";
        int stateProblem=Integer.parseInt(itemsList.getConvertFlag());
        switch (stateProblem){
            case 0:
                solveState=context.getResources().getString(R.string.solved);
                break;
            case 1:
                solveState=context.getResources().getString(R.string.convert_to_software_department);
                break;
            case 2:
                solveState=context.getResources().getString(R.string.convert_to_out_door);
                break;
            case 3:
                solveState=context.getResources().getString(R.string.convert_to_contracts_department);
                break;

            case 4:
                solveState=context.getResources().getString(R.string.transfer_to_another_engineer);
                break;

        }

        stateOfSolve.setText(""+solveState);

//                    Toast.makeText(context, "main "+ holder.EngName.getText().toString(), Toast.LENGTH_SHORT).show();

        dialog.show();


    }
}
