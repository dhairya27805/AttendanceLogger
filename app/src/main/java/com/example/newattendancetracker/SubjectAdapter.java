package com.example.newattendancetracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private List<SubjectModel> subjectList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SubjectModel model);
    }

    public SubjectAdapter(List<SubjectModel> subjectList, OnItemClickListener listener) {
        this.subjectList = subjectList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectModel model = subjectList.get(position);
        holder.tvSubjectName.setText(model.getSubjectName());
        holder.tvAttendanceStats.setText("Attended: " + model.getPresentClasses() + " / Total: " + model.getTotalClasses());
        holder.tvPercentage.setText(String.format("%.1f%%", model.getPercentage()));
        holder.pbAttendance.setProgress((int) model.getPercentage());

        if (model.getPercentage() < 75) {
            holder.tvPercentage.setTextColor(Color.parseColor("#F44336")); // Red
            holder.pbAttendance.setIndicatorColor(Color.parseColor("#F44336"));
        } else {
            holder.tvPercentage.setTextColor(Color.parseColor("#4CAF50")); // Green
            holder.pbAttendance.setIndicatorColor(Color.parseColor("#4CAF50"));
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(model));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvAttendanceStats, tvPercentage;
        LinearProgressIndicator pbAttendance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvAttendanceStats = itemView.findViewById(R.id.tvAttendanceStats);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            pbAttendance = itemView.findViewById(R.id.pbAttendance);
        }
    }
}
