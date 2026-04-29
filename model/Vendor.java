package model;

import java.util.List;

public class Vendor {
    private String name;
    private String phone;
    private List<String> serviceTypes;
    private double minPrice;
    private double maxPrice;

    public Vendor(String name, String phone, List<String> serviceTypes,
                  double minPrice, double maxPrice) {
        this.name = name;
        this.phone = phone;
        this.serviceTypes = serviceTypes;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public List<String> getServiceTypes() { return serviceTypes; }
    public double getMinPrice() { return minPrice; }
    public double getMaxPrice() { return maxPrice; }
}