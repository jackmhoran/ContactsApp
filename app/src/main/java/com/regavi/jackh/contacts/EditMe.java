package com.regavi.jackh.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditMe extends AppCompatActivity {
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
        setContentView(R.layout.activity_edit_me);
        convert();
        instantiate();
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
    private void instantiate(){
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        name.setText(preferences.getString("myName","You"));
        String temp = String.valueOf(preferences.getLong("myNumber",0)).equals("0") ? "" : String.valueOf(preferences.getLong("myNumber",0));
        number.setText(temp);
        email.setText(preferences.getString("myEmail",""));
        bio.setText(preferences.getString("myBio",""));
    }
    public void yes(View v){
        String cName = name.getText().toString().equals("") ? "You" : name.getText().toString();
        long cNumber = Long.parseLong(number.getText().toString());
        String cEmail = email.getText().toString();
        String cBio = bio.getText().toString();

        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        edit.putString("myName", cName);
        edit.putLong("myNumber", cNumber);
        edit.putString("myEmail", cEmail);
        edit.putString("myBio", cBio);
        edit.apply();
        leave();
    }

    public void no(View v){
        leave();
    }

    private void leave(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
