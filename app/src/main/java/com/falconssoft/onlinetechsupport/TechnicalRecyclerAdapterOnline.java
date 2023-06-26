package com.falconssoft.onlinetechsupport;

import static com.falconssoft.onlinetechsupport.TechnicalActivityOnline.isOpenFirstTime;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.onlinetechsupport.Modle.CustomerOnline;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TechnicalRecyclerAdapterOnline extends RecyclerView.Adapter<TechnicalRecyclerAdapterOnline.TechnicalViewHolder> {

    private List<CustomerOnline> list;
    private TechnicalActivityOnline technicalActivity;
    MediaPlayer mp;
    public TechnicalRecyclerAdapterOnline(List<CustomerOnline> list, TechnicalActivityOnline technicalActivity) {
        this.list = list;
        this.technicalActivity = technicalActivity;
        mp = MediaPlayer.create(technicalActivity, R.raw.danger_alarm);
    }

    @NonNull
    @Override
    public TechnicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.technical_customers_row, parent, false);
        return new TechnicalViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final TechnicalViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        holder.company.setText(list.get(i).getCompanyName());
        holder.customer.setText(list.get(i).getCustomerName());
        holder.phone.setText(list.get(i).getPhoneNo());
        holder.problem.setText(list.get(i).getProblem());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                technicalActivity.getLocation(list.get(i),1);
               try {
                   mp.stop();
                   mp.release();
               }catch (Exception e){

               }
            }
        });

        if((list.get(i).getDangerStatus()==2||list.get(i).getDangerStatus()==3)&isOpenFirstTime){

            technicalActivity.getLocation(list.get(i),2);
            isOpenFirstTime=false;
            if(list.get(i).getDangerStatus()==2){

                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                holder.image.startAnimation(anim);
            }
        }

        if(list.get(i).getDangerStatus()==4||list.get(i).getDangerStatus()==5){

            holder.TecLinear.setBackground(technicalActivity.getResources().getDrawable(R.drawable.shadow_wait));
        }

        if(list.get(i).getDangerStatus()==1){
            //notification when only one danger is found we will

            try {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(technicalActivity, R.raw.danger_alarm);

                } mp.start();
            } catch(Exception e) { e.printStackTrace(); }

            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(50);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            holder.image.startAnimation(anim);

            Toast.makeText(technicalActivity, "danger"+i, Toast.LENGTH_SHORT).show();
        }else {

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TechnicalViewHolder extends RecyclerView.ViewHolder{
        TextView company, customer, phone, problem;
        CircleImageView image;
        LinearLayout TecLinear;

        public TechnicalViewHolder(@NonNull View itemView) {
            super(itemView);

            company = itemView.findViewById(R.id.technicalRow_company);
            customer = itemView.findViewById(R.id.technicalRow_customer);
            phone = itemView.findViewById(R.id.technicalRow_phone);
            problem = itemView.findViewById(R.id.technicalRow_problem);
            image = itemView.findViewById(R.id.technicalRow_image);
            TecLinear=itemView.findViewById(R.id.TecLinear);

        }
    }
}
