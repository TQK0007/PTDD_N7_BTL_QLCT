package com.example.ptdd_btl_qlct_n7_final2.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.R;
import com.example.ptdd_btl_qlct_n7_final2.adapter.DanhSachTKAdapter;
import com.example.ptdd_btl_qlct_n7_final2.dao.CategoryDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityMainBinding;
import com.example.ptdd_btl_qlct_n7_final2.dto.CategoryDTO;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

    //    Khai bao viewBinding ung voi activity
//    Moi viewBinding se quan ly cac thanh phan trong tung activity cu the
    private ActivityMainBinding binding;
    private Toolbar toolbar;

    private TextView tvTC, tvTT, tvSTCT, tvSTTN, tvSTThuChi;
    private View indicatorTC, indicatorTT;
    private boolean isTienChiTab=true;
    private ListView lvTKDM;
    private PieChart pieChart;
    private EditText editMonth;
    private ImageView imgDate;

    private CategoryDAO categoryDAO;
    private List<CategoryDTO> categoryDTOs;

    private String yearForBarChart="", monthForBarChart="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        getWidget();
        initQuery();
        crateDefaultDate();
        createInformationThuChi();
        createPieChart(false);
        createLV(false);


        tvTC.setOnClickListener(view -> {
            isTienChiTab=true;
            switchTab();
            createPieChart(false);
            createLV(false);
        });
        tvTT.setOnClickListener(view -> {
            isTienChiTab=false;
            switchTab();
            createPieChart(true);
            createLV(true);
        });

        editMonth.setOnClickListener(view -> {
            createDate();
        });

        imgDate.setOnClickListener(view->{
            createDate();
        });

        editMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createInformationThuChi();
                createPieChart(false);
                createLV(false);
            }
        });

        lvTKDM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,BieuDoActivity.class);
                intent.putExtra("year",yearForBarChart);
                intent.putExtra("month",monthForBarChart);

                boolean isIncome = !isTienChiTab;
                List<CategoryDTO> categoryDTOWithCondition  = categoryDTOs.stream().filter(c->c.isIncome()==isIncome).collect(Collectors.toList());
                intent.putExtra("categoryName",categoryDTOWithCondition.get(i).getCategoryName());
                intent.putExtra("amount",categoryDTOWithCondition.get(i).getAmount());
                startActivity(intent);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        if(menu instanceof MenuBuilder){
            @SuppressLint("RestrictedApi") MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu==R.id.menu_TimKiem) navigate_TimKiem();
        else if(id_menu==R.id.menu_ThuChi) navigate_ThuChi();
        else if (id_menu==R.id.menu_DanhMuc) navigate_DanhMuc();
        else if (id_menu==R.id.menu_LapKH) navigate_LapKeHoach();
        else finishAffinity();
        return super.onOptionsItemSelected(item);
    }

    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        indicatorTC = binding.indicatorTc;
        indicatorTT = binding.indicatorTt;

        tvTC=binding.tvTC;
        tvTT=binding.tvTT;

        tvSTCT = binding.tvSTCT;
        tvSTTN = binding.tvSTTN;
        tvSTThuChi = binding.tvSTThuChi;

        pieChart = binding.pieChart;
        lvTKDM = binding.lvTKDM;

        editMonth = binding.editMonth;
        imgDate = binding.imgDate;

    }

    private void initQuery()
    {
        categoryDAO = AppDatabase.getInstance(this).categoryDAO();
    }

    private void navigate_TimKiem() {
        Intent intent = new Intent(MainActivity.this, TimKiemActivity.class);
        startActivity(intent);
    }

    private void navigate_LapKeHoach() {
        Intent intent = new Intent(MainActivity.this, LapKeHoachActivity.class);
        startActivity(intent);
    }

    private void navigate_DanhMuc() {
        Intent intent = new Intent(MainActivity.this, DanhMucActivity.class);
        startActivity(intent);
    }

    private void navigate_ThuChi() {
        Intent intent = new Intent(MainActivity.this, ThuChiActivity.class);
        startActivity(intent);
    }

    private void createInformationThuChi() {
        String formattedDate="";
        try {
            formattedDate = formatDateToYearMonth();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

//        lay danh sach
        categoryDTOs = categoryDAO.getAllByDate(formattedDate);

        double tongTT=0, tongTC=0, tongThuChi=0;
        for(CategoryDTO c : categoryDTOs)
        {
            if(c.isIncome()) tongTT+=c.getAmount();
            else tongTC += c.getAmount();
        }
        tongThuChi = tongTT-tongTC;
        tvSTTN.setText("+"+tongTT);
        tvSTCT.setText("-"+tongTC);
        tvSTThuChi.setText(""+tongThuChi);
    }

    private void createPieChart(boolean condition)
    {
//        loc tra nhung phan tu theo dieu kien
        List<CategoryDTO> categoryDTOWithIncome = categoryDTOs.stream().filter(c->c.isIncome()==condition).collect(Collectors.toList());

//        tao du lieu cho bieu do
        List<PieEntry> entries = new ArrayList<>();
        categoryDTOWithIncome.forEach(categoryDTO -> {
            entries.add(new PieEntry((float) categoryDTO.getAmount(), categoryDTO.getCategoryName()));
        });

        PieDataSet pieDataSet = new PieDataSet(entries,"Category");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

//        Thiet lap kich thuoc font chu
        pieDataSet.setValueTextSize(14f);


        PieData pieData = new PieData(pieDataSet);

//        them ky tu % sau moi gia tri
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();

//      bo chu thich
        pieChart.getLegend().setEnabled(false);

//        chinh che do %
        pieChart.setUsePercentValues(true);

    }


    private void crateDefaultDate()
    {
        LocalDate localDate = null;
        int year = 0,month=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//                lay ngay hien tai
            localDate = LocalDate.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year=localDate.getYear();
            month=localDate.getMonthValue();

        }
        yearForBarChart = String.valueOf(year);
        monthForBarChart = String.valueOf(month);
        editMonth.setText(month+"/"+year);
    }

    private void createDate()
    {
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                editMonth.setText((i1+1) + "/" + i);
                yearForBarChart = String.valueOf(i);
                monthForBarChart = String.valueOf((i1+1));
            }
        };
        LocalDate localDate = null;
        int year = 0,month=0,day=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//                lay ngay hien tai
            localDate = LocalDate.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            year=localDate.getYear();
            month=localDate.getMonthValue();
            day=localDate.getDayOfMonth();
        }
        DatePickerDialog dialog =new DatePickerDialog(MainActivity.this,callBack,year,month-1,day);
        dialog.show();
    }

