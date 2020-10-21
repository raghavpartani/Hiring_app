package com.example.tinder.Chat;

import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.tinder.Chat.ChatActivity;
import com.example.tinder.R;

import static androidx.core.widget.NestedScrollView.*;


public class ChatObject {
    private String message;
    private Boolean currentUser;
    NestedScrollView scrollView;
    public ChatObject(String message, Boolean currentUser){
        this.message = message;
        this.currentUser = currentUser;

    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);

    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }
}
