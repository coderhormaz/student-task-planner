package com.hormaz.studentplanner.ui.task.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.data.model.SubTask;

import java.util.ArrayList;
import java.util.List;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.SubTaskViewHolder> {

    private List<SubTask> subTasks;
    private final OnSubTaskListener listener;

    public interface OnSubTaskListener {
        void onSubTaskChecked(SubTask subTask, boolean checked);
        void onSubTaskRemoved(SubTask subTask);
    }

    public SubTaskAdapter(OnSubTaskListener listener) {
        this.subTasks = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_task, parent, false);
        return new SubTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubTaskViewHolder holder, int position) {
        SubTask subTask = subTasks.get(position);
        holder.bind(subTask, position);
    }

    @Override
    public int getItemCount() {
        return subTasks.size();
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = new ArrayList<>(subTasks);
        notifyDataSetChanged();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        notifyItemInserted(subTasks.size() - 1);
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    class SubTaskViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox cbSubTask;
        private final TextView tvSubTaskTitle;
        private final ImageButton btnRemove;

        SubTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSubTask = itemView.findViewById(R.id.cbSubTask);
            tvSubTaskTitle = itemView.findViewById(R.id.tvSubTaskTitle);
            btnRemove = itemView.findViewById(R.id.btnRemoveSubTask);
        }

        void bind(SubTask subTask, int position) {
            tvSubTaskTitle.setText(subTask.getTitle());
            cbSubTask.setChecked(subTask.isCompleted());

            cbSubTask.setOnClickListener(v -> {
                boolean checked = cbSubTask.isChecked();
                subTask.setCompleted(checked);
                if (listener != null) listener.onSubTaskChecked(subTask, checked);
            });

            btnRemove.setOnClickListener(v -> {
                subTasks.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, subTasks.size());
                if (listener != null) listener.onSubTaskRemoved(subTask);
            });
        }
    }
}
