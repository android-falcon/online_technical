package com.falconssoft.onlinetechsupport;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.falconssoft.onlinetechsupport.Modle.Systems;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapterGridSystem extends BaseAdapter {
    Context context;
    List<Systems> listofSystem;
    private static String selectedPostionName="";
    adapterGridSystem(Context context, List<Systems> listOfSystem){
        this.context=context;
        listofSystem =new ArrayList();
        this.listofSystem =listOfSystem;
        Log.e("list_system",""+ listofSystem.size());


    }
    @Override
    public int getCount() {
        return listofSystem.size();
    }

    @Override
    public Object getItem(int i) {
        return listofSystem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View myView = View.inflate(context, R.layout.item_sys_row,null);
        final TextView textView_Nmae;
        textView_Nmae = myView.findViewById(R.id.systemName);
        textView_Nmae.setText(listofSystem.get(i).getSystemName());
        textView_Nmae.setTag(i);


        return myView;
    }
}
