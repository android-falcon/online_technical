package com.falconssoft.onlinetechsupport;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;
import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;
import static com.falconssoft.onlinetechsupport.LoginActivity.editor;
import static com.falconssoft.onlinetechsupport.LoginActivity.sharedPreferences;

//import android.support.design.widget.Snackbar;


public class OnlineActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout coordinatorLayout;
    private CircleImageView profilePicture, onlineImage;
    private ImageView breakButton, exitButton;
    private GifImageView gifImageView;
    private CircularView.OptionsBuilder builder;
    //    private CircleTimeView timer;
    private EditText problem;
    private TextView phoneNo, username;
    public static TextView  system;
    private Button exitBreak, new_customer;
    private FloatingActionButton addactionButton;
    private Snackbar snackbar;
    private Animation animation;
    private PresenterClass presenterClass;
    private LinearLayout customerLayout;
    private Date currentTime;
    CustomerOnline customerOnlineGlobel;
    Button online_new_customer;
    String ipAdress = "";
    DatabaseHandler databaseHandler;
    public SharedPreferences onlineSharedPreferences;
    public SharedPreferences.Editor onlineEditor;
    public static final String ONLINE_PREFERNCES = "login_preferences";
    public static final String ONLINE_CHECH_IN_TIME = "check_in_time";
    public static final String ONLINE_COMPANY_NAME = "company_name";
    public static final String ONLINE_CUST_NAME = "customer";
    public static final String ONLINE_PHONE_NO = "phone";
    public static final String ONLINE_SYSTEM_NAME = "system_name";
    public static final String ONLINE_SYS_ID = "system_id";
    public static final String ONLINE_PROBLEM = "problem";
    public static final String ONLINE_CHECH_OUT_TIME = "check_out_time";
    List <Systems>systemsListActivity;
    Timer timer;
    //     MediaPlayer mp;
    Animation animations;
    public static boolean isTimerWork = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        presenterClass = new PresenterClass(this, 0);
        onlineSharedPreferences = getSharedPreferences(ONLINE_PREFERNCES, Context.MODE_PRIVATE);
        profilePicture = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.online_settings);
        breakButton = findViewById(R.id.online_break);
        exitButton = findViewById(R.id.online_exit);
        coordinatorLayout = findViewById(R.id.online_cordinator_layout);
        addactionButton = findViewById(R.id.online_add);
        gifImageView = findViewById(R.id.online_gif_image);
        customerLayout = findViewById(R.id.online_customer_layout);
        new_customer = findViewById(R.id.online_new_customer);
        onlineImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.online_image_customer);
        phoneNo = findViewById(R.id.online_phone);
        system = findViewById(R.id.online_system);
        username = findViewById(R.id.online_username);
        problem = findViewById(R.id.online_problem);
        online_new_customer = findViewById(R.id.online_new_customer);
        systemsListActivity=new ArrayList<>();

        system.setMovementMethod(new ScrollingMovementMethod());
        fillEngineerInfoList();

        system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                systemGridDialog(systemsListActivity);
            }
        });

//        phoneNo = findViewById(R.id.online_image_phone);
//        system = findViewById(R.id.online_image_system);

//        animation = AnimationUtils.loadAnimation(this, R.anim.zoom);
//        customerName.startAnimation(animation);
//
//        animation = AnimationUtils.loadAnimation(this, R.anim.zoom);
//        animation.setStartOffset(20);
//        phoneNo.startAnimation(animation);
//
//        animation = AnimationUtils.loadAnimation(this, R.anim.zoom);
//        animation.setStartOffset(30);
//        system.startAnimation(animation);

        databaseHandler = new DatabaseHandler(OnlineActivity.this);
        new_customer.setOnClickListener(this);
        addactionButton.setOnClickListener(this);
        breakButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);


//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                presenterClass.getCustomersData("ifFound");
//            }
//
//        }, 0, 1000);

        timerWork();
//         mp = MediaPlayer.create(this, R.raw.sound);

    }

    private void timerWork() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                presenterClass.getCustomersData("ifFound");

            }

        }, 0, 1000);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.online_new_customer:
//                if(timer != null) {
//                    timer.cancel();
//                    timer.purge();
//                    timer = null;
//                }
                isTimerWork = false;
//                animations.cancel();
                online_new_customer.clearAnimation();
                presenterClass.getCustomersData("get");
//                timer.setCurrentTime(20000);
//                start circular view to rotate
//                // pause circular view and timer
//                if(timer.pauseTimer())
//                {
//                    //Timer Paused
//                }
//
//                // resume circular view and timer
//                timer.resumeTimer();

                // stop circular view and timer
                break;
            case R.id.online_add:
