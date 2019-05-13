package app.controller;

import animation.Shaker;
import app.helper.DbConnect;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
import java.util.Optional;
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

    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private StackPane parentContainer;


    @FXML
    private HBox adminChangePW;




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

    private static AdminLoginController Instance;

    public AdminLoginController()
    {
        Instance = this;
    }


    public static AdminLoginController getInstance()
    {
        return Instance;
    }


    public String usernme()
    {
        return tf_username.getText();
    }

    public String psswrd() { return pf_password.getText();}



    @FXML
    void login(MouseEvent event) throws SQLException, IOException {


        String usernameDB, passwordDB;

        usernameDB = tf_username.getText();
        passwordDB = pf_password.getText();

        Connection connection = DbConnect.getInstance().getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS_TABLE WHERE username" +
                " = '" + usernameDB + "' AND password = '" + passwordDB + "' AND role > '" + 0 + "'");


        if (resultSet.next()) {

            String initial = AdminLoginController.getInstance().usernme();


            ResultSet rSS = statement.executeQuery("SELECT * FROM USERS_TABLE WHERE username" +
                    " = '" + initial + "' AND adminfirst = 1");

            if (rSS.next()) {

                Parent root = FXMLLoader.load(getClass().getResource("/app/view/AdminHome.fxml"));

                Node node = (Node) event.getSource();

                Stage stage = (Stage) node.getScene().getWindow();

                stage.setScene(new Scene(root));
                System.out.println("Admin Account Initialized...");

            } else
            {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("First Time Sign In");
                alert.setHeaderText("Change your password!");
                alert.setContentText("Please change the default password given!");
                Optional<ButtonType> action = alert.showAndWait();

                if(action.get() == ButtonType.OK) {
                    changePW();
                }

            }

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

    public void changePW() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/ChangePW.fxml"));
        Scene scene = adminChangePW.getScene();

        root.translateYProperty().set(scene.getHeight());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0 , Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            parentContainer.getChildren().remove(anchorRoot);
        });
        timeline.play();
    }







    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
