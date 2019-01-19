package com.example.hasib.noteshare;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.hasib.noteshare.adapter.DisplayImageAdapter;
import com.example.hasib.noteshare.adapter.UploadListAdapter;
import com.example.hasib.noteshare.model.UploadImage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowImages extends AppCompatActivity {


    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView showImagesRecycler;
    String courseName;
    String uploaderName;
    private DatabaseReference reference;
    private List<UploadImage> imagesList;
    private DisplayImageAdapter displayImageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

        toolbar=findViewById(R.id.imagesToolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Images");
        actionBar.setDisplayHomeAsUpEnabled(true);

        imagesList=new ArrayList<>();
        courseName=getIntent().getExtras().getString("COURSE_NAME");
        uploaderName=getIntent().getExtras().getString("UPLOADER_NAME");
        displayImageAdapter=new DisplayImageAdapter(imagesList,ShowImages.this);
        reference=FirebaseDatabase.getInstance().getReference().child("CSE").child(courseName).child(uploaderName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UploadImage image=ds.getValue(UploadImage.class);
                    imagesList.add(image);
                    displayImageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        showImagesRecycler=findViewById(R.id.showImagesRecycler);
        showImagesRecycler.setHasFixedSize(true);
        showImagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        showImagesRecycler.setAdapter(displayImageAdapter);
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
