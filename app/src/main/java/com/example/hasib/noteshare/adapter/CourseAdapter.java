package com.example.hasib.noteshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hasib.noteshare.R;
import com.example.hasib.noteshare.ShowUploaders;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Context context;
    private List<String> courseNames;

    public CourseAdapter(Context context,List courseNames){
        this.context=context;
        this.courseNames=courseNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.course_row,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.courseName.setText(courseNames.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return courseNames.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView courseName;
        LinearLayout courseBar;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            courseName=itemView.findViewById(R.id.courseName);
            courseBar=itemView.findViewById(R.id.courseBar);
            courseBar.setOnClickListener(v->{
                int p=getAdapterPosition();
                String name=courseNames.get(p);
                Intent intent=new Intent(context,ShowUploaders.class);
                intent.putExtra("COURSE_NAME",name);
                context.startActivity(intent);
            });
        }


    }
}
