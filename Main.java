
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    private final HostelService service = new HostelService();
    private DefaultTableModel model;
    private JTable table;
    private JPanel dashboard;
    private JLabel totalStudents, totalRooms, occupiedRooms, revenue;
    private JTextField searchField;

    private final Color NAVY = new Color(20, 28, 52);
    private final Color BLUE = new Color(67, 97, 238);
    private final Color BG = new Color(243, 246, 252);
    private final Color WHITE = Color.WHITE;

    public Main() {
        setTitle("HostelPro Premium | Management System");
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainArea(), BorderLayout.CENTER);

        refreshData();
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setBackground(NAVY);
        side.setPreferredSize(new Dimension(225, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(25, 16, 20, 16));

        JLabel logo = new JLabel("  HOSTELPRO");
        logo.setForeground(WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 23));

        JLabel subtitle = new JLabel("  PREMIUM MANAGEMENT");
        subtitle.setForeground(new Color(160, 175, 210));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        side.add(logo);
        side.add(Box.createVerticalStrut(5));
        side.add(subtitle);
        side.add(Box.createVerticalStrut(55));

        side.add(menuLabel("MAIN MENU"));
        side.add(Box.createVerticalStrut(15));
        side.add(menuButton("Dashboard", "dashboard"));
        side.add(menuButton("Student Management", "students"));
        side.add(menuButton("Room Management", "rooms"));
        side.add(menuButton("Fee Management", "fees"));

        side.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("HostelPro Premium v1.0");
        footer.setForeground(new Color(160, 175, 210));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        side.add(footer);

        return side;
    }

    private JLabel menuLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(130, 150, 190));
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return label;
    }

    private JButton menuButton(String text, String page) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(195, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(NAVY);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 12, 10, 5));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> showPage(page));
        return button;
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new EmptyBorder(17, 28, 17, 28));

        JLabel heading = new JLabel("Hostel Administration");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(NAVY);

        JLabel admin = new JLabel("ADMINISTRATOR");
        admin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        admin.setForeground(BLUE);

        header.add(heading, BorderLayout.WEST);
        header.add(admin, BorderLayout.EAST);

        dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(BG);
        dashboard.setBorder(new EmptyBorder(25, 25, 25, 25));

        main.add(header, BorderLayout.NORTH);
        main.add(dashboard, BorderLayout.CENTER);

        showPage("dashboard");
        return main;
    }

    private void showPage(String page) {
        dashboard.removeAll();

        if (page.equals("dashboard")) {
            showDashboard();
        } else if (page.equals("students")) {
            showStudents();
        } else if (page.equals("rooms")) {
            showRooms();
        } else if (page.equals("fees")) {
            showFees();
        }

        dashboard.revalidate();
        dashboard.repaint();
    }

    private JPanel createCard(String title, JLabel value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 234, 242)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel name = new JLabel(title);
        name.setForeground(new Color(110, 120, 140));
        name.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        value.setForeground(color);
        value.setFont(new Font("Segoe UI", Font.BOLD, 29));

        card.add(name);
        card.add(Box.createVerticalStrut(15));
        card.add(value);

        return card;
    }

    private void showDashboard() {
        JPanel content = new JPanel(new BorderLayout(0, 25));
        content.setBackground(BG);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(NAVY);

        JLabel welcome = new JLabel("Welcome to HostelPro Premium");
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        welcome.setForeground(Color.GRAY);

        JPanel titles = new JPanel();
        titles.setBackground(BG);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(title);
        titles.add(Box.createVerticalStrut(7));
        titles.add(welcome);

        JButton add = new JButton("+ Add Student");
        styleButton(add);
        add.addActionListener(e -> addStudent());

        top.add(titles, BorderLayout.WEST);
        top.add(add, BorderLayout.EAST);

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
        cards.setBackground(BG);

        totalStudents = new JLabel("0");
        totalRooms = new JLabel("0");
        occupiedRooms = new JLabel("0");
        revenue = new JLabel("₹0");

        cards.add(createCard("TOTAL STUDENTS", totalStudents, BLUE));
        cards.add(createCard("TOTAL ROOMS", totalRooms, new Color(20, 160, 130)));
        cards.add(createCard("OCCUPIED ROOMS", occupiedRooms, new Color(220, 145, 30)));
        cards.add(createCard("FEES COLLECTED", revenue, new Color(130, 85, 200)));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(WHITE);
        bottom.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel recent = new JLabel("Recent Student Records");
        recent.setFont(new Font("Segoe UI", Font.BOLD, 17));
        recent.setForeground(NAVY);

        JTable recentTable = new JTable();
        DefaultTableModel recentModel = new DefaultTableModel(
                new String[]{"Student Name", "Roll Number", "Room", "Course", "Fee Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Student s : service.getStudents()) {
            recentModel.addRow(new Object[]{
                    s.name, s.roll, s.room, s.course,
                    s.paid ? "Paid" : "Pending"
            });
        }

        recentTable.setModel(recentModel);
        styleTable(recentTable);

        bottom.add(recent, BorderLayout.NORTH);
        bottom.add(new JScrollPane(recentTable), BorderLayout.CENTER);

        content.add(top, BorderLayout.NORTH);
        content.add(cards, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        dashboard.add(content, BorderLayout.CENTER);
        updateStatistics();
    }

    private void showStudents() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BG);

        JLabel title = new JLabel("Student Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(NAVY);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);
        top.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(BG);

        searchField = new JTextField(15);
        JButton search = new JButton("Search");
        JButton add = new JButton("+ Add Student");
        JButton delete = new JButton("Delete Selected");

        styleButton(search);
        styleButton(add);
        styleButton(delete);

        search.addActionListener(e -> refreshTable(searchField.getText()));
        add.addActionListener(e -> addStudent());
        delete.addActionListener(e -> deleteStudent());

        actions.add(searchField);
        actions.add(search);
        actions.add(add);
        actions.add(delete);

        top.add(actions, BorderLayout.EAST);

        model = new DefaultTableModel(
                new String[]{"Name", "Roll Number", "Phone", "Course", "Room", "Fee Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        styleTable(table);
        refreshTable("");

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        dashboard.add(panel, BorderLayout.CENTER);
    }

    private void showRooms() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BG);

        JLabel title = new JLabel("Room Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(NAVY);

        JPanel cards = new JPanel(new GridLayout(0, 3, 18, 18));
        cards.setBackground(BG);

        for (int i = 101; i <= 112; i++) {
            final int roomNumber = i;
            int count = service.countRoom(String.valueOf(i));
            String status = count == 0 ? "Available" : "Occupied";

            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(WHITE);
            card.setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel room = new JLabel("ROOM " + i);
            room.setFont(new Font("Segoe UI", Font.BOLD, 20));
            room.setForeground(NAVY);

            JLabel occupied = new JLabel(count + " student(s)");
            occupied.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JLabel state = new JLabel(status);
            state.setForeground(count == 0 ? new Color(20, 160, 100) : BLUE);

            card.add(room);
            card.add(Box.createVerticalStrut(12));
            card.add(occupied);
            card.add(Box.createVerticalStrut(10));
            card.add(state);

            cards.add(card);
        }

        JLabel note = new JLabel("12 standard rooms | Room numbers 101–112");
        note.setForeground(Color.GRAY);

        panel.add(title, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);
        panel.add(note, BorderLayout.SOUTH);

        dashboard.add(panel, BorderLayout.CENTER);
    }

    private void showFees() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BG);

        JLabel title = new JLabel("Fee Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(NAVY);

        JTable feeTable = new JTable();
        DefaultTableModel feeModel = new DefaultTableModel(
                new String[]{"Student", "Roll Number", "Fee Amount", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Student s : service.getStudents()) {
            feeModel.addRow(new Object[]{
                    s.name, s.roll, "₹" + s.fee,
                    s.paid ? "Paid" : "Pending"
            });
        }

        feeTable.setModel(feeModel);
        styleTable(feeTable);

        JButton payment = new JButton("Mark Selected Student as Paid");
        styleButton(payment);

        payment.addActionListener(e -> {
            int row = feeTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a student first.");
                return;
            }

            String roll = String.valueOf(feeModel.getValueAt(row, 1));
            service.markPaid(roll);
            showPage("fees");
            updateStatistics();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(BG);
        bottom.add(payment);

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(feeTable), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        dashboard.add(panel, BorderLayout.CENTER);
    }

    private void addStudent() {
        JTextField name = new JTextField(18);
        JTextField roll = new JTextField(18);
        JTextField phone = new JTextField(18);
        JTextField course = new JTextField(18);
        JComboBox<String> room = new JComboBox<>();

        for (int i = 101; i <= 112; i++) {
            room.addItem(String.valueOf(i));
        }

        JTextField fee = new JTextField("50000", 18);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));
        form.add(new JLabel("Student Name:"));
        form.add(name);
        form.add(new JLabel("Roll Number:"));
        form.add(roll);
        form.add(new JLabel("Phone Number:"));
        form.add(phone);
        form.add(new JLabel("Course:"));
        form.add(course);
        form.add(new JLabel("Room Number:"));
        form.add(room);
        form.add(new JLabel("Hostel Fee (₹):"));
        form.add(fee);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Register New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            String studentName = name.getText().trim();
            String studentRoll = roll.getText().trim();
            String studentPhone = phone.getText().trim();
            String studentCourse = course.getText().trim();
            String studentRoom = String.valueOf(room.getSelectedItem());
            double amount = Double.parseDouble(fee.getText().trim());

            if (studentName.isEmpty() || studentRoll.isEmpty()
                    || studentPhone.isEmpty() || studentCourse.isEmpty()
                    || amount <= 0) {
                throw new IllegalArgumentException("All fields are required.");
            }

            if (!studentPhone.matches("[0-9]{10}")) {
                throw new IllegalArgumentException("Enter a valid 10-digit phone number.");
            }

            if (service.exists(studentRoll)) {
                throw new IllegalArgumentException("Roll number already exists.");
            }

            if (service.countRoom(studentRoom) >= 4) {
                throw new IllegalArgumentException("This room is full.");
            }

            service.addStudent(new Student(
                    studentName, studentRoll, studentPhone,
                    studentCourse, studentRoom, amount));

            JOptionPane.showMessageDialog(this, "Student registered successfully!");
            showPage("students");
            updateStatistics();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid fee amount.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }

        String roll = String.valueOf(model.getValueAt(row, 1));

        int choice = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to delete this student?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            service.deleteStudent(roll);
            refreshTable("");
            updateStatistics();
        }
    }

    private void refreshTable(String search) {
        if (model == null) return;

        model.setRowCount(0);

        for (Student s : service.getStudents()) {
            String combined = (s.name + " " + s.roll + " " + s.course)
                    .toLowerCase();

            if (combined.contains(search.toLowerCase())) {
                model.addRow(new Object[]{
                        s.name, s.roll, s.phone, s.course,
                        s.room, s.paid ? "Paid" : "Pending"
                });
            }
        }
    }

    private void refreshData() {
        updateStatistics();
    }

    private void updateStatistics() {
        if (totalStudents == null) return;

        totalStudents.setText(String.valueOf(service.getStudents().size()));
        totalRooms.setText("12");

        int occupied = 0;
        double total = 0;

        for (int i = 101; i <= 112; i++) {
            if (service.countRoom(String.valueOf(i)) > 0) occupied++;
        }

        for (Student s : service.getStudents()) {
            if (s.paid) total += s.fee;
        }

        occupiedRooms.setText(String.valueOf(occupied));
        revenue.setText("₹" + String.format("%.0f", total));
    }

    private void styleButton(JButton button) {
        button.setBackground(BLUE);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(WHITE);
        table.setSelectionBackground(new Color(220, 230, 255));
        table.setSelectionForeground(NAVY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new Main().setVisible(true);
        });
    }
}

