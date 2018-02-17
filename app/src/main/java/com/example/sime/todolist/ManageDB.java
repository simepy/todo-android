package com.example.sime.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sime on 02/02/18.
 */

public class ManageDB extends SQLiteOpenHelper {

    private static final String nameDB = "Data";
    private static final String tableDB = "Task";
    private static final String columDB = "TaskName";

    public ManageDB(Context context) {
        super(context, nameDB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", tableDB, columDB);

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeDB = String.format("DELETE TABLE IF EXIST %s", tableDB);

        db.execSQL(upgradeDB);
        onCreate(db);
    }

    public void updateTask (String task, String newTask) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columDB, newTask);

        db.update(tableDB, values, columDB + " = ?", new String[]{task});
    }

    public void insertNewTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues val = new ContentValues();

        val.put(columDB, task);
        db.insertWithOnConflict(tableDB, null, val, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(tableDB, columDB + " = ?", new String[]{task});
        db.close();
    }

    public ArrayList<String> getTaskList() {
        ArrayList<String> listTask = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tableDB, new String[]{columDB}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(columDB);
            listTask.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return listTask;
    }
}
