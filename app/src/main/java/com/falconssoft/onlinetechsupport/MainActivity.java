package com.falconssoft.onlinetechsupport;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    List<ManagerLayout> cheakIn,cheakout,hold;

    ListView listCheakIn,listCheakout,holds ;
    ImageView falcon;
    Animation animZoom;
    Timer  T;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cheakIn=new ArrayList<>();
        cheakout=new ArrayList<>();
        hold=new ArrayList<>();
        listCheakIn=findViewById(R.id.listCheakIn);
        listCheakout=findViewById(R.id.listCheakout);
        holds=findViewById(R.id.hold);

//        animZoom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
//        falcon.startAnimation(animZoom);
         falcon=findViewById(R.id.falcon);
        final Animation  animZoom = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom);
        falcon.startAnimation(animZoom);


        for(int i=0;i<20;i++){
            ManagerLayout managerLayout =new ManagerLayout();
            managerLayout.setCompanyName("Falcons Soft");
            managerLayout.setCheakInTime("10:21 AM"+i);
            managerLayout.setEnginerName("Eng.Name");
            cheakIn.add(managerLayout);
        }

        for(int i=0;i<20;i++){
            ManagerLayout managerLayout =new ManagerLayout();
            managerLayout.setCompanyName("Falcons Soft");
            managerLayout.setCheakInTime("15:21 AM"+i);
            managerLayout.setEnginerName("Eng.Name");
            cheakout.add(managerLayout);
        }

        for(int i=0;i<20;i++){
            ManagerLayout managerLayout =new ManagerLayout();
            managerLayout.setCompanyName("Falcons Soft");
            managerLayout.setCheakInTime("12:21 AM"+i);
            managerLayout.setEnginerName("Eng.Name");
            hold.add(managerLayout);
        }
        ManagerLayOutAdapter managerLayOutAdapter1;
        managerLayOutAdapter1=new ManagerLayOutAdapter(MainActivity.this,cheakIn,0);
        listCheakIn.setAdapter(managerLayOutAdapter1);
        ManagerLayOutAdapter managerLayOutAdapter2;
        managerLayOutAdapter2=new ManagerLayOutAdapter(MainActivity.this,hold,1);
        listCheakout.setAdapter(managerLayOutAdapter2);
        ManagerLayOutAdapter managerLayOutAdapter3;
        managerLayOutAdapter3=new ManagerLayOutAdapter(MainActivity.this,cheakout,2);
        holds.setAdapter(managerLayOutAdapter3);



    }
}
