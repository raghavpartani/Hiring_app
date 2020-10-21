package com.example.tinder.Reject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.tinder.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RejectMatchesAdapter extends RecyclerView.Adapter<RejectMatchesViewHolder> {

    private List<RejectMatchesObject>matchesList;
    private Context context;

    public RejectMatchesAdapter(List<RejectMatchesObject> matchesList, Context context)
    {
        this.matchesList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RejectMatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        RejectMatchesViewHolder rcv = new RejectMatchesViewHolder((layoutView));
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull RejectMatchesViewHolder holder, int position)
    {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchesName.setText(matchesList.get(position).getName());
        if (!matchesList.get(position).getProfileImageUrl().equals("default"))
        {
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
