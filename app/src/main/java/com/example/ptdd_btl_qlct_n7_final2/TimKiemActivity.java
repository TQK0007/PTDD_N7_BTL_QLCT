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
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityTimKiemBinding;

public class TimKiemActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityTimKiemBinding binding;
    private Intent intent;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tim_kiem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWidget();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome();
            }
        });
    }


    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityTimKiemBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());
        imageView = binding.imageBack;


    }
    //
    private void navigateHome()
    {
        intent = new Intent(TimKiemActivity.this, MainActivity.class);
        startActivity(intent);
    }

}