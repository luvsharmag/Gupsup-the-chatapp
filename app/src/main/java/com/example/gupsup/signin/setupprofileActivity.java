package com.example.gupsup.signin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.gupsup.Activities.MainActivity;
import com.example.gupsup.databinding.ActivitySetupprofileBinding;
import com.example.gupsup.modals.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class setupprofileActivity extends AppCompatActivity {
ActivitySetupprofileBinding binding;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
Uri selectedimage;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating Profile...");
        dialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);
            }
        });
        binding.btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.namebox.getText().toString();
                if(name.isEmpty()){
                    binding.namebox.setError("Please enter your name");
                    return;
                }
                dialog.show();
                if(selectedimage!=null){
                    StorageReference reference = storage.getReference().child("Profile").child(auth.getUid());
                    reference.putFile(selectedimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uid = auth.getUid();
                                        String name = binding.namebox.getText().toString();
                                        String phone = auth.getCurrentUser().getPhoneNumber();
                                        String image = uri.toString();
                                        user User = new user(uid,name,phone,image);
                                        database.getReference().child("Users").child(uid).setValue(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                startActivity(new Intent(setupprofileActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }else{
                    String uid = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();
                    user User = new user(uid,name,phone,"No image");
                    database.getReference().child("Users").child(uid).setValue(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                            startActivity(new Intent(setupprofileActivity.this,MainActivity.class));
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getData()!=null){
                binding.imageView.setImageURI(data.getData());
                selectedimage = data.getData();
            }
        }
    }
}