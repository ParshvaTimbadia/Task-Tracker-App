package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Objects;

import edu.northeastern.numad22fa_team51_project.models.UserModel;

public class MyProfileActivity extends AppCompatActivity {

    private Intent intent;
    public UserModel curr_user;
    private ImageView user_img;
    private EditText user_name;
    private EditText user_mobile;
    private EditText user_email;
    private EditText user_pass;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFirebaseUserData();

//        intent = getIntent(); // to avoid stale data
//        curr_user = (UserModel) intent.getSerializableExtra("user_details");

        user_img = (ImageView) findViewById(R.id.my_profile_img_view);
        user_name = (EditText) findViewById(R.id.user_name_my_profile_edit_text);
        user_mobile = (EditText) findViewById(R.id.mobile_my_profile_edit_text);
        user_email = (EditText) findViewById(R.id.email_id_my_profile_edit_text);
        user_pass = (EditText) findViewById(R.id.password_my_profile_edit_text);
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

                //        user_img.setImageBitmap();
                user_name.setText(curr_user.getUser_name());

                if (!curr_user.getUser_mobile().equals("0")){
                    user_mobile.setText(curr_user.getUser_mobile());
                }

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
        hMap.put("user_img", " ");

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}