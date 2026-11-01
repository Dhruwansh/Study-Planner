import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SubjectFrame extends JFrame {

    private static final Color BG = new Color(9, 10, 20);
    private static final Color SIDEBAR = new Color(14, 17, 34);
    private static final Color PANEL = new Color(20, 25, 48);
    private static final Color PANEL2 = new Color(24, 29, 55);
    private static final Color BORDER = new Color(48, 58, 94);
    private static final Color PURPLE = new Color(119, 72, 255);
    private static final Color TEXT = new Color(245, 247, 255);
    private static final Color MUTED = new Color(145, 155, 185);
    private static final Color RED = new Color(235, 75, 100);

    private JTextField subjectField;
    private JTextField facultyField;
    private DefaultTableModel model;
    private JTable table;

    public SubjectFrame() {
        setTitle("Study Planner - Subjects");
        Navigation.setupFrame(this, createMainContent(), "Subjects");
        loadSubjects();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 700));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 22, 25, 22));

        JLabel logo = new JLabel("SP");
        logo.setForeground(PURPLE);
        logo.setFont(new Font("Arial", Font.BOLD, 27));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("STUDY PLANNER");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 17));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(45));

        JLabel workspace = new JLabel("WORKSPACE");
        workspace.setForeground(new Color(105, 120, 165));
        workspace.setFont(new Font("Arial", Font.BOLD, 11));
        workspace.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(workspace);
        sidebar.add(Box.createVerticalStrut(15));

        sidebar.add(sideItem("01", "Dashboard", false));
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(sideItem("02", "Subjects", true));
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(sideItem("03", "Notes", false));
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(sideItem("04", "Schedule", false));
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(sideItem("05", "Revision", false));
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(sideItem("06", "Reminders", false));
        sidebar.add(Box.createVerticalStrut(7));
        sidebar.add(sideItem("07", "Progress", false));

        return sidebar;
    }

    private JPanel sideItem(String number, String text, boolean active) {
        JPanel panel = new RoundedPanel(active ? PURPLE : SIDEBAR, 8);
        panel.setMaximumSize(new Dimension(186, 43));
        panel.setPreferredSize(new Dimension(186, 43));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 8));

        JLabel num = new JLabel(number);
        num.setForeground(active ? Color.WHITE : new Color(90, 115, 175));
        num.setFont(new Font("Arial", Font.BOLD, 10));
        num.setPreferredSize(new Dimension(30, 30));

        JLabel label = new JLabel(text);
        label.setForeground(active ? Color.WHITE : new Color(205, 211, 230));
        label.setFont(new Font("Arial", Font.BOLD, 13));

        panel.add(num, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(32, 30, 28, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("My Subjects");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subtitle = new JLabel("Manage your academic subjects and faculty information.");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitle);

        JLabel count = new JLabel("SUBJECT MANAGEMENT");
        count.setForeground(PURPLE);
        count.setFont(new Font("Arial", Font.BOLD, 11));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(count, BorderLayout.EAST);

        main.add(header, BorderLayout.NORTH);
        main.add(createBody(), BorderLayout.CENTER);

        return main;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);

        RoundedPanel addPanel = new RoundedPanel(PANEL, 14);
        addPanel.setLayout(new BorderLayout(15, 0));
        addPanel.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        addPanel.setPreferredSize(new Dimension(0, 100));

        JPanel fields = new JPanel(new GridLayout(1, 2, 14, 0));
        fields.setOpaque(false);

        subjectField = createField("Subject name");
        facultyField = createField("Faculty name");

        fields.add(subjectField);
        fields.add(facultyField);

        JButton addButton = new JButton("ADD SUBJECT");
        addButton.setBackground(PURPLE);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        addPanel.add(fields, BorderLayout.CENTER);
        addPanel.add(addButton, BorderLayout.EAST);

        addButton.addActionListener(e -> addSubject());

        RoundedPanel tablePanel = new RoundedPanel(PANEL, 14);
        tablePanel.setLayout(new BorderLayout(0, 12));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel tableTitle = new JLabel("Your Subjects");
        tableTitle.setForeground(TEXT);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 17));

        String[] columns = {"ID", "SUBJECT", "FACULTY"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(42);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(PANEL2);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(58, 50, 100));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(new Color(28, 35, 65));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setForeground(TEXT);
        center.setBackground(PANEL2);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);

        table.getColumnModel().getColumn(0).setCellRenderer(center);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(PANEL2);

        JButton deleteButton = new JButton("DELETE SELECTED");
        deleteButton.setBackground(new Color(55, 25, 40));
        deleteButton.setForeground(new Color(255, 110, 130));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 11));
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createLineBorder(new Color(100, 45, 65)));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JLabel hint = new JLabel("Select a subject from the table to delete it.");
        hint.setForeground(MUTED);
        hint.setFont(new Font("Arial", Font.PLAIN, 11));

        bottom.add(hint, BorderLayout.WEST);
        bottom.add(deleteButton, BorderLayout.EAST);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);
        tablePanel.add(bottom, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> deleteSubject());

        body.add(addPanel, BorderLayout.NORTH);
        body.add(tablePanel, BorderLayout.CENTER);

        return body;
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setBackground(PANEL2);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("Arial", Font.PLAIN, 13));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));

        field.setToolTipText(placeholder);

        return field;
    }

    private void addSubject() {
        String subject = subjectField.getText().trim();
        String faculty = facultyField.getText().trim();

        if (subject.isEmpty() || faculty.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter subject and faculty name."
            );
            return;
        }

        String sql = "INSERT INTO subjects (subject_name, user_id, faculty_name) VALUES (?, ?, ?)";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, subject);
            ps.setInt(2, 1);
            ps.setString(3, faculty);

            ps.executeUpdate();

            subjectField.setText("");
            facultyField.setText("");

            loadSubjects();

            JOptionPane.showMessageDialog(
                    this,
                    "Subject added successfully!"
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage()
            );
        }
    }

    private void loadSubjects() {
        if (model == null) {
            return;
        }

        model.setRowCount(0);

        String sql = "SELECT subject_id, subject_name, faculty_name FROM subjects WHERE user_id = 1";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("subject_id"),
                        rs.getString("subject_name"),
                        rs.getString("faculty_name")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage()
            );
        }
    }

    private void deleteSubject() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a subject to delete."
            );
            return;
        }

        int subjectId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this subject?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM subjects WHERE subject_id = ?";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, subjectId);
            ps.executeUpdate();

            loadSubjects();

            JOptionPane.showMessageDialog(
                    this,
                    "Subject deleted successfully!"
            );

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