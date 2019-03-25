package app.controller;

import app.helper.DbConnect;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.scene.text.Text;

public class MainController implements Initializable {


    @FXML
    private Text errorText;

    @FXML
    private Text DInterpreter;

    @FXML
    private JFXTextField FNamefield;

    @FXML
    private JFXTextField LNamefield;

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

    @FXML
    private TextArea commentsBox;

    @FXML
    private DatePicker bdayField;

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


    private static MainController Instance;

    public MainController()
    {
        Instance = this;
    }


    public static MainController getInstance()
    {
        return Instance;
    }


    public String firstname()
    {
        return FNamefield.getText();
    }

    public String lastname()
    {
        return LNamefield.getText();
    }





    private String showAge() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int byear = (bdayField.getValue().getYear());
        int age = year - byear;
        return String.valueOf(age);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUsername(LoginController.getInstance().firstname());

    }

    public void setUsername(String user)
    {
        this.DInterpreter.setText(user);
    }


    @FXML
    void interpret(MouseEvent event) throws SQLException {


      /*  if (FNamefield.getText() != null && Agefield.getText() != null && Phfield.getText() != null && Cofield.getText() != null
                && Hcofield.getText() != null && Ofield.getText() != null) {
            Datainterp();
        }else
            errorText.setText("Please Input A Value to Interpret!");
        }
*/

        String Pname = FNamefield.getText();
        String Page = showAge();



        String Comments = commentsBox.getText();
        LocalTime Rtime = LocalTime.now();
        LocalDate Rdate = LocalDate.now();
        float Ph = Float.parseFloat(Phfield.getText());
        float PCO = Float.parseFloat(Cofield.getText());
        float HCO = Float.parseFloat(Hcofield.getText());
        float FIO = Float.parseFloat(Ofield.getText());

        String IResults = "TEST";

        double Basebalance = (HCO / (0.03 * PCO));

        double ABbalance = 6.1 + Math.log10(Basebalance);

        System.out.println("Name: " + Pname);
        System.out.println("Age: " + Page);
        System.out.println("Recorded: " + Rdate + " at " + Rtime);

        System.out.println("------------------------------------");
        System.out.println(" ");
        System.out.println("Comments: "+Comments);

        System.out.println("Acid - Base Balance: " + ABbalance);
        System.out.println("Interpreted by: " +DInterpreter.getText()) ;
        System.out.println("Interpreted Result: ");

        if (Ph >= 7.35 && Ph <= 7.45) {
            if (Ph < 7.4 && PCO < 35) {
                IResults = "Compensated Metabolic Acidosis";
                System.out.println("Compensated Metabolic Acidosis");
                if (PCO < 35) {
                    IResults = "";
                    System.out.println("Compensated by Respiratory Alkalosis");
                } else {
                    IResults = "";
                    System.out.println("Compensated by Respiratory Acidosis");
                }
            } else if (Ph < 7.4 && PCO > 45) {
                IResults = "";
                System.out.println("Compensated Resperatory Acidosis");
                if (HCO > 26) {
                    IResults = "";
                    System.out.println("Compensated by Metabolic Alkalosis");
                }
            } else if (Ph > 7.4 && PCO < 35) {
                IResults = "";
                System.out.println("Compensated Respiratory Alkalosis");
                if (PCO < 35) {
                    IResults = "";
                    System.out.println("Compensated by Metabolic Acidosis");
                }

            } else if (Ph > 7.4 && PCO > 45) {
                IResults = "";
                System.out.println("Compensated Metabolic Alkalosis");
                if (HCO > 28) {
                    IResults = "";
                    System.out.println("Compensated by Respiratory Acidosis");
                }
            }

        } else if ((Ph >= 7.35 && Ph <= 7.45) && (PCO >= 35 && PCO <= 45)) {
            IResults = "";
            System.out.println("Normal Acid Base");

        } else if ((Ph < 7.35) && (PCO >= 35 && PCO <= 45)) {
            IResults = "";
            System.out.println("Uncompensated Metabolic Acidosis");
        } else if (Ph < 7.35 && PCO < 35) {
            IResults = "";
            System.out.println("Partly Compensated Metabolic Acidosis");
            if (PCO < 35) {
                IResults = "";
                System.out.println("Partly Compensated by Resperatory Alkalosis");
            }
        } else if (Ph < 7.35 && PCO > 45) {
            if (HCO >= 22 && HCO <= 28) {
                IResults = "";
                System.out.println("Uncompensated Respiratory Acidosis");
            } else if (HCO > 28) {
                IResults = "";
                System.out.println("Partly Compensated Respiratory Acidosis");
                if (PCO > 45) {
                    IResults = "";
                    System.out.println("Partly Compensated by Metabolic Alkalosis");
                }
            } else if (HCO < 22) {
                IResults = "";
                System.out.println("Combined Respiratory and Metabolic Acidosis");
            }
        } else if (Ph > 7.45 && (PCO >= 35 && PCO <= 45)) {
            IResults = "";
            System.out.println("Uncompensated Metabolic Alkalosis");
        } else if (Ph > 7.45 && PCO > 45) {

            System.out.println("Partly Compensated Metabolic Alkalosis");
            if (PCO > 45) {
                IResults = "";
                System.out.println("Partly Compensated by Respiratory Acidosis");
            }
        } else if (Ph > 7.45 && PCO < 35) {
            if (PCO < 35 && (HCO >= 22 && HCO <= 28)) {
                IResults = "";
                System.out.println("Uncompensated Respiratory Alkalosis");
            } else if (PCO < 35 && HCO > 28) {
                IResults = "";
                System.out.println("Combined Respiratory and Metabolic Alkalosis");
                if (PCO > 45) {

                    IResults = "";
                    System.out.println("Partly Compensated by Metabolic Alkalosis");
                }
            } else if (PCO < 35 && HCO < 22) {
                IResults = "";
                System.out.println("Partly Compensated Respiratory Alkalosis");
            }


        } else {
            System.out.println("Error Values");
            errorText.setText("Error Values!");

        }

        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();


        DbConnect.registerPatient(FNamefield.getText(), LNamefield.getText(), showAge() ,
                Phfield.getText(), Cofield.getText(), Hcofield.getText(), Ofield.getText(), Rdate.toString(),
                Rtime.toString(), commentsBox.getText(), IResults, DInterpreter.getText());

        errorText.setText("The patient data is now recorded!");

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
    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) alert.setContentText("Please Select " + field);
        else {
            if (empty) alert.setContentText("Please Enter " + field);
            else alert.setContentText("Please Enter Valid " + field);
        }
        alert.showAndWait();
    }



}
