package com.regavi.jackh.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Map;

public class AddContact extends AppCompatActivity {
    ImageView no;
    ImageView yes;

    ImageView image;

    EditText name;
    EditText number;
    EditText email;
    EditText bio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        convert();

    }
    private void convert(){
        no = (ImageView) findViewById(R.id.no);
        yes = (ImageView) findViewById(R.id.yes);

        image = (ImageView) findViewById(R.id.contactImage);

        name = (EditText) findViewById(R.id.contactName);
        number = (EditText) findViewById(R.id.contactNumber);
        email = (EditText) findViewById(R.id.contactEmail);
        bio = (EditText) findViewById(R.id.contactBio);
    }
    public void yes(View v){
        String cName = name.getText().toString();
        if(isEmpty(cName)){}
        else {
            long cNumber = Long.parseLong(number.getText().toString());
            String cEmail = email.getText().toString();
            String cBio = bio.getText().toString();

            SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();

            if (hasDuplicate(preferences.getAll(), cName)) {
                duplicate();
            } else {
                edit.putString("name" + cName, cName);
                edit.putLong("number" + cName, cNumber);
                edit.putString("email" + cName, cEmail);
                edit.putString("bio" + cName, cBio);
                edit.apply();
            }
            leave();
        }
    }
    private boolean isEmpty(String s){
        if(s.equals("")){
            Context c = getApplicationContext();
            Toast toast = Toast.makeText(c, "Can't make a contact without a name!", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        else
            return false;
    }
    public void no(View v){
        leave();
    }
    private void leave(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
    private boolean hasDuplicate(Map<String, ?> map, String name){
        for(Map.Entry<String,?> entry : map.entrySet()){
            String temp = entry.getKey();
            if(temp.substring(0,4).equals("name")){
                if(entry.getValue().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }
    private void duplicate(){
        Context c = getApplicationContext();
        Toast toast = Toast.makeText(c, "This contact already exists!", Toast.LENGTH_SHORT);
        toast.show();
    }
}