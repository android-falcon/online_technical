package com.falconssoft.onlinetechsupport;

import android.animation.Animator;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static List<ManagerLayout> cheakIn, cheakout, hold;
    ImageView addEmp,AddSystem;
    ListView listCheakIn, listCheakout, holds;
    ImageView falcon;
    Animation animZoom;
    Timer T;
    RelativeLayout imageMove;
    public static TextView refresh;
    TextView waite;
    ManagerLayOutAdapter managerLayOutAdapter1;
    ManagerLayOutAdapter2 managerLayOutAdapter2;
    ManagerLayOutAdapter3 managerLayOutAdapter3;
    String ipAddres = "5.189.130.98:8085";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        add = findViewById(R.id.ADD);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ManagerImport managerImport = new ManagerImport(MainActivity.this);
//                managerImport.startSending("ManageriN");
//            }
//        });
//        RecyclerView recyclerView = null;
//        recyclerView.getAdapter().notifyItemInserted(position);
//        recyclerView.getAdapter().notifyItemRangeInserted(positionStart, itemCount);
        waite = findViewById(R.id.waite);
        addEmp=  findViewById(R.id.addEmp);
        AddSystem= findViewById(R.id.addSys);

        refresh = findViewById(R.id.refresh);
        imageMove = findViewById(R.id.imageMove);
        cheakIn = new ArrayList<>();
        cheakout = new ArrayList<>();
        hold = new ArrayList<>();
        listCheakIn = findViewById(R.id.listCheakIn);
        listCheakout = findViewById(R.id.listCheakout);
        holds = findViewById(R.id.hold);
        ManagerImport managerImport = new ManagerImport(MainActivity.this);
        managerImport.startSending("Manager");
//        animZoom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
//        falcon.startAnimation(animZoom);
        falcon = findViewById(R.id.falcon);
        final Animation animZoom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
        falcon.startAnimation(animZoom);

        addEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddEmploy();

            }
        });

        AddSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddSystem();
            }
        });

//        cheakIn.clear();
//        cheakout.clear();
//        hold.clear();

//        for(int i=0;i<20;i++){
//            ManagerLayout managerLayout =new ManagerLayout();
//            managerLayout.setCompanyName("Falcons Soft");
//            managerLayout.setCheakInTime("10:21 AM"+i);
//            managerLayout.setEnginerName("Eng.Name");
//            cheakIn.add(managerLayout);
//        }
//
//        for(int i=0;i<20;i++){
//            ManagerLayout managerLayout =new ManagerLayout();
//            managerLayout.setCompanyName("Falcons Soft");
//            managerLayout.setCheakInTime("15:21 AM"+i);
//            managerLayout.setEnginerName("Eng.Name");
//            cheakout.add(managerLayout);
//        }
//
//        for(int i=0;i<20;i++){
//            ManagerLayout managerLayout =new ManagerLayout();
//            managerLayout.setCompanyName("Falcons Soft");
//            managerLayout.setCheakInTime("12:21 AM"+i);
//            managerLayout.setEnginerName("Eng.Name");
//            hold.add(managerLayout);
//        }

        managerLayOutAdapter1 = new ManagerLayOutAdapter(MainActivity.this, cheakIn, 0);
        managerLayOutAdapter2 = new ManagerLayOutAdapter2(MainActivity.this, cheakout, 1);
        managerLayOutAdapter3 = new ManagerLayOutAdapter3(MainActivity.this, hold, 2);

