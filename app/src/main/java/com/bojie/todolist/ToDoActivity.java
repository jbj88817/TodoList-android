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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ToDoActivity extends ActionBarActivity {

    static final String LOG_TAG = ToDoActivity.class.getSimpleName();

    private final int REQUEST_CODE = 20;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lv_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lv_items = (ListView) findViewById(R.id.lv_items);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lv_items.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.et_newItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ToDoActivity.this, EditItemActivity.class);
                intent.putExtra(Contract.KEY_DATA, items.get(position));
                intent.putExtra(Contract.KEY_POSITION, position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK  && requestCode == REQUEST_CODE) {
            String editedData = data.getExtras().getString(Contract.KEY_EDITED_DATA);
            int position = data.getExtras().getInt(Contract.KEY_POSITION, 0);
            Log.d(LOG_TAG, editedData);
            Log.d(LOG_TAG, position + "");
            items.remove(position);
            items.add(position, editedData);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
