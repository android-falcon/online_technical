package com.falconssoft.onlinetechsupport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
//import android.support.design.widget.Snackbar;

import de.hdodenhof.circleimageview.CircleImageView;


public class OnlineActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout coordinatorLayout;
    private CircleImageView profilePicture;
    private ImageView breakButton, exitButton,customerName;
    private TextView  phoneNo, system;
    private Button exitBreak;
    private FloatingActionButton addactionButton;
    private Snackbar snackbar;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        profilePicture = findViewById(R.id.online_settings);
        breakButton = findViewById(R.id.online_break);
        exitButton = findViewById(R.id.online_exit);
        coordinatorLayout = findViewById(R.id.online_cordinator_layout);
        addactionButton = findViewById(R.id.online_add);
        customerName = findViewById(R.id.online_image_customer);
//        phoneNo = findViewById(R.id.online_image_customer);
//        system = findViewById(R.id.online_image_customer);

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

        addactionButton.setOnClickListener(this);
        breakButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.online_add:
                snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#3167F0\">Checked out Successfully</font>"), Snackbar.LENGTH_SHORT);
                View snackbarLayout = snackbar.getView();
                TextView textViewSnackbar = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
                textViewSnackbar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
                textViewSnackbar.setCompoundDrawablePadding(15);
                snackbar.show();
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
}
