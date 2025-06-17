package com.example.appcartochka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcartochka.databinding.ActivitySignUpBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class ActivitySignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    File image;
    ActivityResultLauncher<Intent> resultLauncher;

    NotesDataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerResult();

        db = new NotesDataBaseHelper(this);

        binding.Avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString();
                String fullname = binding.editTextFullnameSignUp.getText().toString();
                String password =  binding.editTextPassword.getText().toString();
                List<User> users =  db.getAllUsers();
                if( fullname.isEmpty()|| password.isEmpty() || email.isEmpty()){
                    Toast.makeText(ActivitySignUp.this,
                            "Все поля должны быть заполнены",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ActivitySignUp.this,
                            "Неверный формат почты",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i = 0; i < users.size();i++){
                    if(users.get(i).email.equals(email)){
                        Toast.makeText(ActivitySignUp.this,
                                "Такой пользователь уже существует",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                byte[] byteImage = new byte[0];
                Drawable drawable = binding.Avatar.getDrawable();
                if(drawable instanceof BitmapDrawable){
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteImage = stream.toByteArray();
                    byteImage = imagemTratada(byteImage);
                }
                if(byteImage.length >  50000){
                    Toast.makeText(ActivitySignUp.this,
                            "Изображение слишком велико",Toast.LENGTH_SHORT).show();

                }
                else{
                    User user = new User(email,fullname,password,
                            0,byteImage);
                    db.insertUser(user);
                    finish();
                    Toast.makeText(ActivitySignUp.this,
                            "Вы зарегистрировались",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private byte[] imagemTratada(
            byte[] imagem_img){
        while (imagem_img.length > 50000){
            Bitmap bitmap =
                    BitmapFactory.decodeByteArray(
                    imagem_img, 0,
                    imagem_img.length);
            Bitmap resized =
                    Bitmap.createScaledBitmap(bitmap,
                    (int)(bitmap.getWidth()*0.8),
                    (int)(bitmap.getHeight()*0.8),
                            true);
            ByteArrayOutputStream stream =
                    new ByteArrayOutputStream();
            resized.compress(
                    Bitmap.CompressFormat.PNG,
                    100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;
    }

    public void onSignInClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private  void pickImage() {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
            intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        }
        resultLauncher.launch(intent);
    }

    private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try {

                            Uri imageUri = o.getData().getData();
                            image = new File(imageUri.toString());
                            binding.Avatar.setImageURI(imageUri);
                        }
                        catch (Exception e){
                            Toast.makeText(ActivitySignUp.this,"Изображение не выбрано",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}