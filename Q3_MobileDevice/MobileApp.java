// Q3 - Mobile Device: javac MobileApp.java && java MobileApp

// MobileApp (provided)
public class MobileApp {
    public static void main(String[] args) {
        MobileDevice m1 = new SmartPhone("Samsung", "S20", "Galaxy");
        MobileDevice m2 = new BasicPhone("Sonny Ericson", "Walkman");
        m1.setStation("Hot FM");
        m1.setFrequency(97.60);
        m2.setStation("Fly FM");
        m2.setFrequency(109.8);
        m1.printDetails();
        System.out.println("Station:" + m1.station);
        System.out.println("Frqeuency:" + m1.frequency);
        m2.printDetails();
        System.out.println("Station:" + m2.station);
        System.out.println("Frequency:" + m2.frequency);
    }
}

// ---- supporting classes ----

interface RadioTuner {
    void setStation(String station);
    void setFrequency(double frequency);
}

class MobileDevice implements RadioTuner {
    protected String manufacturer;
    protected String model;
    public String station;      // public: MobileApp reads m1.station directly
    public double frequency;    // public: MobileApp reads m1.frequency directly

    public MobileDevice(String manufacturer, String model) {
        this.manufacturer = manufacturer;
        this.model = model;
    }

    @Override
    public void setStation(String station) { this.station = station; }
    @Override
    public void setFrequency(double frequency) { this.frequency = frequency; }

    public void printDetails() {
        System.out.println("Manufacturer: " + manufacturer);
        System.out.println("Model: " + model);
    }
}

class SmartPhone extends MobileDevice {
    private String type;

    public SmartPhone(String manufacturer, String model, String type) {
        super(manufacturer, model);
        this.type = type;
    }

    @Override
    public void printDetails() {
        System.out.println("SmartPhone Detail:");
        super.printDetails();
        System.out.println("Type: " + type);
    }
}

class BasicPhone extends MobileDevice {
    public BasicPhone(String manufacturer, String model) {
        super(manufacturer, model);
    }

    @Override
    public void printDetails() {
        System.out.println("Basic Phone Detail:");
        super.printDetails();
    }
}
