package app.controller;

import app.helper.DbConnect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.scene.text.Text;




public class ResultController implements Initializable {

    @FXML
    private Text PatientFirstName;

    @FXML
    private Text PatientLastName;

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

        ResultSet resultSet = statement.executeQuery("SELECT * FROM PATIENT_TABLE WHERE firstname" +
                " = '" + PatientFirstName.getText() + "' AND lastname = '" + PatientLastName.getText() + "'");

        while (resultSet.next()){
            String Patientage = resultSet.getString("age");
            String Patientph = resultSet.getString("ph");
            String Patientpco = resultSet.getString("pco");
            String Patienthco = resultSet.getString("hco");
            String Patientfio = resultSet.getString("fio");
            String Patientresults = resultSet.getString("result");
            String Usercomments = resultSet.getString("comments");
        }



    }








}
