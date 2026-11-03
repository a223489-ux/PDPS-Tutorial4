// Q3 - Mobile Device (GUI version)
// Compile: javac *.java     Run: java MobileGUI
// Uses the classes from MobileApp.java (RadioTuner, MobileDevice, SmartPhone, BasicPhone).

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MobileGUI extends JFrame {

    private final JComboBox<String> deviceBox = new JComboBox<>(new String[]{"SmartPhone", "BasicPhone"});
    private final JTextField manufacturerField = new JTextField("Samsung");
    private final JTextField modelField = new JTextField("S20");
    private final JLabel typeLabel = new JLabel("Type:");
    private final JTextField typeField = new JTextField("Galaxy");
    private final JTextField stationField = new JTextField("Hot FM");
    private final JTextField frequencyField = new JTextField("97.60");
    private final JTextArea output = new JTextArea();

    public MobileGUI() {
        super("Q3 - Mobile Device");
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        form.add(new JLabel("Device:"));        form.add(deviceBox);
        form.add(new JLabel("Manufacturer:"));  form.add(manufacturerField);
        form.add(new JLabel("Model:"));         form.add(modelField);
        form.add(typeLabel);                    form.add(typeField);
        form.add(new JLabel("Radio station:")); form.add(stationField);
        form.add(new JLabel("Frequency (MHz):")); form.add(frequencyField);

        JButton showBtn = new JButton("Show Details");
        JButton demoBtn = new JButton("Run Demo (same as console)");
        JButton clearBtn = new JButton("Clear");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(showBtn);
        buttons.add(demoBtn);
        buttons.add(clearBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // BasicPhone has no "type", so disable that field
        deviceBox.addActionListener(e -> {
            boolean smart = "SmartPhone".equals(deviceBox.getSelectedItem());
            typeLabel.setEnabled(smart);
            typeField.setEnabled(smart);
        });

        showBtn.addActionListener(e -> {
            String manufacturer = manufacturerField.getText().trim();
            String model = modelField.getText().trim();
            String station = stationField.getText().trim();
            if (manufacturer.isEmpty() || model.isEmpty() || station.isEmpty()) {
                warn("Please fill in manufacturer, model and radio station.");
                return;
            }
            double frequency;
            try {
                frequency = Double.parseDouble(frequencyField.getText().trim());
            } catch (NumberFormatException ex) {
                warn("Frequency must be a number, e.g. 97.60");
                return;
            }
            MobileDevice device;
            if ("SmartPhone".equals(deviceBox.getSelectedItem())) {
                String type = typeField.getText().trim();
                if (type.isEmpty()) { warn("Please enter the smartphone type."); return; }
                device = new SmartPhone(manufacturer, model, type);
            } else {
                device = new BasicPhone(manufacturer, model);
            }
            device.setStation(station);
            device.setFrequency(frequency);
            show(device);
        });

        demoBtn.addActionListener(e -> {
            MobileDevice m1 = new SmartPhone("Samsung", "S20", "Galaxy");
            MobileDevice m2 = new BasicPhone("Sonny Ericson", "Walkman");
            m1.setStation("Hot FM");
            m1.setFrequency(97.60);
            m2.setStation("Fly FM");
            m2.setFrequency(109.8);
            show(m1);
            show(m2);
        });

        clearBtn.addActionListener(e -> output.setText(""));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 560);
        setLocationRelativeTo(null);
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid input", JOptionPane.WARNING_MESSAGE);
    }

    /** printDetails() prints to the console, so we capture that text and show it in the window. */
    private void show(MobileDevice d) {
        output.append(capture(d::printDetails));
        output.append("Station: " + d.station + "\n");
        output.append("Frequency: " + d.frequency + "\n\n");
    }

    private static String capture(Runnable action) {
        PrintStream old = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
        try {
            action.run();
        } finally {
            System.out.flush();
            System.setOut(old);
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MobileGUI().setVisible(true));
    }
}
