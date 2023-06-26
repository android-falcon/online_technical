package com.falconssoft.onlinetechsupport;

import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_ID;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_NAME;
import static com.falconssoft.onlinetechsupport.LoginActivity.LOGIN_TYPE;
import static com.falconssoft.onlinetechsupport.LoginActivity.sharedPreferences;
import static com.falconssoft.onlinetechsupport.OnlineCenter.EngId;

import static com.falconssoft.onlinetechsupport.OnlineCenter.norDanFlag;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;
import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.Modle.Systems;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class TechnicalActivityOnline extends AppCompatActivity implements View.OnClickListener {
    public static List<EngineerInfo> engineerInfoList, listEngforAdapter, engInfoTra;

    Spinner appSpinner;
    private TextView customer, company, problem, phone, system, clock, emptyListTV;
    private EditText tProblem;
    private RecyclerView recyclerView,recyclerViewDanger;
    private ImageButton visitReport, checkout, exit,transfer,wait;
    private TechnicalRecyclerAdapterOnline adapter;
    private TechnicalRecyclerAdapterOnlineDanger adapterDanger;
    private PresenterClass presenterClass;
    private CustomerOnline checkInfo;
    private CircleImageView visitReportPic;
    private Bitmap bitmap, serverPicBitmap;
    private CustomerOnline customerOnline;
    private  ManagerLayout managerLayoutTransfer;
    private boolean isPermition;
    public static boolean isOpenFirstTime=true;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    public static List<Systems> systemsList;

    private Date date;
    private SimpleDateFormat format;
    private long startTime = 0L, timeInMillies = 0L, timeSwap = 0L, finalTime = 0L;
    private Handler myHandler = new Handler();
    private Timer timer;

    private LocationManager locationManager;
    //    private LocationClass locationClass;
    private int isCheckIn = 0, flag = 0;// 0 nothing, 1 checkin , 2 checkout
    private SweetAlertDialog checkoutDialog, attentionDialog;
    private String mCameraFileName, path;
    private Uri image;
    private Location checkinLocation, checkoutLocation;
    private ProgressDialog progressDialog;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LocationRequest mLocationRequest;
    double longiOne, latOne, longiTwo, latTwo;
    private LocationCallback callback;
    int callType=1;
    public static List<String> engStringName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_on);
        managerLayoutTransfer=new ManagerLayout();
        presenterClass = new PresenterClass(this);
        format = new SimpleDateFormat("hh:mm:ss");//"dd/MM/yyyy   hh:mm:ss"
        bitmap = null;
//        locationClass = new LocationClass();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        callType = sharedPreferences.getInt(LOGIN_TYPE, -1);
        Log.e("callType", "" + callType);

        checkinLocation = new Location("dummyprovider");
        checkoutLocation = new Location("dummyprovider");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waiting..");
        progressDialog.setCanceledOnTouchOutside(false);

//        presenterClass.getCustomersData(this);
        wait=findViewById(R.id.technical_wait);
        company = findViewById(R.id.technical_company);
        customer = findViewById(R.id.technical_customer);
        problem = findViewById(R.id.technical_problem);
        tProblem = findViewById(R.id.technical_t_problem);
        recyclerView = findViewById(R.id.technical_customers_recycler);
        recyclerViewDanger=findViewById(R.id.technical_customers_recycler_danger);
        checkout = findViewById(R.id.technical_checkout);
        visitReport = findViewById(R.id.technical_add_image);
        exit = findViewById(R.id.technical_exist);
        phone = findViewById(R.id.technical_phone);
        system = findViewById(R.id.technical_system);
        emptyListTV = findViewById(R.id.technical_customers_tv);
        visitReportPic = findViewById(R.id.technical_visit_report);
        customerOnline = new CustomerOnline();
        transfer=findViewById(R.id.technical_transfer);
        engineerInfoList = new ArrayList<>();
        listEngforAdapter = new ArrayList<>();
        engInfoTra = new ArrayList<>();

        clock = findViewById(R.id.technical_clock);
        clock.setVisibility(View.GONE);
        checkInfo = new CustomerOnline();
        system.setOnClickListener(this);
        checkout.setOnClickListener(this);
        visitReport.setOnClickListener(this);
        exit.setOnClickListener(this);
        transfer.setOnClickListener(this);
        visitReportPic.setOnClickListener(this);
        wait.setOnClickListener(this);

        emptyListTV.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                presenterClass.getCustomersData2(TechnicalActivityOnline.this);
                presenterClass.getCustomersHoldDanger(TechnicalActivityOnline.this);
            }

        }, 0, 60000);

//        callback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                // do work here
//                onLocationChanged(locationResult.getLastLocation());
//            }
//        };

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.technical_visit_report:
                openLargePicDialog(bitmap);
                break;
            case R.id.technical_add_image:
