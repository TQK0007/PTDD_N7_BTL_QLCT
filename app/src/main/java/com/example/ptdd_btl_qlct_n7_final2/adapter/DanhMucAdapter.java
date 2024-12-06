package com.example.ptdd_btl_qlct_n7_final2.adapter;

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
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.util.List;

public class DanhMucAdapter extends ArrayAdapter {
    Activity context;
    int layoutID;
    List<Category> list = null;

    public DanhMucAdapter(@NonNull Activity context, int resource, @NonNull List<Category> objects)
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
            final ImageView imageView = convertView.findViewById(R.id.im_DM_item_rw);
            final TextView title_DM_item = convertView.findViewById(R.id.tv_title_DM_item);

            Category o = list.get(position);

            Resources resources = context.getResources();
            Bitmap bitmap =convertStringToBitmap(o.getIconName());
            Drawable drawable = new BitmapDrawable(resources,bitmap);
            imageView.setImageDrawable(drawable);
            title_DM_item.setText(o.getCategoryName());

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
