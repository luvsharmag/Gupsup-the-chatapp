package com.example.gupsup.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gupsup.Activities.MainActivity;
import com.example.gupsup.databinding.ActivityPhonenumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhonenumberActivity extends AppCompatActivity {
ActivityPhonenumberBinding binding;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhonenumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(PhonenumberActivity.this, MainActivity.class));
            finish();
        }
        binding.editText.requestFocus();
        binding.btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = binding.editText.getText().toString();
                Intent intent = new Intent(PhonenumberActivity.this, otpActivity.class);
                intent.putExtra("Number",number);
                startActivity(intent);
            }
        });
    }
}