// Q1 - DataSet: javac DataSetApp.java && java DataSetApp

import java.text.DecimalFormat;

public class DataSetApp {
    public static void main(String[] args) {
        DecimalFormat df = new DecimalFormat("0.00");
        DataSet bankData = new DataSet();
        bankData.add(new BankAccount(0));
        bankData.add(new BankAccount(10000));
        bankData.add(new BankAccount(2000));

        System.out.println("Bank Average Balance: " + bankData.getAverage());
        Measurable max = bankData.getMax();
        System.out.println("Bank Highest Balance: " + max.getMeasure());

        DataSet coinData = new DataSet();
        coinData.add(new Coin(0.25, "Quarter"));
        coinData.add(new Coin(0.1, "Dime"));
        coinData.add(new Coin(0.05, "Nickel"));
        System.out.println("Average Coin Value: " + df.format(coinData.getAverage()));

        max = coinData.getMax();
        System.out.println("Highest Coin Balance: " + df.format(max.getMeasure()));
        System.out.println("Highest Coin's Name: " + max.getName());
        DataSet student1 = new DataSet();
        student1.add(new Student("Ali", 157.50));
        student1.add(new Student("Alex", 165.00));
        student1.add(new Student("Maria", 147.00));
        System.out.println("Average Student's Height: " + df.format(student1.getAverage()));
        max = student1.getMax();
        System.out.println("Tallest Height: " + df.format(max.getMeasure()));
        System.out.println("Tallest Student's Name: " + max.getName());
    }
}

// ---- supporting classes ----

interface Measurable {
    double getMeasure();
    String getName();
}

class BankAccount implements Measurable {
    private int accNumber;
    private double balance;
    private static int lastAssignedNumber = 1000;

    public BankAccount() { this(0); }
    public BankAccount(double balance) {
        lastAssignedNumber++;
        accNumber = lastAssignedNumber;
        this.balance = balance;
    }
    public int getAccountNumber() { return accNumber; }
    public double getMeasure() { return balance; }
    public String getName() { return "Account " + accNumber; }
}

class Coin implements Measurable {
    private double value;
    private String name;

    public Coin(double value, String name) { this.value = value; this.name = name; }
    public double getValue() { return value; }
    public double getMeasure() { return value; }
    public String getName() { return name; }
}

class Student implements Measurable {
    private String name;
    private double height;

    public Student(String name, double height) { this.name = name; this.height = height; }
    public double getHeight() { return height; }
    public double getMeasure() { return height; }
    public String getName() { return name; }
}

class DataSet implements Measurable {
    private double sum;
    private Measurable max;
    private int count;
    private String name = "DataSet";

    public DataSet() { sum = 0; count = 0; max = null; }

    public void add(Measurable x) {
        sum += x.getMeasure();
        if (count == 0 || x.getMeasure() > max.getMeasure())
            max = x;
        count++;
    }
    public Measurable getMax() { return max; }
    public double getAverage() { return count == 0 ? 0 : sum / count; }

    public double getMeasure() { return getAverage(); }
    public String getName() { return name; }
}
