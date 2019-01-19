package com.example.hasib.noteshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hasib.noteshare.R;
import com.example.hasib.noteshare.model.Pdf;

import java.util.List;

public class ShowPdfAdapter extends RecyclerView.Adapter<ShowPdfAdapter.ViewHolder> {
    private Context context;
    private List<Pdf> pdfList;

    public ShowPdfAdapter(Context context, List<Pdf> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.pdf_list_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txtPdfName.setText(pdfList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPdfName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPdfName=itemView.findViewById(R.id.txtPdfName);
            txtPdfName.setOnClickListener(v->{
                Uri uri = Uri.parse(pdfList.get(getAdapterPosition()).getUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            });
        }
    }
}
