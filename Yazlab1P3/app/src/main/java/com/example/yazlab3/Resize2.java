package com.example.yazlab3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Resize2 extends AppCompatActivity {

    private StorageReference mStorageRef1;
    EditText TextView;
    String Baki = UUID.randomUUID().toString().replaceAll("-", "");
    ImageView imageView1;
    public Uri imageUri1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resize2);

        mStorageRef1 = FirebaseStorage.getInstance().getReference();

        imageView1 = findViewById(R.id.iView1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageUri1 = Uri.parse(extras.getString("imageUri"));
            imageView1.setImageURI(imageUri1);
        } else {
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentMain);
        }

        FotoYukleme();


        Button btnYukleme1 = findViewById(R.id.button4);
        btnYukleme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FotoIndirme();
            }
        });

    }

    private void FotoYukleme() {


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Yükleniyor...");
        progressDialog.show();
        StorageReference Ref = mStorageRef1.child(Baki + "." + "jpg");
        Ref.putFile(imageUri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(Resize2.this, "Fotoğraf Başarıyla Yüklendi", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(Resize2.this, "Fotoğraf Yüklemede Hata Oluştu ", Toast.LENGTH_LONG).show();
                        progressDialog.cancel();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Yüklenme Durumu " + (int) progress + "%");
                if (progress == 100.0)
                    progressDialog.cancel();
            }
        });
    }


    public void FotoIndirme() {

        TextView = findViewById(R.id.editText1);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef2 = storage.getReferenceFromUrl("gs://yazlab3-a9c33.appspot.com");
        StorageReference Reff;

        if (TextView.getText().toString().equalsIgnoreCase("90")) {
            Reff = mStorageRef2.child(Baki + "_1000x1150.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("80")) {
            Reff = mStorageRef2.child(Baki + "_1400x1600.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("70")) {
            Reff = mStorageRef2.child(Baki + "_1650x1950.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("60")) {
            Reff = mStorageRef2.child(Baki + "_1850x2200.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("50")) {
            Reff = mStorageRef2.child(Baki + "_2000x2500.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("40")) {
            Reff = mStorageRef2.child(Baki + "_2100x2750.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("30")) {
            Reff = mStorageRef2.child(Baki + "_2765x3000.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("20")) {
            Reff = mStorageRef2.child(Baki + "_2900x3300.jpg");
        } else if (TextView.getText().toString().equalsIgnoreCase("10")) {
            Reff = mStorageRef2.child(Baki + "_3150x3500.jpg");
        } else Reff = mStorageRef2.child(Baki + ".jpg");


        final long ONE_MEGABYTE = 1024 * 1024 * 12;
        Reff.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Glide.with(Resize2.this)
                                .load(bytes)
                                .into(imageView1);
                        Toast.makeText(Resize2.this, "Fotoğraf Başarıyla İdirildi", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Resize2.this, "Fotoğraf İndirilirken Hata Oluştu ", Toast.LENGTH_LONG).show();
            }
        });


    }


}






