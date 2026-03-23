package test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminHistoryFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private JTable allLoginHistoryTable;
    private JTable allParkingHistoryTable;
    private JTable allUsersAnalyticsTable;

    public AdminHistoryFrame() {
        setTitle("Admin - All Users History & Analytics");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("All Login History", createAllLoginHistoryPanel());
        tabbedPane.addTab("All Parking History", createAllParkingHistoryPanel());
        tabbedPane.addTab("All Users Analytics", createAllAnalyticsPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createAllLoginHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("All Users Login History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        allLoginHistoryTable = new JTable();
        loadAllLoginHistory();
        JScrollPane scrollPane = new JScrollPane(allLoginHistoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAllLoginHistory());
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAllParkingHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("All Users Parking History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        allParkingHistoryTable = new JTable();
        loadAllParkingHistory();
        JScrollPane scrollPane = new JScrollPane(allParkingHistoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAllParkingHistory());
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAllAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("All Users Analytics");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        allUsersAnalyticsTable = new JTable();
        loadAllUsersAnalytics();
        JScrollPane scrollPane = new JScrollPane(allUsersAnalyticsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAllUsersAnalytics());
        buttonPanel.add(refreshBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAllLoginHistory() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT u.full_name, u.email, lh.login_time, lh.logout_time, " +
                         "lh.session_duration_minutes, lh.status " +
                         "FROM login_history lh " +
                         "JOIN users u ON lh.user_id = u.id " +
                         "ORDER BY lh.login_time DESC";
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Email");
            model.addColumn("Login Time");
            model.addColumn("Logout Time");
            model.addColumn("Duration (min)");
            model.addColumn("Status");

            while (rs.next()) {
                Object[] row = {
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("login_time"),
                    rs.getString("logout_time"),
                    rs.getInt("session_duration_minutes"),
                    rs.getString("status")
                };
                model.addRow(row);
            }

            allLoginHistoryTable.setModel(model);
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadAllParkingHistory() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT u.full_name, u.email, ph.plate_number, ph.vehicle_type, " +
                         "ph.spot_name, ph.entry_time, ph.exit_time, ph.duration_minutes, ph.total_fee " +
                         "FROM parking_history ph " +
                         "JOIN users u ON ph.user_id = u.id " +
                         "ORDER BY ph.entry_time DESC";
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Email");
            model.addColumn("Plate");
            model.addColumn("Type");
            model.addColumn("Spot");
            model.addColumn("Entry Time");
            model.addColumn("Exit Time");
            model.addColumn("Duration (min)");
            model.addColumn("Fee ($)");

            while (rs.next()) {
                Object[] row = {
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("plate_number"),
                    rs.getString("vehicle_type"),
                    rs.getString("spot_name"),
                    rs.getString("entry_time"),
                    rs.getString("exit_time"),
                    rs.getInt("duration_minutes"),
                    String.format("%.2f", rs.getDouble("total_fee"))
                };
                model.addRow(row);
            }

            allParkingHistoryTable.setModel(model);
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadAllUsersAnalytics() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT u.full_name, u.email, ua.total_logins, ua.total_parking_sessions, " +
                         "ua.total_parking_fee, ua.total_parking_duration_minutes, ua.last_login_date " +
                         "FROM user_analytics ua " +
                         "JOIN users u ON ua.user_id = u.id " +
                         "ORDER BY ua.total_parking_fee DESC";
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Email");
            model.addColumn("Total Logins");
            model.addColumn("Parking Sessions");
            model.addColumn("Total Fee ($)");
            model.addColumn("Total Duration (min)");
            model.addColumn("Last Login");

            while (rs.next()) {
                Object[] row = {
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getInt("total_logins"),
                    rs.getInt("total_parking_sessions"),
                    String.format("%.2f", rs.getDouble("total_parking_fee")),
                    rs.getInt("total_parking_duration_minutes"),
                    rs.getString("last_login_date")
                };
                model.addRow(row);
            }

            allUsersAnalyticsTable.setModel(model);
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}