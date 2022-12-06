package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CreateTaskCardActivity extends AppCompatActivity {

    private Dialog progressDialog;

    public Intent intent;
    public String documentId = "";

    private Toolbar toolbar;
    public TextView et_card_name;
    public TextView et_card_notes;
    public Button btn_pick_date;
    public TextView tv_due_date;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_card);

        setupActionBar();

        intent = getIntent();
        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            documentId = intent.getStringExtra(Constants.DOCUMENT_ID);
        }

        et_card_name = findViewById(R.id.et_card_name);
        et_card_notes = findViewById(R.id.et_card_notes);
        btn_pick_date = findViewById(R.id.btn_pick_date);
        tv_due_date = findViewById(R.id.tv_due_date);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btn_pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dpl = new DatePickerDialog(CreateTaskCardActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        tv_due_date.setText(month_str  + "-" + date_str  + "-" + year_str);
                    }
                },year, month, day);
                dpl.show();
            }
        });
    }


    public void addTaskCardToBoard(View view){
        // Add more field checks here if needed
        Log.d("CreateTask", tv_due_date.getText().toString());
        if(et_card_name.getText() == null || et_card_name.getText().toString().equals("")){
            Toast.makeText(CreateTaskCardActivity.this, "Please enter a name for the task", Toast.LENGTH_SHORT).show();
        }else if (tv_due_date.getText() == null || tv_due_date.getText().toString().equals("")){
            Toast.makeText(CreateTaskCardActivity.this, "Please enter a due date for the task", Toast.LENGTH_SHORT).show();
        } else{
            showProgressDialog("Adding Card");
            createCard();
        }
    }

    public void createCard(){

        HashMap<String, String> hMap = new HashMap<>();
        hMap.put("board_id", documentId);
        hMap.put("card_name", et_card_name.getText().toString());
        hMap.put("card_notes", et_card_notes.getText().toString());
        hMap.put("DueDate", tv_due_date.getText().toString());

        databaseReference.child(Constants.TASKS).child(documentId).push().setValue(hMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateTaskCardActivity.this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
                        cardCreatedSuccessfully();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateTaskCardActivity.this, "Unable to add task at this moment, please try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void cardCreatedSuccessfully(){
        progressDialog.dismiss();
        finish();
    }

    private void setupActionBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_create_card_activity);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
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
}