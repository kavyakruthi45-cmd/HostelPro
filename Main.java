
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class Main extends JFrame {

    // HIGH-CONTRAST PROFESSIONAL THEME
    static final Color NAVY = new Color(18, 30, 55);
    static final Color NAVY_LIGHT = new Color(30, 48, 78);
    static final Color BLUE = new Color(35, 105, 220);
    static final Color BLUE_DARK = new Color(22, 75, 165);
    static final Color BG = new Color(242, 245, 250);
    static final Color WHITE = Color.WHITE;
    static final Color TEXT = new Color(25, 35, 55);
    static final Color MUTED = new Color(85, 98, 120);
    static final Color GREEN = new Color(20, 145, 95);
    static final Color RED = new Color(195, 55, 65);
    static final Color BORDER = new Color(215, 222, 233);

    private CardLayout cardLayout = new CardLayout();
    private JPanel content = new JPanel(cardLayout);
    private JLabel welcomeLabel;

    private DefaultTableModel studentModel;
    private JTable studentTable;
    private DefaultTableModel feeModel;
    private JTable feeTable;

    private List<Student> students;

    public Main() {
        students = HostelService.loadStudents();

        setTitle("HostelPro Premium | Hostel Management System");
        setSize(1250, 760);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        createSidebar();
        createPages();

        getContentPane().setBackground(BG);
        setVisible(true);
    }

    // LOGIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.put("OptionPane.background", WHITE);
                UIManager.put("Panel.background", WHITE);
                UIManager.put("OptionPane.messageForeground", TEXT);
                UIManager.put("Label.foreground", TEXT);
                UIManager.put("TextField.foreground", TEXT);
                UIManager.put("TextField.background", WHITE);
                UIManager.put("PasswordField.foreground", TEXT);
                UIManager.put("PasswordField.background", WHITE);
                UIManager.put("ComboBox.foreground", TEXT);
                UIManager.put("ComboBox.background", WHITE);
            } catch (Exception ignored) {}

            showLogin();
        });
    }

    static void showLogin() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("HOSTELPRO PREMIUM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(NAVY);

        JLabel subtitle = new JLabel("Administrator Login", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JTextField username = new JTextField("admin", 18);
        JPasswordField password = new JPasswordField(18);

        styleInput(username);
        styleInput(password);

        panel.add(title);
        panel.add(subtitle);
        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Password"));
        panel.add(password);

        int result = JOptionPane.showConfirmDialog(
            null, panel, "HostelPro Login",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String user = username.getText().trim();
            String pass = new String(password.getPassword());

            if (user.equals("admin") && pass.equals("admin123")) {
                new Main();
            } else {
                JOptionPane.showMessageDialog(
                    null, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE
                );
                showLogin();
            }
        }
    }

    // SIDEBAR
    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(235, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(22, 14, 18, 14));

        JLabel logo = new JLabel("HOSTELPRO");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        logo.setForeground(WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel premium = new JLabel("PREMIUM MANAGEMENT");
        premium.setFont(new Font("Segoe UI", Font.BOLD, 10));
        premium.setForeground(new Color(180, 200, 230));
        premium.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(premium);
        sidebar.add(Box.createVerticalStrut(35));

        addNavButton(sidebar, "Dashboard", "dashboard");
        addNavButton(sidebar, "Student Management", "students");
        addNavButton(sidebar, "Room Management", "rooms");
        addNavButton(sidebar, "Fee Management", "fees");
        addNavButton(sidebar, "Reports & Analytics", "reports");

        sidebar.add(Box.createVerticalGlue());

        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logout.setForeground(WHITE);
        logout.setBackground(new Color(165, 48, 60));
        logout.setFocusPainted(false);
        logout.setBorder(new EmptyBorder(12, 15, 12, 15));
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logout.addActionListener(e -> {
            dispose();
            showLogin();
        });

        sidebar.add(logout);
        add(sidebar, BorderLayout.WEST);
    }

    private void addNavButton(JPanel sidebar, String text, String page) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(WHITE);
        button.setBackground(NAVY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(13, 12, 13, 8));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(NAVY_LIGHT);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(NAVY);
            }
        });

        button.addActionListener(e -> {
            cardLayout.show(content, page);
            refreshTables();
        });

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }

    // PAGES
    private void createPages() {
        content.setBackground(BG);

        content.add(createDashboard(), "dashboard");
        content.add(createStudentPage(), "students");
        content.add(createRoomPage(), "rooms");
        content.add(createFeePage(), "fees");
        content.add(createReportPage(), "reports");

        add(content, BorderLayout.CENTER);
    }

    private JPanel pagePanel(String heading, String description) {
        JPanel page = new JPanel(new BorderLayout(15, 15));
        page.setBackground(BG);
        page.setBorder(new EmptyBorder(25, 28, 25, 28));

        JPanel top = new JPanel();
        top.setBackground(BG);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(heading);
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(NAVY);

        JLabel sub = new JLabel(description);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(MUTED);

        top.add(title);
        top.add(Box.createVerticalStrut(5));
        top.add(sub);

        page.add(top, BorderLayout.NORTH);
        return page;
    }

    // DASHBOARD
    private JPanel createDashboard() {
        JPanel page = pagePanel(
            "Dashboard",
            "Welcome to your HostelPro Premium management dashboard"
        );

        JPanel body = new JPanel(new BorderLayout(15, 15));
        body.setBackground(BG);

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 15));
        cards.setBackground(BG);

        cards.add(statCard("TOTAL STUDENTS", () -> "" + students.size(), BLUE));
        cards.add(statCard("FEES PAID", () -> "" + countPaid(), GREEN));
        cards.add(statCard("FEES PENDING", () -> "" + countPending(), RED));

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(WHITE);
        info.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel infoTitle = new JLabel("Hostel Management Overview");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        infoTitle.setForeground(NAVY);

        JTextArea description = new JTextArea(
            "\nWelcome to HostelPro Premium!\n\n" +
            "Manage your hostel activities from one place.\n\n" +
            "• Register and manage student records\n" +
            "• Track room allocation and occupancy\n" +
            "• Manage student fee payments\n" +
            "• Generate printable fee receipts\n" +
            "• View hostel reports and statistics\n\n" +
            "Use the navigation menu to access each section."
        );
        description.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        description.setForeground(TEXT);
        description.setBackground(WHITE);
        description.setEditable(false);
        description.setFocusable(false);

        info.add(infoTitle, BorderLayout.NORTH);
        info.add(description, BorderLayout.CENTER);

        body.add(cards, BorderLayout.NORTH);
        body.add(info, BorderLayout.CENTER);

        page.add(body, BorderLayout.CENTER);
        return page;
    }

    interface ValueProvider {
        String get();
    }

    private JPanel statCard(String title, ValueProvider value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(5, 12));
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(22, 20, 22, 20)
        ));

        JLabel name = new JLabel(title);
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));
        name.setForeground(MUTED);

        JLabel number = new JLabel(value.get());
        number.setFont(new Font("Segoe UI", Font.BOLD, 34));
        number.setForeground(accent);

        card.add(name, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);
        return card;
    }

    // STUDENT MANAGEMENT
    private JPanel createStudentPage() {
        JPanel page = pagePanel(
            "Student Management",
            "Add, edit, search and manage hostel student records"
        );

        studentModel = new DefaultTableModel(
            new String[]{"Name", "Roll Number", "Phone", "Course", "Room", "Fee", "Status"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = createTable(studentModel);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBackground(BG);

        JButton add = button("Add Student", BLUE);
        JButton edit = button("Edit Student", NAVY);
        JButton delete = button("Delete Student", RED);

        JTextField search = new JTextField(18);
        styleInput(search);
        search.setToolTipText("Search by name or roll number");

        JButton find = button("Search", BLUE_DARK);
        JButton clear = button("Show All", MUTED);

        add.addActionListener(e -> addStudent());
        edit.addActionListener(e -> editStudent());
        delete.addActionListener(e -> deleteStudent());
        find.addActionListener(e -> searchStudents(search.getText()));
        clear.addActionListener(e -> {
            search.setText("");
            refreshTables();
        });

        toolbar.add(add);
        toolbar.add(edit);
        toolbar.add(delete);
        toolbar.add(search);
        toolbar.add(find);
        toolbar.add(clear);

        JPanel center = whitePanel();
        center.setLayout(new BorderLayout(10, 10));
        center.add(toolbar, BorderLayout.NORTH);
        center.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        page.add(center, BorderLayout.CENTER);
        refreshTables();
        return page;
    }

    private void addStudent() {
        JTextField name = new JTextField();
        JTextField roll = new JTextField();
        JTextField phone = new JTextField();
        JTextField course = new JTextField();
        JTextField room = new JTextField("101");
        JTextField fee = new JTextField("0");

        styleInput(name);
        styleInput(roll);
        styleInput(phone);
        styleInput(course);
        styleInput(room);
        styleInput(fee);

        JPanel form = studentForm(name, roll, phone, course, room, fee);

        int result = JOptionPane.showConfirmDialog(
            this, form, "Add New Student",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                if (name.getText().trim().isEmpty() ||
                    roll.getText().trim().isEmpty()) {
                    showError("Student name and roll number are required.");
                    return;
                }

                for (Student s : students) {
                    if (s.roll.equalsIgnoreCase(roll.getText().trim())) {
                        showError("This roll number already exists.");
                        return;
                    }
                }

                double amount = Double.parseDouble(fee.getText().trim());
                if (amount < 0) {
                    showError("Fee cannot be negative.");
                    return;
                }

                Student s = new Student(
                    name.getText().trim(),
                    roll.getText().trim(),
                    phone.getText().trim(),
                    course.getText().trim(),
                    room.getText().trim(),
                    amount
                );

                students.add(s);
                save();
                refreshTables();
                showMessage("Student added successfully!");

            } catch (NumberFormatException ex) {
                showError("Please enter a valid fee amount.");
            }
        }
    }

    private JPanel studentForm(
        JTextField name, JTextField roll, JTextField phone,
        JTextField course, JTextField room, JTextField fee
    ) {
        JPanel form = new JPanel(new GridLayout(0, 2, 12, 10));
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        addFormField(form, "Student Name", name);
        addFormField(form, "Roll Number", roll);
        addFormField(form, "Phone Number", phone);
        addFormField(form, "Course", course);
        addFormField(form, "Room Number", room);
        addFormField(form, "Total Fee", fee);

        return form;
    }

    private void editStudent() {
        int row = studentTable.getSelectedRow();

        if (row < 0) {
            showError("Please select a student to edit.");
            return;
        }

        String rollNumber = studentModel.getValueAt(row, 1).toString();
        Student s = findStudent(rollNumber);

        if (s == null) return;

        JTextField name = new JTextField(s.name);
        JTextField roll = new JTextField(s.roll);
        JTextField phone = new JTextField(s.phone);
        JTextField course = new JTextField(s.course);
        JTextField room = new JTextField(s.room);
        JTextField fee = new JTextField(String.valueOf(s.fee));

        styleInput(name);
        styleInput(roll);
        styleInput(phone);
        styleInput(course);
        styleInput(room);
        styleInput(fee);

        JPanel form = studentForm(name, roll, phone, course, room, fee);

        int result = JOptionPane.showConfirmDialog(
            this, form, "Edit Student",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                if (name.getText().trim().isEmpty() ||
                    roll.getText().trim().isEmpty()) {
                    showError("Student name and roll number are required.");
                    return;
                }

                for (Student other : students) {
                    if (other != s &&
                        other.roll.equalsIgnoreCase(roll.getText().trim())) {
                        showError("Another student already uses this roll number.");
                        return;
                    }
                }

                double amount = Double.parseDouble(fee.getText().trim());
                if (amount < 0) {
                    showError("Fee cannot be negative.");
                    return;
                }

                s.name = name.getText().trim();
                s.roll = roll.getText().trim();
                s.phone = phone.getText().trim();
                s.course = course.getText().trim();
                s.room = room.getText().trim();
                s.fee = amount;

                save();
                refreshTables();
                showMessage("Student details updated!");

            } catch (NumberFormatException ex) {
                showError("Please enter a valid fee amount.");
            }
        }
    }

    private void deleteStudent() {
        int row = studentTable.getSelectedRow();

        if (row < 0) {
            showError("Please select a student to delete.");
            return;
        }

        String roll = studentModel.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this student?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            students.removeIf(s -> s.roll.equals(roll));
            save();
            refreshTables();
            showMessage("Student deleted successfully.");
        }
    }

    private void searchStudents(String query) {
        studentModel.setRowCount(0);
        String q = query.trim().toLowerCase();

        for (Student s : students) {
            if (s.name.toLowerCase().contains(q) ||
                s.roll.toLowerCase().contains(q)) {
                addStudentRow(s);
            }
        }
    }

    // ROOM MANAGEMENT
    private JPanel createRoomPage() {
        JPanel page = pagePanel(
            "Room Management",
            "View room occupancy and available student capacity"
        );

        String[] columns = {"Room Number", "Occupied Beds", "Available Beds", "Capacity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = createTable(model);

        for (int room = 101; room <= 112; room++) {
            int count = 0;
            for (Student s : students) {
                if (s.room.equals(String.valueOf(room))) count++;
            }
            model.addRow(new Object[]{
                room, count, Math.max(0, 4 - count), 4
            });
        }

        JPanel center = whitePanel();
        center.setLayout(new BorderLayout());
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = button("Refresh Room Details", BLUE);
        refresh.addActionListener(e -> {
            model.setRowCount(0);
            for (int room = 101; room <= 112; room++) {
                int count = 0;
                for (Student s : students) {
                    if (s.room.equals(String.valueOf(room))) count++;
                }
                model.addRow(new Object[]{
                    room, count, Math.max(0, 4 - count), 4
                });
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(WHITE);
        bottom.add(refresh);
        center.add(bottom, BorderLayout.SOUTH);

        page.add(center, BorderLayout.CENTER);
        return page;
    }

    // FEE MANAGEMENT
    private JPanel createFeePage() {
        JPanel page = pagePanel(
            "Fee Management",
            "Track fee payment status and print student receipts"
        );

        feeModel = new DefaultTableModel(
            new String[]{"Name", "Roll Number", "Room", "Fee Amount", "Payment Status"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        feeTable = createTable(feeModel);

        JButton paid = button("Mark Fee as Paid", GREEN);
        JButton receipt = button("Print Receipt", BLUE);
        JButton refresh = button("Refresh", NAVY);

        paid.addActionListener(e -> markPaid());
        receipt.addActionListener(e -> printReceipt());
        refresh.addActionListener(e -> refreshTables());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBackground(WHITE);
        toolbar.add(paid);
        toolbar.add(receipt);
        toolbar.add(refresh);

        JPanel center = whitePanel();
        center.setLayout(new BorderLayout());
        center.add(toolbar, BorderLayout.NORTH);
        center.add(new JScrollPane(feeTable), BorderLayout.CENTER);

        page.add(center, BorderLayout.CENTER);
        refreshTables();
        return page;
    }

    private void markPaid() {
        int row = feeTable.getSelectedRow();

        if (row < 0) {
            showError("Please select a student.");
            return;
        }

        String roll = feeModel.getValueAt(row, 1).toString();
        Student s = findStudent(roll);

        if (s == null) return;

        s.paid = true;
        save();
        refreshTables();

        showMessage("Fee marked as paid successfully.");
    }

    private void printReceipt() {
        int row = feeTable.getSelectedRow();

        if (row < 0) {
            showError("Please select a student.");
            return;
        }

        String roll = feeModel.getValueAt(row, 1).toString();
        Student s = findStudent(roll);

        if (s == null) return;

        if (!s.paid) {
            showError("This student's fee is still pending.");
            return;
        }

        JTextArea receipt = new JTextArea();
        receipt.setFont(new Font("Monospaced", Font.PLAIN, 14));
        receipt.setForeground(TEXT);
        receipt.setBackground(WHITE);
        receipt.setEditable(false);

        receipt.setText(
            "             HOSTELPRO PREMIUM\n" +
            "             FEE PAYMENT RECEIPT\n" +
            "------------------------------------------\n" +
            "Student Name : " + s.name + "\n" +
            "Roll Number  : " + s.roll + "\n" +
            "Phone        : " + s.phone + "\n" +
            "Course       : " + s.course + "\n" +
            "Room Number  : " + s.room + "\n" +
            "Fee Amount   : Rs. " + s.fee + "\n" +
            "Status       : PAID\n" +
            "------------------------------------------\n" +
            "       Thank you for your payment!\n" +
            "------------------------------------------\n" +
            "This is a computer-generated receipt.\n"
        );

        try {
            receipt.print();
        } catch (Exception ex) {
            showError("Unable to print receipt: " + ex.getMessage());
        }
    }

    // REPORTS
    private JPanel createReportPage() {
        JPanel page = pagePanel(
            "Reports & Analytics",
            "View student statistics and hostel fee collection summary"
        );

        JPanel center = whitePanel();
        center.setLayout(new BorderLayout(10, 15));

        JTextArea report = new JTextArea();
        report.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        report.setForeground(TEXT);
        report.setBackground(WHITE);
        report.setEditable(false);
        report.setMargin(new Insets(20, 20, 20, 20));

        JButton refresh = button("Generate Report", BLUE);
        JButton print = button("Print Report", GREEN);

        refresh.addActionListener(e -> report.setText(generateReport()));
        print.addActionListener(e -> {
            try {
                report.print();
            } catch (Exception ex) {
                showError("Unable to print report.");
            }
        });

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(WHITE);
        toolbar.add(refresh);
        toolbar.add(print);

        center.add(toolbar, BorderLayout.NORTH);
        center.add(new JScrollPane(report), BorderLayout.CENTER);

        report.setText(generateReport());

        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private String generateReport() {
        double total = 0;
        double collected = 0;
        int paidCount = 0;
        int pendingCount = 0;

        for (Student s : students) {
            total += s.fee;
            if (s.paid) {
                collected += s.fee;
                paidCount++;
            } else {
                pendingCount++;
            }
        }

        return
            "HOSTELPRO PREMIUM - HOSTEL REPORT\n" +
            "============================================\n\n" +
            "STUDENT STATISTICS\n" +
            "--------------------------------------------\n" +
            "Total Students       : " + students.size() + "\n" +
            "Fees Paid Students   : " + paidCount + "\n" +
            "Fees Pending Students: " + pendingCount + "\n\n" +
            "FEE COLLECTION SUMMARY\n" +
            "--------------------------------------------\n" +
            "Total Expected Fees  : Rs. " + total + "\n" +
            "Total Fees Collected : Rs. " + collected + "\n" +
            "Pending Fee Amount   : Rs. " + (total - collected) + "\n\n" +
            "ROOM SUMMARY\n" +
            "--------------------------------------------\n" +
            "Rooms Available      : 101 - 112\n" +
            "Total Room Capacity  : 48 beds\n\n" +
            "============================================\n" +
            "Generated by HostelPro Premium\n";
    }

    // TABLE REFRESH
    private void refreshTables() {
        if (studentModel != null) {
            studentModel.setRowCount(0);
            for (Student s : students) addStudentRow(s);
        }

        if (feeModel != null) {
            feeModel.setRowCount(0);
            for (Student s : students) {
                feeModel.addRow(new Object[]{
                    s.name, s.roll, s.room, "Rs. " + s.fee,
                    s.paid ? "PAID" : "PENDING"
                });
            }
        }
    }

    private void addStudentRow(Student s) {
        studentModel.addRow(new Object[]{
            s.name, s.roll, s.phone, s.course, s.room,
            "Rs. " + s.fee, s.paid ? "PAID" : "PENDING"
        });
    }

    // UI HELPERS
    private static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setSelectionBackground(new Color(205, 225, 255));
        table.setSelectionForeground(NAVY);
        table.setRowHeight(34);
        table.setGridColor(BORDER);
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(NAVY);
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        return table;
    }

    private static JPanel whitePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        return panel;
    }

    private static JButton button(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(WHITE);
        b.setBackground(color);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(11, 15, 11, 15));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setCaretColor(TEXT);
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(165, 180, 200), 1),
            new EmptyBorder(8, 9, 8, 9)
        ));
    }

    private static void addFormField(JPanel panel, String label, JTextField field) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT);
        panel.add(l);
        panel.add(field);
    }

    private Student findStudent(String roll) {
        for (Student s : students) {
            if (s.roll.equals(roll)) return s;
        }
        return null;
    }

    private int countPaid() {
        int count = 0;
        for (Student s : students) if (s.paid) count++;
        return count;
    }

    private int countPending() {
        return students.size() - countPaid();
    }

    private void save() {
        HostelService.saveStudents(students);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "HostelPro", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "HostelPro", JOptionPane.ERROR_MESSAGE);
    }

    // LOCAL DATA SERVICE
    static class HostelService {
        private static final String FILE_NAME = "hostel_data.dat";

        static List<Student> loadStudents() {
            File file = new File(FILE_NAME);

            if (!file.exists()) return new ArrayList<>();

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Object data = in.readObject();

                if (data instanceof List<?>) {
                    List<Student> result = new ArrayList<>();
                    for (Object item : (List<?>) data) {
                        if (item instanceof Student) result.add((Student) item);
                    }
                    return result;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Could not load saved student data.\n" + e.getMessage(),
                    "Data Loading Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            return new ArrayList<>();
        }

        static void saveStudents(List<Student> students) {
            try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
                out.writeObject(new ArrayList<>(students));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Could not save student data.\n" + e.getMessage(),
                    "Data Saving Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}

// KEEP THIS STUDENT CLASS OUTSIDE MAIN.
// Its serialVersionUID preserves compatibility with your existing saved data.
class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    String name, roll, phone, course, room;
    double fee;
    boolean paid;

    Student(String name, String roll, String phone,
            String course, String room, double fee) {
        this.name = name;
        this.roll = roll;
        this.phone = phone;
        this.course = course;
        this.room = room;
        this.fee = fee;
        this.paid = false;
    }
}
