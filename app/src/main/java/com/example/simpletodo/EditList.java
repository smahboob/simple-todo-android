package com.example.simpletodo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditList extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText updatedText;
    String finalText;
    private String item;
    private String task;
    private String date;
    private String time;
    private EditText dateText;
    private EditText timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.my_action_bar1);

        updatedText = findViewById(R.id.updateText);
        dateText = findViewById(R.id.editTextDate);
        timeText = findViewById(R.id.editTextTime);
        dateText.setEnabled(false);
        timeText.setEnabled(false);

        item = getIntent().getStringExtra(MainActivity.KEY_ITEM_NAME);
        int pos = getIntent().getIntExtra(MainActivity.KEY_ITEM_POS,-1);

        int idx = item.indexOf('*');
        int idx1 = item.indexOf('#');

        task = item.substring(0, idx-1);
        date = item.substring(idx+8, idx1-1);
        time = item.substring(idx1+8);

        updatedText.setText(task);
        dateText.setText(date);
        timeText.setText(time);

    }

    //user is done editing, update the list item
    public void updateList(View view){

        String todo = updatedText.getText().toString();
        String mDate = date;
        String mTime = time;

        finalText = todo + " * Date: " + mDate + " # Time: " + mTime;


        if(!updatedText.getText().toString().equals("")){
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

    public void openDatePicker(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String mMonth;
        String mDate;
        mMonth = month < 10 ? "0"+(month+1) : ""+month;
        mDate = dayOfMonth < 10 ? "0"+dayOfMonth : ""+dayOfMonth;

        date = mMonth + "-" + mDate + "-" + year;
        dateText.setText(date);

    }

    public void openTimePicker(View view){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view1, hourOfDay, minute) -> {
                    String mmHour = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
                    String mmMinute = minute < 10 ? "0"+minute : ""+minute;
                    time = mmHour+":"+mmMinute;
                    timeText.setText(time);
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void returnToMain(View view){
        Intent back = new Intent(EditList.this, MainActivity.class);
        startActivity(back);
        finish();
    }

}