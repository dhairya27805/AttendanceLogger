package com.example.newattendancetracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvOverallPercentage, tvOverallStats;
    private RecyclerView rvSubjects;
    private FloatingActionButton fabAdd;
    private DBHelper dbHelper;
    private SubjectAdapter adapter;
    private List<SubjectModel> subjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        tvOverallPercentage = findViewById(R.id.tvOverallPercentage);
        tvOverallStats = findViewById(R.id.tvOverallStats);
        rvSubjects = findViewById(R.id.rvSubjects);
        fabAdd = findViewById(R.id.fabAdd);

        rvSubjects.setLayoutManager(new LinearLayoutManager(this));
        subjectList = new ArrayList<>();

        fabAdd.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, AddAttendanceActivity.class)));

        findViewById(R.id.btnViewAll).setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, ViewAttendanceActivity.class)));

        findViewById(R.id.btnDeleteAll).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Reset All Data")
                    .setMessage("Are you sure you want to delete all attendance records?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteAllData();
                        loadDashboardData();
                        Toast.makeText(DashboardActivity.this, "All data deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        float overallPercent = dbHelper.calculateOverallPercentage();
        tvOverallPercentage.setText(String.format("%.1f%%", overallPercent));
        if (overallPercent < 75) {
            tvOverallPercentage.setTextColor(Color.parseColor("#F44336"));
        } else {
            tvOverallPercentage.setTextColor(Color.parseColor("#4CAF50"));
        }

        int totalClasses = 0;
        int presentClasses = 0;

        subjectList.clear();
        Cursor cursor = dbHelper.getUniqueSubjects();
        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(0);
                int total = dbHelper.getSubjectTotalClasses(subject);
                int present = dbHelper.getSubjectPresentClasses(subject);
                float percent = dbHelper.calculatePercentage(subject);

                subjectList.add(new SubjectModel(subject, total, present, percent));
                
                totalClasses += total;
                presentClasses += present;
            } while (cursor.moveToNext());
        }
        cursor.close();

        tvOverallStats.setText("Total Classes: " + totalClasses + " | Present: " + presentClasses);

        adapter = new SubjectAdapter(subjectList, model -> {
            Intent intent = new Intent(DashboardActivity.this, SubjectDetailActivity.class);
            intent.putExtra("subject_name", model.getSubjectName());
            startActivity(intent);
        });
        rvSubjects.setAdapter(adapter);
    }
}
