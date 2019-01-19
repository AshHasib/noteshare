package com.example.hasib.noteshare;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.hasib.noteshare.adapter.ShowPdfAdapter;
import com.example.hasib.noteshare.model.Pdf;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PdfListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView showPdfRecycler;
    private ShowPdfAdapter showPdfAdapter;
    private List<Pdf> pdfList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);


        /**
         * Toolbar
         * */
        toolbar=findViewById(R.id.pdfListToolbar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setTitle("PDF List");
        actionBar.setDisplayHomeAsUpEnabled(true);

        pdfList=new ArrayList<>();
        showPdfAdapter=new ShowPdfAdapter(this,pdfList);
        showPdfRecycler=findViewById(R.id.showPdfRecycler);
        showPdfRecycler.setHasFixedSize(true);
        showPdfRecycler.setLayoutManager(new LinearLayoutManager(this));
        showPdfRecycler.setAdapter(showPdfAdapter);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("PDF");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    Pdf pdf=ds.getValue(Pdf.class);
                    pdfList.add(pdf);
                    showPdfAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
