package com.falconssoft.onlinetechsupport;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.falconssoft.onlinetechsupport.PresenterClass.listInfo;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id, username, password,IP;
    public  static TextView intentText;
    private Button done, login;
    private ImageView settings, logo;
    private int employeeID;
    private String employeeIP;

    private Animation animation;
    private Dialog settingDialog;
    private PresenterClass presenterClass;
    private DatabaseHandler handler;
//    List<EngineerInfo> list;
    boolean found = false;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static final String LOGIN_PREFERNCES = "login_preferences";
    public static final String LOGIN_NAME = "name";
    public static final String LOGIN_ID = "id";
    public static final String LOGIN_STATE = "state";
    public static final String LOGIN_PASSWORD = "password";
    public static final String LOGIN_TYPE = "type";
    String localUsername;
    String localPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = new DatabaseHandler(this);
        presenterClass = new PresenterClass(this);

        sharedPreferences = getSharedPreferences(LOGIN_PREFERNCES, Context.MODE_PRIVATE);
        settings = findViewById(R.id.login_settings);
        login = findViewById(R.id.login_login);
        logo = findViewById(R.id.login_logo);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        username.requestFocus();
        intentText=findViewById(R.id.intentText);
        intentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("start"))
                {
                    for (int i = 0; i < listInfo.size(); i++)
                        if (localUsername.toLowerCase().equals(listInfo.get(i).getName().toLowerCase())
                                && localPassword.equals(listInfo.get(i).getPassword())) {//
                            found = true;
                            editor = sharedPreferences.edit();
                            editor.putString(LOGIN_NAME, listInfo.get(i).getName());
                            editor.putString(LOGIN_ID, listInfo.get(i).getId());
                            editor.putInt(LOGIN_STATE, listInfo.get(i).getState());
                            editor.putString(LOGIN_PASSWORD, listInfo.get(i).getPassword());
                            editor.putInt(LOGIN_TYPE, listInfo.get(i).getEng_type());
                            editor.commit();

                            switch (listInfo.get(i).getEng_type()) {
                                case 0:// manager
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    break;
                                case 1:// call center
                                    Intent intent2 = new Intent(LoginActivity.this, OnlineCenter.class);
                                    startActivity(intent2);
                                    break;
                                case 2:// online
                                    Intent intent3 = new Intent(LoginActivity.this, OnlineActivity.class);
                                    startActivity(intent3);
                                    break;
                            }
                            presenterClass.setState(listInfo.get(i).getId(), 0);// log in
                            break;
                        }

                    if (!found) {
                        if (localUsername.equals("Manager")&&localPassword.equals("1234m")) {

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        }else if(localUsername.equals("online")&&localPassword.equals("1234o")){

                            Intent intent3 = new Intent(LoginActivity.this, OnlineActivity.class);
                            startActivity(intent3);
                        }else  if(localUsername.equals("callCenter")&&localPassword.equals("1234c")){
                            Intent intent2 = new Intent(LoginActivity.this, OnlineCenter.class);
                            startActivity(intent2);
                        }else{
                            Toast.makeText(LoginActivity.this, "Username or Password isn't Existing!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        animation = AnimationUtils.loadAnimation(this, R.anim.pop_up);
        logo.startAnimation(animation);

        login.setOnClickListener(this);
        settings.setOnClickListener(this);
        ManagerImport managerImport=new ManagerImport(LoginActivity.this);
        managerImport.startSending("CustomerPhone");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
//                Intent intent2 = new Intent(LoginActivity.this, OnlineCenter.class);
//                startActivity(intent2);
//                list = handler.getLoginData();
                 localUsername = username.getText().toString();
                 localPassword = password.getText().toString();


                if (!TextUtils.isEmpty(localUsername))
                    if (!TextUtils.isEmpty(localPassword)) {
                        try {
                            presenterClass = new PresenterClass(this);
                            presenterClass.getLoginData(localUsername,localPassword);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        animation = AnimationUtils.loadAnimation(this, R.anim.shake);
                        password.startAnimation(animation);
                    }
                else {
                    animation = AnimationUtils.loadAnimation(this, R.anim.shake);
                    username.startAnimation(animation);

                }

                break;
            case R.id.login_settings:
                settingDialog = new Dialog(LoginActivity.this);
                settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                settingDialog.setContentView(R.layout.settings_dialog);

                id = settingDialog.findViewById(R.id.settings_id);
                IP= settingDialog.findViewById(R.id.settings_ip);
                done = settingDialog.findViewById(R.id.settings_done);
              String ip=  handler.getIp();
                IP.setText(""+ip);
                employeeID = 0;

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(IP.getText().toString())) {
//                            employeeID = Integer.parseInt(id.getText().toString());
//
//                            switch (employeeID) {
//                                case 1:// manager
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    break;
//                                case 2:// call center
//                                    Intent intent2 = new Intent(LoginActivity.this, OnlineCenter.class);
//                                    startActivity(intent2);
//                                    break;
//                                case 3:// online
//                                    Intent intent3 = new Intent(LoginActivity.this, OnlineActivity.class);
//                                    startActivity(intent3);
//                                    break;
//                            }

                            handler.deleteIpData();
                            employeeIP = IP.getText().toString();
                            handler.addIPSetting(employeeIP);
                            Toast.makeText(LoginActivity.this, "Save Success", Toast.LENGTH_SHORT).show();
                            settingDialog.dismiss();
//                            presenterClass.getLoginData();

                        }else{}

                    }
                });
                settingDialog.show();
                break;
        }
    }
}
