package com.example.todolist.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todolist.data.dao.TaskDao;
import com.example.todolist.data.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AppDatabase adalah kelas utama Room database.
 * Menggunakan pola singleton untuk memastikan hanya ada satu instance.
 */
@Database(entities = {TaskEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "todo_database"
                    )
                    .fallbackToDestructiveMigration()
                    .addCallback(sRoomDatabaseCallback)
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback database untuk mengisi data contoh saat pertama kali dibuat
     */
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Isi database dengan data contoh saat pertama kali dibuat
            databaseWriteExecutor.execute(() -> {
                TaskDao dao = INSTANCE.taskDao();
                
                // Cek apakah database kosong
                if (dao.getTaskCount() == 0) {
                    List<TaskEntity> dummyTasks = createDummyTasks();
                    dao.insertAll(dummyTasks);
                }
            });
        }
    };

    /**
     * Membuat tugas contoh untuk mengisi database awal
     */
    private static List<TaskEntity> createDummyTasks() {
        List<TaskEntity> tasks = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        long oneDay = 86400000L; // 24 jam dalam milidetik

        tasks.add(new TaskEntity(
            "Belajar Android MVVM",
            "Pelajari pola arsitektur ViewModel dan LiveData untuk membangun aplikasi Android yang handal",
            false,
            currentTime,
            currentTime + (oneDay * 3), // Tenggat 3 hari lagi
            TaskEntity.PRIORITY_HIGH,
            "Belajar"
        ));

        tasks.add(new TaskEntity(
            "Setup Database Room",
            "Konfigurasi entity, DAO, dan kelas database untuk penyimpanan data lokal",
            true,
            currentTime - 3600000, // 1 jam lalu
            currentTime - oneDay, // Tenggat kemarin (sudah selesai)
            TaskEntity.PRIORITY_HIGH,
            "Belajar"
        ));

        tasks.add(new TaskEntity(
            "Desain Layout UI",
            "Buat layout Material Design menggunakan ConstraintLayout dan CardView",
            false,
            currentTime - 7200000, // 2 jam lalu
            currentTime + oneDay, // Tenggat besok
            TaskEntity.PRIORITY_MEDIUM,
            "Desain"
        ));

        tasks.add(new TaskEntity(
            "Tulis Unit Test",
            "Test repository dan ViewModel dengan JUnit dan Espresso",
            false,
            currentTime - 10800000, // 3 jam lalu
            currentTime + (oneDay * 7), // Tenggat 1 minggu lagi
            TaskEntity.PRIORITY_LOW,
            "Testing"
        ));

        tasks.add(new TaskEntity(
            "Publikasi ke Play Store",
            "Siapkan build release, buat signing key, dan upload ke Google Play Console",
            false,
            currentTime - 14400000, // 4 jam lalu
            0, // Tanpa tenggat
            TaskEntity.PRIORITY_MEDIUM,
            "Deploy"
        ));

        tasks.add(new TaskEntity(
            "Belanja Bulanan",
            "Beli kebutuhan dapur: beras, minyak, gula, dan bumbu-bumbu",
            false,
            currentTime - 1800000, // 30 menit lalu
            currentTime + (oneDay * 2), // Tenggat 2 hari lagi
            TaskEntity.PRIORITY_LOW,
            "Pribadi"
        ));

        return tasks;
    }
}
