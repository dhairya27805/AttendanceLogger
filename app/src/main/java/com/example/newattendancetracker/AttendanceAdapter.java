package com.example.newattendancetracker;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<AttendanceModel> attendanceList;

    public AttendanceAdapter(List<AttendanceModel> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceModel model = attendanceList.get(position);
        holder.tvSubjectName.setText(model.getSubject());
        holder.tvDate.setText(model.getDate());
        holder.tvStatus.setText(model.getStatus());

        GradientDrawable background = (GradientDrawable) holder.tvStatus.getBackground();
        if (model.getStatus().equalsIgnoreCase("Present")) {
            background.setColor(Color.parseColor("#4CAF50")); // Green
        } else {
            background.setColor(Color.parseColor("#F44336")); // Red
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public void updateList(List<AttendanceModel> newList) {
        this.attendanceList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvDate, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
