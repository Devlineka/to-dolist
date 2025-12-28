package com.devlin.todolist.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.devlin.todolist.data.dao.TaskDao;
import com.devlin.todolist.data.database.AppDatabase;
import com.devlin.todolist.data.entity.TaskEntity;

import java.util.List;

/**
 * TaskRepository mengabstraksi lapisan data dari ViewModel.
 * Menyediakan API bersih untuk akses data ke seluruh aplikasi.
 */
public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<TaskEntity>> allTasks;
    private final LiveData<List<TaskEntity>> activeTasks;
    private final LiveData<List<TaskEntity>> completedTasks;
    private final LiveData<List<String>> allCategories;

    public TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
        activeTasks = taskDao.getActiveTasks();
        completedTasks = taskDao.getCompletedTasks();
        allCategories = taskDao.getAllCategories();
    }

    // Getter LiveData - Room menjalankan semua query di thread terpisah
    public LiveData<List<TaskEntity>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<TaskEntity>> getActiveTasks() {
        return activeTasks;
    }

    public LiveData<List<TaskEntity>> getCompletedTasks() {
        return completedTasks;
    }

    public LiveData<TaskEntity> getTaskById(int taskId) {
        return taskDao.getTaskById(taskId);
    }

    public LiveData<List<String>> getAllCategories() {
        return allCategories;
    }

    /**
     * Cari tugas berdasarkan query
     */
    public LiveData<List<TaskEntity>> searchTasks(String query) {
        return taskDao.searchTasks(query);
    }

    /**
     * Ambil tugas berdasarkan kategori
     */
    public LiveData<List<TaskEntity>> getTasksByCategory(String category) {
        return taskDao.getTasksByCategory(category);
    }

    /**
     * Statistik: Hitung tugas aktif
     */
    public LiveData<Integer> getActiveTaskCount() {
        return taskDao.getActiveTaskCount();
    }

    /**
     * Statistik: Hitung tugas selesai
     */
    public LiveData<Integer> getCompletedTaskCount() {
        return taskDao.getCompletedTaskCount();
    }

    /**
     * Statistik: Hitung tugas terlambat
     */
    public LiveData<Integer> getOverdueTaskCount() {
        return taskDao.getOverdueTaskCount(System.currentTimeMillis());
    }

    /**
     * Ambil tugas berdasarkan ID secara sinkron (untuk operasi edit)
     * Harus dipanggil dari thread latar belakang
     */
    public TaskEntity getTaskByIdSync(int taskId) {
        return taskDao.getTaskByIdSync(taskId);
    }

    /**
     * Sisipkan tugas baru
     * Berjalan di thread latar belakang via ExecutorService
     */
    public void insert(TaskEntity task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task);
        });
    }

    /**
     * Perbarui tugas yang ada
     * Berjalan di thread latar belakang via ExecutorService
     */
    public void update(TaskEntity task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.update(task);
        });
    }

    /**
     * Hapus tugas
     * Berjalan di thread latar belakang via ExecutorService
     */
    public void delete(TaskEntity task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.delete(task);
        });
    }

    /**
     * Hapus tugas berdasarkan ID
     * Berjalan di thread latar belakang via ExecutorService
     */
    public void deleteById(int taskId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteById(taskId);
        });
    }

    /**
     * Hapus semua tugas
     * Berjalan di thread latar belakang via ExecutorService
     */
    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteAll();
        });
    }

    /**
     * Toggle status selesai tugas
     * Berjalan di thread latar belakang via ExecutorService
     */
    public void toggleComplete(TaskEntity task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            task.setCompleted(!task.isCompleted());
            taskDao.update(task);
        });
    }
}
