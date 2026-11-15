import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KelolaLaporanPage implements Refreshable {
    private final ArrayList<User> daftarUser;
    private final ArrayList<Laporan> daftarLaporan;
    private final Runnable onChange;
    private final JPanel root = new JPanel();

    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable tabel = new JTable(model);
    private final JTextField txtSearch = Ui.textField();
    private final JComboBox<String> cbUrut = Ui.combo(
            new String[]{"Terbaru", "Terlama", "Status A-Z", "Prioritas Tertinggi"});
    private final JComboBox<String> cbFilterStatus = Ui.combo(
            new String[]{"Semua Status", "Baru", "Ditugaskan", "Diproses", "Selesai", "Ditolak"});

    private final JComboBox<String> cbTeknisi = Ui.combo(new String[]{});
    private final JComboBox<String> cbStatus = Ui.combo(
            new String[]{"Baru", "Ditugaskan", "Diproses", "Selesai", "Ditolak"});

    private final JPanel statRow = new JPanel(new GridLayout(1, 4, Theme.GAP, Theme.GAP));
    private final List<Laporan> current = new ArrayList<Laporan>();
    private boolean built;

    public KelolaLaporanPage(ArrayList<User> daftarUser, ArrayList<Laporan> daftarLaporan, Runnable onChange) {
        this.daftarUser = daftarUser;
        this.daftarLaporan = daftarLaporan;
        this.onChange = onChange;
    }

    public JComponent build() {
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(Theme.BG);

        statRow.setOpaque(false);
        statRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statRow.setPreferredSize(new Dimension(0, 110));
        root.add(statRow);
        root.add(Box.createVerticalStrut(Theme.GAP));

        JPanel card = Ui.card(new BorderLayout(0, 14));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.add(buildToolbar(), BorderLayout.NORTH);

        buildModel();
        Ui.styleTable(tabel);
        tabel.getColumnModel().getColumn(7).setCellRenderer(new ChipRenderer("status"));
        tabel.getColumnModel().getColumn(9).setCellRenderer(new ChipRenderer("prioritas"));
        Ui.centerColumns(tabel, 0);
        JScrollPane sp = Ui.scroll(tabel);
        sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);
        card.add(buildActions(), BorderLayout.SOUTH);

        root.add(card);
        built = true;
        refresh();
        return root;
    }

    private JComponent buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.add(Ui.fieldLabel("Cari"));
        txtSearch.setPreferredSize(new Dimension(200, 34));
        left.add(txtSearch);
        left.add(cbFilterStatus);
        JButton cari = Ui.primary("Cari");
        cari.addActionListener(e -> refresh());
        left.add(cari);
        txtSearch.addActionListener(e -> refresh());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(Ui.muted("Urutkan"));
        right.add(cbUrut);
        JButton terapkan = Ui.neutral("Terapkan");
        terapkan.addActionListener(e -> refresh());
        right.add(terapkan);
        JButton detail = Ui.success("Detail");
        detail.addActionListener(e -> showDetail());
        right.add(detail);
        cbUrut.addActionListener(e -> refresh());

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void buildModel() {
        model.setColumnIdentifiers(new Object[]{
                "ID", "Tgl Laporan", "Nama", "NPM", "Judul", "Lokasi", "Kategori", "Status", "Teknisi", "Prioritas"
        });
    }

    private JComponent buildActions() {
        JPanel wrap = new JPanel(new GridLayout(2, 1, 6, 6));
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        JPanel assign = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        assign.setOpaque(false);
        assign.add(Ui.fieldLabel("Tugaskan ke"));
        cbTeknisi.setPreferredSize(new Dimension(200, 32));
        assign.add(cbTeknisi);
        JButton btnAssign = Ui.primary("Tugaskan Teknisi");
        btnAssign.addActionListener(e -> assignTeknisi());
        assign.add(btnAssign);
        wrap.add(assign);

        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        status.setOpaque(false);
        status.add(Ui.fieldLabel("Ubah status"));
        status.add(cbStatus);
        JButton btnStatus = Ui.success("Simpan Status");
        btnStatus.addActionListener(e -> ubahStatus());
        status.add(btnStatus);
        wrap.add(status);
        return wrap;
    }

    private void reloadTeknisi() {
        cbTeknisi.removeAllItems();
        for (User u : daftarUser) {
            if (u instanceof Teknisi) cbTeknisi.addItem(u.getNama());
        }
    }

    private void showDetail() {
        Laporan l = selected();
        if (l == null) return;
        JTextArea area = Ui.textArea();
        area.setEditable(false);
        area.setText(l.getDetailLaporan());
        area.setRows(16);
        area.setColumns(46);
        area.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        JOptionPane.showMessageDialog(root, Ui.scroll(area), "Detail Laporan #" + l.getIdLaporan(),
                JOptionPane.PLAIN_MESSAGE);
    }

    private Laporan selected() {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(root, "Pilih laporan pada tabel terlebih dahulu.");
            return null;
        }
        int modelRow = tabel.convertRowIndexToModel(row);
        return current.get(modelRow);
    }

    private void assignTeknisi() {
        Laporan l = selected();
        if (l == null) return;
        if (cbTeknisi.getItemCount() == 0) {
            JOptionPane.showMessageDialog(root, "Belum ada teknisi terdaftar.");
            return;
        }
        String teknisi = cbTeknisi.getSelectedItem().toString();
        l.setNamaTeknisi(teknisi);
        l.setStatus("Ditugaskan");
        l.setTanggalPenugasan(Laporan.waktuSekarang());
        onChange.run();
        refresh();
        JOptionPane.showMessageDialog(root, "Laporan ditugaskan kepada " + teknisi + ".");
    }

    private void ubahStatus() {
        Laporan l = selected();
        if (l == null) return;
        String status = cbStatus.getSelectedItem().toString();
        l.setStatus(status);
        if ("Diproses".equals(status) && "-".equals(l.getTanggalDiproses())) {
            l.setTanggalDiproses(Laporan.waktuSekarang());
        }
        if ("Selesai".equals(status) && "-".equals(l.getTanggalSelesai())) {
            l.setTanggalSelesai(Laporan.waktuSekarang());
        }
        onChange.run();
        refresh();
    }

    @Override
    public void refresh() {
        if (!built) return;
        reloadTeknisi();

        String kw = txtSearch.getText().trim().toLowerCase();
        String filterStatus = cbFilterStatus.getSelectedItem().toString();

        current.clear();
        for (Laporan l : daftarLaporan) {
            boolean match = kw.isEmpty() || matches(l, kw);
            boolean statusOk = "Semua Status".equals(filterStatus) || filterStatus.equals(l.getStatus());
            if (match && statusOk) current.add(l);
        }
        sortCurrent();

        model.setRowCount(0);
        for (Laporan l : current) {
            model.addRow(new Object[]{
                    l.getIdLaporan(), l.getTanggalLaporan(), l.getNamaMahasiswa(), l.getNpmMahasiswa(),
                    l.getJudulMasalah(), l.getLokasi(), l.getKategori(), l.getStatus(),
                    l.getNamaTeknisi(), l.getPrioritas()
            });
        }

        int baru = 0, proses = 0, selesai = 0;
        for (Laporan l : daftarLaporan) {
            if ("Selesai".equals(l.getStatus())) selesai++;
            else if ("Diproses".equals(l.getStatus()) || "Ditugaskan".equals(l.getStatus())) proses++;
            else baru++;
        }
        statRow.removeAll();
        statRow.add(Ui.statCard("Total", String.valueOf(daftarLaporan.size()), Theme.PRIMARY));
        statRow.add(Ui.statCard("Baru", String.valueOf(baru), Theme.WARNING));
        statRow.add(Ui.statCard("Diproses", String.valueOf(proses), Theme.PRIMARY));
        statRow.add(Ui.statCard("Selesai", String.valueOf(selesai), Theme.SECONDARY));
        statRow.revalidate();
        statRow.repaint();
    }

    private boolean matches(Laporan l, String kw) {
        return l.getNamaMahasiswa().toLowerCase().contains(kw)
                || l.getNpmMahasiswa().toLowerCase().contains(kw)
                || l.getJudulMasalah().toLowerCase().contains(kw)
                || l.getLokasi().toLowerCase().contains(kw)
                || l.getKategori().toLowerCase().contains(kw)
                || l.getStatus().toLowerCase().contains(kw)
                || l.getNamaTeknisi().toLowerCase().contains(kw);
    }

    private void sortCurrent() {
        String pilihan = cbUrut.getSelectedItem().toString();
        if ("Terbaru".equals(pilihan)) current.sort((a, b) -> b.getIdLaporan() - a.getIdLaporan());
        else if ("Terlama".equals(pilihan)) current.sort((a, b) -> a.getIdLaporan() - b.getIdLaporan());
        else if ("Status A-Z".equals(pilihan)) current.sort((a, b) -> a.getStatus().compareTo(b.getStatus()));
        else if ("Prioritas Tertinggi".equals(pilihan)) current.sort((a, b) -> prioritasRank(b.getPrioritas()) - prioritasRank(a.getPrioritas()));
    }

    private int prioritasRank(String p) {
        if ("Darurat".equals(p)) return 4;
        if ("Tinggi".equals(p)) return 3;
        if ("Sedang".equals(p)) return 2;
        if ("Rendah".equals(p)) return 1;
        return 0;
    }
}
