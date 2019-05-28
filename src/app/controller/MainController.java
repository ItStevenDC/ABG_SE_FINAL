package app.controller;

import app.Main;
import app.helper.DbConnect;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
import javafx.util.Duration;

public class MainController implements Initializable {


    @FXML
    private Text errorText;

    @FXML
    private Text DInterpreter;

    @FXML
    private Text clack;

    @FXML
    private Text resultField;

    @FXML
    private JFXTextField FNamefield;

    @FXML
    private JFXTextField LNamefield;


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

        try {
            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

                int second = LocalDateTime.now().getSecond();
                int minute = LocalDateTime.now().getMinute();
                int hour = LocalDateTime.now().getHour();

                clack.setText(hour + ":" + (minute) + ":" + second);

            }), new KeyFrame(Duration.seconds(1)));
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();
        }catch (Exception e){System.err.println(e);}


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

    public String IResults() {


        return IResults();
    }

        public void DataInterp () throws SQLException, IOException {
            String Pname = FNamefield.getText();
            int Page = showAge();


            String Comments = commentsBox.getText();


            String Rtime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            LocalDate Rdate = LocalDate.now();

            float ph = Float.parseFloat(Phfield.getText());
            float co2 = Float.parseFloat(Cofield.getText());
            float hco3 = Float.parseFloat(Hcofield.getText());
            float fio = Float.parseFloat(Ofield.getText());
            String IResults = "";
            String DIResult;


            double Basebalance = (hco3 / (0.03 * co2));

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
            System.out.println(returnResult());


             DbConnect DbConnect = new DbConnect();
            Connection connection = DbConnect.getConnection();

            Ofield.setOnKeyPressed(e -> {
        System.out.println("System Output: "+ returnResult());
        resultField.setText("Interpreted Result: " + returnResult() + "!");
            });

            char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
            Random rnd = new Random();
            int n = 100000 + rnd.nextInt(900000);
            int uniqueID = n;
            StringBuilder sb = new StringBuilder("ABG-" +( 0 + rnd.nextInt(900000)) + "-");
            for (int i = 0; i < 3; i++)
                sb.append(chars[rnd.nextInt(chars.length)]);

            PreparedStatement st = connection.prepareStatement("SELECT * FROM PATIENT_TABLE WHERE id = ? ");
            st.setString(1, sb.toString());
            ResultSet r1=st.executeQuery();
            if(r1.next()) {
                sb = new StringBuilder((0 + rnd.nextInt(900000)) + "-");
                for (int i = 0; i < 3; i++)
                    sb.append(chars[rnd.nextInt(chars.length)]);

                System.out.println("Redoing similar id found");
            }else

                DbConnect.registerPatient(sb.toString(),FNamefield.getText(), LNamefield.getText(), Page,
                Phfield.getText(), Cofield.getText(), Hcofield.getText(), Ofield.getText(), Rdate.toString(),
                Rtime.toString(), commentsBox.getText(), returnResult(), DInterpreter.getText());

                errorText.setText("The patient data is now recorded!");

        }




    public String returnResult(){

        String ABGresult;
        float ph = Float.parseFloat(Phfield.getText());
        float co2 = Float.parseFloat(Cofield.getText());
        float hco3 = Float.parseFloat(Hcofield.getText());

        if ((ph < 7.35) && (co2 > 45) && (hco3 >=22 && hco3 <=28)) {
            ABGresult ="Acute Respiratory Acidosis";
            return ABGresult;
        }

        else if (((co2 - 35) < (hco3 - 22)) && (ph >= 7.35 && ph <= 7.45) && (co2 < 35) && (hco3 <22)) {
            ABGresult ="Compensated Respiratory Alkalosis";
            return ABGresult;
        }

        else if (((co2 - 35) > (hco3 - 22)) && (ph >= 7.35 && ph <= 7.45) && (co2 < 35) && (hco3 <22)) {
            ABGresult ="Compensated Metabolic Acidosis";
            return ABGresult;
        }

        else if (((co2 - 35) == (hco3 - 22)) && (ph >= 7.35 && ph <= 7.45) && (co2 < 35) && (hco3 <22)) {
            ABGresult ="Compensated Metabolic Acidosis or Compensated Respiratory Alkalosis";
            return ABGresult;
        }

        else if ((ph > 7.45) && (co2 < 35) && (hco3 >=22 && hco3 <=28)) {
            ABGresult ="Acute Respiratory Alkalosis";
            return ABGresult;
        }

        else if (((co2 - 45) < (hco3 - 28)) && (ph >= 7.35 && ph <= 7.45) && (co2 > 45) && (hco3 >28)) {
            ABGresult ="Compensated Metabolic Alkalosis";
            return ABGresult;
        }

        else if (((co2 - 45) > (hco3 - 28)) && (ph >= 7.35 && ph <= 7.45) && (co2 > 45) && (hco3 >28)) {
            ABGresult ="Compensated Respiratory Acidosis";
            return ABGresult;
        }

        else if (((co2 - 45) == (hco3 - 28)) && (ph >= 7.35 && ph <= 7.45) && (co2 > 45) && (hco3 >28)) {
            ABGresult ="Compensated Respiratory Acidosis or Compensated Metabolic Alkalosis";
            return ABGresult;
        }

        else if ((ph <7.35) && (co2 >=35 && co2 <=45) && (hco3 <22)) {
            ABGresult ="Acute Metabolic Acidosis";
            return ABGresult;
        }

        else if ((ph <7.35) && !(ph==0) && (co2 <35) && (hco3 <22)) {
            ABGresult ="Partly Compensated Metabolic Acidosis";
            return ABGresult;
        }

        else if ((ph >7.45) && (co2 >=35 && co2 <=45) && (hco3 >28)) {
            ABGresult ="Acute Metabolic Alkalosis";
            return ABGresult;
        }

        else if ((ph >7.45) && (co2 >45) && (hco3 >28)) {
            ABGresult ="Partly Compensated Metabolic Alkalosis";
            return ABGresult;
        }

        else if ((ph >= 7.34 && ph <= 7.46) && (co2 >=34 && co2 <=46) && (hco3 >=21 && hco3 <=29)) {
            ABGresult ="Normal Arterial Blood Gas";
            return ABGresult;
        }

        else if ((ph < 7.35) && (co2 >45) && (hco3 >28)) {
            ABGresult ="Partly Compensated Respiratory Acidosis";
            return ABGresult;
        }

        else if (( ph > 7.45) && (co2 <35) && (hco3 <22)) {
            ABGresult ="Partly Compensated Respiratory Alkalosis";
            return ABGresult;
        }

        else if ((ph < 7.35) && (co2 >= 44) && (hco3 <= 21))
        {
            ABGresult = "Combined Respiratory and Metabolic Acidosis";
            return ABGresult;
        }

        else if ((ph > 7.45) && (co2 <= 34) && (hco3 >= 28))
        {
            ABGresult = "Combined Respiratory and Metabolic Acidosis";
            return ABGresult;
        }

        else {
            ABGresult ="Unable to determine.";
            return ABGresult;
        }


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
