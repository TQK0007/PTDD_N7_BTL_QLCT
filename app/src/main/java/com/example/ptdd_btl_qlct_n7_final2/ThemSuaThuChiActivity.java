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

import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaThuChiBinding;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThuChiBinding;

public class ThemSuaThuChiActivity extends AppCompatActivity {

    private ActivityThemSuaThuChiBinding binding;
    private ImageView imageViewBack;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_sua_thu_chi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWidget();
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateThuChi();
            }
        });
    }

    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityThemSuaThuChiBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());

        imageViewBack = binding.imageBack;
    }

    private void navigateThuChi()
    {
        intent = new Intent(ThemSuaThuChiActivity.this,ThuChiActivity.class);
        startActivity(intent);
    }

}