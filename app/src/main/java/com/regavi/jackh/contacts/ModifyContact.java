package com.regavi.jackh.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyContact extends AppCompatActivity {
    ImageView no;
    ImageView yes;

    ImageView image;
    String s;
    EditText name;
    EditText number;
    EditText email;
    EditText bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
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
        s = getIntent().getStringExtra("name");
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        name.setText(s);
        String temp = String.valueOf(preferences.getLong("number"+s,0)).equals("0") ? "" : String.valueOf(preferences.getLong("number"+s,0));
        number.setText(temp);
        email.setText(preferences.getString("email"+s,""));
        bio.setText(preferences.getString("bio"+s,""));
    }
    public void yes(View v){
        String cName = name.getText().toString();
        long cNumber = Long.parseLong(number.getText().toString());
        String cEmail = email.getText().toString();
        String cBio = bio.getText().toString();

        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        removeOldContact(cName);

            edit.putString("name" + cName, cName);
            edit.putLong("number" + cName, cNumber);
            edit.putString("email" + cName, cEmail);
            edit.putString("bio" + cName, cBio);
            edit.apply();
        leave();
    }
    private void removeOldContact(String name){
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        if(!name.equals(s)){
            edit.remove("name"+s);
            edit.remove("number"+s);
            edit.remove("email"+s);
            edit.remove("bio"+s);
        }
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
