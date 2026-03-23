package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class HistoryFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private JTable loginHistoryTable;
    private JTable parkingHistoryTable;
    private JTable userAnalyticsTable;
    private int currentUserId;

    public HistoryFrame(int userId) {
        this.currentUserId = userId;
        
        setTitle("User History & Analytics");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Tab 1: Login History
        tabbedPane.addTab("Login History", createLoginHistoryPanel());
        
        // Tab 2: Parking History
        tabbedPane.addTab("Parking History", createParkingHistoryPanel());
        
        // Tab 3: User Analytics
        tabbedPane.addTab("Analytics", createAnalyticsPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // LOGIN HISTORY PANEL
    private JPanel createLoginHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel titleLabel = new JLabel("Login History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create table
        loginHistoryTable = new JTable();
        loadLoginHistory();
        JScrollPane scrollPane = new JScrollPane(loginHistoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadLoginHistory());
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // PARKING HISTORY PANEL
    private JPanel createParkingHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel titleLabel = new JLabel("Parking History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create table
        parkingHistoryTable = new JTable();
        loadParkingHistory();
        JScrollPane scrollPane = new JScrollPane(parkingHistoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadParkingHistory());
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ANALYTICS PANEL
    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel titleLabel = new JLabel("Your Analytics & Statistics");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create table
        userAnalyticsTable = new JTable();
        loadUserAnalytics();
        JScrollPane scrollPane = new JScrollPane(userAnalyticsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadUserAnalytics());
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // LOAD LOGIN HISTORY
    private void loadLoginHistory() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT login_time, logout_time, session_duration_minutes, ip_address, status " +
                         "FROM login_history WHERE user_id = ? ORDER BY login_time DESC";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, currentUserId);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Login Time");
            model.addColumn("Logout Time");
            model.addColumn("Duration (minutes)");
            model.addColumn("IP Address");
            model.addColumn("Status");

            while (rs.next()) {
                Object[] row = {
                    rs.getString("login_time"),
                    rs.getString("logout_time"),
                    rs.getInt("session_duration_minutes"),
                    rs.getString("ip_address"),
                    rs.getString("status")
                };
                model.addRow(row);
            }

            loginHistoryTable.setModel(model);
            rs.close();
            pst.close();
            con.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading login history: " + ex.getMessage());
        }
    }

    // LOAD PARKING HISTORY
    private void loadParkingHistory() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT plate_number, vehicle_type, spot_name, entry_time, exit_time, " +
                         "duration_minutes, total_fee, payment_status " +
                         "FROM parking_history WHERE user_id = ? ORDER BY entry_time DESC";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, currentUserId);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Plate");
            model.addColumn("Vehicle Type");
            model.addColumn("Spot");
            model.addColumn("Entry Time");
            model.addColumn("Exit Time");
            model.addColumn("Duration (min)");
            model.addColumn("Fee ($)");
            model.addColumn("Payment Status");

            while (rs.next()) {
                Object[] row = {
                    rs.getString("plate_number"),
                    rs.getString("vehicle_type"),
                    rs.getString("spot_name"),
                    rs.getString("entry_time"),
                    rs.getString("exit_time"),
                    rs.getInt("duration_minutes"),
                    String.format("%.2f", rs.getDouble("total_fee")),
                    rs.getString("payment_status")
                };
                model.addRow(row);
            }

            parkingHistoryTable.setModel(model);
            rs.close();
            pst.close();
            con.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading parking history: " + ex.getMessage());
        }
    }

    // LOAD USER ANALYTICS
    private void loadUserAnalytics() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT total_logins, total_parking_sessions, total_parking_duration_minutes, " +
                         "total_parking_fee, most_used_spot, average_parking_duration_minutes, " +
                         "last_parking_date, last_login_date " +
                         "FROM user_analytics WHERE user_id = ?";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, currentUserId);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Metric");
            model.addColumn("Value");

            if (rs.next()) {
                model.addRow(new Object[]{"Total Logins", rs.getInt("total_logins")});
                model.addRow(new Object[]{"Total Parking Sessions", rs.getInt("total_parking_sessions")});
                model.addRow(new Object[]{"Total Parking Duration (minutes)", rs.getInt("total_parking_duration_minutes")});
                model.addRow(new Object[]{"Total Parking Fee ($)", String.format("%.2f", rs.getDouble("total_parking_fee"))});
                model.addRow(new Object[]{"Most Used Spot", rs.getString("most_used_spot")});
                model.addRow(new Object[]{"Average Parking Duration (minutes)", rs.getInt("average_parking_duration_minutes")});
                model.addRow(new Object[]{"Last Parking Date", rs.getString("last_parking_date")});
                model.addRow(new Object[]{"Last Login Date", rs.getString("last_login_date")});
            }

            userAnalyticsTable.setModel(model);
            rs.close();
            pst.close();
            con.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading analytics: " + ex.getMessage());
        }
    }
}