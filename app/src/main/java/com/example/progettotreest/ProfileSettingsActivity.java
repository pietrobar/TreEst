package com.example.progettotreest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity {
    private String newImage = null;
    private String oldName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);


        loadProfileInfo();


        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    if (uri!=null){
                        Log.d(MyStrings.PROVA,"URI: "+uri.toString());

                        InputStream imageStream=null;
                        try {
                            imageStream = getContentResolver().openInputStream(uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        newImage=Base64.encodeToString(byteArray, Base64.DEFAULT);
                        if (newImage.length()>137000 || selectedImage.getHeight()!=selectedImage.getWidth()){
                            new AlertDialog.Builder(this)
                                    .setTitle("L'immagine è troppo grande o non è quadrata")
                                    .setNegativeButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }else {
                            showSelectedImage(selectedImage);
                        }
                    }

                    else
                        Log.d(MyStrings.PROVA,"Non ha selezionato una immagine");

                });

        findViewById(R.id.profilePictureView).setOnClickListener(v -> {
            // Pass in the mime type you'd like to allow the user to select
            // as the input
            mGetContent.launch("image/*");

        });

        findViewById(R.id.save_profile_settings_btn).setOnClickListener(v -> {

            if (newImage!=null) {
                Log.d(MyStrings.PROVA, "image changed ");
            }

            EditText et = findViewById(R.id.editName);
            String newName = et.getText().toString();
            if (!oldName.equals(newName)){//the name has changed
                Log.d(MyStrings.PROVA, "name changed: "+newName);

            }

            CommunicationController.setProfile(this, Model.getInstance().getSid(), newImage, newName,response -> Log.d(MyStrings.VOLLEY,"aaa " + response.toString()),
                    error -> {
                        Log.d(MyStrings.VOLLEY,error.toString());
                        CommunicationController.connectionError(this, "Impossibile effettuare operazione");
                    });
            super.onBackPressed();



        });



    }

    private void loadProfileInfo() {
        //get info from server
        CommunicationController.getProfile(this,Model.getInstance().getSid(), response ->{
            Log.d(MyStrings.VOLLEY, response.toString());
            try {
                EditText et = findViewById(R.id.editName);
                et.setText(response.getString("name"));
                oldName = et.getText().toString();
                String picBase64 = response.getString("picture");
                if (!picBase64.equals("null")){
                    byte[] decodedString = Base64.decode(picBase64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    showSelectedImage(decodedByte);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d(MyStrings.VOLLEY, error.toString());
            CommunicationController.connectionError(this,"Problema di connessione");
        });
    }

    private void showSelectedImage(Bitmap decodedByte) {
        ImageView profileImage = this.findViewById(R.id.profilePictureView);
        profileImage.setImageBitmap(decodedByte);
    }
}