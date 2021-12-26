package com.example.gupsup.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gupsup.R;
import com.example.gupsup.databinding.ItemreceiveBinding;
import com.example.gupsup.databinding.ItemsendBinding;
import com.example.gupsup.modals.Message;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {
     Context context;
     ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;
    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.itemsend,parent,false);
            return new sentviewholder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.itemreceive,parent,false);
            return new sentviewholder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderUid())){
            return ITEM_SENT;
        }else{
            return ITEM_RECEIVE;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if(holder.getClass()==sentviewholder.class){
            sentviewholder viewholder = (sentviewholder)holder;
            viewholder.binding.msg.setText(message.getMessage());
            if(message.getMessage().equals("photo")){
                viewholder.binding.image.setVisibility(View.VISIBLE);
                viewholder.binding.msg.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageurl()).placeholder(R.drawable.avatar).into(viewholder.binding.image);
            }
        }else{
            receiveviewholder viewholder = (receiveviewholder)holder;
            viewholder.binding.msg.setText(message.getMessage());
            if(message.getMessage().equals("photo")){
                viewholder.binding.image.setVisibility(View.VISIBLE);
                viewholder.binding.msg.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageurl()).placeholder(R.drawable.avatar).into(viewholder.binding.image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class sentviewholder extends RecyclerView.ViewHolder{
    ItemsendBinding binding;
    public sentviewholder(@NonNull @NotNull View itemView) {
        super(itemView);
        binding = ItemsendBinding.bind(itemView);
    }
}
public class receiveviewholder extends RecyclerView.ViewHolder{
    ItemreceiveBinding binding;
    public receiveviewholder(@NonNull @NotNull View itemView) {
        super(itemView);
        binding = ItemreceiveBinding.bind(itemView);
    }
}

}
