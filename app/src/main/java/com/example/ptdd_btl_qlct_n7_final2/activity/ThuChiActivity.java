package com.example.ptdd_btl_qlct_n7_final2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.adapter.ThuChiAdapter;
import com.example.ptdd_btl_qlct_n7_final2.dao.LongTermGoalDAO;
import com.example.ptdd_btl_qlct_n7_final2.dao.TransactionsDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThuChiBinding;
import com.example.ptdd_btl_qlct_n7_final2.dto.TransactionsDTO;
import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;
import com.example.ptdd_btl_qlct_n7_final2.entity.Transactions;

import java.util.List;

public class ThuChiActivity extends AppCompatActivity {

//    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityThuChiBinding binding;
    private Intent intent;
    private ImageView imageViewBack, imageViewAdd;

    private TextView tvTC, tvTT;
    private View indicatorTC, indicatorTT;
    private ListView lvTC;

    private List<TransactionsDTO> transactionsDTOS=null;

    private ThuChiAdapter thuChiAdapter=null;
    private boolean isTienChiTab=true;


    private TransactionsDAO transactionsDAO;
    private LongTermGoalDAO longTermGoalDAO;

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
                navigateThemSuaTCWithAdd();
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

        lvTC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Transactions transactions = transactionsDAO.findByID(transactionsDTOS.get(i).getTransactionId());
                navigateThemSuaTCWithEdit(transactions);
            }
        });

        lvTC.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Tạo một AlertDialog để xác nhận hành động
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc chắn muốn xoá khoản thu chi này?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Transactions delTransactions = transactionsDAO.findByID(transactionsDTOS.get(i).getTransactionId());
                                transactionsDAO.delete(delTransactions);
                                transactionsDTOS.remove(i);
                                thuChiAdapter.notifyDataSetChanged();
//                                cap nhat danh LongTermGoal
                                Integer longTermGoalID = delTransactions.getGoalId();
                                if(longTermGoalID != null)
                                {
                                    LongTermGoal updateLongTermGoal = longTermGoalDAO.findByID(longTermGoalID);
                                    double updateProcess = updateLongTermGoal.getProgress()-delTransactions.getAmount();
                                    updateLongTermGoal.setProgress(updateProcess);
                                    longTermGoalDAO.update(updateLongTermGoal);
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)  // Không làm gì khi người dùng nhấn "Hủy"
                        .show();
                return true;
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

        indicatorTC = binding.indicatorTc;
        indicatorTT = binding.indicatorTt;

        tvTC=binding.tvTC;
        tvTT=binding.tvTT;

        lvTC=binding.lvTC;

    }

// khoi tao truy van
    private void initQuery()
    {
        transactionsDAO = AppDatabase.getInstance(this).transactionsDAO();
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void createListView()
    {
        if(isTienChiTab) transactionsDTOS = transactionsDAO.getAllTransactionsDtoByIncome(false);
        else transactionsDTOS = transactionsDAO.getAllTransactionsDtoByIncome(true);
        thuChiAdapter = new ThuChiAdapter(this,R.layout.fragment_thuchi_item,transactionsDTOS);
        lvTC.setAdapter(thuChiAdapter);
    }


    private void navigateHome()
    {
        intent = new Intent(ThuChiActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateThemSuaTCWithAdd()
    {
        intent = new Intent(ThuChiActivity.this, ThemSuaThuChiActivity.class);
        intent.putExtra("isTienChiTab",isTienChiTab);
        intent.putExtra("isAdd",true);
        startActivity(intent);
    }

    private void navigateThemSuaTCWithEdit(Transactions transactions)
    {
        intent = new Intent(ThuChiActivity.this, ThemSuaThuChiActivity.class);
        intent.putExtra("isTienChiTab",isTienChiTab);
        intent.putExtra("isAdd",false);
        intent.putExtra("transactions",transactions);
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

}