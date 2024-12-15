package com.example.ptdd_btl_qlct_n7_final2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.ThemSuaKeHoachActivity;
import com.example.ptdd_btl_qlct_n7_final2.adapter.LapKeHoachAdapter;
import com.example.ptdd_btl_qlct_n7_final2.dao.LongTermGoalDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityLapKeHoachBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;

import java.sql.Date;
import java.util.List;

public class LapKeHoachActivity extends AppCompatActivity {

    private ActivityLapKeHoachBinding binding;
    private LongTermGoalDAO longTermGoalDAO;
    private ListView lvKH;
    private LapKeHoachAdapter lapKeHoachAdapter;
    private List<LongTermGoal> longTermGoalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lap_ke_hoach);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews(); //
        initializeDatabase(); //ket noi database
        loadListView();

        setupEventHandlers();
    }

    private void initializeViews() {
        binding = ActivityLapKeHoachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lvKH = binding.lvKeHoach;
    }

    private void initializeDatabase() {
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void loadListView() {
        longTermGoalList = longTermGoalDAO.getAll();
        lapKeHoachAdapter = new LapKeHoachAdapter(this, R.layout.fragment_kehoach_item, longTermGoalList);
        lvKH.setAdapter(lapKeHoachAdapter);
    }

    private void setupEventHandlers() {
        binding.imageBack.setOnClickListener(view -> navigateToHome());

        binding.imageAdd.setOnClickListener(view -> navigateToAddOrEditPlan());

        lvKH.setOnItemClickListener((parent, view, position, id) -> {
            LongTermGoal selectedItem = longTermGoalList.get(position);
            navigateToEditPlan(selectedItem);
        });

        lvKH.setOnItemLongClickListener((parent, view, position, id) -> {
            LongTermGoal selectedItem = longTermGoalList.get(position);
            confirmAndDeletePlan(position, selectedItem);
            return true;
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LapKeHoachActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToAddOrEditPlan() {
        Intent intent = new Intent(LapKeHoachActivity.this, ThemSuaKeHoachActivity.class);
        startActivity(intent);
    }

    private void navigateToEditPlan(LongTermGoal selectedItem) {
        Intent intent = new Intent(this, ThemSuaKeHoachActivity.class);
        intent.putExtra("id", selectedItem.getId());
        intent.putExtra("name", selectedItem.getName());
        intent.putExtra("targetAmount", selectedItem.getTarget());
        intent.putExtra("currentProgress", selectedItem.getProgress());
        intent.putExtra("deadline", selectedItem.getDeadline());
        startActivity(intent);
    }

    private void confirmAndDeletePlan(int position, LongTermGoal selectedItem) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có muốn xóa kế hoạch này không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    longTermGoalList.remove(position);
                    longTermGoalDAO.delete(selectedItem);
                    lapKeHoachAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void resetData() {
        List<LongTermGoal> allGoals = longTermGoalDAO.getAll();
        if (allGoals != null && !allGoals.isEmpty()) {
            allGoals.forEach(longTermGoalDAO::delete);
        }
        loadListView();
    }

    private void fakeData() {
        java.util.Date currentDate = new java.util.Date();
        Date sqlDate = new Date(currentDate.getTime());
        LongTermGoal sampleGoal = new LongTermGoal("Hưu trí", 100.0, sqlDate, 30.0, true);
        longTermGoalDAO.add(sampleGoal);
    }
}
