import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;

    private final Color BG = new Color(8, 10, 20);
    private final Color CARD = new Color(21, 26, 50);
    private final Color PURPLE = new Color(139, 92, 246);
    private final Color TEXT = new Color(245, 245, 250);
    private final Color MUTED = new Color(165, 170, 190);

    public UserFrame() {
        setTitle("Study Planner - User Profile");

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("User Profile");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel("Manage your account information");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        main.add(heading, BorderLayout.NORTH);

        JPanel card = new JPanel(new GridLayout(4, 1, 10, 10));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel nameLabel = createLabel("Full Name");
        nameField = new JTextField();

        JLabel emailLabel = createLabel("Email");
        emailField = new JTextField();

        styleField(nameField);
        styleField(emailField);

        card.add(nameLabel);
        card.add(nameField);
        card.add(emailLabel);
        card.add(emailField);

        JButton updateButton = new JButton("Update Profile");
        styleButton(updateButton);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(updateButton);

        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setOpaque(false);
        center.add(card, BorderLayout.CENTER);
        center.add(bottom, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);
        Navigation.setupFrame(this, main, "Settings");

        loadUser();

        updateButton.addActionListener(e -> updateUser());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(12, 15, 28));
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 65, 90)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleButton(JButton button) {
        button.setBackground(PURPLE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadUser() {
        String sql = "SELECT full_name, email FROM users WHERE user_id = 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                nameField.setText(rs.getString("full_name"));
                emailField.setText(rs.getString("email"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void updateUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all details");
            return;
        }

        String sql = "UPDATE users SET full_name = ?, email = ? WHERE user_id = 1";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
}