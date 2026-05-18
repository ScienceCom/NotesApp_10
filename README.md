Strategi & Implementasi Testing
Proyek ini menerapkan pengujian ketat untuk memastikan stabilitas kode dengan target Code Coverage komponen logika bisnis (ViewModel & Repository) minimal 60%.

1. Flow Testing dengan Turbine (Layer ViewModel)
Pengujian aliran data asinkronus menggunakan Turbine untuk memastikan emisi data dari StateFlow berjalan lancar tanpa hambatan threading. Menggunakan UnconfinedTestDispatcher() guna menghindari isu blocking coroutine.

Skenario pengujian meliputi:

Test Case 1 (test5_NotesFlow_EmitsInitialStateAndUpdatedData): Memastikan Flow memancarkan state awal (kosong) dan berhasil memancarkan data terbaru setelah pemicu (loadNotes()) dipanggil.

Test Case 2 (test6_NotesFlow_ShouldBeEmpty_WhenRepositoryIsEmpty): Memastikan Flow mengembalikan daftar kosong secara valid ketika repository tidak memiliki data.

2. UI Testing (Layer NotesScreen)
Pengujian visual dan fungsionalitas UI Compose ditempatkan pada direktori androidInstrumentedTest dengan cakupan minimal 3 kasus uji:

Test Case 1 (test1_NotesList_ShouldDisplayNotes): Memastikan item catatan (Judul & Konten) muncul dengan benar di layar saat list berisi data.

Test Case 2 (test2_AddNoteForm_ShouldAcceptInput): Mensimulasikan input teks pada form judul/konten dan memastikan tombol "Simpan" meneruskan data secara valid.

Test Case 3 (test3_EmptyNotes_ShouldShowEmptyMessage): Memastikan pesan fallback/placeholder seperti "Tidak ada catatan" muncul jika list data kosong.

⚙️ Cara Menjalankan Aplikasi & Pengujian
Menjalankan Aplikasi
Pilih konfigurasi run composeApp di Android Studio dan klik tombol Run, atau gunakan gradle command:

Bash
./gradlew :composeApp:assembleDebug
Menjalankan Unit & Flow Test
Untuk mengeksekusi seluruh pengujian logika bisnis pada ViewModel dan Repository, jalankan perintah berikut:

Bash
./gradlew :composeApp:testDebugUnitTest
Menjalankan UI Test Compose
Pastikan emulator atau perangkat Android fisik dalam kondisi aktif dan terhubung, kemudian jalankan:

Bash
./gradlew :composeApp:connectedDebugAndroidTest
Memeriksa Code Coverage (Batas Minimum 60%)
Untuk melihat persentase cakupan kode logika bisnis:

Klik kanan pada paket test atau class test utama (misal NotesViewModelTest).

Pilih opsi "Run 'NotesViewModelTest' with Coverage".

Hasil presentase baris (Line %) dan metode (Method %) akan muncul pada panel Coverage Android Studio.
