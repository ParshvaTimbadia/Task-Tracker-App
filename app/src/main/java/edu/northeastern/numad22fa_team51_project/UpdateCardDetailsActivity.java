package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.northeastern.numad22fa_team51_project.adapters.SelectedMembersListAdapter;
import edu.northeastern.numad22fa_team51_project.models.SelectedMembers;
import edu.northeastern.numad22fa_team51_project.models.TaskSerializableModel;
import edu.northeastern.numad22fa_team51_project.models.UserModel;

public class UpdateCardDetailsActivity extends AppCompatActivity {

    private Dialog progressDialog;
    private Intent intent;
    private TaskSerializableModel passed_task_obj;
    private TaskSerializableModel curr_task_obj;
    private DatabaseReference databaseReference;
    ArrayList<String> taskAssignedMembersList;
    ArrayList<SelectedMembers> selectedMembersArrayList;
    private RecyclerView rv_select_members;
    private EditText card_name;
    private EditText card_notes;
    ArrayList<UserModel> users;
    // declare type for card members here;
    private TextView card_due_date;
    private TextView select_members;
    Dialog memberDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_card_details);
        setupActionBar();
        users = new ArrayList<>();
        selectedMembersArrayList = new ArrayList<>();
        select_members = findViewById(R.id.tv_update_select_members);
        rv_select_members = findViewById(R.id.rv_selected_members_list);
        intent = getIntent();
        if (intent.hasExtra(Constants.TASK_DETAILS)){
            passed_task_obj = (TaskSerializableModel) intent.getSerializableExtra(Constants.TASK_DETAILS);
        }
        card_name = findViewById(R.id.et_update_card_name);
        card_notes = findViewById(R.id.et_update_card_notes);
        card_due_date = findViewById(R.id.tv_update_due_date);

        showProgressDialog("Please Wait");
        getBoardMembersAssignedDetails();
        fetchCardDataFromFirebase();

        select_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberDialog = new MembersListDialog(UpdateCardDetailsActivity.this, users, "Members List") {
                    @Override
                    protected void onItemSelected(UserModel user, String action) {
                    }
                };
                memberDialog.show();
            }
        });

        setupSelectedMembersList();

    }


    private void fetchCardDataFromFirebase(){
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TASKS).child(passed_task_obj.getBoard_id()).child(passed_task_obj.getCard_id());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curr_task_obj = snapshot.getValue(TaskSerializableModel.class);

                card_name.setText(curr_task_obj.getCard_name());

                if ((curr_task_obj.getCard_notes() != null) || (!curr_task_obj.getCard_notes().equals(""))){
                    card_notes.setText(curr_task_obj.getCard_notes());
                }

                if ((curr_task_obj.getDueDate() != null) || (!curr_task_obj.getDueDate().equals(""))){
                    card_due_date.setText(curr_task_obj.getDueDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateCardDetailsActivity.this, "Failed to fetch task data, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.dismiss();
    }

    public void updateFirebaseTaskData(View view){

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TASKS).child(passed_task_obj.getBoard_id()).child(passed_task_obj.getCard_id());

        HashMap<String, String> hMap = new HashMap<>();
        hMap.put("board_id", passed_task_obj.getBoard_id());

        if (card_name.getText().toString().isEmpty()){
            Toast.makeText(UpdateCardDetailsActivity.this, "Card name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!card_name.getText().toString().equals(passed_task_obj.getCard_name())){
            hMap.put("card_name", card_name.getText().toString());
        }else{
            hMap.put("card_name", passed_task_obj.getCard_name());
        }

        if (!card_notes.getText().toString().equals(passed_task_obj.getCard_notes())){
            hMap.put("card_notes", card_notes.getText().toString());
        }else{
            hMap.put("card_notes", passed_task_obj.getCard_notes());
        }

        hMap.put("createdBy", passed_task_obj.getCreatedBy());
        hMap.put("memberList", String.valueOf(passed_task_obj.getAssignedTo()));

        if (!passed_task_obj.getDueDate().equals(card_due_date)){
            hMap.put("DueDate", card_due_date.getText().toString());
        }else{
            hMap.put("DueDate", passed_task_obj.getDueDate());
        }

        hMap.put("points", "0");
        hMap.put("isComplete", "false");

        //TODO: add check if user add new members!! and points!!
        // check if user made any changes

        if ((hMap.get("card_name").equals(passed_task_obj.getCard_name())) && (hMap.get("card_notes").equals(passed_task_obj.getCard_notes()))){
            Toast.makeText(UpdateCardDetailsActivity.this, "No Changes made", Toast.LENGTH_SHORT).show();
        }else{
            databaseReference.setValue(hMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                // callback may be needed
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(UpdateCardDetailsActivity.this, "Card Data Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }


    private void populateMembersListUI(String userUid, Boolean value){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapShot) {
                String user_email = datasnapShot.child("user_email").getValue().toString();
                String user_id = datasnapShot.child("user_id").getValue().toString();
                String user_img = datasnapShot.child("user_img").getValue().toString();
                String user_mobile = datasnapShot.child("user_mobile").getValue().toString();
                String user_name = datasnapShot.child("user_name").getValue().toString();
                String user_passwd = datasnapShot.child("user_passwd").getValue().toString();
                UserModel user = new UserModel(user_email, user_id, user_name, user_passwd, user_img, user_mobile);
                user.setSelected(value);
                users.add(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateCardDetailsActivity.this, "Failed to fetch user data, try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void membersListDialog(ArrayList<String> groupMembers){
        ArrayList<String> taskAssignedMembers = passed_task_obj.getAssignedTo();
        Log.d("taskAssignedMembers", taskAssignedMembers.toString());

        if (taskAssignedMembers.size() > 0){
            for (String i : groupMembers){
                for (String j : taskAssignedMembers){
                    if (i.equals(j)){
                        populateMembersListUI(i, true);
                    }
                }
            }
        }else{
            for (String i : groupMembers) {
                populateMembersListUI(i, false);
            }
        }
    }


    public void getBoardMembersAssignedDetails(){
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.BOARDS).child(passed_task_obj.getBoard_id());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapShot) {
                DataSnapshot assignedToSnapShot = datasnapShot.child("group_assignedTo");
                String assignTo = assignedToSnapShot.getValue().toString();
                String[] assignToList = assignTo.split(",");
                ArrayList<String> assignToArrayList = new ArrayList<String>(
                        Arrays.asList(assignToList));

                membersListDialog(assignToArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateCardDetailsActivity.this, "Failed to fetch user data, try again later!", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private ArrayList<String> convertStringToArrayList(String list){
        ArrayList<String> newList = new ArrayList<>();
        return newList;
    }

    @NonNull
    private String covertArrayListToString(ArrayList<String> list) {
        String StringList = "";
        for (int i = 0; i < list.size(); i++) {
            StringList += list.get(i) + ",";
        }
        return StringList;
    }



    private void setupActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_card_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Update Card Details");
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

    private void setupSelectedMembersList(){
        ArrayList<String> taskAssignedMembers = passed_task_obj.getAssignedTo();
        for (int i = 0; i < users.size(); i++){
            Log.d("setupSelectedMembersList-users", users.get(i).toString());
        }
    }
}