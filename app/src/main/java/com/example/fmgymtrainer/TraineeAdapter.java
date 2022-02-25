package com.example.fmgymtrainer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TraineeAdapter extends RecyclerView.Adapter<TraineeAdapter.TraineeHolder>{


    Context context;
    List<com.example.fmgymtrainer.TraineeListName> names2;

    public TraineeAdapter(Context context, List<com.example.fmgymtrainer.TraineeListName> names2) {
        this.context = context;
        this.names2 = names2;
    }

    @NonNull
    @Override
    public TraineeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_view, parent, false);
        return new TraineeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TraineeHolder holder, int position) {

        String hisUid = names2.get(position).getUid();
        String trainerNames = names2.get(position).getName();
        String profile = names2.get(position).getProfile();

        holder.name.setText(trainerNames);

        Glide.with(context).load(profile).into(holder.chatprof);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, com.example.fmgymtrainer.ChatActivity.class);
                intent.putExtra("hisUid", hisUid);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return names2.size();
    }

    class TraineeHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView chatprof;

        public TraineeHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.listName);
            chatprof = itemView.findViewById(R.id.chatprofiles2);
        }
    }

}
