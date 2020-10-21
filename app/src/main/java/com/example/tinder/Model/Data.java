package com.example.tinder.Model;

public class Data {

    private String Title;
    private String Description;
    private String Salary;

    private String id;
    private String Date;


    public Data(String title, String description, String salary, String id, String date) {
        this.Title = title;
        this.Description = description;
        this.Salary = salary;
        this.id = id;
        this.Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
