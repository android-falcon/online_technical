package com.falconssoft.onlinetechsupport;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.falconssoft.onlinetechsupport.Modle.CompaneyInfo;
import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_TYPE;
import static com.falconssoft.onlinetechsupport.LoginActivity.sharedPreferences;
import static com.falconssoft.onlinetechsupport.OnlineCenter.EngId;
import static com.falconssoft.onlinetechsupport.OnlineCenter.engInfoTra;
import static com.falconssoft.onlinetechsupport.OnlineCenter.engStringName;
import static com.falconssoft.onlinetechsupport.OnlineCenter.engineerInfoList;
import static com.falconssoft.onlinetechsupport.OnlineCenter.isInHold;
import static com.falconssoft.onlinetechsupport.OnlineCenter.norDanFlag;
import static com.falconssoft.onlinetechsupport.OnlineCenter.text_delet_id;

public class CheckInCompanyAdapter extends  RecyclerView.Adapter<CheckInCompanyAdapter.ViewHolder> {
    //    RecyclerView.Adapter<engineer_adapter.ViewHolder>
    OnlineCenter context;
    List<ManagerLayout> companey;
    List<CompaneyInfo> companeyInfos=new ArrayList<>();
     int row_index=-1;

     ManagerLayout managerLayoutTransfer=new ManagerLayout();
    String ipAdress="";
     DatabaseHandler databaseHandler;
    String convFalg="0";

    public CheckInCompanyAdapter(OnlineCenter context, List<ManagerLayout> companeyInfo) {
        this.context = context;
        this.companey = companeyInfo;
        databaseHandler=new DatabaseHandler(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hold_row, viewGroup, false);
        return new CheckInCompanyAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.cancelButton.setVisibility(View.GONE);
            viewHolder.hold_company_name.setText(companey.get(i).getCompanyName());
            viewHolder.companyTel.setText(companey.get(i).getEnginerName());
            viewHolder.hold_company_time.setText(companey.get(i).getCheakInTime());
            viewHolder.linear_companey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    notifyDataSetChanged();
                     getChoesDialog(companey.get(i));
                     managerLayoutTransfer=companey.get(i);
//                    checkInDialog(companey.get(i));


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


    void checkInDialog(final ManagerLayout managerLayout ){
        final Dialog dialog = new Dialog(context,R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.check_in_dialog);

        RadioGroup radioGroupSolved;
        final RadioButton solved,convert,softConver,doorConvert,contractConvert;

        final LinearLayout convertLinear;

        solved=dialog.findViewById(R.id.SolvedRadioButton);
        convert=dialog.findViewById(R.id.ConvertRadioButton);
        softConver=dialog.findViewById(R.id.SoftwareRadioButton);
        doorConvert=dialog.findViewById(R.id.outDoorButton);
        contractConvert=dialog.findViewById(R.id.contractsRadioButton);

        convertLinear=dialog.findViewById(R.id.ConvertLinear);

        radioGroupSolved=dialog.findViewById(R.id.radioGroupSolved);

        convertLinear.setVisibility(View.GONE);


        solved.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    convFalg="0";
                    convertLinear.setVisibility(View.GONE);
                }

            }
        });


        convert.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
//                    convFalg="1";
                    convertLinear.setVisibility(View.VISIBLE);
                }

            }
        });

        TextView engName=dialog.findViewById(R.id.engName);
        final EditText problem=dialog.findViewById(R.id.online_problem);

        FloatingActionButton addActionButton=dialog.findViewById(R.id.online_add);

        engName.setText(""+managerLayout.getEnginerName());

        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String engName="";
                try {
                     engName=managerLayout.getEnginerName();

                }catch (Exception e){
                    engName="-1";
                }


                if(!solved.isChecked()) {
                    if (softConver.isChecked()) {
                        convFalg = "1";//soft
                    } else if (doorConvert.isChecked()) {
                        convFalg = "2";//door
                    } else if (contractConvert.isChecked()) {
                        convFalg = "3";//con
                    }
                }else {
                    convFalg = "0";//solved
                }


