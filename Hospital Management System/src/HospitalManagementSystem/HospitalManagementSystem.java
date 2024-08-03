package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Mypassword";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection,scanner);
            Docters docter = new Docters(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. view Docters");
                System.out.println("4. Book Appoinment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice : ");
                int choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        docter.viewDocters();
                        System.out.println();
                        break;
                    case 4:
                        bookAppoinment(patient,docter,connection,scanner);
                        System.out.println();
                        break;

                    case 5:
                        return;
                    default :
                        System.out.println("Enter valid choice");
                        break;
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppoinment( Patient patient, Docters docter, Connection  connection, Scanner scanner){
        System.out.print("Enter Patient Id : ");
        int PatientId = scanner.nextInt();
        System.out.print("Enter Docter Id : ");
        int docterId = scanner.nextInt();
        System.out.print("Enter appointments date (YYYY-MM-DD) : ");
        String appointmentsDate = scanner.next();
        if (patient.getPatientById(PatientId) && docter.getDocterById(docterId)){
            if(checkDocterAvailability(docterId,appointmentsDate,connection)){
                String appointmentsQuery = "INSERT INTO appointments(patient_id, docter_id, appointments_date)VALUES(?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentsQuery);
                    preparedStatement.setInt(1, PatientId);
                    preparedStatement.setInt(2,docterId);
                    preparedStatement.setString(3,appointmentsDate);
                    int rowAffected = preparedStatement.executeUpdate();
                    if(rowAffected>0){
                        System.out.println("Appointment Booked!!");
                    }else{
                        System.out.println("Failed to Book Appointment!!");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }

            }else{
                System.out.println("Docter not available on this date ");
            }
        }else{
            System.out.println("Either Docter or patient doesn't exit");
        }
    }
    public static boolean checkDocterAvailability(int docterId, String appointmentsDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE docter_id = ? AND appointments_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,docterId);
            preparedStatement.setString(2,appointmentsDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }
                else{
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
