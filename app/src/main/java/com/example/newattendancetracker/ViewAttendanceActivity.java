package com.example.newattendancetracker;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceActivity extends AppCompatActivity {

    private RecyclerView rvLogs;
    private AttendanceAdapter adapter;
    private List<AttendanceModel> attendanceList;
    private DBHelper dbHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DBHelper(this);
        rvLogs = findViewById(R.id.rvLogs);
        searchView = findViewById(R.id.searchView);

        rvLogs.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList);
        rvLogs.setAdapter(adapter);

        loadLogs("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadLogs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadLogs(newText);
                return true;
            }
        });
    }

    private void loadLogs(String keyword) {
        attendanceList.clear();
        Cursor cursor;
        if (keyword.isEmpty()) {
            cursor = dbHelper.getAllData();
        } else {
            cursor = dbHelper.searchData(keyword);
        }

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
