import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Laporan {
    private static int nomorBerikutnya = 1;

    private int idLaporan;
    private String namaMahasiswa;
    private String npmMahasiswa;
    private String judulMasalah;
    private String lokasi;
    private String kategori;
    private String deskripsi;
    private String status;
    private String namaTeknisi;
    private String prioritas;
    private String catatanPenanganan;

    private String tanggalLaporan;
    private String tanggalPenugasan;
    private String tanggalDiproses;
    private String tanggalSelesai;

    public Laporan(String namaMahasiswa, String npmMahasiswa, String judulMasalah,
                   String lokasi, String kategori, String deskripsi) {
        this.idLaporan = nomorBerikutnya++;
        this.namaMahasiswa = namaMahasiswa;
        this.npmMahasiswa = npmMahasiswa;
        this.judulMasalah = judulMasalah;
        this.lokasi = lokasi;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.status = "Baru";
        this.namaTeknisi = "Belum ditugaskan";
        this.prioritas = "Belum ditentukan";
        this.catatanPenanganan = "-";

        this.tanggalLaporan = waktuSekarang();
        this.tanggalPenugasan = "-";
        this.tanggalDiproses = "-";
        this.tanggalSelesai = "-";
    }

    public static String waktuSekarang() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.now().format(format);
    }

    public int getIdLaporan() {
        return idLaporan;
    }

    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }

    public String getNpmMahasiswa() {
        return npmMahasiswa;
    }

    public String getJudulMasalah() {
        return judulMasalah;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getKategori() {
        return kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public String getNamaTeknisi() {
        return namaTeknisi;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public String getCatatanPenanganan() {
        return catatanPenanganan;
    }

    public String getTanggalLaporan() {
        return tanggalLaporan;
    }

    public String getTanggalPenugasan() {
        return tanggalPenugasan;
    }

    public String getTanggalDiproses() {
        return tanggalDiproses;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNamaTeknisi(String namaTeknisi) {
        this.namaTeknisi = namaTeknisi;
    }

    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }

    public void setCatatanPenanganan(String catatanPenanganan) {
        this.catatanPenanganan = catatanPenanganan;
    }

    public void setTanggalPenugasan(String tanggalPenugasan) {
        this.tanggalPenugasan = tanggalPenugasan;
    }

    public void setTanggalDiproses(String tanggalDiproses) {
        this.tanggalDiproses = tanggalDiproses;
    }

    public void setTanggalSelesai(String tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getDetailLaporan() {
        return "ID Laporan              : " + idLaporan +
               "\nNama Mahasiswa       : " + namaMahasiswa +
               "\nNPM Mahasiswa        : " + npmMahasiswa +
               "\nJudul Masalah          : " + judulMasalah +
               "\nLokasi                       : " + lokasi +
               "\nKategori                   : " + kategori +
               "\nDeskripsi                  : " + deskripsi +
               "\nStatus                       : " + status +
               "\nTeknisi                     : " + namaTeknisi +
               "\nPrioritas                  : " + prioritas +
               "\nCatatan Penanganan : " + catatanPenanganan +
               "\nTanggal Laporan      : " + tanggalLaporan +
               "\nTanggal Penugasan : " + tanggalPenugasan +
               "\nTanggal Diproses     : " + tanggalDiproses +
               "\nTanggal Selesai        : " + tanggalSelesai;
    }
}