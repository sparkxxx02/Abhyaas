package com.placements.abhyaas.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.placements.abhyaas.Login.Intro_Signin;
import com.placements.abhyaas.MainActivity;
import com.placements.abhyaas.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(() -> {

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));finish();
            }else{
                startActivity(new Intent(getApplicationContext(), Intro_Signin.class));finish();
            }
        },2000);

    }
}