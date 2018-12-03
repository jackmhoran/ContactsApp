package com.regavi.jackh.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    LinearLayout m;
    LinearLayout userBar;
    ImageView plus;
    ImageView userImage;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        convert();
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        loadContacts(preferences.getAll());
        userName.setText(preferences.getString("myName","You"));
        onClickMe();
    }

    private void convert(){
        userBar = (LinearLayout) findViewById(R.id.userBar);
        plus = (ImageView) findViewById(R.id.plus);
        m = (LinearLayout) findViewById(R.id.main);
        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
    }
    private void onClickMe(){
        //For viewing me
        final Intent j = new Intent(this, ViewMe.class);
        userBar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(j);
            }
        });
        //For modifying me
        final Intent f = new Intent(this, EditMe.class);
        userImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(f);
            }
        });
    }


    public void toAdd(View v){
        Intent add = new Intent(this, AddContact.class);
        startActivity(add);
    }
    private void loadContacts(Map<String, ?> map){
        for(Map.Entry<String,?> entry : map.entrySet()){
            String temp = entry.getKey();
            if(temp.substring(0,4).equals("name")){
                addContact((String) entry.getValue());
            }
        }
    }
    private void addContact(String string){
        LinearLayout l = new LinearLayout(this);
        ImageView  image = new ImageView(this);
        TextView text = new TextView(this);

        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        image.setImageResource(R.drawable.anon);
        int sizeTemp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        image.setLayoutParams(new ViewGroup.LayoutParams(sizeTemp, ViewGroup.LayoutParams.MATCH_PARENT));


        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        text.setText(string);
        int paddingTemp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        text.setPadding(paddingTemp,paddingTemp,paddingTemp,paddingTemp);

        l.addView(image);
        l.addView(text);
        m.addView(l);

        addOnClick(l, image, string);
    }
    private void addOnClick(LinearLayout l, ImageView v, String name){
        //For viewing contact
        final Intent j = new Intent(this, ViewContact.class);
        j.putExtra("name",name);
        l.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(j);
            }
        });
        //For modifying contact
        final Intent f = new Intent(this, ModifyContact.class);
        f.putExtra("name",name);
        v.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(f);
            }
        });
    }
}
