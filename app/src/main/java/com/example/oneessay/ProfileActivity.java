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
        //continuebutton= (Button) view.findViewById(R.id.continuebutton);
        /*camerabutton = (ImageView) findViewById(R.id.profileimage);
        camerabutton.setEnabled(true);
        camerabutton.setVisibility(View.VISIBLE);
        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
//                   DialogFragment newFragment = new ImageOptionsLearner();
//                   newFragment.show(getActivity().getSupportFragmentManager(), "images");
            }
        });*/

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
                    if (LoginActivity.currentUser.getEmail().equals(student.getEmail()))
                        break;
                }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                // camerabutton.setImageBitmap(bitmap);
                uploadCamera();
                downloadCamera();

            } else if (requestCode == SELECT_PICTURE) {
                Uri selectedimageuri = data.getData();
                camerabutton.setImageURI(selectedimageuri);
            }
        }
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

    private void uploadCamera() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] mdata = baos.toByteArray();

        String path = "images/" + name.getText().toString() + ".png";

        storageReference = storage.getReference(path);

        UploadTask uploadTask = storageReference.putBytes(mdata);
    }

    public static String getRealPathFromDocumentUri(Context context, Uri uri) {
        String filePath = "";

        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {
            return filePath;
        }
        String imgId = m.group();

        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{imgId}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        gallery = true;
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                    //dispatchTakePictureIntent();
                    else if (userChoosenTask.equals("Gallery")) {
                        userChoosenTask = "Gallery";
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        startActivityForResult(Intent.createChooser(i,
                                "Select Picture"), SELECT_PICTURE);
                    }
                    //galleryIntent();
                } else {
                }
                break;
        }
    }


    private File createImageFile() {

        File imagev = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp;
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            imagev = File.createTempFile(imageFileName, ".jpg", storageDir);


            photoPath = imagev.getAbsolutePath();
            return imagev;
        } catch (Exception e) {

        }
        return imagev;
    }

}
