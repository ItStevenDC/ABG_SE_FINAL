package app.controller;

import app.helper.DbConnect;
import app.helper.UpdatableBCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {



    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_firstname;

    @FXML
    private TextField tf_lastname;

    @FXML
    private CheckBox isMale;

    @FXML
    private CheckBox isFemale;

    @FXML
    private TextField tf_email;

    @FXML
    private PasswordField pf_password;
    @FXML
    private Text takenusername;


    double x = 0, y = 0;

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void dragged(MouseEvent event) {

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void login(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Login.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    public String getGender()  {

        String gender = null;
        if (isMale.isSelected()) {
             gender = "Male";

        } else if (isFemale.isSelected()) {
             gender = "Female";
        } else if (isFemale.isSelected() && isMale.isSelected()) {
            System.out.println("Select Only One Gender");
        }
        return gender;
    }
    @FXML
    void signup(MouseEvent event) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        /*
        String gender = "";

        if (checkBoxMale.isSelected()) {

            gender += checkBoxMale.getText() + "";
        }
        else if (checkBoxFemale.isSelected()){

            gender += checkBoxFemale.getText() + "";
        } else
        {
            System.out.println("Error Gender");
        }
*/



        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();



        String password = String.valueOf(pf_password.getText());

        String hashpw = (UpdatableBCrypt.hashpw(password, UpdatableBCrypt.gensalt()));

        DbConnect.signUpUser(tf_firstname.getText(),tf_lastname.getText(), tf_username.getText(),
                tf_email.getText(), hashpw, getGender());





    }




        /*
        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getInstance().getConnection();

        PreparedStatement ps = null;

        int recordCounter = 0;

        try {

            String firstname = tf_firstname.getText();
            String lastname = tf_lastname.getText();
            String username = tf_username.getText();
            String email = tf_email.getText();
            String password = pf_password.getText();


            Statement statement = connection.createStatement();

            int status = statement.executeUpdate("insert into USERS_TABLE (firstname,lastname,username,email,password)" +
                    " values(' " + firstname + "',' " + lastname + "','" + username + "','" + email + "','" + password + "','" + "MALE" + "')");

            ps.setString(1,firstname);
            ps.setString(2,lastname);
            ps.setString(3,username);
            ps.setString(4,email);
            ps.setString(5,password);


            recordCounter = ps.executeUpdate();

            if (status > 0) {
                System.out.println("user registered");
            }

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

        return recordCounter;
    }

*/



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
