package com.example.todolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.data.entity.TaskEntity;
import com.example.todolist.ui.adapter.TaskAdapter;
import com.example.todolist.ui.viewmodel.TaskViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * MainActivity menampilkan daftar tugas dengan opsi filter via navigasi bawah.
 * Menyediakan navigasi untuk tambah/edit tugas dan menangani konfirmasi hapus.
 */
public class MainActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private TextView textViewEmpty;
    private TextView textViewTitle;
    private EditText editTextSearch;
    private ImageButton buttonClearSearch;
    private BottomNavigationView bottomNavigationView;
    private View rootView;

    // Statistik
    private TextView textViewActiveCount;
    private TextView textViewCompletedCount;
    private TextView textViewOverdueCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.root_layout);

        // Inisialisasi views
        initViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup ViewModel
        setupViewModel();
        
        // Setup Bottom Navigation
        setupBottomNavigation();
        
        // Setup Search
        setupSearch();
        
        // Setup FAB
        setupFab();
    }

    private void initViews() {
        textViewEmpty = findViewById(R.id.text_view_empty);
        textViewTitle = findViewById(R.id.text_view_title);
        editTextSearch = findViewById(R.id.edit_text_search);
        buttonClearSearch = findViewById(R.id.button_clear_search);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        
        // Statistik
        textViewActiveCount = findViewById(R.id.text_view_active_count);
        textViewCompletedCount = findViewById(R.id.text_view_completed_count);
        textViewOverdueCount = findViewById(R.id.text_view_overdue_count);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        // Set click listeners
        adapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(TaskEntity task) {
                // Buka activity edit
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.EXTRA_IS_COMPLETED, task.isCompleted());
                intent.putExtra(AddEditTaskActivity.EXTRA_CREATED_AT, task.getCreatedAt());
                intent.putExtra(AddEditTaskActivity.EXTRA_DUE_DATE, task.getDueDate());
                intent.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority());
                intent.putExtra(AddEditTaskActivity.EXTRA_CATEGORY, task.getCategory());
                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }

            @Override
            public void onTaskLongClick(TaskEntity task) {
                // Tampilkan dialog konfirmasi hapus
                showDeleteConfirmationDialog(task);
            }

            @Override
            public void onCheckBoxClick(TaskEntity task, boolean isChecked) {
                // Toggle status selesai tugas
                taskViewModel.toggleComplete(task);
            }
        });
    }

    private void setupViewModel() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        
        // Observe filtered tasks
        taskViewModel.getFilteredTasks().observe(this, tasks -> {
            adapter.submitList(tasks);
            
            // Tampilkan/sembunyikan empty state
            if (tasks == null || tasks.isEmpty()) {
                textViewEmpty.setVisibility(View.VISIBLE);
            } else {
                textViewEmpty.setVisibility(View.GONE);
            }
        });

        // Observe filter saat ini untuk update title
        taskViewModel.getCurrentFilter().observe(this, filter -> {
            switch (filter) {
                case ALL:
                    textViewTitle.setText(R.string.filter_all_tasks);
                    break;
                case ACTIVE:
                    textViewTitle.setText(R.string.filter_active_tasks);
                    break;
                case COMPLETED:
                    textViewTitle.setText(R.string.filter_completed_tasks);
                    break;
            }
        });

        // Observe statistik
        taskViewModel.getActiveTaskCount().observe(this, count -> {
            textViewActiveCount.setText(String.valueOf(count != null ? count : 0));
        });

        taskViewModel.getCompletedTaskCount().observe(this, count -> {
            textViewCompletedCount.setText(String.valueOf(count != null ? count : 0));
        });

        taskViewModel.getOverdueTaskCount().observe(this, count -> {
            textViewOverdueCount.setText(String.valueOf(count != null ? count : 0));
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_all) {
                taskViewModel.setFilter(TaskViewModel.FilterType.ALL);
                return true;
            } else if (itemId == R.id.nav_active) {
                taskViewModel.setFilter(TaskViewModel.FilterType.ACTIVE);
                return true;
            } else if (itemId == R.id.nav_completed) {
                taskViewModel.setFilter(TaskViewModel.FilterType.COMPLETED);
                return true;
            }
            
            return false;
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.nav_all);
    }

    private void setupSearch() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskViewModel.setSearchQuery(s.toString());
                buttonClearSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonClearSearch.setOnClickListener(v -> {
            editTextSearch.setText("");
            taskViewModel.setSearchQuery("");
        });
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab_add_task);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });
    }

    private void showDeleteConfirmationDialog(TaskEntity task) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_task)
                .setMessage(getString(R.string.delete_confirmation_message, task.getTitle()))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    taskViewModel.delete(task);
                    showUndoSnackbar();
                })
                .setNegativeButton(R.string.cancel, null)
                .setIcon(R.drawable.ic_delete)
                .show();
    }

    private void showUndoSnackbar() {
        if (taskViewModel.canUndo()) {
            TaskEntity deletedTask = taskViewModel.getRecentlyDeletedTask();
            String message = getString(R.string.task_deleted_undo, deletedTask.getTitle());
            
            Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, v -> taskViewModel.undoDelete())
                    .setActionTextColor(getResources().getColor(R.color.secondary, null))
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            long dueDate = data.getLongExtra(AddEditTaskActivity.EXTRA_DUE_DATE, 0);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, TaskEntity.PRIORITY_MEDIUM);
            String category = data.getStringExtra(AddEditTaskActivity.EXTRA_CATEGORY);

            if (requestCode == ADD_TASK_REQUEST) {
                // Buat tugas baru
                TaskEntity task = new TaskEntity(
                        title,
                        description,
                        false,
                        System.currentTimeMillis(),
                        dueDate,
                        priority,
                        category
                );
                taskViewModel.insert(task);
                
                Snackbar.make(rootView, R.string.task_added, Snackbar.LENGTH_SHORT).show();
            } else if (requestCode == EDIT_TASK_REQUEST) {
                // Update tugas yang ada
                int id = data.getIntExtra(AddEditTaskActivity.EXTRA_TASK_ID, -1);
                if (id != -1) {
                    TaskEntity task = new TaskEntity(
                            title,
                            description,
                            data.getBooleanExtra(AddEditTaskActivity.EXTRA_IS_COMPLETED, false),
                            data.getLongExtra(AddEditTaskActivity.EXTRA_CREATED_AT, System.currentTimeMillis()),
                            dueDate,
                            priority,
                            category
                    );
                    task.setId(id);
                    taskViewModel.update(task);
                    
                    Snackbar.make(rootView, R.string.task_updated, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
}
