package com.example.appcartochka;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appcartochka.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());


        binding.bottomNavBar.setOnItemSelectedListener(
                menuItem -> {
                    if(menuItem.getItemId() == R.id.home){
                        replaceFragment(new HomeFragment());
                        return  true;
                    }
                    else if(menuItem.getItemId() == R.id.profile){
                        replaceFragment(new ProfileFragment());
                        return  true;
                    }
                    return true;
                }

        );
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentLayer,fragment);
        fragmentTransaction.commit();
    }


}

