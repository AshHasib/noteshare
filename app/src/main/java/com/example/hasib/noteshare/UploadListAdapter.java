package com.example.hasib.noteshare;

import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    List<String>fileNames;
    List<Uri>filesUri;


    public UploadListAdapter(List<String>fileNames,List<Uri> filesUri){
        this.fileNames=fileNames;
        this.filesUri=filesUri;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.up_list_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name=fileNames.get(i);
        Uri uri=filesUri.get(i);

        viewHolder.txtFileName.setText(name);
        Picasso.get().load(uri).into(viewHolder.image);
        Log.d("TAGFROMADAPTER",name);
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtFileName;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            View view=itemView;

            txtFileName=view.findViewById(R.id.filename);
            image=view.findViewById(R.id.upImage);
        }
    }
}
