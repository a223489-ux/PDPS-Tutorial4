// Q1 - DataSet (GUI version)
// Compile: javac *.java     Run: java DataSetGUI
//
// Structure
//   DataSetApp.java  - original console program + model classes (Measurable, BankAccount, Coin, Student, DataSet)
//   UiKit.java       - shared colors, fonts and small UI helpers
//   DataSetGUI.java  - DataSetGUI (window) -> DataSetPanel (page) -> DataTab (one tab per data type)

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** The window. It only hosts the panel and handles start-up. */
public class DataSetGUI extends JFrame {

    public DataSetGUI() {
        super("Q1 - DataSet");
        setContentPane(new DataSetPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 740);
        setMinimumSize(new Dimension(600, 620));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        UiKit.install();
        SwingUtilities.invokeLater(() -> new DataSetGUI().setVisible(true));
    }
}

/** The whole page: header + three tabs. */
class DataSetPanel extends JPanel {

    DataSetPanel() {
        super(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Bank Account", new DataTab(
            new String[]{"Balance (RM)"}, "Balance (RM)",
            t -> new BankAccount(Double.parseDouble(t[0])),
            new String[][]{{"0"}, {"10000"}, {"2000"}}));
        tabs.addTab("Coin", new DataTab(
            new String[]{"Value (RM)", "Name"}, "Value (RM)",
            t -> new Coin(Double.parseDouble(t[0]), t[1]),
            new String[][]{{"0.25", "Quarter"}, {"0.1", "Dime"}, {"0.05", "Nickel"}}));
        tabs.addTab("Student", new DataTab(
            new String[]{"Name", "Height (cm)"}, "Height (cm)",
            t -> new Student(t[0], Double.parseDouble(t[1])),
            new String[][]{{"Ali", "157.50"}, {"Alex", "165.00"}, {"Maria", "147.00"}}));

        JPanel body = new JPanel(new BorderLayout());
        body.add(tabs, BorderLayout.CENTER);

        add(UiKit.page(
            UiKit.header("DataSet", "Average and highest value of any Measurable object"),
            body), BorderLayout.CENTER);
    }
}

/** One tab: input form, table of items, and result tiles. Works with any Measurable. */
class DataTab extends JPanel {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    private final Function<String[], Measurable> factory;
    private final String[][] sample;
    private final JTextField[] fields;
    private final List<Measurable> items = new ArrayList<>();
    private DataSet data = new DataSet();
    private int maxRow = -1;

    private final DefaultTableModel model;
    private final JTable table;
    private final JLabel status = UiKit.status();
    private final JLabel countValue = new JLabel("0");
    private final JLabel avgValue = new JLabel("-");
    private final JLabel maxValue = new JLabel("-");
    private final JLabel maxName = new JLabel(" ");

    DataTab(String[] labels, String valueHeader, Function<String[], Measurable> factory, String[][] sample) {
        super(new BorderLayout(0, 12));
        this.factory = factory;
        this.sample = sample;
        setOpaque(false);
        setBorder(new EmptyBorder(12, 0, 0, 0));

        // --- input card ---
        JPanel form = UiKit.form();
        fields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            fields[i] = UiKit.field("");
            fields[i].addActionListener(e -> onAdd());
            UiKit.row(form, i, labels[i], fields[i]);
        }
        JButton addBtn = UiKit.button("Add", UiKit.Kind.PRIMARY);
        JButton sampleBtn = UiKit.button("Load sample data", UiKit.Kind.SECONDARY);
        JButton clearBtn = UiKit.button("Clear all", UiKit.Kind.DANGER);
        addBtn.addActionListener(e -> onAdd());
        sampleBtn.addActionListener(e -> onSample());
        clearBtn.addActionListener(e -> onClear());

        JPanel inputBody = new JPanel(new BorderLayout(0, 8));
        inputBody.setOpaque(false);
        inputBody.add(form, BorderLayout.NORTH);
        inputBody.add(UiKit.buttons(addBtn, sampleBtn, clearBtn), BorderLayout.CENTER);
        inputBody.add(status, BorderLayout.SOUTH);

        // --- table card ---
        model = new DefaultTableModel(new String[]{"No.", "Name", valueHeader}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = buildTable();

        // --- result tiles ---
        JPanel tiles = new JPanel(new GridLayout(1, 3, 12, 0));
        tiles.setOpaque(false);
        tiles.add(UiKit.tile("ITEMS", countValue, new JLabel("added so far")));
        tiles.add(UiKit.tile("AVERAGE", avgValue, new JLabel("mean value")));
        tiles.add(UiKit.tile("HIGHEST", maxValue, maxName));

        add(UiKit.card("Add an item", inputBody), BorderLayout.NORTH);
        add(UiKit.card("Items", UiKit.scroll(table)), BorderLayout.CENTER);
        add(tiles, BorderLayout.SOUTH);
    }

    private JTable buildTable() {
        JTable t = new JTable(model);
        t.setRowHeight(28);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(UiKit.BOLD);
        t.getColumnModel().getColumn(0).setMaxWidth(60);

        // the highest row is shown in green, the value column is right-aligned
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tb, v, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(col == 2 ? SwingConstants.RIGHT : SwingConstants.LEFT);
                if (!sel) {
                    setBackground(row == maxRow ? UiKit.OK_BG : Color.WHITE);
                    setForeground(UiKit.TEXT);
                }
                return this;
            }
        });
        return t;
    }

    // ---- actions ----

    private void onAdd() {
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) values[i] = fields[i].getText().trim();
        if (addItem(values)) {
            for (JTextField f : fields) f.setText("");
            fields[0].requestFocusInWindow();
        }
    }

    private void onSample() {
        reset();
        for (String[] row : sample) addItem(row);
        UiKit.say(status, "Sample data loaded.", true);
    }

    private void onClear() {
        reset();
        UiKit.say(status, "All items cleared.", true);
    }

    private boolean addItem(String[] values) {
        for (String v : values) {
            if (v.isEmpty()) {
                UiKit.say(status, "Please fill in every field.", false);
                return false;
            }
        }
        Measurable m;
        try {
            m = factory.apply(values);
        } catch (NumberFormatException ex) {
            UiKit.say(status, "Enter a valid number.", false);
            return false;
        }
        data.add(m);
        items.add(m);
        model.addRow(new Object[]{items.size(), m.getName(), DF.format(m.getMeasure())});
        refreshResults();
        UiKit.say(status, "Added " + m.getName() + ".", true);
        return true;
    }

    private void reset() {
        data = new DataSet();
        items.clear();
        model.setRowCount(0);
        refreshResults();
    }

    private void refreshResults() {
        Measurable max = data.getMax();
        countValue.setText(String.valueOf(items.size()));
        if (max == null) {
            avgValue.setText("-");
            maxValue.setText("-");
            maxName.setText(" ");
            maxRow = -1;
        } else {
            avgValue.setText(DF.format(data.getAverage()));
            maxValue.setText(DF.format(max.getMeasure()));
            maxName.setText(max.getName());
            maxRow = items.indexOf(max);
        }
        table.repaint();
    }
}
