package com.bojie.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bojiejiang on 4/12/15.
 */
public class TodoItemDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "todoList.db";

    // To do table name
    private static final String TABLE_TODO = "todo_items";

    // To do columns names

    private static final String KEY_ID = BaseColumns._ID;
    private static final String KEY_BODY = "body";
    private static final String KEY_PRIORITY = "priority";


    public TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_BODY + " TEXT,"
                + KEY_PRIORITY + " INTEGER" + ");";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            // Wipe older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            // Create tables again
            onCreate(db);
        }
    }

    public void addTodoItem(TodoItem item) {
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_BODY, item.getBody());
        contentValues.put(KEY_PRIORITY, item.getPriority());
        // Insert row
        long id = db.insertOrThrow(TABLE_TODO, null, contentValues);
        item.setId((int)id);
        db.close();
    }

    public TodoItem getTodoItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct and execute query
        Cursor cursor = db.query(TABLE_TODO,  // Table
                        new String[] {KEY_ID, KEY_BODY, KEY_PRIORITY }, // Select
                KEY_ID + "= ?", new String[] {String.valueOf(id)}, // Where, args
                null, null, "id ASC", "100"); // GROUP BY, HAVING, ORDER BY, LIMIT

        if(cursor != null) {
            cursor.moveToFirst();
            // Load result into model object
            TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            Log.d("!!!INDEX OF ID", String.valueOf(cursor.getColumnIndexOrThrow(KEY_ID)));
            cursor.close();
            return item;
        }
        db.close();
        return null;
    }

    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();
        // Select all query
        String selectAllQuery = "SELECT * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if (cursor.moveToFirst()) {
            do{
                TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
                item.setId(cursor.getInt(0));
                todoItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoItems;
    }

    public int getTodoItemCount() {
        String selectAllQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public int updateTodoItem (TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BODY, item.getBody());
        values.put(KEY_PRIORITY, item.getPriority());
        values.put(KEY_ID, item.getId());
        // Update
        int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return result;
    }

    public void deleteTodoItem(TodoItem item) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the record with the specified id
        Log.d("!!! ID", item.getId()+"");
        db.delete(TABLE_TODO,  KEY_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        // Close the database
        db.close();
    }
}
