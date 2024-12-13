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
import com.example.ptdd_btl_qlct_n7_final2.dto.CategoryDTO;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.text.DecimalFormat;
import java.util.List;

public class DanhSachTKAdapter extends ArrayAdapter {
    Activity context;
    int layoutID;
    List<CategoryDTO> list = null;

    public DanhSachTKAdapter(@NonNull Activity context, int resource, @NonNull List<CategoryDTO> objects)
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
            final ImageView imageView = convertView.findViewById(R.id.im_DMTK_item);
            final TextView tv_title_DMTK_item = convertView.findViewById(R.id.tv_title_DMTK_item);
            final TextView tv_DMTK_amount = convertView.findViewById(R.id.tv_DMTK_amount);
            final TextView tv_DMTK_percent = convertView.findViewById(R.id.tv_DMTK_percent);

            CategoryDTO o = list.get(position);
            Resources resources = context.getResources();
            Bitmap bitmap =convertStringToBitmap(o.getIconName());
            Drawable drawable = new BitmapDrawable(resources,bitmap);
            imageView.setImageDrawable(drawable);

            double totalAmout = 0;
            for(CategoryDTO c:list) totalAmout+=c.getAmount();

            tv_title_DMTK_item.setText(o.getCategoryName());
            tv_DMTK_amount.setText(o.getAmount()+"");

            DecimalFormat format = new DecimalFormat("#.0");
            String formattedText = format.format( (o.getAmount()*100) / totalAmout );
            tv_DMTK_percent.setText(formattedText+"%");
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
