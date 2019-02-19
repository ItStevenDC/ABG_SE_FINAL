package app.helper;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;
import java.sql.*;

public class DbConnect extends Config {

    @FXML
    private Text usernameTaken;

    public DbConnect() {

    }

    public static DbConnect getInstance() {

        return new DbConnect();
    }

    Connection DbConnect;


    public Connection getConnection() {

        String connect_string = "jdbc:mysql://localhost:3306/abgdb?useUnicode=true&"
                + "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
                + "useSSL=false";


        Connection connection = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connect_string, dbUser, dbPass);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }


    //Write
    public void signUpUser(String firstname, String lastname, String username, String password, String email, String gender) throws SQLException {


        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getInstance().getConnection();

        String insert = "INSERT INTO " + Const.USERS_TABLE + "(" +
                Const.USERS_FIRSTNAME + "," +
                Const.USERS_LASTNAME + "," +
                Const.USERS_USERNAME + "," +
                Const.USERS_PASSWORD + "," +
                Const.USERS_EMAIL + "," +
                Const.USERS_GENDER + ")"
                + "VALUES(?,?,?,?,?,?)";

        PreparedStatement ps = null;

        try {
            PreparedStatement preparedStatement = DbConnect.getConnection().prepareStatement(insert);

            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, gender);

            preparedStatement.executeUpdate();


        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getSQLState().equals("23000")) {
                if (e.getMessage().contains("Duplicate")) {
                    {
                        System.out.println("Username Is Already Taken!");

                    }
                } else {
                    {
                        System.err.println("SQL STATE" + e.getSQLState());
                        System.err.println("exception on update: " + e.getMessage());
                        throw e;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL STATE" + e.getSQLState());
            System.err.println("exception on update: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}


    //Read

    //Update

    //Delete


