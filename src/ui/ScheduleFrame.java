import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ScheduleFrame extends JFrame {

    private JTextField activityField;
    private JTextField startField;
    private JTextField endField;
    private DefaultTableModel model;
    private JTable table;

    private final Color BG = new Color(8, 10, 20);
    private final Color CARD = new Color(21, 26, 50);
    private final Color PURPLE = new Color(139, 92, 246);
    private final Color TEXT = new Color(245, 245, 250);
    private final Color MUTED = new Color(165, 170, 190);

    public ScheduleFrame() {
        setTitle("Study Planner - Schedule");

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("My Schedule");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel("Plan your daily study activities");
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

        JLabel activityLabel = createLabel("Activity Name");
        JLabel startLabel = createLabel("Start Time");
        JLabel endLabel = createLabel("End Time");

        activityField = new JTextField();
        startField = new JTextField();
        endField = new JTextField();

        styleField(activityField);
        styleField(startField);
        styleField(endField);

        formCard.add(activityLabel);
        formCard.add(startLabel);
        formCard.add(endLabel);
        formCard.add(activityField);
        formCard.add(startField);
        formCard.add(endField);

        JButton addButton = new JButton("Add Schedule");
        styleButton(addButton);

        JPanel topPanel = new JPanel(new BorderLayout(15, 15));
        topPanel.setOpaque(false);
        topPanel.add(formCard, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        String[] columns = {"ID", "Activity", "Start Time", "End Time"};

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

        Navigation.setupFrame(this, main, "Schedule");

        addButton.addActionListener(e -> addSchedule());
        deleteButton.addActionListener(e -> deleteSchedule());

        loadSchedules();
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

    private void styleButton(JButton button) {
        button.setBackground(PURPLE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addSchedule() {
        String activity = activityField.getText().trim();
        String start = startField.getText().trim();
        String end = endField.getText().trim();

        if (activity.isEmpty() || start.isEmpty() || end.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all schedule details");
            return;
        }

        String sql = "INSERT INTO schedule (activity_name, start_time, end_time) VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, activity);
            ps.setString(2, start);
            ps.setString(3, end);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Schedule added successfully!");

            activityField.setText("");
            startField.setText("");
            endField.setText("");

            loadSchedules();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void loadSchedules() {
        model.setRowCount(0);

        String sql = "SELECT schedule_id, activity_name, start_time, end_time FROM schedule ORDER BY start_time";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("schedule_id"),
                        rs.getString("activity_name"),
                        rs.getString("start_time"),
                        rs.getString("end_time")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void deleteSchedule() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a schedule to delete");
            return;
        }

        int scheduleId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this schedule?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM schedule WHERE schedule_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Schedule deleted successfully!");

            loadSchedules();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
}