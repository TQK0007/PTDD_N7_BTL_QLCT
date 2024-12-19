package com.example.ptdd_btl_qlct_n7_final2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.adapter.LapKeHoachAdapter;
import com.example.ptdd_btl_qlct_n7_final2.dao.LongTermGoalDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityLapKeHoachBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LapKeHoachActivity extends AppCompatActivity {

    private ActivityLapKeHoachBinding binding;
    private LongTermGoalDAO longTermGoalDAO;
    private TextView tvQuaHanCount, tvChuaHoanThanhCount, tvHoanThanhCount;
    private ListView lvQuaHan, lvChuaHoanThanh, lvHoanThanh;
    private LapKeHoachAdapter adapterQuaHan, adapterChuaHoanThanh, adapterHoanThanh;
    private List<LongTermGoal> quaHanList, chuaHoanThanhList, hoanThanhList;

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

        initializeViews();
        initializeDatabase();
        loadListViews();
        setupEventHandlers();
//        fakeData();
    }

    private void initializeViews() {
        binding = ActivityLapKeHoachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lvQuaHan = binding.lvQuaHan;
        lvChuaHoanThanh = binding.lvChuaHoanThanh;
        lvHoanThanh = binding.lvHoanThanh;
        tvQuaHanCount = binding.tvQuaHanCount;  // Số lượng kế hoạch quá hạn
        tvChuaHoanThanhCount = binding.tvChuaHoanThanhCount;  // Số lượng kế hoạch đang thực hiện
        tvHoanThanhCount = binding.tvHoanThanhCount;  // Số lượng kế hoạch đã hoàn thành
    }

    private void initializeDatabase() {
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void loadListViews() {
        List<LongTermGoal> longTermGoalList = longTermGoalDAO.getAllSorted();

        // Phân loại danh sách
        quaHanList = new ArrayList<>();
        chuaHoanThanhList = new ArrayList<>();
        hoanThanhList = new ArrayList<>();

        for (LongTermGoal goal : longTermGoalList) {
            if (goal.getProgress() >= goal.getTarget()) {
                hoanThanhList.add(goal); // Đã hoàn thành
            } else if (goal.getDeadline().before(new java.util.Date())) {
                quaHanList.add(goal); // Quá hạn
            } else {
                chuaHoanThanhList.add(goal); // Chưa hoàn thành
            }
        }

        tvQuaHanCount.setText(String.valueOf(quaHanList.size()));
        tvHoanThanhCount.setText(String.valueOf(hoanThanhList.size()));
        tvChuaHoanThanhCount.setText(String.valueOf(chuaHoanThanhList.size()));

        // Tạo adapter cho từng danh sách
        adapterQuaHan = new LapKeHoachAdapter(this, R.layout.fragment_kehoach_item, quaHanList);
        adapterChuaHoanThanh = new LapKeHoachAdapter(this, R.layout.fragment_kehoach_item, chuaHoanThanhList);
        adapterHoanThanh = new LapKeHoachAdapter(this, R.layout.fragment_kehoach_item, hoanThanhList);

        // Gắn adapter vào từng ListView
        lvQuaHan.setAdapter(adapterQuaHan);
        lvChuaHoanThanh.setAdapter(adapterChuaHoanThanh);
        lvHoanThanh.setAdapter(adapterHoanThanh);
    }

    private void setupEventHandlers() {
        binding.imageBack.setOnClickListener(view -> navigateToHome());
        binding.imageAdd.setOnClickListener(view -> navigateToAddOrEditPlan());
        binding.btnClearAll.setOnClickListener(view -> resetData());

        setupListViewEvents(lvQuaHan, quaHanList, adapterQuaHan);
        setupListViewEvents(lvChuaHoanThanh, chuaHoanThanhList, adapterChuaHoanThanh);
        setupListViewEvents(lvHoanThanh, hoanThanhList, adapterHoanThanh);
    }

    private void setupListViewEvents(ListView listView, List<LongTermGoal> goalList, LapKeHoachAdapter adapter) {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            LongTermGoal selectedItem = goalList.get(position);
            navigateToEditPlan(selectedItem);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            LongTermGoal selectedItem = goalList.get(position);
            confirmAndDeletePlan(position, selectedItem, goalList, adapter);
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
        intent.putExtra("isDefault", selectedItem.isDefault());
        startActivity(intent);
    }

    private void confirmAndDeletePlan(int position, LongTermGoal selectedItem, List<LongTermGoal> goalList, LapKeHoachAdapter adapter) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có muốn xóa kế hoạch này không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    goalList.remove(position);
                    longTermGoalDAO.delete(selectedItem);
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void resetData() {
        // Tạo một AlertDialog xác nhận
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa dữ liệu")
                .setMessage("Bạn có chắc chắn muốn xóa tất cả dữ liệu?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Nếu người dùng chọn "Có", xóa dữ liệu
                    List<LongTermGoal> allGoals = longTermGoalDAO.getAll();
                    if (allGoals != null && !allGoals.isEmpty()) {
                        allGoals.forEach(longTermGoalDAO::delete);
                        Toast.makeText(this, "Dữ liệu đã được xóa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Không có dữ liệu để xóa", Toast.LENGTH_SHORT).show();
                    }
                    loadListViews(); // Tải lại danh sách sau khi xóa
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    // Nếu người dùng chọn "Không", chỉ cần đóng hộp thoại
                    dialog.dismiss();
                })
                .show();
    }


    private void fakeData() {
        java.util.Date currentDate = new java.util.Date();
        Date sqlDate = new Date(currentDate.getTime());

        // Tạo dữ liệu mẫu với 5 mục tiêu giả lập
        LongTermGoal goal1 = new LongTermGoal("Mua xe ô tô", 3000000.0, new Date(currentDate.getTime() - 1000000000), 1500000.0, false); // Quá hạn
        LongTermGoal goal2 = new LongTermGoal("Đầu tư bất động sản", 5000000.0, new Date(currentDate.getTime() - 2000000000), 5000000.0, false); // Hoàn thành
        LongTermGoal goal3 = new LongTermGoal("Mua nhà", 6000000.0, new Date(currentDate.getTime() + 1000000000), 2000000.0, false); // Chưa hoàn thành
        LongTermGoal goal4 = new LongTermGoal("Đi du lịch Nhật Bản", 1000000.0, new Date(currentDate.getTime() - 500000000), 800000.0, false); // Quá hạn
        LongTermGoal goal5 = new LongTermGoal("Học thêm ngoại ngữ", 300000.0, new Date(currentDate.getTime() + 2000000000), 150000.0, false); // Chưa hoàn thành

        // Chỉ có một mục tiêu mặc định (isDefault = true)
        LongTermGoal goal6 = new LongTermGoal("Mua điện thoại mới", 1500000.0, new Date(currentDate.getTime() + 500000000), 1500000.0, true); // Hoàn thành

        // Thêm dữ liệu vào cơ sở dữ liệu
        longTermGoalDAO.addWithDefaultCheck(goal1);
        longTermGoalDAO.addWithDefaultCheck(goal2);
        longTermGoalDAO.addWithDefaultCheck(goal3);
        longTermGoalDAO.addWithDefaultCheck(goal4);
        longTermGoalDAO.addWithDefaultCheck(goal5);
        longTermGoalDAO.addWithDefaultCheck(goal6);

        // Load lại dữ liệu để cập nhật giao diện
        loadListViews();
    }


}
