package com.example.hasib.noteshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasib.noteshare.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<Uri> fileUriList;

    public Context context;

    public UploadListAdapter(Context context,List<String> fileNameList,List<Uri> fileUriList){
        this.context=context;
        this.fileNameList=fileNameList;
        this.fileUriList=fileUriList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_image,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name=fileNameList.get(i);
        viewHolder.imgName.setText(name);

        CircularProgressDrawable cr=new CircularProgressDrawable(context);
        cr.setColorSchemeColors(Color.BLACK);
        cr.setCenterRadius(30f);
        cr.setStrokeWidth(5f);
        Picasso.get().load(fileUriList.get(i)).placeholder(cr).into(viewHolder.img);

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView img;
        TextView imgName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            img=view.findViewById(R.id.singleImgUpload);
            imgName=view.findViewById(R.id.singleImgName);

        }
    }
}
