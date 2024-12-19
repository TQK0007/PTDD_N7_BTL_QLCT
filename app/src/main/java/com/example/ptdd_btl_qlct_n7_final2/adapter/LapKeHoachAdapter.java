package com.example.ptdd_btl_qlct_n7_final2.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;

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
        convertView = layoutInflater.inflate(layoutID, null);

        if (list.size() > 0 && position >= 0) {
            TextView tv_plan_goal, tv_target_amount, tv_dealine, tv_progress;
            ProgressBar progressBar;

            tv_plan_goal = convertView.findViewById(R.id.tv_plan_goal);
            tv_target_amount = convertView.findViewById(R.id.tv_target_amount);
            tv_progress = convertView.findViewById(R.id.tv_progress);
            progressBar = convertView.findViewById(R.id.progress_bar);
            tv_dealine = convertView.findViewById(R.id.tv_dealine);

            LongTermGoal o = list.get(position);
            tv_plan_goal.setText(o.getName());
            DecimalFormat df = new DecimalFormat("#,###"); // Định dạng với dấu phân cách ngàn
            tv_target_amount.setText("Mục tiêu: " + df.format(o.getTarget()) + "đ");
            tv_progress.setText("Tiến độ: " + df.format(o.getProgress()) + "đ");

            // Format deadline sang dd/MM/yyyy
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(o.getDeadline());
            tv_dealine.setText(formattedDate);

            // Tính toán % progress
            double progressPercentage = (o.getProgress() / o.getTarget()) * 100;
            progressBar.setProgress((int) progressPercentage);

            // Lấy ngày hiện tại
            Date currentDate = new Date();

            // Kiểm tra nếu đã quá hạn mà progress < 100%
            if (o.getDeadline().before(currentDate) && progressPercentage < 100) {
                tv_plan_goal.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                tv_plan_goal.setTypeface(null, Typeface.BOLD);
            } else if (progressPercentage >= 100) {
                // Hiện icon hoàn thành
                ImageView ivStatus = convertView.findViewById(R.id.iv_status);
                ivStatus.setVisibility(View.VISIBLE);

                // Đổi màu và kiểu chữ cho tiêu đề
                tv_plan_goal.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                tv_plan_goal.setTypeface(null, Typeface.BOLD);
            } else {

                tv_plan_goal.setTypeface(null, Typeface.NORMAL);
            }
        }
        return convertView;
    }


}
