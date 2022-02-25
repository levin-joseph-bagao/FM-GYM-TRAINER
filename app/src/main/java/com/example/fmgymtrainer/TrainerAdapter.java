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

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.TrainerHolder>{


    Context context;
    List<com.example.fmgymtrainer.TrainerListName> names;

    public TrainerAdapter(Context context, List<com.example.fmgymtrainer.TrainerListName> names) {
        this.context = context;
        this.names = names;
    }

    @NonNull
    @Override
    public TrainerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_list_view, parent, false);
        return new TrainerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerHolder holder, int position) {

        String hisUid = names.get(position).getUid();
        String trainerNames = names.get(position).getName();
        String profile = names.get(position).getProfile();

        holder.name.setText(trainerNames);

        Glide.with(context).load(profile).into(holder.chatprof);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, com.example.fmgymtrainer.TrainerProfileVC.class);
                intent.putExtra("hisUid", hisUid);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class TrainerHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView chatprof;

        public TrainerHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.listName);
            chatprof = itemView.findViewById(R.id.chatprofiles2);
        }
    }

}