//                openCamera();
                if (checkInfo.getSerial() == null)
                    attentionDialog("Please checkin first!", SweetAlertDialog.WARNING_TYPE, 0);
                else {
//                    flag = 0;
                    isPermition = isStoragePermissionGranted();
                    if (isPermition)
                        cameraIntent();

                    //                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                }
                break;
            case R.id.technical_system:
                presenterClass.getSystems(TechnicalActivityOnline.this,2);
                break;
            case R.id.technical_checkout:
                checkOut();
//                isCheckIn = 2;
//                startLocationUpdates(true);
                break;
            case R.id.technical_exist:
                onBackPressed();
                break;

            case R.id.technical_transfer:
                if(checkInfo.getCompanyName()!=null) {
                    TransferDialog(managerLayoutTransfer);
                }else{
                    attentionDialog("Please checkin first!", SweetAlertDialog.WARNING_TYPE, 0);
                }
                
                break;
            case R.id.technical_wait:
                if(checkInfo.getCompanyName()!=null) {
                    final SweetAlertDialog dialog = new SweetAlertDialog(this);
                    dialog.setTitle(getResources().getString(R.string.check_in_time));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentText("Are you want start Wait This Order  ?");
                    dialog.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            presenterClass.updateTechnicalDangerState(TechnicalActivityOnline.this,checkInfo,2);
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            dialog.dismiss();

                        }
                    });
                    dialog.show();


                }else{
                    attentionDialog("Please checkin first!", SweetAlertDialog.WARNING_TYPE, 0);
                }


                break;
        }
    }

    void checkOut() {
//        Log.e("timer", checkInfo.getVisitReport() );
//        Toast.makeText(this, "out:long:" + longitude + "lat:" + latitude, Toast.LENGTH_LONG).show();

//        Log.e("ccccccheckout", "long: " + location.getLongitude() + "/lat: " + location.getLatitude());
        checkoutLocation.setLatitude(latTwo);
        checkoutLocation.setLongitude(longiTwo);
        if (checkoutDialog != null)
            checkoutDialog.dismiss();
        double dis = distance(checkinLocation, checkoutLocation);
//        isCheckIn = 0;
        if (!TextUtils.isEmpty(checkInfo.getSerial()))
            if (dis != -1 && dis <= 3000)// one meter
                if (!TextUtils.isEmpty(company.getText().toString()))
                    if (!TextUtils.isEmpty(customer.getText().toString()))
                        if (!TextUtils.isEmpty(phone.getText().toString()))
                            if (!TextUtils.isEmpty(system.getText().toString()))
                                if (!TextUtils.isEmpty(problem.getText().toString()))
                                    if (!TextUtils.isEmpty(tProblem.getText().toString())) {
//                                        if (!TextUtils.isEmpty(checkInfo.getVisitReportImage())) {

                                        tProblem.setError(null);
                                        system.setError(null);
                                        phone.setError(null);
                                        customer.setError(null);
                                        company.setError(null);

                                        if (attentionDialog != null)
                                            attentionDialog.dismiss();

                                        checkoutDialog = new SweetAlertDialog(this, SweetAlertDialog.BUTTON_CONFIRM);
                                        checkoutDialog.setContentText("Are you want checkout ?");
                                        checkoutDialog.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                myHandler.removeCallbacks(updateTimerMethod);
                                                date = Calendar.getInstance().getTime();
                                                checkInfo.setCheakOutTime(format.format(date));
                                                clock.setText("00:00");
                                                clock.setVisibility(View.GONE);
                                                clock.setAnimation(AnimationUtils.loadAnimation(TechnicalActivityOnline.this, R.anim.move_to_top));

                                                CustomerOnline customerOnline = new CustomerOnline();
                                                customerOnline.setCompanyName(company.getText().toString());
                                                customerOnline.setProblem(tProblem.getText().toString());
                                                customerOnline.setSystemName(system.getText().toString());
                                                customerOnline.setCustomerName(customer.getText().toString());
                                                customerOnline.setPhoneNo(phone.getText().toString());
                                                customerOnline.setSystemId(checkInfo.getSystemId());
                                                customerOnline.setEngineerID(sharedPreferences.getString(LOGIN_ID, null));
                                                customerOnline.setEngineerName(sharedPreferences.getString(LOGIN_NAME, null));
                                                customerOnline.setSerial(checkInfo.getSerial());
                                                customerOnline.setCheakInTime(checkInfo.getCheakInTime());
                                                customerOnline.setCheakOutTime(checkInfo.getCheakOutTime());
                                                customerOnline.setCustomerState(0);
                                                customerOnline.setLongitudeIn(checkInfo.getLongitudeIn());
                                                customerOnline.setLatitudeIn(checkInfo.getLatitudeIn());
                                                customerOnline.setLongitudeOut(checkoutLocation.getLongitude());
                                                customerOnline.setLatitudeOut(checkoutLocation.getLatitude());
                                                customerOnline.setUpdate(checkInfo.getUpdate());
                                                customerOnline.setCompanyId(checkInfo.getCompanyId());
                                                customerOnline.setVisitReportImage(checkInfo.getVisitReportImage());
                                                customerOnline.setIsSavPic(1);
                                                customerOnline.setDate(checkInfo.getDate());

                                                checkoutDialog.dismiss();
                                                showDialog();
                                                flag = 0;
//                                                    timer.cancel();
                                             //   getFusedLocationProviderClient(TechnicalActivityOnline.this).removeLocationUpdates(callback);
                                                presenterClass.checkOut(customerOnline, TechnicalActivityOnline.this, 2);

                                            }
                                        });
                                        checkoutDialog.show();
                                        // } else
//                                            attentionDialog("You forgot visit report!", SweetAlertDialog.WARNING_TYPE, 0);
                                    }  else
                                        tProblem.setError(getResources().getString(R.string.required));
                                else
                                    problem.setError(getResources().getString(R.string.required));
                            else
                                system.setError(getResources().getString(R.string.required));
                        else
                            phone.setError(getResources().getString(R.string.required));
                    else
                        customer.setError(getResources().getString(R.string.required));
                else
                    company.setError(getResources().getString(R.string.required));
            else
                Toast.makeText(this, "Out of range! close to checkin place and try to checkout", Toast.LENGTH_SHORT).show();
