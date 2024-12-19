package com.example.ptdd_btl_qlct_n7_final2.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaKeHoachBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;
import com.example.ptdd_btl_qlct_n7_final2.dao.LongTermGoalDAO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ThemSuaKeHoachActivity extends AppCompatActivity {

    private ActivityThemSuaKeHoachBinding binding;
    private Date selectedDate;
    private CheckBox checkBoxDefault;
    private LongTermGoalDAO longTermGoalDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Sử dụng View Binding
        binding = ActivityThemSuaKeHoachBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cấu hình Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initQuery();
        setupUI();
    }

    private void initQuery() {
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void setupUI() {
        // Xử lý sự kiện quay lại
        binding.imageBack.setOnClickListener(v -> navigateToLapKeHoach());

        // Nhận Intent để kiểm tra xem có phải là chỉnh sửa hay không
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name != null) {
            setupEditMode(intent);
        } else {
            setupAddMode();
        }

        // Xử lý sự kiện chọn ngày
        binding.etDeadline.setOnClickListener(v -> showDatePickerDialog());

        // Xử lý sự kiện lưu dữ liệu
        binding.btnSave.setOnClickListener(v -> saveData());
    }

    private void setupEditMode(Intent intent) {
        binding.btnSave.setText("Sửa");
        binding.tvTitleGoal.setText("Sửa kế hoạch");

        double targetAmount = intent.getDoubleExtra("targetAmount", 0);
        double currentProgress = intent.getDoubleExtra("currentProgress", 0);
        Date deadline = (Date) intent.getSerializableExtra("deadline");
        boolean isDefault = getIntent().getBooleanExtra("isDefault", false);

        DecimalFormat df = new DecimalFormat("#,###");
        binding.etPlanName.setText(intent.getStringExtra("name"));
        binding.etTargetAmount.setText(df.format(targetAmount));
        binding.etCurrentAmount.setText(df.format(currentProgress));
        binding.checkboxDefault.setChecked(isDefault);

        if (deadline != null) {
            binding.etDeadline.setText(new SimpleDateFormat("dd/MM/yyyy").format(deadline));
            selectedDate = deadline;
        }
    }

    private void setupAddMode() {
        binding.btnSave.setText("Thêm");
        binding.tvTitleGoal.setText("Thêm kế hoạch");
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = new Date(year - 1900, month, dayOfMonth);
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(selectedDate);
                    binding.etDeadline.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveData() {
        String planName = binding.etPlanName.getText().toString();
        String targetAmountText = binding.etTargetAmount.getText().toString().replace(".", "");  // Loại bỏ dấu phân cách ngàn
        String currentAmountText = binding.etCurrentAmount.getText().toString().replace(".", "");  // Loại bỏ dấu phân cách ngàn
        boolean isDefault = binding.checkboxDefault.isChecked();

        // Validate dữ liệu
        if (!validateInputs(planName, targetAmountText, currentAmountText)) return;

        double targetAmount = Double.parseDouble(targetAmountText);
        double currentAmount = Double.parseDouble(currentAmountText);

        LongTermGoal goal = new LongTermGoal(planName, targetAmount, selectedDate, currentAmount, isDefault);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        if (name != null) {
            // Cập nhật dữ liệu
            int goalId = intent.getIntExtra("id", -1);
            if (goalId != -1) {
                goal.setId(goalId);
                longTermGoalDAO.update(goal);
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Thêm mới dữ liệu
            longTermGoalDAO.addWithDefaultCheck(goal);
            Toast.makeText(this, "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
        }

        navigateToLapKeHoach();
    }


    private boolean validateInputs(String planName, String targetAmountText, String currentAmountText) {
        if (planName.isEmpty()) {
            Toast.makeText(this, "Tên kế hoạch không được để trống!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (targetAmountText.isEmpty()) {
            Toast.makeText(this, "Số tiền mục tiêu không được để trống!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (currentAmountText.isEmpty()) {
            Toast.makeText(this, "Số tiền hiện tại không được để trống!", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double targetAmount = Double.parseDouble(targetAmountText);
            double currentAmount = Double.parseDouble(currentAmountText);

            if (targetAmount <= 0) {
                Toast.makeText(this, "Số tiền mục tiêu phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (currentAmount < 0 || currentAmount > targetAmount) {
                Toast.makeText(this, "Số tiền hiện tại phải trong khoảng từ 0 đến " + targetAmount + "!", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày deadline!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedDate.before(new Date())) {
            Toast.makeText(this, "Ngày deadline không được nằm trong quá khứ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void navigateToLapKeHoach() {
        Intent intent = new Intent(this, LapKeHoachActivity.class);
        startActivity(intent);
    }
}
