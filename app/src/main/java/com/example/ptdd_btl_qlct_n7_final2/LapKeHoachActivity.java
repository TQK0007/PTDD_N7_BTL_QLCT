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
//        fakeData();
        createLV();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome();
            }
        });
        imageViewAdd.setOnClickListener(view -> navigateThemSuaKeHoach());

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

       Date date = new Date(2025,11,10);

        System.out.println("start");
        LongTermGoal l = new LongTermGoal("Mua nhà",100.0,date,80.0,false);
//        LongTermGoal updatel = new LongTermGoal(1,"Hưu trí",100.0,new Date(System.currentTimeMillis()),30.0,true);
        longTermGoalDAO.add(l);
        System.out.println("finish step 1");

        LongTermGoal longTermGoalNeed = longTermGoalDAO.findByID(1);
        System.out.println("finish step 2");
        System.out.println(longTermGoalNeed);

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