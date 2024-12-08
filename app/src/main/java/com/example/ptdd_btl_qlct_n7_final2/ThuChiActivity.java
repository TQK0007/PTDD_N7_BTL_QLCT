package com.example.ptdd_btl_qlct_n7_final2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityDanhMucBinding;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThuChiBinding;

public class ThuChiActivity extends AppCompatActivity {

//    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityThuChiBinding binding;
    private Intent intent;
    private ImageView imageViewBack, imageViewAdd;

    private TextView tvTC, tvTT;
    private View indicatorTC, indicatorTT;
    private boolean isTienChiTab=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thu_chi);
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
                navigateThemSuaTC();
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
        binding = ActivityThuChiBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());
        imageViewBack = binding.imageBack;
        imageViewAdd = binding.imageAdd;

        indicatorTC = binding.indicatorTc;
        indicatorTT = binding.indicatorTt;

        tvTC=binding.tvTC;
        tvTT=binding.tvTT;

    }

    private void navigateHome()
    {
        intent = new Intent(ThuChiActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateThemSuaTC()
    {
        intent = new Intent(ThuChiActivity.this, ThemSuaThuChiActivity.class);
        startActivity(intent);
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