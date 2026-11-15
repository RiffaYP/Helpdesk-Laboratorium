import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public final class ChipRenderer extends DefaultTableCellRenderer {
    private final String kind;

    public ChipRenderer(String kind) {
        this.kind = kind;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        String text = value == null ? "" : value.toString();
        Color fg = "status".equals(kind) ? Theme.statusFg(text) : Theme.prioritasFg(text);
        Color bg = "status".equals(kind) ? Theme.statusBg(text) : Theme.prioritasBg(text);

        JLabel chip = new JLabel(text, SwingConstants.CENTER);
        chip.setFont(Theme.FONT_SMALL);
        chip.setForeground(fg);
        chip.setOpaque(true);
        chip.setBackground(bg);
        chip.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        wrap.add(chip);
        return wrap;
    }
}
