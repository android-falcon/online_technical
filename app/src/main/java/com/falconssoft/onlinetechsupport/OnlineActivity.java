package com.falconssoft.onlinetechsupport;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;

//import android.support.design.widget.Snackbar;


public class OnlineActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout coordinatorLayout;
    private CircleImageView profilePicture, onlineImage;
    private ImageView breakButton, exitButton;
    private CircularView timer;
    private CircularView.OptionsBuilder builder;
//    private CircleTimeView timer;
    private EditText problem;
    private TextView phoneNo, system, username;
    private Button exitBreak, new_customer;
    private FloatingActionButton addactionButton;
    private Snackbar snackbar;
    private Animation animation;
    private PresenterClass presenterClass;
    private LinearLayout customerLayout;
    private Date currentTime;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        presenterClass = new PresenterClass(this, 0);
        onlineSharedPreferences = getSharedPreferences(ONLINE_PREFERNCES, Context.MODE_PRIVATE);
        profilePicture = findViewById(R.id.online_settings);
        breakButton = findViewById(R.id.online_break);
        exitButton = findViewById(R.id.online_exit);
        coordinatorLayout = findViewById(R.id.online_cordinator_layout);
        addactionButton = findViewById(R.id.online_add);
        timer = findViewById(R.id.online_timer);
        customerLayout = findViewById(R.id.online_customer_layout);
        new_customer = findViewById(R.id.online_new_customer);
        onlineImage = findViewById(R.id.online_image_customer);
        phoneNo = findViewById(R.id.online_phone);
        system = findViewById(R.id.online_system);
        username = findViewById(R.id.online_username);
        problem = findViewById(R.id.online_problem);

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

        new_customer.setOnClickListener(this);
        addactionButton.setOnClickListener(this);
        breakButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.online_new_customer:
                presenterClass.getCustomersData();
                onlineImage.setVisibility(View.GONE);
                timer.setVisibility(View.VISIBLE);
//                timer.setCurrentTime(20000);
//                start circular view to rotate
                builder = new
                        CircularView.OptionsBuilder()
                        .shouldDisplayText(true)
                        .setCounterInSeconds(60)
                        .setCircularViewCallback(new CircularViewCallback() {
                            @Override
                            public void onTimerFinish() {
                                Toast.makeText(OnlineActivity.this, "CircularCallback: Timer Finished ", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onTimerCancelled() {
                                Toast.makeText(OnlineActivity.this, "CircularCallback: Timer Cancelled ", Toast.LENGTH_SHORT).show();
                            }
                        });
                timer.setOptions(builder);
                timer.startTimer();


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
                onlineImage.setVisibility(View.VISIBLE);
                timer.setVisibility(View.GONE);
                resetFields();
//                currentTime = Calendar.getInstance().getTime();

                onlineEditor = onlineSharedPreferences.edit();
                onlineEditor.putString(ONLINE_PROBLEM,"" + problem.getText());
                onlineEditor.putString(ONLINE_CHECH_OUT_TIME,"" + currentTime);

                CustomerOnline customerOnline  =new CustomerOnline();
                customerOnline.setProblem("" + problem.getText());
                customerOnline.setCustomerName("" + username.getText());
                customerOnline.setPhoneNo("" + phoneNo.getText());
                customerOnline.setCheakInTime(onlineSharedPreferences.getString(ONLINE_CHECH_IN_TIME, null));
                customerOnline.setCompanyName(onlineSharedPreferences.getString(ONLINE_COMPANY_NAME, null));
                customerOnline.setSystemName(onlineSharedPreferences.getString(ONLINE_SYSTEM_NAME, null));
                customerOnline.setSystemId(onlineSharedPreferences.getString(ONLINE_SYS_ID, null));
                customerOnline.setEngineerID(LoginActivity.sharedPreferences.getString(LOGIN_ID, null));
                customerOnline.setEngineerName(LoginActivity.sharedPreferences.getString(LOGIN_NAME, null));
                customerOnline.setCheakOutTime(onlineSharedPreferences.getString(ONLINE_CHECH_OUT_TIME, null));

//                presenterClass.pushCustomerProblem(customerOnline, 0);// check out

                break;
            case R.id.online_break:
                profilePicture.setBorderColor(getResources().getColor(R.color.yellowf));
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.break_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);

                exitBreak = dialog.findViewById(R.id.breakDialog_exit);
                exitBreak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        profilePicture.setBorderColor(getResources().getColor(R.color.greenf));
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case R.id.online_exit:
                profilePicture.setBorderColor(getResources().getColor(R.color.redf));
                break;
        }
    }

    public void resetFields(){
        snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#3167F0\">Checked out Successfully</font>"), Snackbar.LENGTH_SHORT);
        View snackbarLayout = snackbar.getView();
        TextView textViewSnackbar = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        textViewSnackbar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
        textViewSnackbar.setCompoundDrawablePadding(15);
        snackbar.show();
        phoneNo.setText("");
        username.setText("");
        system.setText("");
        hideCustomerLinear();
    }

    public void showCustomerLinear(CustomerOnline customerOnline){
        new_customer.setVisibility(View.GONE);
        customerLayout.setVisibility(View.VISIBLE);
        phoneNo.setText(customerOnline.getPhoneNo());
        system.setText(customerOnline.getSystemName());
        username.setText(customerOnline.getCustomerName());

        onlineEditor = onlineSharedPreferences.edit();
        onlineEditor.putString(ONLINE_CHECH_IN_TIME,customerOnline.getCheakInTime());
        onlineEditor.putString(ONLINE_COMPANY_NAME,customerOnline.getCompanyName());
        onlineEditor.putString(ONLINE_CUST_NAME,customerOnline.getCustomerName());
        onlineEditor.putString(ONLINE_PHONE_NO,customerOnline.getPhoneNo());
        onlineEditor.putString(ONLINE_SYSTEM_NAME,customerOnline.getSystemName());
        onlineEditor.putString(ONLINE_SYS_ID,customerOnline.getSystemId());

    }

     void hideCustomerLinear(){
        new_customer.setVisibility(View.VISIBLE);
        customerLayout.setVisibility(View.GONE);

    }
}
