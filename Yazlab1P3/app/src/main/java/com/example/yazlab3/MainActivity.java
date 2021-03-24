package com.example.yazlab3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 1;
    public static final int GALLERY_REQUEST = 2;
    public static final int REQUEST_PERMISSION = 200;

    private String imageFilePath = "";
    private StorageReference mStorageRef;

    ImageView imageView, İView;
    public Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");


//KAMERADAN FOTO ÇEKME
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }

        Button btnIslemler = findViewById(R.id.button3);
        btnIslemler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btnIslemler = findViewById(R.id.button3);
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                btnIslemler.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.setTitle("Seçim Zamanı");
                        alertDialog.setMessage("Görüntü İşleme Teknikleri");
                        alertDialog.setButton("SIKIŞTIRMA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent1 = new Intent(getApplicationContext(), Resize2.class);
                                startActivity(intent1);
                                //Intent intent1 = new Intent(MainActivity.this, Resize2.class);
                                intent1.putExtra("imageUri", imageUri.toString());
                                startActivity(intent1);
                            }
                        });
                        alertDialog.setButton2("SEGMENTASYON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent2 = new Intent(getApplicationContext(), Segmentation3.class);
                                startActivity(intent2);

                            }
                        });

                        alertDialog.setIcon(R.drawable.ic_action_name);
                        alertDialog.show();


                    }
                });
            }
        });

        Button btnKamera = findViewById(R.id.button1);
        imageView = findViewById(R.id.iView);

        btnKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, CAMERA_REQUEST);*/
                kameraIntenti();

            }
        });


//GALERİDEN FOTO SEÇME
        Button btnGaleri = findViewById(R.id.button2);
        btnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                //startActivityForResult(intent2, GALLERY_REQUEST);
                startActivityForResult(Intent.createChooser(intent2, "Foto Seç"), GALLERY_REQUEST);

            }
        });

    }


    private void kameraIntenti() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File fotoFile = null;
            try {
                fotoFile = FotoDosyaYoluOlusturucu();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (fotoFile != null) {
                Uri fotoUri = FileProvider.getUriForFile(this, "com.example.yazlab3.fileprovider", fotoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(pictureIntent, CAMERA_REQUEST);
                imageUri = fotoUri;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "İzin verdiğiniz için teşekkürler", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File FotoDosyaYoluOlusturucu() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                //imageView.setImageURI(Uri.parse(imageFilePath));
                Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
                //imageUri=Uri.parse(imageFilePath);
                imageView.setImageBitmap(bitmap);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "İşlemi İptal Ettiniz", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }

    }


}
