import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BuatLaporanPage implements Refreshable {
    private final Mahasiswa mahasiswa;
    private final ArrayList<Laporan> daftarLaporan;
    private final Runnable onChange;
    private final JPanel root = new JPanel();

    private final JTextField txtJudul = Ui.textField();
    private final JTextField txtLokasi = Ui.textField();
    private final JComboBox<String> cbKategori = Ui.combo(
            new String[]{"Hardware", "Software", "Jaringan", "Perangkat Pendukung", "Lainnya"});
    private final JTextArea txtDeskripsi = Ui.textArea();

    private final JLabel lblError = new JLabel(" ");
    private final DefaultListModel<String> riwayatModel = new DefaultListModel<String>();
    private boolean built;

    public BuatLaporanPage(Mahasiswa mahasiswa, ArrayList<Laporan> daftarLaporan, Runnable onChange) {
        this.mahasiswa = mahasiswa;
        this.daftarLaporan = daftarLaporan;
        this.onChange = onChange;
    }

    public JComponent build() {
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(Theme.BG);

        JPanel columns = new JPanel(new GridLayout(1, 2, Theme.GAP, Theme.GAP));
        columns.setOpaque(false);
        columns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 560));
        columns.setPreferredSize(new Dimension(0, 560));
        columns.setAlignmentX(Component.LEFT_ALIGNMENT);
        columns.add(buildForm());
        columns.add(buildRiwayat());
        root.add(columns);

        built = true;
        refresh();
        return root;
    }

    private JComponent buildForm() {
        JPanel card = Ui.card(new BorderLayout(0, 14));
        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(Ui.h3("Buat Laporan Baru"));
        head.add(Box.createVerticalStrut(3));
        head.add(Ui.muted("Isi detail kendala yang Anda alami."));
        card.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(0, 0, 10, 0);
        g.gridx = 0;
        g.weightx = 1;

        g.gridy = 0; form.add(Ui.fieldLabel("Judul Masalah"), g);
        g.gridy = 1; form.add(txtJudul, g);
        g.gridy = 2; form.add(Ui.fieldLabel("Lokasi / Nomor Komputer"), g);
        g.gridy = 3; form.add(txtLokasi, g);
        g.gridy = 4; form.add(Ui.fieldLabel("Kategori"), g);
        g.gridy = 5; form.add(cbKategori, g);
        g.gridy = 6; form.add(Ui.fieldLabel("Deskripsi Masalah"), g);

        JScrollPane descScroll = Ui.scroll(txtDeskripsi);
        descScroll.setPreferredSize(new Dimension(0, 130));
        g.gridy = 7;
        g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        form.add(descScroll, g);

        card.add(form, BorderLayout.CENTER);

        lblError.setFont(Theme.FONT_SMALL);
        lblError.setForeground(Theme.DANGER);
        JButton kirim = Ui.primary("Kirim Laporan");
        JButton reset = Ui.neutral("Bersihkan");
        kirim.addActionListener(e -> submit());
        reset.addActionListener(e -> clearForm());

        JPanel actions = new JPanel(new BorderLayout(10, 0));
        actions.setOpaque(false);
        actions.add(lblError, BorderLayout.CENTER);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        btns.add(reset);
        btns.add(kirim);
        actions.add(btns, BorderLayout.EAST);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JComponent buildRiwayat() {
        JPanel card = Ui.card(new BorderLayout(0, 12));
        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(Ui.h3("Riwayat Laporan Saya"));
        head.add(Box.createVerticalStrut(3));
        head.add(Ui.muted("Laporan yang pernah Anda kirimkan."));
        card.add(head, BorderLayout.NORTH);

        JList<String> list = new JList<String>(riwayatModel);
        list.setFont(Theme.FONT_SMALL);
        list.setFixedCellHeight(58);
        list.setCellRenderer(new RiwayatRenderer());
        list.setBackground(Theme.CARD);
        JScrollPane sp = Ui.scroll(list);
        sp.setBorder(null);
        card.add(sp, BorderLayout.CENTER);

        JLabel empty = Ui.muted("");
        card.add(empty, BorderLayout.SOUTH);
        return card;
    }

    private void submit() {
        String judul = txtJudul.getText().trim();
        String lokasi = txtLokasi.getText().trim();
        String kategori = cbKategori.getSelectedItem().toString();
        String deskripsi = txtDeskripsi.getText().trim();

        if (judul.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty()) {
            lblError.setText("Judul, lokasi, dan deskripsi wajib diisi.");
            return;
        }
        lblError.setText(" ");

        Laporan laporan = new Laporan(mahasiswa.getNama(), mahasiswa.getNpm(), judul, lokasi, kategori, deskripsi);
        daftarLaporan.add(laporan);
        clearForm();
        onChange.run();
        JOptionPane.showMessageDialog(root, "Laporan #" + laporan.getIdLaporan() + " berhasil dikirim.", "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        txtJudul.setText("");
        txtLokasi.setText("");
        txtDeskripsi.setText("");
        cbKategori.setSelectedIndex(0);
        lblError.setText(" ");
    }

    @Override
    public void refresh() {
        if (!built) return;
        riwayatModel.clear();
        List<Laporan> mine = new ArrayList<Laporan>();
        for (Laporan l : daftarLaporan) {
            if (l.getNpmMahasiswa() != null && l.getNpmMahasiswa().equals(mahasiswa.getNpm())) {
                mine.add(l);
            }
        }
        mine.sort((a, b) -> b.getIdLaporan() - a.getIdLaporan());
        for (Laporan l : mine) {
            riwayatModel.addElement(l.getIdLaporan() + "|" + l.getJudulMasalah() + "|" + l.getStatus()
                    + "|" + l.getKategori() + " · " + l.getTanggalLaporan());
        }
    }

    private static class RiwayatRenderer extends JPanel implements ListCellRenderer<String> {
        private final JLabel title = new JLabel();
        private final JLabel sub = new JLabel();
        private final JLabel badge = new JLabel();

        RiwayatRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(new EmptyBorder(8, 10, 8, 10));
            JPanel text = new JPanel();
            text.setOpaque(false);
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
            title.setFont(Theme.FONT_BOLD);
            title.setForeground(Theme.TEXT);
            sub.setFont(Theme.FONT_SMALL);
            sub.setForeground(Theme.TEXT_MUTED);
            text.add(title);
            text.add(sub);
            add(text, BorderLayout.CENTER);
            add(badge, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            String[] parts = value.split("\\|", 4);
            title.setText("#" + parts[0] + "  " + parts[1]);
            String status = parts.length > 2 ? parts[2] : "";
            sub.setText(parts.length > 3 ? parts[3] : "");
            badge.setText(status);
            badge.setFont(Theme.FONT_SMALL);
            badge.setForeground(Theme.statusFg(status));
            badge.setOpaque(true);
            badge.setBackground(Theme.statusBg(status));
            badge.setBorder(new EmptyBorder(3, 8, 3, 8));
            setBackground(isSelected ? Theme.PRIMARY_SOFT : Theme.CARD);
            return this;
        }
    }
}
