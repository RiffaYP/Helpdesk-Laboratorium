import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardPage implements Refreshable {
    private final User user;
    private final java.util.ArrayList<Laporan> daftarLaporan;
    private final JPanel root = new JPanel();
    private final JPanel statRow = new JPanel(new GridLayout(1, 4, Theme.GAP, Theme.GAP));
    private final JPanel recentHost = new JPanel(new BorderLayout());
    private boolean built;

    public DashboardPage(User user, java.util.ArrayList<Laporan> daftarLaporan) {
        this.user = user;
        this.daftarLaporan = daftarLaporan;
    }

    public JComponent build() {
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel hero = Ui.card(new BorderLayout(10, 6));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        JLabel welcome = Ui.h2("Selamat datang, " + user.getNama());
        JLabel hint = Ui.muted(hintForRole());
        JPanel heroText = new JPanel();
        heroText.setOpaque(false);
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));
        heroText.add(welcome);
        heroText.add(Box.createVerticalStrut(4));
        heroText.add(hint);
        hero.add(heroText, BorderLayout.CENTER);

        JLabel roleBadge = Ui.badge(user.getRole(), Theme.PRIMARY, Theme.PRIMARY_SOFT);
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        badgeWrap.setOpaque(false);
        badgeWrap.add(roleBadge);
        hero.add(badgeWrap, BorderLayout.EAST);

        root.add(hero);
        root.add(Box.createVerticalStrut(Theme.GAP));

        statRow.setOpaque(false);
        statRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statRow.setPreferredSize(new Dimension(0, 110));
        root.add(statRow);
        root.add(Box.createVerticalStrut(Theme.GAP));

        JPanel recent = Ui.card(new BorderLayout(0, 12));
        recent.add(Ui.h3("Aktivitas Terbaru"), BorderLayout.NORTH);
        recent.add(recentHost, BorderLayout.CENTER);
        root.add(recent);

        built = true;
        refresh();
        return root;
    }

    private String hintForRole() {
        if (user instanceof Mahasiswa) return "Laporkan kendala laboratorium dan pantau statusnya di sini.";
        if (user instanceof AdminLab) return "Pantau laporan masuk, tugaskan teknisi, dan kelola status.";
        return "Tangani laporan yang ditugaskan kepada Anda.";
    }

    @Override
    public void refresh() {
        if (!built) return;
        java.util.List<Laporan> visible = visibleLaporan();

        int baru = 0, proses = 0, selesai = 0;
        for (Laporan l : visible) {
            if ("Selesai".equals(l.getStatus())) selesai++;
            else if ("Diproses".equals(l.getStatus()) || "Ditugaskan".equals(l.getStatus())) proses++;
            else baru++;
        }
        int total = visible.size();

        statRow.removeAll();
        statRow.add(Ui.statCard("Total Laporan", String.valueOf(total), Theme.PRIMARY));
        statRow.add(Ui.statCard("Menunggu / Baru", String.valueOf(baru), Theme.WARNING));
        statRow.add(Ui.statCard("Sedang Diproses", String.valueOf(proses), Theme.PRIMARY));
        statRow.add(Ui.statCard("Selesai", String.valueOf(selesai), Theme.SECONDARY));
        statRow.revalidate();
        statRow.repaint();

        recentHost.removeAll();
        if (visible.isEmpty()) {
            JLabel empty = Ui.muted("Belum ada laporan.");
            empty.setBorder(new EmptyBorder(10, 0, 10, 0));
            recentHost.add(empty, BorderLayout.NORTH);
        } else {
            JPanel list = new JPanel();
            list.setOpaque(false);
            list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
            java.util.List<Laporan> sorted = new java.util.ArrayList<>(visible);
            sorted.sort((a, b) -> b.getIdLaporan() - a.getIdLaporan());
            int limit = Math.min(5, sorted.size());
            for (int i = 0; i < limit; i++) {
                list.add(recentRow(sorted.get(i)));
                list.add(Box.createVerticalStrut(8));
            }
            recentHost.add(list, BorderLayout.NORTH);
        }
        recentHost.revalidate();
        recentHost.repaint();
    }

    private JComponent recentRow(Laporan l) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(0, 2, 10, 2)));

        JLabel title = new JLabel("#" + l.getIdLaporan() + "  " + l.getJudulMasalah());
        title.setFont(Theme.FONT_BOLD);
        title.setForeground(Theme.TEXT);
        JLabel sub = Ui.muted(l.getKategori() + " · " + l.getLokasi() + " · " + l.getTanggalLaporan());
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(title);
        text.add(Box.createVerticalStrut(2));
        text.add(sub);
        row.add(text, BorderLayout.CENTER);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        badges.setOpaque(false);
        badges.add(Ui.prioritasBadge(l.getPrioritas()));
        badges.add(Ui.statusBadge(l.getStatus()));
        row.add(badges, BorderLayout.EAST);
        return row;
    }

    private java.util.List<Laporan> visibleLaporan() {
        java.util.List<Laporan> out = new java.util.ArrayList<>();
        for (Laporan l : daftarLaporan) {
            if (user instanceof Teknisi) {
                if (l.getNamaTeknisi().equals(user.getNama())) out.add(l);
            } else {
                out.add(l);
            }
        }
        return out;
    }
}
