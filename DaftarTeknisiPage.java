import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DaftarTeknisiPage implements Refreshable {
    private final ArrayList<User> daftarUser;
    private final JPanel root = new JPanel();
    private final JPanel grid = new JPanel(new GridLayout(0, 2, Theme.GAP, Theme.GAP));

    public DaftarTeknisiPage(ArrayList<User> daftarUser) {
        this.daftarUser = daftarUser;
    }

    public JComponent build() {
        root.setLayout(new BorderLayout(0, Theme.GAP));
        root.setBackground(Theme.BG);

        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(Ui.h3("Daftar Teknisi"));
        head.add(Box.createVerticalStrut(3));
        head.add(Ui.muted("Teknisi yang dapat ditugaskan pada laporan."));
        root.add(head, BorderLayout.NORTH);

        grid.setOpaque(false);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Theme.BG);
        wrap.add(grid, BorderLayout.NORTH);
        root.add(wrap, BorderLayout.CENTER);

        refresh();
        return root;
    }

    @Override
    public void refresh() {
        grid.removeAll();
        for (User u : daftarUser) {
            if (!(u instanceof Teknisi)) continue;
            grid.add(teknisiCard(u));
        }
        grid.revalidate();
        grid.repaint();
    }

    private JComponent teknisiCard(User u) {
        JPanel card = Ui.card(new BorderLayout(12, 0));

        JLabel avatar = new JLabel(initial(u.getNama()), SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY_SOFT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        avatar.setForeground(Theme.PRIMARY);
        avatar.setPreferredSize(new Dimension(48, 48));
        card.add(avatar, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel nama = new JLabel(u.getNama());
        nama.setFont(Theme.FONT_BOLD);
        nama.setForeground(Theme.TEXT);
        JLabel user = Ui.muted("@" + u.getUsername());
        info.add(nama);
        info.add(Box.createVerticalStrut(3));
        info.add(user);
        card.add(info, BorderLayout.CENTER);

        card.add(Ui.badge(u.getRole(), Theme.statusFg("Diproses"), Theme.PRIMARY_SOFT), BorderLayout.EAST);
        return card;
    }

    private String initial(String nama) {
        String[] parts = nama.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) return "?";
        String first = parts[0].substring(0, 1);
        String second = parts.length > 1 ? parts[1].substring(0, 1) : "";
        return (first + second).toUpperCase();
    }
}
