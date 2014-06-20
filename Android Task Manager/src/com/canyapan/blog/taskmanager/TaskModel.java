package com.canyapan.blog.taskmanager;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class TaskModel implements Serializable {
	private static final long serialVersionUID = -3562692232875804749L;
	
	private long id;
	private String text;
	private boolean done;
	
	public TaskModel(String text) {
		this(text, false);
	}

	public TaskModel(String text, boolean isDone) {
		this(-1, text, false);
	}
	
	private TaskModel(long id, String text, boolean isDone) {
		setId(id);
		setText(text);
		setDone(isDone);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
	public boolean insert(Context context) throws TaskDatabaseException {
		ContentValues insertValues = new ContentValues();
		insertValues.put("text", getText());
		 
		TaskDatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		try {
		    dbHelper = new TaskDatabaseHelper(context);
		    db = dbHelper.getWritableDatabase();
		 
		    long id = db.insert("Task", null, insertValues);
		 
		    if (id > 0) {
		        Log.d("Task DB", MessageFormat.format("Task has been inserted with id {0}.", id));
		        
		        setId(id);
		        return true;
		    }
		} catch (SQLiteException e) {
			throw new TaskDatabaseException("Error on inserting record.", e);
		} finally {
		    if (null != db) {
		        db.close();
		    }
		 
		    if (null != dbHelper) {
		        dbHelper.close();
		    }
		}
		
		return false;
	}
	
	public boolean update(Context context) throws TaskDatabaseException {
		if (id == -1) {
			throw new TaskDatabaseException("This record does not exists on database.");
		}
		
		String[] whereArgs = new String[1];
		whereArgs[0] = String.valueOf(getId());
		 
		ContentValues updateValues = new ContentValues();
		updateValues.put("text", getText());
		updateValues.put("completed", isDone() ? "1" : "0");
		 
		TaskDatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		try {
		    dbHelper = new TaskDatabaseHelper(context);
		    db = dbHelper.getWritableDatabase();
		 
		    int updatedRowCount = db.update("Task", updateValues, "id == ?", whereArgs);
		 
		    if (updatedRowCount > 0) {
		        Log.d("Task DB", MessageFormat.format("Task with id {0} has been updated.", getId()));
		        
		        return true;
		    }
		} catch (SQLiteException e) {
		    throw new TaskDatabaseException("Error on updating record(s).", e);
		} finally {
		    if (null != db) {
		        db.close();
		    }
		 
		    if (null != dbHelper) {
		        dbHelper.close();
		    }
		}
		
		return false;
	}
	
	public boolean delete(Context context) throws TaskDatabaseException {
		if (id == -1) {
			throw new TaskDatabaseException("This record does not exists on database.");
		}
		
		String[] whereArgs = new String[1];
		whereArgs[0] = String.valueOf(getId());
		 
		TaskDatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		try {
		    dbHelper = new TaskDatabaseHelper(context);
		    db = dbHelper.getWritableDatabase();
		 
		    int deletedRowCount = db.delete("Task", "id == ?", whereArgs);
		 
		    if (deletedRowCount > 0) {
		        Log.d("Task DB", MessageFormat.format("Task with id {0} has been deleted.", getId()));
		        
		        return true;
		    }
		} catch (SQLiteException e) {
			throw new TaskDatabaseException("Error on deleting record(s).", e);
		} finally {
		    if (null != db) {
		        db.close();
		    }
		 
		    if (null != dbHelper) {
		        dbHelper.close();
		    }
		}
		
		return false;
	}
	
	public static ArrayList<TaskModel> selectAllTasks(Context context) {
		TaskDatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		 
		try {
		    dbHelper = new TaskDatabaseHelper(context);
		    db = dbHelper.getReadableDatabase();
		 
		    cursor = db.query("Task", null, null, null, null, null, null);
		    
		    final ArrayList<TaskModel> activeTasks = new ArrayList<TaskModel>(cursor.getColumnCount());
		 
		    cursor.moveToFirst();
		 
		    if (cursor.isAfterLast()) {
		        return activeTasks;
		    }
		 
		    final int idColumnIndex = cursor.getColumnIndex("id");
		    final int textColumnIndex = cursor.getColumnIndex("text");
		    final int completedColumnIndex = cursor.getColumnIndex("completed");
		    
		    do {
		        TaskModel task = new TaskModel(
		                cursor.getLong(idColumnIndex),
		                cursor.getString(textColumnIndex),
		                cursor.getInt(completedColumnIndex) == 1);
		 
		        activeTasks.add(task);
		    } while (cursor.moveToNext());
		 
		    return activeTasks;
		} finally {
		    if (null != cursor) {
		        cursor.close();
		    }
		 
		    if (null != db) {
		        db.close();
		    }
		 
		    if (null != dbHelper) {
		        dbHelper.close();
		    }
		}
	}
	
	public static boolean deleteCompleted(Context context) throws TaskDatabaseException {
		String[] whereArgs = new String[] {String.valueOf(1)};
		 
		TaskDatabaseHelper dbHelper = null;
		SQLiteDatabase db = null;
		try {
		    dbHelper = new TaskDatabaseHelper(context);
		    db = dbHelper.getWritableDatabase();
		 
		    int deletedRowCount = db.delete("Task", "completed == ?", whereArgs);
		 
		    if (deletedRowCount > 0) {
		        Log.d("Task DB", MessageFormat.format("{0} completed task(s) has been deleted.", deletedRowCount));
		        
		        return true;
		    }
		} catch (SQLiteException e) {
			throw new TaskDatabaseException("Error on deleting record(s).", e);
		} finally {
		    if (null != db) {
		        db.close();
		    }
		 
		    if (null != dbHelper) {
		        dbHelper.close();
		    }
		}
		
		return false;
	}
}
