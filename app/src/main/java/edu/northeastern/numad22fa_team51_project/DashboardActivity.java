package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import edu.northeastern.numad22fa_team51_project.adapters.GroupItemsAdapter;
import edu.northeastern.numad22fa_team51_project.models.BoardSerializable;
import edu.northeastern.numad22fa_team51_project.models.UserModel;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout menuDrawer;
    private NavigationView navView;
    private TextView navUserTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    protected UserModel user_obj;
    private RecyclerView groupListRV;
    private TextView rvtextVeiw;
    private FloatingActionButton createBoard;
    private GroupItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        menuDrawer = (DrawerLayout) findViewById(R.id.profile_details_drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        groupListRV = findViewById(R.id.rv_boards_list);
        rvtextVeiw = findViewById(R.id.tv_no_boards_available);
        setupCustomActionBar();
        getFirebaseUserData();
        navView.setNavigationItemSelectedListener(this);


        createBoard = findViewById(R.id.create_board);
        createBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFirebaseUserData();
                Intent intent = new Intent(DashboardActivity.this, CreateBoardActivity.class);
                intent.putExtra(Constants.NAME, user_obj.getUser_id());
                startActivity(intent);
            }
        });

        if (Constants.refersh) {
            Log.d("Constant-Refresh", Constants.refersh.toString());
            getGroupsList();
            Constants.refersh = Boolean.FALSE;
        }
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


    private void populateGroupsListToUI(ArrayList<BoardSerializable> groupList){
        if (groupList.size() > 0){
            groupListRV.setVisibility(View.VISIBLE);
            rvtextVeiw.setVisibility(View.GONE);
            groupListRV.setLayoutManager(new LinearLayoutManager(this));
            groupListRV.setHasFixedSize(true);
            adapter = new GroupItemsAdapter(this, groupList);
            groupListRV.setAdapter(adapter);
            adapter.setOnClickListener(new GroupItemsAdapter.onClickListener() {
                @Override
                public void onClick(int position, BoardSerializable model) {
                    Intent intent = new Intent(DashboardActivity.this, TaskListActivity.class);
                    intent.putExtra(Constants.DOCUMENT_ID, model.getDocumentId());
                    startActivity(intent);
                }
            });
        }else{
            groupListRV.setVisibility(View.GONE);
            rvtextVeiw.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void getGroupsList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("boards");
        ArrayList<BoardSerializable> groupList = new ArrayList<>();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Use the values to update the UI
                for (DataSnapshot datasnapShot : snapshot.getChildren()) {

                    String documentId = datasnapShot.getKey();

                    DataSnapshot board_name_snapshot = datasnapShot.child("board_name");
                    String board_name = board_name_snapshot.getValue().toString();

                    DataSnapshot assignedToSnapShot = datasnapShot.child("group_assingedTo");
                    String assignTo = assignedToSnapShot.getValue().toString();
                    String[] assignToList = assignTo.split(",");
                    ArrayList<String> assignToArrayList = new ArrayList<String>(
                            Arrays.asList(assignToList));


                    DataSnapshot group_image_snapshot = datasnapShot.child("group_image");
                    String group_image = group_image_snapshot.getValue().toString();


                    DataSnapshot group_creadedBy_snapshot = datasnapShot.child("group_creadedBy");
                    String group_creadedBy = group_creadedBy_snapshot.getValue().toString();

                    BoardSerializable group = new BoardSerializable(board_name, group_image, group_creadedBy, assignToArrayList, documentId);

                    if (assignToArrayList.contains(firebaseUser.getUid())) {
                        groupList.add(group);
                    }
                }
                populateGroupsListToUI(groupList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DashboardActivity", "Error while creating the board", databaseError.toException());
            }
        };

        databaseReference.addValueEventListener(postListener);
    }


}