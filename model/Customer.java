package model;

public class Customer {
    private String name;
    private String phone;
    private String eventType;
    private double budget;

    public Customer(String name, String phone, String eventType, double budget) {
        this.name = name;
        this.phone = phone;
        this.eventType = eventType;
        this.budget = budget;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEventType() { return eventType; }
    public double getBudget() { return budget; }
}