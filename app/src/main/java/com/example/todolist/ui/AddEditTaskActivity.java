package com.example.todolist.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.todolist.R;
import com.example.todolist.data.entity.TaskEntity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * AddEditTaskActivity menangani penambahan tugas baru dan pengeditan tugas yang ada.
 * Mode ditentukan berdasarkan apakah EXTRA_TASK_ID ada di intent.
 */
public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "com.example.todolist.EXTRA_TASK_ID";
    public static final String EXTRA_TITLE = "com.example.todolist.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.todolist.EXTRA_DESCRIPTION";
    public static final String EXTRA_IS_COMPLETED = "com.example.todolist.EXTRA_IS_COMPLETED";
    public static final String EXTRA_CREATED_AT = "com.example.todolist.EXTRA_CREATED_AT";
    public static final String EXTRA_DUE_DATE = "com.example.todolist.EXTRA_DUE_DATE";
    public static final String EXTRA_PRIORITY = "com.example.todolist.EXTRA_PRIORITY";
    public static final String EXTRA_CATEGORY = "com.example.todolist.EXTRA_CATEGORY";

    private TextInputLayout textInputLayoutTitle;
    private TextInputLayout textInputLayoutDescription;
    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private MaterialButton buttonSelectDate;
    private MaterialButton buttonClearDate;
    private TextView textViewSelectedDate;
    private RadioGroup radioGroupPriority;
    private AutoCompleteTextView autoCompleteCategory;

    private int taskId = -1;
    private boolean isCompleted = false;
    private long createdAt = 0;
    private long dueDate = 0;
    private int priority = TaskEntity.PRIORITY_MEDIUM;
    private String category = "";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    // Kategori yang tersedia
    private static final String[] CATEGORIES = {
            "Belajar", "Pekerjaan", "Pribadi", "Belanja", "Kesehatan", 
            "Keuangan", "Keluarga", "Proyek", "Desain", "Testing", "Deploy", "Lainnya"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        initViews();
        setupToolbar();
        setupDatePicker();
        setupPrioritySelection();
        setupCategoryDropdown();
        loadIntentData();
    }

    private void initViews() {
        textInputLayoutTitle = findViewById(R.id.text_input_layout_title);
        textInputLayoutDescription = findViewById(R.id.text_input_layout_description);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonSelectDate = findViewById(R.id.button_select_date);
        buttonClearDate = findViewById(R.id.button_clear_date);
        textViewSelectedDate = findViewById(R.id.text_view_selected_date);
        radioGroupPriority = findViewById(R.id.radio_group_priority);
        autoCompleteCategory = findViewById(R.id.auto_complete_category);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }
    }

    private void setupDatePicker() {
        buttonSelectDate.setOnClickListener(v -> showDatePicker());
        buttonClearDate.setOnClickListener(v -> clearDueDate());
    }

    private void setupPrioritySelection() {
        radioGroupPriority.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_priority_high) {
                priority = TaskEntity.PRIORITY_HIGH;
            } else if (checkedId == R.id.radio_priority_medium) {
                priority = TaskEntity.PRIORITY_MEDIUM;
            } else if (checkedId == R.id.radio_priority_low) {
                priority = TaskEntity.PRIORITY_LOW;
            }
        });
    }

    private void setupCategoryDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                CATEGORIES
        );
        autoCompleteCategory.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        // Jika tanggal jatuh tempo sudah diset, gunakan sebagai default
        if (dueDate > 0) {
            calendar.setTimeInMillis(dueDate);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 23, 59, 59);
                    dueDate = selectedCalendar.getTimeInMillis();
                    updateDueDateDisplay();
                },
                year, month, day
        );

        // Set tanggal minimum ke hari ini
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void clearDueDate() {
        dueDate = 0;
        updateDueDateDisplay();
    }

    private void updateDueDateDisplay() {
        if (dueDate > 0) {
            textViewSelectedDate.setText(dateFormat.format(new Date(dueDate)));
            textViewSelectedDate.setVisibility(View.VISIBLE);
            buttonClearDate.setVisibility(View.VISIBLE);
        } else {
            textViewSelectedDate.setText(R.string.no_due_date);
            textViewSelectedDate.setVisibility(View.VISIBLE);
            buttonClearDate.setVisibility(View.GONE);
        }
    }

    private void updatePrioritySelection() {
        switch (priority) {
            case TaskEntity.PRIORITY_HIGH:
                radioGroupPriority.check(R.id.radio_priority_high);
                break;
            case TaskEntity.PRIORITY_LOW:
                radioGroupPriority.check(R.id.radio_priority_low);
                break;
            case TaskEntity.PRIORITY_MEDIUM:
            default:
                radioGroupPriority.check(R.id.radio_priority_medium);
                break;
        }
    }

    private void loadIntentData() {
        Intent intent = getIntent();
        
        if (intent.hasExtra(EXTRA_TASK_ID)) {
            // Mode edit
            setTitle(R.string.edit_task);
            taskId = intent.getIntExtra(EXTRA_TASK_ID, -1);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            isCompleted = intent.getBooleanExtra(EXTRA_IS_COMPLETED, false);
            createdAt = intent.getLongExtra(EXTRA_CREATED_AT, System.currentTimeMillis());
            dueDate = intent.getLongExtra(EXTRA_DUE_DATE, 0);
            priority = intent.getIntExtra(EXTRA_PRIORITY, TaskEntity.PRIORITY_MEDIUM);
            category = intent.getStringExtra(EXTRA_CATEGORY);
            if (category == null) category = "";
            autoCompleteCategory.setText(category, false);
        } else {
            // Mode tambah
            setTitle(R.string.add_task);
            createdAt = System.currentTimeMillis();
            dueDate = 0;
            priority = TaskEntity.PRIORITY_MEDIUM;
        }
        
        updateDueDateDisplay();
        updatePrioritySelection();
    }

    private boolean validateInput() {
        boolean isValid = true;

        String title = editTextTitle.getText() != null ? 
                editTextTitle.getText().toString().trim() : "";
        
        if (TextUtils.isEmpty(title)) {
            textInputLayoutTitle.setError(getString(R.string.error_title_required));
            isValid = false;
        } else {
            textInputLayoutTitle.setError(null);
        }

        return isValid;
    }

    private void saveTask() {
        if (!validateInput()) {
            return;
        }

        String title = editTextTitle.getText() != null ? 
                editTextTitle.getText().toString().trim() : "";
        String description = editTextDescription.getText() != null ? 
                editTextDescription.getText().toString().trim() : "";
        category = autoCompleteCategory.getText() != null ?
                autoCompleteCategory.getText().toString().trim() : "";

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_TITLE, title);
        resultIntent.putExtra(EXTRA_DESCRIPTION, description);
        resultIntent.putExtra(EXTRA_IS_COMPLETED, isCompleted);
        resultIntent.putExtra(EXTRA_CREATED_AT, createdAt);
        resultIntent.putExtra(EXTRA_DUE_DATE, dueDate);
        resultIntent.putExtra(EXTRA_PRIORITY, priority);
        resultIntent.putExtra(EXTRA_CATEGORY, category);

        if (taskId != -1) {
            resultIntent.putExtra(EXTRA_TASK_ID, taskId);
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == android.R.id.home) {
            // Tutup tanpa menyimpan
            finish();
            return true;
        } else if (itemId == R.id.action_save) {
            saveTask();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