//                timer.stopTimer();
//                isTimerWork = true;

                if (customerLayout.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(problem.getText().toString())) {

//                        currentTime = Calendar.getInstance().getTime();
                        Log.e("time", "" + currentTime);
                        onlineEditor = onlineSharedPreferences.edit();
                        onlineEditor.putString(ONLINE_PROBLEM, "" + problem.getText());
                        onlineEditor.putString(ONLINE_CHECH_OUT_TIME, "" + "00:00:00");
                        onlineEditor.commit();

                        CustomerOnline customerOnline = new CustomerOnline();
                        customerOnline.setProblem("" + problem.getText());
                        customerOnline.setCustomerName("" + username.getText());
                        customerOnline.setPhoneNo("" + phoneNo.getText());
                        customerOnline.setCheakInTime(onlineSharedPreferences.getString(ONLINE_CHECH_IN_TIME, null));
                        customerOnline.setCompanyName(onlineSharedPreferences.getString(ONLINE_COMPANY_NAME, null));
                        customerOnline.setSystemName(onlineSharedPreferences.getString(ONLINE_SYSTEM_NAME, null));
                        customerOnline.setSystemId(onlineSharedPreferences.getString(ONLINE_SYS_ID, null));
                        customerOnline.setEngineerID(sharedPreferences.getString(LOGIN_ID, null));
                        customerOnline.setEngineerName(sharedPreferences.getString(LOGIN_NAME, null));
                        customerOnline.setCheakOutTime(onlineSharedPreferences.getString(ONLINE_CHECH_OUT_TIME, null));
                        customerOnline.setCustomerState(2);

                        customerOnlineGlobel = new CustomerOnline();
                        customerOnlineGlobel = customerOnline;
                        new SyncManagerLayoutIN().execute();


//                        presenterClass.pushCustomerProblem(customerOnline, 0);// check out
//                        final String engId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
//                        presenterClass.setState(engId, 0);// checkout
                    } else {
                        Toast.makeText(this, "Please add problem first!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "please add customer first!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.online_break:

                new SweetAlertDialog(OnlineActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Break")
                        .setContentText("Are you sure?!")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                profilePicture.setBorderColor(getResources().getColor(R.color.yellowf));
                                final Dialog dialog = new Dialog(OnlineActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.break_dialog);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                final String engId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
                                presenterClass.setState(engId, 2);// break

                                exitBreak = dialog.findViewById(R.id.breakDialog_exit);
                                exitBreak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        profilePicture.setBorderColor(getResources().getColor(R.color.greenf));

                                        presenterClass.setState(engId, 0);// back break
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                                sDialog.dismissWithAnimation();
                            }

                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

                break;
            case R.id.online_exit:

                new SweetAlertDialog(OnlineActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Exit")
                        .setContentText("Are you sure?!")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
//
                                profilePicture.setBorderColor(getResources().getColor(R.color.redf));
                                final String engIds = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
                                presenterClass.setState(engIds, -1);//exit
                                finish();
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

                break;
        }
    }

    public void ShowNotification() {
        notifyThis("Master", "belcin");
        blinking();
    }

    public void StopNotification() {
//        notifyThis("Master","belcin");
//        mp.stop();
    }


    public void  systemGridDialog(final List<Systems> listOfsystem){

        final Dialog fillSysDialog = new Dialog(OnlineActivity.this,R.style.Theme_Dialog);
        fillSysDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fillSysDialog.setCancelable(true);
        fillSysDialog.setContentView(R.layout.sys_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        final GridView SysGrid;

        SysGrid=fillSysDialog.findViewById(R.id.Sysgrid);
//        SysGrid

        adapterGridSystem adapterSystem = new adapterGridSystem(this, listOfsystem);
        SysGrid.setAdapter(adapterSystem);

        SysGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( !system.getText().toString().contains(listOfsystem.get(position).getSystemName())) {
                    system.setText(system.getText().toString() + "," + listOfsystem.get(position).getSystemName());
                }else{
                    Toast.makeText(OnlineActivity.this, "This System ADD Before", Toast.LENGTH_SHORT).show();
                }
                fillSysDialog.dismiss();

            }
        });

        fillSysDialog.show();
    }


    void blinking() {

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        online_new_customer.startAnimation(anim);
        animations = anim;
    }

    public void showCustomerLinear(CustomerOnline customerOnline) {
        new_customer.setVisibility(View.GONE); // button
        onlineImage.setVisibility(View.GONE); // image
        customerLayout.setVisibility(View.VISIBLE); // customer fields
        gifImageView.setVisibility(View.VISIBLE); // timer

        Log.e("trrrr", "master");
        phoneNo.setText(customerOnline.getPhoneNo());
        system.setText(customerOnline.getSystemName());
        username.setText(customerOnline.getCustomerName());

        onlineEditor = onlineSharedPreferences.edit();
        onlineEditor.putString(ONLINE_CHECH_IN_TIME, customerOnline.getCheakInTime());
        onlineEditor.putString(ONLINE_COMPANY_NAME, customerOnline.getCompanyName());
        onlineEditor.putString(ONLINE_CUST_NAME, customerOnline.getCustomerName());
        onlineEditor.putString(ONLINE_PHONE_NO, customerOnline.getPhoneNo());
        onlineEditor.putString(ONLINE_SYSTEM_NAME, customerOnline.getSystemName());
        onlineEditor.putString(ONLINE_SYS_ID, customerOnline.getSystemId());
        onlineEditor.commit();

    }

    public void hideCustomerLinear() {
        final String engId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
        presenterClass.setState(engId, 0);// checkout
        snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#3167F0\">Checked out Successfully</font>"), Snackbar.LENGTH_SHORT);
        View snackbarLayout = snackbar.getView();
        TextView textViewSnackbar = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        textViewSnackbar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
        textViewSnackbar.setCompoundDrawablePadding(15);
        snackbar.show();
        phoneNo.setText("");
        username.setText("");
        system.setText("");
        problem.setText("");
        new_customer.setVisibility(View.VISIBLE);
        onlineImage.setVisibility(View.VISIBLE);
        customerLayout.setVisibility(View.GONE);
        gifImageView.setVisibility(View.GONE);
        isTimerWork = true;
    }

    public void notifyThis(String title, String message) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(OnlineActivity.this);
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.icon2_f);
//                .setWhen(System.currentTimeMillis())
//                .setTicker("{your tiny message}")
//                .setContentTitle(title)
//                .setContentText(message)
//                .setContentInfo("INFO");

        NotificationManager nm = (NotificationManager) OnlineActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());
    }


    private void fillEngineerInfoList() {
//        if(TextUtils.isEmpty(ipAddres)){
//            Toast.makeText(this, "ip Not Found,Please Add Ip", Toast.LENGTH_SHORT).show();
//        }
        databaseHandler=new DatabaseHandler(OnlineActivity.this);
        String ipAddres=databaseHandler.getIp();
        final String url = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                                try{
                                    JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
                                    Log.e("systemInfoArray", "" + systemInfoArray);
                                    for (int i = 0; i < systemInfoArray.length(); i++) {
                                        JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
                                        Systems systemInfo = new Systems();
                                        systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
                                        systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));
                                        systemsListActivity.add(systemInfo);
                                        Log.e("textM","!!text"+systemsListActivity.get(i).getSystemName());
                                    }
                                }catch (Exception e){
                                    Log.e("No_Sys       ","Exception");

                                }
