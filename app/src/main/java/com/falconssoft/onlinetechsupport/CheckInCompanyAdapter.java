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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.CompaneyInfo;
import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
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
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;
import static com.falconssoft.onlinetechsupport.LoginActivity.sharedPreferences;
import static com.falconssoft.onlinetechsupport.OnlineCenter.engineerInfoList;
import static com.falconssoft.onlinetechsupport.OnlineCenter.isInHold;
import static com.falconssoft.onlinetechsupport.OnlineCenter.text_delet_id;

public class CheckInCompanyAdapter extends  RecyclerView.Adapter<CheckInCompanyAdapter.ViewHolder> {
    //    RecyclerView.Adapter<engineer_adapter.ViewHolder>
    OnlineCenter context;
    List<ManagerLayout> companey;
    List<CompaneyInfo> companeyInfos=new ArrayList<>();
     int row_index=-1;

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

            viewHolder.hold_company_name.setText(companey.get(i).getCompanyName());
            viewHolder.companyTel.setText(companey.get(i).getEnginerName());
            viewHolder.hold_company_time.setText(companey.get(i).getCheakInTime());
            viewHolder.linear_companey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    notifyDataSetChanged();

                    checkInDialog(companey.get(i));

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
        TextView green, hold_company_time,hold_company_name,companyTel;
        LinearLayout linear_companey;

        public ViewHolder(View itemView) {
            super(itemView);

            hold_company_name=itemView.findViewById(R.id.hold_company_name);
            companyTel=itemView.findViewById(R.id.hold_company_tel);
            hold_company_time=itemView.findViewById(R.id.hold_company_time);
            linear_companey=itemView.findViewById(R.id.linear_companey);


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


                Toast.makeText(context, "in Online Add!", Toast.LENGTH_SHORT).show();

                if(!engName.equals("-1")) {
                    if (!TextUtils.isEmpty(problem.getText().toString())) {

                        managerLayout.setProplem(problem.getText().toString());
                        managerLayout.setConvertFlag(convFalg);

                      new   UpdateProblemSolved(managerLayout,dialog).execute();
                    } else {
                        Toast.makeText(context, "Please add problem first!", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }


            }
        });


        dialog.show();

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



}