# GoFood Profit Calculator — Android v1

Proyek Android native (Kotlin + Jetpack Compose), berjalan offline.

Fitur: input harga jual, modal, diskon menu merchant, nilai voucher, persentase subsidi, komisi yang dapat diedit, pilihan dasar komisi, estimasi uang bersih, keuntungan dan margin. Preset subsidi 20% (minimum Rp35.000; 21–30 November) dan 25% (minimum Rp40.000; 1–31 Desember).

## Membuat APK
1. Ekstrak ZIP dan buka folder proyek di Android Studio.
2. Tunggu Gradle Sync (memerlukan internet untuk mengunduh dependensi).
3. Pilih Build > Build Bundle(s) / APK(s) > Build APK(s).
4. APK debug ada di `app/build/outputs/apk/debug/app-debug.apk`.

## Asumsi kalkulasi
Subsidi dihitung sebagai bagian dari nominal voucher; diskon menu dianggap ditanggung merchant. Komisi bisa dihitung dari harga jual atau setelah diskon. Belum termasuk biaya tetap/biaya lain. Angka merupakan simulasi, bukan integrasi resmi dengan GoFood.
