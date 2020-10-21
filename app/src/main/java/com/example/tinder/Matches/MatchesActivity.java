package com.example.tinder.Matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tinder.MainActivity;
import com.example.tinder.MapAcitvity.mapactivity;
import com.example.tinder.R;
import com.example.tinder.Reject.RejectMatchesObject;
import com.example.tinder.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigationbar, R.string.close_navigationbar);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navi_view);
        navigationView.setNavigationItemSelectedListener(this);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mMatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDatasetMatches(), MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);
        getUserMatchId();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainactivityitem1:
                        Intent in = new Intent(MatchesActivity.this,MainActivity.class);
                        startActivity(in);
                        finish();;

                        break;
                    case R.id.matchactivtyitem:
                        Toast.makeText(MatchesActivity.this, "You are in Matches", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        /*MatchesObject obj = new MatchesObject("ASD");
        resultMatches.add(obj);
        resultMatches.add(obj);
        resultMatches.add(obj);
        resultMatches.add(obj);
        mMatchesAdapter.notifyDataSetChanged(); */

        /*for (int i = 0; i<100; i++ )
        {
            MatchesObject obj = new MatchesObject(Integer.toString(i));
            resultMatches.add(obj);
        } */
        //mMatchesAdapter.notifyDataSetChanged();
    }


    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("yeps");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists())
                {
                    for (DataSnapshot match : datasnapshot.getChildren())
                    {
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists())
                {
                    String userID = datasnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";

                    if (datasnapshot.child("name").getValue() != null)
                    {
                        name = datasnapshot.child("name").getValue().toString();
                    }
                    if (datasnapshot.child("profileImageUrl").getValue() != null)
                    {
                        profileImageUrl = datasnapshot.child("profileImageUrl").getValue().toString();
                    }

                    MatchesObject obj = new MatchesObject(userID, name, profileImageUrl);
                    resultMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<MatchesObject> resultMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getDatasetMatches() {
        return resultMatches;

    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MatchesActivity.this, SettingsActivity.class);
            //intent.putExtra("userSex",userSex);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.Map) {
            Intent in = new Intent(MatchesActivity.this, mapactivity.class);
            startActivity(in);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}