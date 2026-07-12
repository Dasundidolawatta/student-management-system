import java.util.InputMismatchException;
import java.util.Scanner;

class Student {

    private String studentId;
    private String studentName;
    private String email;
    private Course[] registeredCourses;

    public Student(String studentId, String studentName,
                   String email, Course[] registeredCourses) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.registeredCourses = registeredCourses;
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

    public Course[] getRegisteredCourses() {
        return registeredCourses;
    }

    public double calculateTotalFee() {

        double totalFee = 0;

        for (Course course : registeredCourses) {
            totalFee += course.calculateFee();
        }

        return totalFee;
    }
}

abstract class Course {

    private String courseId;
    private String courseName;
    private double courseFee;

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

    abstract double calculateFee();
}

class OnlineCourse extends Course {

    private int videoHours;

    public OnlineCourse(String courseId, String courseName,
                        double courseFee, int videoHours) {

        super(courseId, courseName, courseFee);
        this.videoHours = videoHours;
    }

    public int getVideoHours() {
        return videoHours;
    }

    @Override
    double calculateFee() {
        return getCourseFee();
    }
}

class PhysicalCourse extends Course {

    private int classroomSessions;

    public PhysicalCourse(String courseId, String courseName,
                          double courseFee, int classroomSessions) {

        super(courseId, courseName, courseFee);
        this.classroomSessions = classroomSessions;
    }

    public int getClassroomSessions() {
        return classroomSessions;
    }

    @Override
    double calculateFee() {
        return getCourseFee() + (classroomSessions * 100);
    }
}

public class StudentManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter number of students: ");
            int numberOfStudents = sc.nextInt();
            sc.nextLine();

            if (numberOfStudents <= 0) {
                throw new IllegalArgumentException(
                        "Number of students must be greater than zero."
                );
            }

            Student[] students = new Student[numberOfStudents];

            for (int i = 0; i < numberOfStudents; i++) {

                System.out.println("\n--------------------------------");
                System.out.println("Registering Student " + (i + 1));
                System.out.println("--------------------------------");

                System.out.print("Enter Student ID: ");
                String studentId = sc.nextLine();

                System.out.print("Enter Student Name: ");
                String studentName = sc.nextLine();

                System.out.print("Enter Email: ");
                String email = sc.nextLine();

                System.out.print("Enter Number of Courses: ");
                int numberOfCourses = sc.nextInt();
                sc.nextLine();

                if (numberOfCourses <= 0) {
                    throw new IllegalArgumentException(
                            "Number of courses must be greater than zero."
                    );
                }

                Course[] courses = new Course[numberOfCourses];

                for (int j = 0; j < numberOfCourses; j++) {

                    System.out.println("\nRegistering Course " + (j + 1));
                    System.out.println("--------------------------------");

                    System.out.println("Select Course Type");
                    System.out.println("1. Online Course");
                    System.out.println("2. Physical Course");
                    System.out.print("Enter Choice: ");

                    int choice = sc.nextInt();
                    sc.nextLine();

                    if (choice != 1 && choice != 2) {
                        throw new IllegalArgumentException(
                                "Course choice must be 1 or 2."
                        );
                    }

                    System.out.print("Enter Course ID: ");
                    String courseId = sc.nextLine();

                    System.out.print("Enter Course Name: ");
                    String courseName = sc.nextLine();

                    System.out.print("Enter Course Fee: ");
                    double courseFee = sc.nextDouble();

                    if (courseFee < 0) {
                        throw new IllegalArgumentException(
                                "Course fee cannot be negative."
                        );
                    }

                    if (choice == 1) {

                        System.out.print("Enter Number of Video Hours: ");
                        int videoHours = sc.nextInt();

                        if (videoHours < 0) {
                            throw new IllegalArgumentException(
                                    "Video hours cannot be negative."
                            );
                        }

                        courses[j] = new OnlineCourse(
                                courseId,
                                courseName,
                                courseFee,
                                videoHours
                        );

                    } else {

                        System.out.print(
                                "Enter Number of Classroom Sessions: "
                        );

                        int classroomSessions = sc.nextInt();

                        if (classroomSessions < 0) {
                            throw new IllegalArgumentException(
                                    "Classroom sessions cannot be negative."
                            );
                        }

                        courses[j] = new PhysicalCourse(
                                courseId,
                                courseName,
                                courseFee,
                                classroomSessions
                        );
                    }

                    sc.nextLine();
                }

                students[i] = new Student(
                        studentId,
                        studentName,
                        email,
                        courses
                );
            }

            System.out.println("\n================================");
            System.out.println("All Registration Summaries");
            System.out.println("================================");

            for (int i = 0; i < students.length; i++) {

                Student student = students[i];

                System.out.println("\nStudent " + (i + 1));
                System.out.println("--------------------------------");
                System.out.println(
                        "Student ID: " + student.getStudentId()
                );
                System.out.println(
                        "Student Name: " + student.getStudentName()
                );
                System.out.println(
                        "Email: " + student.getEmail()
                );

                Course[] courses = student.getRegisteredCourses();

                for (int j = 0; j < courses.length; j++) {

                    Course course = courses[j];

                    System.out.println("\nCourse " + (j + 1));
                    System.out.println("--------------------------------");
                    System.out.println(
                            "Course ID: " + course.getCourseId()
                    );
                    System.out.println(
                            "Course Name: " + course.getCourseName()
                    );

                    if (course instanceof OnlineCourse) {

                        OnlineCourse onlineCourse =
                                (OnlineCourse) course;

                        System.out.println(
                                "Course Type: Online Course"
                        );

                        System.out.println(
                                "Video Hours: "
                                        + onlineCourse.getVideoHours()
                        );

                    } else {

                        PhysicalCourse physicalCourse =
                                (PhysicalCourse) course;

                        System.out.println(
                                "Course Type: Physical Course"
                        );

                        System.out.println(
                                "Classroom Sessions: "
                                        + physicalCourse
                                        .getClassroomSessions()
                        );
                    }

                    System.out.println(
                            "Course Fee: " + course.calculateFee()
                    );
                }

                System.out.println(
                        "\nTotal Fee for All Courses: "
                                + student.calculateTotalFee()
                );
            }

        } catch (InputMismatchException e) {

            System.out.println(
                    "Invalid input. Please enter a valid numerical value."
            );

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());

        } finally {

            sc.close();
        }
    }
}