package com.smartlearning.app.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.smartlearning.app.database.DatabaseHelper;
import com.smartlearning.app.model.Grade;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    private DatabaseHelper dbHelper;

    public GradeDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long addGrade(Grade grade) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_GRADE_SUBJECT_ID, grade.getSubjectId());
        values.put(DatabaseHelper.COL_GRADE_USER_ID, grade.getUserId());
        values.put(DatabaseHelper.COL_GRADE_SUBJECT_NAME, grade.getSubjectName());
        values.put(DatabaseHelper.COL_GRADE_SCORE, grade.getScore());
        return db.insert(DatabaseHelper.TABLE_GRADES, null, values);
    }

    public List<Grade> getAllGrades(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Grade> grades = new ArrayList<>();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_GRADES,
                null,
                DatabaseHelper.COL_GRADE_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                DatabaseHelper.COL_GRADE_CREATED_AT + " DESC"
        );

        while (cursor.moveToNext()) {
            Grade grade = new Grade();
            grade.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GRADE_ID)));
            grade.setSubjectId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GRADE_SUBJECT_ID)));
            grade.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GRADE_USER_ID)));
            grade.setSubjectName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GRADE_SUBJECT_NAME)));
            grade.setScore(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GRADE_SCORE)));
            grade.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GRADE_CREATED_AT)));
            grades.add(grade);
        }
        cursor.close();
        return grades;
    }

    public double getAverageScore(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + DatabaseHelper.COL_GRADE_SCORE + ") FROM " + DatabaseHelper.TABLE_GRADES + " WHERE " + DatabaseHelper.COL_GRADE_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        double average = 0;
        if (cursor.moveToFirst()) {
            average = cursor.getDouble(0);
        }
        cursor.close();
        return average;
    }

    public void deleteGrade(int gradeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_GRADES, DatabaseHelper.COL_GRADE_ID + "=?", new String[]{String.valueOf(gradeId)});
    }

    public int updateGrade(Grade grade) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_GRADE_SUBJECT_NAME, grade.getSubjectName());
        values.put(DatabaseHelper.COL_GRADE_SCORE, grade.getScore());
        return db.update(DatabaseHelper.TABLE_GRADES, values, DatabaseHelper.COL_GRADE_ID + "=?", new String[]{String.valueOf(grade.getId())});
    }
}
