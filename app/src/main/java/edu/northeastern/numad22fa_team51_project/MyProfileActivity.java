package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import edu.northeastern.numad22fa_team51_project.models.UserModel;

public class MyProfileActivity extends AppCompatActivity {

    private Intent intent;
    private Uri selectedImageUri = null;
    public UserModel curr_user;
    private ImageView user_img;
    private EditText user_name;
    private EditText user_mobile;
    private EditText user_email;
    private EditText user_pass;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String UserImageURI = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFirebaseUserData();

        user_img = (ImageView) findViewById(R.id.my_profile_img_view);
        user_name = (EditText) findViewById(R.id.user_name_my_profile_edit_text);
        user_mobile = (EditText) findViewById(R.id.mobile_my_profile_edit_text);
        user_email = (EditText) findViewById(R.id.email_id_my_profile_edit_text);
        user_pass = (EditText) findViewById(R.id.password_my_profile_edit_text);

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractedCheckSelfPermission();
            }
        });
    }

    private void extractedCheckSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Constants.showImageChooser(MyProfileActivity.this);
        }else{
            ActivityCompat.requestPermissions(MyProfileActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.STORAGE_PERMISSIONS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data != null){
            selectedImageUri = data.getData();
            uploadBoardImage();
            try{
                user_img.setImageURI(selectedImageUri);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.STORAGE_PERMISSIONS){
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this);
            }else{
                Toast.makeText(this, "You denied permission for storage, change from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getFirebaseUserData(){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curr_user = snapshot.getValue(UserModel.class);

                // user_img.setImageBitmap();
                user_name.setText(curr_user.getUser_name());

                //user_img.set

                if (!curr_user.getUser_mobile().equals("0")){
                    user_mobile.setText(curr_user.getUser_mobile());
                }

                Picasso.get().load(curr_user.getUser_img()).into(user_img);

                user_email.setText(curr_user.getUser_email());
                user_pass.setText(curr_user.getUser_passwd());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfileActivity.this, "Failed to fetch user data, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFirebaseUserData(View view){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());

        HashMap<String, String> hMap = new HashMap<>();
        hMap.put("user_id", mAuth.getUid());
        hMap.put("user_email", curr_user.getUser_email());
        if (selectedImageUri == null) {
            hMap.put("user_img", " ");
        }else{
            //uploadBoardImage();
            hMap.put("user_img", UserImageURI);
        }

        if (!user_mobile.getText().equals("") || !user_mobile.getText().equals(" ")){
            hMap.put("user_mobile", user_mobile.getText().toString());
        }else{
            hMap.put("user_mobile", "0");
        }
        hMap.put("user_name", user_name.getText().toString());
        hMap.put("user_passwd", user_pass.getText().toString());

        // check if user made any changes
        if ((hMap.get("user_name").equals(curr_user.getUser_name())) && (hMap.get("user_img").equals(curr_user.getUser_img())) &&
                (hMap.get("user_mobile").equals(curr_user.getUser_mobile())) && (hMap.get("user_passwd").equals(curr_user.getUser_passwd()))){
            Toast.makeText(MyProfileActivity.this, "No Changes made", Toast.LENGTH_SHORT).show();
        }else{
            databaseReference.setValue(hMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MyProfileActivity.this, "User Data Updated", Toast.LENGTH_SHORT).show();
                        setResult(1002);
                        finish();
                    }
                }
            });
        }
    }


    private void uploadBoardImage() {
        StorageReference sref = FirebaseStorage.getInstance().getReference().child("USER_IMAGES").child(
                "USER_IMAGE" + System.currentTimeMillis() + "."
                        + Constants.getFileExtension(this, selectedImageUri)
        );
        sref.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload is successful
                        Log.i(
                                "Firebase Image URL",
                                taskSnapshot.getMetadata().getReference().getDownloadUrl().toString()
                        );
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(
                                new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.e("Downloadable Image URL", uri.toString());
                                        UserImageURI = uri.toString();
                                        // Call a function to create the board.
                                    }
                                }
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Toast.makeText(MyProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                          }
                                      }
                );

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}