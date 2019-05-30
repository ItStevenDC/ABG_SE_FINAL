package app.controller;

import app.helper.DbConnect;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.qrcode.ByteArray;
import com.jfoenix.controls.JFXTextArea;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;


import java.awt.*;
import java.awt.print.*;
import javafx.embed.swing.SwingNode;

import java.io.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ResourceBundle;
;

import javafx.scene.Parent;
import javafx.scene.Scene;


import javax.swing.JTextArea;

import javafx.scene.Node;

import javafx.scene.control.Alert;

import javafx.scene.input.MouseEvent;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;



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

    private JTextArea resultPrint;



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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PatientFname(MainController.getInstance().firstname());
        PatientLname(MainController.getInstance().lastname());

    }


    public void PatientFname(String user) {
        this.PatientFirstName.setText(user);
    }

    public void PatientLname(String user) {

        this.PatientLastName.setText(user);
    }

    void UpdateResult() throws SQLException {

        Connection connection = DbConnect.getInstance().getConnection();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM PATIENT_TABLE WHERE fname" + " = '" + PatientFirstName.getText() + "' AND lname = '" + PatientLastName.getText() + "' AND  datem = '" + LocalDate.now() + "'");

        while (resultSet.next()) {
            String PatientRef = resultSet.getString("id");
            String Patientage = resultSet.getString("age");
            String RecordedDate = resultSet.getString("datem");
            String RecordedTime = resultSet.getString("timem");
            String Patientph = resultSet.getString("ph");
            String Patientpco = resultSet.getString("pco");
            String Patientpo = resultSet.getString("po");
            String Patienthco = resultSet.getString("hco");
            String PatientOsat = resultSet.getString("Osat");
            String Patientfio = resultSet.getString("fio");
            String Patientresults = resultSet.getString("result");
            String Usercomments = resultSet.getString("comments");
            String Result = resultSet.getString("result");
            String Interpreter = resultSet.getString("interpreter");
            String Oxygenation = resultSet.getString("oxy");


            PatientResult.setText(Patientresults);
            UserComment.appendText("\t\t ABG Interpretation Result \n\n" + "========================================================\n" + "Patient First Name:\t\t\t" + MainController.getInstance().firstname() + "\n" +
                    "Patient Last Name:\t\t\t" + MainController.getInstance().lastname() +
                    "\n" + "Patient Age:\t\t\t\t" + Patientage +
                    "\n" + "Date Recorded:\t\t\t" + RecordedDate + "at " + RecordedTime +
                    "\n" + "Patient Ph:\t\t\t\t" + Patientph +
                    "\n" + "Patient PCO2:\t\t\t\t" + Patientpco +
                    "\n" + "Patient PO2:\t\t\t\t" + Patientpo +
                    "\n" + "Patient Bicarbonate(Hco):\t\t" + Patienthco +
                    "\n" + "Patient Oxygen Saturation:\t\t" + PatientOsat +
                    "\n" + "Patient FiO:\t\t\t\t" + Patientfio +
                    "\n" + "Patient Interpreted Result:\t" + Result + ". \n\t\t\t"+ Oxygenation +"."+ "\n" +

                    "=========================================================\n" + "Comments:\t\t\t" + Usercomments + "\n\n" + "Interpreted by:\t\t\t" + Interpreter + "\n" +

                    "");


        }


    }

    @FXML
    void updateResult(MouseEvent event) throws SQLException {
        UpdateResult();
    }

    @FXML
    void Printinterp(MouseEvent event) {

        UserComment.getText();
    }

    @FXML
    void Doneinterp(MouseEvent event) throws SQLException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/ViewPatient.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    @FXML


    public void printResult(MouseEvent event) throws PrinterException, IOException, SQLException, DocumentException {
        Connection connection = DbConnect.getInstance().getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PATIENT_TABLE WHERE fname" + " = '" + PatientFirstName.getText() + "' AND lname = '" + PatientLastName.getText() + "' AND  datem = '" + LocalDate.now() + "'");

        while (resultSet.next()) {
            String PatientRef = resultSet.getString("id");
            String Patientage = resultSet.getString("age");
            String RecordedDate = resultSet.getString("datem");
            String RecordedTime = resultSet.getString("timem");
            String Patientph = resultSet.getString("ph");
            String Patientpco = resultSet.getString("pco");
            String Patientpo = resultSet.getString("po");
            String Patienthco = resultSet.getString("hco");
            String PatientOsat = resultSet.getString("Osat");
            String Patientfio = resultSet.getString("fio");
            String Patientresults = resultSet.getString("result");
            String Usercomments = resultSet.getString("comments");
            String Oxy = resultSet.getString("oxy");
            String Interpreter = resultSet.getString("interpreter");


            int Hour = LocalDateTime.now().getHour();
            int Min = LocalDateTime.now().getMinute();
            int Sec = LocalDateTime.now().getSecond();

            PdfReader pdfTemplate = new PdfReader("app/pdf/Template2.pdf");
            FileOutputStream fileOut = new FileOutputStream(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\" + PatientFirstName.getText() + "_" + "" + PatientLastName.getText() + "_" + Hour + "-" + Min + "-" + Sec + ".pdf"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(pdfTemplate, fileOut);

            AcroFields form = stamper.getAcroFields();
            stamper.setFormFlattening(true);


            stamper.getAcroFields().setField("PatientRef", PatientRef +"");
            stamper.getAcroFields().setField("PatientName", PatientLastName.getText().toUpperCase() + ", " + PatientFirstName.getText().toUpperCase());
            stamper.getAcroFields().setField("PatientAge", Patientage+"");
            stamper.getAcroFields().setField("Patientph", Patientph+" pH");
            stamper.getAcroFields().setField("Patientpco", Patientpco+" mmHg");
            stamper.getAcroFields().setField("Patientpo", Patientpo+" mmHg");
            stamper.getAcroFields().setField("Patienthco", Patienthco+" mEq/L");
            stamper.getAcroFields().setField("PatientOsat", PatientOsat+"%");
            stamper.getAcroFields().setField("Patientfio", Patientfio+"%");
            stamper.getAcroFields().setField("Usercomments", Usercomments+"");
            stamper.getAcroFields().setField("Result", Patientresults+". "+Oxy+"");
            stamper.getAcroFields().setField("Interpreter", Interpreter.toUpperCase()+"");
            stamper.getAcroFields().setField("RecordedDate", RecordedDate);
            stamper.getAcroFields().setField("RecordedTime", RecordedTime);

            // Bottom Second Copy Form

            stamper.getAcroFields().setField("PatientRefC", PatientRef +"");
            stamper.getAcroFields().setField("PatientNameC", PatientLastName.getText().toUpperCase() + ", " + PatientFirstName.getText().toUpperCase());
            stamper.getAcroFields().setField("PatientAgeC", Patientage+"");
            stamper.getAcroFields().setField("PatientphC", Patientph+" pH");
            stamper.getAcroFields().setField("PatientpcoC", Patientpco+" mmHg");
            stamper.getAcroFields().setField("PatientpoC", Patientpo+" mmHg");
            stamper.getAcroFields().setField("PatienthcoC", Patienthco+" mEq/L");
            stamper.getAcroFields().setField("PatientOsatC", PatientOsat+"%");
            stamper.getAcroFields().setField("PatientfioC", Patientfio+"%");
            stamper.getAcroFields().setField("UsercommentsC", Usercomments+"");
            stamper.getAcroFields().setField("ResultC", Patientresults+". "+Oxy+"");
            stamper.getAcroFields().setField("InterpreterC", Interpreter.toUpperCase()+"");
            stamper.getAcroFields().setField("RecordedDateC", RecordedDate);
            stamper.getAcroFields().setField("RecordedTimeC", RecordedTime);


            form.setGenerateAppearances(true);
            stamper.close();
            pdfTemplate.close();

            Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
            alertC.setTitle("Exported Successfully!");
            alertC.setHeaderText(null);
            alertC.setContentText("Patient Data as of "+LocalDate.now()+" is successfully exported!");
            alertC.showAndWait();

            Parent root = FXMLLoader.load(getClass().getResource("/app/view/ViewPatient.fxml"));

            Node node = (Node) event.getSource();

            Stage stage = (Stage) node.getScene().getWindow();

            stage.setScene(new Scene(root));



        }

    }
}






