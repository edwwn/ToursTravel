package com.group.tourstravel;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Gallery extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private TextView mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mButtonChooseImage = findViewById(R.id.button_choose_file);
        mButtonUpload = findViewById(R.id.button_Upload_Image);
        mTextViewShowUploads =findViewById(R.id.Text_View_ShowUploads);
        mEditTextFileName = findViewById(R.id.EditText_File_Name);
        mImageView =findViewById(R.id.Image_View_Gallery);
        mProgressBar =findViewById(R.id.Progress_bar_ImageUpload);

        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();

            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask !=null && mUploadTask.isInProgress()){
                    Toast.makeText(Gallery.this, "Upload in Progress", Toast.LENGTH_LONG).show();
                }else {

            upLoadFile();
            }}
        });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryView();
            }
        });
    }
    private void openGalleryView() {
        Intent intent = new Intent(Gallery.this,GalleryCheckView.class);
        startActivity(intent);
    }

    private String getFileExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void upLoadFile() {
        if (mImageUri !=null){

            StorageReference fileReference =mStorageRef.child(System.currentTimeMillis()
            +"." + getFileExtension(mImageUri));

            mUploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           // mProgressBar.setProgress(0); user wont see this when progress is 100
                            // so we user a handler

                            Handler handler =new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);

                                }
                            }, 5000); //delay in seconds after upload is successful

                            Toast.makeText(Gallery.this, "Upload Successful", Toast.LENGTH_LONG).show();

                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());


                                String uploadId =mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Gallery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());

                            mProgressBar.setProgress((int)progress);
                            
                        }
                    });

        }else {
            Toast.makeText(Gallery.this, "No File Selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("Image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data != null &&
                data.getData()!=null){

            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);

        }

    }
}
