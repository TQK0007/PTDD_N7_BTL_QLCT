package com.example.ptdd_btl_qlct_n7_final2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityDanhMucBinding;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

public class DanhMucActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityDanhMucBinding binding;
    private Intent intent;
    private ImageView imageViewBack, imageViewAdd;
    private View indicatorTC, indicatorTT;
    private TextView tvTC, tvTT;
    private boolean isTienChiTab = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danh_muc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWidget();
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome();
            }
        });
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateThemSuaDM();
            }
        });

        tvTC.setOnClickListener(view -> {
            isTienChiTab=true;
            switchTab();
        });
        tvTT.setOnClickListener(view -> {
            isTienChiTab=false;
            switchTab();
        });

    }

    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityDanhMucBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());
        imageViewAdd = binding.imageAdd;
        imageViewBack = binding.imageBack;

        indicatorTC = binding.indicatorTc;
        indicatorTT = binding.indicatorTt;
        tvTC = binding.tvTC;
        tvTT = binding.tvTT;




    }
//
    private void navigateHome()
    {
        intent = new Intent(DanhMucActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateThemSuaDM()
    {
        intent = new Intent(DanhMucActivity.this, ThemSuaDMActivity.class);
        startActivity(intent);
    }

    private void displayImgResource()
    {

    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void switchTab() {

        if(isTienChiTab)
        {
            // Cập nhật UI
            tvTC.setTextColor(getResources().getColor(R.color.md_yellow_600));
            indicatorTC.setVisibility(View.VISIBLE);
            tvTT.setTextColor(getResources().getColor(R.color.black));
            indicatorTT.setVisibility(View.INVISIBLE);
        }
        else
        {
            // Cập nhật UI
            tvTT.setTextColor(getResources().getColor(R.color.md_yellow_600));
            indicatorTT.setVisibility(View.VISIBLE);
            tvTC.setTextColor(getResources().getColor(R.color.black));
            indicatorTC.setVisibility(View.INVISIBLE);
        }

    }


}