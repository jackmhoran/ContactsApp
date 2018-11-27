package com.regavi.jackh.contacts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class AddContact extends AppCompatActivity {
    ImageView deleteChangesImage;
    ImageView saveChangesImage;

    ImageView image;

    EditText name;
    EditText number;
    EditText email;
    EditText bio;

    Drawable drawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        convert();

    }
    private void convert(){
        deleteChangesImage = (ImageView) findViewById(R.id.deleteChanges);
        saveChangesImage = (ImageView) findViewById(R.id.saveChanges);

        image = (ImageView) findViewById(R.id.contactImage);

        name = (EditText) findViewById(R.id.contactName);
        number = (EditText) findViewById(R.id.contactNumber);
        email = (EditText) findViewById(R.id.contactEmail);
        bio = (EditText) findViewById(R.id.contactBio);
    }
    public void onSaveChanges(View v){
        String cName = name.getText().toString();
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        if(isEmpty(cName)){preventEmpty();}
        else if(hasDuplicate(preferences.getAll(),cName)){preventDuplicate();}
        else {
            saveChanges(cName);
        }
    }
    private void saveChanges(String cName){
        long cNumber = Long.parseLong(number.getText().toString());
        String cEmail = email.getText().toString();
        String cBio = bio.getText().toString();
//TODO MAKE onsavechanges and savechanges and duplicate/empty stuff in modifyContact
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        edit.putString("name" + cName, cName);
        edit.putLong("number" + cName, cNumber);
        edit.putString("email" + cName, cEmail);
        edit.putString("bio" + cName, cBio);
        edit.apply();

        leave();
    }
    public void pickImage(View v){
        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick,0);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
        switch(requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                        drawable = Drawable.createFromStream(inputStream, selectedImage.toString());
                        image.setImageDrawable(drawable);
                    } catch (FileNotFoundException e) {
                        drawable = getResources().getDrawable(R.drawable.anon);
                    }
                }
        }
    }
    private boolean isEmpty(String name){
        if(name.equals("")){
            return true;
        }
        else
            return false;
    }
    private void preventEmpty(){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "Can't make a contact without a name!", Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onDeleteChanges(View v){
        leave();
    }
    private void leave(){
        Intent toMainActivity = new Intent(this, MainActivity.class);
        startActivity(toMainActivity);
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
    private void preventDuplicate(){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "This contact already exists!", Toast.LENGTH_SHORT);
        toast.show();
    }
}