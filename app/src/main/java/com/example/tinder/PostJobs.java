package com.example.tinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tinder.Model.Data;
import com.example.tinder.Model.JobsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.tinder.R.layout.activity_insert_job_post;
import static com.example.tinder.R.layout.activity_post_jobs;

public class PostJobs extends AppCompatActivity {

   // private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mJobAdapter;
    private RecyclerView.LayoutManager mJobLayoutManager;
    private String currentUserId, JobId;


    private FirebaseAuth mAuth;
    private DatabaseReference mJobPostDatabase;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PostJobs.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_post_jobs);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        String uId = mUser.getUid();
        mJobPostDatabase = FirebaseDatabase.getInstance().getReference().child("Post Job").child(uId);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = findViewById(R.id.recycler_Job_Post);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);


        mJobLayoutManager = new LinearLayoutManager(PostJobs.this);
        mRecyclerView.setLayoutManager(mJobLayoutManager);
        mJobAdapter = new JobsAdapter(getDatasetMatches(), PostJobs.this);
        mRecyclerView.setAdapter(mJobAdapter);
        getUserMatchId();
    }

    private ArrayList<Data> resultMatches = new ArrayList<Data>();
    private List<Data> getDatasetMatches() {
        return  resultMatches;
    }


    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Post Job").child(currentUserId);
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    JobId = datasnapshot.getValue().toString();
                    mJobPostDatabase = mJobPostDatabase.child(JobId);
                    if (datasnapshot.exists())
                    {
                        for (DataSnapshot match : datasnapshot.getChildren())
                        {
                            FetchMatchInformation(match.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });     }

            private void FetchMatchInformation(String key) {
                DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Post Job").child(currentUserId).child(key);
                userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        if (datasnapshot.exists())
                        {
                            String userId = datasnapshot.getKey();
                            String name = " ";
                            String profileImageUrl = " ";
                            String title = " ";
                            String date = " ";
                            String description = " ";
                            String salary = " ";

                            if (datasnapshot.child("title").getValue() != null)
                            {
                                title = datasnapshot.child("title").getValue().toString();
                            }
                            if (datasnapshot.child("description").getValue() != null)
                            {
                                description = datasnapshot.child("description").getValue().toString();
                            }
                            if (datasnapshot.child("salary").getValue() != null)
                            {
                                salary = datasnapshot.child("salary").getValue().toString();
                            }
                            if (datasnapshot.child("date").getValue() != null)
                            {
                                date = datasnapshot.child("date").getValue().toString();
                            }


                            Data obj = new Data(title, description, salary, userId, date );
                            resultMatches.add(obj);
                            mJobAdapter.notifyDataSetChanged();
                        }
                    }




                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError error) {

                                                   }


                                                   public void goToInsertJobs(View view) {

                                                       Intent intent = new Intent(PostJobs.this, InsertJobPost.class);
                                                       startActivity(intent);
                                                   }

    });
}

    public void goToInsertJobs(View view) {
        Intent intent=new Intent(PostJobs.this,InsertJobPost.class);
        startActivity(intent);
    }
}


            // @RequiresApi(api = Build.VERSION_CODES.N)
  /*  @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(Data.class, job_post_item, MyViewHolder.class, mJobPostDatabase) {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {

            }


        };
        /*FirebaseRecyclerAdapter<Data,MyViewHolder>adapter
        RecyclerView.Adapter<Data,MyViewHolder>adapter = new RecyclerView.Adapter<Data>() {
            @NonNull
            @Override
            public Data onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull Data holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };*/


     /*   Query category = null;
        FirebaseRecyclerOptions<ULocale.Category> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<ULocale.Category>().setQuery(category, ULocale.Category.class).build();
        FirebaseRecyclerAdapter<ULocale.Category, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<ULocale.Category, MenuViewHolder>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_post_item, parent, false);
                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull ULocale.Category model) {
                //code here
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }*/





