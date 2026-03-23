package test;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class ForgotPasswordFrame extends JFrame {

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

    public ForgotPasswordFrame() {
        setTitle("E-Parking System - Reset Password");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // LEFT SIDEBAR - Blue section
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);

        // RIGHT SECTION - White form area
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 102, 210));
        panel.setPreferredSize(new Dimension(320, 600));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel titleLabel = new JLabel("E-PARKING");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Smart Parking Solutions");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 220, 255));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        panel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Reset Your Password");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerLabel.setForeground(new Color(40, 40, 40));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subheaderLabel = new JLabel("Enter your email to reset your password");
        subheaderLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subheaderLabel.setForeground(new Color(120, 120, 120));
        subheaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(headerLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(subheaderLabel);
        formPanel.add(Box.createVerticalStrut(30));

        // Email Field
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(80, 80, 80));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField emailField = createTextField();
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(20));

        // New Password Field
        JLabel passLabel = new JLabel("New Password");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        passLabel.setForeground(new Color(80, 80, 80));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField passField = createPasswordField();
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(passField);
        formPanel.add(Box.createVerticalStrut(20));

        // Confirm Password Field
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        confirmLabel.setForeground(new Color(80, 80, 80));
        confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField confirmField = createPasswordField();
        formPanel.add(confirmLabel);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(confirmField);
        formPanel.add(Box.createVerticalStrut(25));

        // Reset Button
        JButton resetBtn = new JButton("Reset Password");
        resetBtn.setBackground(new Color(25, 102, 210));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        resetBtn.setPreferredSize(new Dimension(350, 40));
        resetBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        resetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        resetBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String newPassword = new String(passField.getPassword());
            String confirmPassword = new String(confirmField.getPassword());

            // Validation
            if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                emailField.requestFocus();
                return;
            }

            if (!isValidPassword(newPassword)) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
                passField.requestFocus();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                confirmField.requestFocus();
                return;
            }

            try {
                Connection con = DBConnection.getConnection();

                // Check if email exists
                String checkSql = "SELECT * FROM users WHERE email=?";
                PreparedStatement checkStmt = con.prepareStatement(checkSql);
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Email exists, update password
                    String updateSql = "UPDATE users SET password=? WHERE email=?";
                    PreparedStatement updateStmt = con.prepareStatement(updateSql);
                    updateStmt.setString(1, newPassword);
                    updateStmt.setString(2, email);
                    int rows = updateStmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Password reset successfully! Please login with your new password.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        new LoginFrame().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to reset password", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    updateStmt.close();
                } else {
                    JOptionPane.showMessageDialog(this, "Email address not found in the system", "Error", JOptionPane.ERROR_MESSAGE);
                    emailField.requestFocus();
                }

                checkStmt.close();
                rs.close();
                con.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "MySQL Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(resetBtn);
        formPanel.add(Box.createVerticalStrut(15));

        // Back to Login Link
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(Color.WHITE);
        JLabel backLabel = new JLabel("← Back to Login");
        backLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        backLabel.setForeground(new Color(25, 102, 210));
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
            }
        });

        backPanel.add(backLabel);
        formPanel.add(backPanel);

        panel.add(formPanel);
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(350, 36));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(350, 36));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
}