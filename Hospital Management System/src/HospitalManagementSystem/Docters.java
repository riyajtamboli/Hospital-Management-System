package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Docters {
    private final Connection connection;
    public Docters(Connection connection) {
        this.connection = connection;
    }

    public void viewDocters(){
        String query = "select * from Docters";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Docters : ");
            System.out.println("+------------+-------------------+-----------------------+");
            System.out.println("| Docter id  | Name              | Specialization        |");
            System.out.println("+------------+-------------------+-----------------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("|%-12s|%-19s|%-23s|\n", id, name, specialization);
                System.out.println("+------------+-------------------+-----------------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public  boolean getDocterById(int id){
        String query ="select * from docters where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