//            attentionDialog("Out of range! close to checkin place and try to checkout", SweetAlertDialog.CUSTOM_IMAGE_TYPE, R.drawable.ic_baseline_sad);
        else
            attentionDialog("You didn't checkin, Try to choose from customers list", SweetAlertDialog.CUSTOM_IMAGE_TYPE, R.drawable.ic_baseline_sad);

    }

    void checkIn(Location loc, final int checkIn) {
//        Toast.makeText(this, "in:long:" + longitude + "lat:" + latitude, Toast.LENGTH_LONG).show();
//
        final boolean check = true;
//
//        if (customerOnline.getLongitudeIn() != 0 && customerOnline.getLatitudeIn() != 0) {// check to make checkin in the range
//            checkinLocation.setLongitude(customerOnline.getLongitudeIn());// right location
//            checkinLocation.setLatitude(customerOnline.getLatitudeIn());
//            Log.e("ccccccheckIn", "1");
//
//            double dis = distance(checkinLocation, loc);
//            if (dis != -1 && dis >= 1500) {//// one kilo if (customerOnline.getLongitudeIn() != longitude && customerOnline.getLatitudeIn() != latitude)
//                flag = 0;
//                check = false;
//                Toast.makeText(this, "You're in another place, please check information from manager!", Toast.LENGTH_SHORT).show();
////                attentionDialog("You're in another place, please check information from manager!", SweetAlertDialog.WARNING_TYPE, 0);
//            }
//        } else {
//            Log.e("ccccccheckIn", "2");
//            checkinLocation.setLongitude(loc.getLongitude());// right location
//            checkinLocation.setLatitude(loc.getLatitude());
//        }
//
//        Log.e("ccccccheckIn", "long: " + loc.getLongitude() + "/lat: " + loc.getLatitude());
//        Log.e("checkIn", "" + (customerOnline.getLongitudeIn() == 0));
        if (check) {
            if (checkInfo.getSerial() == null) {
                final SweetAlertDialog dialog = new SweetAlertDialog(this);
                dialog.setTitle(getResources().getString(R.string.check_in_time));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentText("Are you want start check in ?");
                dialog.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        startLocationUpdates(false);
                        flag = 2;
                        tProblem.setError(null);
                        system.setError(null);
                        phone.setError(null);
                        customer.setError(null);
                        company.setError(null);
//                    locationUpdate();
                       // clock.setVisibility(View.VISIBLE);
                       // clock.setAnimation(AnimationUtils.loadAnimation(TechnicalActivityOnline.this, R.anim.move_to_down));

                        checkInfo = new CustomerOnline();
                        checkInfo = customerOnline;
                        Log.e("companyId", checkInfo.getCompanyId() + customerOnline.getCompanyId());
                        if (customerOnline.getLongitudeIn() != 0 && customerOnline.getLatitudeIn() != 0)
                            checkInfo.setUpdate(0);// checkout
                        else
                            checkInfo.setUpdate(1);// update
                        checkInfo.setLongitudeIn(checkinLocation.getLongitude());
                        checkInfo.setLatitudeIn(checkinLocation.getLatitude());
                        checkInfo.setVisitReportImage("");
                        date = Calendar.getInstance().getTime();
                        checkInfo.setCheakInTime(format.format(date));
//                        startTimer();
                        company.setText(customerOnline.getCompanyName());
                        problem.setText(customerOnline.getProblem());
                        phone.setText(customerOnline.getPhoneNo());
                        system.setText(customerOnline.getSystemName());
                        customer.setText(customerOnline.getCustomerName());




                        dialog.dismiss();
//
//                        object.put("PROBLEM", managerLayout.getProplem());
//                        object.put("CUST_NAME", managerLayout.getCustomerName());
//                        object.put("CHECH_IN_TIME", managerLayout.getCheakInTime());
//                        object.put("COMPANY_NAME", managerLayout.getCompanyName());
//                        object.put("PHONE_NO", managerLayout.getPhoneNo());
//                        object.put("SYSTEM_NAME", managerLayout.getSystemName());
//                        object.put("SYS_ID", managerLayout.getSystemId());
//                        object.put("ENG_ID", managerLayout.getEngId());
//                        object.put("ENG_NAME", managerLayout.getEnginerName());
//                        object.put("STATE", 0);
//                        object.put("SERIAL", managerLayout.getSerial());
//                        object.put("CONVERT_STATE", managerLayout.getConvertFlag());
//
//                        object.put("TRANSFER_FLAG", managerLayout.getTransferFlag());
//                        object.put("TRANSFER_TO_ENG_ID", managerLayout.getTransferToEngId());
//                        object.put("TRANSFER_TO_ENG_NAME", managerLayout.getTransferToEngName());
//                        object.put("TRANSFER_RESON", managerLayout.getTransferReason());
//                        object.put("TRANSFER_TO_SERIAL", managerLayout.getTransferToSerial());
//
//                        if (managerLayout.getOriginalSerial().equals("-2")) {
//                        

                        managerLayoutTransfer=new ManagerLayout();
                        managerLayoutTransfer.setProplem(checkInfo.getProblem());
                        managerLayoutTransfer.setCustomerName(checkInfo.getCustomerName());
                        managerLayoutTransfer.setCheakInTime(checkInfo.getCheakInTime());
                        managerLayoutTransfer.setCompanyName(checkInfo.getCompanyName());
                        managerLayoutTransfer.setPhoneNo(checkInfo.getPhoneNo());
                        managerLayoutTransfer.setSystemName(checkInfo.getSystemName());
                        managerLayoutTransfer.setSystemId(checkInfo.getSystemId());
                        managerLayoutTransfer.setCallCenterId(checkInfo.getCallCenterId());
                        managerLayoutTransfer.setCallCenterName(checkInfo.getCallCenterName());
                            managerLayoutTransfer.setEngId(checkInfo.getEngineerID());
                            managerLayoutTransfer.setEnginerName(checkInfo.getEngineerName());
                            managerLayoutTransfer.setState("0");
                            managerLayoutTransfer.setSerial(checkInfo.getSerial());
                        managerLayoutTransfer.setOriginalSerial(checkInfo.getOriginalSerial());
                        managerLayoutTransfer.setDangerStatus(checkInfo.getDangerStatus());


                        if(checkIn==1){
                            sendHoldData(managerLayoutTransfer);
                        }else {
                            presenterClass.updateTechnicalState(TechnicalActivityOnline.this, customerOnline,2,1);

                        }



//                        isCheckIn = 0;
                    }
                });
                dialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        flag = 0;
                        dialog.dismiss();

                    }
                });
                dialog.show();
            } else
                attentionDialog("You're checked in, Please checkout first!", SweetAlertDialog.WARNING_TYPE, 0);
        }

    }

    void sendHoldData(ManagerLayout managerLayout){
        JSONObject data = null;
        try {
            data = getData(managerLayout, 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("data", "" + data);
        ManagerImport managerImport = new ManagerImport(TechnicalActivityOnline.this);
        managerImport.startUpdateHold(data,1);

    }

    void firstCheckIn(){
        flag = 2;
        tProblem.setError(null);
        system.setError(null);
        phone.setError(null);
        customer.setError(null);
        company.setError(null);

        checkInfo = new CustomerOnline();
        checkInfo = customerOnline;
        Log.e("companyId", checkInfo.getCompanyId() + customerOnline.getCompanyId());
        if (customerOnline.getLongitudeIn() != 0 && customerOnline.getLatitudeIn() != 0)
            checkInfo.setUpdate(0);// checkout
        else
            checkInfo.setUpdate(1);// update
        checkInfo.setLongitudeIn(checkinLocation.getLongitude());
        checkInfo.setLatitudeIn(checkinLocation.getLatitude());
        checkInfo.setVisitReportImage("");
        date = Calendar.getInstance().getTime();
        checkInfo.setCheakInTime(format.format(date));
//                        startTimer();
        company.setText(customerOnline.getCompanyName());
        problem.setText(customerOnline.getProblem());
        phone.setText(customerOnline.getPhoneNo());
        system.setText(customerOnline.getSystemName());
        customer.setText(customerOnline.getCustomerName());


        managerLayoutTransfer=new ManagerLayout();
        managerLayoutTransfer.setProplem(checkInfo.getProblem());
        managerLayoutTransfer.setCustomerName(checkInfo.getCustomerName());
        managerLayoutTransfer.setCheakInTime(checkInfo.getCheakInTime());
        managerLayoutTransfer.setCompanyName(checkInfo.getCompanyName());
        managerLayoutTransfer.setPhoneNo(checkInfo.getPhoneNo());
        managerLayoutTransfer.setSystemName(checkInfo.getSystemName());
        managerLayoutTransfer.setSystemId(checkInfo.getSystemId());
        managerLayoutTransfer.setCallCenterId(checkInfo.getCallCenterId());
        managerLayoutTransfer.setCallCenterName(checkInfo.getCallCenterName());
        managerLayoutTransfer.setEngId(checkInfo.getEngineerID());
        managerLayoutTransfer.setEnginerName(checkInfo.getEngineerName());
        managerLayoutTransfer.setState("0");
        managerLayoutTransfer.setSerial(checkInfo.getSerial());
        managerLayoutTransfer.setOriginalSerial(checkInfo.getOriginalSerial());


    }

    public void dumpData(String response) {
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                presenterClass.getCustomersData(TechnicalActivity.this);
//            }
//
//        }, 0, 60000);
        dismissDialog();
        if (response.contains("PROBLEM_SOLVED SUCCESS")) {
            company.setText("");
            customer.setText("");
            system.setText("");
            phone.setText("");
            problem.setText("");
            tProblem.setText("");
            visitReportPic.setImageDrawable(getResources().getDrawable(R.drawable.call_center_boy));
            checkInfo = new CustomerOnline();
            checkInfo.setVisitReportImage(null);
            customerOnline = new CustomerOnline();
        } else
            Toast.makeText(this, "Checkout failed!", Toast.LENGTH_SHORT).show();
    }

    private double distance(Location locationOne, Location locationTwo) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1))
