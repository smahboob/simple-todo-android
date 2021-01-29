package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditList extends AppCompatActivity {

    private static final String KEY_ITEM_NAME = "updated_name";
    private static final String KEY_ITEM_POS = "updated_pos";
    private EditText updatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        getSupportActionBar().setTitle("Edit Item");

        updatedText = findViewById(R.id.updateText);

        String name = getIntent().getStringExtra(MainActivity.KEY_ITEM_NAME);
        int pos = getIntent().getIntExtra(MainActivity.KEY_ITEM_POS,-1);

    }

    //user is done editing, update the list item
    public void updateList(View view){
        Intent backToMain = new Intent(EditList.this, MainActivity.class);
        backToMain.putExtra(KEY_ITEM_NAME, updatedText.getText().toString());
        backToMain.putExtra(KEY_ITEM_POS, getIntent().getIntExtra(MainActivity.KEY_ITEM_POS,-1));
    }
}