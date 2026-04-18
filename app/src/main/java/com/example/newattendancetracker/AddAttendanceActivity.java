package com.example.newattendancetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class AddAttendanceActivity extends AppCompatActivity {

    private TextInputEditText etSubject, etDate;
    private RadioGroup rgStatus;
    private Button btnSave;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DBHelper(this);
        etSubject = findViewById(R.id.etSubject);
        etDate = findViewById(R.id.etDate);
        rgStatus = findViewById(R.id.rgStatus);
        btnSave = findViewById(R.id.btnSave);

        Calendar calendar = Calendar.getInstance();
        String currentDate = calendar.get(Calendar.YEAR) + "-" + 
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" + 
                String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        etDate.setText(currentDate);

        etDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                String selectedDate = year1 + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                etDate.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> {
            String subject = etSubject.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            int selectedId = rgStatus.getCheckedRadioButtonId();
            RadioButton rbStatus = findViewById(selectedId);
            String status = rbStatus.getText().toString();

            if (subject.isEmpty()) {
                etSubject.setError("Please enter subject name");
                return;
            }

            boolean isInserted = dbHelper.insertData(subject, status, date);
            if (isInserted) {
                Toast.makeText(this, "Attendance saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
