package com.example.hasib.noteshare;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.hasib.noteshare.adapter.CourseAdapter;

import java.util.ArrayList;
import java.util.List;

public class CSEDashBoard extends AppCompatActivity {

    private Toolbar cseToolbar;
    private ActionBar actionBar;
    private RecyclerView courseNameRecycler;
    private CourseAdapter courseAdapter;
    private List<String> courseNames;
    private FloatingActionButton addNotes,showPdfList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csedash_board);


        /**
         * Toolbar
         * */
        cseToolbar=findViewById(R.id.cseToolbar);
        setSupportActionBar(cseToolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("CSE");
        actionBar.setDisplayHomeAsUpEnabled(true);


        courseNames=new ArrayList<>();
        courseNames.add("Data Communication");
        courseNames.add("Digital System Design");
        courseNames.add("Numerical Analysis");
        courseNames.add("Sociology");
        courseNames.add("Applied Statistics");
        courseNames.add("Software Engineering");


        courseNameRecycler=findViewById(R.id.cseCourseRecycler);
        courseAdapter=new CourseAdapter(CSEDashBoard.this,courseNames);
        courseNameRecycler.setHasFixedSize(true);
        courseNameRecycler.setLayoutManager(new LinearLayoutManager(this));
        courseNameRecycler.setAdapter(courseAdapter);


        /**
         * Activity for adding notes handler
         * */
        addNotes=findViewById(R.id.addNotes);
        addNotes.setOnClickListener(v->{
            startActivity(new Intent(CSEDashBoard.this,UploadImageActivity.class));
        });


        showPdfList=findViewById(R.id.showPdfList);
        showPdfList.setOnClickListener(v->{
            startActivity(new Intent(CSEDashBoard.this,PdfListActivity.class));
        });

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
