package com.regavi.jackh.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
    LinearLayout mainContent;
    LinearLayout userBar;
    ImageView plusSymbol;
    ImageView userImage;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        convert();
        populate();
        onClickMe();
    }

    private void convert(){
        userBar = (LinearLayout) findViewById(R.id.userBar);
        plusSymbol = (ImageView) findViewById(R.id.plus);
        mainContent = (LinearLayout) findViewById(R.id.main);
        userImage = (ImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
    }
    private void populate(){
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        loadContacts(preferences.getAll());
        userName.setText(preferences.getString("myName","You"));
        setMyImage(preferences.getString("myImage",""));


    }

    private void onClickMe(){
        //For viewing me
        final Intent toViewMe = new Intent(this, ViewMe.class);
        userBar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(toViewMe);
            }
        });
        //For modifying me
        final Intent toEditMe = new Intent(this, EditMe.class);
        userImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(toEditMe);
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

    private void addContact(String name){
        LinearLayout contactView = new LinearLayout(this);
        contactView.setOrientation(LinearLayout.HORIZONTAL);
        contactView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        contactView.addView(setImage(name));
        contactView.addView(setName(name));
        mainContent.addView(contactView);
        addSpacing();
        addOnClickView(contactView, name);
    }
    private TextView setName(String name){
        TextView contactName = new TextView(this);
        contactName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        contactName.setText(capitalizeName(name));
        int paddingTemp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        contactName.setPadding(paddingTemp,paddingTemp,paddingTemp,paddingTemp);
        return contactName;
    }
    private String capitalizeName(String name){
        char[] chars = name.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    private ImageView convertStringToImageView(String encodedString){
            ImageView image = new ImageView(this);
            byte[] imageAsBytes = Base64.decode(encodedString.getBytes(), Base64.DEFAULT);
            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

            final float scale = getResources().getDisplayMetrics().density;
            int independentSize = (int) (30 * scale);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(independentSize, independentSize);
            image.setLayoutParams(layoutParams);
            return image;

    }
    private void setMyImage(String encodedString){
        if(encodedString.equals("")){
            userImage = getDefaultImage();
        }
        else {
            byte[] imageAsBytes = Base64.decode(encodedString.getBytes(), Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            userImage.setImageBitmap(imageBitmap);
        }
    }
    private ImageView setImage(String name){
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        String encodedString = preferences.getString("image" + name, "");
        if(encodedString.equals("")){ return getDefaultImage(name); }
        else { return convertStringToImageView(encodedString); }
    }
    private ImageView getDefaultImage(){
        ImageView  image = new ImageView(this);
        image.setImageResource(R.drawable.anon);
        int sizeTemp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        image.setLayoutParams(new ViewGroup.LayoutParams(sizeTemp, ViewGroup.LayoutParams.MATCH_PARENT));
        return image;
    }

    private ImageView getDefaultImage(String name){
        ImageView  image = new ImageView(this);
        image.setImageResource(R.drawable.anon);
        int sizeTemp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        image.setLayoutParams(new ViewGroup.LayoutParams(sizeTemp, ViewGroup.LayoutParams.MATCH_PARENT));
        addOnClickModify(image, name);
        return image;
    }
    //TODO CLASS EXTENDS VIEW
    //OOP CONTACT RATHER THAN CLASS
    private void addSpacing(){
        LinearLayout separation = new LinearLayout(this);
        separation.setOrientation(LinearLayout.HORIZONTAL);
        separation.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        separation.setBackgroundColor(getColor(R.color.colorPrimaryDarkPlus));
        mainContent.addView(separation);
    }
    private void addOnClickModify(ImageView contactImage, String name){
        //For modifying contact
        final Intent toModifyContact = new Intent(this, ModifyContact.class);
        toModifyContact.putExtra("name",name);
        contactImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(toModifyContact);
            }
        });
    }
    private void addOnClickView(LinearLayout contact, String name){
        //For viewing contact
        final Intent toViewContact = new Intent(this, ViewContact.class);
        toViewContact.putExtra("name",name);
        contact.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivity(toViewContact);
            }
        });
    }
}
