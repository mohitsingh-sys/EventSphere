package service;

import db.DBConnection;
import model.Customer;
import model.Vendor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventService {


    public void addCustomer(Customer c) {
        String sql = "INSERT INTO customers (name, phone, event_type, budget) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEventType());
            ps.setDouble(4, c.getBudget());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Customer> getCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("event_type"),
                        rs.getDouble("budget")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteCustomer(int index) {
        List<Customer> list = getCustomers();
        if (index < 0 || index >= list.size()) return;
        Customer c = list.get(index);
        String sql = "DELETE FROM customers WHERE name=? AND phone=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addVendor(Vendor v) {
        String sql = "INSERT INTO vendors (name, phone, services, min_price, max_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getName());
            ps.setString(2, v.getPhone());
            ps.setString(3, String.join("|", v.getServiceTypes()));
            ps.setDouble(4, v.getMinPrice());
            ps.setDouble(5, v.getMaxPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vendor> getVendors() {
        List<Vendor> list = new ArrayList<>();
        String sql = "SELECT * FROM vendors";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                List<String> services = Arrays.asList(
                        rs.getString("services").split("\\|"));
                list.add(new Vendor(
                        rs.getString("name"),
                        rs.getString("phone"),
                        services,
                        rs.getDouble("min_price"),
                        rs.getDouble("max_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteVendor(int index) {
        List<Vendor> list = getVendors();
        if (index < 0 || index >= list.size()) return;
        Vendor v = list.get(index);
        String sql = "DELETE FROM vendors WHERE name=? AND phone=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getName());
            ps.setString(2, v.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    public List<Vendor> getMatchingVendors(String eventType, double budget) {
        List<Vendor> result = new ArrayList<>();
        for (Vendor v : getVendors()) {
            if (v.getServiceTypes().contains(eventType) &&
                v.getMinPrice() <= budget &&
                v.getMaxPrice() >= budget) {
                result.add(v);
            }
        }
        return result;
    }
}