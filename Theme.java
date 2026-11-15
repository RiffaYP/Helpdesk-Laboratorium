import java.awt.*;
import javax.swing.UIManager;

public final class Theme {
    private Theme() {}

    public static final Color PRIMARY = new Color(37, 99, 235);
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216);
    public static final Color SECONDARY = new Color(16, 185, 129);
    public static final Color DANGER = new Color(239, 68, 68);
    public static final Color WARNING = new Color(245, 158, 11);
    public static final Color NEUTRAL = new Color(100, 116, 139);

    public static final Color BG = new Color(241, 245, 249);
    public static final Color CARD = Color.WHITE;
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color SIDEBAR = new Color(30, 41, 59);
    public static final Color SIDEBAR_HOVER = new Color(51, 65, 85);
    public static final Color TEXT = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color FIELD_BG = new Color(248, 250, 252);

    public static final Color PRIMARY_SOFT = new Color(219, 234, 254);
    public static final Color SECONDARY_SOFT = new Color(209, 250, 229);
    public static final Color DANGER_SOFT = new Color(254, 226, 226);
    public static final Color WARNING_SOFT = new Color(254, 243, 199);
    public static final Color NEUTRAL_SOFT = new Color(226, 232, 240);

    public static final Color SIDEBAR_TEXT = new Color(203, 213, 225);
    public static final Color SIDEBAR_MUTED = new Color(148, 163, 184);
    public static final Color SELECTION = PRIMARY_SOFT;

    public static final Font FONT_BASE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_H1 = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_H3 = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_STAT = new Font("Segoe UI", Font.BOLD, 28);

    public static final int PAD = 16;
    public static final int GAP = 12;
    public static final int RADIUS = 10;
    public static final int RADIUS_CARD = 16;

    public static void install() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        UIManager.put("defaultFont", FONT_BASE);
        UIManager.put("Label.font", FONT_BASE);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("TextField.font", FONT_BASE);
        UIManager.put("PasswordField.font", FONT_BASE);
        UIManager.put("TextArea.font", FONT_BASE);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("TextArea.background", FIELD_BG);
        UIManager.put("TextPane.font", FONT_BASE);
        UIManager.put("EditorPane.font", FONT_BASE);
        UIManager.put("ComboBox.font", FONT_BASE);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("ComboBox.background", CARD);
        UIManager.put("ComboBox.selectionBackground", SELECTION);
        UIManager.put("ComboBox.selectionForeground", TEXT);
        UIManager.put("CheckBox.font", FONT_BASE);
        UIManager.put("RadioButton.font", FONT_BASE);
        UIManager.put("Table.font", FONT_SMALL);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.background", CARD);
        UIManager.put("Table.selectionBackground", SELECTION);
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("TableHeader.foreground", TEXT_MUTED);
        UIManager.put("TableHeader.background", FIELD_BG);
        UIManager.put("ScrollPane.background", CARD);
        UIManager.put("Viewport.background", CARD);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("ScrollBar.thumb", NEUTRAL_SOFT);
        UIManager.put("ScrollBar.track", FIELD_BG);
        UIManager.put("ToolTip.font", FONT_SMALL);
        UIManager.put("ToolTip.background", SIDEBAR);
        UIManager.put("ToolTip.foreground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", FONT_BASE);
        UIManager.put("OptionPane.buttonFont", FONT_BOLD);
        UIManager.put("OptionPane.background", CARD);
        UIManager.put("Panel.background", BG);
        UIManager.put("List.font", FONT_SMALL);
        UIManager.put("List.background", CARD);
        UIManager.put("List.selectionBackground", SELECTION);
        UIManager.put("List.selectionForeground", TEXT);
        UIManager.put("TitledBorder.font", FONT_BOLD);
        UIManager.put("TitledBorder.titleColor", TEXT_MUTED);
    }

    public static Color statusFg(String status) {
        switch (status) {
            case "Selesai":    return new Color(4, 120, 87);
            case "Diproses":   return new Color(29, 78, 216);
            case "Ditugaskan": return new Color(3, 105, 161);
            case "Ditolak":    return new Color(185, 28, 28);
            default:           return new Color(71, 85, 105);
        }
    }

    public static Color statusBg(String status) {
        switch (status) {
            case "Selesai":    return SECONDARY_SOFT;
            case "Diproses":   return PRIMARY_SOFT;
            case "Ditugaskan": return new Color(224, 242, 254);
            case "Ditolak":    return DANGER_SOFT;
            default:           return NEUTRAL_SOFT;
        }
    }

    public static Color prioritasFg(String prioritas) {
        switch (prioritas) {
            case "Darurat": return new Color(185, 28, 28);
            case "Tinggi":  return new Color(194, 65, 12);
            case "Sedang":  return new Color(161, 98, 7);
            case "Rendah":  return new Color(4, 120, 87);
            default:        return TEXT_MUTED;
        }
    }

    public static Color prioritasBg(String prioritas) {
        switch (prioritas) {
            case "Darurat": return DANGER_SOFT;
            case "Tinggi":  return new Color(255, 237, 213);
            case "Sedang":  return WARNING_SOFT;
            case "Rendah":  return SECONDARY_SOFT;
            default:        return NEUTRAL_SOFT;
        }
    }
}
