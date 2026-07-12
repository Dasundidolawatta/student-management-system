package aiassisted;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String studentId;
    private final String studentName;
    private final String email;
    private final ArrayList<Course> courses;

    public Student(String studentId, String studentName, String email) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.courses = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public boolean hasCourse(String courseId) {

        for (Course course : courses) {
            if (course.getCourseId().equalsIgnoreCase(courseId)) {
                return true;
            }
        }

        return false;
    }
}

abstract class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String courseId;
    private final String courseName;
    private final double courseFee;

    public Course(String courseId, String courseName, double courseFee) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseFee = courseFee;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getCourseFee() {
        return courseFee;
    }

    public abstract double calculateFee();

    public abstract String getCourseType();

    public abstract String getExtraDetails();
}

class OnlineCourse extends Course {

    private static final long serialVersionUID = 1L;

    private final int videoHours;

    public OnlineCourse(String courseId, String courseName,
                        double courseFee, int videoHours) {

        super(courseId, courseName, courseFee);
        this.videoHours = videoHours;
    }

    @Override
    public double calculateFee() {
        return getCourseFee();
    }

    @Override
    public String getCourseType() {
        return "Online Course";
    }

    @Override
    public String getExtraDetails() {
        return "Video Hours: " + videoHours;
    }
}

class PhysicalCourse extends Course {

    private static final long serialVersionUID = 1L;

    private static final double SESSION_CHARGE = 100.0;

    private final int classroomSessions;

    public PhysicalCourse(String courseId, String courseName,
                          double courseFee, int classroomSessions) {

        super(courseId, courseName, courseFee);
        this.classroomSessions = classroomSessions;
    }

    @Override
    public double calculateFee() {
        return getCourseFee()
                + classroomSessions * SESSION_CHARGE;
    }

    @Override
    public String getCourseType() {
        return "Physical Course";
    }

    @Override
    public String getExtraDetails() {
        return "Classroom Sessions: " + classroomSessions;
    }
}

public class StudentManagementSystem {

    private static final String FILE_NAME = "students.dat";

    private final Scanner scanner;
    private ArrayList<Student> students;

    public StudentManagementSystem() {
        scanner = new Scanner(System.in);
        students = loadStudents();
    }

    public static void main(String[] args) {

        StudentManagementSystem system =
                new StudentManagementSystem();

        system.run();
    }

    private void run() {

        System.out.println(
                "Student records loaded: " + students.size()
        );

        boolean running = true;

        while (running) {

            showMenu();

            int choice = readIntInRange(
                    "Enter choice: ",
                    1,
                    5
            );

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    addCourseToStudent();
                    break;

                case 3:
                    displayAllStudents();
                    break;

                case 4:
                    searchStudent();
                    break;

                case 5:
                    if (saveStudents()) {
                        System.out.println(
                                "Data saved successfully."
                        );
                    } else {
                        System.out.println(
                                "Warning: Data could not be saved."
                        );
                    }

                    running = false;
                    System.out.println("Program closed.");
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }

    private void showMenu() {

        System.out.println("\n================================");
        System.out.println("Student Management System");
        System.out.println("================================");
        System.out.println("1. Add Student");
        System.out.println("2. Add Course to Student");
        System.out.println("3. Display All Students");
        System.out.println("4. Search Student by ID");
        System.out.println("5. Exit");
    }

    private void addStudent() {

        System.out.println("\n--- Add Student ---");

        String studentId =
                readRequiredText("Enter Student ID: ");

        if (findStudent(studentId) != null) {
            System.out.println(
                    "A student with this ID already exists."
            );
            return;
        }

        String studentName =
                readRequiredText("Enter Student Name: ");

        String email = readEmail();

        Student student = new Student(
                studentId,
                studentName,
                email
        );

        students.add(student);

        if (saveStudents()) {
            System.out.println(
                    "Student added and saved successfully."
            );
        } else {
            System.out.println(
                    "Student added to memory, but it could not be saved."
            );
        }
    }

    private void addCourseToStudent() {

        System.out.println("\n--- Add Course to Student ---");

        String studentId =
                readRequiredText("Enter Student ID: ");

        Student student = findStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        String courseId =
                readRequiredText("Enter Course ID: ");

        if (student.hasCourse(courseId)) {
            System.out.println(
                    "This student is already registered for that course."
            );
            return;
        }

        String courseName =
                readRequiredText("Enter Course Name: ");

        double courseFee =
                readNonNegativeDouble("Enter Course Fee: ");

        System.out.println("\nSelect Course Type");
        System.out.println("1. Online Course");
        System.out.println("2. Physical Course");

        int courseType = readIntInRange(
                "Enter choice: ",
                1,
                2
        );

        Course course;

        if (courseType == 1) {

            int videoHours = readNonNegativeInt(
                    "Enter Number of Video Hours: "
            );

            course = new OnlineCourse(
                    courseId,
                    courseName,
                    courseFee,
                    videoHours
            );

        } else {

            int classroomSessions = readNonNegativeInt(
                    "Enter Number of Classroom Sessions: "
            );

            course = new PhysicalCourse(
                    courseId,
                    courseName,
                    courseFee,
                    classroomSessions
            );
        }

        student.addCourse(course);

        if (saveStudents()) {
            System.out.println(
                    "Course added and saved successfully."
            );
        } else {
            System.out.println(
                    "Course added to memory, but it could not be saved."
            );
        }
    }

    private void displayAllStudents() {

        System.out.println("\n--- All Students ---");

        if (students.isEmpty()) {
            System.out.println(
                    "No students are registered."
            );
            return;
        }

        for (Student student : students) {
            displayStudent(student);
        }
    }

    private void searchStudent() {

        System.out.println("\n--- Search Student ---");

        String studentId =
                readRequiredText("Enter Student ID: ");

        Student student = findStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            displayStudent(student);
        }
    }

