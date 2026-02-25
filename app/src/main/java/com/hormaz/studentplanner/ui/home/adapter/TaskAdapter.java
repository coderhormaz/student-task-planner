package com.hormaz.studentplanner.ui.home.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.data.model.Task;
import com.hormaz.studentplanner.util.AnimationUtils;
import com.hormaz.studentplanner.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private final OnTaskClickListener listener;
    private final boolean reduceMotion;

    public interface OnTaskClickListener {
        void onTaskClick(Task task, int position);
        void onTaskChecked(Task task, int position, boolean isChecked);
    }

    public TaskAdapter(OnTaskClickListener listener, boolean reduceMotion) {
        this.tasks = new ArrayList<>();
        this.listener = listener;
        this.reduceMotion = reduceMotion;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task, position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        if (position >= 0 && position < tasks.size()) {
            return tasks.get(position);
        }
        return null;
    }

    public void removeAt(int position) {
        if (position >= 0 && position < tasks.size()) {
            tasks.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addAt(int position, Task task) {
        tasks.add(position, task);
        notifyItemInserted(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox cbComplete;
        private final TextView tvTitle;
        private final TextView tvSubtitle;
        private final View priorityDot;
        private final TextView tvCategoryTag;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbComplete = itemView.findViewById(R.id.cbComplete);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvSubtitle = itemView.findViewById(R.id.tvTaskSubtitle);
            priorityDot = itemView.findViewById(R.id.priorityDot);
            tvCategoryTag = itemView.findViewById(R.id.tvCategoryTag);
        }

        void bind(Task task, int position) {
            tvTitle.setText(task.getTitle());

            // Subtitle: time + relative date
            String subtitle = "";
            if (task.getDueDate() != null && !task.getDueDate().isEmpty()) {
                subtitle = DateUtils.getRelativeDate(task.getDueDate());
                String time = DateUtils.formatTime(task.getDueDate());
                if (!time.isEmpty()) {
                    subtitle += " · " + time;
                }
            }
            tvSubtitle.setText(subtitle);
            tvSubtitle.setVisibility(subtitle.isEmpty() ? View.GONE : View.VISIBLE);

            // Category tag
            if (task.getCategoryName() != null && !task.getCategoryName().isEmpty()) {
                tvCategoryTag.setText(task.getCategoryName());
                tvCategoryTag.setVisibility(View.VISIBLE);
            } else {
                tvCategoryTag.setVisibility(View.GONE);
            }

            // Priority dot
            Context context = itemView.getContext();
            int priorityColor;
            switch (task.getPriority()) {
                case 3:
                    priorityColor = context.getColor(R.color.priority_high);
                    break;
                case 2:
                    priorityColor = context.getColor(R.color.priority_medium);
                    break;
                default:
                    priorityColor = context.getColor(R.color.priority_low);
                    break;
            }
            priorityDot.getBackground().setTint(priorityColor);

            // Completed state
            boolean isCompleted = task.isCompleted();
            cbComplete.setChecked(isCompleted);
            if (isCompleted) {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvTitle.setAlpha(0.5f);
                tvSubtitle.setAlpha(0.5f);
            } else {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                tvTitle.setAlpha(1f);
                tvSubtitle.setAlpha(1f);
            }

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTaskClick(task, position);
            });

            cbComplete.setOnClickListener(v -> {
                if (!reduceMotion) {
                    AnimationUtils.checkboxAnimation(cbComplete, null);
                }
                v.performHapticFeedback(HapticFeedbackConstants.CONFIRM);
                if (listener != null) listener.onTaskChecked(task, position, cbComplete.isChecked());
            });
        }
    }
}
