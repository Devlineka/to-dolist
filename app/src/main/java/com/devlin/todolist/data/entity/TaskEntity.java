package com.devlin.todolist.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * TaskEntity merepresentasikan tugas dalam daftar tugas.
 * Kelas ini dianotasi dengan Room annotations untuk mendefinisikan tabel database.
 */
@Entity(tableName = "tasks")
public class TaskEntity {

    // Konstanta prioritas
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_HIGH = 2;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "due_date")
    private long dueDate; // 0 berarti tanpa tenggat

    @ColumnInfo(name = "priority")
    private int priority; // 0=Rendah, 1=Sedang, 2=Tinggi

    @ColumnInfo(name = "category")
    private String category; // Kategori tugas

    // Constructor
    public TaskEntity(String title, String description, boolean isCompleted, long createdAt, long dueDate, int priority, String category) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
    }

    // Getters dan Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriorityText() {
        switch (priority) {
            case PRIORITY_HIGH:
                return "Tinggi";
            case PRIORITY_MEDIUM:
                return "Sedang";
            case PRIORITY_LOW:
            default:
                return "Rendah";
        }
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isCompleted=" + isCompleted +
                ", createdAt=" + createdAt +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", category='" + category + '\'' +
                '}';
    }
}
