package com.example.tinder.Chat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tinder.R;

import androidx.recyclerview.widget.RecyclerView;



public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView mMessage;
    public LinearLayout mContainer;
    public  OnChatListener onChatListener;
    public ChatViewHolders(View itemView, OnChatListener onChatListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.onChatListener = onChatListener;
        mMessage = itemView.findViewById(R.id.message);
        mContainer = itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view) {
        onChatListener.onChatCLick(getAdapterPosition(),mMessage.getTextColors().toString());

    }
    public interface OnChatListener
    {
        void onChatCLick(int position,String color);
    }
//resultsChat = chatList
}
