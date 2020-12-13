package com.falconssoft.onlinetechsupport;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.falconssoft.onlinetechsupport.reports.CallCenterTrackingReport;
import com.falconssoft.onlinetechsupport.reports.EngineersTrackingReport;
import com.falconssoft.onlinetechsupport.reports.ProgrammerReport;
import com.falconssoft.onlinetechsupport.reports.TechnicalTrackingReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    public static List<ManagerLayout> cheakIn, cheakout, hold,schaeduale;
    //    ImageView addEmp, AddSystem;
    ListView listCheakIn, listCheakout, holds,listSchedule;
    ImageView falcon;
    Animation animZoom;
    Timer T;
    RelativeLayout imageMove;
    public static TextView refresh,countChickHold,countChickOut,countChickIn,countSchedule,countOfScheduleS;
    TextView waite;
    ManagerLayOutAdapter managerLayOutAdapter1;
    ManagerLayOutAdapter2 managerLayOutAdapter2;
    ManagerLayOutAdapter3 managerLayOutAdapter3;
    ManagerLayOutAdapter4 managerLayOutAdapter4;
    String ipAddres = "5.189.130.98:8085";
    private Toolbar toolbar;
    ManagerImport managerImport;
    TextView countOfSchedule,line;
    int isVisible=1;
    LinearLayout scheduleLinear;


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
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        waite = findViewById(R.id.waite);
        line= findViewById(R.id.line);
//        addEmp = findViewById(R.id.addEmp);
//        AddSystem = findViewById(R.id.addSys);

        refresh = findViewById(R.id.refresh);
        countChickHold= findViewById(R.id.countChickHold);
        countChickOut= findViewById(R.id.countChickOut);
        countChickIn= findViewById(R.id.countChickIn);
        countSchedule= findViewById(R.id.countSchedule);
        countOfScheduleS= findViewById(R.id.countOfSchedule);
        imageMove = findViewById(R.id.imageMove);
        cheakIn = new ArrayList<>();
        cheakout = new ArrayList<>();
        hold = new ArrayList<>();
        schaeduale=new ArrayList<>();

        listCheakIn = findViewById(R.id.listCheakIn);
        scheduleLinear=findViewById(R.id.scheduleLinear);
        countOfSchedule = findViewById(R.id.countOfSchedule);
        listCheakout = findViewById(R.id.listCheakout);
        holds = findViewById(R.id.hold);
        listSchedule=findViewById(R.id.listSchedule);
         managerImport = new ManagerImport(MainActivity.this);
        managerImport.startSending("Manager");
//        animZoom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
//        falcon.startAnimation(animZoom);
        falcon = findViewById(R.id.falcon);
//        final Animation animZoom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
//        falcon.startAnimation(animZoom);

//        addEmp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AddEmploy();
//
//            }
//        });
//
//        AddSystem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AddSystem();
//            }
//        });

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
        scheduleLinear.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        countOfSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isVisible==0){
                    line.setVisibility(View.GONE);
                    scheduleLinear.setVisibility(View.GONE);
                    isVisible=1;
                }else if(isVisible==1){
                    line.setVisibility(View.VISIBLE);
                    scheduleLinear.setVisibility(View.VISIBLE);
                    isVisible=0;
                }

            }
        });

        managerLayOutAdapter1 = new ManagerLayOutAdapter(MainActivity.this, cheakIn, 0);
        managerLayOutAdapter2 = new ManagerLayOutAdapter2(MainActivity.this, cheakout, 1);
        managerLayOutAdapter3 = new ManagerLayOutAdapter3(MainActivity.this, hold, 2);
        managerLayOutAdapter4= new  ManagerLayOutAdapter4(MainActivity.this, schaeduale, 3);
