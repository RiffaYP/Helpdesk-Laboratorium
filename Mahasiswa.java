public class Mahasiswa extends User {
    private String npm;

    public Mahasiswa(String nama, String npm, String username, String password) {
        super(nama, username, password, "Mahasiswa");
        this.npm = npm;
    }

    public String getNpm() {
        return npm;
    }
}