import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final java.util.ArrayList<User> daftarUser;
    private final java.util.ArrayList<Laporan> daftarLaporan;

    private final JTextField txtNama = Ui.textField();
    private final JTextField txtNpm = Ui.textField();
    private final JTextField txtUsername = Ui.textField();
    private final JPasswordField txtPassword = Ui.passwordField();
    private final JPasswordField txtKonfirmasi = Ui.passwordField();
    private final JLabel lblError = new JLabel(" ");

    public RegisterFrame(java.util.ArrayList<User> daftarUser, java.util.ArrayList<Laporan> daftarLaporan) {
        this.daftarUser = daftarUser;
        this.daftarLaporan = daftarLaporan;

        setTitle("Registrasi Mahasiswa — Help Desk Laboratorium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setMinimumSize(new Dimension(820, 560));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        add(buildHero());
        add(buildForm());
        setVisible(true);
    }

    private JComponent buildHero() {
        JPanel hero = new JPanel(new GridBagLayout());
        hero.setBackground(Theme.SIDEBAR);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBorder(new EmptyBorder(0, 44, 0, 44));

        JLabel brand = new JLabel("Buat Akun");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 30));
        brand.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("<html>Daftarkan diri sebagai mahasiswa<br>untuk melaporkan kendala laboratorium.</html>");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tagline.setForeground(Theme.SIDEBAR_TEXT);

        text.add(brand);
        text.add(Box.createVerticalStrut(14));
        text.add(tagline);
        hero.add(text);
        return hero;
    }

    private JComponent buildForm() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Theme.BG);

        JPanel card = Ui.card(new BorderLayout(0, 14));
        card.setPreferredSize(new Dimension(380, 500));

        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(Ui.h2("Registrasi Mahasiswa"));
        head.add(Box.createVerticalStrut(4));
        head.add(Ui.muted("Semua kolom wajib diisi."));
        card.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 6, 0);

        int y = 0;
        y = addField(form, g, y, "Nama Lengkap", txtNama);
        y = addField(form, g, y, "NPM", txtNpm);
        y = addField(form, g, y, "Username", txtUsername);
        y = addField(form, g, y, "Password", txtPassword);
        y = addField(form, g, y, "Konfirmasi Password", txtKonfirmasi);

        lblError.setFont(Theme.FONT_SMALL);
        lblError.setForeground(Theme.DANGER);
        g.gridy = y++;
        g.insets = new Insets(0, 0, 10, 0);
        form.add(lblError, g);

        JButton daftar = Ui.success("Daftar Sekarang");
        JButton kembali = Ui.neutral("Kembali ke Login");
        JPanel btns = new JPanel(new GridLayout(1, 2, 8, 0));
        btns.setOpaque(false);
        btns.add(kembali);
        btns.add(daftar);
        g.gridy = y;
        g.insets = new Insets(4, 0, 0, 0);
        form.add(btns, g);

        card.add(form, BorderLayout.CENTER);

        daftar.addActionListener(e -> prosesRegister());
        kembali.addActionListener(e -> {
            new LoginFrame(daftarUser, daftarLaporan);
            dispose();
        });

        right.add(card);
        return right;
    }

    private int addField(JPanel form, GridBagConstraints g, int y, String label, JComponent field) {
        g.gridy = y++;
        g.insets = new Insets(0, 0, 4, 0);
        form.add(Ui.fieldLabel(label), g);
        g.gridy = y++;
        g.insets = new Insets(0, 0, 10, 0);
        form.add(field, g);
        return y;
    }

    private void prosesRegister() {
        String nama = txtNama.getText().trim();
        String npm = txtNpm.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String konfirmasi = new String(txtKonfirmasi.getPassword());

        if (nama.isEmpty() || npm.isEmpty() || username.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            lblError.setText("Semua data harus diisi.");
            return;
        }
        if (!password.equals(konfirmasi)) {
            lblError.setText("Konfirmasi password tidak sesuai.");
            return;
        }

        for (User user : daftarUser) {
            if (user.getUsername().equals(username)) {
                lblError.setText("Username sudah digunakan.");
                return;
            }
            if (user instanceof Mahasiswa && ((Mahasiswa) user).getNpm().equals(npm)) {
                lblError.setText("NPM sudah terdaftar.");
                return;
            }
        }

        daftarUser.add(new Mahasiswa(nama, npm, username, password));
        JOptionPane.showMessageDialog(this, "Registrasi berhasil. Silakan login.");
        new LoginFrame(daftarUser, daftarLaporan);
        dispose();
    }
}
