package com.example.newattendancetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AttendanceDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "attendance";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SUBJECT = "subject";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SUBJECT + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String subject, String status, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_DATE, date);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC", null);
    }

    public Cursor getSubjectData(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SUBJECT + " = ? ORDER BY date DESC", new String[]{subject});
    }

    public Cursor searchData(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SUBJECT + " LIKE ? OR " + COLUMN_DATE + " LIKE ? ORDER BY date DESC",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"});
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void deleteSubjectData(String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_SUBJECT + " = ?", new String[]{subject});
    }

    public float calculatePercentage(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor totalCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_SUBJECT + " = ?", new String[]{subject});
        totalCursor.moveToFirst();
        int total = totalCursor.getInt(0);
        totalCursor.close();

        if (total == 0) return 0;

        Cursor presentCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_SUBJECT + " = ? AND " + COLUMN_STATUS + " = 'Present'", new String[]{subject});
        presentCursor.moveToFirst();
        int present = presentCursor.getInt(0);
        presentCursor.close();

        return ((float) present / total) * 100;
    }

    public float calculateOverallPercentage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor totalCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        totalCursor.moveToFirst();
        int total = totalCursor.getInt(0);
        totalCursor.close();

        if (total == 0) return 0;

        Cursor presentCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 'Present'", null);
        presentCursor.moveToFirst();
        int present = presentCursor.getInt(0);
        presentCursor.close();

        return ((float) present / total) * 100;
    }

    public int getSubjectTotalClasses(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_SUBJECT + " = ?", new String[]{subject});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getSubjectPresentClasses(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_SUBJECT + " = ? AND " + COLUMN_STATUS + " = 'Present'", new String[]{subject});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public Cursor getUniqueSubjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT DISTINCT " + COLUMN_SUBJECT + " FROM " + TABLE_NAME, null);
    }
}
