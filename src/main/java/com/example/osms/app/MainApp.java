package com.example.osms.app;

import java.util.List;
import java.util.Scanner;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.osms.config.AppConfig;
import com.example.osms.model.Course;
import com.example.osms.model.Student;
import com.example.osms.service.FeeService;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        FeeService feeService = ctx.getBean(FeeService.class);
        SessionFactory sf = ctx.getBean(SessionFactory.class);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Online Student Management System =====");
            System.out.println("1. Add Course");
            System.out.println("2. Add Student");
            System.out.println("3. View All Students");
            System.out.println("4. View Student by ID");
            System.out.println("5. Update Student");
            System.out.println("6. Delete Student");
            System.out.println("7. Make Payment");
            System.out.println("8. Refund Payment");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter course name: ");
                        String cname = sc.nextLine();
                        System.out.print("Enter duration: ");
                        String duration = sc.nextLine();

                        var session = sf.getCurrentSession();
                        session.beginTransaction();
                        Course c = new Course(cname, duration);
                        session.save(c);
                        session.getTransaction().commit();

                        System.out.println("✅ Course added with ID: " + c.getCourseId());
                    }

                    case 2 -> {
                        System.out.print("Enter student name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter course ID (or leave blank for none): ");
                        String cidStr = sc.nextLine();
                        Integer cid = cidStr.isBlank() ? null : Integer.parseInt(cidStr);
                        System.out.print("Enter initial balance: ");
                        double balance = Double.parseDouble(sc.nextLine());

                        sf.getCurrentSession().beginTransaction();
                        Course course = null;
                        if (cid != null)
                            course = sf.getCurrentSession().get(Course.class, cid);
                        Student s = new Student(name, course, balance);
                        Integer id = feeService.addStudent(s);
                        sf.getCurrentSession().getTransaction().commit();

                        System.out.println("✅ Student added with ID: " + id);
                    }

                    case 3 -> {
                        sf.getCurrentSession().beginTransaction();
                        List<Student> students = feeService.getAllStudents();
                        sf.getCurrentSession().getTransaction().commit();
                        students.forEach(System.out::println);
                    }

                    case 4 -> {
                        System.out.print("Enter student ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        sf.getCurrentSession().beginTransaction();
                        Student s = feeService.getStudent(id);
                        sf.getCurrentSession().getTransaction().commit();

                        System.out.println(s == null ? "❌ Student not found" : s);
                    }

                    case 5 -> {
                        System.out.print("Enter student ID to update: ");
                        int id = Integer.parseInt(sc.nextLine());
                        sf.getCurrentSession().beginTransaction();
                        Student s = feeService.getStudent(id);
                        if (s == null) {
                            sf.getCurrentSession().getTransaction().commit();
                            System.out.println("❌ Student not found");
                            break;
                        }

                        System.out.print("Enter new name (press Enter to keep same): ");
                        String newName = sc.nextLine();
                        if (!newName.isBlank())
                            s.setName(newName);

                        System.out.print("Enter new balance (press Enter to keep same): ");
                        String newBal = sc.nextLine();
                        if (!newBal.isBlank())
                            s.setBalance(Double.parseDouble(newBal));

                        feeService.updateStudent(s);
                        sf.getCurrentSession().getTransaction().commit();
                        System.out.println("✅ Student updated!");
                    }

                    case 6 -> {
                        System.out.print("Enter student ID to delete: ");
                        int id = Integer.parseInt(sc.nextLine());
                        sf.getCurrentSession().beginTransaction();
                        feeService.deleteStudent(id);
                        sf.getCurrentSession().getTransaction().commit();
                        System.out.println("🗑️ Student deleted (if existed)");
                    }

                    case 7 -> {
                        System.out.print("Enter student ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter payment amount: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        feeService.makePayment(id, amt);
                        System.out.println("💰 Payment successful!");
                    }

                    case 8 -> {
                        System.out.print("Enter student ID: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter refund amount: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        feeService.refundPayment(id, amt);
                        System.out.println("↩️ Refund successful!");
                    }

                    case 9 -> {
                        System.out.println("👋 Exiting...");
                        sc.close();
                        ctx.close();
                        return;
                    }

                    default -> System.out.println("⚠️ Invalid choice, please try again!");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
