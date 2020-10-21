package com.example.tinder.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.tinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity implements ChatViewHolders.OnChatListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private EditText mSendEditText;
    private Button mSendButton;
    NestedScrollView scrollView;
    private String currentUserID, matchId, chatId;
    Map newMessage;
    DatabaseReference newMessageDb, mDatabaseUser, mDatabaseChat;
    String sendMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        scrollView = findViewById(R.id.sendscroll);
        scrollDown();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        matchId = getIntent().getExtras().getString("matchId");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchId).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");
        getChatId();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(),ChatActivity.this, this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


    }

    private void sendMessage() {
         sendMessageText = mSendEditText.getText().toString();

        if (!sendMessageText.isEmpty()) {
            newMessageDb = mDatabaseChat.push();
            newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            newMessage.put("show","true");
            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages() {
        //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser = null;
                    String showornot = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(dataSnapshot.child("show").getValue()!=null){
                        showornot = dataSnapshot.child("show").getValue().toString();

                    /*  if(showornot.equals("false"))
                        {
                            mDatabaseChat.child(chatId).removeValue();
                        }*/
                    }


                    if(message!=null && createdByUser!=null && showornot.equalsIgnoreCase("true")){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                        scrollDown();
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private ArrayList<com.example.tinder.Chat.ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }
    void scrollDown()
    {
        Thread scrollThread = new Thread(){
            public void run(){
                try {
                    sleep(200);
                    ChatActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                             scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        scrollThread.start();
    }

    @Override
    public void onChatCLick(int position,String colors)
    {

        String color = colors;
        if(color.equals("ColorStateList{mThemeAttrs=nullmChangingConfigurations=0mStateSpecs=[[]]mColors=[-12566464]mDefaultColor=-12566464}"))
        {
            String[] singleChoiceItems = getResources().getStringArray(R.array.delete);
            int itemSelected = 0;
        new AlertDialog.Builder(ChatActivity.this)
                .setTitle("Select to delete for everyone")
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        if(selectedIndex==0)
                        {
                            delete();
                        }
                    }
                })
                .show();
    }
        else
            {
                Toast.makeText(this,"Click on your message to delete",Toast.LENGTH_SHORT).show();
            }
    }

    void delete()
    {
        Thread deleteThread = new Thread(){
            public void run(){
                try {
                    sleep(100);
                    ChatActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            newMessage.put("createdByUser", currentUserID);
                            newMessage.put("text", sendMessageText);
                            newMessage.put("show","false");
                            newMessageDb.setValue(newMessage);
                            mChatAdapter.notifyDataSetChanged();
                            finish();
                            startActivity(getIntent());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        deleteThread.start();
    }
}
