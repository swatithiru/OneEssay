package com.example.oneessay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    final static int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 3;
    public static boolean gallery = false;
    private static final int SELECT_PICTURE = 0;

    private Button continuebutton;
    public static String photoPath = "asd";
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    // val REQUEST_IMAGE_CAPTURE = 1;

    public static File currentImageFile;
    private ImageView camerabutton;
    private String userChoosenTask;
    private Student student;
    private Bitmap bitmap;
    private TextView name;
    private TextView university;
    private TextView subject;
    private TextView professor;
    private TextView email;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        displayProfile();

    }

    private void displayProfile() {
        name = (TextView) findViewById(R.id.Name);
        university = (TextView) findViewById(R.id.Univ);
        email = (TextView) findViewById(R.id.email);
        subject = (TextView) findViewById(R.id.Subject);
        professor = (TextView) findViewById(R.id.professor);
        camerabutton = (ImageView) findViewById(R.id.profileimage);
        camerabutton.setEnabled(false);

            LoginActivity.mRootRef.child("student").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                    while (iterator.hasNext()) {
                        DataSnapshot s = iterator.next();
                        student = s.getValue(Student.class);
                        if(LoginActivity.currentUser != null)
                            if (LoginActivity.currentUser.getEmail().equals(student.getEmail()))
                                break;
                    }
                    if(LoginActivity.currentUser != null) {
                        if (student.getImage().equalsIgnoreCase("none")) {
                            // do nothing
                        } else {
                            downloadCamera();
                        }
                        name.setText(student.getName());
                        university.setText(student.getUniversity());
                        email.setText(student.getEmail());
                        subject.setText("English");
                        professor.setText(student.getProfessor());
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private void downloadCamera() {
        storageReference = storage.getReferenceFromUrl("gs://oneessay-66407.appspot.com/").child(student.getImage());
        try {
            final File localFile = File.createTempFile("images", "png");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    camerabutton.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }

    }
}
