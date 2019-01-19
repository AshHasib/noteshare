package com.example.hasib.noteshare;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasib.noteshare.adapter.UploadListAdapter;
import com.example.hasib.noteshare.model.Key;
import com.example.hasib.noteshare.model.UploadImage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadImageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private static final int RESULT_LOAD_IMAGE=1;
    public List<String> fileNameList;
    public List<Uri> fileUriList;
    private RecyclerView imageRecycler;
    private UploadListAdapter uploadListAdapter;
    private CardView selectCard,uploadCard,btnSelectPdf;
    private Spinner courseName;
    private UserSessionManager sessionManager;
    private StorageReference mainStorageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference imageReference;
    StorageReference folderReference;
    private StorageTask uploadTask;
    TextView uploadStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);


        toolbar=findViewById(R.id.uploadtoolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Upload");
        actionBar.setDisplayHomeAsUpEnabled(true);


        uploadStatus=findViewById(R.id.uploadStatus);
        mainStorageReference=FirebaseStorage.getInstance().getReference("CSE");
        sessionManager=new UserSessionManager(UploadImageActivity.this);
        fileNameList=new ArrayList<>();
        fileUriList=new ArrayList<>();
        uploadListAdapter=new UploadListAdapter(UploadImageActivity.this,fileNameList,fileUriList);
        imageRecycler=findViewById(R.id.imagesRecycler);
        imageRecycler.setHasFixedSize(true);
        imageRecycler.setLayoutManager(new GridLayoutManager(this,2));
        imageRecycler.setAdapter(uploadListAdapter);


        courseName=findViewById(R.id.courseNameSpinner);




        /**
         * Handling image uploads
         * */
        selectCard=findViewById(R.id.selectBtn);
        selectCard.setOnClickListener(v->{
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),RESULT_LOAD_IMAGE);

        });
        uploadCard=findViewById(R.id.uploadBtn);
        uploadCard.setOnClickListener(v->{
            uploadImages();
        });



        /**
         * Handling PDF uploads
         **/
        btnSelectPdf=findViewById(R.id.btnPdf);
        btnSelectPdf.setOnClickListener(v-> {
            startActivity(new Intent(UploadImageActivity.this,UploadPdfActivity.class));
        });


    }


    /**
     * Fetching all the images to arraylist
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            if(data.getClipData()!=null){
                //Multiple selected
                int totalData=data.getClipData().getItemCount();

                for(int i=0;i<totalData;i++) {
                    Uri fileUri=data.getClipData().getItemAt(i).getUri();
                    fileUriList.add(fileUri);
                    String fileName=getFileName(fileUri);
                    fileNameList.add(fileName);
                    uploadListAdapter.notifyDataSetChanged();
                }
            }
            else if(data.getData()!=null){
                //single selected
            }
        }
    }







    /**
     * Method for uploading images
     * */
    public void uploadImages(){

        String cName=courseName.getSelectedItem().toString();

        HashMap<String,String> uploader=sessionManager.getUserDetails();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("CSE").child(cName).child(uploader.get(Key.USER_NAME));
        for(int i=0;i<fileUriList.size();i++){
            Uri imgUri=fileUriList.get(i);
            folderReference=mainStorageReference.child(cName).child(uploader.get(Key.USER_NAME)).child(getFileName(imgUri));

            uploadTask=folderReference.putFile(imgUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        uploadStatus.setText("Upload Status: Suceess");
                        //Toast.makeText(UploadImageActivity.this,"Upload Success",Toast.LENGTH_LONG).show();
                        UploadImage image=new UploadImage(getFileName(imgUri),taskSnapshot.getDownloadUrl().toString());
                        String uploadId=databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(image);
                    })
                    .addOnFailureListener(e->{
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(),Toast.LENGTH_LONG).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        uploadStatus.setText("Upload Status: Uploading");
                        //Toast.makeText(getApplicationContext(),"Uploading. . .",Toast.LENGTH_LONG).show();
                    });
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






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }
}
