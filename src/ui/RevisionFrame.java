import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RevisionFrame extends JFrame {

    private JComboBox<String> subjectBox;
    private JTextField dateField;
    private JComboBox<String> statusBox;
    private DefaultTableModel model;
    private JTable table;

    private final Color BG = new Color(8, 10, 20);
    private final Color CARD = new Color(21, 26, 50);
    private final Color PURPLE = new Color(139, 92, 246);
    private final Color TEXT = new Color(245, 245, 250);
    private final Color MUTED = new Color(165, 170, 190);

    public RevisionFrame() {
        setTitle("Study Planner - Revision");

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Revision Tracker");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel("Track your subject revision progress");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        main.add(heading, BorderLayout.NORTH);

        JPanel formCard = new JPanel(new GridLayout(2, 3, 15, 8));
        formCard.setBackground(CARD);
        formCard.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel subjectLabel = createLabel("Subject");
        JLabel dateLabel = createLabel("Revision Date");
        JLabel statusLabel = createLabel("Status");

        subjectBox = new JComboBox<>();
        dateField = new JTextField();
        statusBox = new JComboBox<>(new String[]{"Pending", "Completed"});

        styleComboBox(subjectBox);
        styleField(dateField);
        styleComboBox(statusBox);

        formCard.add(subjectLabel);
        formCard.add(dateLabel);
        formCard.add(statusLabel);
        formCard.add(subjectBox);
        formCard.add(dateField);
        formCard.add(statusBox);

        JButton addButton = new JButton("Add Revision");
        styleButton(addButton);

        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setOpaque(false);
        topPanel.add(formCard, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        String[] columns = {"ID", "Subject", "Revision Date", "Status"};

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
        Navigation.setupFrame(this, main, "Revision");

        loadSubjects();
        loadRevisions();

        addButton.addActionListener(e -> addRevision());
        deleteButton.addActionListener(e -> deleteRevision());
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
        box.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 90)));
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

    private void addRevision() {
        if (subjectBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please add a subject first");
            return;
        }

        String date = dateField.getText().trim();

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter revision date");
            return;
        }

        String selected = subjectBox.getSelectedItem().toString();
        int subjectId = Integer.parseInt(selected.split(" - ")[0]);
        String status = statusBox.getSelectedItem().toString();

        String sql = "INSERT INTO revision (subject_id, revision_date, status) VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, subjectId);
            ps.setString(2, date);
            ps.setString(3, status);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Revision added successfully!");

            dateField.setText("");
            statusBox.setSelectedIndex(0);

            loadRevisions();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void loadRevisions() {
        model.setRowCount(0);

        String sql =
                "SELECT r.revision_id, s.subject_name, r.revision_date, r.status " +
                "FROM revision r " +
                "JOIN subjects s ON r.subject_id = s.subject_id " +
                "ORDER BY r.revision_date";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("revision_id"),
                        rs.getString("subject_name"),
                        rs.getString("revision_date"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void deleteRevision() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a revision to delete");
            return;
        }

        int revisionId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this revision?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM revision WHERE revision_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, revisionId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Revision deleted successfully!");

            loadRevisions();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
}