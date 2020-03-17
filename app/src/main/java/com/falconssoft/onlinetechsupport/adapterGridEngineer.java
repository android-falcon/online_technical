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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.LayoutInflater.from;

public class adapterGridEngineer extends BaseAdapter {
    Context context;
    List<EngineerInfo> list_engineer;
    private static String selectedPostionName="";
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View myView = View.inflate(context, R.layout.item_rows,null);
        final TextView textView_Nmae;
        CircleImageView profile_image;
        final RelativeLayout engineerLayout;
        engineerLayout= myView.findViewById(R.id.engineerLayout);
        textView_Nmae = myView.findViewById(R.id.textView_Nmae);
        profile_image = myView.findViewById(R.id.profile_image);
        textView_Nmae.setText(list_engineer.get(i).getName());
        textView_Nmae.setTag(i);

//        textView_Nmae.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int value = (Integer)((TextView)v).getTag();
//
//                for(int i=0;i<viewGroup.getChildCount();i++)
//                {
//                    View c = viewGroup.getChildAt(i);
//                    c.findViewById(R.id.engineerLayout).setBackgroundColor(context.getResources().getColor(R.color.layer1));
//
//                }
//                engineerLayout.setBackgroundColor(context.getResources().getColor(R.color.layer4));
//                selectedPostionName=textView_Nmae.getText().toString();
//                Log.e("selectedPostionName",""+selectedPostionName);
//
//            }
//        });


        if(i%2!=0)
        {
            profile_image.setImageResource(R.drawable.call_center_gir);
        }
        return myView;
    }
}
