package com.bojie.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class ToDoActivity extends ActionBarActivity {

    static final String LOG_TAG = ToDoActivity.class.getSimpleName();

    private final int REQUEST_CODE = 20;

    ArrayList<TodoItem> mTodoItemsList;
    ArrayAdapter<String> itemsAdapter;
    ArrayList<String> mItemBodyArray;
    ListView lv_items;
    TodoItemDatabase mTodoItemDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lv_items = (ListView) findViewById(R.id.lv_items);
        readFromSQLiteDB();
        setupListViewListener();
    }

    private void readFromSQLiteDB() {
        mTodoItemDatabase = new TodoItemDatabase(this);
        mTodoItemsList = (ArrayList<TodoItem>) mTodoItemDatabase.getAllTodoItems();
        updateBodyArrayList();
    }

    private void updateBodyArrayList() {
        mItemBodyArray = new ArrayList<String>();
        for (TodoItem item : mTodoItemsList) {
            mItemBodyArray.add(item.getBody());
        }
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mItemBodyArray);
        lv_items.setAdapter(itemsAdapter);
    }

    private void addItemToSQLiteDB(String data) {
        TodoItem todoItem = new TodoItem(data, 0);
        mTodoItemsList.add(todoItem);
        mTodoItemDatabase.addTodoItem(todoItem);
    }


    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.et_newItem);
        String itemText = etNewItem.getText().toString();
        //itemsAdapter.add(itemText);
        etNewItem.setText("");
        //writeItems();
        addItemToSQLiteDB(itemText);
        updateBodyArrayList();
    }

    private void setupListViewListener() {
        lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mTodoItemDatabase.deleteTodoItem(mTodoItemsList.get(position));
                mTodoItemsList.remove(position);
                updateBodyArrayList();
                //itemsAdapter.notifyDataSetChanged();
                //writeItems();

                return true;
            }
        });

        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ToDoActivity.this, EditItemActivity.class);
                intent.putExtra(TodoListContract.KEY_DATA, mTodoItemsList.get(position).getBody());
                intent.putExtra(TodoListContract.KEY_POSITION, position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    // Read from txt file
    /*private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }*/


    // Write to txt file
    /*private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK  && requestCode == REQUEST_CODE) {
            String editedData = data.getExtras().getString(TodoListContract.KEY_EDITED_DATA);
            int position = data.getExtras().getInt(TodoListContract.KEY_POSITION, 0);
            Log.d(LOG_TAG, editedData);
            Log.d(LOG_TAG, position + "");
            int id=  mTodoItemsList.get(position).getId();
            mTodoItemsList.remove(position);
            TodoItem todoItem = new TodoItem(id, editedData, 0);
            mTodoItemsList.add(position, todoItem);
            mTodoItemDatabase.updateTodoItem(mTodoItemsList.get(position));
            updateBodyArrayList();
            //itemsAdapter.notifyDataSetChanged();
            //writeItems();
        }
    }
}
