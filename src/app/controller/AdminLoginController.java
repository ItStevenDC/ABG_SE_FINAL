package app.controller;

import animation.Shaker;
import app.helper.DbConnect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AdminLoginController implements Initializable {

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



    @FXML
    void login(MouseEvent event) throws SQLException, IOException {


        String usernameDB, passwordDB;

        usernameDB = tf_username.getText();
        passwordDB = pf_password.getText();

        Connection connection = DbConnect.getInstance().getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS_TABLE WHERE username" +
                " = '" + usernameDB + "' AND password = '" + passwordDB + "' AND role = 1");


        if (resultSet.next()) {
            Parent root = FXMLLoader.load(getClass().getResource("/app/view/SignUp.fxml"));

            Node node = (Node) event.getSource();

            Stage stage = (Stage) node.getScene().getWindow();

            stage.setScene(new Scene(root));

        } else {
            Shaker shaker = new Shaker(tf_username);
            shaker.shake();

            errorLogin.setText("Incorrect Username or Password!");

        }
    }


    @FXML
    void UserLogin(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Login.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
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




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
