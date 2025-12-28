package com.devlin.todolist.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.devlin.todolist.data.entity.TaskEntity;
import com.devlin.todolist.data.repository.TaskRepository;

import java.util.List;

/**
 * TaskViewModel menyimpan data terkait UI untuk daftar tugas.
 * Bertahan saat perubahan konfigurasi dan menyediakan data ke UI.
 */
public class TaskViewModel extends AndroidViewModel {

    // Tipe filter
    public enum FilterType {
        ALL,
        ACTIVE,
        COMPLETED
    }

    private final TaskRepository repository;
    private final MutableLiveData<FilterType> currentFilter;
    private final MutableLiveData<String> searchQuery;
    private final MediatorLiveData<List<TaskEntity>> filteredTasks;

    // Sumber LiveData untuk setiap filter
    private final LiveData<List<TaskEntity>> allTasks;
    private final LiveData<List<TaskEntity>> activeTasks;
    private final LiveData<List<TaskEntity>> completedTasks;
    private final LiveData<List<String>> allCategories;

    // Statistik
    private final LiveData<Integer> activeTaskCount;
    private final LiveData<Integer> completedTaskCount;
    private final LiveData<Integer> overdueTaskCount;

    // Tugas yang baru dihapus untuk fitur undo
    private TaskEntity recentlyDeletedTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        
        // Inisialisasi filter dan pencarian
        currentFilter = new MutableLiveData<>(FilterType.ALL);
        searchQuery = new MutableLiveData<>("");
        
        // Ambil semua sumber LiveData
        allTasks = repository.getAllTasks();
        activeTasks = repository.getActiveTasks();
        completedTasks = repository.getCompletedTasks();
        allCategories = repository.getAllCategories();
        
        // Statistik
        activeTaskCount = repository.getActiveTaskCount();
        completedTaskCount = repository.getCompletedTaskCount();
        overdueTaskCount = repository.getOverdueTaskCount();
        
        // Inisialisasi filtered tasks dengan mediator
        filteredTasks = new MediatorLiveData<>();
        
        // Tambahkan sumber ke mediator
        filteredTasks.addSource(allTasks, tasks -> {
            if (currentFilter.getValue() == FilterType.ALL && isSearchEmpty()) {
                filteredTasks.setValue(tasks);
            }
        });
        
        filteredTasks.addSource(activeTasks, tasks -> {
            if (currentFilter.getValue() == FilterType.ACTIVE && isSearchEmpty()) {
                filteredTasks.setValue(tasks);
            }
        });
        
        filteredTasks.addSource(completedTasks, tasks -> {
            if (currentFilter.getValue() == FilterType.COMPLETED && isSearchEmpty()) {
                filteredTasks.setValue(tasks);
            }
        });
        
        // Reaktif terhadap perubahan filter
        filteredTasks.addSource(currentFilter, filter -> {
            if (isSearchEmpty()) {
                updateFilteredTasks(filter);
            }
        });

        // Reaktif terhadap perubahan pencarian
        filteredTasks.addSource(searchQuery, query -> {
            if (query != null && !query.isEmpty()) {
                // Pencarian aktif - gunakan hasil pencarian
                LiveData<List<TaskEntity>> searchResults = repository.searchTasks(query);
                filteredTasks.addSource(searchResults, results -> {
                    filteredTasks.setValue(results);
                    filteredTasks.removeSource(searchResults);
                });
            } else {
                // Pencarian kosong - gunakan filter normal
                updateFilteredTasks(currentFilter.getValue());
            }
        });
    }

    private boolean isSearchEmpty() {
        String query = searchQuery.getValue();
        return query == null || query.isEmpty();
    }

    private void updateFilteredTasks(FilterType filter) {
        if (filter == null) filter = FilterType.ALL;
        
        switch (filter) {
            case ALL:
                if (allTasks.getValue() != null) {
                    filteredTasks.setValue(allTasks.getValue());
                }
                break;
            case ACTIVE:
                if (activeTasks.getValue() != null) {
                    filteredTasks.setValue(activeTasks.getValue());
                }
                break;
            case COMPLETED:
                if (completedTasks.getValue() != null) {
                    filteredTasks.setValue(completedTasks.getValue());
                }
                break;
        }
    }

    /**
     * Ambil tugas yang difilter berdasarkan pilihan filter saat ini
     */
    public LiveData<List<TaskEntity>> getFilteredTasks() {
        return filteredTasks;
    }

    /**
     * Ambil tipe filter saat ini
     */
    public LiveData<FilterType> getCurrentFilter() {
        return currentFilter;
    }

    /**
     * Set tipe filter
     */
    public void setFilter(FilterType filter) {
        currentFilter.setValue(filter);
    }

    /**
     * Set query pencarian
     */
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    /**
     * Ambil query pencarian saat ini
     */
    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    /**
     * Ambil semua kategori
     */
    public LiveData<List<String>> getAllCategories() {
        return allCategories;
    }

    /**
     * Ambil tugas berdasarkan ID
     */
    public LiveData<TaskEntity> getTaskById(int taskId) {
        return repository.getTaskById(taskId);
    }

    // Statistik
    public LiveData<Integer> getActiveTaskCount() {
        return activeTaskCount;
    }

    public LiveData<Integer> getCompletedTaskCount() {
        return completedTaskCount;
    }

    public LiveData<Integer> getOverdueTaskCount() {
        return overdueTaskCount;
    }

    /**
     * Sisipkan tugas baru
     */
    public void insert(TaskEntity task) {
        repository.insert(task);
    }

    /**
     * Perbarui tugas yang ada
     */
    public void update(TaskEntity task) {
        repository.update(task);
    }

    /**
     * Hapus tugas (dengan menyimpan untuk undo)
     */
    public void delete(TaskEntity task) {
        recentlyDeletedTask = task;
        repository.delete(task);
    }

    /**
     * Undo hapus terakhir
     */
    public void undoDelete() {
        if (recentlyDeletedTask != null) {
            repository.insert(recentlyDeletedTask);
            recentlyDeletedTask = null;
        }
    }

    /**
     * Cek apakah ada tugas yang bisa di-undo
     */
    public boolean canUndo() {
        return recentlyDeletedTask != null;
    }

    /**
     * Ambil tugas yang baru dihapus
     */
    public TaskEntity getRecentlyDeletedTask() {
        return recentlyDeletedTask;
    }

    /**
     * Hapus tugas berdasarkan ID
     */
    public void deleteById(int taskId) {
        repository.deleteById(taskId);
    }

    /**
     * Toggle status selesai tugas
     */
    public void toggleComplete(TaskEntity task) {
        repository.toggleComplete(task);
    }

    /**
     * Hapus semua tugas
     */
    public void deleteAll() {
        repository.deleteAll();
    }
}
