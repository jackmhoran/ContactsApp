package com.regavi.jackh.contacts;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {
    TextView name;
    TextView number;
    TextView email;
    TextView bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        convert();
        instantiate();
    }
    private void convert(){
        name = (TextView) findViewById(R.id.contactName);
        number = (TextView) findViewById(R.id.contactNumber);
        email = (TextView) findViewById(R.id.contactEmail);
        bio = (TextView) findViewById(R.id.contactBio);

    }
    private void instantiate(){
        String s = getIntent().getStringExtra("name");
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        name.setText(s);
        long temp = preferences.getLong("number"+s,0);
        if(temp >= 1000000000 && temp <= 1111111111*9){
            number.setText(String.valueOf(temp).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3"));
        }
        else if(temp != 0){
            number.setText(""+temp);
        }
        else{
            number.setText("");
        }
        email.setText(preferences.getString("email"+s,""));
        bio.setText(preferences.getString("bio"+s,""));
    }
}