// ham chuyen dinh dang tu ngay dang ve chuoi de tim kiem
    public String formatDateToYearMonth() throws ParseException {

        // Định dạng đầu vào MM/yyyy
        @SuppressLint("SimpleDateFormat")
        DateFormat inputFormat = new SimpleDateFormat("MM/yyyy");

        // Định dạng ngày đầy đủ yyyy-MM-dd HH:mm:ss
        @SuppressLint("SimpleDateFormat")
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM");

        // Chuyển đổi từ chuỗi MM/yyyy -> Date
        Date date = inputFormat.parse(editMonth.getText().toString());

        // Chuyển đổi Date -> chuỗi với định dạng yyyy-MM-dd HH:mm:ss
        String formattedDate = outputFormat.format(date);

        return formattedDate;
    }

    private void createLV(boolean condition)
    {
//        loc tra nhung phan tu theo dieu kien
        List<CategoryDTO> categoryDTOWithIncome = categoryDTOs.stream().filter(c->c.isIncome()==condition).collect(Collectors.toList());
        DanhSachTKAdapter adapter = new DanhSachTKAdapter(this,R.layout.fragment_thongke_item,categoryDTOWithIncome);
        lvTKDM.setAdapter(adapter);
    }


    private void switchTab() {

        if(isTienChiTab)
        {
            // Cập nhật UI
            tvTC.setTextColor(getResources().getColor(R.color.md_yellow_600));
            indicatorTC.setVisibility(View.VISIBLE);
            tvTT.setTextColor(getResources().getColor(R.color.black));
            indicatorTT.setVisibility(View.INVISIBLE);
        }
        else
        {
            // Cập nhật UI
            tvTT.setTextColor(getResources().getColor(R.color.md_yellow_600));
            indicatorTT.setVisibility(View.VISIBLE);
            tvTC.setTextColor(getResources().getColor(R.color.black));
            indicatorTC.setVisibility(View.INVISIBLE);
        }

    }



}