# Changelog

Semua perubahan penting pada project ini akan didokumentasikan di file ini.

Format berdasarkan [Keep a Changelog](https://keepachangelog.com/id-ID/1.0.0/),
dan project ini mengikuti [Semantic Versioning](https://semver.org/lang/id/).

## [1.0.0] - 2024-12-21

### Ditambahkan
- ✨ Fitur CRUD tugas (Create, Read, Update, Delete)
- ⭐ Sistem prioritas 3 level (Tinggi, Sedang, Rendah)
- 🏷️ Kategori tugas dengan autocomplete
- 📅 Tenggat waktu dengan DatePicker
- 🔍 Pencarian tugas berdasarkan judul/deskripsi
- 📊 Dashboard statistik (Aktif, Selesai, Terlambat)
- ↩️ Undo delete dengan Snackbar
- 🌙 Dark mode otomatis mengikuti sistem
- 🔽 Filter tugas (Semua, Aktif, Selesai)
- 📱 Bottom Navigation untuk navigasi filter
- 💾 Penyimpanan lokal dengan Room Database
- 🎨 Material Design 3 theming
- 📝 Data contoh saat instalasi pertama

### Teknologi
- Java 17
- Android SDK 24+
- Room Database 2.6.1
- ViewModel + LiveData
- Material Design 3
- RecyclerView + DiffUtil

---

## Template untuk Rilis Selanjutnya

```markdown
## [X.Y.Z] - YYYY-MM-DD

### Ditambahkan
- Fitur baru

### Diubah
- Perubahan pada fitur yang ada

### Diperbaiki
- Perbaikan bug

### Dihapus
- Fitur yang dihapus

### Keamanan
- Perbaikan keamanan
```
