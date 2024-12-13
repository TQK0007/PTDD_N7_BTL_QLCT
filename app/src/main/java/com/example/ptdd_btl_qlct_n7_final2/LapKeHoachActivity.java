package com.example.ptdd_btl_qlct_n7_final2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.adapter.LapKeHoachAdapter;
import com.example.ptdd_btl_qlct_n7_final2.dao.LongTermGoalDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityLapKeHoachBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;

import java.sql.Date;
import java.util.List;

public class LapKeHoachActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityLapKeHoachBinding binding;
    private Intent intent;
    private ImageView imageView, imageViewAdd;
    private LongTermGoalDAO longTermGoalDAO;
    private ListView lvKH;
    private LapKeHoachAdapter lapKeHoachAdapter;
    List<LongTermGoal> longTermGoalList=null;

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

        getWidget();
        initQuery();
        //fakeData();
        createLV();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome();
            }
        });
        imageViewAdd.setOnClickListener(view -> navigateThemSuaKeHoach());

        lvKH.setOnItemClickListener((parent, view, position, id) -> {
            LongTermGoal selectedItem = longTermGoalList.get(position);

            // Tạo Intent để chuyển màn hình
            Intent intent = new Intent(this, ThemSuaKeHoachActivity.class);

            // Truyền dữ liệu sang màn hình ThemSuaKeHoachActivity
            intent.putExtra("id", selectedItem.getId());
            intent.putExtra("name", selectedItem.getName());
            intent.putExtra("targetAmount", selectedItem.getTarget());
            intent.putExtra("currentProgress", selectedItem.getProgress());
            intent.putExtra("deadline", selectedItem.getDeadline()); // Truyền kiểu long cho Date

            // Chuyển sang màn hình ThemSuaKeHoachActivity
            startActivity(intent);
        });

        // Sự kiện giữ lâu
        lvKH.setOnItemLongClickListener((parent, view, position, id) -> {
            LongTermGoal selectedItem = longTermGoalList.get(position);

            // Tạo AlertDialog
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có muốn xóa kế hoạch này không?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        // Xóa kế hoạch từ danh sách
                        longTermGoalList.remove(position);
                        // Xóa kế hoạch từ cơ sở dữ liệu
                        longTermGoalDAO.delete(selectedItem); // Gọi phương thức delete trong DAO để xóa khỏi cơ sở dữ liệu
                        // Cập nhật lại ListView
                        lapKeHoachAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Hủy", null) // Không làm gì khi nhấn "Hủy"
                    .show();

            return true; // Trả về true để xử lý sự kiện giữ lâu
        });

    }

    private void getWidget()
    {
        binding = ActivityLapKeHoachBinding.inflate(getLayoutInflater());

        // Set layout cho Activity
        setContentView(binding.getRoot());
        imageView = binding.imageBack;
        imageViewAdd = binding.imageAdd;
        lvKH = binding.lvKeHoach;
    }

    private void navigateHome()
    {
        intent = new Intent(LapKeHoachActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateThemSuaKeHoach() {
        intent = new Intent(LapKeHoachActivity.this,ThemSuaKeHoachActivity.class);
        startActivity(intent);
    }

    private void initQuery()
    {
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void fakeData()
    {

        java.util.Date currentDate = new java.util.Date();

        // Chuyển đổi sang `java.sql.Date`
        Date date = new Date(currentDate.getTime());

        System.out.println(date);
        LongTermGoal l = new LongTermGoal("Hưu trí",100.0,date,30.0,true);
//        LongTermGoal updatel = new LongTermGoal(1,"Hưu trí",100.0,new Date(System.currentTimeMillis()),30.0,true);
        longTermGoalDAO.add(l);
        System.out.println("finish step 1");

    }

    private void resetData() {
        // Lấy danh sách tất cả các mục từ DAO
        List<LongTermGoal> allGoals = longTermGoalDAO.getAll();

        // Kiểm tra danh sách có mục nào không
        if (allGoals != null && !allGoals.isEmpty()) {
            for (LongTermGoal goal : allGoals) {
                // Xóa từng mục
                longTermGoalDAO.delete(goal);
            }
            System.out.println("All data has been reset.");
        } else {
            System.out.println("No data to reset.");
        }

        // Cập nhật lại ListView sau khi reset
        createLV();
    }


    private void createLV()
    {
        System.out.println("Start");
        longTermGoalList = longTermGoalDAO.getAll();
        System.out.println("step1");
        lapKeHoachAdapter= new LapKeHoachAdapter(this,R.layout.fragment_kehoach_item,longTermGoalList);
        System.out.println("step2");
        lvKH.setAdapter(lapKeHoachAdapter);
        System.out.println("Size of list: "+longTermGoalList.size());
        longTermGoalList.forEach(System.out::println);
    }
}