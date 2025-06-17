package com.example.appcartochka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.appcartochka.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    NotesDataBaseHelper db;
    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new NotesDataBaseHelper(requireContext());
        binding = FragmentProfileBinding.inflate(inflater,container,false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_id", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        User user = db.getUserById(id);





        if( user.getImage().length > 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            binding.AvatarProfile.setImageBitmap(bitmap);
        }
        binding.fullnameUser.setText(user.fullName);
        binding.emailUser.setText(user.email);


        binding.logOutAction.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View v) {
                requireContext().getSharedPreferences("my_id", Context.MODE_PRIVATE).edit().putInt("id",-1).apply();
                startActivity(new Intent(requireContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        binding.editAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), EditProfileActivity.class));
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_id", Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("id", 0);
        User user = db.getUserById(id);

        if(user.getImage().length > 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            binding.AvatarProfile.setImageBitmap(bitmap);
        }
        binding.fullnameUser.setText(user.fullName);
        binding.emailUser.setText(user.email);
    }
}