package com.regavi.jackh.contacts;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {
    TextView name;
    TextView number;
    TextView email;
    TextView bio;
    String s;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        s = getIntent().getStringExtra("name");
        convert();
        instantiate();
    }
    private void convert(){
        name = (TextView) findViewById(R.id.contactName);
        number = (TextView) findViewById(R.id.contactNumber);
        email = (TextView) findViewById(R.id.contactEmail);
        bio = (TextView) findViewById(R.id.contactBio);
        image = (ImageView) findViewById(R.id.contactImage);

    }
    private void getImage(){
        SharedPreferences sharedPreferences = getSharedPreferences("contacts",MODE_PRIVATE);
        String encodedString = sharedPreferences.getString("image"+s,"");
        if(!encodedString.equals("")){
            byte[] imageAsBytes = Base64.decode(encodedString.getBytes(), Base64.DEFAULT);
            setImageView(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
    }
    private void setImageView(Bitmap bitmap){

        image.setImageBitmap(bitmap);
        final float scale = getResources().getDisplayMetrics().density;
        int independentSize  = (int) (150 * scale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(independentSize, independentSize);
        image.setLayoutParams(layoutParams);

    }
    private void instantiate(){
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
