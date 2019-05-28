package app.controller;

import app.helper.DbConnect;
import app.helper.UpdatableBCrypt;
import com.jfoenix.controls.JFXPasswordField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChangePWController  implements Initializable {

    @FXML
    private PasswordField Pfc_field;

    @FXML
    private PasswordField NPfc_field;

    @FXML
    private PasswordField NPfcc_field;


    public void changePW() throws SQLException

    {
        String userID = AdminLoginController.getInstance().usernme();
        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();
        String query = "UPDATE USERS_TABLE SET password= ? WHERE id='" + userID + "'";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, Pfc_field.getText());


        Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
        alertC.setTitle("Confirmation Dialog");
        alertC.setHeaderText(null);
        alertC.setContentText("Are you sure with your new password?");
        Optional<ButtonType> action = alertC.showAndWait();
        if(action.get() == ButtonType.OK) {
            ps.execute();

            ps.close();


        }
    }

    @FXML
    void pwChanged (MouseEvent event) throws IOException, SQLException {
        try{
        String AdmPass = Pfc_field.getText();
        String validated = "1";
        String userID = AdminLoginController.getInstance().usernme();
        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();
        String query = "UPDATE USERS_TABLE SET password= ?, adminfirst=? WHERE username='" + userID + "'";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, hashPassword(AdmPass));
        ps.setString(2, validated);


        Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
        alertC.setTitle("Confirmation Dialog");
        alertC.setHeaderText(null);
        alertC.setContentText("Are you sure with your new password?");
        Optional<ButtonType> action = alertC.showAndWait();
        if(action.get() == ButtonType.OK) {
            ps.execute();

            ps.close();

            Parent root = FXMLLoader.load(getClass().getResource("/app/view/AdminHome.fxml"));

            Node node = (Node) event.getSource();

            Stage stage = (Stage) node.getScene().getWindow();

            stage.setScene(new Scene(root));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password updated successfully.");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully updated your password! Your new password will take effect in the next log-in!");
            alert.showAndWait();
        }

            }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void upwChanged (MouseEvent event) throws IOException, SQLException {
        try {
            String cPass = Pfc_field.getText();
            String nPass = NPfc_field.getText();
            String cfPass = NPfcc_field.getText();

            String usernme = LoginController.getInstance().usrnme();

            String psswrd = LoginController.getInstance().pwsrd();

            DbConnect DbConnect = new DbConnect();

            Connection connection = DbConnect.getConnection();

            Statement statement = connection.createStatement();


                    if (cPass.equals(psswrd) && !nPass.equals(null) && nPass.equals(cfPass)) {
                        String query = "UPDATE USERS_TABLE SET password= ? WHERE username='" + usernme + "'";
                        PreparedStatement ps = connection.prepareStatement(query);

                        ps.setString(1, hashPassword(cfPass));

                        Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
                        alertC.setTitle("Confirmation Dialog");
                        alertC.setHeaderText(null);
                        alertC.setContentText("Are you sure with your new password?");
                        Optional<ButtonType> action = alertC.showAndWait();
                        if (action.get() == ButtonType.OK) {
                            ps.execute();

                            ps.close();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Password updated successfully.");
                            alert.setHeaderText(null);
                            alert.setContentText("You have successfully updated your password! Your new password will take effect in the next log-in!");
                            alert.showAndWait();

                            Parent root = FXMLLoader.load(getClass().getResource("/app/view/Home.fxml"));

                            Node node = (Node) event.getSource();

                            Stage stage = (Stage) node.getScene().getWindow();

                            stage.setScene(new Scene(root));

                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Invalid Password Field");
                        alert.setHeaderText(null);
                        alert.setContentText("Please make sure you have entered the correct password!");
                        alert.showAndWait();
                    }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private static String hashPassword(String plainPass){

        return UpdatableBCrypt.hashpw(plainPass, UpdatableBCrypt.gensalt());
    }
}