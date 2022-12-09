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
import java.util.HashMap;

import edu.northeastern.numad22fa_team51_project.adapters.SelectedMembersListAdapter;
import edu.northeastern.numad22fa_team51_project.models.BoardSerializable;
import edu.northeastern.numad22fa_team51_project.models.SelectedMembers;
import edu.northeastern.numad22fa_team51_project.models.TaskSerializableModel;
import edu.northeastern.numad22fa_team51_project.models.UserModel;

public class UpdateCardDetailsActivity extends AppCompatActivity {

    public Dialog progressDialog;
    private Intent intent;
    private TaskSerializableModel passed_task_obj;
    private BoardSerializable passed_board_obj;
    private TaskSerializableModel curr_task_obj;
    private DatabaseReference databaseReference;
    ArrayList<SelectedMembers> selectedMembersArrayList;
    ArrayList<UserModel> passed_user_obj_arr;
    private RecyclerView rv_select_members;
    private EditText card_name;
    private EditText card_notes;
    ArrayList<UserModel> users;
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
        if (intent.hasExtra(Constants.BOARD_OBJ)){
            passed_board_obj = (BoardSerializable) intent.getSerializableExtra(Constants.BOARD_OBJ);
        }
        if (intent.hasExtra(Constants.USERS_OBJ_ARR)){
            passed_user_obj_arr = (ArrayList<UserModel>) intent.getSerializableExtra(Constants.USERS_OBJ_ARR);
        }

        card_name = findViewById(R.id.et_update_card_name);
        card_notes = findViewById(R.id.et_update_card_notes);
        card_due_date = findViewById(R.id.tv_update_due_date);

        fetchCardDataFromFirebase();

        select_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberListDialog();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        setupSelectedMembersList();
    }

    private void fetchCardDataFromFirebase(){
        showProgressDialog("Fetching Card Details");

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
        else if (!card_name.getText().toString().equals(curr_task_obj.getCard_name())){
            hMap.put("card_name", card_name.getText().toString());
        }else{
            hMap.put("card_name", curr_task_obj.getCard_name());
        }

        if (!card_notes.getText().toString().equals(curr_task_obj.getCard_notes())){
            hMap.put("card_notes", card_notes.getText().toString());
        }else{
            hMap.put("card_notes", curr_task_obj.getCard_notes());
        }

        hMap.put("createdBy", passed_board_obj.getGroup_createdBy());

        if (!curr_task_obj.getMemberList().equals(covertArrayListToString(passed_task_obj.getAssignedTo()))){
            hMap.put("memberList", covertArrayListToString(passed_task_obj.getAssignedTo()));
        }else{
            hMap.put("memberList", curr_task_obj.getMemberList());
        }

        if (!curr_task_obj.getDueDate().equals(card_due_date)){
            hMap.put("DueDate", card_due_date.getText().toString());
        }else{
            hMap.put("DueDate", curr_task_obj.getDueDate());
        }

        hMap.put("points", "0");
        hMap.put("isComplete", "false");

        //TODO: add check if user add new members!! and points!!
        // check if user made any changes add date check as well

        if ((hMap.get("card_name").equals(curr_task_obj.getCard_name())) && (hMap.get("card_notes").equals(curr_task_obj.getCard_notes()))
            && (hMap.get("memberList").equals(curr_task_obj.getMemberList()))){
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

    private void memberListDialog(){
        ArrayList<String> taskAssignedMembers = passed_task_obj.getAssignedTo();

        if (taskAssignedMembers.size() > 0){
            for (int i = 0; i < passed_user_obj_arr.size(); i++){
                for (String j : taskAssignedMembers){
                    if (passed_user_obj_arr.get(i).getUser_id().equals(j)){
                        passed_user_obj_arr.get(i).setSelected(true);
                    }
                }
            }
        }else{
            for (int i = 0; i < passed_user_obj_arr.size(); i++){
                passed_user_obj_arr.get(i).setSelected(false);
            }
        }

        memberDialog = new MembersListDialog(UpdateCardDetailsActivity.this, passed_user_obj_arr, "Members List") {
            @Override
            protected void onItemSelected(UserModel user, String action) {
                if(action.equals(Constants.SELECT)){
                    if(!passed_task_obj.getAssignedTo().contains(user.getUser_id())){
                        passed_task_obj.getAssignedTo().add(user.getUser_id());
                    }
                }else{
                    passed_task_obj.getAssignedTo().remove(user.getUser_id());

                    for(int i = 0; i < passed_user_obj_arr.size(); i++){
                        if (passed_user_obj_arr.get(i).getUser_id().equals(user.getUser_id())){
                            passed_user_obj_arr.get(i).setSelected(false);
                        }
                    }
                }
                setupSelectedMembersList();
            }
        };
        memberDialog.show();
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

    public void showProgressDialog(String text){
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.dialog_progress);
        TextView progressTV = (TextView) progressDialog.findViewById(R.id.tv_progress_text);
        progressTV.setText(text);
        progressDialog.show();
    }

    private void setupSelectedMembersList(){
        ArrayList<String> taskAssignedMembers = passed_task_obj.getAssignedTo();
        ArrayList<SelectedMembers> selectedMembersArrayList = new ArrayList<>();

        for (int i = 0; i < passed_user_obj_arr.size(); i++){
            for (String j : taskAssignedMembers){
                if (passed_user_obj_arr.get(i).getUser_id().equals(j)){
                    selectedMembersArrayList.add(new SelectedMembers(
                            passed_user_obj_arr.get(i).getUser_id(),
                            passed_user_obj_arr.get(i).getUser_img()
                    ));
                }
            }
        }

        if (selectedMembersArrayList.size() > 0){
            selectedMembersArrayList.add(new SelectedMembers("", ""));
            select_members.setVisibility(View.GONE);
            rv_select_members.setVisibility(View.VISIBLE);

            rv_select_members.setLayoutManager(new GridLayoutManager(UpdateCardDetailsActivity.this, 6));

            SelectedMembersListAdapter adapter = new SelectedMembersListAdapter(this, selectedMembersArrayList, true);

            rv_select_members.setAdapter(adapter);
            adapter.setOnClickListener(new SelectedMembersListAdapter.onClickListener() {
                @Override
                public void onClick() {
                    memberListDialog();
                }
            });
        }
        else{
            select_members.setVisibility(View.VISIBLE);
            rv_select_members.setVisibility(View.GONE);
        }
    }
}