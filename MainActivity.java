package com.example.appcartochka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcartochka.databinding.ActivityMainBinding;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NotesDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("my_id", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", -1);
        if(id != -1){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new NotesDataBaseHelper(this);
    }


    public void onButtonClick(View view) {
        String email = binding.editTextNickname.getText().toString();
        String password =  binding.editTextPassword.
                getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this,
                    "Все поля должны быть заполнены",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(MainActivity.this,
                    "Неверный формат почты",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        List<User> users =  db.getAllUsers();
        boolean isDone = false;
        User[] userArray = users.toArray(new User[users.size()]);
        SharedPreferences sharedPreferences =
                getSharedPreferences("my_id",
                        Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0;i < userArray.length;i++){
            if(userArray[i].email.equals(email)
                    && userArray[i].password.equals(password)){
                isDone = true;
                editor.putInt("id", userArray[i].user_id);
                editor.apply();
                startActivity(new Intent(this,
                        HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            }
            else if(userArray[i].email.equals(email)
                    && !userArray[i].password.equals(password)){
                Toast.makeText(MainActivity.this,
                        "Неверный пароль",
                        Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if(!isDone){
            Toast.makeText(MainActivity.this,
                    "Такого пользователя не существует",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void onSignUpClick(View view) {
        Intent intent = new Intent(this, ActivitySignUp.class);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}