package com.example.hasib.noteshare;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.hasib.noteshare.adapter.UploadersAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ShowUploaders extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView uploaderRecycler;
    DatabaseReference reference;
    private List<String> uploaderList;
    UploadersAdapter uploadersAdapter;

    private String courseName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_uploaders);


        toolbar=findViewById(R.id.uploadersToolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Uploaders");
        actionBar.setDisplayHomeAsUpEnabled(true);

        uploaderList=new ArrayList<>();
        courseName=getIntent().getExtras().getString("COURSE_NAME");
        uploadersAdapter=new UploadersAdapter(ShowUploaders.this,uploaderList,courseName);
        reference=FirebaseDatabase.getInstance().getReference().child("CSE").child(courseName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String key=ds.getKey();
                    uploaderList.add(key);
                    uploadersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uploaderRecycler=findViewById(R.id.uploadersRecycler);
        uploaderRecycler.setHasFixedSize(true);
        uploaderRecycler.setLayoutManager(new LinearLayoutManager(this));
        uploaderRecycler.setAdapter(uploadersAdapter);
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
