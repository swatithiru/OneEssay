package com.example.oneessay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.example.oneessay.ProfileActivity.REQUEST_IMAGE_CAPTURE;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    DatabaseReference newUserRef;

    private EditText name, password, confirmpassword, email, studentid;
    private Spinner professor;
    private Spinner uni_names;
    private String image;
    private ImageView imageView;
    private String userChoosenTask;
    private static final int SELECT_PICTURE = 0;
    private int photo_captured = 0;
    private Bitmap bitmap;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        professor = (Spinner) findViewById(R.id.spinner);

        uni_names = (Spinner) findViewById(R.id.University);
        imageView = (ImageView) findViewById(R.id.signUpImg);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Professor, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> uni_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.University, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        professor.setAdapter(arrayAdapter);
        professor.setOnItemSelectedListener(this);
        professor.setSelection(0);

        uni_names.setAdapter(uni_adapter);
        uni_names.setOnItemSelectedListener(this);
        uni_names.setSelection(0);

    }

    public void onClickRegister(View view) {

        name = (EditText) findViewById(R.id.Name);
        String uName = name.getText().toString();

        email = (EditText) findViewById(R.id.Email);
        String emailid = email.getText().toString();

        password = (EditText) findViewById(R.id.Password);
        String pass = password.getText().toString();

        confirmpassword = (EditText) findViewById(R.id.ConfirmPassword);
        String confirmPass = confirmpassword.getText().toString();

        studentid = (EditText) findViewById(R.id.StudentID);
        String stuID = studentid.getText().toString();


        Boolean result = validateEmptyFields(uName, emailid, pass, confirmPass, stuID);

        if (result) {

            if (pass.length() > 5) {

                if (password.getText().toString().equals(confirmpassword.getText().toString())) {

                    progressDialog.setMessage("You are Registering .....");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()) {

                                newUserRef = LoginActivity.mRootRef.child("student").child(email.getText().toString().replace("@", "").replace(".", ""));

                                newUserRef.child("email").setValue(email.getText().toString());
                                newUserRef.child("name").setValue(name.getText().toString());
                                newUserRef.child("professor").setValue(professor.getSelectedItem().toString());
                                newUserRef.child("studentid").setValue(studentid.getText().toString());
                                newUserRef.child("university").setValue(uni_names.getSelectedItem().toString());


                                if (photo_captured == 1) {
                                    newUserRef.child("image").setValue("images/" + name.getText().toString() + ".png");
                                    uploadCamera();

                                }
                                else
                                {
                                    newUserRef.child("image").setValue("none");
                                }
                                Toast.makeText(getApplicationContext(), "Registered Succesfully " + email.getText().toString(), Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();
                                Intent in = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(in);

                            } else {
                                Toast.makeText(SignUpActivity.this, "Could not Register Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match !!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Passwords should be atleast 6 characters !!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "The Fields cannot be empty", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validateEmptyFields(String uName, String emailid, String user, String password, String sid) {
        Boolean res = false;
        if (!(uName.isEmpty() || emailid.isEmpty() || user.isEmpty() || password.isEmpty() || sid.isEmpty())) {
            //Toast.makeText(this,"The User Name and Password Fields cannot be empty",Toast.LENGTH_LONG).show();
            res = true;

        }

        return res;
    }

    public void onClickImage(View view) {
        selectImage();

    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Camera")) {
                    userChoosenTask = "Camera";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    //if(result)
                    //cameraIntent();
                    // dispatchTakePictureIntent();
                    //Toast.makeText(ProfileActivity.this,"camera",Toast.LENGTH_SHORT).show();
                } else if (items[item].equals("Gallery")) {
                    userChoosenTask = "Gallery";
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i,
                            "Select Picture"), SELECT_PICTURE);

                    //if(result)
                    //galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bitmap);
                photo_captured = 1;

            } else if (requestCode == SELECT_PICTURE) {
                Uri selectedimageuri = data.getData();
                imageView.setImageURI(selectedimageuri);
                photo_captured = 1;
            }
        }
    }

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private void downloadCamera() {
        storageReference = storage.getReferenceFromUrl("gs://oneessay-66407.appspot.com/").child("images/bran.png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }

    }

    private void uploadCamera() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] mdata = baos.toByteArray();

        String path = "images/" + name.getText().toString() + ".png";

        storageReference = storage.getReference(path);

        UploadTask uploadTask = storageReference.putBytes(mdata);
    }


}
