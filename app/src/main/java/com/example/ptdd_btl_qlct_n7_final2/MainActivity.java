package com.example.ptdd_btl_qlct_n7_final2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private ImageView imageView;
    private Context currentContext = this;
    private Button btnGui;
    private TextView tvTC, tvTT;
    private View indicatorTC, indicatorTT;
    private boolean isTienChiTab=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        getWidget();
        tvTC.setOnClickListener(view -> {
            isTienChiTab=true;
            switchTab();
        });
        tvTT.setOnClickListener(view -> {
            isTienChiTab=false;
            switchTab();
        });

    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        if(menu instanceof MenuBuilder){
            @SuppressLint("RestrictedApi") MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu==R.id.menu_TimKiem) navigate_TimKiem();
        else if(id_menu==R.id.menu_ThuChi) navigate_ThuChi();
        else if (id_menu==R.id.menu_DanhMuc) navigate_DanhMuc();
        else if (id_menu==R.id.menu_LapKH) navigate_LapKeHoach();
        else finishAffinity();
        return super.onOptionsItemSelected(item);
    }

    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        imageView = binding.imageAdd;
        indicatorTC = binding.indicatorTc;
        indicatorTT = binding.indicatorTt;

        tvTC=binding.tvTC;
        tvTT=binding.tvTT;

    }

    private void navigate_TimKiem() {
        Intent intent = new Intent(MainActivity.this, TimKiemActivity.class);
        startActivity(intent);
    }

    private void navigate_LapKeHoach() {
        Intent intent = new Intent(MainActivity.this, LapKeHoachActivity.class);
        startActivity(intent);
    }

    private void navigate_DanhMuc() {
        Intent intent = new Intent(MainActivity.this, DanhMucActivity.class);
        startActivity(intent);
    }

    private void navigate_ThuChi() {
        Intent intent = new Intent(MainActivity.this, ThuChiActivity.class);
        startActivity(intent);
    }




//    Khoi tao du lieu ban dau
    private void createData()
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