// ---------------- BACKEND: STUDENT MODEL ----------------

class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    String roll;
    String phone;
    String course;
    String room;
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

// ---------------- BACKEND: HOSTEL SERVICE ----------------

class HostelService {

    private final List<Student> students = new ArrayList<>();
    private final File dataFile = new File("hostel_data.dat");

    HostelService() {
        loadData();
    }

    List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    boolean exists(String roll) {
        for (Student s : students) {
            if (s.roll.equalsIgnoreCase(roll)) return true;
        }
        return false;
    }

    int countRoom(String room) {
        int count = 0;
        for (Student s : students) {
            if (s.room.equals(room)) count++;
        }
        return count;
    }

    void addStudent(Student student) {
        students.add(student);
        saveData();
    }

    void deleteStudent(String roll) {
        students.removeIf(s -> s.roll.equalsIgnoreCase(roll));
        saveData();
    }

    void markPaid(String roll) {
        for (Student s : students) {
            if (s.roll.equalsIgnoreCase(roll)) {
                s.paid = true;
                break;
            }
        }
        saveData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        if (!dataFile.exists()) return;

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(dataFile))) {
            students.addAll((List<Student>) in.readObject());
        } catch (Exception e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }

    private void saveData() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(students);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to save data: " + e.getMessage());
        }
    }
}
