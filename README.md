# 📝 Daftar Tugas - Android To-Do List App

Aplikasi Daftar Tugas Android yang lengkap dengan arsitektur MVVM, Room Database, dan Material Design 3.

![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Room](https://img.shields.io/badge/Room-Database-blue)
![Material Design](https://img.shields.io/badge/Material%20Design-3-purple)

## ✨ Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| ✅ CRUD Tugas | Tambah, edit, hapus, dan tandai selesai |
| ⭐ Prioritas | 3 level: Tinggi, Sedang, Rendah |
| 🏷️ Kategori | Organisasi tugas berdasarkan kategori |
| 📅 Tenggat | Atur tanggal jatuh tempo dengan DatePicker |
| 🔍 Pencarian | Cari tugas berdasarkan judul/deskripsi |
| 📊 Statistik | Dashboard dengan jumlah tugas aktif, selesai, terlambat |
| ↩️ Undo Delete | Batalkan penghapusan dengan Snackbar |
| 🌙 Dark Mode | Tema gelap otomatis mengikuti sistem |
| 🔽 Filter | Filter tugas: Semua, Aktif, Selesai |

## 📱 Screenshot

| Light Mode | Dark Mode |
|------------|-----------|
| Tampilan terang | Tampilan gelap |

## 🏗️ Arsitektur

Aplikasi menggunakan arsitektur **MVVM (Model-View-ViewModel)** yang direkomendasikan oleh Google.

```
┌─────────────────────────────────────────────────────┐
│                      UI Layer                        │
│  ┌───────────────┐  ┌───────────────┐               │
│  │ MainActivity  │  │ AddEditTask   │               │
│  │               │  │ Activity      │               │
│  └───────┬───────┘  └───────┬───────┘               │
│          │                  │                        │
│          └────────┬─────────┘                        │
│                   ▼                                  │
│          ┌───────────────┐                          │
│          │ TaskViewModel │                          │
│          └───────┬───────┘                          │
└──────────────────┼──────────────────────────────────┘
                   │
┌──────────────────┼──────────────────────────────────┐
│                  ▼           Data Layer              │
│          ┌───────────────┐                          │
│          │TaskRepository │                          │
│          └───────┬───────┘                          │
│                  │                                   │
│                  ▼                                   │
│          ┌───────────────┐                          │
│          │   TaskDao     │                          │
│          └───────┬───────┘                          │
│                  │                                   │
│                  ▼                                   │
│          ┌───────────────┐                          │
│          │  AppDatabase  │ (Room)                   │
│          └───────────────┘                          │
└─────────────────────────────────────────────────────┘
```

## 📂 Struktur Project

```
app/src/main/
├── java/com/example/todolist/
│   ├── data/
│   │   ├── dao/
│   │   │   └── TaskDao.java          # Data Access Object
│   │   ├── database/
│   │   │   └── AppDatabase.java      # Room Database
│   │   ├── entity/
│   │   │   └── TaskEntity.java       # Entity/Model
│   │   └── repository/
│   │       └── TaskRepository.java   # Repository Pattern
│   └── ui/
│       ├── adapter/
│       │   └── TaskAdapter.java      # RecyclerView Adapter
│       ├── viewmodel/
│       │   └── TaskViewModel.java    # ViewModel
│       ├── MainActivity.java         # Main Screen
│       └── AddEditTaskActivity.java  # Add/Edit Screen
└── res/
    ├── layout/                       # XML Layouts
    ├── values/                       # Colors, Strings, Themes
    ├── values-night/                 # Dark Mode Colors
    ├── drawable/                     # Icons & Drawables
    └── menu/                         # Menu Resources
```

## 🔧 Tech Stack

| Teknologi | Versi | Kegunaan |
|-----------|-------|----------|
| Android SDK | 24+ | Min API Level |
| Java | 17 | Bahasa Pemrograman |
| Room | 2.6.1 | Local Database |
| LiveData | - | Observable Data |
| ViewModel | - | UI State Management |
| Material Design 3 | - | UI Components |
| RecyclerView | - | List Display |
| CardView | - | Card Layout |

## 📦 Dependencies

```gradle
dependencies {
    // Room Database
    implementation "androidx.room:room-runtime:2.6.1"
    annotationProcessor "androidx.room:room-compiler:2.6.1"
    
    // Lifecycle (ViewModel + LiveData)
    implementation "androidx.lifecycle:lifecycle-viewmodel:2.7.0"
    implementation "androidx.lifecycle:lifecycle-livedata:2.7.0"
    
    // UI Components
    implementation "com.google.android.material:material:1.11.0"
    implementation "androidx.recyclerview:recyclerview:1.3.2"
    implementation "androidx.cardview:cardview:1.0.0"
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
}
```

## 🚀 Instalasi

### Prerequisites
- Android Studio Hedgehog atau lebih baru
- JDK 17
- Android SDK 24+
- Gradle 8.2

### Langkah-langkah

1. **Clone repository**
   ```bash
   git clone https://github.com/username/daftar-tugas-android.git
   cd daftar-tugas-android
   ```

2. **Buka di Android Studio**
   ```
   File > Open > Pilih folder project
   ```

3. **Sync Gradle**
   ```
   Klik "Sync Now" saat muncul notifikasi
   ```

4. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   # atau tekan Shift+F10 di Android Studio
   ```

## 📖 Penggunaan

### Menambah Tugas
1. Ketuk tombol **+** (FAB) di pojok kanan bawah
2. Isi judul tugas (wajib)
3. Isi deskripsi (opsional)
4. Pilih kategori dari dropdown
5. Pilih prioritas: Tinggi/Sedang/Rendah
6. Pilih tanggal jatuh tempo (opsional)
7. Ketuk ikon ✓ untuk menyimpan

### Filter Tugas
Gunakan **Bottom Navigation Bar**:
- **Semua** - Tampilkan semua tugas
- **Aktif** - Tampilkan tugas yang belum selesai
- **Selesai** - Tampilkan tugas yang sudah selesai

### Pencarian
Ketik kata kunci di **Search Bar** untuk mencari tugas berdasarkan judul atau deskripsi.

### Hapus Tugas
1. Tekan lama pada tugas yang ingin dihapus
2. Konfirmasi penghapusan
3. Ketuk **"Urungkan"** di Snackbar untuk membatalkan

## 🗄️ Database Schema

### Tabel: tasks

| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| title | TEXT | Judul tugas |
| description | TEXT | Deskripsi tugas |
| is_completed | INTEGER | Status selesai (0/1) |
| created_at | INTEGER | Timestamp pembuatan |
| due_date | INTEGER | Timestamp tenggat (0 = tanpa tenggat) |
| priority | INTEGER | Prioritas (0=Rendah, 1=Sedang, 2=Tinggi) |
| category | TEXT | Nama kategori |

## 🎨 Theme Colors

### Light Mode
| Color | Hex | Usage |
|-------|-----|-------|
| Primary | `#6200EE` | Toolbar, FAB, Accent |
| Background | `#F5F5F5` | Screen Background |
| Surface | `#FFFFFF` | Cards, Dialogs |
| Error | `#F44336` | Overdue, Delete |

### Dark Mode
| Color | Hex | Usage |
|-------|-----|-------|
| Primary | `#BB86FC` | Toolbar, FAB, Accent |
| Background | `#121212` | Screen Background |
| Surface | `#1E1E1E` | Cards, Dialogs |
| Error | `#EF5350` | Overdue, Delete |

## 🧪 Testing

```bash
# Unit Tests
./gradlew test

# Instrumented Tests
./gradlew connectedAndroidTest

# Build APK
./gradlew assembleDebug
```

## 📄 License

```
MIT License

Copyright (c) 2024

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🤝 Contributing

1. Fork repository ini
2. Buat branch fitur (`git checkout -b fitur/FiturBaru`)
3. Commit perubahan (`git commit -m 'Tambah fitur baru'`)
4. Push ke branch (`git push origin fitur/FiturBaru`)
5. Buat Pull Request

## 📞 Kontak

- **Email**: developer@example.com
- **GitHub**: [@username](https://github.com/username)

---

⭐ **Jangan lupa beri bintang jika project ini membantu!**