//                * Math.sin(deg2rad(lat2))
//                + Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;

        double dist = -1;
//        if (checkinLocation != null && checkoutLocation != null)
        dist = locationOne.distanceTo(locationTwo);

        Toast.makeText(this, "" + dist + "m", Toast.LENGTH_SHORT).show();
        Log.e("cccccdistance", "" + dist + "/between: one: " + locationOne.getLongitude() + "**" + locationOne.getLatitude());
        Log.e("cccccdistance", "" + dist + "/between: two: " + locationTwo.getLongitude() + "**" + locationTwo.getLatitude());

        return (dist);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLocation(CustomerOnline customerOnline,int statusFlag) {
        this.customerOnline = customerOnline;
        flag = 1;
        if(statusFlag==1) {//in normal case  no select any thing
            checkIn(null,0);
        }else if(statusFlag==2){ // when check in order then close system
            firstCheckIn();
        }else if(statusFlag==3){
            checkIn(null,1);
        }
//        startLocationUpdates();
//        isCheckIn = true;
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//
////            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//            if (ActivityCompat.checkSelfPermission(TechnicalActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TechnicalActivity.this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        10);
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//
//
//            locationClass.onLocationChanged(location);
//            isCheckIn = 1;
//        }

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //    class LocationClass implements LocationListener {//LocationListener
//
//        @Override
//        public void onLocationChanged(Location location) {
//            try {
//                double longitude = location.getLongitude();
//                double latitude = location.getLatitude();
//                if (isCheckIn == 1) {
////                checkinLocation = location;
//                    Log.e("checkin", ":long:" + longitude + "/lat:" + latitude);
//                    checkIn(location);
//                } else if (isCheckIn == 2) {
////                checkoutLocation = location;
//                    Log.e("checkout", ":long:" + longitude + "/lat:" + latitude);
////                    checkOut(location);
//                }
////            showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
//            } catch (Exception e) {
//                Log.e("exception", "" + e.getMessage());
//            }
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle bundle) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String s) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String s) {
//
//        }
    protected void startLocationUpdates() {
//        Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this
//                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, locationClass);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, locationClass);
//        if (flag)
//            locationClass.onLocationChanged(locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER));
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, callback,
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        if (flag == 1) {// checkin
            longiOne = location.getLongitude();
            latOne = location.getLatitude();
            checkIn(location,0);

        } else if (flag == 2) {
            longiTwo = location.getLongitude();
            latTwo = location.getLatitude();
        }
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//        findDistance(location.getLongitude(), location.getLatitude());
        // You can now create a LatLng Object for use with maps
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void openLargePicDialog(Bitmap pic) {
        if (checkInfo.getVisitReportImage() == null)
            Toast.makeText(this, "No visit report found!", Toast.LENGTH_SHORT).show();
        else {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.pic_dialog);
            dialog.setCanceledOnTouchOutside(true);

            ImageView imageView = dialog.findViewById(R.id.main_pic);
            if (pic != null)
                imageView.setImageBitmap(pic);

            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (checkInfo.getSerial() == null) {
            timer.cancel();
            isOpenFirstTime=true;
            super.onBackPressed();
        } else
            attentionDialog("You're checked in, Please checkout first!", SweetAlertDialog.WARNING_TYPE, 0);
    }

    void startTimer() {
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
    }

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            int hour = minutes / 60;

            seconds = seconds % 60;
//            int milliseconds = (int) (finalTime % 1000);
            clock.setText(String.format("%02d", hour) + ":" + String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds)
            );// + ":" + String.format("%03d", milliseconds)
            if (clock.getText().equals("01:00:00") || clock.getText().equals("02:00:00") || clock.getText().equals("03:00:00")) {
                MediaPlayer mp = new MediaPlayer();
                if (mp != null) {
                    mp = MediaPlayer.create(TechnicalActivityOnline.this, R.raw.tone2);
                    mp.start();
                }
            }
//            if (minutes%2 == 1 && seconds == 01){
////                Toast.makeText(TechnicalActivity.this, "one minute", Toast.LENGTH_SHORT).show();
//
//                startLocationUpdates(false);
//            }
            myHandler.postDelayed(this, 0);
        }

    };

    public void fillRecyclerData(List<CustomerOnline> list) {
        if (list.size() > 0) {
            emptyListTV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            adapter = new TechnicalRecyclerAdapterOnline(list, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            emptyListTV.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void fillRecyclerDataDanger(List<CustomerOnline> list) {
        if (list.size() > 0) {
            //emptyListTV.setVisibility(View.GONE);
           // recyclerView.setVisibility(View.VISIBLE);
            Log.e("tec_1",""+list.size());
            recyclerViewDanger.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            adapterDanger = new TechnicalRecyclerAdapterOnlineDanger(list, this);
            recyclerViewDanger.setAdapter(adapterDanger);
            adapterDanger.notifyDataSetChanged();
        } else {
            Log.e("tec_2",""+list.size());
            adapterDanger = new TechnicalRecyclerAdapterOnlineDanger(list, this);
            recyclerViewDanger.setAdapter(adapterDanger);
           //// emptyListTV.setVisibility(View.VISIBLE);
            //recyclerViewDanger.setVisibility(View.GONE);
        }
    }

    public void fillSystemsDialog(final List<Systems> listOfsystem) {

        final Dialog systemDialog = new Dialog(TechnicalActivityOnline.this, R.style.Theme_Dialog);
        systemDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        systemDialog.setCancelable(true);
        systemDialog.setContentView(R.layout.sys_dialog);
//                    dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bac_list_3_1)); // transpa

        GridView SysGrid;
        SysGrid = systemDialog.findViewById(R.id.Sysgrid);
        adapterGridSystem adapterSystem = new adapterGridSystem(this, listOfsystem);
        SysGrid.setAdapter(adapterSystem);
        system.setMovementMethod(new ScrollingMovementMethod());

        SysGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                system.setText(listOfsystem.get(position).getSystemName());
                systemDialog.dismiss();


            }
        });

        systemDialog.show();
    }

    public void attentionDialog(String message, int theme, int image) {
        attentionDialog = new SweetAlertDialog(this, theme);
        attentionDialog.setContentText(message);
        if (image != 0)
            attentionDialog.setCustomImage(image);
        attentionDialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 100);
            Log.e("camera", "1");
