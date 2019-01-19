package com.example.hasib.noteshare.adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hasib.noteshare.R;
import com.example.hasib.noteshare.ShowImages;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadersAdapter extends RecyclerView.Adapter<UploadersAdapter.ViewHolder> {

    public List<String> uploadersList;
    public Context context;
    String courseName;
    public UploadersAdapter( Context context,List<String> uploadersList,String courseName) {
        this.uploadersList = uploadersList;
        this.context = context;
        this.courseName=courseName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.uploader,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String name=uploadersList.get(i);
        viewHolder.uploaderName.setText(name);
    }

    @Override
    public int getItemCount() {
        return uploadersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView uploaderName;
        LinearLayout uploaderBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            uploaderName=view.findViewById(R.id.uploaderName);
            uploaderBar=view.findViewById(R.id.uploaderBar);
            uploaderBar.setOnClickListener(v->{
                int pos=getAdapterPosition();
                Intent intent=new Intent(context,ShowImages.class);
                intent.putExtra("COURSE_NAME",courseName);
                intent.putExtra("UPLOADER_NAME",uploadersList.get(pos));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }
    }
}
