package com.example.tinder;
import com.example.tinder.cards;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class informationclass extends AppCompatActivity {
    private DatabaseReference usersDb,seconduserdb;
    String nameofuser,genderofuser;
    TextView t1,t2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informationwindow);

        t1 = findViewById(R.id.name_card);
        t2 = findViewById(R.id.gender_card);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.6));

        final Intent in = getIntent();
        final String name = in.getStringExtra("userid");
        Log.i("user","user is"+name);

                usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(name);
                usersDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot )
                    {
                         nameofuser = snapshot.child("name").getValue(String.class);
                         genderofuser = snapshot.child("Gender").getValue(String.class);
                         t1.setText(nameofuser);
                         t2.setText(genderofuser);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }

