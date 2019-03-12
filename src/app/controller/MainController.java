package app.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import javafx.scene.text.Text;

public class MainController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private Text errorText;

    @FXML
    private JFXTextField Namefield;

    @FXML
    private JFXTextField Agefield;

    @FXML
    private JFXTextField Phfield;

    @FXML
    private JFXTextField Cofield;

    @FXML
    private JFXTextField Hcofield;

    @FXML
    private JFXTextField Ofield;

    @FXML
    private Button InterpretData;

    @FXML
    private Button prevButton;

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
    void interpret (MouseEvent event) {

        if (Namefield.getText() != null && Agefield.getText() != null && Phfield.getText() != null && Cofield.getText() != null
                && Hcofield.getText() != null && Ofield.getText() != null) {
            Datainterp();
        }else
            errorText.setText("Please Input A Value to Interpret!");
        }


    public void Datainterp() {
        String Pname = Namefield.getText();
        String Page = Agefield.getText();
        LocalTime Rtime = LocalTime.now();
        LocalDate Rdate = LocalDate.now();
        float Ph = Float.parseFloat(Phfield.getText());
        float PCO = Float.parseFloat(Cofield.getText());
        float HCO = Float.parseFloat (Hcofield.getText());
        float FIO = Float.parseFloat(Ofield.getText());

        double Basebalance = (HCO / (0.03 * PCO));

        double ABbalance = 6.1 + Math.log10(Basebalance);

        System.out.println("Name: " + Pname);
        System.out.println("Age: " + Page);
        System.out.println("Recorded: " + Rdate + " at " + Rtime);

        System.out.println ("------------------------------------");
        System.out.println(" ");
        System.out.println("Acid - Base Balance: " + ABbalance);

        System.out.println("Interpreted Result: ");

        if (Ph >= 7.35 && Ph <= 7.45)
        {
            if (Ph < 7.4 && PCO < 35){
                System.out.println("Compensated Metabolic Acidosis");
                if (PCO < 35){
                    System.out.println("Compensated by Respiratory Alkalosis");
                }else {
                    System.out.println("Compensated by Respiratory Acidosis");
                }
            } else if (Ph < 7.4 && PCO > 45) {
                System.out.println("Compensated Resperatory Acidosis");
                if (HCO > 26){
                    System.out.println("Compensated by Metabolic Alkalosis");
                }
            }
            else if(Ph >7.4 && PCO <35){
                System.out.println("Compensated Respiratory Alkalosis");
                if(PCO <35){
                    System.out.println("Compensated by Metabolic Acidosis");
                }

            }
            else if(Ph >7.4 && PCO >45){
                System.out.println("Compensated Metabolic Alkalosis");
                if(HCO >28){
                    System.out.println("Compensated by Respiratory Acidosis");
                }
            }

    } else if ( (Ph >= 7.35 && Ph <= 7.45) && (PCO >= 35 && PCO <= 45)) {

            System.out.println("Normal Acid Base");

        } else if ( (Ph < 7.35) && (PCO >= 35 && PCO <= 45))
        {
            System.out.println("Uncompensated Metabolic Acidosis");
        } else if ( Ph < 7.35 && PCO < 35 )
        {
            System.out.println("Partly Compensated Metabolic Acidosis");
            if (PCO < 35) {
                System.out.println("Partly Compensated by Resperatory Alkalosis");
            }
        } else if ( Ph < 7.35 && PCO > 45 )
        {
            if (HCO >= 22 && HCO <= 28){
                System.out.println("Uncompensated Respiratory Acidosis");
            }else if (HCO > 28)
            {
                System.out.println("Partly Compensated Respiratory Acidosis");
                if (PCO > 45)
                System.out.println("Partly Compensated by Metabolic Alkalosis");
            }else if (HCO < 22)
            {
                System.out.println("Combined Respiratory and Metabolic Acidosis");
            }
        }
        else if (Ph > 7.45 && (PCO >= 35 && PCO <= 45))
        {
            System.out.println("Uncompensated Metabolic Alkalosis");
        } else if (Ph > 7.45 && PCO > 45)
        {
            System.out.println("Partly Compensated Metabolic Alkalosis");
            if (PCO > 45) {
                System.out.println("Partly Compensated by Respiratory Acidosis");
            }
        } else if (Ph > 7.45 && PCO < 35){
            if (PCO <35 && (HCO >= 22 && HCO <= 28)){
                System.out.println("Uncompensated Respiratory Alkalosis");
            }else if (PCO < 35 && HCO > 28)
            {
                System.out.println("Combined Respiratory and Metabolic Alkalosis");
                if (PCO > 45) {

                    System.out.println("Partly Compensated by Metabolic Alkalosis");
                }
            }else if (PCO <35 && HCO < 22)
            {
                System.out.println("Partly Compensated Respiratory Alkalosis");
            }
        }



}

    @FXML
    void backPage(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Home.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    /*
     * Validations
     */
    private boolean validate(String field, String value, String pattern){
        if(!value.isEmpty()){
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if(m.find() && m.group().equals(value)){
                return true;
            }else{
                validationAlert(field, false);
                return false;
            }
        }else{
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty){
        if(!empty){
            return true;
        }else{
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if(field.equals("Role")) alert.setContentText("Please Select "+ field);
        else{
            if(empty) alert.setContentText("Please Enter "+ field);
            else alert.setContentText("Please Enter Valid "+ field);
        }
        alert.showAndWait();
    }



}
