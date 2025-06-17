package com.example.appcartochka;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcartochka.databinding.ActivityEditProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;

    NotesDataBaseHelper db;

    File image;

    ActivityResultLauncher<Intent> resultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerResult();
        db = new NotesDataBaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("my_id", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        User user = db.getUserById(id);

        if(user.getImage().length > 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            binding.editAvatarProfile.setImageBitmap(bitmap);
        }


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.editTextFullname.setText(user.fullName);
        binding.editEmail.setText(user.email);


        binding.editAvatarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });



        binding.btnSaveEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String fullnameUser  = binding.editTextFullname.getText().toString();
               String email  = binding.editEmail.getText().toString();

                byte[] byteImage = new byte[0];
                Drawable drawable = binding.editAvatarProfile.getDrawable();
                if(drawable instanceof BitmapDrawable){
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteImage = stream.toByteArray();
                    byteImage = imagemTratada(byteImage);
                }

                if(byteImage.length >  50000){
                    Toast.makeText(EditProfileActivity.this,
                            "Изображение слишком велико",Toast.LENGTH_SHORT).show();

                }
                else{
                    User newUser = new User(email,fullnameUser,user.password,user.user_id,byteImage);

                    db.updateUser(newUser,id);
                    finish();
                    Toast.makeText(EditProfileActivity.this,"Изменено",Toast.LENGTH_SHORT).show();
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
    private  void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
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


                            binding.editAvatarProfile.setImageURI(imageUri);

                        }catch (Exception e){
                            Toast.makeText(EditProfileActivity.this,"Изображение не выбрано",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}