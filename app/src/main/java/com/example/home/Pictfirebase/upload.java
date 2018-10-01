package com.example.home.Pictfirebase;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class upload extends AppCompatActivity {

    EditText mtitleet,mdescriptionet;
    ImageView mimageup;
    Button muploadbtn;
    String mFolderPath = "Image_Uploads/";
    String mDatabasePath = "data";
    Uri mFilePathUri;
    StorageReference mstorageReference;
    DatabaseReference mdatabaseReference;
    ProgressDialog mprogressDialog;
    int IMAGE_REQUEST_CODE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mtitleet = (EditText)findViewById(R.id.uptitle);
        mdescriptionet = (EditText)findViewById(R.id.updesc);
        mimageup = (ImageView) findViewById(R.id.upimage);
        muploadbtn = (Button) findViewById(R.id.upbtn);
        mimageup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Image"),IMAGE_REQUEST_CODE);

            }
        });
        muploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  uploadDataToFirebase();
            }
        });
        mstorageReference = FirebaseStorage.getInstance().getReference();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference(mDatabasePath);
        mprogressDialog = new ProgressDialog(upload.this);
    }
    private void uploadDataToFirebase()
    {
        if(mFilePathUri != null)
        {
            mprogressDialog.setTitle("Image is Uploading ...");
            mprogressDialog.show();
            StorageReference storageReference2nd = mstorageReference.child(mFolderPath+ System.currentTimeMillis()+"."+getFileExtension(mFilePathUri));
            storageReference2nd.putFile(mFilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String mPosttitle = mtitleet.getText().toString().trim();
                            String mPostDescr = mdescriptionet.getText().toString().trim();
                            mprogressDialog.dismiss();
                            Toast.makeText(upload.this,"Image Uploaded ....",Toast.LENGTH_LONG).show();
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(mPosttitle,mPostDescr,taskSnapshot.getDownloadUrl().toString());
                            String imageUploadId = mdatabaseReference.push().getKey();
                            mdatabaseReference.child(imageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(upload.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            mprogressDialog.setTitle("Image is uploading");
                        }
                    });
        }
        else
        {
            Toast.makeText(this,"Please select imge or add image name",Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            mFilePathUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mFilePathUri);
                mimageup.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
