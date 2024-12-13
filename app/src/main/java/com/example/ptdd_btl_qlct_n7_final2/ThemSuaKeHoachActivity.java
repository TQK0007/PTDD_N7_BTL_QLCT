package com.example.ptdd_btl_qlct_n7_final2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.ptdd_btl_qlct_n7_final2.database.AppDatabase;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaDmactivityBinding;
import com.example.ptdd_btl_qlct_n7_final2.databinding.ActivityThemSuaKeHoachBinding;
import com.example.ptdd_btl_qlct_n7_final2.entity.LongTermGoal;
import com.example.ptdd_btl_qlct_n7_final2.dao.LongTermGoalDAO;

public class ThemSuaKeHoachActivity extends AppCompatActivity {


    private EditText etPlanName, etTargetAmount, etCurrentAmount, etDeadline;
    private Button btnSave;
    private TextView title;
    private Date selectedDate;
    private LongTermGoalDAO longTermGoalDAO; // DAO để thao tác với cơ sở dữ liệu

    private ImageView image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_sua_ke_hoach);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initQuery();

        // Khởi tạo các EditText
        etPlanName = findViewById(R.id.et_plan_name);
        etTargetAmount = findViewById(R.id.et_target_amount);
        etCurrentAmount = findViewById(R.id.et_current_amount);
        etDeadline = findViewById(R.id.et_deadline); // EditText cho ngày
        btnSave = findViewById(R.id.btn_save);
        title = findViewById(R.id.tv_title_goal);

        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(view -> navigateLapKeHoach());

        // Nhận Intent để kiểm tra xem có phải là chỉnh sửa không
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (intent != null && name != null) {

            double targetAmount = intent.getDoubleExtra("targetAmount", 0);
            double currentProgress = intent.getDoubleExtra("currentProgress", 0);
            Date deadline = (Date) getIntent().getSerializableExtra("deadline");

            btnSave.setText("Sửa");
            title.setText("Sửa kế hoạch");

            // Hiển thị dữ liệu vào các EditText
            etPlanName.setText(name);
            etTargetAmount.setText(String.valueOf(targetAmount));
            etCurrentAmount.setText(String.valueOf(currentProgress));

            if (deadline != null) {
                etDeadline.setText(new SimpleDateFormat("dd/MM/yyyy").format(deadline));
                selectedDate = deadline;
            }
        }
        else {
            btnSave.setText("Thêm");
            title.setText("Thêm kế hoạch");
        }

        // Xử lý sự kiện nhấn vào EditText ngày để mở DatePickerDialog
        etDeadline.setOnClickListener(v -> showDatePickerDialog());

        // Nút Lưu

        btnSave.setOnClickListener(v -> saveData());
    }


    private void initQuery()
    {
        longTermGoalDAO = AppDatabase.getInstance(this).longTermGoalDAO();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ThemSuaKeHoachActivity.this,
                (view, year1, month1, dayOfMonth1) -> {
                    selectedDate = new Date(year1 - 1900, month1, dayOfMonth1); // -1900 vì năm bắt đầu từ 1900 trong Date
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(selectedDate);
                    etDeadline.setText(formattedDate);
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void saveData() {
        // Lấy dữ liệu từ EditText
        String planName = etPlanName.getText().toString();
        String targetAmountText = etTargetAmount.getText().toString();
        String currentAmountText = etCurrentAmount.getText().toString();

        // Kiểm tra dữ liệu nhập vào
        if (planName.isEmpty()) {
            Toast.makeText(this, "Tên kế hoạch không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (targetAmountText.isEmpty()) {
            Toast.makeText(this, "Số tiền mục tiêu không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentAmountText.isEmpty()) {
            Toast.makeText(this, "Số tiền hiện tại không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        double targetAmount;
        double currentAmount;

        try {
            targetAmount = Double.parseDouble(targetAmountText);
            currentAmount = Double.parseDouble(currentAmountText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (targetAmount <= 0) {
            Toast.makeText(this, "Số tiền mục tiêu phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentAmount < 0 || currentAmount > targetAmount) {
            Toast.makeText(this, "Số tiền hiện tại phải trong khoảng từ 0 đến " + targetAmount + "!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày deadline!", Toast.LENGTH_SHORT).show();
            return;
        }

        Date today = new Date();
        if (selectedDate.before(today)) {
            Toast.makeText(this, "Ngày deadline không được nằm trong quá khứ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu tất cả validate đều hợp lệ, tiếp tục lưu dữ liệu
        LongTermGoal goal = new LongTermGoal(planName, targetAmount, selectedDate, currentAmount, true);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name != null) {
            // Nếu có intent, tức là đang chỉnh sửa
            int goalId = intent.getIntExtra("id", -1);  // Lấy ID từ Intent
            if (goalId != -1) {
                goal.setId(goalId); // Cập nhật ID vào đối tượng
                longTermGoalDAO.update(goal);  // Cập nhật vào cơ sở dữ liệu
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Nếu không có intent, tức là thêm mới
            longTermGoalDAO.add(goal);  // Thêm mới vào cơ sở dữ liệu
            Toast.makeText(this, "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
        }

        navigateLapKeHoach();  // Quay lại màn hình trước
    }


    private void navigateLapKeHoach() {
        Intent intent1 = new Intent(ThemSuaKeHoachActivity.this, LapKeHoachActivity.class);
        startActivity(intent1);
    }
}
