package com.example.hasib.noteshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class ShowPdfActivity extends AppCompatActivity {

    PDFView pdfHolder;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);

        url=getIntent().getExtras().getString("URL");
        pdfHolder=findViewById(R.id.pdfHolder);
    }
}
