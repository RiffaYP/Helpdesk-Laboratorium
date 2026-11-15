import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final java.util.ArrayList<User> daftarUser;
    private final java.util.ArrayList<Laporan> daftarLaporan;

    private final JTextField txtUsername = Ui.textField();
    private final JPasswordField txtPassword = Ui.passwordField();
    private final JLabel lblError = new JLabel(" ");

    public LoginFrame(java.util.ArrayList<User> daftarUser, java.util.ArrayList<Laporan> daftarLaporan) {
        this.daftarUser = daftarUser;
        this.daftarLaporan = daftarLaporan;

        setTitle("Login — Help Desk Laboratorium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 560);
        setMinimumSize(new Dimension(820, 520));
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

        JLabel brand = new JLabel("HelpDesk");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 34));
        brand.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("<html>Pusat layanan & pelaporan<br>kendala laboratorium komputer.</html>");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tagline.setForeground(Theme.SIDEBAR_TEXT);

        JLabel hint = new JLabel("<html><span style='color:#94a3b8'>Kelola laporan, penugasan teknisi,<br>dan status penanganan dalam satu tempat.</span></html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        text.add(brand);
        text.add(Box.createVerticalStrut(14));
        text.add(tagline);
        text.add(Box.createVerticalStrut(28));
        text.add(hint);
        hero.add(text);
        return hero;
    }

    private JComponent buildForm() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Theme.BG);

        JPanel card = Ui.card(new BorderLayout(0, 16));
        card.setPreferredSize(new Dimension(360, 380));

        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        JLabel title = Ui.h2("Masuk ke akun");
        JLabel sub = Ui.muted("Gunakan kredensial akun laboratorium Anda.");
        head.add(title);
        head.add(Box.createVerticalStrut(4));
        head.add(sub);
        card.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.gridx = 0;
        g.insets = new Insets(0, 0, 8, 0);

        g.gridy = 0; form.add(Ui.fieldLabel("Username"), g);
        g.gridy = 1; g.insets = new Insets(0, 0, 14, 0); form.add(txtUsername, g);
        g.gridy = 2; g.insets = new Insets(0, 0, 8, 0); form.add(Ui.fieldLabel("Password"), g);
        g.gridy = 3; g.insets = new Insets(0, 0, 6, 0); form.add(txtPassword, g);

        lblError.setFont(Theme.FONT_SMALL);
        lblError.setForeground(Theme.DANGER);
        g.gridy = 4; g.insets = new Insets(0, 0, 10, 0); form.add(lblError, g);

        JButton login = Ui.primary("Masuk");
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        g.gridy = 5; g.insets = new Insets(4, 0, 0, 0);
        form.add(login, g);
        card.add(form, BorderLayout.CENTER);

        JButton register = Ui.ghost("Belum punya akun? Daftar sebagai mahasiswa");
        register.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(register, BorderLayout.SOUTH);

        login.addActionListener(e -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocusInWindow());
        register.addActionListener(e -> {
            new RegisterFrame(daftarUser, daftarLaporan);
            dispose();
        });

        right.add(card);
        return right;
    }

    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username dan password wajib diisi.");
            return;
        }

        for (User user : daftarUser) {
            if (user.cekLogin(username, password)) {
                lblError.setText(" ");
                new DashboardFrame(user, daftarUser, daftarLaporan);
                dispose();
                return;
            }
        }
        lblError.setText("Username atau password salah.");
    }
}
