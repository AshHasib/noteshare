package com.example.hasib.noteshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasib.noteshare.model.Pdf;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPdfActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private CardView btnSelectPdf,btnUploadPdf;
    private Uri pdfUri;
    public TextView nameOfPdf,progress;
    private UserSessionManager sessionManager;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);


        toolbar=findViewById(R.id.uploadPdfToolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Upload PDF");
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameOfPdf=findViewById(R.id.nameOfPdf);
        progress=findViewById(R.id.progressPdfUpload);
        progressBar=findViewById(R.id.progressPdfUploadP);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFF, android.graphics.PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.INVISIBLE);


        btnSelectPdf=findViewById(R.id.btnSelectPdf);
        btnSelectPdf.setOnClickListener(v->{
            if(ContextCompat.checkSelfPermission(UploadPdfActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    ==PackageManager.PERMISSION_GRANTED) {

                selectPdf();

            }
            else {
                ActivityCompat.requestPermissions(UploadPdfActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        9);
            }
        });

        btnUploadPdf=findViewById(R.id.btnUploadPdf);
        btnUploadPdf.setOnClickListener(v->{
            uploadPdf();
        });


    }

    public void uploadPdf() {
        if(pdfUri!=null) {
            progress.setText("Progress : Uploading");
            progressBar.setVisibility(View.VISIBLE);
            String fileName=getFileName(pdfUri);
            StorageReference pdfReference=FirebaseStorage.getInstance().getReference("PDF").child(fileName);
            pdfReference.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.setText("Progress : Uploading");
                    String url=taskSnapshot.getDownloadUrl().toString();
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("PDF").push();
                    reference.setValue(new Pdf(fileName,url));
                    progress.setText("Progress : Successful");
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UploadPdfActivity.this,"File Uploaded Successfully",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
        else {
            Toast.makeText(this,"Please select a file", Toast.LENGTH_LONG).show();
        }
    }



    public void selectPdf() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        }
        else {
            Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==86 && resultCode==RESULT_OK && data != null) {
            pdfUri=data.getData();
            nameOfPdf.setText(getFileName(pdfUri));
        }
        else {
            Toast.makeText(this,"Please select a file", Toast.LENGTH_LONG).show();
        }
    }





    /**
     * method for getting file name
     * */
    public String getFileName(Uri uri){
        String res=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            try{
                if(cursor!=null && cursor.moveToFirst()){
                    res=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if(res==null){
            res=uri.getPath();
            int cut=res.lastIndexOf('/');
            if(cut!=-1){
                res=res.substring(cut+1);
            }
        }
        return res;
    }
}
