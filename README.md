# Help Desk Laboratorium

Sistem pelaporan dan penanganan kendala laboratorium komputer berbasis
Java Swing (desktop).

## Tujuan

Memberi satu tempat untuk:
- Mahasiswa melaporkan kendala perangkat di laboratorium.
- Admin menugaskan teknisi dan mengelola status laporan.
- Teknisi menangani, memprioritaskan, dan menyelesaikan laporan.

## Fitur

- Login multi-role (Admin, Teknisi, Mahasiswa) + registrasi mahasiswa.
- Dashboard dengan ringkasan jumlah laporan (stat cards) dan aktivitas terbaru.
- Mahasiswa: buat laporan (judul, lokasi, kategori, deskripsi) + riwayat.
- Admin: cari/filter/urutkan laporan, tugaskan teknisi, ubah status, daftar teknisi.
- Teknisi: lihat tugas, atur prioritas, tangani, selesaikan, simpan catatan.
- Badge status & prioritas berwarna, navigasi sidebar multi-halaman.

## Cara Menjalankan

Butuh JDK 8 atau lebih baru (diuji pada JDK 21).

```bash
javac -encoding UTF-8 -d . *.java
java -cp . Main
```

Atau buka folder sebagai proyek BlueJ lalu jalankan kelas `Main`.

## Akun Demo

| Username   | Password | Role      |
|------------|----------|-----------|
| admin      | 123      | Admin     |
| teknisi1   | 123      | Teknisi   |
| teknisi2   | 123      | Teknisi   |
| riffa      | 123      | Mahasiswa |

## Struktur Kode

Model:
- `User` — base class (nama, username, password, role) + `cekLogin`.
- `AdminLab`, `Teknisi`, `Mahasiswa` — turunan `User` (Mahasiswa menyimpan NPM).
- `Laporan` — data laporan + status, prioritas, catatan, dan timestamp.

Tampilan / UI:
- `Theme` — palet warna, font, warna status/prioritas, `install()` UI defaults.
- `Ui` — komponen bersama (tombol, field, combo, badge, stat card, tabel).
- `ChipRenderer` — render badge berwarna di dalam tabel.
- `LoginFrame`, `RegisterFrame` — layar autentikasi.
- `DashboardFrame` — shell: sidebar + topbar + halaman (CardLayout) + scroll.

Halaman dashboard:
- `DashboardPage` — ringkasan & aktivitas terbaru.
- `BuatLaporanPage` — form laporan mahasiswa + riwayat.
- `KelolaLaporanPage` — pengelolaan laporan (admin).
- `TugasTeknisiPage` — penanganan tugas (teknisi).
- `DaftarTeknisiPage` — daftar teknisi (admin).

`Main` — entry point: `Theme.install()`, seed data user, buka `LoginFrame`.

## Catatan

- Data disimpan di memori (ArrayList); hilang saat aplikasi ditutup.
- Password masih disimpan plaintext — untuk keperluan pembelajaran.

## Penulis

Riffa Yudika Permana — 2415061091
