package com.example.ptdd_btl_qlct_n7_final2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
    }

    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityThuChiBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());
        imageViewBack = binding.imageBack;
        imageViewAdd = binding.imageAdd;

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
}