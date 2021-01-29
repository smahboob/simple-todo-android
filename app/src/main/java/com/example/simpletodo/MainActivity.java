package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> itemsList;
    EditText textEntered;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;

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
            itemAdapter.notifyItemInserted(itemsList.size()-1);
            Toast.makeText(MainActivity.this, "Item Successfully Added!", Toast.LENGTH_LONG).show();
        }
        else{
            textEntered.setError("Enter text");
            textEntered.requestFocus();
        }
    }

    public void updateAdapters(){
        itemAdapter = new ItemAdapter(itemsList);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}