package com.example.hasib.noteshare;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.example.hasib.noteshare.model.Key;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity {
    private Button btnSelect,btnUpload;
    List<String> fileNames;
    List<Uri> filesUri;


    private StorageReference storageReference;

    private RecyclerView recyclerView;
    private UploadListAdapter uploadListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        fileNames=new ArrayList<>();
        filesUri=new ArrayList<>();

        btnSelect=findViewById(R.id.btnSelect);
        btnUpload=findViewById(R.id.btnUpload);

        btnSelect.setOnClickListener(v->{
            openFileChooser();
        });

        uploadListAdapter=new UploadListAdapter(fileNames,filesUri);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(uploadListAdapter);
    }


    public void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),Key.RESULT_CODE);
    }


    public void uploadImages(){
        for(Uri uri:filesUri){
            if(uri!=null){


            }
        }
    }


    public String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG","REQ_CODE Accepted ____________");
        if(requestCode==Key.RESULT_CODE && resultCode== RESULT_OK){


            if(data.getClipData()!=null){
                //multiple images selected
                int N=data.getClipData().getItemCount();

                for(int i=0;i<N;i++){
                    Uri uri=data.getClipData().getItemAt(i).getUri();
                    filesUri.add(uri);

                    String name=getFileName(uri);
                    Log.d("TAG",name);
                    fileNames.add(name);
                    uploadListAdapter.notifyDataSetChanged();

                }
            }
            else if(data.getData()!=null){
                //single file selected
            }
        }
    }


    public String getFileName(Uri uri){
        String res=null;

        if(uri.getScheme().equals("context")){
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            try{
                if(cursor!=null && cursor.moveToFirst()){
                    res=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
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
