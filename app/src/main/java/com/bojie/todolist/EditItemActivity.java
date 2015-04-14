package com.bojie.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

     static final String LOG_TAG = EditItemActivity.class.getSimpleName();

    EditText etEditItem;
    int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.et_editItem);
        Intent intent = getIntent();
        String data = intent.getStringExtra(TodoListContract.KEY_DATA);
        mPosition = intent.getIntExtra(TodoListContract.KEY_POSITION, 0);
        etEditItem.setText(data);
        etEditItem.setSelection(etEditItem.getText().length());
    }

    public void btnSave(View view) {
        String editedData = etEditItem.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(TodoListContract.KEY_EDITED_DATA, editedData);
        intent.putExtra(TodoListContract.KEY_POSITION, mPosition);
//        Log.d(LOG_TAG, editedData);
//        Log.d(LOG_TAG, mPosition+"");
        setResult(RESULT_OK, intent);
        finish();
    }
}
