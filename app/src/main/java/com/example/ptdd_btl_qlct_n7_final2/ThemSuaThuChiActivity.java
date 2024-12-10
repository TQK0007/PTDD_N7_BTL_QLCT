package com.example.ptdd_btl_qlct_n7_final2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptdd_btl_qlct_n7_final2.adapter.DanhSachDMAdapter;
import com.example.ptdd_btl_qlct_n7_final2.adapter.SelectListener;
import com.example.ptdd_btl_qlct_n7_final2.dao.CategoryDAO;
import com.example.ptdd_btl_qlct_n7_final2.dao.LongTermGoalDAO;
import com.example.ptdd_btl_qlct_n7_final2.dao.TransactionsDAO;
import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaThuChiBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.Category;
import com.example.ptdd_btl_qlct_n7_final2.entity.Transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThemSuaThuChiActivity extends AppCompatActivity implements SelectListener {

    private ActivityThemSuaThuChiBinding binding;
    private ImageView imageViewBack, imgDate;
    private Intent intent, intentFromTCA;
    private RecyclerView recyclerViewDMTC;
    private Button btnDM, btnThem;
    private TextView tvTitle,tvTTTC;
    private EditText editText;
    private LinearLayout layoutDMTK;
    private Spinner spinnerDMTK;

    private boolean isAdd, isTienChiTab;
    private List<Category> categoryList=null;
    private DanhSachDMAdapter danhSachDMAdapter=null;
    private Transactions editTransactions=null;

    private CategoryDAO categoryDAO;
    private TransactionsDAO transactionsDAO;
    private LongTermGoalDAO longTermGoalDAO;
    private List<String> LongTermGoalNames=null;

    private int categoryID=0;
    private String categoryName="", LongTermGoalName="";

    private Context curContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_sua_thu_chi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intentFromTCA=getIntent();
        getWidget();
        initQuery();
        configView();
        createRecyclerView();


        imageViewBack.setOnClickListener(view -> navigateThuChi());
        imgDate.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    editText.setText(i2 + "/" + (i1+1) + "/" + i);
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
            DatePickerDialog dialog =new DatePickerDialog(ThemSuaThuChiActivity.this,callBack,year,month-1,day);
            dialog.show();
        });

        editText.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    editText.setText(i2 + "/" + (i1+1) + "/" + i);
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
            DatePickerDialog dialog =new DatePickerDialog(ThemSuaThuChiActivity.this,callBack,year,month-1,day);
            dialog.show();
        });

        btnDM.setOnClickListener(view -> navigateDanhMuc());

        btnThem.setOnClickListener(view -> {
            if(isAdd && isTienChiTab)
            {
                isTabTienChiAndAdd();
            }
            else if(isAdd && !isTienChiTab)
            {
                isNotTabTienChiAndAdd();
            }
            else if(!isAdd && isTienChiTab)
            {
                isTabTienChiAndEdit();
            }
            else
            {
                isNotTabTienChiAndEdit();
            }
            intent = new Intent(ThemSuaThuChiActivity.this,ThuChiActivity.class);
            startActivity(intent);
        });

        spinnerDMTK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LongTermGoalName = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getWidget()
    {
        // Khởi tạo ViewBinding
        binding = ActivityThemSuaThuChiBinding.inflate(getLayoutInflater());
        // Set layout cho Activity
        setContentView(binding.getRoot());

        imageViewBack = binding.imageBack;
        recyclerViewDMTC = binding.recyclerView;
        tvTitle=binding.tvTitle;
        btnDM=binding.btnDanhMuc;
        btnThem=binding.btnThemTienThu;
        imgDate = binding.imgDate;
        editText=binding.editDate;
        tvTTTC = binding.tvTTTC;

        layoutDMTK=binding.layoutDMTK;
        spinnerDMTK=binding.spinnerDMTK;
    }

    private void initQuery()
    {
        categoryDAO = AppDatabase.getInstance(this).categoryDAO();
        transactionsDAO = AppDatabase.getInstance(this).transactionsDAO();
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void createRecyclerView()
    {
        isTienChiTab = intentFromTCA.getBooleanExtra("isTienChiTab",true);
        if(isTienChiTab) categoryList = categoryDAO.getAllByIsIncome(false);
        else categoryList = categoryDAO.getAllByIsIncome(true);

        danhSachDMAdapter = new DanhSachDMAdapter(this,categoryList,this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerViewDMTC.setLayoutManager(layoutManager);
        recyclerViewDMTC.setAdapter(danhSachDMAdapter);

    }

    private void navigateThuChi()
    {
        intent = new Intent(ThemSuaThuChiActivity.this,ThuChiActivity.class);
        startActivity(intent);
    }
    private void navigateDanhMuc()
    {
        intent = new Intent(ThemSuaThuChiActivity.this,DanhMucActivity.class);
        startActivity(intent);
    }

    //    Cấu hình động vì giao diện thêm và sửa dùng chung
    private void configView()
    {
        if(intentFromTCA!=null)
        {
            isAdd = intentFromTCA.getBooleanExtra("isAdd",true);
            editTransactions = (Transactions) intentFromTCA.getSerializableExtra("transactions");
            isTienChiTab = intentFromTCA.getBooleanExtra("isTienChiTab",true);
        }
        if(isAdd)
        {
            tvTitle.setText("Thêm thu chi");
            btnThem.setText("Thêm");
        }
        else
        {
            tvTitle.setText("Sửa thu chi");
            btnThem.setText("Sửa");
        }
        if(isTienChiTab)
        {
            tvTTTC.setText("Tiền chi");

        }
        else
        {
            tvTTTC.setText("Tiên thu");
            layoutDMTK.setVisibility(View.GONE);
        }
        if(editTransactions!=null)
        {
//            thiet lap ngay
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(editTransactions.getCreatedAt());
            binding.editDate.setText(formattedDate);

            binding.editNote.setText(editTransactions.getNote());

            binding.editTienThu.setText(editTransactions.getAmount()+"");
        }
    }

    @Override
    public void onItemClicked(Category c) {
        categoryID=c.getCategoryId();
        categoryName=c.getCategoryName();
         createSprinner();
        Toast.makeText(this,"Category id: "+categoryID,Toast.LENGTH_SHORT).show();
    }

    private void isTabTienChiAndAdd()
    {
        String dateString = binding.editDate.getText().toString();
        String amoutText = binding.editTienThu.getText().toString();
        String note = binding.editNote.getText().toString();
        String type = isTienChiTab?"Expense" : "Income";
        if(dateString.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap ngay",Toast.LENGTH_SHORT).show();
            return;
        }
        if(amoutText.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap tien",Toast.LENGTH_SHORT).show();
            return;
        }
        double amout = Double.parseDouble(amoutText);
        if(categoryID==0)
        {
            Toast.makeText(curContext,"Vui long chon danh muc",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng chuỗi
        Date date=null;
        try {
            // Chuyển chuỗi sang Date
            date = dateFormat.parse(dateString);
            System.out.println("Date: " + date);
        } catch (ParseException e) {
            // Xử lý ngoại lệ nếu chuỗi không đúng định dạng
            System.out.println("Định dạng ngày tháng không hợp lệ!");
            e.printStackTrace();
        }


        if(LongTermGoalName.isEmpty() && !LongTermGoalNames.isEmpty())
        {
            Toast.makeText(curContext,"Vui long chon danh muc tiet kiem",Toast.LENGTH_SHORT).show();
            return;
        }

        int LongTermGoalID = longTermGoalDAO.getLongTermGoalByName(LongTermGoalName);

        Transactions transactions = new Transactions(amout,categoryID,date,note,type, LongTermGoalID);
        transactionsDAO.add(transactions);
    }

    private void isNotTabTienChiAndAdd()
    {
        String dateString = binding.editDate.getText().toString();
        String amoutText = binding.editTienThu.getText().toString();
        String note = binding.editNote.getText().toString();
        String type = isTienChiTab?"Expense" : "Income";
        if(dateString.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap ngay",Toast.LENGTH_SHORT).show();
            return;
        }
        if(amoutText.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap tien",Toast.LENGTH_SHORT).show();
            return;
        }
        double amout = Double.parseDouble(amoutText);
        if(categoryID==0)
        {
            Toast.makeText(curContext,"Vui long chon danh muc",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng chuỗi
        Date date=null;
        try {
            // Chuyển chuỗi sang Date
            date = dateFormat.parse(dateString);
            System.out.println("Date: " + date);
        } catch (ParseException e) {
            // Xử lý ngoại lệ nếu chuỗi không đúng định dạng
            System.out.println("Định dạng ngày tháng không hợp lệ!");
            e.printStackTrace();
        }

        Transactions transactions = new Transactions(amout,categoryID,date,note,type,null);
        transactionsDAO.add(transactions);
    }

    private void isNotTabTienChiAndEdit()
    {
        String dateString = binding.editDate.getText().toString();
        String amoutText = binding.editTienThu.getText().toString();
        String note = binding.editNote.getText().toString();
        if(dateString.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap ngay",Toast.LENGTH_SHORT).show();
            return;
        }
        if(amoutText.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap tien",Toast.LENGTH_SHORT).show();
            return;
        }
        double amout = Double.parseDouble(amoutText);
        if(categoryID==0)
        {
            Toast.makeText(curContext,"Vui long chon danh muc",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng chuỗi
        Date date=null;
        try {
            // Chuyển chuỗi sang Date
            date = dateFormat.parse(dateString);

        } catch (ParseException e) {
            // Xử lý ngoại lệ nếu chuỗi không đúng định dạng
            e.printStackTrace();
        }

//        cap nhat du lieu
        editTransactions.setCreatedAt(date);
        editTransactions.setNote(note);
        editTransactions.setAmount(amout);
        editTransactions.setCategoryId(categoryID);
        transactionsDAO.update(editTransactions);

    }
    private void isTabTienChiAndEdit()
    {
        String dateString = binding.editDate.getText().toString();
        String amoutText = binding.editTienThu.getText().toString();
        String note = binding.editNote.getText().toString();
        String type = isTienChiTab?"Expense" : "Income";
        if(dateString.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap ngay",Toast.LENGTH_SHORT).show();
            return;
        }
        if(amoutText.isEmpty())
        {
            Toast.makeText(curContext,"Vui long nhap tien",Toast.LENGTH_SHORT).show();
            return;
        }
        double amout = Double.parseDouble(amoutText);
        if(categoryID==0)
        {
            Toast.makeText(curContext,"Vui long chon danh muc",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng chuỗi
        Date date=null;
        try {
            // Chuyển chuỗi sang Date
            date = dateFormat.parse(dateString);
            System.out.println("Date: " + date);
        } catch (ParseException e) {
            // Xử lý ngoại lệ nếu chuỗi không đúng định dạng
            System.out.println("Định dạng ngày tháng không hợp lệ!");
            e.printStackTrace();
        }


        if(LongTermGoalName.isEmpty() && !LongTermGoalNames.isEmpty())
        {
            Toast.makeText(curContext,"Vui long chon danh muc tiet kiem",Toast.LENGTH_SHORT).show();
            return;
        }

        int LongTermGoalID = longTermGoalDAO.getLongTermGoalByName(LongTermGoalName);

        //        cap nhat du lieu
        editTransactions.setCreatedAt(date);
        editTransactions.setNote(note);
        editTransactions.setAmount(amout);
        editTransactions.setCategoryId(categoryID);
        editTransactions.setGoalId(LongTermGoalID);
        transactionsDAO.update(editTransactions);
    }

    private void createSprinner()
    {
        if(categoryName.equals("Tiết kiệm"))
        {
            LongTermGoalNames = longTermGoalDAO.getAllLongTermGoalName();
        }
        else LongTermGoalNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,LongTermGoalNames);
        spinnerDMTK.setAdapter(adapter);
    }
}