//        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1888);

            Log.e("camera", "2");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {// && permission1 != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED
            Log.e("camera", "3");
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return;
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {

//            Log.e("camera", "4");
//            Bundle intent = data.getExtras();
//            bitmap = (Bitmap) data.getExtras().get("data");
////            bitmap = getResizedBitmap(bitmap, 100, 100);
//            File pictureFile;
//
//            if (intent != null) {
//                visitReportPic.setImageBitmap(bitmap);
//                checkInfo.setVisitReportImage(bitMapToString(bitmap));
//            }

            if (data != null) {
                image = data.getData();
                visitReportPic.setImageURI(image);
//                CheckPic.setVisibility(View.VISIBLE);
            }
            if (image == null && mCameraFileName != null) {
                image = Uri.fromFile(new File(mCameraFileName));
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png";
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png");
                visitReportPic.setImageBitmap(bitmap);
                checkInfo.setVisitReportImage(bitMapToString(bitmap));
                deleteFiles(path);
//                CheckPicText.setError(null);
            }
            File file = new File(mCameraFileName);
            if (!file.exists()) {
                file.mkdir();
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png";
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png");
                visitReportPic.setImageBitmap(bitmap);
                checkInfo.setVisitReportImage(bitMapToString(bitmap));
                deleteFiles(path);
            } else {

                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png";
                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png");
                visitReportPic.setImageBitmap(bitmap);
                checkInfo.setVisitReportImage(bitMapToString(bitmap));
                deleteFiles(path);

            }
        }

//        if (requestCode == 2) {
//            if (data != null) {
//                image = data.getData();
//                visitReportPic.setImageURI(image);
////                CheckPic.setVisibility(View.VISIBLE);
//            }
//            if (image == null && mCameraFileName != null) {
//                image = Uri.fromFile(new File(mCameraFileName));
//                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png";
//                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png");
//                visitReportPic.setImageBitmap(bitmap);
//                checkInfo.setVisitReportImage(bitMapToString(bitmap));
//                deleteFiles(path);
////                CheckPicText.setError(null);
//            }
//            File file = new File(mCameraFileName);
//            if (!file.exists()) {
//                file.mkdir();
//                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png";
//                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png");
//                visitReportPic.setImageBitmap(bitmap);
//                checkInfo.setVisitReportImage(bitMapToString(bitmap));
//                deleteFiles(path);
////                    Bitmap bitmap1 = StringToBitMap(serverPic);
////                    showImageOfCheck(bitmap1);
//            } else {
//
//                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png";
////                BitmapFactory.Options options = new BitmapFactory.Options();
////                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
////                serverPicBitmap = BitmapFactory.decodeFile(path, options);
//                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/in.png");
//                visitReportPic.setImageBitmap(bitmap);
//                checkInfo.setVisitReportImage(bitMapToString(bitmap));
//                deleteFiles(path);
////                Bitmap bitmap1 = StringToBitMap(serverPic);
////                showImageOfCheck(bitmap1);
//
//            }
//        }

    }


    public void deleteFiles(String path) {
        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLocation(customerOnline,1);
//                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                        mGoogleApiClient);

            }
        } else if (requestCode == 2) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("jj4", "Permission: " + permissions[0] + "was " + grantResults[0]);
                //resume tasks needing this permission
//            if(flagINoUT==1){
//                ExportDbToExternal();
//            }else if (flagINoUT==2){
//                ImportDbToMyApp();
//            }

                cameraIntent();

            }

        }
    }

    public String bitMapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] arr = baos.toByteArray();
            String result = Base64.encodeToString(arr, Base64.DEFAULT);
            return result;
        }

        return "";
    }

    //    public void ShowNotification() {
