package com.example.tmi.fragments;

public class PostInfo {
    private String filename;
    private String Title;
    private String First_category;
    private String Second_category;
    private String DDay;
    private String StartDate;
    private String DueDate;
    private String Team;
    private String NumPerson;
    private String MaxNum;
    private String Link;
    private String Image_Link;

    public PostInfo(String Title, String First_category, String DDay, String Second_category, String StartDate, String DueDate, String Team,
                    String NumPerson, String MaxNum, String Link, String Image_Link, String filename){
        this.Title = Title;
        this.First_category = First_category;
        this.DDay = DDay;
        this.Second_category = Second_category;
        this.StartDate = StartDate;
        this.DueDate = DueDate;
        this.Team = Team;
        this.NumPerson = NumPerson;
        this.MaxNum = MaxNum;
        this.Link = Link;
        this.Image_Link = Image_Link;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFirst_category() {
        return First_category;
    }

    public void setFirst_category(String first_category) {
        First_category = first_category;
    }

    public String getSecond_category() {
        return Second_category;
    }

    public void setSecond_category(String second_category) {
        Second_category = second_category;
    }

    public String getDDay() {
        return DDay;
    }

    public void setDDay(String DDay) {
        this.DDay = DDay;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }

    public String getNumPerson() {
        return NumPerson;
    }

    public void setNumPerson(String numPerson) {
        NumPerson = numPerson;
    }

    public String getMaxNum() {
        return MaxNum;
    }

    public void setMaxNum(String maxNum) {
        MaxNum = maxNum;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getImage_Link() {
        return Image_Link;
    }

    public void setImage_Link(String image_Link) {
        Image_Link = image_Link;
    }


}
