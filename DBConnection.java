package test;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DashboardFrame — Enhanced modern UI with professional styling
 */
public class DashboardFrame extends JFrame {
    private final JLabel totalSpotsValue = new JLabel("50", SwingConstants.CENTER);
    private final JLabel occupiedValue = new JLabel("0", SwingConstants.CENTER);
    private final JLabel availableValue = new JLabel("50", SwingConstants.CENTER);

    private final String[] spotNames;
    private final JButton[] spotButtons;

    private int currentOccupancy = 0;
    private final List<Integer> occupancyHistory = new ArrayList<>();
    private final List<LocalDateTime> occupancyTimestamps = new ArrayList<>();

    private final Map<String, VehicleRecord> parkedVehicles = new LinkedHashMap<>();
    private final DefaultTableModel historyModel;

    private final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final double HOURLY_RATE = 20.0;
    private int currentUserId;

    public DashboardFrame() {
        setTitle("E-Parking System - Dashboard");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout(0, 0));

        int spotCount = 50;
        spotNames = new String[spotCount];
        for (int i = 0; i < spotCount; i++) spotNames[i] = "A" + (i + 1);
        spotButtons = new JButton[spotCount];

        occupancyHistory.add(0);
        occupancyTimestamps.add(LocalDateTime.now());

        JPanel mainContent = new JPanel(new BorderLayout(0, 0));
        mainContent.setBackground(UITheme.BG_LIGHT);

        add(createTopPanel(), BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);

        mainContent.add(createCenterPanel(), BorderLayout.CENTER);

        historyModel = new DefaultTableModel(new String[]{"Plate", "Type", "Spot", "Entry Time", "Exit Time", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        mainContent.add(createHistoryTablePanel(), BorderLayout.SOUTH);

        updateCounts();
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel top;
        top = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(UITheme.createGradientPaint(getWidth(), getHeight(),
                        UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_DARK));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
            // In the createTopPanel() method, add this:
            
            private JPanel createTopPanel() {
                JPanel top = new JPanel(new BorderLayout());
                top.setBorder(new EmptyBorder(8, 12, 0, 12));
                
                JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 6));
                cards.setOpaque(false);
                
                cards.add(makeStatCard("Total Spots", String.valueOf(spotNames.length), totalSpotsValue));
                cards.add(makeStatCard("Occupied", "0", occupiedValue));
                cards.add(makeStatCard("Available", String.valueOf(spotNames.length), availableValue));
                
                top.add(cards, BorderLayout.WEST);
                
                // right-side small actions: Analytics, History, Logout
                JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
                rightActions.setOpaque(false);
                
                JButton analyticsBtn = new JButton("View Analytics");
                analyticsBtn.addActionListener(e -> new AnalyticsFrame(new ArrayList<>(occupancyHistory), new ArrayList<>(occupancyTimestamps), spotNames.length).setVisible(true));
                rightActions.add(analyticsBtn);
                
                // ADD HISTORY BUTTON ONLY FOR ADMINS
                if ("Admin".equals(LoginFrame.currentUserRole)) {
                    JButton historyBtn = new JButton("View All History");
                    historyBtn.addActionListener(e -> new AdminHistoryFrame().setVisible(true));
                    rightActions.add(historyBtn);
                }
                
                JButton logoutBtn = new JButton("Logout");
                logoutBtn.addActionListener(e -> {
                    new LoginFrame().setVisible(true);
                    this.dispose();
                });
                rightActions.add(logoutBtn);
                
                top.add(rightActions, BorderLayout.EAST);
                
                return top;
            }

            private PopupMenu makeStatCard(String total_Spots, String valueOf, JLabel totalSpotsValue) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            private void dispose() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
    
