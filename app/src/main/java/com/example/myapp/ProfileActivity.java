package com.example.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 22;
    TextView tvName, tvEmail, tvPhone;
    Button BtnSignOut, BtnUpImg;
    ImageView ImgProfile;
    String UserID ;
    Uri Filepath;

    BottomNavigationView bottomNavigationView1;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage storage;
    StorageReference SR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        BtnSignOut = findViewById(R.id.BtnSignOut);
        ImgProfile = findViewById(R.id.ImgProfile);
        BtnUpImg = findViewById(R.id.BtnUpImg);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        SR = storage.getReference();

        bottomNavigationView1 = findViewById(R.id.BottomNav1);

        bottomNavigationView1.setSelectedItemId(R.id.NavProfile);
        bottomNavigationView1.setOnNavigationItemSelectedListener(this);


        UserID = mAuth.getCurrentUser().getUid();

        try{
        DocumentReference db = mStore.collection("Users").document(UserID);
        db.addSnapshotListener(ProfileActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                String ImgUrl = documentSnapshot.getString("URI");
                Picasso.get().load(ImgUrl).into(ImgProfile);
                tvName.setText(documentSnapshot.getString("Name"));
                tvEmail.setText(documentSnapshot.getString("Email"));
                tvPhone.setText(documentSnapshot.getString("Phone"));
                }
            });
        }
        catch(NullPointerException e5){
                Toast.makeText(ProfileActivity.this,"Error"+e5.getMessage(),Toast.LENGTH_SHORT).show();
        }




        BtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });




        ImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


        BtnUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });

    }




    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Filepath = data.getData();
            BtnUpImg.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Filepath);
                ImgProfile.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private void UploadImage() {
        if (Filepath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = SR.child("Images/ProfileImages/" + UserID);

            ref.putFile(Filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoLink = uri.toString();
                            //Update URI to FireStore
                            DocumentReference db = mStore.collection("Users").document(UserID);
                            db.update("URI", photoLink);
                        }
                    });
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    BtnUpImg.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e){

                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.NavHome:
                startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
                break;

            case R.id.NavProfile:
                break;

            case R.id.NavBook:
                startActivity(new Intent(ProfileActivity.this,Book1Activity.class));
                break;

            case R.id.NavSetting:
                break;
        }
        return true;
    }
}



