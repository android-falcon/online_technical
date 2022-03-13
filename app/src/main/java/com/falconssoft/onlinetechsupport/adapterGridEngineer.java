package com.falconssoft.onlinetechsupport;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.EngineerInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.LayoutInflater.from;

public class adapterGridEngineer extends BaseAdapter {
    Context context;
    List<EngineerInfo> list_engineer;
    private static String selectedPostionName="";
    adapterGridEngineer(Context context,List list_engineerInfo){
        this.context=context;
        list_engineer=new ArrayList();
        this.list_engineer=list_engineerInfo;
        Log.e("list_engineer",""+list_engineer.size());


    }
    @Override
    public int getCount() {
        return list_engineer.size();
    }

    @Override
    public Object getItem(int i) {
        return list_engineer.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View myView = View.inflate(context, R.layout.item_rows,null);
        final TextView textView_Nmae;
        CircleImageView profile_image;
        final RelativeLayout engineerLayout;
        engineerLayout= myView.findViewById(R.id.engineerLayout);
        textView_Nmae = myView.findViewById(R.id.textView_Nmae);
        profile_image = myView.findViewById(R.id.profile_image);
        Log.e("list_list_engineer",""+list_engineer.size());
        textView_Nmae.setText(list_engineer.get(i).getName());
        textView_Nmae.setTag(i);



        if(i%2!=0)
        {
            profile_image.setImageResource(R.drawable.call_center_gir);
        }
        return myView;
    }
}
