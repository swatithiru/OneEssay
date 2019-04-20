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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    final static int REQUEST_IMAGE_CAPTURE  =1;
    static final int REQUEST_IMAGE_PICK = 3;
    public static boolean gallery=false;
    private static final int SELECT_PICTURE = 0;

    private Button continuebutton;
    public static  String photoPath="asd";
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    // val REQUEST_IMAGE_CAPTURE = 1;

    public static File currentImageFile;
    private ImageView camerabutton;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //continuebutton= (Button) view.findViewById(R.id.continuebutton);
        camerabutton = (ImageView) findViewById(R.id.profileimage);
        camerabutton.setEnabled(true);
        camerabutton.setVisibility(View.VISIBLE);
        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
//                   DialogFragment newFragment = new ImageOptionsLearner();
//                   newFragment.show(getActivity().getSupportFragmentManager(), "images");
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode,resultCode,data);
       if(resultCode==RESULT_OK)
       {
           if(requestCode==REQUEST_IMAGE_CAPTURE)
           {
               Bundle bundle=data.getExtras();
               final Bitmap bitmap=(Bitmap)bundle.get("data");
               camerabutton.setImageBitmap(bitmap);
           }
           else  if(requestCode==SELECT_PICTURE)
           {
               Uri selectedimageuri= data.getData();
               camerabutton.setImageURI(selectedimageuri);
                                       }
       }
    }

    public static String getRealPathFromDocumentUri(Context context, Uri uri){
        String filePath = "";

        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {
            return filePath;
        }
        String imgId = m.group();

        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ imgId }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }


    private void selectImage() {
        final CharSequence[] items = { "Camera", "Gallery",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Camera")) {
                    userChoosenTask="Camera";
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                    //if(result)
                    //cameraIntent();
                   // dispatchTakePictureIntent();
                    //Toast.makeText(ProfileActivity.this,"camera",Toast.LENGTH_SHORT).show();
                }
                else if (items[item].equals("Gallery")) {
                    userChoosenTask="Gallery";
                    Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i,
                            "Select Picture"), SELECT_PICTURE);

                    //if(result)
                    //galleryIntent();
                }
                else if(items[item].equals("Cancel"))
                {
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
        gallery=true;
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Camera"))
                    {
                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                    }
                        //dispatchTakePictureIntent();
                    else if(userChoosenTask.equals("Gallery"))

                    {
                        userChoosenTask="Gallery";
                        Intent i=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

//        if (intent.resolveActivity(getPackageManager()) != null) {
//
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (Exception ex) {
//                // Error occurred while creating the File
//            }
//            if (photoFile != null) {
//
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE  );
//            }
//            else
//            {
//                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
//            }
//        }
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
