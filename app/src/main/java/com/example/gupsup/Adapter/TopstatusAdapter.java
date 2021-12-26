package com.example.gupsup.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gupsup.Activities.MainActivity;
import com.example.gupsup.R;
import com.example.gupsup.databinding.ItemStatusBinding;
import com.example.gupsup.modals.Status;
import com.example.gupsup.modals.Userstatus;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class TopstatusAdapter extends RecyclerView.Adapter<TopstatusAdapter.TopstatusViewholder> {
    private Context context;
    private ArrayList<Userstatus> userstatuses;
    public TopstatusAdapter(Context context, ArrayList<Userstatus> userstatuses) {
        this.context = context;
        this.userstatuses = userstatuses;
    }

    @NotNull
    @Override
    public TopstatusViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);
        return new TopstatusViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopstatusViewholder holder, int position) {
        Userstatus userstatus = userstatuses.get(position);
        Status laststatus = userstatus.getStatuses().get(userstatus.getStatuses().size()-1);
        Glide.with(context).load(laststatus.getImageurl()).into(holder.binding.image);
        holder.binding.circularStatusView.setPortionsCount(userstatus.getStatuses().size());
        holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for (Status status:userstatus.getStatuses()){
                    myStories.add(new MyStory(status.getImageurl()));

                }
                new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userstatus.getName()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(userstatus.getProfileimage()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return userstatuses.size();
    }

    public class TopstatusViewholder extends RecyclerView.ViewHolder{
        ItemStatusBinding binding;
        public TopstatusViewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemStatusBinding.bind(itemView);
        }
    }
}
