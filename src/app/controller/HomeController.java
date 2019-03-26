package app.controller;

import app.helper.DbConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import java.awt.TextArea;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;





public class HomeController implements Initializable {

    @FXML
    private Button logout_button;

    @FXML
    private VBox Viewbox;

    @FXML
    private VBox InterpretBox;

    @FXML
    private Text UserWelcome;


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
    void logout(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Login.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

        System.out.println("User Logged Out");
    }

    @FXML
    void Viewpatients(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/ViewPatient.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    @FXML
    void Interpretpatients(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Main.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    @FXML
    void UpdateData(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/EditPatient.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    @FXML
    void abgAlgo(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Abgtree.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUsername(LoginController.getInstance().firstname());
    }

    public void setUsername(String user)
    {

        this.UserWelcome.setText("Welcome, "+user.toUpperCase());
    }
}
