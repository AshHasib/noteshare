package com.example.hasib.noteshare;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowCourses extends AppCompatActivity {

    private FloatingActionButton fabAddNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_courses);

        fabAddNew=(FloatingActionButton)findViewById(R.id.addNewFab);

        fabAddNew.setOnClickListener(v->{
            startActivity(new Intent(ShowCourses.this,UploadActivity.class));
            finish();
        });
    }
}
