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
import com.example.hasib.noteshare.model.UploadImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DisplayImageAdapter extends RecyclerView.Adapter<DisplayImageAdapter.ViewHolder> {

    public List<UploadImage> imagesList;
    public Context context;

    public DisplayImageAdapter(List<UploadImage> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_view_image,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CircularProgressDrawable cr=new CircularProgressDrawable(context);
        cr.setColorSchemeColors(Color.BLACK);
        cr.setCenterRadius(40f);
        cr.setStrokeWidth(8f);
        Picasso.get().load(imagesList.get(i).getImageUri()).placeholder(cr).into(viewHolder.img);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            img=view.findViewById(R.id.singleImgUpload);
        }
    }
}
