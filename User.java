public class User {
    protected String nama;
    protected String username;
    protected String password;
    protected String role;

    public User(String nama, String username, String password, String role) {
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean cekLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}