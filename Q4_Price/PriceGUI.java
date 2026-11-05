// Q4 - Price (GUI version)
// Compile: javac *.java     Run: java PriceGUI
//
// Structure
//   PriceApp.java  - original console program + model classes (Price, Customer, Product, SmartPhone, Laptop, Car)
//   UiKit.java     - shared colors, fonts and small UI helpers
//   PriceGUI.java  - PriceGUI (window) -> PricePanel (page)

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** The window. It only hosts the panel and handles start-up. */
public class PriceGUI extends JFrame {

    public PriceGUI() {
        super("Q4 - Price");
        setContentPane(new PricePanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(660, 820);
        setMinimumSize(new Dimension(540, 700));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        UiKit.install();
        SwingUtilities.invokeLater(() -> new PriceGUI().setVisible(true));
    }
}

/** The whole page: header, input card, result banner and output. */
class PricePanel extends JPanel {

    private final JComboBox<String> itemBox = UiKit.combo("SmartPhone", "Laptop", "Car");
    private final JTextField nameField = UiKit.field("Iphone 14 ProMax");
    private final JTextField priceField = UiKit.field("5500.00");
    private final JTextField customerField = UiKit.field("Sally");
    private final JTextField salaryField = UiKit.field("3000.00");
    private final JLabel status = UiKit.status();
    private final JLabel banner = new JLabel("No result yet", SwingConstants.CENTER);
    private final JTextArea output = UiKit.output();

    PricePanel() {
        super(new BorderLayout());

        JPanel form = UiKit.form();
        UiKit.row(form, 0, "Item", itemBox);
        UiKit.row(form, 1, "Product name / car brand", nameField);
        UiKit.row(form, 2, "Price (RM)", priceField);
        UiKit.row(form, 3, "Customer name", customerField);
        UiKit.row(form, 4, "Customer salary (RM)", salaryField);

        JButton checkBtn = UiKit.button("Check price", UiKit.Kind.PRIMARY);
        JButton demoBtn = UiKit.button("Run demo", UiKit.Kind.SECONDARY);
        JButton clearBtn = UiKit.button("Clear", UiKit.Kind.DANGER);

        JPanel inputBody = new JPanel(new BorderLayout(0, 8));
        inputBody.setOpaque(false);
        inputBody.add(form, BorderLayout.NORTH);
        inputBody.add(UiKit.buttons(checkBtn, demoBtn, clearBtn), BorderLayout.CENTER);
        inputBody.add(status, BorderLayout.SOUTH);

        banner.setOpaque(true);
        banner.setFont(UiKit.BOLD);
        banner.setBorder(new EmptyBorder(10, 10, 10, 10));
        setBanner("No result yet", null);

        JPanel outputBody = new JPanel(new BorderLayout(0, 8));
        outputBody.setOpaque(false);
        outputBody.add(banner, BorderLayout.NORTH);
        outputBody.add(UiKit.scroll(output), BorderLayout.CENTER);

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.add(UiKit.card("Customer and product", inputBody), BorderLayout.NORTH);
        body.add(UiKit.card("Result", outputBody), BorderLayout.CENTER);

        add(UiKit.page(
            UiKit.header("Price", "Can the customer afford it? (Price interface)"),
            body), BorderLayout.CENTER);

        itemBox.addActionListener(e -> onItemChanged());
        checkBtn.addActionListener(e -> onCheck());
        demoBtn.addActionListener(e -> onDemo());
        clearBtn.addActionListener(e -> onClear());
    }

    // ---- actions ----

    private void onItemChanged() {
        switch ((String) itemBox.getSelectedItem()) {
            case "SmartPhone": nameField.setText("Iphone 14 ProMax"); priceField.setText("5500.00"); break;
            case "Laptop": nameField.setText("Lenovo Yoga"); priceField.setText("6000.00"); break;
            default: nameField.setText("BMW"); priceField.setText("200000");
        }
    }

    private void onCheck() {
        String name = nameField.getText().trim();
        String customerName = customerField.getText().trim();
        if (name.isEmpty() || customerName.isEmpty()) {
            UiKit.say(status, "Fill in the product and customer name.", false);
            return;
        }
        double price, salary;
        try {
            price = Double.parseDouble(priceField.getText().trim());
            salary = Double.parseDouble(salaryField.getText().trim());
        } catch (NumberFormatException ex) {
            UiKit.say(status, "Price and salary must be numbers.", false);
            return;
        }

        Price item;   // polymorphism: one interface, three different classes
        switch ((String) itemBox.getSelectedItem()) {
            case "SmartPhone": item = new SmartPhone("SmartPhone", name, price); break;
            case "Laptop": item = new Laptop("Laptop", name, price); break;
            default: item = new Car(name, price);
        }
        check(item, new Customer(customerName, salary));
        UiKit.say(status, "Result shown below.", true);
    }

    /** Same data as PriceApp.main. */
    private void onDemo() {
        check(new SmartPhone("SmartPhone", "Iphone 14 ProMax", 5500.00), new Customer("Sally", 3000.00));
        check(new Laptop("Laptop", "Lenovo Yoga", 6000.00), new Customer("Aaron", 8000.00));
        check(new Car("BMW", 200000), new Customer("Simon", 10000.00));
        UiKit.say(status, "Demo finished.", true);
    }

    private void onClear() {
        output.setText("");
        setBanner("No result yet", null);
        UiKit.say(status, "Output cleared.", true);
    }

    /** getPrice() prints to the console, so its text is captured and shown in the window instead. */
    private void check(Price item, Customer customer) {
        String text = UiKit.capture(() -> item.getPrice(customer));
        output.append(text + "\n");
        boolean canBuy = !text.contains("cannot buy");
        String thing = (item instanceof Car) ? "car" : "product";
        setBanner(customer.getName() + (canBuy ? " can buy this " : " cannot buy this ") + thing, canBuy);
    }

    private void setBanner(String text, Boolean good) {
        banner.setText(text);
        if (good == null) {
            banner.setBackground(new Color(0xF1F5F9));
            banner.setForeground(UiKit.MUTED);
        } else {
            banner.setBackground(good ? UiKit.OK_BG : UiKit.BAD_BG);
            banner.setForeground(good ? UiKit.OK : UiKit.BAD);
        }
    }
}
