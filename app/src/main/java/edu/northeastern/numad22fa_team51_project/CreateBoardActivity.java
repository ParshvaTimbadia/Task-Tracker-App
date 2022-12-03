package edu.northeastern.numad22fa_team51_project;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;

public class CreateBoardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Uri selectedImageUri = null;
    private CircleImageView iv_board_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        setupActionBar();
        iv_board_image = findViewById(R.id.iv_board_image);

        iv_board_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractedCheckSeldPermission();
            }
        });

    }

    private void extractedCheckSeldPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Constants.showImageChooser(CreateBoardActivity.this);
        }else{
            ActivityCompat.requestPermissions(CreateBoardActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.STORAGE_PERMISSIONS);
        }
    }

    private void setupActionBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_create_board_activity);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.STORAGE_PERMISSIONS){
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this);
            }else{
                Toast.makeText(this, "You denied permission for storage, change from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data != null){
        selectedImageUri = data.getData();

        try{
            Glide.with(this)
                    .load(Uri.parse((selectedImageUri.toString())))
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_board_image);
        }catch (Exception e){
            e.printStackTrace();
        }

        }
    }

}