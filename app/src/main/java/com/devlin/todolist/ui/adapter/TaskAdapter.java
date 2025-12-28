package com.devlin.todolist.ui.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.devlin.todolist.R;
import com.devlin.todolist.data.entity.TaskEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TaskAdapter untuk menampilkan tugas di RecyclerView.
 * Menggunakan ListAdapter dengan DiffUtil untuk update yang efisien.
 */
public class TaskAdapter extends ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder> {

    private OnTaskClickListener listener;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dueDateFormat;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
        dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        dueDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    private static final DiffUtil.ItemCallback<TaskEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TaskEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle()) &&
                            oldItem.getDescription().equals(newItem.getDescription()) &&
                            oldItem.isCompleted() == newItem.isCompleted() &&
                            oldItem.getDueDate() == newItem.getDueDate() &&
                            oldItem.getPriority() == newItem.getPriority();
                }
            };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskEntity currentTask = getItem(position);
        holder.bind(currentTask);
    }

    public TaskEntity getTaskAt(int position) {
        return getItem(position);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }

    /**
     * Interface untuk menangani interaksi item tugas
     */
    public interface OnTaskClickListener {
        void onTaskClick(TaskEntity task);
        void onTaskLongClick(TaskEntity task);
        void onCheckBoxClick(TaskEntity task, boolean isChecked);
    }

    /**
     * ViewHolder untuk item tugas
     */
    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final CheckBox checkBoxComplete;
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final TextView textViewDate;
        private final TextView textViewDueDate;
        private final TextView textViewPriority;
        private final TextView textViewCategory;
        private final View priorityIndicator;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view_task);
            checkBoxComplete = itemView.findViewById(R.id.checkbox_complete);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewDueDate = itemView.findViewById(R.id.text_view_due_date);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            textViewCategory = itemView.findViewById(R.id.text_view_category);
            priorityIndicator = itemView.findViewById(R.id.priority_indicator);

            // Click listener untuk edit
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(getItem(position));
                }
            });

            // Long click listener untuk hapus
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTaskLongClick(getItem(position));
                }
                return true;
            });

            // Checkbox listener untuk toggle selesai
            checkBoxComplete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCheckBoxClick(getItem(position), checkBoxComplete.isChecked());
                }
            });
        }

        public void bind(TaskEntity task) {
            textViewTitle.setText(task.getTitle());
            textViewDescription.setText(task.getDescription());
            textViewDate.setText(dateFormat.format(new Date(task.getCreatedAt())));
            
            // Set checkbox state tanpa memicu listener
            checkBoxComplete.setOnClickListener(null);
            checkBoxComplete.setChecked(task.isCompleted());
            checkBoxComplete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCheckBoxClick(getItem(position), checkBoxComplete.isChecked());
                }
            });

            // Tampilkan tanggal jatuh tempo
            if (task.getDueDate() > 0) {
                textViewDueDate.setVisibility(View.VISIBLE);
                String dueDateText = dueDateFormat.format(new Date(task.getDueDate()));
                
                // Cek apakah terlambat (dan belum selesai)
                if (!task.isCompleted() && task.getDueDate() < System.currentTimeMillis()) {
                    textViewDueDate.setText(itemView.getContext().getString(R.string.overdue_format, dueDateText));
                    textViewDueDate.setTextColor(Color.parseColor("#F44336")); // Merah untuk terlambat
                } else {
                    textViewDueDate.setText(itemView.getContext().getString(R.string.due_format, dueDateText));
                    textViewDueDate.setTextColor(itemView.getContext().getResources().getColor(R.color.primary, null));
                }
            } else {
                textViewDueDate.setVisibility(View.GONE);
            }

            // Tampilkan prioritas
            textViewPriority.setText(task.getPriorityText());
            int priorityColor;
            switch (task.getPriority()) {
                case TaskEntity.PRIORITY_HIGH:
                    priorityColor = Color.parseColor("#F44336"); // Merah
                    break;
                case TaskEntity.PRIORITY_MEDIUM:
                    priorityColor = Color.parseColor("#FF9800"); // Oranye
                    break;
                case TaskEntity.PRIORITY_LOW:
                default:
                    priorityColor = Color.parseColor("#4CAF50"); // Hijau
                    break;
            }
            textViewPriority.setTextColor(priorityColor);
            priorityIndicator.setBackgroundColor(priorityColor);

            // Tampilkan kategori
            if (task.getCategory() != null && !task.getCategory().isEmpty()) {
                textViewCategory.setVisibility(View.VISIBLE);
                textViewCategory.setText(task.getCategory());
            } else {
                textViewCategory.setVisibility(View.GONE);
            }

            // Terapkan strikethrough untuk tugas selesai
            if (task.isCompleted()) {
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewDescription.setPaintFlags(textViewDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                cardView.setAlpha(0.7f);
            } else {
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                textViewDescription.setPaintFlags(textViewDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                cardView.setAlpha(1.0f);
            }
        }
    }
}
