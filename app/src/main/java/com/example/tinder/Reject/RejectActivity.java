package com.example.tinder.Reject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tinder.MainActivity;
import com.example.tinder.MapAcitvity.mapactivity;
import com.example.tinder.R;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RejectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reject_activity);

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

        mRecyclerView=findViewById(R.id.rejectedrecyclerView1);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mMatchesLayoutManager = new LinearLayoutManager(RejectActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new RejectMatchesAdapter(getDatasetMatches(), RejectActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);
        getUserMatchId();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainactivityitem1:
                        finish();
                        break;
                    case R.id.matchactivtyitem:
                        Toast.makeText(RejectActivity.this, "You are in Rejected", Toast.LENGTH_SHORT).show();
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
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("nope");
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
                    String name = " ";
                    String profileImageUrl = " ";

                    if (datasnapshot.child("name").getValue() != null)
                    {
                        name = datasnapshot.child("name").getValue().toString();
                    }
                    if (datasnapshot.child("profileImageUrl").getValue() != null)
                    {
                        profileImageUrl = datasnapshot.child("profileImageUrl").getValue().toString();
                    }

                    RejectMatchesObject obj = new RejectMatchesObject(userID, name, profileImageUrl);
                    resultMatches.add(obj);

                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<RejectMatchesObject> resultMatches = new ArrayList<RejectMatchesObject>();
    private List<RejectMatchesObject> getDatasetMatches() {

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
            Intent intent = new Intent(RejectActivity.this, SettingsActivity.class);
            //intent.putExtra("userSex",userSex);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.Map) {
            Intent in = new Intent(RejectActivity.this, mapactivity.class);
            startActivity(in);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}