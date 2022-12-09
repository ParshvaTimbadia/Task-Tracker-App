package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import edu.northeastern.numad22fa_team51_project.adapters.TaskListItemsAdapter;
import edu.northeastern.numad22fa_team51_project.models.BoardSerializable;
import edu.northeastern.numad22fa_team51_project.models.TaskSerializableModel;
import edu.northeastern.numad22fa_team51_project.models.UserModel;

public class TaskListActivity extends AppCompatActivity {

    private Intent intent;
    String documentId = "";
    private Dialog progressDialog;
    TaskListItemsAdapter taskAdapter;
    private DatabaseReference databaseReference;
    private String boardDetail;
    private FloatingActionButton createTaskCard;
    RecyclerView taskCardRcw;
    View parentLayout;
    ArrayList<TaskSerializableModel> arrTaskCards;
    TextView cardRcwText;
    private ArrayList<UserModel> assignedMembersDetailList;
    public BoardSerializable group;
    public HashSet user_lookup_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        intent = getIntent();
        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            documentId = intent.getStringExtra(Constants.DOCUMENT_ID);
        }
        showProgressDialog("Please wait");

        taskCardRcw = findViewById(R.id.rv_task_list);
        taskCardRcw.setLayoutManager(new LinearLayoutManager(this));
        parentLayout = findViewById(R.id.card_create_root_layout);
        cardRcwText = findViewById(R.id.tv_no_tasks_available);


        //TODO: delete on swipe etc, to be decided
//        new ItemTouchHelper(ith).attachToRecyclerView(taskCardRcw);

        createTaskCard = findViewById(R.id.create_task_card);

        getBoardDetails();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getBoardUserDetailsForLookup();

        populateRecyclerViewWithTaskCards();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == 2110 && requestCode == 2111){
//            getBoardUserDetailsForLookup();
//        }
//    }

    public void populateRecyclerViewWithTaskCards(){
        arrTaskCards = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TASKS);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                snapshot = snapshot.child(documentId);
                String board_id = documentId;
                for (DataSnapshot datasnapShot : snapshot.getChildren()) {

                    String card_id = datasnapShot.getRef().getKey();
                    String card_name = (String) datasnapShot.child("card_name").getValue();
                    String card_notes = (String) datasnapShot.child("card_notes").getValue();
                    String createdBy = (String) datasnapShot.child("createdBy").getValue();
                    String memberList = (String) datasnapShot.child("memberList").getValue();
                    ArrayList<String> assignedTo = convertStringToArrayList(memberList);
                    String DueDate = (String) datasnapShot.child("DueDate").getValue();
                    String points = (String) datasnapShot.child("points").getValue();
                    String isComplete = (String) datasnapShot.child("isComplete").getValue();

                    TaskSerializableModel task = new TaskSerializableModel(card_id, board_id, card_name, card_notes, createdBy, assignedTo, memberList, DueDate, points, isComplete);
                    arrTaskCards.add(task);
                }

                if (arrTaskCards.size() > 0){
                    cardRcwText.setVisibility(View.GONE);
                    taskAdapter = new TaskListItemsAdapter(TaskListActivity.this, arrTaskCards, group, assignedMembersDetailList);
                    taskCardRcw.setAdapter(taskAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TaskListActivity.this, "Failed to fetch task details, try again later!", Toast.LENGTH_SHORT).show();
            }
        };

        databaseReference.addValueEventListener(postListener);
        }

    public void addCardToBoard(View view){
        Intent intent = new Intent(TaskListActivity.this, CreateTaskCardActivity.class);
        intent.putExtra(Constants.DOCUMENT_ID, documentId);
        startActivity(intent);
    }

    private void fetchBoardDetails(BoardSerializable board){
        boardDetail = board.getDocumentId();

        progressDialog.dismiss();
        setupActionBar(board.getGroup_name());
    }

    private void getBoardUserDetailsForLookup(){
        assignedMembersDetailList = new ArrayList<>();
        user_lookup_ids = new HashSet();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.BOARDS).child(documentId).child("group_assignedTo");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String assignTo = snapshot.getValue().toString();
                String[] assignToList = assignTo.split(",");
                ArrayList<String> assignToArrayList = new ArrayList<String>(Arrays.asList(assignToList));

                for (String i: assignToArrayList){
                    user_lookup_ids.add(i);
                }

                getBoardUserObjects(user_lookup_ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskListActivity.this, "Failed to get member user details, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBoardUserObjects(HashSet user_lookup_ids) {

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USERS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapShot : snapshot.getChildren()){

                    if (user_lookup_ids.contains(datasnapShot.getKey())){
                        UserModel curr_user = datasnapShot.getValue(UserModel.class);

                        assignedMembersDetailList.add(curr_user);
                    }
                }
                populateRecyclerViewWithTaskCards();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskListActivity.this, "Failed to get member user details, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBoardDetails(){
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.BOARDS).child(documentId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapShot) {

                DataSnapshot board_name_snapshot = datasnapShot.child("board_name");
                String board_name = board_name_snapshot.getValue().toString();

                DataSnapshot assignedToSnapShot = datasnapShot.child("group_assignedTo");
                String assignTo = assignedToSnapShot.getValue().toString();
                String[] assignToList = assignTo.split(",");
                ArrayList<String> assignToArrayList = new ArrayList<String>(Arrays.asList(assignToList));

                DataSnapshot group_image_snapshot = datasnapShot.child("group_image");
                String group_image = group_image_snapshot.getValue().toString();

                DataSnapshot group_creadedBy_snapshot = datasnapShot.child("group_createdBy");
                String group_creadedBy = group_creadedBy_snapshot.getValue().toString();

                group = new BoardSerializable(board_name, group_image, group_creadedBy, assignToArrayList, documentId);
                fetchBoardDetails(group);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskListActivity.this, "Failed to fetch board details, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActionBar(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_task_list_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showProgressDialog(String text){
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.dialog_progress);
        TextView progressTV = (TextView) progressDialog.findViewById(R.id.tv_progress_text);
        progressTV.setText(text);
        progressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_members, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_members){
            Intent intent = new Intent(TaskListActivity.this, MembersActivity.class);
            intent.putExtra(Constants.BOARD_DETAILS, boardDetail);
            startActivityForResult(intent, 2111);
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> convertStringToArrayList(String list){

        String[] assignToList = list.split(",");
        ArrayList<String> assignToArrayList = new ArrayList<String>(Arrays.asList(assignToList));
        return assignToArrayList;
    }
}