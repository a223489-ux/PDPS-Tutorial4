// Q4 - Price (GUI version)
// Compile: javac *.java     Run: java PriceGUI
// Uses the classes from PriceApp.java (Price, Customer, Product, SmartPhone, Laptop, Car).

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PriceGUI extends JFrame {

    private final JComboBox<String> itemBox = new JComboBox<>(new String[]{"SmartPhone", "Laptop", "Car"});
    private final JTextField nameField = new JTextField("Iphone 14 ProMax");
    private final JTextField priceField = new JTextField("5500.00");
    private final JTextField customerField = new JTextField("Sally");
    private final JTextField salaryField = new JTextField("3000.00");
    private final JTextArea output = new JTextArea();

    public PriceGUI() {
        super("Q4 - Price");
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(5, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        form.add(new JLabel("Item:"));                 form.add(itemBox);
        form.add(new JLabel("Product name / car brand:")); form.add(nameField);
        form.add(new JLabel("Price (RM):"));           form.add(priceField);
        form.add(new JLabel("Customer name:"));        form.add(customerField);
        form.add(new JLabel("Customer salary (RM):")); form.add(salaryField);

        JButton checkBtn = new JButton("Check Price");
        JButton demoBtn = new JButton("Run Demo (same as console)");
        JButton clearBtn = new JButton("Clear");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(checkBtn);
        buttons.add(demoBtn);
        buttons.add(clearBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        itemBox.addActionListener(e -> {
            switch ((String) itemBox.getSelectedItem()) {
                case "SmartPhone": nameField.setText("Iphone 14 ProMax"); priceField.setText("5500.00"); break;
                case "Laptop": nameField.setText("Lenovo Yoga"); priceField.setText("6000.00"); break;
                default: nameField.setText("BMW"); priceField.setText("200000");
            }
        });

        checkBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String customerName = customerField.getText().trim();
            if (name.isEmpty() || customerName.isEmpty()) {
                warn("Please fill in the product name and customer name.");
                return;
            }
            double price, salary;
            try {
                price = Double.parseDouble(priceField.getText().trim());
                salary = Double.parseDouble(salaryField.getText().trim());
            } catch (NumberFormatException ex) {
                warn("Price and salary must be numbers.");
                return;
            }
            Price item;                       // polymorphism: one interface, three classes
            switch ((String) itemBox.getSelectedItem()) {
                case "SmartPhone": item = new SmartPhone("SmartPhone", name, price); break;
                case "Laptop": item = new Laptop("Laptop", name, price); break;
                default: item = new Car(name, price);
            }
            Customer customer = new Customer(customerName, salary);
            output.append(capture(() -> item.getPrice(customer)) + "\n");
        });

        demoBtn.addActionListener(e -> {
            SmartPhone sp = new SmartPhone("SmartPhone", "Iphone 14 ProMax", 5500.00);
            Laptop lp = new Laptop("Laptop", "Lenovo Yoga", 6000.00);
            Car c = new Car("BMW", 200000);
            Customer c1 = new Customer("Sally", 3000.00);
            Customer c2 = new Customer("Aaron", 8000.00);
            Customer c3 = new Customer("Simon", 10000.00);
            output.append(capture(() -> {
                sp.getPrice(c1);
                lp.getPrice(c2);
                c.getPrice(c3);
            }));
        });

        clearBtn.addActionListener(e -> output.setText(""));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 520);
        setLocationRelativeTo(null);
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid input", JOptionPane.WARNING_MESSAGE);
    }

    /** getPrice() prints to the console, so we capture that text and show it in the window. */
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
        SwingUtilities.invokeLater(() -> new PriceGUI().setVisible(true));
    }
}
