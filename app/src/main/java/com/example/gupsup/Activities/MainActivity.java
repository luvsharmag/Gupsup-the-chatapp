package com.example.gupsup.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gupsup.Adapter.TopstatusAdapter;
import com.example.gupsup.R;
import com.example.gupsup.databinding.ActivityMainBinding;
import com.example.gupsup.modals.Status;
import com.example.gupsup.modals.Userstatus;
import com.example.gupsup.modals.user;
import com.example.gupsup.Adapter.usersadapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
FirebaseDatabase database;
//user recycleview data or adapter
ArrayList<user> users;
com.example.gupsup.Adapter.usersadapter usersadapter;
//status recycleview data or adapter
TopstatusAdapter statusAdapter;
ArrayList<Userstatus> userstatuses;
//progress
ProgressDialog dialog;
//object of user class
user user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //progressdialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);
        //progressdialog
        database = FirebaseDatabase.getInstance();
        //users Recycleview setting
        binding.recyclerView.showShimmerAdapter();
        users = new ArrayList<>();
        usersadapter = new usersadapter(this,users);
        binding.recyclerView.setAdapter(usersadapter);
        //users Recycleview setting
        //shimmer effect
        //status recycleview setting
        binding.statuslist.showShimmerAdapter();
        userstatuses = new ArrayList<>();
        statusAdapter = new TopstatusAdapter(this,userstatuses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.statuslist.setLayoutManager(linearLayoutManager);
        binding.statuslist.setAdapter(statusAdapter);
        //status recycleview setting because we want orientation we need to do like this,

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user = snapshot.getValue(user.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });





        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    user user = snapshot1.getValue(user.class);
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                    users.add(user);
                }
                binding.recyclerView.hideShimmerAdapter();
                usersadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userstatuses.clear();
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        Userstatus status = new Userstatus();
                        status.setName(snapshot1.child("name").getValue(String.class));
                        status.setProfileimage(snapshot1.child("profileimage").getValue(String.class));
                        status.setLastupdate(snapshot1.child("lastupdate").getValue(Long.class));
                        userstatuses.add(status);

                        ArrayList<Status> statuses = new ArrayList<>();
                        for(DataSnapshot statussnapshot : snapshot1.child("statuses").getChildren()){
                            Status samplestatus = statussnapshot.getValue(Status.class);
                            statuses.add(samplestatus);
                        }
                        status.setStatuses(statuses);
                        userstatuses.add(status);
                    }
                    binding.statuslist.hideShimmerAdapter();
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.status:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,75);
                        break;
                }
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(data.getData()!=null){
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status").child(date.getTime()+"");
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Userstatus userstatus = new Userstatus();
                                    userstatus.setName(user.getName());
                                    userstatus.setProfileimage(user.getProfileimage());
                                    userstatus.setLastupdate(date.getTime());

                                    HashMap<String,Object> obj = new HashMap<>();
                                    obj.put("name",userstatus.getName());
                                    obj.put("profileimage",userstatus.getProfileimage());
                                    obj.put("lastupdate",userstatus.getLastupdate());

                                    String imageurl = uri.toString();
                                    Status status = new Status(imageurl,userstatus.getLastupdate());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);
                                    database.getReference().child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(id).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String id = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(id).setValue("Offline");
        super.onStop();
    }
    //    @Override
//    protected void onStop() {
//        String id = FirebaseAuth.getInstance().getUid();
//        database.getReference().child("presence").child(id).setValue("Offline");
//        super.onStop();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.groups:
                Toast.makeText(this, "groups", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }
}