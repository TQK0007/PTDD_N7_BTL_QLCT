package com.example.ptdd_btl_qlct_n7_final2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.adapter.DanhMucAdapter;
import com.example.ptdd_btl_qlct_n7_final2.dao.CategoryDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityDanhMucBinding;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityMainBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DanhMucActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityDanhMucBinding binding;
    private Intent intent;
    private ImageView imageViewBack, imageViewAdd;
    private View indicatorTC, indicatorTT;
    private TextView tvTC, tvTT;
    private ListView lvDM;

    private boolean isTienChiTab = true;
    private List<Category> categoryList=null;
    private DanhMucAdapter danhMucAdapter=null;

    private CategoryDAO categoryDAO;


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
        initQuery();
        createListView();

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome();
            }
        });
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateThemSuaDMWithAdd();
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
        lvDM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = categoryList.get(i);
                navigateThemSuaDMWithEdit(category);

            }
        });

        lvDM.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                categoryList.remove(i);
                categoryDAO.delete(categoryList.get(i));
                danhMucAdapter.notifyDataSetChanged();
                return true;
            }
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

        lvDM = binding.lvDM;
    }

    private void initQuery()
    {
        categoryDAO = AppDatabase.getInstance(this).categoryDAO();
    }

//
    private void navigateHome()
    {
        intent = new Intent(DanhMucActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateThemSuaDMWithAdd()
    {
        intent = new Intent(DanhMucActivity.this, ThemSuaDMActivity.class);
        intent.putExtra("isTienChiTab",isTienChiTab);
        intent.putExtra("isAdd",true);
        startActivity(intent);
    }

    private void navigateThemSuaDMWithEdit(Category category)
    {
        intent = new Intent(DanhMucActivity.this, ThemSuaDMActivity.class);
        intent.putExtra("isTienChiTab",isTienChiTab);
        intent.putExtra("isAdd",false);
        intent.putExtra("category",category);
        startActivity(intent);
    }


// cap nhat giao dien va giu lieu.
    private void switchTab() {

        if(isTienChiTab)
        {
            // Cập nhật UI
            tvTC.setTextColor(getResources().getColor(R.color.md_yellow_600));
            indicatorTC.setVisibility(View.VISIBLE);
            tvTT.setTextColor(getResources().getColor(R.color.black));
            indicatorTT.setVisibility(View.INVISIBLE);
            createListView();
        }
        else
        {
            // Cập nhật UI
            tvTT.setTextColor(getResources().getColor(R.color.md_yellow_600));
            indicatorTT.setVisibility(View.VISIBLE);
            tvTC.setTextColor(getResources().getColor(R.color.black));
            indicatorTC.setVisibility(View.INVISIBLE);
            createListView();
        }

    }

    private void createListView()
    {
        if(isTienChiTab) categoryList = categoryDAO.getAllByIsIncome(false);
        else categoryList = categoryDAO.getAllByIsIncome(true);
        danhMucAdapter = new DanhMucAdapter(this,R.layout.fragment_danhmuc_item,categoryList);
        lvDM.setAdapter(danhMucAdapter);
    }
}