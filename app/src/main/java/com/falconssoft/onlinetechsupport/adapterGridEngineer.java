package com.falconssoft.onlinetechsupport;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class adapterGridEngineer extends BaseAdapter {
    Context context;
    List<String> list_engineer;
    adapterGridEngineer(Context context,List list_engineer){
        this.context=context;
        this.list_engineer=list_engineer;

    }
    @Override
    public int getCount() {
        return list_engineer.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = View.inflate(context, R.layout.item_row,null);
        TextView green,textView_Nmae;
        CircleImageView profile_image;
//        green = myView.findViewById(R.id.green);
        textView_Nmae = myView.findViewById(R.id.textView_Nmae);
        profile_image= myView.findViewById(R.id.profile_image);
        if(i%2==0)
        {
//           green.setBackgroundResource(R.drawable.red_circle);
            textView_Nmae.setText("Eng Abeer Hayari");
            profile_image.setImageResource(R.drawable.call_center_gir);
        }
        return myView;
    }
}
