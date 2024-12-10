package com.example.ptdd_btl_qlct_n7_final2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.dto.TransactionsDTO;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.text.SimpleDateFormat;
import java.util.List;

public class ThuChiAdapter extends ArrayAdapter {
    Activity context;
    int layoutID;
    List<TransactionsDTO> list = null;

    public ThuChiAdapter(@NonNull Activity context, int resource, @NonNull List<TransactionsDTO> objects)
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
            final ImageView imageView = convertView.findViewById(R.id.im_item_rw);
            final TextView tvNgay = convertView.findViewById(R.id.tvNgay);
            final TextView tvTongTC = convertView.findViewById(R.id.tvTongTC);
            final TextView tv_Category = convertView.findViewById(R.id.tv_Category);
            final TextView tv_money = convertView.findViewById(R.id.tv_money);


            TransactionsDTO o = list.get(position);
            Resources resources = context.getResources();
            Bitmap bitmap =convertStringToBitmap(o.getIconName());
            Drawable drawable = new BitmapDrawable(resources,bitmap);
            imageView.setImageDrawable(drawable);

            tvTongTC.setText(o.getAmount()+"");
            tv_Category.setText(o.getCategoryName());
            tv_money.setText(o.getAmount()+"");

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            String formattedDate = dateFormat.format(o.getCreatedAt());
            tvNgay.setText(formattedDate);
        }
        return  convertView;
    }

    private Bitmap convertStringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