//
        holds.setAdapter(managerLayOutAdapter3);
        listCheakIn.setAdapter(managerLayOutAdapter1);
        listCheakout.setAdapter(managerLayOutAdapter2);

        refresh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (refresh.getText().toString().equals("1")) {
//                    listCheakIn.setAdapter(managerLayOutAdapter1);
//                    listCheakout.setAdapter(managerLayOutAdapter2);
//                    holds.setAdapter(managerLayOutAdapter3);
                    managerLayOutAdapter1.notifyDataSetChanged();
                    managerLayOutAdapter2.notifyDataSetChanged();
                    managerLayOutAdapter3.notifyDataSetChanged();
                    refresh.setText("0");
                } else if (refresh.getText().toString().equals("2")) {//wait to in
//                    insertImage(waite,null,listCheakIn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//
        T = new Timer();
        T.schedule(new TimerTask() {
            @Override
            public void run() {
                ManagerImport managerImport = new ManagerImport(MainActivity.this);
                managerImport.startSending("Manager");
            }

        }, 0, 1000);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ImageView insertImage(View view, String picItem, ListView toView) {


        final ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.bac_list_0_2));
        int p1[] = new int[2];
        int p2[] = new int[2];
        toView.getLocationInWindow(p2);
        view.getLocationInWindow(p1);
        imageView.setVisibility(View.VISIBLE);
        Log.e("location ", "" + p1[0] + "    " + p1[1]);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(70,
                70);
        imageView.setLayoutParams(params);
        imageView.setX(p1[0] + 10);
        imageView.setY(p1[1]);

        imageMove.addView(imageView);

        imageView.animate()
                .x(p2[0])
                .y(p2[1])
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        imageView.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }


                })
                .start();


        imageView.setImageDrawable(getResources().getDrawable(R.drawable.bac_list_0));


        return imageView;
    }
    public void  AddEmploy(){

        final Dialog AddEmployeDialog = new Dialog(MainActivity.this,R.style.Theme_Dialog);
        AddEmployeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddEmployeDialog.setCancelable(true);
        AddEmployeDialog.setContentView(R.layout.add_emp_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        final EditText UserName,Password;
        final RadioButton Manager,Online,callCenter;
        Button add,cancel;



        UserName=AddEmployeDialog.findViewById(R.id.UserName);
        Password=AddEmployeDialog.findViewById(R.id.password);
        Manager=AddEmployeDialog.findViewById(R.id.managerRadio);
        Online =AddEmployeDialog.findViewById(R.id.OnlineRadio);
        callCenter=AddEmployeDialog.findViewById(R.id.CallRadio);

        add =AddEmployeDialog.findViewById(R.id.Addbutton);
        cancel =AddEmployeDialog.findViewById(R.id.Cancelbutton);



//        Manager.setChecked(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserName.getText().toString().equals("")&&!Password.getText().toString().equals("")){

                    JSONObject obj=new JSONObject();
                    EngineerInfo engineerInfo=new EngineerInfo();
                    int EngType=0;
                    if(Manager.isChecked()){
                        EngType=0;
                    }else if(Online.isChecked()){
                        EngType=2;
                    }else if(callCenter.isChecked()){
                        EngType=1;
                    }

                    engineerInfo.setEng_type(EngType);
                    engineerInfo.setName(UserName.getText().toString());
                    engineerInfo.setId("0");
                    engineerInfo.setPassword(Password.getText().toString());
                    engineerInfo.setState(0);


                    try {
                        obj= engineerInfo.getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ManagerExport managerExport=new ManagerExport(MainActivity.this,obj);
                    managerExport.startSending("AddEmp");

                    UserName.setText("");
                    Password.setText("");

                } else {
                    Toast.makeText(MainActivity.this, "Please Add Information Of User", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             AddEmployeDialog.dismiss();
            }
        });


        AddEmployeDialog.show();
    }
    public void  AddSystem(){

        final Dialog AddSystem = new Dialog(MainActivity.this,R.style.Theme_Dialog);
        AddSystem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddSystem.setCancelable(true);
        AddSystem.setContentView(R.layout.add_sys_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        final EditText SystemName;
        Button add,cancel;



        SystemName=AddSystem.findViewById(R.id.sysName);

        add =AddSystem.findViewById(R.id.Addbutton);
        cancel =AddSystem.findViewById(R.id.Cancelbutton);



//        Manager.setChecked(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SystemName.getText().toString().equals("")){

                    JSONObject obj=new JSONObject();
                    Systems systems=new Systems();



                    systems.setSystemName(SystemName.getText().toString());
                    systems.setSystemNo("1");


                    try {
                        obj= systems.getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ManagerExport managerExport=new ManagerExport(MainActivity.this,obj);
                    managerExport.startSending("AddSystem");
                    SystemName.setText("");

                } else {
                    Toast.makeText(MainActivity.this, "Please Add Information Of User", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSystem.dismiss();
            }
        });


        AddSystem.show();
    }
}
