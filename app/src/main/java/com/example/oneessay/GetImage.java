package com.example.oneessay;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class GetImage extends AsyncTask<String,Void, Bitmap> {
    Activity activity = new Activity();
    ImageView imageView;

    StorageReference storageReference;
    public GetImage(Activity activity)
    {
        this.activity=activity;
    }

    FirebaseStorage storage;

    Bitmap bitmap;
    File localFile;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        imageView = (ImageView) activity.findViewById(R.id.profileimage);
        storage = FirebaseStorage.getInstance();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        storageReference = storage.getReferenceFromUrl("gs://oneessay-66407.appspot.com").child(strings[0]);

        try {
            localFile = File.createTempFile("images", "png");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    imageView.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
            return bitmap;
        } catch (IOException e) {
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
