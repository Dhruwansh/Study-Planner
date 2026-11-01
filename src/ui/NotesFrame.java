import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class NotesFrame extends JFrame {

    private final Color BG = new Color(8, 10, 20);
    private final Color SIDEBAR = new Color(13, 16, 32);
    private final Color CARD = new Color(20, 25, 48);
    private final Color CARD2 = new Color(25, 31, 58);
    private final Color PURPLE = new Color(116, 72, 255);
    private final Color BLUE = new Color(65, 135, 255);
    private final Color CYAN = new Color(36, 205, 220);
    private final Color TEXT = new Color(245, 246, 252);
    private final Color MUTED = new Color(145, 155, 185);
    private final Color BORDER = new Color(48, 59, 95);

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> subjectBox;
    private DefaultTableModel model;
    private JTable table;

    public NotesFrame() {
        setTitle("Study Planner - Notes");
        JPanel content = createContent();
        Navigation.setupFrame(this, content, "Notes");
        loadSubjects();
        loadNotes();
    }

    private JPanel createSidebar() {

        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(250, 720));
        side.setBackground(SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(35, 22, 25, 22));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(205, 40));

        JLabel sp = new JLabel("SP");
        sp.setForeground(PURPLE);
        sp.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel logo = new JLabel("  STUDY PLANNER");
        logo.setForeground(TEXT);
        logo.setFont(new Font("Arial", Font.BOLD, 19));

        logoPanel.add(sp);
        logoPanel.add(logo);

        side.add(logoPanel);

        JLabel tagline = new JLabel("Plan  •  Focus  •  Achieve");
        tagline.setForeground(MUTED);
        tagline.setFont(new Font("Arial", Font.PLAIN, 12));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        side.add(Box.createVerticalStrut(8));
        side.add(tagline);
        side.add(Box.createVerticalStrut(45));

        JLabel workspace = new JLabel("WORKSPACE");
        workspace.setForeground(new Color(105, 125, 175));
        workspace.setFont(new Font("Arial", Font.BOLD, 12));
        workspace.setAlignmentX(Component.CENTER_ALIGNMENT);

        side.add(workspace);
        side.add(Box.createVerticalStrut(18));

        side.add(menuButton("01   Dashboard", false));
        side.add(Box.createVerticalStrut(8));
        side.add(menuButton("02   Subjects", false));
        side.add(Box.createVerticalStrut(8));
        side.add(menuButton("03   Notes", true));
        side.add(Box.createVerticalStrut(8));
        side.add(menuButton("04   Schedule", false));
        side.add(Box.createVerticalStrut(8));
        side.add(menuButton("05   Revision", false));
        side.add(Box.createVerticalStrut(8));
        side.add(menuButton("06   Reminders", false));
        side.add(Box.createVerticalStrut(8));
        side.add(menuButton("07   Progress", false));
        side.add(Box.createVerticalStrut(8));
        side.add(menuButton("08   Settings", false));

        side.add(Box.createVerticalGlue());

        JPanel line = new JPanel();
        line.setBackground(new Color(50, 62, 100));
        line.setMaximumSize(new Dimension(205, 1));
        side.add(line);

        side.add(Box.createVerticalStrut(22));

        JLabel logged = new JLabel("LOGGED IN AS");
        logged.setForeground(new Color(105, 125, 175));
        logged.setFont(new Font("Arial", Font.BOLD, 11));
        logged.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(logged);

        side.add(Box.createVerticalStrut(12));

        JPanel userCard = new JPanel(new BorderLayout(12, 0));
        userCard.setBackground(CARD);
        userCard.setMaximumSize(new Dimension(205, 62));
        userCard.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel avatar = new JLabel("TU", SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(PURPLE);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Arial", Font.BOLD, 14));
        avatar.setPreferredSize(new Dimension(46, 46));

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("Test User");
        name.setForeground(TEXT);
        name.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel role = new JLabel("Student Account");
        role.setForeground(MUTED);
        role.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel online = new JLabel("• Online");
        online.setForeground(new Color(45, 220, 135));
        online.setFont(new Font("Arial", Font.PLAIN, 11));

        userInfo.add(name);
        userInfo.add(role);
        userInfo.add(online);

        userCard.add(avatar, BorderLayout.WEST);
        userCard.add(userInfo, BorderLayout.CENTER);

        side.add(userCard);
        side.add(Box.createVerticalStrut(18));

        JButton logout = menuButton("09   Logout", false);
        side.add(logout);

        return side;
    }

    private JButton menuButton(String text, boolean active) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(205, 43));
        button.setPreferredSize(new Dimension(205, 43));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Arial", Font.BOLD, 14));

        button.setForeground(active ? Color.WHITE : new Color(195, 202, 225));
        button.setBackground(active ? PURPLE : SIDEBAR);

        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JPanel createContent() {

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);
        content.setBorder(BorderFactory.createEmptyBorder(35, 38, 30, 38));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("My Notes");
        title.setForeground(TEXT);
        title.setFont(new Font("Arial", Font.BOLD, 31));

        JLabel subtitle = new JLabel("Organize your study material in one place.");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        heading.add(title);
        heading.add(Box.createVerticalStrut(6));
        heading.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JTextField search = new JTextField("Search notes...");
        search.setPreferredSize(new Dimension(190, 40));
        search.setBackground(CARD);
        search.setForeground(MUTED);
        search.setFont(new Font("Arial", Font.PLAIN, 13));
        search.setBorder(BorderFactory.createLineBorder(BORDER));

        JButton add = new JButton("+  Add Note");
        add.setPreferredSize(new Dimension(125, 40));
        add.setBackground(PURPLE);
        add.setForeground(Color.WHITE);
        add.setFont(new Font("Arial", Font.BOLD, 13));
        add.setFocusPainted(false);
        add.setBorder(BorderFactory.createEmptyBorder());
        add.setCursor(new Cursor(Cursor.HAND_CURSOR));

        right.add(search);
        right.add(add);

        header.add(heading, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(18, 18));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));

        JPanel editor = createEditorPanel();
        center.add(editor, BorderLayout.WEST);

        JPanel notesPanel = createNotesPanel();
        center.add(notesPanel, BorderLayout.CENTER);

        content.add(center, BorderLayout.CENTER);

        add.addActionListener(e -> addNote());

        return content;
    }

    private JPanel createEditorPanel() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(330, 520));
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Create New Note");
        heading.setForeground(TEXT);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(heading);
        panel.add(Box.createVerticalStrut(24));

        JLabel subjectLabel = fieldLabel("SUBJECT");
        panel.add(subjectLabel);
        panel.add(Box.createVerticalStrut(7));

        subjectBox = new JComboBox<>();
        subjectBox.setMaximumSize(new Dimension(286, 40));
        subjectBox.setBackground(CARD2);
        subjectBox.setForeground(TEXT);
        subjectBox.setFont(new Font("Arial", Font.PLAIN, 13));

        panel.add(subjectBox);
        panel.add(Box.createVerticalStrut(20));

        JLabel titleLabel = fieldLabel("NOTE TITLE");
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(7));

        titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(286, 40));
        titleField.setBackground(CARD2);
        titleField.setForeground(TEXT);
        titleField.setCaretColor(Color.WHITE);
        titleField.setFont(new Font("Arial", Font.PLAIN, 13));
        titleField.setBorder(BorderFactory.createLineBorder(BORDER));

        panel.add(titleField);
        panel.add(Box.createVerticalStrut(20));

        JLabel descLabel = fieldLabel("DESCRIPTION");
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(7));

        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(CARD2);
        descriptionArea.setForeground(TEXT);
        descriptionArea.setCaretColor(Color.WHITE);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(286, 145));
        descScroll.setMaximumSize(new Dimension(286, 145));
        descScroll.setBorder(BorderFactory.createLineBorder(BORDER));

        panel.add(descScroll);
        panel.add(Box.createVerticalGlue());

        JButton save = new JButton("SAVE NOTE");
        save.setMaximumSize(new Dimension(286, 43));
        save.setBackground(BLUE);
        save.setForeground(Color.WHITE);
        save.setFont(new Font("Arial", Font.BOLD, 13));
        save.setFocusPainted(false);
        save.setBorder(BorderFactory.createEmptyBorder());
        save.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(save);

        save.addActionListener(e -> addNote());

        return panel;
    }

    private JLabel fieldLabel(String text) {

        JLabel label = new JLabel(text);
        label.setForeground(new Color(120, 140, 190));
        label.setFont(new Font("Arial", Font.BOLD, 11));

        return label;
    }

    private JPanel createNotesPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel heading = new JLabel("Saved Notes");
        heading.setForeground(TEXT);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel count = new JLabel("YOUR STUDY MATERIAL");
        count.setForeground(PURPLE);
        count.setFont(new Font("Arial", Font.BOLD, 11));

        top.add(heading, BorderLayout.WEST);
        top.add(count, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);

        String[] columns = {"ID", "Subject", "Note Title", "Description", "Date"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(45);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setBackground(CARD2);
        table.setSelectionBackground(PURPLE);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);

        table.getTableHeader().setBackground(new Color(27, 34, 62));
        table.getTableHeader().setForeground(new Color(185, 195, 225));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(145);
        table.getColumnModel().getColumn(3).setPreferredWidth(220);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        scroll.getViewport().setBackground(CARD2);

        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        bottom.setOpaque(false);

        JButton delete = new JButton("Delete Selected");
        delete.setPreferredSize(new Dimension(145, 38));
        delete.setBackground(new Color(45, 50, 75));
        delete.setForeground(new Color(220, 225, 240));
        delete.setFont(new Font("Arial", Font.BOLD, 12));
        delete.setFocusPainted(false);
        delete.setBorder(BorderFactory.createLineBorder(BORDER));
        delete.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottom.add(delete);

        panel.add(bottom, BorderLayout.SOUTH);

        delete.addActionListener(e -> deleteNote());

        return panel;
    }

    private void loadSubjects() {

        subjectBox.removeAllItems();

        String sql = "SELECT subject_id, subject_name FROM subjects WHERE user_id = 1";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                subjectBox.addItem(
                        rs.getInt("subject_id") + " - " + rs.getString("subject_name")
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load subjects: " + e.getMessage()
            );
        }
    }

    private void loadNotes() {

        model.setRowCount(0);

        String sql =
                "SELECT n.note_id, s.subject_name, n.title, n.description, n.upload_date " +
                "FROM notes n " +
                "JOIN subjects s ON n.subject_id = s.subject_id " +
                "WHERE s.user_id = 1 " +
                "ORDER BY n.upload_date DESC";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String description = rs.getString("description");

                if (description == null) {
                    description = "";
                }

                if (description.length() > 35) {
                    description = description.substring(0, 35) + "...";
                }

                model.addRow(new Object[]{
                        rs.getInt("note_id"),
                        rs.getString("subject_name"),
                        rs.getString("title"),
                        description,
                        rs.getString("upload_date")
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load notes: " + e.getMessage()
            );
        }
    }

    private void addNote() {

        if (subjectBox.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please add a subject first."
            );

            return;
        }

        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || description.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter note title and description."
            );

            return;
        }

        String selected = subjectBox.getSelectedItem().toString();
        int subjectId = Integer.parseInt(selected.split(" - ")[0]);

        String sql =
                "INSERT INTO notes (subject_id, title, description, upload_date) " +
                "VALUES (?, ?, ?, NOW())";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, subjectId);
            ps.setString(2, title);
            ps.setString(3, description);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Note saved successfully!"
            );

            titleField.setText("");
            descriptionArea.setText("");

            loadNotes();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage()
            );
        }
    }

    private void deleteNote() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a note to delete."
            );

            return;
        }

        int noteId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this note?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM notes WHERE note_id = ?";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, noteId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Note deleted successfully!"
            );

            loadNotes();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + e.getMessage()
            );
        }
    }
}