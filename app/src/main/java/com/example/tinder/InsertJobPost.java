package com.example.tinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tinder.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class InsertJobPost extends AppCompatActivity {

    private EditText Job_Title, Job_Description, Job_Salary;
    private Button PostJob;

    private FirebaseAuth mAuth;
    private DatabaseReference mJob_Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_job_post);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        mJob_Post = FirebaseDatabase.getInstance().getReference().child("Post Job").child(uId);

        Job_Title = findViewById(R.id.EdtTxt_Job_Title);
        Job_Description = findViewById(R.id.EdtTxt_Job_Description);
        Job_Salary = findViewById(R.id.EdtTxt_Salary);

        PostJob = findViewById(R.id.btn_Post_Job);



        PostJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mJob_Post.push().getKey();

                String date = DateFormat.getDateInstance().format(new Date());
                String title = Job_Title.getText().toString().trim();
                String description = Job_Description.getText().toString().trim();
                String salary = Job_Salary.getText().toString().trim();

                Data data = new Data(title, description, salary, id, date);
                mJob_Post.child(id).setValue(data);

                Toast.makeText(InsertJobPost.this, "Job Posted Successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(InsertJobPost.this, PostJobs.class);
                startActivity(intent);
                finish();
            }
        });
    }
}