    private Student findStudent(String studentId) {

        for (Student student : students) {

            if (student.getStudentId()
                    .equalsIgnoreCase(studentId)) {

                return student;
            }
        }

        return null;
    }

    private void displayStudent(Student student) {

        System.out.println("\n--------------------------------");
        System.out.println(
                "Student ID: " + student.getStudentId()
        );
        System.out.println(
                "Student Name: " + student.getStudentName()
        );
        System.out.println(
                "Email: " + student.getEmail()
        );

        if (student.getCourses().isEmpty()) {
            System.out.println(
                    "Courses: No courses registered"
            );
            return;
        }

        System.out.println("Registered Courses:");

        int number = 1;
        double totalStudentFee = 0;

        for (Course course : student.getCourses()) {

            double totalCourseFee = course.calculateFee();
            totalStudentFee += totalCourseFee;

            System.out.println("\n  Course " + number);
            System.out.println(
                    "  Course ID: " + course.getCourseId()
            );
            System.out.println(
                    "  Course Name: " + course.getCourseName()
            );
            System.out.println(
                    "  Course Type: " + course.getCourseType()
            );
            System.out.printf(
                    "  Base Fee: %.2f%n",
                    course.getCourseFee()
            );
            System.out.println(
                    "  " + course.getExtraDetails()
            );
            System.out.printf(
                    "  Total Course Fee: %.2f%n",
                    totalCourseFee
            );

            number++;
        }

        System.out.printf(
                "%nTotal Fee for Student: %.2f%n",
                totalStudentFee
        );
    }

    private String readRequiredText(String message) {

        while (true) {

            System.out.print(message);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println(
                    "Input cannot be empty. Please try again."
            );
        }
    }

    private String readEmail() {

        while (true) {

            String email =
                    readRequiredText("Enter Email: ");

            if (email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            )) {
                return email;
            }

            System.out.println(
                    "Invalid email address. Please try again."
            );
        }
    }

    private int readIntInRange(String message,
                               int minimum,
                               int maximum) {

        while (true) {

            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {

                int value = Integer.parseInt(input);

                if (value >= minimum && value <= maximum) {
                    return value;
                }

                System.out.println(
                        "Enter a number from "
                                + minimum
                                + " to "
                                + maximum
                                + "."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a whole number."
                );
            }
        }
    }

    private int readNonNegativeInt(String message) {

        while (true) {

            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {

                int value = Integer.parseInt(input);

                if (value >= 0) {
                    return value;
                }

                System.out.println(
                        "Value cannot be negative."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a whole number."
                );
            }
        }
    }

    private double readNonNegativeDouble(String message) {

        while (true) {

            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {

                double value = Double.parseDouble(input);

                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }

                System.out.println(
                        "Enter a finite, non-negative value."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a numerical value."
                );
            }
        }
    }

    private boolean saveStudents() {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME)
                     )) {

            output.writeObject(students);
            return true;

        } catch (IOException e) {

            System.out.println(
                    "File error: Student records could not be saved."
            );

            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Student> loadStudents() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println(
                    "No saved data found. Starting a new system."
            );

            return new ArrayList<>();
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(file)
                     )) {

            Object savedData = input.readObject();

            if (savedData instanceof ArrayList<?>) {
                return (ArrayList<Student>) savedData;
            }

            System.out.println(
                    "The data file contains invalid information."
            );

        } catch (IOException
                 | ClassNotFoundException
                 | ClassCastException e) {

            System.out.println(
                    "Existing student records could not be loaded."
            );
        }

        System.out.println(
                "The system will continue with an empty student list."
        );

        return new ArrayList<>();
    }
}