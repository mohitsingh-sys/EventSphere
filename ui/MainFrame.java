package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Vendor;
import service.EventService;

public class MainFrame extends JFrame {

    private EventService service = new EventService();
    private DefaultTableModel customerTableModel;
    private DefaultTableModel vendorTableModel;
    private int selectedCustomerIndex = -1;
    private int selectedVendorIndex = -1;

    public MainFrame() {
        setTitle("EventSphere Dashboard");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();


        JPanel customerPanel = new JPanel(new BorderLayout());
        JPanel cForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField cName = new JTextField(12);
        JTextField cPhone = new JTextField(12);
        JTextField cBudget = new JTextField(12);

        JCheckBox cBirthday = new JCheckBox("Birthday Party");
        JCheckBox cKirtan = new JCheckBox("Kirtan");
        JCheckBox cAnniversary = new JCheckBox("Anniversary");
        JCheckBox cRing = new JCheckBox("Ring Ceremony");

        JPanel cServicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cServicePanel.add(cBirthday);
        cServicePanel.add(cKirtan);
        cServicePanel.add(cAnniversary);
        cServicePanel.add(cRing);

        JButton addCustomerBtn = new JButton("Add");
        JButton updateCustomerBtn = new JButton("Update");
        JButton deleteCustomerBtn = new JButton("Delete");

        gbc.gridx=0; gbc.gridy=0; cForm.add(new JLabel("Name:"), gbc);
        gbc.gridx=1; cForm.add(cName, gbc);
        gbc.gridx=0; gbc.gridy=1; cForm.add(new JLabel("Phone:"), gbc);
        gbc.gridx=1; cForm.add(cPhone, gbc);
        gbc.gridx=0; gbc.gridy=2; cForm.add(new JLabel("Event Type:"), gbc);
        gbc.gridx=1; cForm.add(cServicePanel, gbc);
        gbc.gridx=0; gbc.gridy=3; cForm.add(new JLabel("Budget:"), gbc);
        gbc.gridx=1; cForm.add(cBudget, gbc);

        JPanel cBtnPanel = new JPanel();
        cBtnPanel.add(addCustomerBtn);
        cBtnPanel.add(updateCustomerBtn);
        cBtnPanel.add(deleteCustomerBtn);
        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2;
        cForm.add(cBtnPanel, gbc);

        customerPanel.add(cForm, BorderLayout.NORTH);

        String[] cCols = {"Name","Phone","Event","Budget"};
        customerTableModel = new DefaultTableModel(cCols,0);
        JTable cTable = new JTable(customerTableModel);
        customerPanel.add(new JScrollPane(cTable), BorderLayout.CENTER);
        loadCustomers();

        cTable.getSelectionModel().addListSelectionListener(e -> {
            int row = cTable.getSelectedRow();
            if(row >= 0){
                selectedCustomerIndex = row;
                cName.setText(customerTableModel.getValueAt(row,0).toString());
                cPhone.setText(customerTableModel.getValueAt(row,1).toString());
                String events = customerTableModel.getValueAt(row,2).toString();
                cBirthday.setSelected(events.contains("Birthday"));
                cKirtan.setSelected(events.contains("Kirtan"));
                cAnniversary.setSelected(events.contains("Anniversary"));
                cRing.setSelected(events.contains("Ring"));
                cBudget.setText(customerTableModel.getValueAt(row,3).toString());
            }
        });

        
        addCustomerBtn.addActionListener(e -> {
            try {
                if (cName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                    return;
                }
                if (!isValidPhone(cPhone.getText())) {
                    JOptionPane.showMessageDialog(this, "Invalid phone number! Must be exactly 10 digits.");
                    return;
                }
                List<String> selectedEvents = getSelectedServices(cBirthday, cKirtan, cAnniversary, cRing);
                if (selectedEvents.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select at least one event type!");
                    return;
                }
                if (cBudget.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Budget cannot be empty!");
                    return;
                }
                String eventTypes = String.join("|", selectedEvents);
                Customer c = new Customer(
                        cName.getText(),
                        cPhone.getText(),
                        eventTypes,
                        Double.parseDouble(cBudget.getText())
                );
                service.addCustomer(c);
                customerTableModel.addRow(new Object[]{
                        c.getName(), c.getPhone(), c.getEventType(), c.getBudget()
                });
                clearCustomerFields(cName, cPhone, cBudget);
                cBirthday.setSelected(false);
                cKirtan.setSelected(false);
                cAnniversary.setSelected(false);
                cRing.setSelected(false);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Invalid input!");
            }
        });

       
        updateCustomerBtn.addActionListener(e -> {
            if(selectedCustomerIndex >= 0){
                try {
                    if (cName.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                        return;
                    }
                    if (!isValidPhone(cPhone.getText())) {
                        JOptionPane.showMessageDialog(this, "Invalid phone number! Must be exactly 10 digits.");
                        return;
                    }
                    List<String> selectedEvents = getSelectedServices(cBirthday, cKirtan, cAnniversary, cRing);
                    if (selectedEvents.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please select at least one event type!");
                        return;
                    }
                    if (cBudget.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Budget cannot be empty!");
                        return;
                    }
                    String eventTypes = String.join("|", selectedEvents);
                    Customer c = new Customer(
                            cName.getText(),
                            cPhone.getText(),
                            eventTypes,
                            Double.parseDouble(cBudget.getText())
                    );
                    service.getCustomers().set(selectedCustomerIndex, c);
                    customerTableModel.setValueAt(c.getName(), selectedCustomerIndex, 0);
                    customerTableModel.setValueAt(c.getPhone(), selectedCustomerIndex, 1);
                    customerTableModel.setValueAt(c.getEventType(), selectedCustomerIndex, 2);
                    customerTableModel.setValueAt(c.getBudget(), selectedCustomerIndex, 3);
                    clearCustomerFields(cName, cPhone, cBudget);
                    cBirthday.setSelected(false);
                    cKirtan.setSelected(false);
                    cAnniversary.setSelected(false);
                    cRing.setSelected(false);
                    selectedCustomerIndex = -1;
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(this,"Invalid input!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to update!");
            }
        });

        
        deleteCustomerBtn.addActionListener(e -> {
            int row = cTable.getSelectedRow();
            if(row >= 0){
                int confirm = JOptionPane.showConfirmDialog(this,"Delete this customer?");
                if(confirm == JOptionPane.YES_OPTION){
                    service.deleteCustomer(row);
                    customerTableModel.removeRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to delete!");
            }
        });

       
        JPanel vendorPanel = new JPanel(new BorderLayout());
        JPanel vForm = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField vName = new JTextField(12);
        JTextField vPhone = new JTextField(12);
        JTextField vMin = new JTextField(8);
        JTextField vMax = new JTextField(8);

        JCheckBox cbBirthday = new JCheckBox("Birthday Party");
        JCheckBox cbKirtan = new JCheckBox("Kirtan");
        JCheckBox cbAnniversary = new JCheckBox("Anniversary");
        JCheckBox cbRing = new JCheckBox("Ring Ceremony");

        JPanel servicePanel = new JPanel();
        servicePanel.add(cbBirthday);
        servicePanel.add(cbKirtan);
        servicePanel.add(cbAnniversary);
        servicePanel.add(cbRing);

        JButton addVendorBtn = new JButton("Add");
        JButton updateVendorBtn = new JButton("Update");
        JButton deleteVendorBtn = new JButton("Delete");

        gbc.gridx=0; gbc.gridy=0; vForm.add(new JLabel("Name:"), gbc);
        gbc.gridx=1; vForm.add(vName, gbc);
        gbc.gridx=0; gbc.gridy=1; vForm.add(new JLabel("Phone:"), gbc);
        gbc.gridx=1; vForm.add(vPhone, gbc);
        gbc.gridx=0; gbc.gridy=2; vForm.add(new JLabel("Services:"), gbc);
        gbc.gridx=1; vForm.add(servicePanel, gbc);
        gbc.gridx=0; gbc.gridy=3; vForm.add(new JLabel("Min Price:"), gbc);
        gbc.gridx=1; vForm.add(vMin, gbc);
        gbc.gridx=0; gbc.gridy=4; vForm.add(new JLabel("Max Price:"), gbc);
        gbc.gridx=1; vForm.add(vMax, gbc);

        JPanel vBtnPanel = new JPanel();
        vBtnPanel.add(addVendorBtn);
        vBtnPanel.add(updateVendorBtn);
        vBtnPanel.add(deleteVendorBtn);
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2;
        vForm.add(vBtnPanel, gbc);

        vendorPanel.add(vForm, BorderLayout.NORTH);

        String[] vCols = {"Name","Phone","Services","Min","Max"};
        vendorTableModel = new DefaultTableModel(vCols,0);
        JTable vTable = new JTable(vendorTableModel);
        vendorPanel.add(new JScrollPane(vTable), BorderLayout.CENTER);
        loadVendors();

        vTable.getSelectionModel().addListSelectionListener(e -> {
            int row = vTable.getSelectedRow();
            if(row >= 0){
                selectedVendorIndex = row;
                vName.setText(vendorTableModel.getValueAt(row,0).toString());
                vPhone.setText(vendorTableModel.getValueAt(row,1).toString());
                String services = vendorTableModel.getValueAt(row,2).toString();
                cbBirthday.setSelected(services.contains("Birthday"));
                cbKirtan.setSelected(services.contains("Kirtan"));
                cbAnniversary.setSelected(services.contains("Anniversary"));
                cbRing.setSelected(services.contains("Ring"));
                vMin.setText(vendorTableModel.getValueAt(row,3).toString());
                vMax.setText(vendorTableModel.getValueAt(row,4).toString());
            }
        });

       
        addVendorBtn.addActionListener(e -> {
            try {
                if (vName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                    return;
                }
                if (!isValidPhone(vPhone.getText())) {
                    JOptionPane.showMessageDialog(this, "Invalid phone number! Must be exactly 10 digits.");
                    return;
                }
                List<String> services = getSelectedServices(cbBirthday,cbKirtan,cbAnniversary,cbRing);
                if (services.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select at least one service!");
                    return;
                }
                if (vMin.getText().trim().isEmpty() || vMax.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Min and Max price cannot be empty!");
                    return;
                }
                Vendor v = new Vendor(
                        vName.getText(),
                        vPhone.getText(),
                        services,
                        Double.parseDouble(vMin.getText()),
                        Double.parseDouble(vMax.getText())
                );
                service.addVendor(v);
                vendorTableModel.addRow(new Object[]{
                        v.getName(), v.getPhone(), String.join(", ",services),
                        v.getMinPrice(), v.getMaxPrice()
                });
                clearVendorFields(vName,vPhone,vMin,vMax,cbBirthday,cbKirtan,cbAnniversary,cbRing);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Invalid input!");
            }
        });

       
        updateVendorBtn.addActionListener(e -> {
            if(selectedVendorIndex >= 0){
                try {
                    if (vName.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                        return;
                    }
                    if (!isValidPhone(vPhone.getText())) {
                        JOptionPane.showMessageDialog(this, "Invalid phone number! Must be exactly 10 digits.");
                        return;
                    }
                    List<String> services = getSelectedServices(cbBirthday,cbKirtan,cbAnniversary,cbRing);
                    if (services.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please select at least one service!");
                        return;
                    }
                    if (vMin.getText().trim().isEmpty() || vMax.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Min and Max price cannot be empty!");
                        return;
                    }
                    Vendor v = new Vendor(
                            vName.getText(),
                            vPhone.getText(),
                            services,
                            Double.parseDouble(vMin.getText()),
                            Double.parseDouble(vMax.getText())
                    );
                    service.deleteVendor(selectedVendorIndex);
                    service.addVendor(v);
                    vendorTableModel.setValueAt(v.getName(),selectedVendorIndex,0);
                    vendorTableModel.setValueAt(v.getPhone(),selectedVendorIndex,1);
                    vendorTableModel.setValueAt(String.join(", ",services),selectedVendorIndex,2);
                    vendorTableModel.setValueAt(v.getMinPrice(),selectedVendorIndex,3);
                    vendorTableModel.setValueAt(v.getMaxPrice(),selectedVendorIndex,4);
                    clearVendorFields(vName,vPhone,vMin,vMax,cbBirthday,cbKirtan,cbAnniversary,cbRing);
                    selectedVendorIndex = -1;
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(this,"Invalid input!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a vendor to update!");
            }
        });

        
        deleteVendorBtn.addActionListener(e -> {
            int row = vTable.getSelectedRow();
            if(row >= 0){
                int confirm = JOptionPane.showConfirmDialog(this,"Delete this vendor?");
                if(confirm == JOptionPane.YES_OPTION){
                    service.deleteVendor(row);
                    vendorTableModel.removeRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a vendor to delete!");
            }
        });

        
        JPanel matchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMatch = new GridBagConstraints();
        gbcMatch.insets = new Insets(5,5,5,5);

        JCheckBox mBirthday = new JCheckBox("Birthday Party");
        JCheckBox mKirtan = new JCheckBox("Kirtan");
        JCheckBox mAnniversary = new JCheckBox("Anniversary");
        JCheckBox mRing = new JCheckBox("Ring Ceremony");

        JPanel mServicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mServicePanel.add(mBirthday);
        mServicePanel.add(mKirtan);
        mServicePanel.add(mAnniversary);
        mServicePanel.add(mRing);

        JTextField mBudget = new JTextField(12);
        JButton matchBtn = new JButton("Find Vendors");

        JTextArea result = new JTextArea(12,35);
        result.setEditable(false);
        JScrollPane scroll = new JScrollPane(result);

        gbcMatch.gridx=0; gbcMatch.gridy=0;
        matchPanel.add(new JLabel("Event Type:"), gbcMatch);
        gbcMatch.gridx=1;
        matchPanel.add(mServicePanel, gbcMatch);
        gbcMatch.gridx=0; gbcMatch.gridy=1;
        matchPanel.add(new JLabel("Budget:"), gbcMatch);
        gbcMatch.gridx=1;
        matchPanel.add(mBudget, gbcMatch);
        gbcMatch.gridx=0; gbcMatch.gridy=2; gbcMatch.gridwidth=2;
        matchPanel.add(matchBtn, gbcMatch);
        gbcMatch.gridy=3;
        matchPanel.add(scroll, gbcMatch);

        matchBtn.addActionListener(e -> {
            try {
                List<String> selectedEvents = getSelectedServices(mBirthday, mKirtan, mAnniversary, mRing);
                if (selectedEvents.isEmpty()) {
                    result.setText("Please select at least one event type!");
                    return;
                }
                if (mBudget.getText().trim().isEmpty()) {
                    result.setText("Please enter a budget!");
                    return;
                }
                result.setText("");
                for (String eventType : selectedEvents) {
                    List<Vendor> list = service.getMatchingVendors(
                            eventType,
                            Double.parseDouble(mBudget.getText())
                    );
                    for (Vendor v : list) {
                        result.append(v.getName() + " | " + v.getPhone() + " | " +
                                String.join(", ", v.getServiceTypes()) + " | " +
                                v.getMinPrice() + "-" + v.getMaxPrice() + "\n");
                    }
                }
                if (result.getText().isEmpty()) result.setText("No vendors found.");
            } catch(Exception ex) {
                result.setText("Invalid input!");
            }
        });

        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        add(tabs, BorderLayout.CENTER);
        add(logoutBtn, BorderLayout.SOUTH);

        tabs.addTab("Customers", customerPanel);
        tabs.addTab("Vendors", vendorPanel);
        tabs.addTab("Match Vendors", matchPanel);
    }

    
    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }

    private List<String> getSelectedServices(JCheckBox... boxes){
        List<String> list = new ArrayList<>();
        for(JCheckBox cb : boxes){
            if(cb.isSelected()) list.add(cb.getText());
        }
        return list;
    }

    private void clearCustomerFields(JTextField... fields){
        for(JTextField f : fields) f.setText("");
    }

    private void clearVendorFields(JTextField vName, JTextField vPhone,
                                   JTextField vMin, JTextField vMax,
                                   JCheckBox... boxes){
        vName.setText(""); vPhone.setText(""); vMin.setText(""); vMax.setText("");
        for(JCheckBox cb : boxes) cb.setSelected(false);
    }

    private void loadCustomers(){
        for(Customer c : service.getCustomers()){
            customerTableModel.addRow(new Object[]{
                    c.getName(), c.getPhone(), c.getEventType(), c.getBudget()
            });
        }
    }

    private void loadVendors(){
        for(Vendor v : service.getVendors()){
            vendorTableModel.addRow(new Object[]{
                    v.getName(),
                    v.getPhone(),
                    String.join(", ",v.getServiceTypes()),
                    v.getMinPrice(),
                    v.getMaxPrice()
            });
        }
    }
}