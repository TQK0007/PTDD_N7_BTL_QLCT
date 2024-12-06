package com.example.ptdd_btl_qlct_n7_final2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.dao.CategoryDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaDmactivityBinding;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaThuChiBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.io.ByteArrayOutputStream;

public class ThemSuaDMActivity extends AppCompatActivity {

    private ActivityThemSuaDmactivityBinding binding;
    private ImageView imageViewBack;
    private Intent intent;
    private Button btnThemDM;

    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_sua_dmactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidget();
        initQuery();
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateDanhMuc();
            }
        });
        btnThemDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Category c = new Category();
                categoryDAO.add(c);

            }
        });
    }

    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityThemSuaDmactivityBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());
        imageViewBack=binding.imageBack;
        btnThemDM = binding.btnThemDanhMuc;
    }

    private void initQuery()
    {
        categoryDAO = AppDatabase.getInstance(this).categoryDAO();
    }

    private void navigateDanhMuc()
    {
        intent = new Intent(ThemSuaDMActivity.this,DanhMucActivity.class);
        startActivity(intent);
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}