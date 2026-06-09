package com.smartlearning.app.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.smartlearning.app.R;
import com.smartlearning.app.adapter.GradeAdapter;
import com.smartlearning.app.dao.GradeDAO;
import com.smartlearning.app.database.DatabaseHelper;
import com.smartlearning.app.model.Grade;
import com.smartlearning.app.utils.SessionManager;
import java.util.List;

public class GradeActivity extends AppCompatActivity {

    private RecyclerView rvGrades;
    private Button btnAddGrade;
    private TextView tvAverageGrade;
    private GradeDAO gradeDAO;
    private SessionManager sessionManager;
    private GradeAdapter adapter;
    private List<Grade> grades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        initViews();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        gradeDAO = new GradeDAO(dbHelper);
        sessionManager = new SessionManager(this);

        loadGrades();
        btnAddGrade.setOnClickListener(v -> showAddGradeDialog());
    }

    private void initViews() {
        rvGrades = findViewById(R.id.rvGrades);
        btnAddGrade = findViewById(R.id.btnAddGrade);
        tvAverageGrade = findViewById(R.id.tvAverageGrade);
        rvGrades.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadGrades() {
        int userId = sessionManager.getUserId();
        grades = gradeDAO.getAllGrades(userId);
        adapter = new GradeAdapter(grades, this);
        rvGrades.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
        // Cập nhật điểm trung bình
        double average = gradeDAO.getAverageScore(userId);
        if (Double.isNaN(average)) {
            average = 0;
        }
        tvAverageGrade.setText(String.format("%.1f", average));
    }

    private void showAddGradeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm điểm môn học");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(15, 15, 15, 15);

        EditText edtSubjectName = new EditText(this);
        edtSubjectName.setHint("Tên môn học");
        LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsName.setMargins(0, 0, 0, 20);
        edtSubjectName.setLayoutParams(paramsName);
        edtSubjectName.setPadding(10, 15, 10, 15);

        EditText edtScore = new EditText(this);
        edtScore.setHint("Điểm (0-10)");
        edtScore.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams paramsScore = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsScore.setMargins(0, 20, 0, 0);
        edtScore.setLayoutParams(paramsScore);
        edtScore.setPadding(10, 15, 10, 15);

        layout.addView(edtSubjectName);
        layout.addView(edtScore);

        builder.setView(layout);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            try {
                String subjectName = edtSubjectName.getText().toString().trim();
                String scoreStr = edtScore.getText().toString().trim();
                int userId = sessionManager.getUserId();

                if (!subjectName.isEmpty() && !scoreStr.isEmpty()) {
                    double score = Double.parseDouble(scoreStr);
                    
                    if (score < 0 || score > 10) {
                        Toast.makeText(GradeActivity.this, "Điểm phải từ 0 đến 10!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Grade grade = new Grade(0, userId, subjectName, score);
                    long result = gradeDAO.addGrade(grade);
                    if (result > 0) {
                        Toast.makeText(GradeActivity.this, "Thêm điểm thành công!", Toast.LENGTH_SHORT).show();
                        loadGrades();
                    } else {
                        Toast.makeText(GradeActivity.this, "Thêm điểm thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GradeActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(GradeActivity.this, "Điểm phải là số!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
