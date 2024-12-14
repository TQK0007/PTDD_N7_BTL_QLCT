package com.example.ptdd_btl_qlct_n7_final2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.adapter.ThuChiAdapter;
import com.example.ptdd_btl_qlct_n7_final2.dao.TransactionsDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityTimKiemBinding;
import com.example.ptdd_btl_qlct_n7_final2.dto.TransactionsDTO;

import java.util.ArrayList;
import java.util.List;

public class TimKiemActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityTimKiemBinding binding;
    private Intent intent;
    private ImageView imageView, imgSearch;
    private EditText txtSearch;
    private TextView txtTongTT,txtTongTC,txtTongThuChi, txtThongBao;
    private ListView lvTC;
    private TransactionsDAO transactionsDAO;
    private List<TransactionsDTO> transactionsDTOS=null;


    private Handler handler = new Handler();
    private Runnable runnable;



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
        initQuery();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome();
            }
        });

        // Đặt thời gian chờ (đơn vị: milliseconds)
        final long delayMillis = 1000;


//        xu ly khi tim kiem thay doi
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Hủy runnable cũ (nếu có)
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Khởi tạo runnable mới
                runnable = () -> {
                    createListViewBySearch(s.toString());
                };

                // Đặt runnable để chạy sau delayMillis
                handler.postDelayed(runnable, delayMillis);
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
        imgSearch = binding.imgSearch;
        txtSearch = binding.txtSearch;
        txtTongTT = binding.txtTongTT;
        txtTongTC = binding.txtTongTC;
        txtTongThuChi = binding.txtTongThuChi;
        txtThongBao = binding.txtThongBao;
        lvTC = binding.lvTC;
    }
    //
    private void navigateHome()
    {
        intent = new Intent(TimKiemActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initQuery()
    {
        transactionsDAO = AppDatabase.getInstance(this).transactionsDAO();
    }


    private void createListViewBySearch(String categoryName)
    {
        transactionsDTOS = transactionsDAO.getAllTransactionsDtoByCategoryName(categoryName);
        if(transactionsDTOS.isEmpty() || categoryName.isEmpty())
        {
            ThuChiAdapter adapter = new ThuChiAdapter(this,R.layout.fragment_thuchi_item,new ArrayList<>());
            lvTC.setAdapter(adapter);
            txtThongBao.setText("Không có kết quả");
            txtTongTT.setText("0đ");
            txtTongTC.setText("0đ");
            txtTongThuChi.setText("0đ");
            return;
        }
        txtThongBao.setText("");
        ThuChiAdapter adapter = new ThuChiAdapter(this,R.layout.fragment_thuchi_item,transactionsDTOS);
        lvTC.setAdapter(adapter);

//        tinh tong tien
        double amount=0;
        for(TransactionsDTO t : transactionsDTOS) amount += t.getAmount();

//        thiet lap hien thi thong ke
        if(transactionsDTOS.get(0).getIsIncome())
        {
            txtTongTC.setText("0đ");
            txtTongTT.setText(amount+"");
            txtTongThuChi.setText(amount+"");
            txtTongThuChi.setTextColor(ContextCompat.getColor(this, R.color.md_green_700));
        }
        else
        {
            txtTongTT.setText("0đ");
            txtTongTC.setText("-"+amount);
            txtTongThuChi.setText("-"+amount);
            txtTongThuChi.setTextColor(ContextCompat.getColor(this, R.color.md_red_700));
        }
    }
}