import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public final class Ui {
    private Ui() {}

    public static JPanel card(LayoutManager layout) {
        JPanel p = new RoundedPanel();
        if (layout != null) p.setLayout(layout);
        p.setBackground(Theme.CARD);
        p.setBorder(new EmptyBorder(Theme.PAD, Theme.PAD, Theme.PAD, Theme.PAD));
        return p;
    }

    public static JLabel h1(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_H1);
        l.setForeground(Theme.TEXT);
        return l;
    }

    public static JLabel h2(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_H2);
        l.setForeground(Theme.TEXT);
        return l;
    }

    public static JLabel h3(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_H3);
        l.setForeground(Theme.TEXT);
        return l;
    }

    public static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_MUTED);
        return l;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_BOLD);
        l.setForeground(Theme.TEXT);
        return l;
    }

    public static JTextField textField() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    public static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    public static void styleField(JTextField f) {
        f.setBackground(Theme.FIELD_BG);
        f.setForeground(Theme.TEXT);
        f.setCaretColor(Theme.TEXT);
        f.setFont(Theme.FONT_BASE);
        f.setBorder(fieldBorder(false));
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) { f.setBorder(fieldBorder(true)); }
            @Override public void focusLost(java.awt.event.FocusEvent e) { f.setBorder(fieldBorder(false)); }
        });
    }

    private static javax.swing.border.Border fieldBorder(boolean focused) {
        Color line = focused ? Theme.PRIMARY : Theme.BORDER;
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(line),
                new EmptyBorder(8, 10, 8, 10));
    }

    public static <T> JComboBox<T> combo(T[] items) {
        JComboBox<T> cb = new JComboBox<T>(items);
        styleCombo(cb);
        return cb;
    }

    public static void styleCombo(JComboBox<?> cb) {
        cb.setFont(Theme.FONT_BASE);
        cb.setBackground(Theme.CARD);
        cb.setForeground(Theme.TEXT);
        cb.setFocusable(false);
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width, 34));
        cb.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
    }

    public static JTextArea textArea() {
        JTextArea a = new JTextArea();
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBackground(Theme.FIELD_BG);
        a.setForeground(Theme.TEXT);
        a.setCaretColor(Theme.TEXT);
        a.setFont(Theme.FONT_BASE);
        a.setBorder(new EmptyBorder(8, 10, 8, 10));
        return a;
    }

    public static JScrollPane scroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        sp.getViewport().setBackground(Theme.CARD);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    public static JButton primary(String text) { return button(text, Theme.PRIMARY, Theme.PRIMARY_HOVER); }
    public static JButton success(String text) { return button(text, Theme.SECONDARY, Theme.SECONDARY.darker()); }
    public static JButton danger(String text)  { return button(text, Theme.DANGER, Theme.DANGER.darker()); }
    public static JButton neutral(String text) { return button(text, Theme.NEUTRAL, Theme.NEUTRAL.darker()); }

    public static JButton button(String text, Color base, Color hover) {
        JButton b = new JButton(text);
        b.setUI(new RoundedButtonUI());
        b.setBackground(base);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setFont(Theme.FONT_BOLD);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(9, 18, 9, 18));
        b.putClientProperty("base", base);
        b.putClientProperty("hover", hover);
        return b;
    }

    public static JButton ghost(String text) {
        JButton b = new JButton(text);
        b.setForeground(Theme.TEXT_MUTED);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setFont(Theme.FONT_SMALL);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JLabel badge(String text, Color fg, Color bg) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setUI(new ChipUI());
        l.setBackground(bg);
        l.setForeground(fg);
        l.setOpaque(false);
        l.setFont(Theme.FONT_SMALL);
        l.setBorder(new EmptyBorder(3, 10, 3, 10));
        return l;
    }

    public static JPanel statusBadge(String status) {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.add(badge(status, Theme.statusFg(status), Theme.statusBg(status)));
        return wrap;
    }

    public static JPanel prioritasBadge(String prioritas) {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.add(badge(prioritas, Theme.prioritasFg(prioritas), Theme.prioritasBg(prioritas)));
        return wrap;
    }

    public static JPanel statCard(String label, String value, Color accent) {
        JPanel card = new RoundedPanel();
        card.setLayout(new BorderLayout(10, 2));
        card.setBackground(Theme.CARD);
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS, Theme.RADIUS);
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(5, 0));
        bar.setOpaque(false);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        JLabel val = new JLabel(value);
        val.setFont(Theme.FONT_STAT);
        val.setForeground(accent);
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_MUTED);
        body.add(val, BorderLayout.CENTER);
        body.add(lbl, BorderLayout.SOUTH);

        card.add(bar, BorderLayout.WEST);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    public static void styleTable(JTable t) {
        t.setRowHeight(38);
        t.setFont(Theme.FONT_SMALL);
        t.setForeground(Theme.TEXT);
        t.setBackground(Theme.CARD);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(Theme.BORDER);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(Theme.PRIMARY_SOFT);
        t.setSelectionForeground(Theme.TEXT);
        t.setFillsViewportHeight(true);

        JTableHeader h = t.getTableHeader();
        h.setDefaultRenderer(new HeaderRenderer());
        h.setPreferredSize(new Dimension(0, 40));
        h.setReorderingAllowed(false);
    }

    public static void centerColumns(JTable t, int... cols) {
        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : cols) {
            t.getColumnModel().getColumn(col).setCellRenderer(c);
        }
    }

    static class HeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            l.setBackground(Theme.FIELD_BG);
            l.setForeground(Theme.TEXT_MUTED);
            l.setFont(Theme.FONT_BOLD);
            l.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                    new EmptyBorder(0, 10, 0, 10)));
            return l;
        }
    }

    static class RoundedPanel extends JPanel {
        RoundedPanel() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS_CARD, Theme.RADIUS_CARD);
            g2.setColor(Theme.BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Theme.RADIUS_CARD, Theme.RADIUS_CARD);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class ChipUI extends javax.swing.plaf.basic.BasicLabelUI {
        @Override public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground());
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), c.getHeight(), c.getHeight());
            g2.dispose();
            super.paint(g, c);
        }
    }

    static class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        @Override public void update(Graphics g, JComponent c) { paint(g, c); }
        @Override public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Object base = b.getClientProperty("base");
            Object hover = b.getClientProperty("hover");
            g2.setColor(b.getModel().isRollover() && hover != null ? (Color) hover : (Color) base);
            g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), Theme.RADIUS, Theme.RADIUS);
            g2.dispose();
            super.paint(g, c);
        }
    }
}
