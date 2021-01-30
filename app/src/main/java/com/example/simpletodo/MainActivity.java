package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    List<String> itemsList;
    EditText textEntered;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    String todoItemText;
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_ITEM_POS = "item_pos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEntered = findViewById(R.id.enterItemText);
        recyclerView = findViewById(R.id.ListViewRecycle);

        loadItems();
        updateAdapters();

    }

    public void AddItem(View view){
        todoItemText = textEntered.getText().toString();
        if(!todoItemText.equals("")){
            showDatePickerDialog();
        }
        else{
            textEntered.setError("Enter text!");
            textEntered.requestFocus();
        }
    }

    private void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
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

        todoItemText = todoItemText +  "  --> Date: " + mDate + "-" + mMonth + "-" + year;
        showTimePickerDialog();
    }

    private void showTimePickerDialog(){
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String mmHour;
                        String mmMinute;
                        mmHour = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
                        mmMinute = minute < 10 ? "0"+minute : ""+minute;

                        todoItemText = todoItemText + "  -->Time: " + mmHour + ":" + mmMinute;
                        itemsList.add(todoItemText);
                        textEntered.setText("");
                        itemAdapter.notifyItemInserted(itemsList.size()-1);
                        saveData();
                        Toast.makeText(MainActivity.this, "Item Successfully Added!", Toast.LENGTH_LONG).show();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    public void updateAdapters(){

        ItemAdapter.OnClickListener onClickListener = (int position) -> {
            Intent editIntent = new Intent(MainActivity.this, EditList.class);
            editIntent.putExtra(KEY_ITEM_NAME, itemsList.get(position));
            editIntent.putExtra(KEY_ITEM_POS, position);
            startActivityForResult(editIntent, 2);
        };


        ItemAdapter.OnLongClickListener onLongClickListener = position -> {
            // 1)remove from the array list, 2) notify the adapter, 3) make a toast
            if(position < itemsList.size()) {
                itemsList.remove(position);
                itemAdapter.notifyItemRemoved(position);
                saveData();
                Toast.makeText(MainActivity.this, "Item Successfully Removed!", Toast.LENGTH_LONG).show();
            }
        };

        itemAdapter = new ItemAdapter(itemsList, onLongClickListener, onClickListener);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //handle result of edit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            String itemName = data.getStringExtra(KEY_ITEM_NAME);
            int pos = data.getIntExtra(KEY_ITEM_POS, -1);
            itemsList.set(pos, itemName);
            itemAdapter.notifyItemChanged(pos);
            saveData();
            Toast.makeText(MainActivity.this, "Item Updated Successfully!", Toast.LENGTH_LONG).show();
        }
    }

    //this will return the file which will store the items from our lis
    private File getDataFile(){
        //pass the directory and the name of the file in the parameters
        File file = new File(getApplicationContext().getFilesDir(), "todoData.txt");
        return file;
    }

    //read all the lines in the data and load it
    private void loadItems(){
        try{
            itemsList = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e){
            itemsList = new ArrayList<>();
        }
    }

    //save all the items by writing them into the file
    private void saveData(){
        try {
            FileUtils.writeLines(getDataFile(), itemsList);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Unable to save data!", Toast.LENGTH_LONG).show();
        }
    }

}