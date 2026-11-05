// Q2 - EdibleApp (GUI version)
// Compile: javac *.java     Run: java EdibleGUI
//
// Structure
//   EdibleApp.java  - original console program + model classes (Edible, Animal, Fruit, Apple, Orange, Person)
//   UiKit.java      - shared colors, fonts and small UI helpers
//   EdibleGUI.java  - EdibleGUI (window) -> EdiblePanel (page)

import javax.swing.*;
import java.awt.*;

/** The window. It only hosts the panel and handles start-up. */
public class EdibleGUI extends JFrame {

    public EdibleGUI() {
        super("Q2 - Edible App");
        setContentPane(new EdiblePanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 600);
        setMinimumSize(new Dimension(520, 520));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        UiKit.install();
        SwingUtilities.invokeLater(() -> new EdibleGUI().setVisible(true));
    }
}

/** The whole page: header, input card and output card. */
class EdiblePanel extends JPanel {

    private final JTextField personField = UiKit.field("Ali");
    private final JComboBox<String> typeBox = UiKit.combo("Animal", "Apple", "Orange", "Fruit");
    private final JTextField foodField = UiKit.field("Chicken");
    private final JLabel status = UiKit.status();
    private final JTextArea output = UiKit.output();

    EdiblePanel() {
        super(new BorderLayout());

        JPanel form = UiKit.form();
        UiKit.row(form, 0, "Person's name", personField);
        UiKit.row(form, 1, "Type of food", typeBox);
        UiKit.row(form, 2, "Food name", foodField);

        JButton eatBtn = UiKit.button("Eat", UiKit.Kind.PRIMARY);
        JButton demoBtn = UiKit.button("Run demo", UiKit.Kind.SECONDARY);
        JButton clearBtn = UiKit.button("Clear", UiKit.Kind.DANGER);

        JPanel inputBody = new JPanel(new BorderLayout(0, 8));
        inputBody.setOpaque(false);
        inputBody.add(form, BorderLayout.NORTH);
        inputBody.add(UiKit.buttons(eatBtn, demoBtn, clearBtn), BorderLayout.CENTER);
        inputBody.add(status, BorderLayout.SOUTH);

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.add(UiKit.card("Who is eating what?", inputBody), BorderLayout.NORTH);
        body.add(UiKit.card("Output", UiKit.scroll(output)), BorderLayout.CENTER);

        add(UiKit.page(
            UiKit.header("Edible App", "Polymorphism with the Edible interface"),
            body), BorderLayout.CENTER);

        typeBox.addActionListener(e -> onTypeChanged());
        eatBtn.addActionListener(e -> onEat());
        demoBtn.addActionListener(e -> onDemo());
        clearBtn.addActionListener(e -> onClear());
        foodField.addActionListener(e -> onEat());
    }

    // ---- actions ----

    private void onTypeChanged() {
        switch ((String) typeBox.getSelectedItem()) {
            case "Animal": foodField.setText("Chicken"); break;
            case "Apple": foodField.setText("Red Apple"); break;
            case "Orange": foodField.setText("Navel Orange"); break;
            default: foodField.setText("Banana");
        }
    }

    private void onEat() {
        String person = personField.getText().trim();
        String food = foodField.getText().trim();
        if (person.isEmpty() || food.isEmpty()) {
            UiKit.say(status, "Enter the person's name and the food name.", false);
            return;
        }
        Edible edible = createEdible((String) typeBox.getSelectedItem(), food);
        output.append(new Person(person).eating(edible) + "\n\n");
        UiKit.say(status, person + " is eating " + food + ".", true);
    }

    /** Same logic as EdibleApp.main: animals go to Ali, everything else to Sarah. */
    private void onDemo() {
        Edible[] objects = {new Animal("Chicken"), new Apple("Red Apple"), new Orange("Navel Orange")};
        Person p1 = new Person("Ali");
        Person p2 = new Person("Sarah");
        for (Edible o : objects) {
            Person p = (o instanceof Animal) ? p1 : p2;
            output.append(p.eating(o) + "\n\n");
        }
        UiKit.say(status, "Demo finished.", true);
    }

    private void onClear() {
        output.setText("");
        UiKit.say(status, "Output cleared.", true);
    }

    private static Edible createEdible(String type, String name) {
        switch (type) {
            case "Animal": return new Animal(name);
            case "Apple": return new Apple(name);
            case "Orange": return new Orange(name);
            default: return new Fruit(name);
        }
    }
}
