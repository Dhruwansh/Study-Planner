import javax.swing.*;
import java.awt.*;

public class Navigation {
    private static final Color BG = new Color(8, 10, 20);
    private static final Color SIDEBAR = new Color(13, 16, 31);
    private static final Color PURPLE = new Color(124, 72, 255);
    private static final Color TEXT = new Color(245, 246, 252);
    private static final Color MUTED = new Color(145, 155, 185);
    private static final Color BORDER = new Color(45, 55, 95);
    private static String userName = "Student";

    public static void setUserName(String name) {
        if (name != null && !name.trim().isEmpty()) userName = name;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setupFrame(JFrame frame, JPanel content, String active) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(createSidebar(frame, active), BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);
        frame.setContentPane(root);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
    }

    private static JPanel createSidebar(JFrame frame, String active) {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
	sidebar.setBorder(BorderFactory.createEmptyBorder(35, 25, 25, 25));
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        JLabel sp = new JLabel("SP");
        sp.setForeground(PURPLE);
        sp.setFont(new Font("Arial", Font.BOLD, 27));
        JLabel logo = new JLabel("  STUDY PLANNER");
        logo.setForeground(TEXT);
        logo.setFont(new Font("Arial", Font.BOLD, 22));
        logoPanel.add(sp);
        logoPanel.add(logo);
        sidebar.add(logoPanel);

        JLabel tagline = new JLabel("Plan  •  Focus  •  Achieve");
        tagline.setForeground(MUTED);
        tagline.setFont(new Font("Arial", Font.PLAIN, 14));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(tagline);
        sidebar.add(Box.createVerticalStrut(45));

        JLabel workspace = new JLabel("WORKSPACE");
        workspace.setForeground(new Color(105, 120, 165));
        workspace.setFont(new Font("Arial", Font.BOLD, 13));
        workspace.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(workspace);
        sidebar.add(Box.createVerticalStrut(18));

        addItem(sidebar, frame, "01", "Dashboard", active);
        addGap(sidebar);
        addItem(sidebar, frame, "02", "Subjects", active);
        addGap(sidebar);
        addItem(sidebar, frame, "03", "Notes", active);
        addGap(sidebar);
        addItem(sidebar, frame, "04", "Schedule", active);
        addGap(sidebar);
        addItem(sidebar, frame, "05", "Revision", active);
        addGap(sidebar);
        addItem(sidebar, frame, "06", "Reminders", active);
        addGap(sidebar);
        addItem(sidebar, frame, "07", "Progress", active);
        addGap(sidebar);
        addItem(sidebar, frame, "08", "Settings", active);

        sidebar.add(Box.createVerticalGlue());
        JSeparator line = new JSeparator();
        line.setForeground(BORDER);
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(line);
        sidebar.add(Box.createVerticalStrut(22));

        JLabel logged = new JLabel("LOGGED IN AS");
        logged.setForeground(new Color(105, 120, 165));
        logged.setFont(new Font("Arial", Font.BOLD, 12));
        logged.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logged);
        sidebar.add(Box.createVerticalStrut(12));

        JLabel name = new JLabel(userName);
        name.setForeground(TEXT);
        name.setFont(new Font("Arial", Font.BOLD, 15));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(name);
        sidebar.add(Box.createVerticalStrut(28));

        JButton logout = navButton("09", "Logout", false);
        logout.addActionListener(e -> {
            frame.dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logout);
        return sidebar;
    }

    private static void addGap(JPanel sidebar) {
        sidebar.add(Box.createVerticalStrut(8));
    }

    private static void addItem(JPanel sidebar, JFrame frame, String number, String text, String active) {
        boolean selected = text.equals(active);
        JButton b = navButton(number, text, selected);
        b.addActionListener(e -> switchTo(frame, text));
        sidebar.add(b);
    }

    private static JButton navButton(String number, String text, boolean selected) {
        JButton b = new JButton(number + "     " + text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(new Font("Arial", Font.BOLD, 15));
        b.setForeground(selected ? Color.WHITE : TEXT);
        b.setBackground(selected ? PURPLE : SIDEBAR);
        b.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 10));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
b.setPreferredSize(new Dimension(250, 48));
b.setMinimumSize(new Dimension(250, 48));
b.setMaximumSize(new Dimension(250, 48));
        return b;
    }

    private static void switchTo(JFrame current, String target) {
        current.dispose();
        JFrame next;
        switch (target) {
            case "Dashboard": next = new DashboardFrame(userName); break;
            case "Subjects": next = new SubjectFrame(); break;
            case "Notes": next = new NotesFrame(); break;
            case "Schedule": next = new ScheduleFrame(); break;
            case "Revision": next = new RevisionFrame(); break;
            case "Reminders": next = new ReminderFrame(); break;
            case "Progress": next = new ProgressFrame(); break;
            case "Settings": next = new UserFrame(); break;
            default: return;
        }
        next.setVisible(true);
    }
}
