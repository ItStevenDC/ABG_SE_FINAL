package app.controller;

import app.helper.DbConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ResultController implements Initializable {

    @FXML
    private Text PatientFirstName;

    @FXML
    private Text PatientLastName;

    @FXML
    private Text PatientAge;

    @FXML
    private Text PatientPh;

    @FXML
    private Text PatientPco;

    @FXML
    private Text PatientHco;

    @FXML
    private Text PatientFio;

    @FXML
    private Text PatientResult;

    @FXML
    private TextArea UserComment;

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
    void updateResult(MouseEvent event) throws SQLException {
        UpdateResult();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
  PatientFname(MainController.getInstance().firstname());
  PatientLname(MainController.getInstance().lastname());

    }


    public void PatientFname(String user)
    {

        this.PatientFirstName.setText(user);
    }

    public void PatientLname(String user)
    {

        this.PatientLastName.setText(user);
    }

    void UpdateResult() throws SQLException {

        Connection connection = DbConnect.getInstance().getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM PATIENT_TABLE WHERE fname" +
                " = '" + PatientFirstName.getText() + "' AND lname = '" + PatientLastName.getText() + "'");

        while (resultSet.next()){
            String Patientage = resultSet.getString("age");
            String Patientph = resultSet.getString("ph");
            String Patientpco = resultSet.getString("pco");
            String Patienthco = resultSet.getString("hco");
            String Patientfio = resultSet.getString("fio");
            String Patientresults = resultSet.getString("result");
            String Usercomments = resultSet.getString("comments");

            PatientAge.setText(Patientage);
            PatientPh.setText(Patientph);
            PatientPco.setText(Patientpco);
            PatientHco.setText(Patienthco);
            PatientFio.setText(Patientfio);
            PatientResult.setText(Patientresults);
            UserComment.setText(Usercomments);

        }



    }

    @FXML
    void Doneinterp (MouseEvent event) throws SQLException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/ViewPatient.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }








}
