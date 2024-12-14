package com.example.ptdd_btl_qlct_n7_final2.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.dao.CategoryDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaDmactivityBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ThemSuaDMActivity extends AppCompatActivity {

    private ActivityThemSuaDmactivityBinding binding;
    private ImageView imageViewBack;
    private Intent intent, intentFromDMA;
    private Button btnThemDM;
    private TextView tvTitle;


    private boolean isTienChiTab, isAdd;
    private List<ImageView> imageViewList=null;

    private String iconNameCategory ="";

    private Category editCategory=null;
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

        intentFromDMA=getIntent();


        getWidget();
        configView();
        initQuery();
        registerOnClickForAllImageView();



        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateDanhMuc();
            }
        });
        btnThemDM.setOnClickListener(view -> {
            if(intentFromDMA!=null) isTienChiTab = intentFromDMA.getBooleanExtra("isTienChiTab",true);
            String categoryName = binding.editNote.getText().toString();
            if(categoryName.isEmpty() || iconNameCategory.isEmpty())
            {
                Toast.makeText(this,"Vui lòng nhập tên danh mục và chọn ảnh",Toast.LENGTH_SHORT).show();
                return;
            }

            if(isAdd)
            {
                boolean isIncome = !isTienChiTab;
                Category newCategory = new Category
                        (
                                categoryName,
                                iconNameCategory,
                                isIncome
                        );
                categoryDAO.add(newCategory);
            }
            else
            {
                editCategory.setCategoryName(categoryName);
                editCategory.setIconName(iconNameCategory);
                categoryDAO.update(editCategory);
            }
            navigateDanhMuc();
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
        tvTitle=binding.tvTitle;
    }

    private void initQuery()
    {
        categoryDAO = AppDatabase.getInstance(this).categoryDAO();
    }

//    Cấu hình động vì giao diện thêm và sửa dùng chung
    private void configView()
    {

        if(intentFromDMA!=null)
        {
            isAdd = intentFromDMA.getBooleanExtra("isAdd",true);
            editCategory = (Category) intentFromDMA.getSerializableExtra("category");

        }
        if(isAdd)
        {
            tvTitle.setText("Thêm danh mục");
            btnThemDM.setText("Thêm");
        }
        else
        {
            tvTitle.setText("Sửa danh mục");
            btnThemDM.setText("Sửa");
        }
        if(editCategory!=null)
        {
            binding.editNote.setText(editCategory.getCategoryName());
        }
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



    private void registerOnClickForAllImageView()
    {
//        Khoi tao danh sach cac imageView
        imageViewList = new ArrayList<>();
        imageViewList.add(binding.imgSalaryTDMT);
        imageViewList.add(binding.imgGiftTDMT);
        imageViewList.add(binding.imgInvestTDMT);
        imageViewList.add(binding.imgFoodTDMT);
        imageViewList.add(binding.imgEduTDMT);
        imageViewList.add(binding.imgElectricityTDMT);
        imageViewList.add(binding.imgMedicineTDMT);
        imageViewList.add(binding.imgTravelTDMT);
        imageViewList.add(binding.imgClothesTDMT);
        imageViewList.add(binding.imgWifiTDMT);
        imageViewList.add(binding.imgSaveTDMT);

//        Dang ky su kien onclick cho cac imageView
        imageViewList.forEach(imageView -> {
            imageView.setOnClickListener(view -> {

                // Đặt lại background của tất cả LinearLayout cha về mặc định
                imageViewList.forEach(img->{
                    LinearLayout parentLayout = (LinearLayout) img.getParent();
                    parentLayout.setBackgroundResource(0); // 0: không có background
                });

                // Đổi background của LinearLayout chứa ImageView được click
                LinearLayout layout = (LinearLayout) imageView.getParent();
                layout.setBackgroundResource(R.drawable.border_drawable);

                Drawable drawable = imageView.getDrawable();
                if(drawable instanceof BitmapDrawable)
                {
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    iconNameCategory = convertBitmapToString(bitmap);
                }
            });
        });
    }

}