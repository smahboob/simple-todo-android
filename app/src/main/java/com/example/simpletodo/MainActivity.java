package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> itemsList;
    EditText textEntered;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
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
        if(!textEntered.getText().toString().equals("")){
            itemsList.add(textEntered.getText().toString());
            textEntered.setText("");
            itemAdapter.notifyItemInserted(itemsList.size()-1);
            saveData();
            Toast.makeText(MainActivity.this, "Item Successfully Added!", Toast.LENGTH_LONG).show();
        }
        else{
            textEntered.setError("Enter text");
            textEntered.requestFocus();
        }
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