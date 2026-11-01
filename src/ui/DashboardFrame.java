import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardFrame extends JFrame {

    private final Color BG = new Color(8, 10, 20);
    private final Color SIDEBAR = new Color(13, 16, 31);
    private final Color CARD = new Color(21, 26, 50);
    private final Color CARD2 = new Color(25, 30, 58);

    private final Color PURPLE = new Color(124, 72, 255);
    private final Color BLUE = new Color(62, 137, 255);
    private final Color CYAN = new Color(25, 205, 220);
    private final Color GREEN = new Color(25, 210, 130);
    private final Color ORANGE = new Color(255, 165, 45);
    private final Color PINK = new Color(235, 50, 220);

    private final Color TEXT = new Color(245, 246, 252);
    private final Color MUTED = new Color(145, 155, 185);
    private final Color BORDER = new Color(45, 55, 95);

    private final int SIDEBAR_WIDTH = 300;
    private int overallProgress;

    public DashboardFrame(String userName) {

        setTitle("Study Planner");
        setSize(1550, 950);
        setMinimumSize(new Dimension(1200, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        overallProgress = getOverallProgress();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(createSidebar(userName), BorderLayout.WEST);
        root.add(createMainContent(userName), BorderLayout.CENTER);

        add(root);
    }

    private JPanel createSidebar(String userName) {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(35, 25, 25, 25));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(250, 45));

        JLabel sp = new JLabel("SP");
        sp.setForeground(PURPLE);
        sp.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel logo = new JLabel("  STUDY PLANNER");
        logo.setForeground(TEXT);
        logo.setFont(new Font("Arial", Font.BOLD, 21));

        logoPanel.add(sp);
        logoPanel.add(logo);

        sidebar.add(logoPanel);

        JLabel tagline = new JLabel("Plan  •  Focus  •  Achieve");
        tagline.setForeground(MUTED);
        tagline.setFont(new Font("Arial", Font.PLAIN, 13));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(tagline);
        sidebar.add(Box.createVerticalStrut(48));

        JLabel workspace = new JLabel("WORKSPACE");
        workspace.setForeground(new Color(105, 130, 190));
        workspace.setFont(new Font("Arial", Font.BOLD, 13));
        workspace.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(workspace);
        sidebar.add(Box.createVerticalStrut(18));

        JButton dashboard = menuButton("01", "Dashboard", true);
        JButton subjects = menuButton("02", "Subjects", false);
        JButton notes = menuButton("03", "Notes", false);
        JButton schedule = menuButton("04", "Schedule", false);
        JButton revision = menuButton("05", "Revision", false);
        JButton reminders = menuButton("06", "Reminders", false);
        JButton progress = menuButton("07", "Progress", false);
        JButton settings = menuButton("08", "Settings", false);

        sidebar.add(dashboard);
        sidebar.add(Box.createVerticalStrut(7));

        sidebar.add(subjects);
        sidebar.add(Box.createVerticalStrut(7));

        sidebar.add(notes);
        sidebar.add(Box.createVerticalStrut(7));

        sidebar.add(schedule);
        sidebar.add(Box.createVerticalStrut(7));

        sidebar.add(revision);
        sidebar.add(Box.createVerticalStrut(7));

        sidebar.add(reminders);
        sidebar.add(Box.createVerticalStrut(7));

        sidebar.add(progress);
        sidebar.add(Box.createVerticalStrut(7));

        sidebar.add(settings);

        sidebar.add(Box.createVerticalGlue());

        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER);
        separator.setMaximumSize(new Dimension(250, 1));
        sidebar.add(separator);

        sidebar.add(Box.createVerticalStrut(24));

        JLabel logged = new JLabel("LOGGED IN AS");
        logged.setForeground(new Color(105, 130, 190));
        logged.setFont(new Font("Arial", Font.BOLD, 12));
        logged.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logged);
        sidebar.add(Box.createVerticalStrut(12));

        JPanel profile = new JPanel(new BorderLayout(12, 0));
        profile.setBackground(CARD);
        profile.setMaximumSize(new Dimension(250, 70));
        profile.setPreferredSize(new Dimension(250, 70));
        profile.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel avatar = new JLabel(getInitials(userName), SwingConstants.CENTER);
        avatar.setForeground(Color.WHITE);
        avatar.setBackground(PURPLE);
        avatar.setOpaque(true);
        avatar.setFont(new Font("Arial", Font.BOLD, 15));
        avatar.setPreferredSize(new Dimension(55, 50));

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(userName);
        name.setForeground(TEXT);
        name.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel role = new JLabel("Student Account");
        role.setForeground(MUTED);
        role.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel online = new JLabel("● Online");
        online.setForeground(GREEN);
        online.setFont(new Font("Arial", Font.PLAIN, 11));

        userInfo.add(name);
        userInfo.add(Box.createVerticalStrut(2));
        userInfo.add(role);
        userInfo.add(online);

        profile.add(avatar, BorderLayout.WEST);
        profile.add(userInfo, BorderLayout.CENTER);

        sidebar.add(profile);
        sidebar.add(Box.createVerticalStrut(18));

        JButton logout = menuButton("09", "Logout", false);
        sidebar.add(logout);

        subjects.addActionListener(e -> {
            SubjectFrame subjectFrame = new SubjectFrame();
            subjectFrame.setVisible(true);
        });

        notes.addActionListener(e -> {
            NotesFrame notesFrame = new NotesFrame();
            notesFrame.setVisible(true);
        });

        schedule.addActionListener(e -> {
            ScheduleFrame scheduleFrame = new ScheduleFrame();
            scheduleFrame.setVisible(true);
        });

       revision.addActionListener(e -> {
    RevisionFrame revisionFrame = new RevisionFrame();
    revisionFrame.setVisible(true);
});

        reminders.addActionListener(e -> {
    ReminderFrame reminderFrame = new ReminderFrame();
    reminderFrame.setVisible(true);
});

        progress.addActionListener(e -> {
    ProgressFrame progressFrame = new ProgressFrame();
    progressFrame.setVisible(true);
});

