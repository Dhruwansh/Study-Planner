import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ProgressFrame extends JFrame {

    private JComboBox<String> subjectBox;
    private JTextField dateField;
    private JTextField hoursField;
    private JComboBox<String> completedBox;
    private DefaultTableModel model;
    private JTable table;

    private final Color BG = new Color(8, 10, 20);
    private final Color CARD = new Color(21, 26, 50);
    private final Color PURPLE = new Color(139, 92, 246);
    private final Color TEXT = new Color(245, 245, 250);
    private final Color MUTED = new Color(165, 170, 190);

    public ProgressFrame() {
        setTitle("Study Planner - Progress");

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Study Progress");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel("Track your daily study progress");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        main.add(heading, BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridLayout(2, 4, 12, 8));
        formCard.setBackground(CARD);
        formCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        formCard.add(createLabel("Subject"));
        formCard.add(createLabel("Study Date"));
        formCard.add(createLabel("Hours Studied"));
        formCard.add(createLabel("Completed"));

        subjectBox = new JComboBox<>();
        dateField = new JTextField();
        hoursField = new JTextField();
        completedBox = new JComboBox<>(new String[]{"No", "Yes"});

        styleComboBox(subjectBox);
        styleField(dateField);
        styleField(hoursField);
        styleComboBox(completedBox);

        formCard.add(subjectBox);
        formCard.add(dateField);
        formCard.add(hoursField);
        formCard.add(completedBox);

        JButton addButton = new JButton("Add Progress");
        styleButton(addButton);

        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setOpaque(false);
        topPanel.add(formCard, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        String[] columns = {"ID", "Subject", "Study Date", "Hours", "Completed"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setForeground(TEXT);
        table.setBackground(CARD);
        table.setGridColor(new Color(50, 55, 80));
        table.setSelectionBackground(PURPLE);
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setBackground(new Color(30, 35, 60));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(45, 50, 75)));
        scrollPane.getViewport().setBackground(CARD);

        JButton deleteButton = new JButton("Delete Selected");
        styleButton(deleteButton);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(deleteButton);

        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setOpaque(false);
        center.add(topPanel, BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        center.add(bottom, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);
        Navigation.setupFrame(this, main, "Progress");

        loadSubjects();
        loadProgress();

        addButton.addActionListener(e -> addProgress());
        deleteButton.addActionListener(e -> deleteProgress());
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
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 65, 90)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setBackground(new Color(12, 15, 28));
        box.setForeground(TEXT);
        box.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void styleButton(JButton button) {
        button.setBackground(PURPLE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadSubjects() {
        subjectBox.removeAllItems();

        String sql = "SELECT subject_id, subject_name FROM subjects WHERE user_id = 1 ORDER BY subject_name";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                subjectBox.addItem(
                        rs.getInt("subject_id") + " - " + rs.getString("subject_name")
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void addProgress() {
        if (subjectBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please add a subject first");
            return;
        }

        String date = dateField.getText().trim();
        String hours = hoursField.getText().trim();

        if (date.isEmpty() || hours.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all progress details");
            return;
        }

        double studyHours;

        try {
            studyHours = Double.parseDouble(hours);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Hours must be a number");
            return;
        }

        String selected = subjectBox.getSelectedItem().toString();
        int subjectId = Integer.parseInt(selected.split(" - ")[0]);
        String completed = completedBox.getSelectedItem().toString();
	int completedValue = completed.equalsIgnoreCase("Yes") ? 1 : 0;

        String sql = "INSERT INTO progress (user_id, subject_id, study_date, hours_studied, completed) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, 1);
            ps.setInt(2, subjectId);
            ps.setString(3, date);
            ps.setDouble(4, studyHours);
            ps.setInt(5, completedValue);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Progress added successfully!");

            dateField.setText("");
            hoursField.setText("");
            completedBox.setSelectedIndex(0);

            loadProgress();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

   private void loadProgress() {
    model.setRowCount(0);

    String sql =
            "SELECT p.progress_id, s.subject_name, p.study_date, " +
            "p.hours_studied, p.completed " +
            "FROM progress p " +
            "JOIN subjects s ON p.subject_id = s.subject_id " +
            "WHERE p.user_id = 1 " +
            "ORDER BY p.study_date DESC";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getInt("progress_id"),
                    rs.getString("subject_name"),
                    rs.getString("study_date"),
                    rs.getDouble("hours_studied"),
                    rs.getInt("completed") == 1 ? "Yes" : "No"
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
    }
}

    private void deleteProgress() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select progress to delete");
            return;
        }

        int progressId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this progress?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM progress WHERE progress_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, progressId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Progress deleted successfully!");

            loadProgress();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
}