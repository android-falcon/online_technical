package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private EditText id;
    private Button done;
    private ImageView settings;
    int employeeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        settings = findViewById(R.id.login_settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.settings_dialog);

                id = dialog.findViewById(R.id.settings_id);
                done = dialog.findViewById(R.id.settings_done);

                  employeeID = 0;

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(id.getText().toString())){
                            employeeID = Integer.parseInt(id.getText().toString());

                            switch (employeeID){
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
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
