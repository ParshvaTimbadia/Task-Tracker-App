package edu.northeastern.numad22fa_team51_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class ShowProgressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout menuDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_progress);

        //setupCustomActionBar();

    }

    private void setupCustomActionBar(){
        toolbar = findViewById(R.id.toolbar_dash_activity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Progress");
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
}