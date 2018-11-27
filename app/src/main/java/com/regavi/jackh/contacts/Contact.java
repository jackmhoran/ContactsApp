package com.regavi.jackh.contacts;

import android.view.LayoutInflater;

public class Contact {

    private String name;
    private int number;
    private String email;
    private String bio;



    Contact(String name){
        this(name,0,"","");
    }
    Contact(String name, int number){
        this(name, number, "", "");
    }
    Contact(String name, String email){
        this(name, 0, email, "");
    }
    Contact(String name, int number, String email){
        this(name, number, email, "");
    }
    Contact(String name, int number, String email, String bio){
        this.name = name;
        this.number = number;
        this.email = email;
        this.bio = bio;
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

}
