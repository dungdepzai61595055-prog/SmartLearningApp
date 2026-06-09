package com.smartlearning.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.smartlearning.app.R;
import com.smartlearning.app.model.Grade;
import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {
    private List<Grade> grades;
    private Context context;

    public GradeAdapter(List<Grade> grades, Context context) {
        this.grades = grades;
        this.context = context;
    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GradeViewHolder holder, int position) {
        Grade grade = grades.get(position);
        holder.tvSubjectName.setText(grade.getSubjectName());
        holder.tvScore.setText(String.format("%.1f", grade.getScore()));
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvScore;

        public GradeViewHolder(View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }
}