//        notifyThis("Master", "belcin");
//        blinking();
//    }
//
//    public void StopNotification() {
////        notifyThis("Master","belcin");
////        mp.stop();
//    }
//
//    void blinking() {
//
//        Animation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(100); //You can manage the blinking time with this parameter
//        anim.setStartOffset(20);
//        anim.setRepeatMode(Animation.REVERSE);
//        anim.setRepeatCount(Animation.INFINITE);
//        online_new_customer.startAnimation(anim);
//        animations = anim;
//    }
//
//    public void showCustomerLinear(CustomerOnline customerOnline) {
//        new_customer.setVisibility(View.GONE); // button
//        onlineImage.setVisibility(View.GONE); // image
//        customerLayout.setVisibility(View.VISIBLE); // customer fields
//        gifImageView.setVisibility(View.VISIBLE); // timer
//
//        Log.e("trrrr", "master");
//        phoneNo.setText(customerOnline.getPhoneNo());
//        system.setText(customerOnline.getSystemName());
//        username.setText(customerOnline.getCustomerName());
//
//        onlineEditor = onlineSharedPreferences.edit();
//        onlineEditor.putString(ONLINE_CHECH_IN_TIME, customerOnline.getCheakInTime());
//        onlineEditor.putString(ONLINE_COMPANY_NAME, customerOnline.getCompanyName());
//        onlineEditor.putString(ONLINE_CUST_NAME, customerOnline.getCustomerName());
//        onlineEditor.putString(ONLINE_PHONE_NO, customerOnline.getPhoneNo());
//        onlineEditor.putString(ONLINE_SYSTEM_NAME, customerOnline.getSystemName());
//        onlineEditor.putString(ONLINE_SYS_ID, customerOnline.getSystemId());
//        onlineEditor.putString(ONLINE_SERIAL, customerOnline.getSerial());
//        onlineEditor.commit();
//
//    }
//
//    public void hideCustomerLinear() {
//        final String engId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "null");
//        presenterClass.setState(engId, 0);// checkout
//        snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#3167F0\">Checked out Successfully</font>"), Snackbar.LENGTH_SHORT);
//        View snackbarLayout = snackbar.getView();
//        TextView textViewSnackbar = snackbarLayout.findViewById(R.id.snackbar_text);
//        textViewSnackbar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
//        textViewSnackbar.setCompoundDrawablePadding(15);
//        snackbar.show();
//        phoneNo.setText("");
//        username.setText("");
//        system.setText("");
//        problem.setText("");
//        new_customer.setVisibility(View.VISIBLE);
//        onlineImage.setVisibility(View.VISIBLE);
//        customerLayout.setVisibility(View.GONE);
//        gifImageView.setVisibility(View.GONE);
//        isTimerWork = true;
//    }
//
//    public void notifyThis(String title, String message) {
//        NotificationCompat.Builder b = new NotificationCompat.Builder(OnlineActivity.this);
//        b.setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setSmallIcon(R.drawable.icon2_f);
////                .setWhen(System.currentTimeMillis())
////                .setTicker("{your tiny message}")
////                .setContentTitle(title)
////                .setContentText(message)
////                .setContentInfo("INFO");
//
//        NotificationManager nm = (NotificationManager) OnlineActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.notify(1, b.build());
//    }
//
//
//    private void fillEngineerInfoList() {
////        if(TextUtils.isEmpty(ipAddres)){
////            Toast.makeText(this, "ip Not Found,Please Add Ip", Toast.LENGTH_SHORT).show();
////        }
//        databaseHandler=new DatabaseHandler(OnlineActivity.this);
//        String ipAddres=databaseHandler.getIp();
//        final String url = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";
//
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        try {
//
//                                try{
//                                    JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
//                                    Log.e("systemInfoArray", "" + systemInfoArray);
//                                    for (int i = 0; i < systemInfoArray.length(); i++) {
//                                        JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
//                                        Systems systemInfo = new Systems();
//                                        systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
//                                        systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));
//                                        systemsListActivity.add(systemInfo);
//                                        Log.e("textM","!!text"+systemsListActivity.get(i).getSystemName());
//                                    }
//                                }catch (Exception e){
//                                    Log.e("No_Sys       ","Exception");
//
//                                }
////                                fillSpennerSystem(systemsList);
////                                systemGridDialog(systemsList);
//
//
//
//                        } catch (Exception e) {
//                            Log.e("Exception", "" + e.getMessage());
//                        }
//                    }
//
//
//                }
//
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if ((error instanceof NoConnectionError)) {
//                    Toast.makeText(OnlineActivity.this,
//                            "تأكد من اتصال الانترنت",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//                Log.e("onErrorResponse: ", "" + error);
//            }
//
//        });
//        MySingeltone.getmInstance(OnlineActivity.this).addToRequestQueue(stringRequest);
//
//
//    }
//
//
//    private class UpdateProblemSolved extends AsyncTask<String, String, String> {
//        private String JsonResponse = null;
//        private HttpURLConnection urlConnection = null;
//        private BufferedReader reader = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                ipAdress = databaseHandler.getIp();
//                String link = "http://" + ipAdress + "//onlineTechnicalSupport/export.php";
//
//                JSONObject object = new JSONObject();
//                try {
//
//                    Log.e("problemDataurlString = ", "" + customerOnlineGlobel.getProblem());
//
//                    object.put("CHECH_OUT_TIME", "00:00:00");
//                    object.put("PROBLEM", customerOnlineGlobel.getProblem());
//                    object.put("CUST_NAME", customerOnlineGlobel.getCustomerName());
//                    object.put("CHECH_IN_TIME", customerOnlineGlobel.getCheakInTime());
//                    object.put("COMPANY_NAME", customerOnlineGlobel.getCompanyName());
//                    object.put("PHONE_NO", customerOnlineGlobel.getPhoneNo());
//                    object.put("SYSTEM_NAME", customerOnlineGlobel.getSystemName());
//                    object.put("SYS_ID", customerOnlineGlobel.getSystemId());
//                    object.put("ENG_ID", customerOnlineGlobel.getEngineerID());
//                    object.put("ENG_NAME", customerOnlineGlobel.getEngineerName());
//                    object.put("STATE", 2);
//                    object.put("SERIAL", customerOnlineGlobel.getSerial());
//
////                    object.put("CALL_CENTER_ID", "'"+customerOnlineGlobel.getCallId()+"'");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                String data = "PROBLEM_SOLVED=" + URLEncoder.encode(object.toString(), "UTF-8");
//
//                URL url = new URL(link);
//                Log.e("urlStringProblem= ", "" + url.toString());
//                Log.e("urlStringData= ", "" + data);
//                Log.e("serial12344 = ", "" + customerOnlineGlobel.getSerial());
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("POST");
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuffer stringBuffer = new StringBuffer();
//
//                while ((JsonResponse = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(JsonResponse + "\n");
//                }
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//                Log.e("tag", "ItemOCodegggppp -->" + stringBuffer.toString());
//
//                return stringBuffer.toString();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("tag", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String JsonResponse) {
//            super.onPostExecute(JsonResponse);
//
//            if (JsonResponse != null && JsonResponse.contains("PROBLEM_SOLVED SUCCESS")) {
//                Log.e("PROBLEM_SOLVED_", "****Success" + JsonResponse.toString());
//                hideCustomerLinear();
//
//            } else {
//
//                Log.e("PROBLEM_SOLVED_", "****Failed to export data");
//
//            }
//
//
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//
//    }
    private void cameraIntent() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("_mm_ss");

        String newPicFile = "in" + ".png";
        String outPath = Environment.getExternalStorageDirectory() + File.separator + newPicFile;
        Log.e("InventoryDBFolder", "" + outPath);
        File outFile = new File(outPath);
        path = outPath;
        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, 2);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.e("gg1", "Permission is granted");
                return true;
            } else {

                Log.e("gg2", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("gg3", "Permission is granted");
            return true;
        }
    }

    public void showDialog() {
        progressDialog.show();
    }

    public void dismissDialog() {
        progressDialog.dismiss();
    }


    void TransferDialog(final ManagerLayout managerLayout ){
        final Dialog dialog = new Dialog(TechnicalActivityOnline.this,R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.transfer_dialog);

        final Spinner engSpinner=dialog.findViewById(R.id.engSpinner);
        final EditText problem=dialog.findViewById(R.id.online_problem);

        final EditText reason=dialog.findViewById(R.id.online_reason);

//       fillEngineerInfoList(engSpinner);

        appSpinner=engSpinner;

        fillEngineerInfoList(1);

        FloatingActionButton addActionButton=dialog.findViewById(R.id.online_add);



       // fillSpinner(engSpinner);
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String engName="";
                try {
                    engName=managerLayout.getEnginerName();

                }catch (Exception e){
                    engName="-1";
                }

//                Toast.makeText(context, "in Online Add!", Toast.LENGTH_SHORT).show();

                try {
                    if (!engName.equals("-1")) {
                        if (!TextUtils.isEmpty(problem.getText().toString())) {
                            if (!TextUtils.isEmpty(reason.getText().toString())) {

                                EngId = Integer.parseInt(managerLayout.getEngId());
                                String problems = problem.getText().toString();
                                String reasons = reason.getText().toString();


                                String engNames = "";
                                String idEng = "";
                                int engIdPos;
                                try {
                                    engNames = engSpinner.getSelectedItem().toString();
                                    engIdPos = engSpinner.getSelectedItemPosition();
                                    idEng = engInfoTra.get(engIdPos).getId();
                                    Log.e("Transeng_idtrans", "" + idEng);
                                    Log.e("Transeng_name", "" + engNames);

                                } catch (Exception e) {
                                    idEng = managerLayout.getEngId();
                                }

                                managerLayout.setEnginerName(engNames);
                                managerLayout.setEngId(idEng);


                                //________________________transData
                                managerLayoutTransfer.setProplem(problems);
                                managerLayoutTransfer.setConvertFlag("4");//transfer to another eng
                                managerLayoutTransfer.setTransferReason(reasons);
                                managerLayoutTransfer.setTransferFlag("1");
                                managerLayoutTransfer.setTransferToEngId(idEng);
                                managerLayoutTransfer.setTransferToEngName(engNames);
                                managerLayoutTransfer.setTransferToSerial("");


                                Log.e("problemSize", "" + problems.length());


                                if (!isProbablyArabic(problems)) {
                                    if (problems.length() <= 500) {

//                                new UpdateTransferSolved(managerLayout, dialog).execute();

                                        if (!engName.equals(engNames)) {
                                            saveTrans(managerLayout, dialog);
                                        } else {
                                            Toast.makeText(TechnicalActivityOnline.this, "Can not Trans To Same Engineer", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(TechnicalActivityOnline.this, "Max Length of problem 255 Char", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (problems.length() <= 130) {

                                        if (!engName.equals(engNames)) {
                                            saveTrans(managerLayout, dialog);
                                        } else {
                                            Toast.makeText(TechnicalActivityOnline.this, "Can not Trans To Same Engineer", Toast.LENGTH_SHORT).show();
                                        }
//                                new UpdateTransferSolved(managerLayout, dialog).execute();

                                    } else {
                                        Toast.makeText(TechnicalActivityOnline.this, "Max Length of problem  130 Char", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(TechnicalActivityOnline.this, "Please add Reason first!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(TechnicalActivityOnline.this, "Please add problem first!", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }catch (Exception e){

                }


            }
        });


        dialog.show();

    }

    void saveTrans(ManagerLayout managerLayout,Dialog dialog){

        JSONObject data = null;
        try {
            data = getData(managerLayout,1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("dataTrancfer", "" + data);
        ManagerImport managerImport = new ManagerImport(TechnicalActivityOnline.this);
        managerImport.startSendingDataTranEng(data, false,8,managerLayoutTransfer,dialog,true);

    }

    private JSONObject getData(ManagerLayout managerLayout,int isTrans) throws JSONException {
        String time = "", sys_name = "", sys_Id = "";
        String customerName = "", companeyName = "", tele = "";

        customerName = managerLayout.getCustomerName();
        companeyName =  managerLayout.getCompanyName();
        int dangerStatus=0;
        if(managerLayout.getDangerStatus()==1||managerLayout.getDangerStatus()==2||managerLayout.getDangerStatus()==4){

            dangerStatus=1;
        }else {
            dangerStatus=0;
        }

//        String phoneFirst="";

//        try{
//            phoneFirst=spinnerPhone.getSelectedItem().toString();
//        }catch (Exception e){
//            phoneFirst="06";
//        }

        tele =  managerLayout.getPhoneNo();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        time = df.format(currentTimeAndDate);
//        sys_name = spenner_systems.getSelectedItem().toString();
//        if(spenner_systems.getCount()!=0){
//            sys_name = spenner_systems.getSelectedItem().toString();
//            Log.e("sys_name",""+sys_name);
//        }
        sys_name= managerLayout.getSystemName();
        sys_Id = managerLayout.getSystemId();

        String engName=managerLayout.getEnginerName();
        String engId=managerLayout.getEngId();


     //   final String CallId = LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1");
           final String CallId = managerLayout.getCallCenterId();

//        final String CallName = LoginActivity.sharedPreferences.getString(LOGIN_NAME, "-1");
        final String CallName = managerLayout.getCallCenterName();

        int callType=sharedPreferences.getInt(LOGIN_TYPE, -1);
        Log.e("call_id1",""+CallId+"    "+sys_Id +"    "+ CallName);

        JSONObject obj = new JSONObject();
if(isTrans==1) {
    obj.put("CUST_NAME", "'" + customerName + "'");
    obj.put("COMPANY_NAME", "'" + companeyName + "'");
    obj.put("SYSTEM_NAME", "'" + sys_name + "'");
    obj.put("PHONE_NO", "'" + tele + "'");
    obj.put("CHECH_IN_TIME", "'" + time + "'");
    obj.put("STATE", "'1'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
    obj.put("ENG_NAME", "'" + engName + "'");
    obj.put("ENG_ID", "'" + engId + "'");
    obj.put("SYS_ID", "'" + sys_Id + "'");
    obj.put("CHECH_OUT_TIME", "'00:00:00'");
    obj.put("PROBLEM", "'problem'");
    obj.put("CALL_CENTER_ID", "'" + CallId + "'");
    obj.put("HOLD_TIME", "'" + "00:00:00" + "'");
    obj.put("DATE_OF_TRANSACTION", "'00/00/00'");
    obj.put("SERIAL", "'" + "222" + "'");
    obj.put("CALL_CENTER_NAME", "'" + CallName + "'");
    obj.put("TRANSFER_FLAG", "'2'");
    obj.put("HOLD_REASON", "''");
    obj.put("DANGER_STATUS", dangerStatus);

    if (callType == 1 || callType == 2) {
        obj.put("TEC_TYPE", "'" + 2 + "'");
    } else if (callType == 3) {
        obj.put("TEC_TYPE", "'" + 4 + "'");
    } else if (callType == 5) {
        obj.put("TEC_TYPE", "'" + 6 + "'");
    }

    obj.put("COMPANY_ID", "'" + managerLayout.getCompanyId() + "'");

    if (managerLayout.getOriginalSerial().equals("-1") || managerLayout.getOriginalSerial().equals("-2")) {
        obj.put("ORGINAL_SERIAL", managerLayout.getSerial());
        Log.e("ORGINAL_SERIAL", "getSerial " + managerLayout.getSerial());
    } else {
        obj.put("ORGINAL_SERIAL", managerLayout.getOriginalSerial());
        Log.e("ORGINAL_SERIAL", "getOriginalSerial " + managerLayout.getSerial());
    }

}else {

    String engName2=LoginActivity.sharedPreferences.getString(LOGIN_NAME, "-1");
    String engId2= LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1");
    obj.put("CUST_NAME", "'" + customerName + "'");
    obj.put("COMPANY_NAME", "'" + companeyName + "'");
    obj.put("SYSTEM_NAME", "'" + sys_name + "'");
    obj.put("PHONE_NO", "'" + tele + "'");
    obj.put("CHECH_IN_TIME", "'" + time + "'");
    obj.put("STATE", "'" + 1 + "'");// state for companey // 1 --> check in  // 2 ---> check out  0----> hold
    obj.put("ENG_NAME", "'" + engName2 + "'");
    obj.put("ENG_ID", "'" + engId2 + "'");
    obj.put("SYS_ID", "'" + sys_Id + "'");
    obj.put("CHECH_OUT_TIME", "'00:00:00'");
    obj.put("PROBLEM", "'problem'");
    obj.put("CALL_CENTER_ID", "'" + CallId + "'");
    obj.put("HOLD_TIME", "'" + "00:00:00" + "'");
    obj.put("DATE_OF_TRANSACTION", managerLayout.getTransactionDate());
    obj.put("SERIAL", managerLayout.getSerial() );
    obj.put("CALL_CENTER_NAME", "'" + CallName + "'");
    obj.put("TRANSFER_FLAG", "'" + 0 + "'");
    obj.put("ORGINAL_SERIAL", "'-2'");
    obj.put("HOLD_REASON", "''");
    obj.put("DANGER_STATUS", "'" + 2 + "'");
    if (callType == 1|| callType == 2) {
        obj.put("TEC_TYPE", "'" + 2 + "'");
    } else if (callType == 3) {
        obj.put("TEC_TYPE", "'" + 4 + "'");
    }else if (callType == 5) {
        obj.put("TEC_TYPE", "'" + 6 + "'");
    }

    obj.put("COMPANY_ID","'"+managerLayout.getCompanyId()+"'");

}


        return obj;


    }


    public static boolean isProbablyArabic(String s) {//know if char is arabic or eng
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }


    void  fillSpinner(Spinner engSpinner){
        ArrayAdapter<String> spinnerEngAdapter=new ArrayAdapter<String>(TechnicalActivityOnline.this, R.layout.spinner_text, engStringName);
//       spinnerEngAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        engSpinner.setAdapter(spinnerEngAdapter);
//
    }


    public void fillEngineerInfoList(final int flag) {
//        if(TextUtils.isEmpty(ipAddres)){
//            Toast.makeText(this, "ip Not Found,Please Add Ip", Toast.LENGTH_SHORT).show();
//        }
        DatabaseHandler   databaseHandler = new DatabaseHandler(TechnicalActivityOnline.this);
         String ipAddres = databaseHandler.getIp();
        final String url = "http://" + ipAddres + "/onlineTechnicalSupport/import.php?FLAG=0";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (flag != 0)// 0 ---->  just first time
                            {
//                                engineerInfoList.clear();

                                JSONArray info = jsonObject.getJSONArray("ENGINEER_INFO");
                                Log.e("info", "" + info);
                                engInfoTra.clear();
                                engStringName.clear();
                                for (int i = 0; i < info.length(); i++) {
                                    JSONObject engineerInfoObject = info.getJSONObject(i);
                                    EngineerInfo engineerInfo = new EngineerInfo();
                                    engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                                    engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                                    engineerInfo.setEng_type(engineerInfoObject.getInt("ENG_TYPE"));
                                    engineerInfo.setState(engineerInfoObject.getInt("STATE"));
                                    engineerInfo.setAvailable(engineerInfoObject.getInt("AVAILABLE"));
                                    Log.e("ENG_TYPEs123 ", callType+"   " + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                    if (callType == 1||callType==2) {
                                        if (engineerInfo.getEng_type() == 2) {
                                            engInfoTra.add(engineerInfo);
                                            engStringName.add(engineerInfo.getName());

                                        }

                                        if (engineerInfo.getEng_type() == 2 && engineerInfo.getState() == 0) {
                                            engineerInfoList.add(engineerInfo);
                                            Log.e("ENG_TYPE_in", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                        }

                                    } else if (callType == 3) {


                                        if (engineerInfo.getEng_type() == 4) {
                                            engInfoTra.add(engineerInfo);
                                            engStringName.add(engineerInfo.getName());

                                        }

                                        if (engineerInfo.getEng_type() == 4 && engineerInfo.getState() == 0) {
                                            engineerInfoList.add(engineerInfo);
                                            Log.e("ENG_TYPE_in", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                        }

                                    }else if (callType == 5) {


                                        if (engineerInfo.getEng_type() == 6) {
                                            engInfoTra.add(engineerInfo);
                                            engStringName.add(engineerInfo.getName());

                                        }

                                        if (engineerInfo.getEng_type() == 6 && engineerInfo.getState() == 0) {
                                            engineerInfoList.add(engineerInfo);
                                            Log.e("ENG_TYPE_in", "" + engineerInfo.getName() + "-->" + engineerInfo.getEng_type());

                                        }

                                    }

                                }
                               // sendEngineerToAdapter();
                                fillSpinner(appSpinner);
                            } else {
                                engineerInfoList.clear();

                                try {
                                    JSONArray info = jsonObject.getJSONArray("ENGINEER_INFO");
                                    Log.e("info", "" + info);
                                    for (int i = 0; i < info.length(); i++) {
                                        JSONObject engineerInfoObject = info.getJSONObject(i);
                                        EngineerInfo engineerInfo = new EngineerInfo();
                                        engineerInfo.setName(engineerInfoObject.getString("ENG_NAME"));
                                        engineerInfo.setId(engineerInfoObject.getString("ENG_ID"));
                                        engineerInfo.setState(engineerInfoObject.getInt("STATE"));
                                        engineerInfo.setEng_type(Integer.parseInt(engineerInfoObject.getString("ENG_TYPE")));
                                        engineerInfo.setAvailable(engineerInfoObject.getInt("AVAILABLE"));

                                        if (callType == 1) {
                                            if (engineerInfo.getEng_type() == 2 && engineerInfo.getState() == 0) {
                                                engineerInfoList.add(engineerInfo);

                                            }

                                        } else if (callType == 3) {
                                            if (engineerInfo.getEng_type() == 4 && engineerInfo.getState() == 0) {
                                                engineerInfoList.add(engineerInfo);

                                            }
                                        }else if (callType == 5) {
                                            if (engineerInfo.getEng_type() == 6 && engineerInfo.getState() == 0) {
                                                engineerInfoList.add(engineerInfo);

                                            }
                                        }
//                                        if (engineerInfo.getEng_type() == 2 && engineerInfo.getState() != 0 ) {
//                                            engineerNotAvil.add(engineerInfo);
//                                        }

                                    }
                                } catch (Exception e) {
                                    Log.e("No_Eng", "Exception");

                                }
                                try {
                                    systemsList.clear();
                                    systemsList.add(new Systems("system Not Known", "-1"));
                                    JSONArray systemInfoArray = jsonObject.getJSONArray("SYSTEMS");
                                    Log.e("systemInfoArray", "" + systemInfoArray);
                                    for (int i = 0; i < systemInfoArray.length(); i++) {
                                        JSONObject systemInfoObject = systemInfoArray.getJSONObject(i);
                                        Systems systemInfo = new Systems();
                                        systemInfo.setSystemName(systemInfoObject.getString("SYSTEM_NAME"));
                                        systemInfo.setSystemNo(systemInfoObject.getString("SYSTEM_NO"));
                                        systemsList.add(systemInfo);
                                    }
                                } catch (Exception e) {
                                    Log.e("No_Sys       ", "Exception");

                                }
//                                fillSpennerSystem(systemsList);
//                                systemGridDialog(systemsList);
                               // sendEngineerToAdapter();
                            }


                        } catch (Exception e) {
                            Log.e("Exception", "" + e.getMessage());
                        }
                    }


                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((error instanceof NoConnectionError)) {
                    Toast.makeText(TechnicalActivityOnline.this,
                            "تأكد من اتصال الانترنت",
                            Toast.LENGTH_SHORT).show();
                }

                Log.e("onErrorResponse: ", "" + error);
            }

        });
        MySingeltone.getmInstance(TechnicalActivityOnline.this).addToRequestQueue(stringRequest);


    }

    public void updateDangerState(){
        String engName2=LoginActivity.sharedPreferences.getString(LOGIN_NAME, "-1");
        String engId2= LoginActivity.sharedPreferences.getString(LOGIN_ID, "-1");
        checkInfo.setEngineerID(engId2);
        checkInfo.setEngineerName(engName2);
        isOpenFirstTime=true;
        presenterClass.updateTechnicalState(TechnicalActivityOnline.this, checkInfo,2,2);


    }

    public void refreshAfterTransfer(){

        tProblem.setError(null);
        system.setError(null);
        phone.setError(null);
        customer.setError(null);
        company.setError(null);
        problem.setError(null);


        tProblem.setText("");
        system.setText("");
        phone.setText("");
        customer.setText("");
        company.setText("");
        problem.setText("");

        checkInfo = new CustomerOnline();
        managerLayoutTransfer=new ManagerLayout();

        presenterClass.getCustomersData2( TechnicalActivityOnline.this);
        presenterClass.getCustomersHoldDanger(TechnicalActivityOnline.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpenFirstTime=true;
    }
}


