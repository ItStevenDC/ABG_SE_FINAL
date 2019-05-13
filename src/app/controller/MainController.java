package app.controller;

import app.helper.DbConnect;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.*;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.scene.text.Text;

public class MainController implements Initializable {


    @FXML
    private Text errorText;

    @FXML
    private Text DInterpreter;

    @FXML
    private Text resultField;

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
    private JFXDatePicker birthField;

    @FXML
    private Button InterpretData;

    @FXML
    private Button prevButton;

    @FXML
    private TextArea commentsBox;


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

    public MainController() {
        Instance = this;
    }


    public static MainController getInstance() {
        return Instance;
    }


    public String firstname() {
        return FNamefield.getText();
    }

    public String lastname() {
        return LNamefield.getText();
    }


    private int showAge() {
        if (!birthField.getPromptText().equals("mm / dd / yyyy")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Birthdate!");
            alert.setHeaderText(null);
            alert.setContentText("Something's wrong with the Birth date!");
            alert.showAndWait();

        } else {
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int byear = birthField.getValue().getYear();
            int bmonth = birthField.getValue().getMonthValue();
            int bday = birthField.getValue().getDayOfMonth();
            LocalDate dte = LocalDate.of(byear, bmonth, bday);
            LocalDate today = LocalDate.now();
            Period diff = Period.between(dte, today);

            int age = diff.getYears();

            if (age > 0) {
                return age;
            } else if (diff.getDays() <= -1 || age <= -1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Birthdate!");
                alert.setHeaderText(null);
                alert.setContentText("Error! Please check your birth date!");
                alert.showAndWait();


            } else {
                commentsBox.appendText("\nPatient is still a baby! Current Age is: " + diff.getMonths() + " Month/s and " + diff.getDays() + " Day/s!");


                System.out.println("Date" + bday + bmonth + byear + diff);
                return age;
            }
        }
        return -1;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setUsername(LoginController.getInstance().firstname());

        LocalDate minDate = LocalDate.of(1900, 1 , 1);
        LocalDate maxDate = LocalDate.now();

        birthField.setDayCellFactory(d ->
                new DateCell() {
            @Override
                    public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }});

                    }



    public void setUsername(String user)
    {
        this.DInterpreter.setText(user);
    }


    @FXML
    void interpret(MouseEvent event) throws SQLException, IOException {

        int Page = showAge();


        if (!FNamefield.getText().isEmpty() && !LNamefield.getText().isEmpty() && (Page > -1 && Page <120) && !Phfield.getText().isEmpty() && !Cofield.getText().isEmpty() && !Hcofield.getText().isEmpty() && !Ofield.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("No Input Given!");
            FNamefield.getValidators().add(validator);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to interpret the data?");
            Optional<ButtonType> action = alert.showAndWait();

            if ((action.get() == ButtonType.OK)) {
                if (FNamefield.getText() != null && LNamefield.getText() != null && (Page > -1 && Page < 120) && Phfield.getText() != null && Cofield.getText() != null && Hcofield.getText() != null && Ofield.getText() != null ) {
                    DataInterp();


                    Parent root = FXMLLoader.load(getClass().getResource("/app/view/Result.fxml"));

                    Node node = (Node) event.getSource();

                    Stage stage = (Stage) node.getScene().getWindow();

                    stage.setScene(new Scene(root));
                } else {
                    errorText.setText("Invalid Data!");
                }
            } else {
                errorText.setText("Submit the form when you are done.");
            }

        }else {

            if (Page > -1 && Page < 120) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Data Detected!");
                alert.setHeaderText(null);
                alert.setContentText("Birth Date is Invalid!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Data Detected!");
                alert.setHeaderText(null);
                alert.setContentText("Error! Invalid Data please check your inputs!");
                alert.showAndWait();
            }
        }

    }
       //         } else {
       //     errorText.setText("PLEASE FILL OUT ALL OF THE BOXES!");
     //   }


        public void DataInterp () throws SQLException, IOException {
            String Pname = FNamefield.getText();
            int Page = showAge();


            String Comments = commentsBox.getText();
            LocalTime Rtime = LocalTime.now();
            LocalDate Rdate = LocalDate.now();
            float Ph = Float.parseFloat(Phfield.getText());
            float PCO = Float.parseFloat(Cofield.getText());
            float HCO = Float.parseFloat(Hcofield.getText());
            float FIO = Float.parseFloat(Ofield.getText());
            String IResults = "";
            String DIResult;


            double Basebalance = (HCO / (0.03 * PCO));

            double ABbalance = 6.1 + Math.log10(Basebalance);

            System.out.println("Name: " + Pname);
            System.out.println("Age: " + Page);
            System.out.println("Recorded: " + Rdate + " at " + Rtime);

            System.out.println("------------------------------------");
            System.out.println(" ");
            System.out.println("Comments: " + Comments);

            System.out.println("Acid - Base Balance: " + ABbalance);
            System.out.println("Interpreted by: " + DInterpreter.getText());
            System.out.println("Interpreted Result: ");
            System.out.println(IResults);

            if (Ph >= 7.35 && Ph <= 7.45) {
                if (Ph < 7.4 && PCO < 35) {
                    System.out.println("Compensated Metabolic Acidosis");
                    if (PCO < 35) {
                        IResults = "Metabolic Acidosis, fully compensated by Respiratory Alkalosis";
                        System.out.println("Compensated by Respiratory Alkalosis");
                    }
                } else if (Ph < 7.4 && PCO > 45) {

                    System.out.println("Compensated Resperatory Acidosis");
                    if (HCO > 26) {
                        IResults = "Resperatory Acidosis, fully compensated by Metabolic Alkalosis";
                        System.out.println("Compensated by Metabolic Alkalosis");
                    }
                } else if (Ph > 7.4 && PCO < 35) {

                    System.out.println("Compensated Respiratory Alkalosis");
                    if (PCO < 35) {
                        IResults = "Respiratory Alkalosis, fully compensated by Metabolic Acidosis";
                        System.out.println("Compensated by Metabolic Acidosis");
                    }

                } else if (Ph > 7.4 && PCO > 45) {

                    System.out.println("Compensated Metabolic Alkalosis");
                    if (HCO > 28) {
                        IResults = "Metabolic Alkalosis, fully compensated by Respiratory Acidosis";
                        System.out.println("Compensated by Respiratory Acidosis");
                    }
                }

            } else if ((Ph >= 7.35 && Ph <= 7.45) && (PCO >= 35 && PCO <= 45)) {
                IResults = "Normal Acid Base";
                System.out.println("Normal Acid Base");

            } else if ((Ph < 7.35) && (PCO >= 35 && PCO <= 45)) {
                IResults = "Uncompensated Metabolic Acidosis";
                System.out.println("Uncompensated Metabolic Acidosis");
            } else if (Ph < 7.35 && PCO < 35) {

                System.out.println("Partly Compensated Metabolic Acidosis");
                if (PCO < 35) {
                    IResults = "Metabolic Acidosis, partially compensated by Respiratory Alkalosis";
                    System.out.println("Partly Compensated by Respiratory Alkalosis");
                }
            } else if (Ph < 7.35 && PCO > 45) {
                if (HCO >= 22 && HCO <= 28) {
                    IResults = "Uncompensated Respiratory Acidosis";
                    System.out.println("Uncompensated Respiratory Acidosis");
                } else if (HCO > 28) {

                    System.out.println("Partly Compensated Respiratory Acidosis");
                    if (PCO > 45) {
                        IResults = "Respiratory Acidosis, partially compensated by Metabolic Alkalosis";
                        System.out.println("Partly Compensated by Metabolic Alkalosis");
                    }
                } else if (HCO < 22) {
                    IResults = "Combine Respiratory and Metabolic Acidosis";
                    System.out.println("Combined Respiratory and Metabolic Acidosis");
                }
            } else if (Ph > 7.45 && (PCO >= 35 && PCO <= 45)) {
                IResults = "Uncompensated Metabolic Alkalosis";
                System.out.println("Uncompensated Metabolic Alkalosis");
            } else if (Ph > 7.45 && PCO > 45) {

                System.out.println("Partly Compensated Metabolic Alkalosis");
                if (PCO > 45) {
                    IResults = "Metabolic Alkalosis, partially compensated by Respiratory Acidosis";
                    System.out.println("Partly Compensated by Respiratory Acidosis");
                }
            } else if (Ph > 7.45 && PCO < 35) {
                if (PCO < 35 && (HCO >= 22 && HCO <= 28)) {
                    IResults = "Uncompensated Respiratory Alkalosis";
                    System.out.println("Uncompensated Respiratory Alkalosis");
                } else if (PCO < 35 && HCO > 28) {
                    IResults = "Combined Respiratory and Metabolic Alkalosis";
                    System.out.println("Combined Respiratory and Metabolic Alkalosis");
                    if (PCO > 45) {


                        System.out.println("Partly Compensated by Metabolic Alkalosis");
                    }
                } else if (PCO < 35 && HCO < 22) {
                    IResults = "Metabolic Alkalosis, partially compensated by Respiratory Acidosis";
                    System.out.println("Partly Compensated Respiratory Acidosis");
                }


            }


            DIResult = IResults;



             DbConnect DbConnect = new DbConnect();
            Connection connection = DbConnect.getConnection();

            Ofield.setOnKeyPressed(e -> {
        System.out.println("System Output: "+ DIResult);
        resultField.setText("Interpreted Result: " + DIResult + "!");
            });


                DbConnect.registerPatient(FNamefield.getText(), LNamefield.getText(), Page,
                Phfield.getText(), Cofield.getText(), Hcofield.getText(), Ofield.getText(), Rdate.toString(),
                Rtime.toString(), commentsBox.getText(), DIResult, DInterpreter.getText());

                errorText.setText("The patient data is now recorded!");

        }





    @FXML
    private void InterpAlert() throws IOException, SQLException {
        if (((FNamefield != null) && (LNamefield != null) && (birthField.getValue() == LocalDate.now()))) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to interpret the data?");
        Optional<ButtonType> action = alert.showAndWait();

        if ((action.get() == ButtonType.OK)) {

            DataInterp();
        }
             else {
                errorText.setText("Please Submit the form when you are done!");
            }
        }else
        {
            errorText.setText("Error! Please Check the values!");
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
