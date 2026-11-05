// UiKit - shared look and feel helpers (colors, fonts, buttons, cards, form rows).
// The same file is kept in every question folder so each folder can be compiled on its own.

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

final class UiKit {

    // ---- Colors ----
    static final Color BRAND = new Color(0x2F5BEA);
    static final Color BRAND_HOVER = new Color(0x2447BD);
    static final Color BACKGROUND = new Color(0xF4F6FB);
    static final Color CARD = Color.WHITE;
    static final Color LINE = new Color(0xDDE2EE);
    static final Color TEXT = new Color(0x1F2937);
    static final Color MUTED = new Color(0x6B7280);
    static final Color OK = new Color(0x15803D);
    static final Color OK_BG = new Color(0xDCFCE7);
    static final Color BAD = new Color(0xB91C1C);
    static final Color BAD_BG = new Color(0xFEE2E2);

    // ---- Fonts ----
    static final Font BASE = new Font("SansSerif", Font.PLAIN, 14);
    static final Font BOLD = BASE.deriveFont(Font.BOLD);
    static final Font MONO = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    enum Kind { PRIMARY, SECONDARY, DANGER }

    private UiKit() { }

    /** Call once in main(), before any component is created. */
    static void install() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // fall back to the default look and feel
        }
        for (String k : new String[]{"Label", "TextField", "ComboBox", "TabbedPane", "Table", "TableHeader", "TextArea"}) {
            UIManager.put(k + ".font", BASE);
        }
    }

    // ---- Page structure ----

    static JPanel header(String title, String subtitle) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setBackground(BRAND);
        p.setBorder(new EmptyBorder(16, 24, 16, 24));
        JLabel t = new JLabel(title);
        t.setFont(BASE.deriveFont(Font.BOLD, 22f));
        t.setForeground(Color.WHITE);
        JLabel s = new JLabel(subtitle);
        s.setFont(BASE.deriveFont(13f));
        s.setForeground(new Color(0xDCE4FF));
        p.add(t);
        p.add(s);
        return p;
    }

    static JPanel page(JComponent header, JComponent body) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BACKGROUND);
        body.setBorder(new EmptyBorder(16, 16, 16, 16));
        body.setBackground(BACKGROUND);
        p.add(header, BorderLayout.NORTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    static JPanel card(String title, JComponent content) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(new LineBorder(LINE, 1), new EmptyBorder(14, 16, 16, 16)));
        JLabel t = new JLabel(title);
        t.setFont(BOLD.deriveFont(15f));
        t.setForeground(TEXT);
        p.add(t, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    static JPanel tile(String caption, JLabel value, JLabel sub) {
        JPanel p = new JPanel(new GridLayout(3, 1, 0, 2));
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(new LineBorder(LINE, 1), new EmptyBorder(10, 14, 10, 14)));
        JLabel c = new JLabel(caption);
        c.setFont(BASE.deriveFont(12f));
        c.setForeground(MUTED);
        value.setFont(BASE.deriveFont(Font.BOLD, 22f));
        value.setForeground(TEXT);
        sub.setFont(BASE.deriveFont(12f));
        sub.setForeground(MUTED);
        p.add(c);
        p.add(value);
        p.add(sub);
        return p;
    }

    // ---- Form helpers ----

    static JPanel form() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        return p;
    }

    static JLabel row(JPanel form, int y, String label, JComponent input) {
        JLabel l = new JLabel(label);
        l.setFont(BASE);
        l.setForeground(TEXT);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 0, 5, 16);
        form.add(l, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = y;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 5, 0);
        form.add(input, c);
        return l;
    }

    static JTextField field(String text) {
        JTextField f = new JTextField(text);
        f.setFont(BASE);
        f.setForeground(TEXT);
        f.setBackground(Color.WHITE);
        f.setBorder(new CompoundBorder(new LineBorder(LINE, 1), new EmptyBorder(6, 8, 6, 8)));
        return f;
    }

    static JComboBox<String> combo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(BASE);
        return c;
    }

    static JPanel buttons(JButton... buttons) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        for (int i = 0; i < buttons.length; i++) {
            if (i > 0) p.add(Box.createHorizontalStrut(8));
            p.add(buttons[i]);
        }
        return p;
    }

    static JButton button(String text, Kind kind) {
        return new FlatButton(text, kind);
    }

    // ---- Feedback ----

    static JLabel status() {
        JLabel l = new JLabel(" ");
        l.setFont(BASE.deriveFont(13f));
        return l;
    }

    static void say(JLabel label, String message, boolean ok) {
        label.setText(message);
        label.setForeground(ok ? OK : BAD);
    }

    static JTextArea output() {
        JTextArea a = new JTextArea();
        a.setEditable(false);
        a.setFont(MONO);
        a.setForeground(TEXT);
        a.setBackground(new Color(0xF8FAFC));
        a.setBorder(new EmptyBorder(10, 12, 10, 12));
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        return a;
    }

    static JScrollPane scroll(Component c) {
        JScrollPane s = new JScrollPane(c);
        s.setBorder(new LineBorder(LINE, 1));
        s.getViewport().setBackground(Color.WHITE);
        return s;
    }

    /** Runs the action and returns whatever it printed with System.out (used to show console output in a window). */
    static String capture(Runnable action) {
        PrintStream old = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
        try {
            action.run();
        } finally {
            System.out.flush();
            System.setOut(old);
        }
        return buf.toString().replace("\r\n", "\n");
    }

    // ---- Custom button (looks the same on every operating system) ----

    private static final class FlatButton extends JButton {
        private final Kind kind;
        private boolean hover;

        FlatButton(String text, Kind kind) {
            super(text);
            this.kind = kind;
            setFont(BOLD);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setBorder(new EmptyBorder(9, 18, 9, 18));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setForeground(kind == Kind.SECONDARY ? TEXT : Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill;
            switch (kind) {
                case PRIMARY: fill = hover ? BRAND_HOVER : BRAND; break;
                case DANGER: fill = hover ? new Color(0x991B1B) : BAD; break;
                default: fill = hover ? new Color(0xE8ECF6) : CARD;
            }
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            if (kind == Kind.SECONDARY) {
                g2.setColor(LINE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
