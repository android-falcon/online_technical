package com.falconssoft.onlinetechsupport;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id, username, password,IP;
    private Button done, login;
    private ImageView settings, logo;
    private int employeeID;
    private String employeeIP;

    private Animation animation;
    private Dialog settingDialog;
    private PresenterClass presenterClass;
    private DatabaseHandler handler;
    List<EngineerInfo> list;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static final String LOGIN_PREFERNCES = "login_preferences";
    public static final String LOGIN_NAME = "name";
    public static final String LOGIN_ID = "id";
    public static final String LOGIN_STATE = "state";
    public static final String LOGIN_PASSWORD = "password";
    public static final String LOGIN_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = new DatabaseHandler(this);
        presenterClass = new PresenterClass(this);
        presenterClass.getLoginData();
        sharedPreferences = getSharedPreferences(LOGIN_PREFERNCES, Context.MODE_PRIVATE);
        settings = findViewById(R.id.login_settings);
        login = findViewById(R.id.login_login);
        logo = findViewById(R.id.login_logo);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);

        animation = AnimationUtils.loadAnimation(this, R.anim.pop_up);
        logo.startAnimation(animation);

        login.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
//                Intent intent2 = new Intent(LoginActivity.this, OnlineCenter.class);
//                startActivity(intent2);
                list = handler.getLoginData();
                String localUsername = username.getText().toString();
                String localPassword = password.getText().toString();
                boolean found = false;

                if (!TextUtils.isEmpty(localUsername))
                    if (!TextUtils.isEmpty(localPassword)) {
                        for (int i = 0; i < list.size(); i++)
                            if (localUsername.toLowerCase().equals(list.get(i).getName().toLowerCase())
                                    ) {//&& localPassword.equals(list.get(i).getPassword())
                                found = true;
                                editor= sharedPreferences.edit();
                                editor.putString(LOGIN_NAME,list.get(i).getName() );
                                editor.putString(LOGIN_ID,list.get(i).getId() );
                                editor.putInt(LOGIN_STATE, list.get(i).getState());
                                editor.putString(LOGIN_PASSWORD,list.get(i).getPassword() );
                                editor.putInt(LOGIN_TYPE,list.get(i).getEng_type() );
                                editor.commit();

                                switch (list.get(i).getEng_type()) {
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
                                presenterClass.setState(list.get(i).getId(), 0);// log in
                                break;
                            }

                        if (!found){
                            Toast.makeText(this, "Username or Password isn't Existing!", Toast.LENGTH_SHORT).show();
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

                        }else{}

                    }
                });
                settingDialog.show();
                break;
        }
    }
}
