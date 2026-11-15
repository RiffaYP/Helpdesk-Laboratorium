import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Theme.install();

        ArrayList<User> daftarUser = new ArrayList<User>();
        ArrayList<Laporan> daftarLaporan = new ArrayList<Laporan>();

        daftarUser.add(new AdminLab("Admin Laboratorium", "admin", "123"));
        daftarUser.add(new Teknisi("Teknisi Satu", "teknisi1", "123"));
        daftarUser.add(new Teknisi("Teknisi Dua", "teknisi2", "123"));
        daftarUser.add(new Mahasiswa("Riffa Yudika Permana", "2415061091", "riffa", "123"));

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame(daftarUser, daftarLaporan);
            }
        });
    }
}
