package com.example.ptdd_btl_qlct_n7_final2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.util.List;

public class DanhSachDMAdapter extends RecyclerView.Adapter<DanhSachDMAdapter.DanhSachDMViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private SelectListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;


    public DanhSachDMAdapter(Context context,List<Category> categoryList, SelectListener listener) {
        this.categoryList = categoryList;
        this.context = context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public DanhSachDMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_danhsach_danhmuc,parent,false);
        return new DanhSachDMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachDMViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = categoryList.get(position);
        holder.textView.setText(category.getCategoryName());

//        gan anh
        Resources resources = context.getResources();
        Bitmap bitmap = convertStringToBitmap(category.getIconName());
        Drawable drawable = new BitmapDrawable(resources,bitmap);
        holder.imageView.setImageDrawable(drawable);


        // Xử lý hiển thị background
        if (position == selectedPosition) {
            holder.layout.setBackgroundResource(R.drawable.border_drawable); // Chọn item hiện tại
        } else {
            holder.layout.setBackgroundResource(android.R.color.transparent); // Reset item khác
        }


//        gan su kien khi click vao 1 item
        holder.layout.setOnClickListener(view -> {

            int previousPosition = selectedPosition;
            selectedPosition = position;

            notifyItemChanged(previousPosition);    // Làm mới mục cũ
            notifyItemChanged(selectedPosition);    // Làm mới mục mới

            listener.onItemClicked(category);

        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class DanhSachDMViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        LinearLayout layout;

        public DanhSachDMViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgDSDM);
            textView = itemView.findViewById(R.id.tvDSDM);
            layout = itemView.findViewById(R.id.layout_parent);
        }
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
