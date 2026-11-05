// Q3 - Mobile Device (GUI version)
// Compile: javac *.java     Run: java MobileGUI
//
// Structure
//   MobileApp.java  - original console program + model classes (RadioTuner, MobileDevice, SmartPhone, BasicPhone)
//   UiKit.java      - shared colors, fonts and small UI helpers
//   MobileGUI.java  - MobileGUI (window) -> MobilePanel (page)

import javax.swing.*;
import java.awt.*;

/** The window. It only hosts the panel and handles start-up. */
public class MobileGUI extends JFrame {

    public MobileGUI() {
        super("Q3 - Mobile Device");
        setContentPane(new MobilePanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 820);
        setMinimumSize(new Dimension(520, 700));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        UiKit.install();
        SwingUtilities.invokeLater(() -> new MobileGUI().setVisible(true));
    }
}

/** The whole page: header, input card and output card. */
class MobilePanel extends JPanel {

    private final JComboBox<String> deviceBox = UiKit.combo("SmartPhone", "BasicPhone");
    private final JTextField manufacturerField = UiKit.field("Samsung");
    private final JTextField modelField = UiKit.field("S20");
    private final JTextField typeField = UiKit.field("Galaxy");
    private final JLabel typeLabel;
    private final JTextField stationField = UiKit.field("Hot FM");
    private final JTextField frequencyField = UiKit.field("97.60");
    private final JLabel status = UiKit.status();
    private final JTextArea output = UiKit.output();

    MobilePanel() {
        super(new BorderLayout());

        JPanel form = UiKit.form();
        UiKit.row(form, 0, "Device", deviceBox);
        UiKit.row(form, 1, "Manufacturer", manufacturerField);
        UiKit.row(form, 2, "Model", modelField);
        typeLabel = UiKit.row(form, 3, "Type (SmartPhone only)", typeField);
        UiKit.row(form, 4, "Radio station", stationField);
        UiKit.row(form, 5, "Frequency (MHz)", frequencyField);

        JButton showBtn = UiKit.button("Show details", UiKit.Kind.PRIMARY);
        JButton demoBtn = UiKit.button("Run demo", UiKit.Kind.SECONDARY);
        JButton clearBtn = UiKit.button("Clear", UiKit.Kind.DANGER);

        JPanel inputBody = new JPanel(new BorderLayout(0, 8));
        inputBody.setOpaque(false);
        inputBody.add(form, BorderLayout.NORTH);
        inputBody.add(UiKit.buttons(showBtn, demoBtn, clearBtn), BorderLayout.CENTER);
        inputBody.add(status, BorderLayout.SOUTH);

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.add(UiKit.card("Device details", inputBody), BorderLayout.NORTH);
        body.add(UiKit.card("Output", UiKit.scroll(output)), BorderLayout.CENTER);

        add(UiKit.page(
            UiKit.header("Mobile Device", "SmartPhone and BasicPhone sharing the RadioTuner interface"),
            body), BorderLayout.CENTER);

        deviceBox.addActionListener(e -> onDeviceChanged());
        showBtn.addActionListener(e -> onShow());
        demoBtn.addActionListener(e -> onDemo());
        clearBtn.addActionListener(e -> onClear());
    }

    // ---- actions ----

    private void onDeviceChanged() {
        boolean smart = "SmartPhone".equals(deviceBox.getSelectedItem());
        typeField.setEnabled(smart);
        typeLabel.setEnabled(smart);
    }

    private void onShow() {
        String manufacturer = manufacturerField.getText().trim();
        String model = modelField.getText().trim();
        String station = stationField.getText().trim();
        boolean smart = "SmartPhone".equals(deviceBox.getSelectedItem());
        String type = typeField.getText().trim();

        if (manufacturer.isEmpty() || model.isEmpty() || station.isEmpty() || (smart && type.isEmpty())) {
            UiKit.say(status, "Please fill in every field.", false);
            return;
        }
        double frequency;
        try {
            frequency = Double.parseDouble(frequencyField.getText().trim());
        } catch (NumberFormatException ex) {
            UiKit.say(status, "Frequency must be a number, for example 97.60.", false);
            return;
        }

        MobileDevice device = smart ? new SmartPhone(manufacturer, model, type)
                                    : new BasicPhone(manufacturer, model);
        device.setStation(station);
        device.setFrequency(frequency);
        show(device);
        UiKit.say(status, "Details shown below.", true);
    }

    /** Same data as MobileApp.main. */
    private void onDemo() {
        MobileDevice m1 = new SmartPhone("Samsung", "S20", "Galaxy");
        MobileDevice m2 = new BasicPhone("Sonny Ericson", "Walkman");
        m1.setStation("Hot FM");
        m1.setFrequency(97.60);
        m2.setStation("Fly FM");
        m2.setFrequency(109.8);
        show(m1);
        show(m2);
        UiKit.say(status, "Demo finished.", true);
    }

    private void onClear() {
        output.setText("");
        UiKit.say(status, "Output cleared.", true);
    }

    /** printDetails() prints to the console, so its text is captured and shown in the window instead. */
    private void show(MobileDevice d) {
        output.append(UiKit.capture(d::printDetails));
        output.append("Station: " + d.station + "\n");
        output.append("Frequency: " + d.frequency + "\n\n");
    }
}