settings.addActionListener(e -> {
    UserFrame userFrame = new UserFrame();
    userFrame.setVisible(true);
});
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        return sidebar;
    }

    private JButton menuButton(
            String number,
            String name,
            boolean active) {

        JButton button = new JButton();

        button.setLayout(new BorderLayout());

        button.setPreferredSize(new Dimension(250, 48));
        button.setMinimumSize(new Dimension(250, 48));
        button.setMaximumSize(new Dimension(250, 48));

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel inside = new JPanel(new BorderLayout());

        inside.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 15, 0, 15
                )
        );

        inside.setBackground(
                active ? PURPLE : SIDEBAR
        );

        JLabel num = new JLabel(number);
        num.setForeground(
                active
                        ? Color.WHITE
                        : new Color(90, 125, 190)
        );

        num.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        num.setPreferredSize(
                new Dimension(35, 0)
        );

        JLabel text = new JLabel(name);
        text.setForeground(TEXT);
        text.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        inside.add(num, BorderLayout.WEST);
        inside.add(text, BorderLayout.CENTER);

        button.add(
                inside,
                BorderLayout.CENTER
        );

        return button;
    }

    private JPanel createMainContent(String userName) {

        JPanel content = new JPanel(
                new BorderLayout()
        );

        content.setBackground(BG);

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 40, 30, 40
                )
        );

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setOpaque(false);

        JPanel welcomePanel = new JPanel();

        welcomePanel.setOpaque(false);

        welcomePanel.setLayout(
                new BoxLayout(
                        welcomePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel welcome = new JLabel(
                "Good Morning, " + userName + "!"
        );

        welcome.setForeground(TEXT);

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        34
                )
        );

        JLabel subtitle = new JLabel(
                "Stay consistent and make today count."
        );

        subtitle.setForeground(MUTED);

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        welcomePanel.add(welcome);

        welcomePanel.add(
                Box.createVerticalStrut(7)
        );

        welcomePanel.add(subtitle);

        JPanel tools = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        10,
                        0
                )
        );

        tools.setOpaque(false);

        JTextField search = new JTextField(
                "Search anything..."
        );

        search.setPreferredSize(
                new Dimension(320, 50)
        );

        search.setForeground(MUTED);
        search.setBackground(CARD);

        search.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        search.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                0, 15, 0, 15
                        )
                )
        );

        JButton notification = new JButton("!");

        notification.setPreferredSize(
                new Dimension(50, 50)
        );

        notification.setForeground(TEXT);
        notification.setBackground(CARD);

        notification.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        notification.setFocusPainted(false);

        notification.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        JLabel userIcon = new JLabel(
                getInitials(userName),
                SwingConstants.CENTER
        );

        userIcon.setPreferredSize(
                new Dimension(50, 50)
        );

        userIcon.setForeground(Color.WHITE);
        userIcon.setBackground(PURPLE);
        userIcon.setOpaque(true);

        userIcon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        tools.add(search);
        tools.add(notification);
        tools.add(userIcon);

        header.add(
                welcomePanel,
                BorderLayout.WEST
        );

        header.add(
                tools,
                BorderLayout.EAST
        );

        content.add(
                header,
                BorderLayout.NORTH
        );

        JPanel body = new JPanel();

        body.setOpaque(false);

        body.setLayout(
                new BoxLayout(
                        body,
                        BoxLayout.Y_AXIS
                )
        );

        body.add(
                Box.createVerticalStrut(30)
        );

        JPanel stats = new JPanel(
                new GridLayout(
                        1, 4, 18, 0
                )
        );

        stats.setOpaque(false);

        stats.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        145
                )
        );

        stats.add(
                statCard(
                        "01",
                        getSubjectCount(),
                        "Total Subjects",
                        "Active Subjects",
                        PURPLE
                )
        );

        stats.add(
                statCard(
                        "02",
                        getTotalStudyHours(),
                        "Study Hours",
                        "Today's Study Time",
                        BLUE
                )
        );

        stats.add(
                statCard(
                        "03",
                        "7 Days",
                        "Current Streak",
                        "Keep it going!",
                        GREEN
                )
        );

        stats.add(
                statCard(
                        "04",
                        overallProgress + "%",
                        "Overall Progress",
                        "Completed study records",
                        PINK
                )
        );

        body.add(stats);

        body.add(
                Box.createVerticalStrut(22)
        );

        JPanel middle = new JPanel(
                new GridLayout(
                        1, 2, 20, 0
                )
        );

        middle.setOpaque(false);

        middle.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        320
                )
        );

        middle.add(
                createScheduleCard()
        );

        middle.add(
                createProgressCard()
        );

        body.add(middle);

        body.add(
                Box.createVerticalStrut(22)
        );

        JPanel bottom = new JPanel(
                new GridLayout(
                        1, 2, 20, 0
                )
        );

        bottom.setOpaque(false);

        bottom.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        190
                )
        );

        bottom.add(
                createNotesCard()
        );

        bottom.add(
                createReminderCard()
        );

        body.add(bottom);

        content.add(
                body,
                BorderLayout.CENTER
        );

        return content;
    }

    private String getSubjectCount() {
        String sql = "SELECT COUNT(*) FROM subjects WHERE user_id = 1";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Subject count error: " + e.getMessage());
        }

        return "0";
    }

    private int getOverallProgress() {
        String sql = "SELECT COUNT(*) AS total, " +
                     "COALESCE(SUM(CASE WHEN completed = 1 THEN 1 ELSE 0 END), 0) AS done " +
                     "FROM progress WHERE user_id = 1";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                int total = rs.getInt("total");
                int done = rs.getInt("done");

                if (total == 0) {
                    return 0;
                }

                return (int) Math.round(done * 100.0 / total);
            }
        } catch (Exception e) {
            System.out.println("Overall progress error: " + e.getMessage());
        }

        return 0;
    }

    private String getTotalStudyHours() {
        String sql = "SELECT COALESCE(SUM(hours_studied), 0) " +
                     "FROM progress WHERE user_id = 1";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                double hours = rs.getDouble(1);

                if (hours == (long) hours) {
                    return (long) hours + "h";
                }

                return hours + "h";
            }
        } catch (Exception e) {
            System.out.println("Study hours error: " + e.getMessage());
        }

        return "0h";
    }

    private JPanel statCard(
            String number,
            String value,
            String title,
            String subtitle,
            Color accent) {

        JPanel panel = roundedPanel(
                CARD,
                BORDER
        );

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 20, 18, 20
                )
        );

        JLabel num = new JLabel(number);

        num.setForeground(accent);

        num.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        JLabel val = new JLabel(value);

        val.setForeground(TEXT);

        val.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        29
                )
        );

        JLabel titleLabel = new JLabel(title);

        titleLabel.setForeground(TEXT);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JLabel sub = new JLabel(subtitle);

        sub.setForeground(MUTED);

        sub.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        JPanel center = new JPanel();

        center.setOpaque(false);

        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );

        center.add(val);

        center.add(titleLabel);

        center.add(
                Box.createVerticalStrut(5)
        );

        center.add(sub);

        panel.add(
                num,
                BorderLayout.NORTH
        );

        panel.add(
                center,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createScheduleCard() {

        JPanel panel = roundedPanel(
                CARD,
                BORDER
        );

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 20, 18, 20
                )
        );

        JPanel titlePanel = new JPanel(
                new BorderLayout()
        );

        titlePanel.setOpaque(false);

        JLabel title = new JLabel(
                "Today's Schedule"
        );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        JLabel view = new JLabel(
                "VIEW ALL  >"
        );

        view.setForeground(
                new Color(
                        140, 95, 255
                )
        );

        view.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        titlePanel.add(
                title,
                BorderLayout.WEST
        );

        titlePanel.add(
                view,
                BorderLayout.EAST
        );

        JPanel rows = new JPanel();

        rows.setOpaque(false);

        rows.setLayout(
                new BoxLayout(
                        rows,
                        BoxLayout.Y_AXIS
                )
        );

        rows.add(
                scheduleRow(
                        "10:00 AM",
                        "Data Structures",
                        "Chapter 4: Trees",
                        "Completed",
                        GREEN
                )
        );

        rows.add(
                Box.createVerticalStrut(10)
        );

        rows.add(
                scheduleRow(
                        "12:00 PM",
                        "Java Programming",
                        "Object Oriented Programming",
                        "Upcoming",
                        ORANGE
                )
        );

        rows.add(
                Box.createVerticalStrut(10)
        );

        rows.add(
                scheduleRow(
                        "02:00 PM",
                        "Database Systems",
                        "Normalization",
                        "Upcoming",
                        ORANGE
                )
        );

        panel.add(
                titlePanel,
                BorderLayout.NORTH
        );

        panel.add(
                rows,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel scheduleRow(
            String time,
            String subject,
            String chapter,
            String status,
            Color statusColor) {

        JPanel row = new JPanel(
                new BorderLayout(15, 0)
        );

        row.setBackground(CARD2);

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                9, 15, 9, 15
                        )
                )
        );

        row.setPreferredSize(
                new Dimension(0, 65)
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        65
                )
        );

        JLabel timeLabel = new JLabel(time);

        timeLabel.setForeground(
                new Color(
                        105, 135, 255
                )
        );

        timeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        timeLabel.setPreferredSize(
                new Dimension(90, 0)
        );

        JPanel info = new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel subjectLabel = new JLabel(subject);

        subjectLabel.setForeground(TEXT);

        subjectLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JLabel chapterLabel = new JLabel(chapter);

        chapterLabel.setForeground(MUTED);

        chapterLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );

        info.add(subjectLabel);

        info.add(
                Box.createVerticalStrut(3)
        );

        info.add(chapterLabel);

        JLabel statusLabel = new JLabel(status);

        statusLabel.setForeground(statusColor);

        statusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        row.add(
                timeLabel,
                BorderLayout.WEST
        );

        row.add(
                info,
                BorderLayout.CENTER
        );

        row.add(
                statusLabel,
                BorderLayout.EAST
        );

        return row;
    }

    private JPanel createProgressCard() {

        JPanel panel = roundedPanel(
                CARD,
                BORDER
        );

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 20, 15, 20
                )
        );

        JPanel top = new JPanel(
                new BorderLayout()
        );

        top.setOpaque(false);

        JLabel title = new JLabel(
                "Weekly Progress"
        );

        title.setForeground(TEXT);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        JLabel week = new JLabel(
                "THIS WEEK"
        );

        week.setForeground(MUTED);

        week.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        10
                )
        );

        top.add(
                title,
                BorderLayout.WEST
        );

        top.add(
                week,
                BorderLayout.EAST
        );

        JPanel graph = new JPanel(
                new BorderLayout()
        );

        graph.setOpaque(false);

        ProgressCircle circle =
                new ProgressCircle();

        circle.setPreferredSize(
                new Dimension(190, 190)
        );

        JPanel textPanel = new JPanel();

        textPanel.setOpaque(false);

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 15, 0, 0
                )
        );

        JLabel heading = new JLabel(
                "You're doing great!"
        );

        heading.setForeground(TEXT);

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        JLabel line1 = new JLabel(
                "Keep up the momentum"
        );

        line1.setForeground(MUTED);

        line1.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        JLabel line2 = new JLabel(
                "and achieve your goals."
        );

        line2.setForeground(MUTED);

        line2.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        textPanel.add(heading);

        textPanel.add(
                Box.createVerticalStrut(12)
        );

        textPanel.add(line1);
        textPanel.add(line2);

        graph.add(
                circle,
                BorderLayout.WEST
        );

        graph.add(
                textPanel,
                BorderLayout.CENTER
        );

        panel.add(
                top,
                BorderLayout.NORTH
        );

        panel.add(
                graph,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createNotesCard() {

        JPanel panel = roundedPanel(
                CARD,
                BORDER
        );

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 20, 15, 20
                )
        );

        JPanel title = new JPanel(
                new BorderLayout()
        );

        title.setOpaque(false);

        JLabel heading = new JLabel(
                "Recent Notes"
        );

        heading.setForeground(TEXT);

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        JLabel view = new JLabel(
                "VIEW ALL  >"
        );

        view.setForeground(PURPLE);

        view.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        title.add(
                heading,
                BorderLayout.WEST
        );

        title.add(
                view,
                BorderLayout.EAST
        );

        JPanel list = new JPanel();

        list.setOpaque(false);

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.add(
                noteRow(
                        "N",
                        "Java OOP Concepts",
                        "Classes, Objects, Inheritance...",
                        "Today, 09:15 AM",
                        PURPLE
                )
        );

        list.add(
                Box.createVerticalStrut(8)
        );

        list.add(
                noteRow(
                        "N",
                        "Database Management",
                        "ER Model, Keys, Relationships...",
                        "Yesterday, 08:30 PM",
                        ORANGE
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        panel.add(
                list,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel noteRow(
            String icon,
            String title,
            String description,
            String time,
            Color color) {

        JPanel row = new JPanel(
                new BorderLayout(12, 0)
        );

        row.setBackground(CARD2);

        row.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 10, 8, 10
                )
        );

        row.setPreferredSize(
                new Dimension(0, 58)
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        58
                )
        );

        JLabel iconLabel = new JLabel(
                icon,
                SwingConstants.CENTER
        );

        iconLabel.setPreferredSize(
                new Dimension(45, 42)
        );

        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBackground(color);
        iconLabel.setOpaque(true);

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JPanel info = new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel = new JLabel(title);

        titleLabel.setForeground(TEXT);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        JLabel desc = new JLabel(description);

        desc.setForeground(MUTED);

        desc.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        info.add(titleLabel);

        info.add(
                Box.createVerticalStrut(3)
        );

        info.add(desc);

        JLabel timeLabel = new JLabel(time);

        timeLabel.setForeground(MUTED);

        timeLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        row.add(
                iconLabel,
                BorderLayout.WEST
        );

        row.add(
                info,
                BorderLayout.CENTER
        );

        row.add(
                timeLabel,
                BorderLayout.EAST
        );

        return row;
    }

    private JPanel createReminderCard() {

        JPanel panel = roundedPanel(
                CARD,
                BORDER
        );

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 20, 15, 20
                )
        );

        JPanel title = new JPanel(
                new BorderLayout()
        );

        title.setOpaque(false);

        JLabel heading = new JLabel(
                "Upcoming Reminders"
        );

        heading.setForeground(TEXT);

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        JLabel view = new JLabel(
                "VIEW ALL  >"
        );

        view.setForeground(PURPLE);

        view.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        11
                )
        );

        title.add(
                heading,
                BorderLayout.WEST
        );

        title.add(
                view,
                BorderLayout.EAST
        );

        JPanel list = new JPanel();

        list.setOpaque(false);

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );

        list.add(
                reminderRow(
                        "R",
                        "Revision Session",
                        "Database Systems",
                        "Today",
                        "05:00 PM",
                        PINK
                )
        );

        list.add(
                Box.createVerticalStrut(8)
        );

        list.add(
                reminderRow(
                        "R",
                        "Assignment Submission",
                        "Java Programming",
                        "Today",
                        "07:00 PM",
                        CYAN
                )
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        panel.add(
                list,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel reminderRow(
            String icon,
            String title,
            String subject,
            String day,
            String time,
            Color color) {

        JPanel row = new JPanel(
                new BorderLayout(12, 0)
        );

        row.setBackground(CARD2);

        row.setBorder(
                BorderFactory.createEmptyBorder(
                        8, 10, 8, 10
                )
        );

        row.setPreferredSize(
                new Dimension(0, 58)
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        58
                )
        );

        JLabel iconLabel = new JLabel(
                icon,
                SwingConstants.CENTER
        );

        iconLabel.setPreferredSize(
                new Dimension(45, 42)
        );

        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBackground(color);
        iconLabel.setOpaque(true);

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JPanel info = new JPanel();

        info.setOpaque(false);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel = new JLabel(title);

        titleLabel.setForeground(TEXT);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        JLabel subjectLabel = new JLabel(subject);

        subjectLabel.setForeground(MUTED);

        subjectLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        info.add(titleLabel);

        info.add(
                Box.createVerticalStrut(3)
        );

        info.add(subjectLabel);

        JPanel timing = new JPanel();

        timing.setOpaque(false);

        timing.setLayout(
                new BoxLayout(
                        timing,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel dayLabel = new JLabel(day);

        dayLabel.setForeground(MUTED);

        dayLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        dayLabel.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        JLabel timeLabel = new JLabel(time);

        timeLabel.setForeground(color);

        timeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        timeLabel.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        timing.add(dayLabel);

        timing.add(
                Box.createVerticalStrut(2)
        );

        timing.add(timeLabel);

        row.add(
                iconLabel,
                BorderLayout.WEST
        );

        row.add(
                info,
                BorderLayout.CENTER
        );

        row.add(
                timing,
                BorderLayout.EAST
        );

        return row;
    }

    private JPanel roundedPanel(
            Color background,
            Color borderColor) {

        JPanel panel = new JPanel() {

            protected void paintComponent(
                    Graphics g) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(background);

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        14,
                        14
                );

                g2.setColor(borderColor);

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        14,
                        14
                );

                g2.dispose();
            }
        };

        panel.setOpaque(false);

        return panel;
    }

    private String getInitials(String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "TU";
        }

        String[] parts =
                name.trim().split("\\s+");

        if (parts.length == 1) {

            return parts[0]
                    .substring(
                            0,
                            Math.min(
                                    2,
                                    parts[0].length()
                            )
                    )
                    .toUpperCase();
        }

        return (
                parts[0].charAt(0) + "" +
                parts[parts.length - 1].charAt(0)
        ).toUpperCase();
    }

    private class ProgressCircle
            extends JPanel {

        public ProgressCircle() {
            setOpaque(false);
        }

        protected void paintComponent(
                Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = 145;
            int x = 20;
            int y = 15;

            g2.setStroke(
                    new BasicStroke(
                            14,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.setColor(
                    new Color(
                            55, 65, 105
                    )
            );

            g2.drawArc(
                    x,
                    y,
                    size,
                    size,
                    0,
                    360
            );

            g2.setColor(PURPLE);

            g2.drawArc(
                    x,
                    y,
                    size,
                    size,
                    90,
                    -Math.round(360 * overallProgress / 100.0f)
            );

            g2.setColor(TEXT);

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            32
                    )
            );

            String value = overallProgress + "%";

            FontMetrics fm =
                    g2.getFontMetrics();

            int tx =
                    x +
                    (size -
                    fm.stringWidth(value)) / 2;

            int ty =
                    y +
                    size / 2 +
                    10;

            g2.drawString(
                    value,
                    tx,
                    ty
            );

            g2.setColor(MUTED);

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            12
                    )
            );

            String label = "Completed";

            fm = g2.getFontMetrics();

            int lx =
                    x +
                    (size -
                    fm.stringWidth(label)) / 2;

            g2.drawString(
                    label,
                    lx,
                    ty + 20
            );

            g2.dispose();
        }
    }
}