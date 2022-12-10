package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
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
    public Button btn_update_pick_date;
    public Button btn_update_clear_date;
    public CheckBox chk_box_task_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_update_card_details);
        setupActionBar();
        users = new ArrayList<>();
        selectedMembersArrayList = new ArrayList<>();
        select_members = findViewById(R.id.tv_update_select_members);
        rv_select_members = findViewById(R.id.rv_selected_members_list);
        btn_update_pick_date = findViewById(R.id.btn_update_pick_date);
        btn_update_clear_date = findViewById(R.id.btn_update_clear_date);

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
        chk_box_task_complete = findViewById(R.id.chk_box_task_complete);

        fetchCardDataFromFirebase();

        select_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberListDialog();
            }
        });

        btn_update_clear_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card_due_date.setText("");
                card_due_date.setHint(R.string.due_date);
            }
        });

        btn_update_pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dpl = new DatePickerDialog(UpdateCardDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String month_str, date_str, year_str;

                        if ((i1 + 1) < 10){
                            month_str = '0' + String.valueOf(i1 + 1);
                        }else{
                            month_str = String.valueOf(i1 + 1);
                        }

                        if ((i2) < 10){
                            date_str = '0' + String.valueOf(i2);
                        }else{
                            date_str = String.valueOf(i2);
                        }

                        year_str = String.valueOf(i);

                        String selected_date = month_str  + "-" + date_str  + "-" + year_str;

                        Date input = new Date(i, i1, i2);
                        Date today = new Date(year, month, day);

                        if (input.equals(today) || input.after(today)){
                            card_due_date.setText(selected_date);
                        }else{
                            Toast.makeText(UpdateCardDetailsActivity.this, "Due date cannot be in the past", Toast.LENGTH_SHORT).show();
                        }
                    }
                },year, month, day);
                dpl.show();
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

                if ((curr_task_obj.getIsComplete().equals(Constants.TRUE))){
                    chk_box_task_complete.setChecked(true);
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

        String tempData = covertArrayListToString(passed_task_obj.getAssignedTo());
        if (tempData.equals(",")){
            tempData = "";
        }

        if (!curr_task_obj.getMemberList().equals(tempData)){
            hMap.put("memberList", tempData);
        }else{
            hMap.put("memberList", curr_task_obj.getMemberList());
        }

        if (!curr_task_obj.getDueDate().equals(card_due_date.getText().toString())){
            hMap.put("DueDate", card_due_date.getText().toString());
        }else{
            hMap.put("DueDate", curr_task_obj.getDueDate());
        }

        if (!curr_task_obj.getIsComplete().equals(String.valueOf(chk_box_task_complete.isChecked()))){
            hMap.put("isComplete", String.valueOf(chk_box_task_complete.isChecked()));
        }else{
            hMap.put("isComplete", curr_task_obj.getIsComplete());
        }

        hMap.put("points", "0");


        //TODO: add check if user adds any points!!
        // check if user made any changes

        if ((hMap.get("card_name").equals(curr_task_obj.getCard_name())) && (hMap.get("card_notes").equals(curr_task_obj.getCard_notes()))
            && (hMap.get("memberList").equals(curr_task_obj.getMemberList())) && (hMap.get("DueDate").equals(curr_task_obj.getDueDate()))
            && (hMap.get("isComplete").equals(curr_task_obj.getIsComplete()))){
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