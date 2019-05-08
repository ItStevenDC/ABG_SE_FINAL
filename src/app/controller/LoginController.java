package app.controller;

import animation.Shaker;
import app.helper.DbConnect;
import app.helper.UpdatableBCrypt;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Text userfname;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField pf_password;

    @FXML
    private Button login;

    @FXML
    private Text errorLogin;




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


    private static LoginController Instance;

    public LoginController()
    {
        Instance = this;
    }


    public static LoginController getInstance()
    {
        return Instance;
    }


    public String firstname()
    {
        return userfname.getText();
    }

    public String usrnme() {
        return tf_username.getText();
    }

    public String pwsrd() {
        return pf_password.getText();
    }



    @FXML
    void login(MouseEvent event) throws SQLException, IOException {


        String usernameDB, passwordDB;

        usernameDB = tf_username.getText();
        passwordDB = pf_password.getText();

        Connection connection = DbConnect.getInstance().getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS_TABLE WHERE username" +
                " = '" + usernameDB + "' AND password = '" + passwordDB + "' AND role = 0 OR role = 2");


    if (resultSet.next()) {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Home.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));


        String Getfname = "SELECT firstname FROM USERS_TABLE WHERE username" + "= '" + usernameDB + "'";
        ResultSet getName = statement.executeQuery(Getfname);

        while (getName.next()) {
            String fName = getName.getString("firstname");
            System.out.println("Login Successful");
            userfname.setText(fName);
            System.out.println("User " + fName + " logged in!");
        }
    } else {
        Shaker shaker = new Shaker(tf_username);
        shaker.shake();

        errorLogin.setText("Incorrect Username or Password!");

    }
}





    @FXML
    void signup(MouseEvent event) throws IOException {
        Notifications notificationBuilder = Notifications.create()
                .title ("Sign Up User")
                .text ("Please Contact the Admin to Create an Account!")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.CENTER)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Sign Up Pressed");
                    }
                });
        notificationBuilder.showConfirm();
    }



    @FXML
    void AdminLogin(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/app/view/AdminLogin.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
