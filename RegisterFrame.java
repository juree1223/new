package test;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    static Object currentUserRole;
    private JTextField email_field;
    private JPasswordField pass_field;

    public LoginFrame() {
        setTitle("E-Parking System - Login");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(UITheme.createGradientPaint(getWidth(), getHeight(), 
                    UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_DARK));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        mainPanel.setOpaque(false);

        // Logo/Welcome Panel (Left side)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel logoLabel = new JLabel("E-PARKING");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 10, 20);
        leftPanel.add(logoLabel, gbc);

        JLabel tagline = new JLabel("Smart Parking Solutions");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(new Color(200, 220, 255));
        gbc.gridy = 1;
        leftPanel.add(tagline, gbc);

        JLabel description = new JLabel("<html>Manage parking spaces<br/>efficiently and professionally</html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(new Color(180, 200, 255));
        description.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 20, 20, 20);
        leftPanel.add(description, gbc);

        // Login Form Panel (Right side)
        JPanel formPanel = new JPanel();
        formPanel.setBackground(UITheme.BG_WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        // Header
        JLabel headerTitle = new JLabel("Login to Your Account");
        headerTitle.setFont(UITheme.FONT_TITLE);
        headerTitle.setForeground(UITheme.TEXT_DARK);
        headerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(headerTitle);

        JLabel headerSubtitle = new JLabel("Enter your credentials to continue");
        headerSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        headerSubtitle.setForeground(UITheme.TEXT_LIGHT);
        headerSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(headerSubtitle);
        formPanel.add(Box.createVerticalStrut(30));

        // Email
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(UITheme.FONT_LABEL);
        emailLabel.setForeground(UITheme.TEXT_DARK);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(8));

        email_field = new JTextField();
        UITheme.styleTextField(email_field);
        email_field.setAlignmentX(Component.LEFT_ALIGNMENT);
        email_field.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        formPanel.add(email_field);
        formPanel.add(Box.createVerticalStrut(20));

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UITheme.FONT_LABEL);
        passLabel.setForeground(UITheme.TEXT_DARK);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(8));

        pass_field = new JPasswordField();
        UITheme.stylePasswordField(pass_field);
        pass_field.setAlignmentX(Component.LEFT_ALIGNMENT);
        pass_field.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.INPUT_HEIGHT));
        formPanel.add(pass_field);
        formPanel.add(Box.createVerticalStrut(16));

        // Remember & Forgot
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JCheckBox rememberBox = new JCheckBox("Remember me");
        rememberBox.setOpaque(false);
        rememberBox.setForeground(UITheme.TEXT_LIGHT);
        rememberBox.setFont(UITheme.FONT_LABEL);
        optionsPanel.add(rememberBox, BorderLayout.WEST);

        JLabel forgotLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotLabel.setForeground(UITheme.PRIMARY_BLUE);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        optionsPanel.add(forgotLabel, BorderLayout.EAST);
        formPanel.add(optionsPanel);
        formPanel.add(Box.createVerticalStrut(30));

        // Login Button
        JButton loginBtn = new JButton("Sign In");
        UITheme.styleButton(loginBtn, UITheme.PRIMARY_BLUE, Color.WHITE);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, UITheme.BUTTON_HEIGHT));
        loginBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(UITheme.PRIMARY_BLUE_DARK);
            }
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(UITheme.PRIMARY_BLUE);
            }
        });
        formPanel.add(loginBtn);
        formPanel.add(Box.createVerticalStrut(20));

        // Register link
        JPanel registerPanel = new JPanel();
        registerPanel.setOpaque(false);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel registerText = new JLabel("Don't have an account? ");
        registerText.setFont(UITheme.FONT_LABEL);
        registerText.setForeground(UITheme.TEXT_LIGHT);
        registerPanel.add(registerText);

        JLabel registerLink = new JLabel("<html><u>Sign up here</u></html>");
        registerLink.setForeground(UITheme.PRIMARY_BLUE);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerPanel.add(registerLink);
        formPanel.add(registerPanel);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // Event Listeners
        loginBtn.addActionListener(e -> handleLogin());
        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame().setVisible(true);
                LoginFrame.this.dispose();
            }
        });

        setVisible(true);
    }

    private void handleLogin() {
        String email = email_field.getText().trim();
        String password = new String(pass_field.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new DashboardFrame().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}