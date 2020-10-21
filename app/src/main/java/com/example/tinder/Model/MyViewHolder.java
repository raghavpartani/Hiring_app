package com.example.tinder.Model;

import android.view.View;
import android.widget.TextView;

import com.example.tinder.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View myView;
    public TextView mJobTitle,mJobDescription,mJobSalary,mJobDate;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        myView = itemView;

        mJobTitle = itemView.findViewById(R.id.Txt_Job_Title);
        mJobDate = itemView.findViewById(R.id.Txt_Job_Date);
        mJobDescription = itemView.findViewById(R.id.Txt_Job_Description);
        mJobSalary = itemView.findViewById(R.id.Txt_Job_Salary);
    }

   /* public void setJobTitle(String title) {
        TextView mTitle = myView.findViewById(R.id.Txt_Job_Title);
        mTitle.setText(title);
    }

    public void setJobDate(String date) {
        TextView mDate = myView.findViewById(R.id.Txt_Job_Date);
        mDate.setText(date);
    }

    public void setJobDescription(String description) {
        TextView mDescription = myView.findViewById(R.id.Txt_Job_Description);
        mDescription.setText(description);
    }

    public void setJobSalary(String salary) {
        TextView mSalary = myView.findViewById(R.id.Txt_Job_Salary);
        mSalary.setText(salary);
    } */

    @Override
    public void onClick(View v) {

    }
}
