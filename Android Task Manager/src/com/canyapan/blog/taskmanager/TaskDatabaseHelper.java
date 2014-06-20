package com.canyapan.blog.taskmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
	private static final int DatabaseVersion = 1;
    private static final String DatabaseName = "Tasks.db";
 
    public TaskDatabaseHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }
    
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
     
        if (!db.isReadOnly()) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	try {
            db.beginTransaction();
     
            db.execSQL("CREATE TABLE Task (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT NOT NULL, completed BOOLEAN DEFAULT 0);");
     
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	try {
            db.beginTransaction();
     
            db.execSQL("DROP TABLE IF EXISTS Task;");
            db.execSQL("CREATE TABLE Task (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT NOT NULL, completed BOOLEAN DEFAULT 0);");
     
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
