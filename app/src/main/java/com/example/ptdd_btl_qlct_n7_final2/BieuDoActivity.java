package com.example.ptdd_btl_qlct_n7_final2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.dao.CategoryDAO;
import com.example.ptdd_btl_qlct_n7_final2.dao.TransactionsDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityBieuDoBinding;
import com.example.ptdd_btl_qlct_n7_final2.dto.DataBarChartDTO;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BieuDoActivity extends AppCompatActivity {

    private ActivityBieuDoBinding binding;
    private ImageView imgBack;
    private BarChart barChart;
    private TextView tv_title;

    private Intent intentFromMain;

    private CategoryDAO categoryDAO;
    private TransactionsDAO transactionsDAO;

    private String categoryName="", yearForBarChart="", monthForBarChart="";
    private double amount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bieu_do);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intentFromMain = getIntent();

        getWidget();
        initQuery();
        createView();
        createBarChart();
        imgBack.setOnClickListener(view -> startActivity(new Intent(BieuDoActivity.this,MainActivity.class)));
    }

    private void getWidget()
    {
        binding = ActivityBieuDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imgBack = binding.imageBack;
        barChart = binding.barChart;
        tv_title = binding.tvTitle;
    }

    private void initQuery()
    {
        categoryDAO = AppDatabase.getInstance(this).categoryDAO();
        transactionsDAO = AppDatabase.getInstance(this).transactionsDAO();
    }

    private void createView()
    {
        categoryName = intentFromMain.getStringExtra("categoryName");
        monthForBarChart = intentFromMain.getStringExtra("month");
        yearForBarChart = intentFromMain.getStringExtra("year");
        amount = intentFromMain.getDoubleExtra("amount",0);

        String title=categoryName+" "+"(T"+monthForBarChart+")"+" "+amount+"đ";
        tv_title.setText(title);

    }

    private void createBarChart() {

        String thang1 = "T1" + "/" + yearForBarChart;
        String[] xValues = {thang1, "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};

//        an nhan ben phai
        barChart.getAxisRight().setDrawLabels(false);
//        List<Float> listAmount = transactionsDAO.getAllAmountInYearByCategoryName(categoryName, yearForBarChart).stream().map(Double::floatValue).collect(Collectors.toList());

        List<DataBarChartDTO> dataBarChartDTOS = transactionsDAO.getAllAmountInYearByCategoryName(categoryName,yearForBarChart);
        dataBarChartDTOS.forEach(System.out::println);

        List<BarEntry> entries = new ArrayList<>();

//        chen du lieu tuong ung tung cot
        for (int i = 0; i < xValues.length; i++)
        {
//            lay ra cac thang
            String subString="";
            if(i==0) subString = xValues[i].substring(1,2);
            else subString =xValues[i].substring(1);

            for(DataBarChartDTO data : dataBarChartDTOS)
            {
                String month = data.getMonth();
                if(month.charAt(0) == '0')
                {
                    String subStringMonth = month.substring(1);

//                  gan du lieu cho tung cot
                    if(subStringMonth.equals(subString)) entries.add(new BarEntry(i,data.getTotalAmount()));
                    else entries.add(new BarEntry(i,0));
                }
                else
                {
                    if(month.equals(subString)) entries.add(new BarEntry(i,data.getTotalAmount()));
                    else entries.add(new BarEntry(i,0));
                }
            }
        }

        // Tìm DataBarChartDTO có totalAmount lớn nhất bằng lambda
        DataBarChartDTO maxData = dataBarChartDTOS.stream()
            .max((a, b) -> Float.compare(a.getTotalAmount(), b.getTotalAmount()))
            .orElse(null); // Trả về null nếu danh sách rỗng`


        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(maxData.getTotalAmount());
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);

//        hien thi 10 nhan -> vd: max la 100 thi moi khung co gia tri la 10
        yAxis.setLabelCount(10);

        BarDataSet dataSet = new BarDataSet(entries,"Subject");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

//        tat mo ta
        barChart.getDescription().setEnabled(false);
//        lam moi bieu do de hien thi du lieu voi cau hinh moi nhat
        barChart.invalidate();

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

//      tao khoang cach cho cac cot
        barChart.getXAxis().setGranularity(1f);
//        dam bao cac nhan hien thi dung
        barChart.getXAxis().setGranularityEnabled(true);

        barChart.getLegend().setEnabled(false);

    }

}