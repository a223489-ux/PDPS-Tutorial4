// Q2 - EdibleApp (GUI version)
// Compile: javac *.java     Run: java EdibleGUI
// Uses the classes from EdibleApp.java (Edible, Animal, Fruit, Apple, Orange, Person).

import javax.swing.*;
import java.awt.*;

public class EdibleGUI extends JFrame {

    private final JTextField personField = new JTextField("Ali");
    private final JComboBox<String> typeBox =
        new JComboBox<>(new String[]{"Animal", "Apple", "Orange", "Fruit"});
    private final JTextField foodField = new JTextField("Chicken");
    private final JTextArea output = new JTextArea();

    public EdibleGUI() {
        super("Q2 - Edible App");
        setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(3, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        form.add(new JLabel("Person's name:"));
        form.add(personField);
        form.add(new JLabel("Type of food:"));
        form.add(typeBox);
        form.add(new JLabel("Food name:"));
        form.add(foodField);

        JButton eatBtn = new JButton("Eat");
        JButton demoBtn = new JButton("Run Demo (same as console)");
        JButton clearBtn = new JButton("Clear");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(eatBtn);
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

        // Selecting a type fills in a sample food name
        typeBox.addActionListener(e -> {
            switch ((String) typeBox.getSelectedItem()) {
                case "Animal": foodField.setText("Chicken"); break;
                case "Apple": foodField.setText("Red Apple"); break;
                case "Orange": foodField.setText("Navel Orange"); break;
                default: foodField.setText("Banana");
            }
        });

        eatBtn.addActionListener(e -> {
            String person = personField.getText().trim();
            String food = foodField.getText().trim();
            if (person.isEmpty() || food.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the person's name and the food name.",
                    "Missing input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Edible edible;
            switch ((String) typeBox.getSelectedItem()) {
                case "Animal": edible = new Animal(food); break;
                case "Apple": edible = new Apple(food); break;
                case "Orange": edible = new Orange(food); break;
                default: edible = new Fruit(food);
            }
            Person p = new Person(person);
            output.append(p.eating(edible) + "\n\n");
        });

        demoBtn.addActionListener(e -> {
            Edible[] objects = {new Animal("Chicken"), new Apple("Red Apple"), new Orange("Navel Orange")};
            Person p1 = new Person("Ali");
            Person p2 = new Person("Sarah");
            for (Edible o : objects) {
                if (o instanceof Animal) output.append(p1.eating(o) + "\n\n");
                else output.append(p2.eating(o) + "\n\n");
            }
        });

        clearBtn.addActionListener(e -> output.setText(""));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(560, 400);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EdibleGUI().setVisible(true));
    }
}
