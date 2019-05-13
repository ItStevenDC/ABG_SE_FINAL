package app.controller;

import app.helper.DbConnect;
import app.helper.UpdatableBCrypt;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.controlsfx.control.Notifications;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {
    private static final Logger Log = Logger.getLogger(sun.rmi.runtime.Log.class);


    @FXML
    private JFXRadioButton RbAdmin;

    @FXML
    private JFXRadioButton RbMale;

    @FXML
    private JFXRadioButton RbSuper;

    @FXML
    private ToggleGroup tgGender;

    @FXML
    private JFXRadioButton RbFemale;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_firstname;

    @FXML
    private TextField tf_lastname;

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
        if (RbMale.isSelected()) {
             gender = "Male";

        } else if (RbFemale.isSelected()) {
             gender = "Female";
        }
        return gender;
    }

    public int isAdmin() {

        int Admin = 0;

        if (RbAdmin.isSelected()) {
            Admin = 2;
        } else {
            Admin = 0;
        }
        return Admin;
    }

    public int isVirg() {
        int adminfirst = 0;

        if (RbAdmin.isSelected()) {
            adminfirst = 0;
        } else
        {
            adminfirst = 1;
        }
        return adminfirst;
    }


    public int isSuper() throws SQLException {

        int SuperRole = 0;
        String usernameDB = AdminLoginController.getInstance().usernme();
        String passwordDB = AdminLoginController.getInstance().psswrd();

        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();


        Statement statement = connection.createStatement();


        ResultSet resultSet = statement.executeQuery("SELECT ROLE FROM USERS_TABLE WHERE username" +
                " = '" + usernameDB + "' AND password = '" + passwordDB + "'");


        while (resultSet.next()) {


            int checkRole = resultSet.getInt("role");

            SuperRole = checkRole;
            System.out.println(SuperRole + " "+checkRole);
        }

        return SuperRole;
    }

    @FXML
    void backPage (MouseEvent event) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/app/view/AdminHome.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));


    }


    @FXML
    void signup(MouseEvent event) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, IOException {


        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();


        Statement statement = connection.createStatement();


        String password = String.valueOf(pf_password.getText());


        if (validateMail()) {
            try {
         /*       String query = String.valueOf(statement.executeUpdate("INSERT INTO USERS_TABLE(firstname,lastname,username,email,password,role,adminfirst,gender)"
                        + "VALUES (?,?,?,?,?,?,?,?)"));
                //"INSERT INTO USERS_TABLE SET firstname=?, lastname=?, username=?, email=?, password=?, role=?,adminfirst=?,gender=?");
                PreparedStatement ps = connection.prepareStatement(query);

                ps.setString(1, tf_firstname.getText());
                ps.setString(2, tf_lastname.getText());
                ps.setString(3, tf_username.getText());
                ps.setString(4, tf_email.getText());
                ps.setString(5, hashPassword(pf_password.getText()));
                ps.setString(6, String.valueOf(isAdmin()));
                ps.setString(7, String.valueOf(isVirg()));
                ps.setString(8, getGender());

                ps.execute();

*/
                DbConnect.signUpUser(tf_firstname.getText(), tf_lastname.getText(), tf_username.getText(), tf_email.getText(), hashPassword(pf_password.getText()), isAdmin(), isVirg(), getGender());


                if (RbAdmin.isSelected()) {

                    Parent root = FXMLLoader.load(getClass().getResource("/app/view/AdminLogin.fxml"));

                    Node node = (Node) event.getSource();

                    Stage stage = (Stage) node.getScene().getWindow();

                    stage.setScene(new Scene(root));

                    SucSign();


                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("/app/view/Login.fxml"));

                    Node node = (Node) event.getSource();

                    Stage stage = (Stage) node.getScene().getWindow();

                    stage.setScene(new Scene(root));

                    SucSign();
                }

              //  ps.close();
            } catch (SQLIntegrityConstraintViolationException e) {
                if (e.getSQLState().equals("23000")) {
                    if (e.getMessage().contains("Duplicate")) {
                        {
                            System.out.println("Username Is Already Taken!");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Username / Email is Already Taken!");
                            alert.setHeaderText(null);
                            alert.setContentText("Username / Email is already taken!");
                            alert.showAndWait();
                        }
                    } else {

                        System.err.println("SQL STATE" + e.getSQLState());
                        System.err.println("exception on update: " + e.getMessage());
                        throw e;

                    }
                }
            }
        }
    }



void SucSign() {
    Notifications notificationBuilder = Notifications.create()
            .title("Sign Up Successful!")
            .text("Please Login to continue using the System")
            .graphic(null)
            .hideAfter(Duration.seconds(5))
            .position(Pos.CENTER)
            .onAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Sign Up Successfull!");
                }
            });
    notificationBuilder.showConfirm();
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

      try {
           int Supercheck = isSuper();
           int check = Supercheck;
            System.out.println(check);
           if (check == 1){
               RbSuper.setDisable(false);
           } else if (check == 2) {
               RbSuper.setDisable(true);
           } else {
               RbSuper.setDisable(true);
           }

        } catch (SQLException e) {
          Log.error(e);

            e.printStackTrace();
        }

    }


    private boolean validateMail() {

        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");

        Matcher m =  p.matcher(tf_email.getText());

        if (m.find() && m.group().equals(tf_email.getText())){
            mail();
            return true;


        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Invalid Mail Detected!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email!");
            alert.showAndWait();

            return false;
        }

    }


    public void mail(){

        try {

            String token = Long.toHexString(Double.doubleToLongBits(Math.random()));

            String host = "smtp.gmail.com";
            String user = "usth.abgir@gmail.com";
            String pass = "rootlocalhost";
            String to = tf_email.getText();
            String from = "usth.abgir@gmail.com";
            String subject = "You have successfully signed up!";
            String messageText = "Token: "+ token +" ";

            boolean sessionDebug = false;

            Properties  props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");





        }catch (Exception ex){

        }


    }

    private static String hashPassword(String plainPass){

        return UpdatableBCrypt.hashpw(plainPass, UpdatableBCrypt.gensalt());
    }

}
