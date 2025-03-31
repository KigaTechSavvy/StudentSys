import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class StudentManagementSystem extends JFrame {

    // Data structures to store student and course information
    private DefaultListModel<String> studentListModel = new DefaultListModel<>();
    private Map<String, String> studentDetails = new HashMap<>(); // Student ID -> Name
    private Map<String, DefaultListModel<String>> studentCourses = new HashMap<>(); // Student ID -> Courses
    private Map<String, String> studentGrades = new HashMap<>(); // Student ID + Course -> Grade

    // GUI Components
    private JList<String> studentList;
    private JTextArea displayArea;
    private JComboBox<String> courseComboBox;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu studentMenu = new JMenu("Student");
        JMenuItem addStudentItem = new JMenuItem("Add Student");
        JMenuItem updateStudentItem = new JMenuItem("Update Student");
        JMenuItem viewStudentItem = new JMenuItem("View Student Details");
        studentMenu.add(addStudentItem);
        studentMenu.add(updateStudentItem);
        studentMenu.add(viewStudentItem);
        menuBar.add(studentMenu);

        JMenu courseMenu = new JMenu("Course");
        JMenuItem enrollStudentItem = new JMenuItem("Enroll Student");
        courseMenu.add(enrollStudentItem);
        menuBar.add(courseMenu);

        JMenu gradeMenu = new JMenu("Grade");
        JMenuItem assignGradeItem = new JMenuItem("Assign Grade");
        gradeMenu.add(assignGradeItem);
        menuBar.add(gradeMenu);

        setJMenuBar(menuBar);

        // Center: Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Left: Student list
        studentList = new JList<>(studentListModel);
        JScrollPane studentScrollPane = new JScrollPane(studentList);
        studentScrollPane.setPreferredSize(new Dimension(150, 0));
        add(studentScrollPane, BorderLayout.WEST);

        // Add Student
        addStudentItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter Student ID:");
                String name = JOptionPane.showInputDialog("Enter Student Name:");
                if (id != null && name != null && !id.isEmpty() && !name.isEmpty()) {
                    studentListModel.addElement(id + " - " + name);
                    studentDetails.put(id, name);
                    studentCourses.put(id, new DefaultListModel<>());
                    displayArea.setText("Student Added: " + id + " - " + name);
                } else {
                    displayArea.setText("Error: ID and Name cannot be empty.");
                }
            }
        });

        // View Student Details
        viewStudentItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder details = new StringBuilder("Student Details:\n");
                for (int i = 0; i < studentListModel.size(); i++) {
                    details.append(studentListModel.get(i)).append("\n");
                }
                displayArea.setText(details.toString());
            }
        });

        // Enroll Student
        enrollStudentItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student = studentList.getSelectedValue();
                if (student == null) {
                    displayArea.setText("Error: Please select a student.");
                    return;
                }

                String[] courses = {"Math", "Science", "History"};
                String course = (String) JOptionPane.showInputDialog(
                        null,
                        "Select Course:",
                        "Enroll Student",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        courses,
                        courses[0]);

                if (course != null) {
                    String studentId = student.split(" - ")[0];
                    studentCourses.get(studentId).addElement(course);
                    displayArea.setText("Enrolled " + student + " in " + course);
                }
            }
        });

        // Assign Grade
        assignGradeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student = studentList.getSelectedValue();
                if (student == null) {
                    displayArea.setText("Error: Please select a student.");
                    return;
                }

                String studentId = student.split(" - ")[0];
                DefaultListModel<String> courses = studentCourses.get(studentId);

                if (courses.size() == 0) {
                    displayArea.setText("Error: Student is not enrolled in any courses.");
                    return;
                }

                String course = (String) JOptionPane.showInputDialog(
                        null,
                        "Select Course:",
                        "Assign Grade",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        courses.toArray(),
                        courses.get(0));

                if (course != null) {
                    String grade = JOptionPane.showInputDialog("Enter Grade:");
                    if (grade != null && !grade.isEmpty()) {
                        studentGrades.put(studentId + " - " + course, grade);
                        displayArea.setText("Assigned grade " + grade + " to " + student + " in " + course);
                    } else {
                        displayArea.setText("Error: Grade cannot be empty.");
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentManagementSystem().setVisible(true);
            }
        });
    }
}