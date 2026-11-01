import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private static final Color BG = new Color(9, 10, 20);
    private static final Color PANEL = new Color(20, 25, 48);
    private static final Color PANEL2 = new Color(24, 29, 55);
    private static final Color BORDER = new Color(48, 58, 94);
    private static final Color PURPLE = new Color(119, 72, 255);
    private static final Color BLUE = new Color(62, 137, 255);
    private static final Color TEXT = new Color(245, 247, 255);
    private static final Color MUTED = new Color(145, 155, 185);

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Study Planner");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(createLeftPanel(), BorderLayout.WEST);
        root.add(createLoginPanel(), BorderLayout.CENTER);

        add(root);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(390, 600));
        panel.setBackground(new Color(14, 17, 34));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(70, 45, 50, 45));

        JLabel logo = new JLabel("SP");
        logo.setForeground(PURPLE);
        logo.setFont(new Font("Arial", Font.BOLD, 52));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("STUDY PLANNER");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel line = new JLabel("PLAN  •  FOCUS  •  ACHIEVE");
        line.setForeground(MUTED);
        line.setFont(new Font("Arial", Font.BOLD, 12));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(logo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(line);
        panel.add(Box.createVerticalStrut(70));

        JLabel heading = new JLabel("Build better study habits.");
        heading.setForeground(TEXT);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel(
                "<html>Organize your subjects, track your progress,<br>" +
                "manage notes and stay consistent every day.</html>"
        );
        desc.setForeground(MUTED);
        desc.setFont(new Font("Arial", Font.PLAIN, 14));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(12));
        panel.add(desc);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);

        RoundedPanel card = new RoundedPanel(PANEL, 18);
        card.setPreferredSize(new Dimension(420, 440));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(35, 42, 30, 42));

        JLabel heading = new JLabel("Welcome Back");
        heading.setForeground(TEXT);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to continue to your dashboard");
        sub.setForeground(MUTED);
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(7));
        card.add(sub);
        card.add(Box.createVerticalStrut(32));

        JLabel emailLabel = createLabel("EMAIL");
        emailField = new JTextField();
        styleField(emailField);

        JLabel passwordLabel = createLabel("PASSWORD");
        passwordField = new JPasswordField();
        styleField(passwordField);

        card.add(emailLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(emailField);
        card.add(Box.createVerticalStrut(20));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(28));

        JButton login = new JButton("LOGIN");
        login.setAlignmentX(Component.LEFT_ALIGNMENT);
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        login.setPreferredSize(new Dimension(330, 48));
        login.setBackground(PURPLE);
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Arial", Font.BOLD, 14));
        login.setFocusPainted(false);
        login.setBorder(BorderFactory.createEmptyBorder());
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.add(login);
        card.add(Box.createVerticalStrut(20));

        JLabel register = new JLabel("New here? Create your account");
        register.setForeground(MUTED);
        register.setFont(new Font("Arial", Font.PLAIN, 12));
        register.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(register);

        login.addActionListener(e -> login());

        passwordField.addActionListener(e -> login());

        wrapper.add(card);

        return wrapper;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(180, 190, 220));
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setPreferredSize(new Dimension(330, 45));
        field.setBackground(PANEL2);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("Arial", Font.PLAIN, 14));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password."
            );
            return;
        }

        String sql = "SELECT full_name FROM users WHERE email = ? AND password = ?";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("full_name");

                dispose();

                DashboardFrame dashboard = new DashboardFrame(name);
                dashboard.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid email or password."
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage()
            );
        }
    }

    static class RoundedPanel extends JPanel {
        private final Color color;
        private final int radius;

        RoundedPanel(Color color, int radius) {
            this.color = color;
            this.radius = radius;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);
            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}