package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;

import edu.northeastern.numad22fa_team51_project.adapters.TaskListItemAdapter;
import edu.northeastern.numad22fa_team51_project.models.BoardSerializable;
import edu.northeastern.numad22fa_team51_project.models.Task;

public class TaskListActivity extends AppCompatActivity {

    private Intent intent;
    String documentId = "";
    private Dialog progressDialog;
    private DatabaseReference databaseReference;
    RecyclerView taskListRv;
    private String boardDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        intent = getIntent();
        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            documentId = intent.getStringExtra(Constants.DOCUMENT_ID);
        }
        showProgressDialog("Please wait");
        taskListRv = findViewById(R.id.rv_task_list);
        getBoardDetails();
    }

    private void boardDetails(BoardSerializable board){

        boardDetail = board.getDocumentId();

        progressDialog.dismiss();
        setupActionBar(board.getGroup_name());
        Task task1 = new Task("Add List", "");

        board.taskList.add(task1);
        taskListRv.setLayoutManager(new LinearLayoutManager(TaskListActivity.this, LinearLayoutManager.HORIZONTAL, false));
        taskListRv.setHasFixedSize(true);

        TaskListItemAdapter adapter = new TaskListItemAdapter(this, board.taskList);
        taskListRv.setAdapter(adapter);

    }

    private void getBoardDetails(){
        databaseReference = FirebaseDatabase.getInstance().getReference("boards").child(documentId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapShot) {
                // TODO: Get Board Details
                DataSnapshot board_name_snapshot = datasnapShot.child("board_name");
                String board_name = board_name_snapshot.getValue().toString();

                DataSnapshot assignedToSnapShot = datasnapShot.child("group_assignedTo");
                String assignTo = assignedToSnapShot.getValue().toString();
                String[] assignToList = assignTo.split(",");
                ArrayList<String> assignToArrayList = new ArrayList<String>(
                        Arrays.asList(assignToList));

                DataSnapshot group_image_snapshot = datasnapShot.child("group_image");
                String group_image = group_image_snapshot.getValue().toString();

                DataSnapshot group_creadedBy_snapshot = datasnapShot.child("group_createdBy");
                String group_creadedBy = group_creadedBy_snapshot.getValue().toString();

                BoardSerializable group = new BoardSerializable(board_name, group_image, group_creadedBy, assignToArrayList, documentId);
                boardDetails(group);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TaskListActivity.this, "Failed to fetch user data, try again later!", Toast.LENGTH_SHORT).show();
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
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}