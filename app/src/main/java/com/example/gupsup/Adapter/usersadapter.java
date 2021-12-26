package com.example.gupsup.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gupsup.Activities.chatActivity;
import com.example.gupsup.R;
import com.example.gupsup.databinding.RowConversationBinding;
import com.example.gupsup.modals.user;

import java.util.ArrayList;

public class usersadapter extends RecyclerView.Adapter<usersadapter.userviewholder>{
    Context context;
    ArrayList<user> users;
    public usersadapter(Context context,ArrayList<user> users){
        this.context = context;
        this.users = users;
    }
    @Override
    public userviewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false);
        return new userviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usersadapter.userviewholder holder, int position) {
        user user = users.get(position);
        holder.binding.username.setText(user.getName());
        Glide.with(context).load(user.getProfileimage()).placeholder(R.drawable.avatar).into(holder.binding.profile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("image",user.getProfileimage());
                intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class userviewholder extends RecyclerView.ViewHolder{
        RowConversationBinding binding;
        public userviewholder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }

}
