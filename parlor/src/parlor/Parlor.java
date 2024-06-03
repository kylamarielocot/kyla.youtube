package parlor;

import java.sql.*;
import java.util.Scanner;

public class Parlor {

    private static final String URL = "jdbc:mysql://localhost:3306/parloursystem";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Parlor System");
                System.out.println("1. View Services");
                System.out.println("2. Book Appointment");
                System.out.println("3. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        viewServices(conn);
                        break;
                    case 2:
                        bookAppointment(conn, scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewServices(Connection conn) {
        String query = "SELECT * FROM Services";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String serviceName = rs.getString("serviceName");
                double price = rs.getDouble("price");
                int duration = rs.getInt("duration");
                System.out.println("ID: " + id + ", Service: " + serviceName + ", Price: " + price + ", Duration: " + duration + " mins");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void bookAppointment(Connection conn, Scanner scanner) {
        try {
            System.out.println("Enter your name:");
            String customerName = scanner.nextLine();

            System.out.println("Enter service ID:");
            int serviceId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter appointment year (YYYY):");
            String year = scanner.nextLine();

            System.out.println("Enter appointment month (MM):");
            String month = scanner.nextLine();

            System.out.println("Enter appointment day (DD):");
            String day = scanner.nextLine();

            System.out.println("Enter appointment hour (HH):");
            String hour = scanner.nextLine();

            System.out.println("Enter appointment minute (MM):");
            String minute = scanner.nextLine();

            System.out.println("Enter appointment second (SS):");
            String second = scanner.nextLine();

            String appointmentDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
            try {
                Timestamp timestamp = Timestamp.valueOf(appointmentDate);

                String query = "INSERT INTO Appointments (customerName, serviceId, appointmentDate) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, customerName);
                    pstmt.setInt(2, serviceId);
                    pstmt.setTimestamp(3, timestamp);
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Appointment booked successfully");
                    } else {
                        System.out.println("Failed to book appointment");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid date format. Please use the format YYYY-MM-DD HH:MM:SS.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
