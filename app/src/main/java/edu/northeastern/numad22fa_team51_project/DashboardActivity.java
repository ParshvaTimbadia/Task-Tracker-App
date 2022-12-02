package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout menuDrawer;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private FloatingActionButton createBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        menuDrawer = (DrawerLayout) findViewById(R.id.profile_details_drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);

        setupCustomActionBar();
        navView.setNavigationItemSelectedListener(this);


        createBoard = findViewById(R.id.create_board);
        createBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, CreateBoardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupCustomActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar_dash_activity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("User Dashboard");
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.drawer_my_profile){
            //TODO
            Toast.makeText(DashboardActivity.this, "My Profile Clicked", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.drawer_sign_out){
            mAuth = FirebaseAuth.getInstance();
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