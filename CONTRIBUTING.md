# Panduan Kontribusi

Terima kasih telah tertarik untuk berkontribusi pada project **Daftar Tugas Android**! 🎉

## 📋 Cara Berkontribusi

### 1. Fork & Clone

```bash
# Fork repository ini melalui GitHub
# Kemudian clone fork Anda
git clone https://github.com/USERNAME_ANDA/daftar-tugas-android.git
cd daftar-tugas-android
```

### 2. Buat Branch

```bash
# Buat branch baru untuk fitur/perbaikan Anda
git checkout -b fitur/nama-fitur
# atau
git checkout -b perbaikan/nama-bug
```

### 3. Lakukan Perubahan

- Pastikan kode mengikuti konvensi yang sudah ada
- Tambahkan komentar jika diperlukan
- Test perubahan Anda

### 4. Commit

```bash
# Stage perubahan
git add .

# Commit dengan pesan yang jelas
git commit -m "feat: tambah fitur reminder notifikasi"
```

#### Format Commit Message

Gunakan format [Conventional Commits](https://www.conventionalcommits.org/):

| Prefix | Kegunaan |
|--------|----------|
| `feat:` | Fitur baru |
| `fix:` | Perbaikan bug |
| `docs:` | Perubahan dokumentasi |
| `style:` | Formatting, tanpa perubahan kode |
| `refactor:` | Refactoring kode |
| `test:` | Menambah/memperbaiki test |
| `chore:` | Maintenance, dependencies |

### 5. Push & Pull Request

```bash
# Push ke fork Anda
git push origin fitur/nama-fitur
```

Kemudian buat Pull Request melalui GitHub.

## 🎨 Coding Style

### Java

- Gunakan **CamelCase** untuk nama class dan method
- Gunakan **snake_case** untuk resource ID
- Maksimal 120 karakter per baris
- Tambahkan Javadoc untuk public methods

```java
/**
 * Menyimpan tugas ke database.
 * @param task Tugas yang akan disimpan
 */
public void insert(TaskEntity task) {
    // implementasi
}
```

### XML Layout

- Gunakan **snake_case** untuk ID: `@+id/text_view_title`
- Gunakan atribut terurut: id, layout, padding, margin, style
- Ekstrak dimensi ke `dimens.xml` jika dipakai berulang

### Resource Naming

| Tipe | Format | Contoh |
|------|--------|--------|
| Layout | `activity_*.xml`, `item_*.xml` | `activity_main.xml` |
| Drawable | `ic_*.xml`, `bg_*.xml` | `ic_search.xml` |
| String | `snake_case` | `add_task` |
| Color | `color_name` | `primary` |

## ✅ Checklist Sebelum PR

- [ ] Kode berhasil di-build tanpa error
- [ ] Tidak ada warning baru
- [ ] Test berjalan dengan baik
- [ ] Dokumentasi diperbarui (jika perlu)
- [ ] Commit message sesuai format
- [ ] Branch up-to-date dengan `main`

## 🐛 Melaporkan Bug

Gunakan [GitHub Issues](https://github.com/username/daftar-tugas-android/issues) dengan template:

```markdown
**Deskripsi Bug**
Penjelasan singkat tentang bug.

**Langkah Reproduksi**
1. Buka aplikasi
2. Klik tombol '...'
3. Lihat error

**Perilaku yang Diharapkan**
Apa yang seharusnya terjadi.

**Screenshot**
Jika ada.

**Informasi Device**
- Device: [Samsung Galaxy S21]
- Android: [Android 13]
- Versi App: [1.0.0]
```

## 💡 Mengusulkan Fitur

Buat issue dengan label `enhancement` dan jelaskan:
- Apa fitur yang diusulkan
- Mengapa fitur ini berguna
- Contoh implementasi (jika ada)

## 📄 Lisensi

Dengan berkontribusi, Anda setuju bahwa kontribusi Anda akan dilisensikan di bawah [MIT License](LICENSE).

---

Terima kasih atas kontribusi Anda! 🙏
