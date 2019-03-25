package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.text.Text;




public class ResultController implements Initializable {

    @FXML
    private Text PatientFullName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
   String PatientFname = MainController.getInstance().firstname();
   String PatientLname = MainController.getInstance().lastname();

    }











}
