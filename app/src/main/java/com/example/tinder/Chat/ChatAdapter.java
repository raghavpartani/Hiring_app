package com.example.tinder.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tinder.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;



public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<ChatObject> resultsChat;
    private Context context;
    private ChatViewHolders.OnChatListener monChatListener;


    public ChatAdapter(List<ChatObject> matchesList, Context context, ChatViewHolders.OnChatListener onChatListener){
        this.resultsChat = matchesList;
        this.context = context;
        this.monChatListener = onChatListener;
    }


    @Override
    public ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView,monChatListener);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatViewHolders holder, int position)
    {
        holder.mMessage.setText(resultsChat.get(position).getMessage());
        if(resultsChat.get(position).getCurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }else{
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#1A237E"));
        }

    }
    @Override
    public int getItemCount() {
        return this.resultsChat.size();
    }
}