//                Toast.makeText(context, "in Online Add!", Toast.LENGTH_SHORT).show();

                if(!engName.equals("-1")) {
                    if (!TextUtils.isEmpty(problem.getText().toString())) {

                        String problems = problem.getText().toString();
                        managerLayout.setProplem(problems);
                        managerLayout.setConvertFlag(convFalg);
                        Log.e("problemSize", "" + problems.length());


                        if (!isProbablyArabic(problems)){
                            if (problems.length() <= 500) {

                                new UpdateProblemSolved(managerLayout, dialog).execute();

                            } else {
                                Toast.makeText(context, "Max Length of problem 255 Char", Toast.LENGTH_SHORT).show();
                            }
                    }else {
                            if (problems.length() <= 130) {

                                new UpdateProblemSolved(managerLayout, dialog).execute();

                            } else {
                                Toast.makeText(context, "Max Length of problem  130 Char", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please add problem first!", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }


            }
        });


        dialog.show();

    }

    void TransferDialog(final ManagerLayout managerLayout ){
        final Dialog dialog = new Dialog(context,R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.transfer_dialog);

        final Spinner engSpinner=dialog.findViewById(R.id.engSpinner);
        final EditText problem=dialog.findViewById(R.id.online_problem);

        final EditText reason=dialog.findViewById(R.id.online_reason);

//       fillEngineerInfoList(engSpinner);



        FloatingActionButton addActionButton=dialog.findViewById(R.id.online_add);



        fillSpinner(engSpinner);

        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String engName="";
                try {
                    engName=managerLayout.getEnginerName();

                }catch (Exception e){
                    engName="-1";
                }

//                Toast.makeText(context, "in Online Add!", Toast.LENGTH_SHORT).show();

                if(!engName.equals("-1")) {
                    if (!TextUtils.isEmpty(problem.getText().toString())) {
                        if (!TextUtils.isEmpty(reason.getText().toString())) {

                            EngId=Integer.parseInt(managerLayout.getEngId());
                        String problems = problem.getText().toString();
                        String reasons = reason.getText().toString();


                        String engNames = "";
                        String idEng = "";
                        int engIdPos;
                        try {
                            engNames = engSpinner.getSelectedItem().toString();
                            engIdPos = engSpinner.getSelectedItemPosition();
                            idEng = engInfoTra.get(engIdPos).getId();
                            Log.e("Transeng_idtrans", "" + idEng);
                            Log.e("Transeng_name", "" + engNames);

                        } catch (Exception e) {
                            idEng = managerLayout.getEngId();
                        }

                        managerLayout.setEnginerName(engNames);
                        managerLayout.setEngId(idEng);


                        //________________________transData
                        managerLayoutTransfer.setProplem(problems);
                        managerLayoutTransfer.setConvertFlag("4");//transfer to another eng
                        managerLayoutTransfer.setTransferReason(reasons);
                        managerLayoutTransfer.setTransferFlag("1");
                        managerLayoutTransfer.setTransferToEngId(idEng);
                        managerLayoutTransfer.setTransferToEngName(engNames);
                        managerLayoutTransfer.setTransferToSerial("");


                        Log.e("problemSize", "" + problems.length());


                        if (!isProbablyArabic(problems)) {
                            if (problems.length() <= 500) {

//                                new UpdateTransferSolved(managerLayout, dialog).execute();

                                if(!engName.equals(engNames)) {
                                    saveTrans(managerLayout, dialog);
                                }else {
                                    Toast.makeText(context, "Can not Trans To Same Engineer", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(context, "Max Length of problem 255 Char", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (problems.length() <= 130) {

                                if(!engName.equals(engNames)) {
                                    saveTrans(managerLayout, dialog);
                                }else {
                                    Toast.makeText(context, "Can not Trans To Same Engineer", Toast.LENGTH_SHORT).show();
                                }
//                                new UpdateTransferSolved(managerLayout, dialog).execute();

                            } else {
                                Toast.makeText(context, "Max Length of problem  130 Char", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                            Toast.makeText(context, "Please add Reason first!", Toast.LENGTH_SHORT).show();
                    }
                    } else {
                        Toast.makeText(context, "Please add problem first!", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }


            }
        });


        dialog.show();

    }

    void saveTrans(ManagerLayout managerLayout,Dialog dialog){

        JSONObject data = null;
        try {
            data = getData(managerLayout);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("dataTrancfer222 ", "" + data);
        ManagerImport managerImport = new ManagerImport(context);
        managerImport.startSendingData(data, false,8,managerLayoutTransfer,dialog);

    }


    private JSONObject getData(ManagerLayout managerLayout) throws JSONException {
        String time = "", sys_name = "", sys_Id = "";
        String customerName = "", companeyName = "", tele = "";

        customerName = managerLayout.getCustomerName();
        companeyName =  managerLayout.getCompanyName();

//        String phoneFirst="";

//        try{
//            phoneFirst=spinnerPhone.getSelectedItem().toString();
//        }catch (Exception e){
//            phoneFirst="06";
//        }

        tele =  managerLayout.getPhoneNo();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        time = df.format(currentTimeAndDate);
//        sys_name = spenner_systems.getSelectedItem().toString();
//        if(spenner_systems.getCount()!=0){
//            sys_name = spenner_systems.getSelectedItem().toString();
//            Log.e("sys_name",""+sys_name);
//        }
        sys_name= managerLayout.getSystemName();
        sys_Id = managerLayout.getSystemId();

        String engName=managerLayout.getEnginerName();
        String engId=managerLayout.getEngId();


        final String CallId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1");
        final String CallName = LoginActivity.sharedPreferences.getString(LOGIN_NAME, "-1");
        int callType=sharedPreferences.getInt(LOGIN_TYPE, -1);
        Log.e("call_id1",""+CallId+"    "+sys_Id +"    "+ CallName);

        JSONObject obj = new JSONObject();

            obj.put("CUST_NAME", "'" + customerName + "'");
            obj.put("COMPANY_NAME", "'" + companeyName + "'");
            obj.put("SYSTEM_NAME", "'" + sys_name + "'");
            obj.put("PHONE_NO", "'" + tele + "'");
            obj.put("CHECH_IN_TIME", "'" + time + "'");
            obj.put("STATE", "'1'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
            obj.put("ENG_NAME", "'" + engName + "'");
            obj.put("ENG_ID", "'" + engId + "'");
            obj.put("SYS_ID", "'" + sys_Id + "'");
            obj.put("CHECH_OUT_TIME", "'00:00:00'");
            obj.put("PROBLEM", "'problem'");
            obj.put("CALL_CENTER_ID", "'"+CallId+"'");
            obj.put("HOLD_TIME", "'"+"00:00:00"+"'");
            obj.put("DATE_OF_TRANSACTION", "'00/00/00'");
            obj.put("SERIAL", "'"+"222"+"'");
            obj.put("CALL_CENTER_NAME", "'"+CallName+"'");
            obj.put("TRANSFER_FLAG", "'2'");
            obj.put("HOLD_REASON", "''");

        int dangerStatus=0;
        if(managerLayout.getDangerStatus()==1||managerLayout.getDangerStatus()==2||managerLayout.getDangerStatus()==4){

            dangerStatus=1;
        }else {
            dangerStatus=0;
        }
        obj.put("DANGER_STATUS", "'"+dangerStatus+"'");

        if(callType==1) {
            obj.put("TEC_TYPE", "'" + 2 + "'");
        }else if(callType==3) {
            obj.put("TEC_TYPE", "'" + 4 + "'");
        }else if(callType==5) {
            obj.put("TEC_TYPE", "'" + 6 + "'");
        }

        obj.put("COMPANY_ID","'"+managerLayout.getCompanyId()+"'");

           if(managerLayout.getOriginalSerial().equals("-1")||managerLayout.getOriginalSerial().equals("-2")) {
               obj.put("ORGINAL_SERIAL", managerLayout.getSerial());
               Log.e("ORGINAL_SERIAL","getSerial "+managerLayout.getSerial());
               }else{
               obj.put("ORGINAL_SERIAL", managerLayout.getOriginalSerial());
               Log.e("ORGINAL_SERIAL","getOriginalSerial "+managerLayout.getSerial());
           }




        return obj;


    }

    private class UpdateProblemSolved extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        Dialog dialog;
        ManagerLayout managerLayout;

        public UpdateProblemSolved(ManagerLayout managerLayout,Dialog dialog) {
            this.managerLayout = managerLayout;
            this.dialog=dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ipAdress = databaseHandler.getIp();
                String link = "http://" + ipAdress + "//onlineTechnicalSupport/export.php";

                JSONObject object = new JSONObject();
                try {

                    Log.e("problemDataurlString = ", "" + managerLayout.getSerial());

                    object.put("CHECH_OUT_TIME", "00:00:00");
                    object.put("PROBLEM", managerLayout.getProplem());
                    object.put("CUST_NAME", managerLayout.getCustomerName());
                    object.put("CHECH_IN_TIME", managerLayout.getCheakInTime());
                    object.put("COMPANY_NAME", managerLayout.getCompanyName());
                    object.put("PHONE_NO", managerLayout.getPhoneNo());
                    object.put("SYSTEM_NAME", managerLayout.getSystemName());
                    object.put("SYS_ID", managerLayout.getSystemId());
                    object.put("ENG_ID", managerLayout.getEngId());
                    object.put("ENG_NAME", managerLayout.getEnginerName());
                    object.put("STATE", 0);
                    object.put("SERIAL", managerLayout.getSerial());
                    object.put("CONVERT_STATE", managerLayout.getConvertFlag());
                    object.put("LONGITUDE", "0");
                    object.put("LATITUDE", "0");
                    object.put("CHECK_OUT_LONGITUDE", "0");
                    object.put("CHECK_OUT_LATITUDE", "0");
                    object.put("SAVE_PIC", "'0'");
                    object.put("DATE_OF_TRANSACTION", managerLayout.getTransactionDate());
                    object.put("UPDATE_CUSTOMER_LOCATION", "'0'");// 0 check out, 1 update


//                    object.put("CALL_CENTER_ID", "'"+customerOnlineGlobel.getCallId()+"'");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String data = "PROBLEM_SOLVED=" + URLEncoder.encode(object.toString(), "UTF-8");

                URL url = new URL(link);
                Log.e("urlStringProblem= ", "" + url.toString());
                Log.e("urlStringData= ", "" + data);
//                Log.e("serial12344 = ", "" + customerOnlineGlobel.getSerial());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(data);
                wr.flush();
                wr.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "ItemOCodegggppp -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String JsonResponse) {
            super.onPostExecute(JsonResponse);

            if (JsonResponse != null && JsonResponse.contains("PROBLEM_SOLVED SUCCESS")) {
                Log.e("PROBLEM_SOLVED_", "****Success" + JsonResponse.toString());
//                hideCustomerLinear();

                dialog.dismiss();
                context.FillCheckIn();


            } else {

                Log.e("PROBLEM_SOLVED_", "****Failed to export data");

            }


        }

    }




    void getChoesDialog(final ManagerLayout managerLayout){
        final Button itemCard = new Button(context);
        final Button itemSwitch = new Button(context);
        final Button sl = new Button(context);

        itemCard.setText(context.getResources().getString(R.string.check_out_time));
        itemSwitch.setText(context.getResources().getString(R.string.transfer));

        itemCard.setTextColor(Color.WHITE);
        itemSwitch.setTextColor(Color.WHITE);

        if (SweetAlertDialog.DARK_STYLE) {
            itemCard.setTextColor(Color.WHITE);
            itemSwitch.setTextColor(Color.WHITE);
        }

        itemCard.setHovered(true);
        sl.setHeight(14);
        itemCard.setBackground(context.getResources().getDrawable(R.drawable.button_f));
        itemSwitch.setBackground(context.getResources().getDrawable(R.drawable.button_f));
        sl.setBackground(context.getResources().getDrawable(R.drawable.bac_list));
        LinearLayout linearLayout = new LinearLayout(context.getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(itemCard);
        linearLayout.addView( sl);
        linearLayout.addView(itemSwitch);



        final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("")
                .hideConfirmButton();

        dialog.setCustomView(linearLayout);
        dialog.show();


        itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInDialog(managerLayout);
                dialog.dismissWithAnimation();
            }
        });

        itemSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferDialog(managerLayout);
                dialog.dismissWithAnimation();

            }
        });


    }

    public static boolean isProbablyArabic(String s) {//know if char is arabic or eng
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }


   void  fillSpinner(Spinner engSpinner){
       ArrayAdapter <String> spinnerEngAdapter=new ArrayAdapter<String>(context, R.layout.spinner_text, engStringName);
//       spinnerEngAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       engSpinner.setAdapter(spinnerEngAdapter);
//
   }

}