//                                fillSpennerSystem(systemsList);
//                                systemGridDialog(systemsList);



                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((error instanceof NoConnectionError)) {
                    Toast.makeText(OnlineActivity.this,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                }

                Log.e("onErrorResponse: ", "" + error);
            }

        });
        MySingeltone.getmInstance(OnlineActivity.this).addToRequestQueue(stringRequest);


    }


    private class SyncManagerLayoutIN extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ipAdress = databaseHandler.getIp();
                String link = "http://" + ipAdress + "//onlineTechnicalSupport/export.php";
                // ITEM_CARD

//                JSONObject obj = new JSONObject();
//                JSONArray NEWI=new JSONArray();

                //                    obj.put("CUST_NAME", "'Eng Tahani'");
//                    obj.put("COMPANY_NAME", "'Falcons'");
//                    obj.put("SYSTEM_NAME", "'Accounting'");
//                    obj.put("PHONE_NO", "'015454'");
//                    obj.put("CHECH_IN_TIME", "'03:30'");
//                    obj.put("STATE", "'1'");
//                    obj.put("ENG_NAME", "'ENG.RAWAN'");

//                NEWI.put(datatoSend);
                JSONObject object = new JSONObject();
                try {
//                    object.put("CHECH_OUT_TIME", "00:00:00");
//                    object.put("PROBLEM", "aNDROID 100");
//                    object.put("CUST_NAME", "FALCONS");
//                    object.put("CHECH_IN_TIME", "hjh");
//                    object.put("COMPANY_NAME", "hjh");
//                    object.put("PHONE_NO", "hjh");
//                    object.put("SYSTEM_NAME", "hjh");
//                    object.put("SYS_ID", "hjh");
//                    object.put("ENG_ID", "2");
//                    object.put("ENG_NAME", "hjh");
//                    object.put("STATE", "2");
                    Log.e("problemDataurlString = ", "" + customerOnlineGlobel.getProblem());

                    object.put("CHECH_OUT_TIME", "00:00:00");
                    object.put("PROBLEM", customerOnlineGlobel.getProblem());
                    object.put("CUST_NAME", customerOnlineGlobel.getCustomerName());
                    object.put("CHECH_IN_TIME", customerOnlineGlobel.getCheakInTime());
                    object.put("COMPANY_NAME", customerOnlineGlobel.getCompanyName());
                    object.put("PHONE_NO", customerOnlineGlobel.getPhoneNo());
                    object.put("SYSTEM_NAME", customerOnlineGlobel.getSystemName());
                    object.put("SYS_ID", customerOnlineGlobel.getSystemId());
                    object.put("ENG_ID", customerOnlineGlobel.getEngineerID());
                    object.put("ENG_NAME", customerOnlineGlobel.getEngineerName());
                    object.put("STATE", 2);
//                    object.put("CALL_CENTER_ID", "'"+customerOnlineGlobel.getCallId()+"'");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String data = "PROBLEM_SOLVED=" + URLEncoder.encode(object.toString(), "UTF-8");

                URL url = new URL(link);
                Log.e("urlStringProblem= ", "" + url.toString());
                Log.e("urlStringData= ", "" + data);

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
                hideCustomerLinear();

            } else {

                Log.e("PROBLEM_SOLVED_", "****Failed to export data");

            }


        }

    }
}


