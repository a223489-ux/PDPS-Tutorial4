// Q1 - DataSet (GUI version)
// Compile: javac *.java     Run: java DataSetGUI
// Uses the classes from DataSetApp.java (Measurable, BankAccount, Coin, Student, DataSet).

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.function.Function;

public class DataSetGUI extends JFrame {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public DataSetGUI() {
        super("Q1 - DataSet");
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Bank Account", new DataPanel(
            new String[]{"Balance (RM)"},
            t -> new BankAccount(Double.parseDouble(t[0])),
            new String[][]{{"0"}, {"10000"}, {"2000"}}));

        tabs.addTab("Coin", new DataPanel(
            new String[]{"Value (RM)", "Name"},
            t -> new Coin(Double.parseDouble(t[0]), t[1]),
            new String[][]{{"0.25", "Quarter"}, {"0.1", "Dime"}, {"0.05", "Nickel"}}));

        tabs.addTab("Student", new DataPanel(
            new String[]{"Name", "Height (cm)"},
            t -> new Student(t[0], Double.parseDouble(t[1])),
            new String[][]{{"Ali", "157.50"}, {"Alex", "165.00"}, {"Maria", "147.00"}}));

        add(tabs);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 440);
        setLocationRelativeTo(null);
    }

    /** One tab: input fields + list + results. Works for any Measurable. */
    static class DataPanel extends JPanel {
        private final String[] labels;
        private final Function<String[], Measurable> factory;
        private final String[][] sample;
        private final JTextField[] fields;
        private final DefaultListModel<String> listModel = new DefaultListModel<>();
        private final JLabel avgLabel = new JLabel("Average: -");
        private final JLabel maxLabel = new JLabel("Highest: -");
        private DataSet data = new DataSet();

        DataPanel(String[] labels, Function<String[], Measurable> factory, String[][] sample) {
            this.labels = labels;
            this.factory = factory;
            this.sample = sample;
            setLayout(new BorderLayout(8, 8));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel form = new JPanel(new GridLayout(labels.length, 2, 6, 6));
            fields = new JTextField[labels.length];
            for (int i = 0; i < labels.length; i++) {
                form.add(new JLabel(labels[i] + ":"));
                fields[i] = new JTextField();
                form.add(fields[i]);
            }
            JButton addBtn = new JButton("Add");
            JButton sampleBtn = new JButton("Load Sample Data");
            JButton clearBtn = new JButton("Clear All");
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttons.add(addBtn);
            buttons.add(sampleBtn);
            buttons.add(clearBtn);

            JPanel top = new JPanel(new BorderLayout(4, 4));
            top.add(form, BorderLayout.CENTER);
            top.add(buttons, BorderLayout.SOUTH);

            JPanel results = new JPanel(new GridLayout(2, 1));
            results.setBorder(BorderFactory.createTitledBorder("Results"));
            results.add(avgLabel);
            results.add(maxLabel);

            add(top, BorderLayout.NORTH);
            add(new JScrollPane(new JList<>(listModel)), BorderLayout.CENTER);
            add(results, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> {
                String[] values = new String[fields.length];
                for (int i = 0; i < fields.length; i++) values[i] = fields[i].getText().trim();
                if (addItem(values)) {
                    for (JTextField f : fields) f.setText("");
                    fields[0].requestFocus();
                }
            });
            sampleBtn.addActionListener(e -> {
                clear();
                for (String[] row : sample) addItem(row);
            });
            clearBtn.addActionListener(e -> clear());
        }

        private boolean addItem(String[] values) {
            for (String v : values) {
                if (v.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Missing input", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            try {
                Measurable m = factory.apply(values);
                data.add(m);
                listModel.addElement(m.getName() + "  -  " + DF.format(m.getMeasure()));
                refresh();
                return true;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        private void clear() {
            data = new DataSet();
            listModel.clear();
            refresh();
        }

        private void refresh() {
            Measurable max = data.getMax();
            if (max == null) {
                avgLabel.setText("Average: -");
                maxLabel.setText("Highest: -");
            } else {
                avgLabel.setText("Average: " + DF.format(data.getAverage()));
                maxLabel.setText("Highest: " + DF.format(max.getMeasure()) + "  (" + max.getName() + ")");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataSetGUI().setVisible(true));
    }
}
