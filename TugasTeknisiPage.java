import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TugasTeknisiPage implements Refreshable {
    private final User user;
    private final ArrayList<Laporan> daftarLaporan;
    private final Runnable onChange;
    private final JPanel root = new JPanel();

    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable tabel = new JTable(model);
    private final JTextField txtSearch = Ui.textField();

    private final JComboBox<String> cbPrioritas = Ui.combo(
            new String[]{"Rendah", "Sedang", "Tinggi", "Darurat"});
    private final JTextArea txtCatatan = Ui.textArea();

    private final JPanel statRow = new JPanel(new GridLayout(1, 4, Theme.GAP, Theme.GAP));
    private final List<Laporan> current = new ArrayList<Laporan>();
    private boolean built;

    public TugasTeknisiPage(User user, ArrayList<Laporan> daftarLaporan, Runnable onChange) {
        this.user = user;
        this.daftarLaporan = daftarLaporan;
        this.onChange = onChange;
    }

    public JComponent build() {
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(Theme.BG);

        statRow.setOpaque(false);
        statRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        statRow.setPreferredSize(new Dimension(0, 110));
        statRow.setMinimumSize(new Dimension(0, 110));
        statRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(statRow);
        root.add(Box.createVerticalStrut(Theme.GAP));

        JPanel card = Ui.card(new BorderLayout(0, 12));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(buildToolbar(), BorderLayout.NORTH);

        model.setColumnIdentifiers(new Object[]{
                "ID", "Tgl Laporan", "Mahasiswa", "Judul", "Lokasi", "Kategori", "Status", "Prioritas"
        });
        Ui.styleTable(tabel);
        tabel.getColumnModel().getColumn(6).setCellRenderer(new ChipRenderer("status"));
        tabel.getColumnModel().getColumn(7).setCellRenderer(new ChipRenderer("prioritas"));
        Ui.centerColumns(tabel, 0);
        JScrollPane sp = Ui.scroll(tabel);
        sp.setBorder(null);
        sp.setPreferredSize(new Dimension(0, 300));
        sp.setMinimumSize(new Dimension(0, 150));
        card.add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(buildActions(), BorderLayout.NORTH);
        bottom.add(buildCatatan(), BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        root.add(card);
        built = true;
        refresh();
        return root;
    }

    private JComponent buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.add(Ui.fieldLabel("Cari"));
        txtSearch.setPreferredSize(new Dimension(220, 34));
        left.add(txtSearch);
        JButton cari = Ui.primary("Cari");
        cari.addActionListener(e -> refresh());
        left.add(cari);
        txtSearch.addActionListener(e -> refresh());
        JButton detail = Ui.success("Detail");
        detail.addActionListener(e -> showDetail());
        left.add(detail);
        bar.add(left, BorderLayout.WEST);
        return bar;
    }

    private JComponent buildActions() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bar.setOpaque(false);
        bar.add(Ui.fieldLabel("Prioritas"));
        bar.add(cbPrioritas);
        JButton simpanPrioritas = Ui.success("Simpan Prioritas");
        simpanPrioritas.addActionListener(e -> simpanPrioritas());
        bar.add(simpanPrioritas);
        JButton tangani = Ui.primary("Tangani");
        tangani.addActionListener(e -> tangani());
        bar.add(tangani);
        JButton selesai = Ui.success("Selesaikan");
        selesai.addActionListener(e -> selesaikan());
        bar.add(selesai);
        return bar;
    }

    private JComponent buildCatatan() {
        JPanel wrap = new JPanel(new BorderLayout(0, 6));
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));
        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.setBorder(new javax.swing.border.EmptyBorder(8, 0, 0, 0));
        head.add(Ui.fieldLabel("Catatan Penanganan"), BorderLayout.WEST);
        JButton simpan = Ui.primary("Simpan Catatan");
        simpan.addActionListener(e -> simpanCatatan());
        head.add(simpan, BorderLayout.EAST);
        wrap.add(head, BorderLayout.NORTH);

        JScrollPane sp = Ui.scroll(txtCatatan);
        sp.setPreferredSize(new Dimension(0, 74));
        wrap.add(sp, BorderLayout.CENTER);
        return wrap;
    }

    private Laporan selected() {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(root, "Pilih tugas pada tabel terlebih dahulu.");
            return null;
        }
        return current.get(tabel.convertRowIndexToModel(row));
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

    private void simpanPrioritas() {
        Laporan l = selected();
        if (l == null) return;
        l.setPrioritas(cbPrioritas.getSelectedItem().toString());
        onChange.run();
        refresh();
    }

    private void tangani() {
        Laporan l = selected();
        if (l == null) return;
        l.setStatus("Diproses");
        if ("-".equals(l.getTanggalDiproses())) l.setTanggalDiproses(Laporan.waktuSekarang());
        onChange.run();
        refresh();
    }

    private void selesaikan() {
        Laporan l = selected();
        if (l == null) return;
        l.setStatus("Selesai");
        if ("-".equals(l.getTanggalSelesai())) l.setTanggalSelesai(Laporan.waktuSekarang());
        onChange.run();
        refresh();
    }

    private void simpanCatatan() {
        Laporan l = selected();
        if (l == null) return;
        String catatan = txtCatatan.getText().trim();
        if (catatan.isEmpty()) {
            JOptionPane.showMessageDialog(root, "Catatan tidak boleh kosong.");
            return;
        }
        l.setCatatanPenanganan(catatan);
        txtCatatan.setText("");
        onChange.run();
        refresh();
    }

    @Override
    public void refresh() {
        if (!built) return;
        String kw = txtSearch.getText().trim().toLowerCase();

        current.clear();
        for (Laporan l : daftarLaporan) {
            if (!l.getNamaTeknisi().equals(user.getNama())) continue;
            if (kw.isEmpty() || matches(l, kw)) current.add(l);
        }
        current.sort((a, b) -> b.getIdLaporan() - a.getIdLaporan());

        model.setRowCount(0);
        for (Laporan l : current) {
            model.addRow(new Object[]{
                    l.getIdLaporan(), l.getTanggalLaporan(), l.getNamaMahasiswa(), l.getJudulMasalah(),
                    l.getLokasi(), l.getKategori(), l.getStatus(), l.getPrioritas()
            });
        }

        int aktif = 0, proses = 0, selesai = 0, darurat = 0;
        for (Laporan l : daftarLaporan) {
            if (!l.getNamaTeknisi().equals(user.getNama())) continue;
            if ("Selesai".equals(l.getStatus())) selesai++;
            else if ("Diproses".equals(l.getStatus())) proses++;
            else aktif++;
            if ("Darurat".equals(l.getPrioritas())) darurat++;
        }
        statRow.removeAll();
        statRow.add(Ui.statCard("Tugas Saya", String.valueOf(aktif + proses + selesai), Theme.PRIMARY));
        statRow.add(Ui.statCard("Perlu Ditangani", String.valueOf(aktif), Theme.WARNING));
        statRow.add(Ui.statCard("Diproses", String.valueOf(proses), Theme.PRIMARY));
        statRow.add(Ui.statCard("Selesai", String.valueOf(selesai), Theme.SECONDARY));
        statRow.revalidate();
        statRow.repaint();
    }

    private boolean matches(Laporan l, String kw) {
        return l.getNamaMahasiswa().toLowerCase().contains(kw)
                || l.getJudulMasalah().toLowerCase().contains(kw)
                || l.getLokasi().toLowerCase().contains(kw)
                || l.getKategori().toLowerCase().contains(kw)
                || l.getStatus().toLowerCase().contains(kw);
    }
}