        top.setOpaque(false);
        top.setLayout(new BorderLayout());
        top.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Title
        JLabel titleLabel = new JLabel("Parking Control Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        top.add(titleLabel, BorderLayout.WEST);

        // Action buttons
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightActions.setOpaque(false);

        // In DashboardFrame, add this button to the top panel:

        JButton analyticsBtn = new JButton("Analytics");
        JButton logoutBtn = new JButton("Logout");
        
        UITheme.styleButton(analyticsBtn, new Color(34, 177, 76), Color.WHITE);
        UITheme.styleButton(logoutBtn, UITheme.ERROR_RED, Color.WHITE);

        analyticsBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                analyticsBtn.setBackground(new Color(24, 157, 56));
            }
            public void mouseExited(MouseEvent e) {
                analyticsBtn.setBackground(new Color(34, 177, 76));
            }
        });

        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(229, 57, 53));
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(UITheme.ERROR_RED);
            }
        });

        analyticsBtn.addActionListener(e -> new AnalyticsFrame(new ArrayList<>(occupancyHistory), 
            new ArrayList<>(occupancyTimestamps), spotNames.length).setVisible(true));
        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            this.dispose();
        });

        rightActions.add(analyticsBtn);
        rightActions.add(logoutBtn);
        top.add(rightActions, BorderLayout.EAST);

        return top;
    }

    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(16, 16));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatCard("Total Spots", totalSpotsValue, UITheme.PRIMARY_BLUE));
        statsPanel.add(createStatCard("Occupied", occupiedValue, UITheme.ERROR_RED));
        statsPanel.add(createStatCard("Available", availableValue, UITheme.SUCCESS_GREEN));

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(statsPanel, BorderLayout.CENTER);

        center.add(topSection, BorderLayout.NORTH);

        // Main content: Add vehicle + Spots
        JPanel contentPanel = new JPanel(new BorderLayout(16, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(createAddVehicleCard(), BorderLayout.WEST);
        contentPanel.add(createSpotsPanel(), BorderLayout.CENTER);

        center.add(contentPanel, BorderLayout.CENTER);

        return center;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(UITheme.BG_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(UITheme.TEXT_LIGHT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createAddVehicleCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(UITheme.BG_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(320, 0));

        JLabel title = new JLabel("🚗 Add Vehicle");
        title.setFont(UITheme.FONT_SUBTITLE);
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        // Plate field
        JLabel plateLabel = new JLabel("License Plate");
        plateLabel.setFont(UITheme.FONT_LABEL);
        plateLabel.setForeground(UITheme.TEXT_DARK);
        plateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(plateLabel);
        card.add(Box.createVerticalStrut(6));

        JTextField plateField = new JTextField();
        UITheme.styleTextField(plateField);
        plateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        plateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        card.add(plateField);
        card.add(Box.createVerticalStrut(16));

        // Type field
        JLabel typeLabel = new JLabel("Vehicle Type");
        typeLabel.setFont(UITheme.FONT_LABEL);
        typeLabel.setForeground(UITheme.TEXT_DARK);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(typeLabel);
        card.add(Box.createVerticalStrut(6));

        String[] types = {"Car", "Motorbike", "Truck", "Other"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setFont(UITheme.FONT_LABEL);
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        typeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(typeCombo);
        card.add(Box.createVerticalStrut(20));

        // Park button
        JButton parkBtn = new JButton("Park Vehicle");
        UITheme.styleButton(parkBtn, UITheme.PRIMARY_BLUE, Color.WHITE);
        parkBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        parkBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BUTTON_HEIGHT));
        parkBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                parkBtn.setBackground(UITheme.PRIMARY_BLUE_DARK);
            }
            public void mouseExited(MouseEvent e) {
                parkBtn.setBackground(UITheme.PRIMARY_BLUE);
            }
        });

        parkBtn.addActionListener(e -> {
            String plate = plateField.getText().trim().toUpperCase();
            if (plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter license plate", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (parkedVehicles.containsKey(plate)) {
                JOptionPane.showMessageDialog(this, "This plate is already parked", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idx = firstAvailableSpotIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this, "No available spots", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String type = (String) typeCombo.getSelectedItem();
            LocalDateTime entry = LocalDateTime.now();
            String spot = spotNames[idx];
            parkedVehicles.put(plate, new VehicleRecord(plate, type, spot, entry));
            markSpotOccupied(idx, plate);
            addHistoryRow(plate, type, spot, entry.format(TIMESTAMP_FMT), "", "");
            updateCounts();
            updateOccupancy(1);
            plateField.setText("");
        });

        card.add(parkBtn);

        return card;
    }

    private JPanel createSpotsPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(UITheme.BG_WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(0, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel areaTitle = new JLabel("🅿️ Parking Spots");
        areaTitle.setFont(UITheme.FONT_SUBTITLE);
        areaTitle.setForeground(UITheme.TEXT_DARK);
        panel.add(areaTitle, BorderLayout.NORTH);

        int cols = 10;
        int rows = (int) Math.ceil((double) spotNames.length / cols);
        JPanel grid = new JPanel(new GridLayout(rows, cols, 10, 10));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        for (int i = 0; i < spotNames.length; i++) {
            JButton b = new JButton(spotNames[i]);
            b.setFont(new Font("Segoe UI", Font.BOLD, 11));
            b.setPreferredSize(new Dimension(90, 50));
            b.setBackground(new Color(230, 240, 255));
            b.setForeground(UITheme.PRIMARY_BLUE);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT));

            final int idx = i;
            b.addActionListener(e -> onSpotClicked(idx));
            b.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (b.getClientProperty("plate") == null) {
                        b.setBackground(new Color(200, 220, 255));
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (b.getClientProperty("plate") == null) {
                        b.setBackground(new Color(230, 240, 255));
                    }
                }
            });

            spotButtons[i] = b;
            grid.add(b);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane createHistoryTablePanel() {
        JTable table = new JTable(historyModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setFont(UITheme.FONT_LABEL);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setGridColor(UITheme.BORDER_LIGHT);
        table.setSelectionBackground(new Color(200, 220, 255));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(0, 200));
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        return scroll;
    }

    private void onSpotClicked(int idx) {
        JButton b = spotButtons[idx];
        String spot = spotNames[idx];
        Object plateObj = b.getClientProperty("plate");

        if (plateObj != null) {
            String plate = (String) plateObj;
            int opt = JOptionPane.showConfirmDialog(this, "Checkout " + plate + " from " + spot + "?", "Confirm Checkout", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                checkoutPlate(plate);
            }
        } else {
            String plate = JOptionPane.showInputDialog(this, "Enter license plate to park in " + spot + ":");
            if (plate != null) {
                plate = plate.trim().toUpperCase();
                if (plate.isEmpty()) return;
                if (parkedVehicles.containsKey(plate)) {
                    JOptionPane.showMessageDialog(this, "This plate is already parked", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String type = (String) JOptionPane.showInputDialog(this, "Select vehicle type", "Vehicle Type",
                        JOptionPane.PLAIN_MESSAGE, null, new String[]{"Car", "Motorbike", "Truck", "Other"}, "Car");
                if (type == null) return;

                LocalDateTime entry = LocalDateTime.now();
                parkedVehicles.put(plate, new VehicleRecord(plate, type, spot, entry));
                markSpotOccupied(idx, plate);
                addHistoryRow(plate, type, spot, entry.format(TIMESTAMP_FMT), "", "");
                updateCounts();
                updateOccupancy(1);
            }
        }
    }

       private void markSpotOccupied(int idx, String plate) {
    JButton b = spotButtons[idx];
    b.setBackground(UITheme.ERROR_RED);
    b.setForeground(Color.WHITE);
    b.putClientProperty("plate", plate);
    b.setText(plate);  // ← CHANGE TO THIS (only plate number)
}

private void markSpotFree(int idx) {
    JButton b = spotButtons[idx];
    b.setBackground(new Color(230, 240, 255));
    b.setForeground(UITheme.PRIMARY_BLUE);
    b.putClientProperty("plate", null);
    b.setText(spotNames[idx]);
}

    private int firstAvailableSpotIndex() {
        for (int i = 0; i < spotButtons.length; i++) {
            if (spotButtons[i].getClientProperty("plate") == null) return i;
        }
        return -1;
    }

    private void addHistoryRow(String plate, String type, String spot, String entry, String exit, String price) {
        historyModel.addRow(new Object[]{plate, type, spot, entry, exit, price});
    }

    private void checkoutPlate(String plate) {
        VehicleRecord rec = parkedVehicles.get(plate);
        if (rec == null) return;

        LocalDateTime exitTime = LocalDateTime.now();
        Duration duration = Duration.between(rec.entryTime, exitTime);
        long minutes = Math.max(0, duration.toMinutes());
        double hoursCharged = Math.ceil(minutes / 60.0);
        double fee = hoursCharged * HOURLY_RATE;

        parkedVehicles.remove(plate);

        int idx = Arrays.asList(spotNames).indexOf(rec.spot);
        if (idx >= 0) markSpotFree(idx);

        for (int r = historyModel.getRowCount() - 1; r >= 0; r--) {
            String p = Objects.toString(historyModel.getValueAt(r, 0), "");
            String s = Objects.toString(historyModel.getValueAt(r, 2), "");
            String exit = Objects.toString(historyModel.getValueAt(r, 4), "");
            if (p.equals(plate) && s.equals(rec.spot) && exit.isEmpty()) {
                historyModel.setValueAt(exitTime.format(TIMESTAMP_FMT), r, 4);
                historyModel.setValueAt(String.format("$%.2f", fee), r, 5);
                break;
            }
        }

        updateCounts();
        updateOccupancy(-1);

        JOptionPane.showMessageDialog(this, 
            "Plate: " + plate + "\nSpot: " + rec.spot + "\nDuration: " + (minutes / 60) + "h " + (minutes % 60) + "m\nTotal Fee: $" + String.format("%.2f", fee),
            "Checkout Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCounts() {
        int total = spotNames.length;
        int occ = parkedVehicles.size();
        int avail = total - occ;
        totalSpotsValue.setText(String.valueOf(total));
        occupiedValue.setText(String.valueOf(occ));
        availableValue.setText(String.valueOf(avail));
    }

    private void updateOccupancy(int change) {
        currentOccupancy = Math.max(0, parkedVehicles.size());
        occupancyHistory.add(currentOccupancy);
        occupancyTimestamps.add(LocalDateTime.now());
        if (occupancyHistory.size() > 192) {
            occupancyHistory.remove(0);
            occupancyTimestamps.remove(0);
        }
    }

    private static class VehicleRecord {
        final String plate;
        final String type;
        final String spot;
        final LocalDateTime entryTime;

        VehicleRecord(String plate, String type, String spot, LocalDateTime entryTime) {
            this.plate = plate;
            this.type = type;
            this.spot = spot;
            this.entryTime = entryTime;
        }
    }
}