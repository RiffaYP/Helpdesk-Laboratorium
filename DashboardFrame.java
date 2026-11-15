import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardFrame extends JFrame {
    private final User user;
    private final ArrayList<User> daftarUser;
    private final ArrayList<Laporan> daftarLaporan;

    private final CardLayout pages = new CardLayout();
    private final JPanel pageHost = new JPanel(pages);
    private final JLabel lblPageTitle = Ui.h2("");
    private final List<NavItem> navItems = new ArrayList<>();
    private final Map<String, JComponent> pageCache = new LinkedHashMap<>();
    private Runnable onDataChange = () -> {};

    public DashboardFrame(User user, ArrayList<User> daftarUser, ArrayList<Laporan> daftarLaporan) {
        this.user = user;
        this.daftarUser = daftarUser;
        this.daftarLaporan = daftarLaporan;

        setTitle("Help Desk Laboratorium — " + user.getRole());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(1000, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        pageHost.setBackground(Theme.BG);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildMain(), BorderLayout.CENTER);

        buildPagesForRole();
        if (!navItems.isEmpty()) navigate(navItems.get(0).key);

        setVisible(true);
    }

    private JComponent buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Theme.SIDEBAR);
        side.setPreferredSize(new Dimension(232, 0));
        side.setBorder(new EmptyBorder(22, 16, 22, 16));

        JLabel brand = new JLabel("HelpDesk");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Lab Center");
        sub.setFont(Theme.FONT_SMALL);
        sub.setForeground(Theme.SIDEBAR_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(brand);
        side.add(Box.createVerticalStrut(2));
        side.add(sub);
        side.add(Box.createVerticalStrut(28));

        side.add(navItem("dashboard", "Dashboard"));
        side.add(Box.createVerticalStrut(6));

        if (user instanceof Mahasiswa) {
            side.add(navItem("buat", "Buat Laporan"));
        } else if (user instanceof AdminLab) {
            side.add(navItem("kelola", "Kelola Laporan"));
            side.add(Box.createVerticalStrut(6));
            side.add(navItem("teknisi", "Daftar Teknisi"));
        } else if (user instanceof Teknisi) {
            side.add(navItem("tugas", "Tugas Saya"));
        }

        side.add(Box.createVerticalGlue());

        JPanel profile = new JPanel();
        profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
        profile.setOpaque(false);
        profile.setAlignmentX(Component.LEFT_ALIGNMENT);
        profile.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.SIDEBAR_HOVER),
                new EmptyBorder(16, 2, 10, 2)));

        JLabel nama = new JLabel(user.getNama());
        nama.setFont(Theme.FONT_BOLD);
        nama.setForeground(Color.WHITE);
        nama.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel role = new JLabel(user.getRole());
        role.setFont(Theme.FONT_SMALL);
        role.setForeground(Theme.SIDEBAR_MUTED);
        role.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton logout = Ui.danger("Logout");
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        logout.addActionListener(e -> {
            new LoginFrame(daftarUser, daftarLaporan);
            dispose();
        });

        profile.add(nama);
        profile.add(Box.createVerticalStrut(2));
        profile.add(role);
        profile.add(Box.createVerticalStrut(14));
        profile.add(logout);
        side.add(profile);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.add(side, BorderLayout.CENTER);
        return side;
    }

    private JComponent navItem(String key, String label) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        item.setOpaque(false);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel text = new JLabel(label);
        text.setFont(Theme.FONT_BOLD);
        text.setForeground(Theme.SIDEBAR_TEXT);
        item.add(text);

        NavItem nav = new NavItem(key, item, text);
        navItems.add(nav);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { navigate(key); }
            @Override public void mouseEntered(MouseEvent e) {
                if (!nav.active) item.setOpaque(true);
                if (!nav.active) { item.setBackground(Theme.SIDEBAR_HOVER); item.repaint(); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (!nav.active) { item.setOpaque(false); item.repaint(); }
            }
        });
        return item;
    }

    private JComponent buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);

        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(Theme.CARD);
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(16, 24, 16, 24)));

        lblPageTitle.setFont(Theme.FONT_H2);
        JLabel crumb = Ui.muted("Dashboard / " + user.getRole());
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(lblPageTitle);
        titles.add(crumb);
        topbar.add(titles, BorderLayout.WEST);

        JLabel hello = Ui.muted("Halo, " + user.getNama());
        topbar.add(hello, BorderLayout.EAST);

        main.add(topbar, BorderLayout.NORTH);

        JPanel hostWrap = new JPanel(new BorderLayout());
        hostWrap.setBackground(Theme.BG);
        hostWrap.setBorder(new EmptyBorder(20, 24, 20, 24));
        hostWrap.add(pageHost, BorderLayout.CENTER);
        main.add(hostWrap, BorderLayout.CENTER);
        return main;
    }

    private void buildPagesForRole() {
        addPage("dashboard", "Dashboard", new DashboardPage(user, daftarLaporan).build());

        if (user instanceof Mahasiswa) {
            addPage("buat", "Buat Laporan",
                    new BuatLaporanPage((Mahasiswa) user, daftarLaporan, this::dataChanged).build());
        } else if (user instanceof AdminLab) {
            addPage("kelola", "Kelola Laporan",
                    new KelolaLaporanPage(daftarUser, daftarLaporan, this::dataChanged).build());
            addPage("teknisi", "Daftar Teknisi",
                    new DaftarTeknisiPage(daftarUser).build());
        } else if (user instanceof Teknisi) {
            addPage("tugas", "Tugas Saya",
                    new TugasTeknisiPage(user, daftarLaporan, this::dataChanged).build());
        }
    }

    private void addPage(String key, String title, JComponent page) {
        page.setName(title);
        page.setBackground(Theme.BG);
        page.setBorder(new EmptyBorder(0, 0, 0, 0));
        pageCache.put(key, page);

        JPanel host = new JPanel(new BorderLayout());
        host.setBackground(Theme.BG);
        host.add(page, BorderLayout.NORTH);

        ScrollableHost wrap = new ScrollableHost(host);

        JScrollPane sp = new JScrollPane(wrap,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.getViewport().setBackground(Theme.BG);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        sp.getVerticalScrollBar().setBlockIncrement(120);
        sp.setName(title);
        pageHost.add(sp, key);
    }

    private void navigate(String key) {
        for (NavItem item : navItems) {
            item.setActive(key.equals(item.key));
        }
        pages.show(pageHost, key);
        JComponent page = pageCache.get(key);
        if (page instanceof Refreshable) {
            ((Refreshable) page).refresh();
        }
        updateTitleFor(key);
    }

    private void updateTitleFor(String key) {
        JComponent page = pageCache.get(key);
        lblPageTitle.setText(page != null && page.getName() != null ? page.getName() : "Dashboard");
    }

    private void dataChanged() {
        onDataChange.run();
        for (JComponent page : pageCache.values()) {
            if (page instanceof Refreshable) ((Refreshable) page).refresh();
        }
    }

    private static class NavItem {
        final String key;
        final JPanel panel;
        final JLabel label;
        boolean active;

        NavItem(String key, JPanel panel, JLabel label) {
            this.key = key;
            this.panel = panel;
            this.label = label;
        }

        void setActive(boolean active) {
            this.active = active;
            if (active) {
                panel.setOpaque(true);
                panel.setBackground(Theme.PRIMARY);
                label.setForeground(Color.WHITE);
            } else {
                panel.setOpaque(false);
                label.setForeground(Theme.SIDEBAR_TEXT);
            }
            panel.repaint();
        }
    }

    private static class ScrollableHost extends JPanel implements Scrollable {
        ScrollableHost(JComponent content) {
            super(new BorderLayout());
            setBackground(Theme.BG);
            add(content, BorderLayout.NORTH);
        }

        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 18; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 120; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }
}