//
        holds.setAdapter(managerLayOutAdapter3);
        listCheakIn.setAdapter(managerLayOutAdapter1);
        listCheakout.setAdapter(managerLayOutAdapter2);
        listSchedule.setAdapter(managerLayOutAdapter4);
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
                    managerLayOutAdapter4.notifyDataSetChanged();
                    refresh.setText("0");
                } else if (refresh.getText().toString().equals("2")) {//wait to in
//                    insertImage(waite,null,listCheakIn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

         managerImport = new ManagerImport(MainActivity.this);
        T = new Timer();
        T.schedule(new TimerTask() {
            @Override
            public void run() {

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

    void addEmploy() {

        final Dialog AddEmployeDialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        AddEmployeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddEmployeDialog.setCancelable(true);
        AddEmployeDialog.setContentView(R.layout.add_emp_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        final EditText UserName, Password;//,EngId;
        final RadioButton Manager, Online, callCenter,CallTecRadio,TecRadio,CallProgRadio,progRadio;
        TextView add, cancel;


        UserName = AddEmployeDialog.findViewById(R.id.UserName);
        Password = AddEmployeDialog.findViewById(R.id.password);
//        EngId=AddEmployeDialog.findViewById(R.id.EngId);

        Manager = AddEmployeDialog.findViewById(R.id.managerRadio);
        Online = AddEmployeDialog.findViewById(R.id.OnlineRadio);
        callCenter = AddEmployeDialog.findViewById(R.id.CallRadio);

        CallTecRadio = AddEmployeDialog.findViewById(R.id.CallTecRadio);
        TecRadio = AddEmployeDialog.findViewById(R.id.TecRadio);
        CallProgRadio=AddEmployeDialog.findViewById(R.id.CallProgRadio);
        progRadio=AddEmployeDialog.findViewById(R.id.progRadio);

        add = AddEmployeDialog.findViewById(R.id.Addbutton);
        cancel = AddEmployeDialog.findViewById(R.id.Cancelbutton);


//        Manager.setChecked(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserName.getText().toString().equals("") && !Password.getText().toString().equals("")) {

                    JSONObject obj = new JSONObject();
                    EngineerInfo engineerInfo = new EngineerInfo();
                    int EngType = 0;
                    if (Manager.isChecked()) {
                        EngType = 0;
                    } else if (Online.isChecked()) {
                        EngType = 2;
                    } else if (callCenter.isChecked()) {
                        EngType = 1;
                    } else if (CallTecRadio.isChecked()) {
                        EngType = 3;
                    } else if (TecRadio.isChecked()) {
                        EngType = 4;
                    }else if (CallProgRadio.isChecked()) {
                        EngType = 5;
                    }else if (progRadio.isChecked()) {
                        EngType = 6;
                    }

                    engineerInfo.setEng_type(EngType);
                    engineerInfo.setName(UserName.getText().toString());
                    engineerInfo.setId("*");
                    engineerInfo.setPassword(Password.getText().toString());
                    engineerInfo.setState(0);


                    try {
                        obj = engineerInfo.getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ManagerExport managerExport = new ManagerExport(MainActivity.this, obj);
                    managerExport.startSending("AddEmp");
//                    EngId.setText("");
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

    void addSystem() {

        final Dialog AddSystem = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        AddSystem.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddSystem.setCancelable(true);
        AddSystem.setContentView(R.layout.add_sys_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        final EditText SystemName;//,SysId;
        TextView add, cancel;


        SystemName = AddSystem.findViewById(R.id.sysName);
//        SysId=AddSystem.findViewById(R.id.sysId);

        add = AddSystem.findViewById(R.id.Addbutton);
        cancel = AddSystem.findViewById(R.id.Cancelbutton);


//        Manager.setChecked(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {///*&&!SysId.getText().toString().equals("")*/
                if (!SystemName.getText().toString().equals("")) {

                    JSONObject obj = new JSONObject();
                    Systems systems = new Systems();

                    systems.setSystemName(SystemName.getText().toString());
                    systems.setSystemNo("*");


                    try {
                        obj = systems.getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ManagerExport managerExport = new ManagerExport(MainActivity.this, obj);
                    managerExport.startSending("AddSystem");
                    SystemName.setText("");
//                    SysId.setText("");

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

    public void passwordForSetting(final int flag){
        final EditText editText = new EditText(MainActivity.this);
        final TextView textView = new TextView(MainActivity.this);
        editText.setHint("ادخل كلمة السر ");
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTextColor(Color.BLACK);
        textView.setTextColor(Color.RED);
        if (SweetAlertDialog.DARK_STYLE) {
            editText.setTextColor(Color.BLACK);
        }
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(editText);
        linearLayout.addView(textView);

        SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("كلمة السر ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String password=editText.getText().toString();
                        textView.setText("");
                        if(flag==0) {
                            if (!password.equals("")) {

                                if (password.equals("co1234")) {

                                    textView.setText("");
                                    Intent intent = new Intent(MainActivity.this, CallCenterTrackingReport.class);
                                    startActivity(intent);

                                    sweetAlertDialog.dismissWithAnimation();

                                } else {
                                    textView.setText("كلمة السر خطا ");
                                }

                            }

                        }else if(flag==1){

                            if (!password.equals("")) {

                                if (password.equals("to1234")) {

                                    textView.setText("");
                                    Intent intent4 = new Intent(MainActivity.this, TechnicalTrackingReport.class);
                                    startActivity(intent4);
                                    sweetAlertDialog.dismissWithAnimation();

                                } else {
                                    textView.setText("كلمة السر خطا ");
                                }

                            }

                        }else if(flag==2){

                            if (!password.equals("")) {

                                if (password.equals("prog1234")) {

                                    textView.setText("");
                                    Intent intent4 = new Intent(MainActivity.this, ProgrammerReport.class);
                                    startActivity(intent4);
                                    sweetAlertDialog.dismissWithAnimation();

                                } else {
                                    textView.setText("كلمة السر خطا ");
                                }

                            }

                        }

                    }
                });
//                        .hideConfirmButton();

        dialog.setCustomView(linearLayout);
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.control_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_system:
                addSystem();
                return true;
            case R.id.menu_add_employee:
                addEmploy();
                return true;
            case R.id.menu_report:
                passwordForSetting(0);

                return true;
            case R.id.menu_engineers_tracking_report:
                Intent intent2 = new Intent(MainActivity.this, EngineersTrackingReport.class);
                startActivity(intent2);
                return true;

            case R.id.DashBoard:
                Intent intent3 = new Intent(MainActivity.this, DashBoard.class);
                startActivity(intent3);
//                Toast.makeText(this, "In Next Version", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_tec_report:
                passwordForSetting(1);

//                Toast.makeText(this, "In Next Version", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_prog_report:
                passwordForSetting(2);

//                Toast.makeText(this, "In Next Version", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
