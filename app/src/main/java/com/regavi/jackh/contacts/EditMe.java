package com.regavi.jackh.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditMe extends AppCompatActivity {
    ImageView deleteChangesImage;
    ImageView saveChangesImage;
    String encodedImage = "";
    ImageView image;
    EditText name;
    EditText number;
    EditText email;
    EditText bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_edit_me);
        convert();
        instantiate();
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
    private void instantiate(){
        SharedPreferences preferences = getSharedPreferences("contacts", MODE_PRIVATE);
        name.setText(preferences.getString("myName","You"));
        String temp = String.valueOf(preferences.getLong("myNumber",0)).equals("0") ? "" : String.valueOf(preferences.getLong("myNumber",0));
        number.setText(temp);
        email.setText(preferences.getString("myEmail",""));
        bio.setText(preferences.getString("myBio",""));
    }
    public void pickImage(View v){
        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick,0);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
        switch(requestCode){
            case 0:
                if(requestCode == 0 && resultCode == RESULT_OK && imageReturnedIntent != null){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        handleBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
    }
    private void handleBitmap(Bitmap bitmap){
        Bitmap newBitmap = makeCircularBitmap(bitmap);
        storeCroppedBitmap(newBitmap);
        setImageView(newBitmap);
    }
    private void setImageView(Bitmap bitmap){
        image.setImageBitmap(bitmap);
        final float scale = getResources().getDisplayMetrics().density;
        int independentSize  = (int) (150 * scale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(independentSize, independentSize);
        image.setLayoutParams(layoutParams);

    }
    private void storeCroppedBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);
        encodedImage = encoded;
    }
    public Bitmap makeCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public void onSaveChanges(View v){
        saveChanges();
    }
    private void saveChanges(){
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
    public void onDeleteChanges(View v){
        leave();
    }

    private void leave(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
