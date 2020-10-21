package com.example.tinder.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tinder.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JobsAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Data> matchesList;
    private Context context;

    public JobsAdapter (List<Data> matchesList, Context context)
    {
        this.matchesList = matchesList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_post_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MyViewHolder rcv = new MyViewHolder((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mJobTitle.setText(matchesList.get(position).getTitle());
        holder.mJobDate.setText(matchesList.get(position).getDate());
        holder.mJobDescription.setText(matchesList.get(position).getDescription());
        holder.mJobSalary.setText(matchesList.get(position).getSalary());



    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
