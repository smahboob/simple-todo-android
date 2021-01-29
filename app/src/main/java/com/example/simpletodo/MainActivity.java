package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> itemsList;
    EditText textEntered;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsList = new ArrayList<>();
        textEntered = findViewById(R.id.enterItemText);
        recyclerView = findViewById(R.id.ListViewRecycle);

        itemsList.add("Buy Milk");
        itemsList.add("Buy Sugar");
        itemsList.add("Buy Cake");

        updateAdapters();

    }

    public void AddItem(View view){
        if(!textEntered.getText().toString().equals("")){
            itemsList.add(textEntered.getText().toString());
            textEntered.setText("");
            updateAdapters();
        }
        else{
            textEntered.setError("Enter text");
            textEntered.requestFocus();
        }
    }

    public void updateAdapters(){
        ItemAdapter itemAdapter = new ItemAdapter(itemsList);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}