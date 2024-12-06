package com.example.ptdd_btl_qlct_n7_final2;

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

import java.util.List;

public class LapKeHoachActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityLapKeHoachBinding binding;
    private Intent intent;
    private ImageView imageView;
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
        fakeData();
        createLV();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome();
            }
        });
    }


    private void getWidget()
    {
        binding = ActivityLapKeHoachBinding.inflate(getLayoutInflater());

        // Set layout cho Activity
        setContentView(binding.getRoot());
        imageView = binding.imageBack;
        lvKH = binding.lvKeHoach;
    }

    private void navigateHome()
    {
        intent = new Intent(LapKeHoachActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initQuery()
    {
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void fakeData()
    {

        LongTermGoal l = new LongTermGoal("Hưu trí",100.0,"Không thời han",30.0,true);
        longTermGoalDAO.add(l);
    }

    private void createLV()
    {
        longTermGoalList = longTermGoalDAO.getAll();
        lapKeHoachAdapter= new LapKeHoachAdapter(this,R.layout.fragment_kehoach_item,longTermGoalList);
        lvKH.setAdapter(lapKeHoachAdapter);
    }
}