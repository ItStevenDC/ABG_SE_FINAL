package app.controller;

import app.helper.DbConnect;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminHomeController implements Initializable {
    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private StackPane parentContainer;

    @FXML
    private HBox adminChangePW;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }


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

/*    public void initialLogin() throws SQLException, IOException {
        String initial = AdminLoginController.getInstance().usernme();
        Connection connection = DbConnect.getInstance().getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS_TABLE WHERE username" +
                " = '" + initial + "' AND adminfirst = 1");

        if (resultSet.next()) {

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
    }
    */


    @FXML
        void SignUp (MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/app/view/SignUp.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    @FXML
    void logout (MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/AdminLogin.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    @FXML
    void UserLogs (MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Work in Progress!");
        alert.setHeaderText(null);
        alert.setContentText("This Feature is not available at the moment!");
        alert.showAndWait();
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

}
