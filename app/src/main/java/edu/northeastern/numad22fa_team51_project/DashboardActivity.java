package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import edu.northeastern.numad22fa_team51_project.models.UserModel;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout menuDrawer;
    private NavigationView navView;
    private TextView navUserTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    protected UserModel user_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        menuDrawer = (DrawerLayout) findViewById(R.id.profile_details_drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);

        setupCustomActionBar();
        getFirebaseUserData();
        navView.setNavigationItemSelectedListener(this);
    }

    private void getFirebaseUserData(){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_obj = snapshot.getValue(UserModel.class);
                navUserTextView = (TextView) findViewById(R.id.username_nav_header_text_view);
                navUserTextView.setText(user_obj.getUser_name());
                // set profile image here if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void setupCustomActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar_dash_activity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_open_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open drawer
                if (menuDrawer.isDrawerOpen(GravityCompat.START)){
                    menuDrawer.closeDrawer(GravityCompat.START);
                }else{
                    menuDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (menuDrawer.isDrawerOpen(GravityCompat.START)){
            menuDrawer.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }
    }

    // callback to check if user data/name was updated, so as to update the drawer layout
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1002 && requestCode == 1001){
            getFirebaseUserData();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.drawer_my_profile){
            Intent intent = new Intent(DashboardActivity.this, MyProfileActivity.class);
//            intent.putExtra("user_details", user_obj);  //showing stale data if user updates
            startActivityForResult(intent, 1001);
        }
        else if (item.getItemId() == R.id.drawer_sign_out){
            mAuth.signOut();

            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }
        menuDrawer.closeDrawer(GravityCompat.START);

        return false;
    }
}