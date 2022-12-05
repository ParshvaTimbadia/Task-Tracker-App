package edu.northeastern.numad22fa_team51_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

public class Constants {

    // Including Firebase constants.
    static final String BOARDS = "boards";
    static final String NAME = "name";
    static final String DOCUMENT_ID = "documentId";
    static final String ASSINGED_TO = "assignedTo";
    static final int STORAGE_PERMISSIONS = 1;
    static final int PICK_IMAGE_REQUEST_CODE = 2;
    static Boolean refersh = true;


    // Opens an activity to choose an image
    public static void showImageChooser(@NonNull Activity activity){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE);
    }

    // Fetch the image extension in our case
    public static String getFileExtension(@NonNull Activity activity, Uri uri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(
                activity.getContentResolver().getType(uri));
    }

}
