package test;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {
    
    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s\\-']+$");
    }
    
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }
    
    private boolean isValidEmployerId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return id.matches("^[a-zA-Z0-9]+$");
    }
    
    private boolean isValidDepartment(String dept) {
        if (dept == null || dept.trim().isEmpty()) {
            return false;
        }
        return true;
    }
    
    private boolean isValidVehicleNumber(String vehicle) {
        if (vehicle == null || vehicle.trim().isEmpty()) {
            return true;
        }
        return vehicle.matches("^[a-zA-Z0-9]+$");
    }
    
    public RegisterFrame() {
        setTitle("E-Parking System - Register");
        setSize(900, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // LEFT SIDEBAR - Blue section
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);

        // RIGHT SECTION - White form area (compact)
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 102, 210));
        panel.setPreferredSize(new Dimension(320, 850));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        // Title
        JLabel titleLabel = new JLabel("E-PARKING");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Smart Parking Solutions");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel("<html><br/><br/>Manage parking spaces<br/>efficiently and professionally</html>");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descLabel.setForeground(new Color(200, 220, 255));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitleLabel);
        panel.add(descLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 40));

        // Scroll pane for form
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Header
        JLabel headerLabel = new JLabel("Create Your Account");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setForeground(new Color(40, 40, 40));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subheaderLabel = new JLabel("Join our parking management system");
        subheaderLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subheaderLabel.setForeground(new Color(120, 120, 120));
        subheaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(headerLabel);
        formPanel.add(Box.createVerticalStrut(3));
        formPanel.add(subheaderLabel);
        formPanel.add(Box.createVerticalStrut(20));

        // Form Fields
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        nameLabel.setForeground(new Color(80, 80, 80));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField nameField = createTextField();
        formPanel.add(nameLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(12));

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        emailLabel.setForeground(new Color(80, 80, 80));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField emailField = createTextField();
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(12));

        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        roleLabel.setForeground(new Color(80, 80, 80));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] roles = {"Employee", "Admin"};
        JComboBox<String> roleCombo = createComboBox(roles);
        formPanel.add(roleLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(roleCombo);
        formPanel.add(Box.createVerticalStrut(12));

        JLabel employerIdLabel = new JLabel("Employer ID");
        employerIdLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        employerIdLabel.setForeground(new Color(80, 80, 80));
        employerIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField employerIdField = createTextField();
        formPanel.add(employerIdLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(employerIdField);
        formPanel.add(Box.createVerticalStrut(12));

        JLabel deptLabel = new JLabel("Department");
        deptLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        deptLabel.setForeground(new Color(80, 80, 80));
        deptLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] departments = {"IT", "REGISTRAR", "Safety and Security", "Administration", "Customer Service", "Operators"};
        JComboBox<String> deptCombo = createComboBox(departments);
        formPanel.add(deptLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(deptCombo);
        formPanel.add(Box.createVerticalStrut(12));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        passLabel.setForeground(new Color(80, 80, 80));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField passField = createPasswordField();
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(passField);
        formPanel.add(Box.createVerticalStrut(12));

        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        confirmLabel.setForeground(new Color(80, 80, 80));
        confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField confirmField = createPasswordField();
        formPanel.add(confirmLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(confirmField);
        formPanel.add(Box.createVerticalStrut(18));

        // Create Account Button
        JButton createBtn = new JButton("Create Account");
        createBtn.setBackground(new Color(25, 102, 210));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        createBtn.setPreferredSize(new Dimension(300, 40));
        createBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        createBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        createBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passField.getPassword());
            String confirmPass = new String(confirmField.getPassword());
            String role = (String) roleCombo.getSelectedItem();
            String employerId = employerIdField.getText();
            String dept = (String) deptCombo.getSelectedItem();

            // Validation checks
            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(this, "Full Name can only contain letters, spaces, hyphens, and apostrophes!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                nameField.requestFocus();
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                emailField.requestFocus();
                return;
            }

            if (!isValidEmployerId(employerId)) {
                JOptionPane.showMessageDialog(this, "Employer ID can only contain letters and numbers!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                employerIdField.requestFocus();
                return;
            }

            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                passField.requestFocus();
                return;
            }

            if (!password.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                confirmField.requestFocus();
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/e_parking", "root", ""
                );

                String sql = "INSERT INTO users(account_type, full_name, email, password, employer_id, department) VALUES (?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, role);
                pst.setString(2, name);
                pst.setString(3, email);
                pst.setString(4, password);
                pst.setString(5, employerId);
                pst.setString(6, dept);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Account Created Successfully!");
                con.close();

                new LoginFrame().setVisible(true);
                dispose();

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "MySQL Driver not found!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        formPanel.add(createBtn);
        formPanel.add(Box.createVerticalStrut(12));

        // Login Link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loginPanel.setBackground(Color.WHITE);
        JLabel existingLabel = new JLabel("Already have an account?");
        existingLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        existingLabel.setForeground(new Color(100, 100, 100));

        JLabel loginLink = new JLabel("Log in here");
        loginLink.setFont(new Font("SansSerif", Font.PLAIN, 11));
        loginLink.setForeground(new Color(25, 102, 210));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        loginPanel.add(existingLabel);
        loginPanel.add(loginLink);
        formPanel.add(loginPanel);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(300, 32));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(300, 32));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        combo.setPreferredSize(new Dimension(300, 32));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return combo;
    }
}