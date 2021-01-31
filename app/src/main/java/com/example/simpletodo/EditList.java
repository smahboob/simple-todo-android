package com.example.simpletodo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditList extends AppCompatActivity {

    private EditText updatedText;
    private String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.my_action_bar1);

        updatedText = findViewById(R.id.updateText);

        item = getIntent().getStringExtra(MainActivity.KEY_ITEM_NAME);
        int pos = getIntent().getIntExtra(MainActivity.KEY_ITEM_POS,-1);

        int idx = item.indexOf('*');
        String task = item.substring(0, idx-1);

        updatedText.setText(task);

    }

    //user is done editing, update the list item
    public void updateList(View view){

        String finalText;

        int idx = item.indexOf('*');
        int idx1 = item.indexOf('#');
        String todo = updatedText.getText().toString();
        String mDate = item.substring(idx+1, idx1-1);
        String mTime = item.substring(idx1+1);

        Log.d("PRINT: ", mDate);
        Log.d("PRINT: ", mTime);

        finalText = todo + " * " + mDate + " # " + mTime;



        if(!updatedText.getText().equals("")){
            Intent backToMain = new Intent(EditList.this, MainActivity.class);
            backToMain.putExtra(MainActivity.KEY_ITEM_NAME, finalText);
            backToMain.putExtra(MainActivity.KEY_ITEM_POS, getIntent().getIntExtra(MainActivity.KEY_ITEM_POS,-1));
            setResult(RESULT_OK, backToMain);
            finish();
        }
        else{
            updatedText.setError("Enter text to update!");
        }

    }

    public void returnToMain(View view){
        Intent back = new Intent(EditList.this, MainActivity.class);
        startActivity(back);
        finish();
    }

}