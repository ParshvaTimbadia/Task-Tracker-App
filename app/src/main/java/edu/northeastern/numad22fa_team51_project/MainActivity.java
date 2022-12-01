package edu.northeastern.numad22fa_team51_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private long lastClickTime = 0;
    private EditText email_edit_text;
    private EditText password_edit_text;
    private Button sign_in_button;
    private Button sign_up_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //private void setupActionBar(){} //if more features needed
        getSupportActionBar().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        email_edit_text = findViewById(R.id.email_address_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        sign_in_button = findViewById(R.id.sign_in_button);
        sign_up_button = findViewById(R.id.sign_up_button);
    }

    public void signInFlow(View view){

        // prevent redundant network calls being executed multiple times if button is pressed more than once within 1 sec
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        String user = email_edit_text.getText().toString().trim();
        String passwd = password_edit_text.getText().toString().trim();

        if (user.isEmpty()){
            email_edit_text.setError("Email cannot be empty");
        }
        else if (passwd.isEmpty()){
            password_edit_text.setError("Password cannot be empty");
        }
        else{
            mAuth.signInWithEmailAndPassword(user,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Login Failed. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void signUpFlow(View view){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}