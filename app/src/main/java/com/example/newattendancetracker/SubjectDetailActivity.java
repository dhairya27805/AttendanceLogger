package com.example.newattendancetracker;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;

public class SubjectDetailActivity extends AppCompatActivity {

    private TextView tvSubjectName, tvTotalClasses, tvPresentClasses, tvPercentage;
    private RecyclerView rvSubjectLogs;
    private AttendanceAdapter adapter;
    private List<AttendanceModel> attendanceList;
    private DBHelper dbHelper;
    private String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DBHelper(this);
        subjectName = getIntent().getStringExtra("subject_name");

        tvSubjectName = findViewById(R.id.tvSubjectName);
        tvTotalClasses = findViewById(R.id.tvTotalClasses);
        tvPresentClasses = findViewById(R.id.tvPresentClasses);
        tvPercentage = findViewById(R.id.tvPercentage);
        rvSubjectLogs = findViewById(R.id.rvSubjectLogs);

        tvSubjectName.setText(subjectName);
        rvSubjectLogs.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList);
        rvSubjectLogs.setAdapter(adapter);

        findViewById(R.id.btnDeleteSubject).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Subject")
                    .setMessage("Are you sure you want to delete all records for " + subjectName + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteSubjectData(subjectName);
                        Toast.makeText(SubjectDetailActivity.this, "Subject records deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        loadSubjectData();
    }

    private void loadSubjectData() {
        int total = dbHelper.getSubjectTotalClasses(subjectName);
        int present = dbHelper.getSubjectPresentClasses(subjectName);
        float percent = dbHelper.calculatePercentage(subjectName);

        tvTotalClasses.setText(String.valueOf(total));
        tvPresentClasses.setText(String.valueOf(present));
        tvPercentage.setText(String.format("%.1f%%", percent));

        attendanceList.clear();
        Cursor cursor = dbHelper.getSubjectData(subjectName);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String subject = cursor.getString(1);
                String status = cursor.getString(2);
                String date = cursor.getString(3);
                attendanceList.add(new AttendanceModel(id, subject, status, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
