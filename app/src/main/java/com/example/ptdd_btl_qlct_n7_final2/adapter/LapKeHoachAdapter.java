package com.example.ptdd_btl_qlct_n7_final2.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;

import java.util.List;

public class LapKeHoachAdapter extends ArrayAdapter {
    Activity context;
    int layoutID;
    List<LongTermGoal> list = null;

    public LapKeHoachAdapter(@NonNull Activity context, int resource, @NonNull List<LongTermGoal> objects)
    {
        super(context,resource,objects);
        this.context = context;
        this.layoutID = resource;
        this.list = objects;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        convertView =   layoutInflater.inflate(layoutID,null);
        if(list.size() > 0 && position >=0)
        {

            TextView tv_plan_goal, tv_target_amount,tv_progress;
            ProgressBar progressBar;
            tv_plan_goal = convertView.findViewById(R.id.tv_plan_goal);
            tv_target_amount = convertView.findViewById(R.id.tv_target_amount);
            tv_progress = convertView.findViewById(R.id.tv_progress);
            progressBar = convertView.findViewById(R.id.progress_bar);

            LongTermGoal o = list.get(position);
            tv_plan_goal.setText(o.getName());
            tv_target_amount.setText(o.getTarget()+"");
            progressBar.setProgress((int)o.getProgress());

        }
        return  convertView;
    }
}
