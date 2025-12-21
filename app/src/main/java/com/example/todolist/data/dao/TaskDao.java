package com.example.todolist.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.data.entity.TaskEntity;

import java.util.List;

/**
 * TaskDao mendefinisikan operasi database untuk tugas.
 * Menggunakan anotasi Room untuk generate query SQL secara otomatis.
 */
@Dao
public interface TaskDao {

    /**
     * Ambil semua tugas diurutkan berdasarkan prioritas (tertinggi dulu), lalu tanggal
     */
    @Query("SELECT * FROM tasks ORDER BY priority DESC, created_at DESC")
    LiveData<List<TaskEntity>> getAllTasks();

    /**
     * Ambil tugas aktif (belum selesai) saja
     */
    @Query("SELECT * FROM tasks WHERE is_completed = 0 ORDER BY priority DESC, created_at DESC")
    LiveData<List<TaskEntity>> getActiveTasks();

    /**
     * Ambil tugas yang sudah selesai saja
     */
    @Query("SELECT * FROM tasks WHERE is_completed = 1 ORDER BY created_at DESC")
    LiveData<List<TaskEntity>> getCompletedTasks();

    /**
     * Cari tugas berdasarkan judul atau deskripsi
     */
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY priority DESC, created_at DESC")
    LiveData<List<TaskEntity>> searchTasks(String query);

    /**
     * Ambil tugas berdasarkan kategori
     */
    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY priority DESC, created_at DESC")
    LiveData<List<TaskEntity>> getTasksByCategory(String category);

    /**
     * Ambil semua kategori unik
     */
    @Query("SELECT DISTINCT category FROM tasks WHERE category IS NOT NULL AND category != '' ORDER BY category")
    LiveData<List<String>> getAllCategories();

    /**
     * Ambil satu tugas berdasarkan ID
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    LiveData<TaskEntity> getTaskById(int taskId);

    /**
     * Ambil satu tugas berdasarkan ID (sinkron untuk operasi edit)
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    TaskEntity getTaskByIdSync(int taskId);

    /**
     * Sisipkan tugas baru
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TaskEntity task);

    /**
     * Sisipkan beberapa tugas (untuk data contoh)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TaskEntity> tasks);

    /**
     * Perbarui tugas yang ada
     */
    @Update
    void update(TaskEntity task);

    /**
     * Hapus tugas
     */
    @Delete
    void delete(TaskEntity task);

    /**
     * Hapus tugas berdasarkan ID
     */
    @Query("DELETE FROM tasks WHERE id = :taskId")
    void deleteById(int taskId);

    /**
     * Hapus semua tugas
     */
    @Query("DELETE FROM tasks")
    void deleteAll();

    /**
     * Hitung jumlah semua tugas
     */
    @Query("SELECT COUNT(*) FROM tasks")
    int getTaskCount();

    /**
     * Hitung tugas aktif
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0")
    LiveData<Integer> getActiveTaskCount();

    /**
     * Hitung tugas selesai
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 1")
    LiveData<Integer> getCompletedTaskCount();

    /**
     * Hitung tugas terlambat (overdue)
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE is_completed = 0 AND due_date > 0 AND due_date < :currentTime")
    LiveData<Integer> getOverdueTaskCount(long currentTime);
}
