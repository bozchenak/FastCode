package com.example.appcartochka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcartochka.databinding.ActivityInfoNoteBinding;

public class InfoNoteActivity extends AppCompatActivity {


    private ActivityInfoNoteBinding binding;

    NotesDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =  getIntent();

        String name =  intent.getStringExtra("Name");
        String address =  intent.getStringExtra("Address");
        String time =  intent.getStringExtra("Time");
        String date =  intent.getStringExtra("Date");
        int id =  intent.getIntExtra("Id",0);

        binding = ActivityInfoNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editTextNameInfo.setText(name);
        binding.editTextTimeInfo.setText(time);
        binding.editTextAddressInfo.setText(address);

        db = new NotesDataBaseHelper(this);


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextNameInfo.getText().toString();
                String address = binding.editTextAddressInfo.getText().toString();
                String time = binding.editTextTimeInfo.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("my_id", Context.MODE_PRIVATE);
                int user_id = sharedPreferences.getInt("id", 0);

                item Item = new item(id,name,user_id,address, date,time);

                db.updateNote(Item,id);
                finish();
                Toast.makeText(InfoNoteActivity.this,"Отредактировано",Toast.LENGTH_SHORT).show();
            }
        });

    }
}