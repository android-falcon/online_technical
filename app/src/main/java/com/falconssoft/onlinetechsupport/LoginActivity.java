package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AppCompatActivity;

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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id, username, password;
    private Button done, login;
    private ImageView settings, logo;
    private int employeeID;
    private Animation animation;
    private Dialog settingDialog;
    private PresenterClass presenterClass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
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

        presenterClass = new PresenterClass(this);
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
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                String localUsername = username.getText().toString();
//                String localPassword = password.getText().toString();
//                if (!TextUtils.isEmpty(localUsername) && (localUsername.))

//                editor.putString(LOGIN_NAME, n);
//                editor.putString(Phone, ph);
//                editor.putString(Email, e);
//                editor.commit();
                break;
            case R.id.login_settings:
                settingDialog = new Dialog(LoginActivity.this);
                settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                settingDialog.setContentView(R.layout.settings_dialog);

                id = settingDialog.findViewById(R.id.settings_id);
                done = settingDialog.findViewById(R.id.settings_done);

                employeeID = 0;

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(id.getText().toString())) {
                            employeeID = Integer.parseInt(id.getText().toString());

                            switch (employeeID) {
                                case 1:// manager
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:// call center
                                    Intent intent2 = new Intent(LoginActivity.this, OnlineCenter.class);
                                    startActivity(intent2);
                                    break;
                                case 3:// online
                                    Intent intent3 = new Intent(LoginActivity.this, OnlineActivity.class);
                                    startActivity(intent3);
                                    break;
                            }
                        }
                        settingDialog.dismiss();
                    }
                });
                settingDialog.show();
                break;
        }
    }
}
