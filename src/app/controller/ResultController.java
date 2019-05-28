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
import java.text.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.ejb.Local;
import javax.imageio.ImageIO;
import javax.jws.soap.SOAPBinding;
import javax.swing.JTextArea;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Frame;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.VerticalSpacer;
import rst.pdfbox.layout.elements.render.VerticalLayoutHint;
import rst.pdfbox.layout.shape.Rect;
import rst.pdfbox.layout.shape.Stroke;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Constants;
import sun.awt.image.PNGImageDecoder;

import javax.swing.*;


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

    @FXML
    private SwingNode swingNode;

    private PDDocument doc;

    private String pdfPath;

    private File imageFile;


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
            String Patienthco = resultSet.getString("hco");
            String Patientfio = resultSet.getString("fio");
            String Patientresults = resultSet.getString("result");
            String Usercomments = resultSet.getString("comments");
            String Result = resultSet.getString("result");
            String Interpreter = resultSet.getString("interpreter");


            PatientResult.setText(Patientresults);
            UserComment.appendText("\t\t ABG Interpretation Result \n\n" + "========================================================\n" + "Patient First Name:\t\t\t" + MainController.getInstance().firstname() + "\n" + "Patient Last Name:\t\t\t" + MainController.getInstance().lastname() + "\n" + "Patient Age:\t\t\t\t" + Patientage + "\n" + "Date Recorded:\t\t\t" + RecordedDate + "at " + RecordedTime + "\n" + "Patient Ph:\t\t\t\t" + Patientph + "\n" + "Patient Pco:\t\t\t\t" + Patientpco + "\n" + "Patient Bicarbonate(Hco):\t\t" + Patienthco + "\n" + "Patient Oxygen Saturation(Fio):\t\t" + Patientfio + "\n" + "Patient Interpreted Result:\t\t" + Result + "\n" +

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
            String Patienthco = resultSet.getString("hco");
            String Patientfio = resultSet.getString("fio");
            String Patientresults = resultSet.getString("result");
            String Usercomments = resultSet.getString("comments");
            String Result = resultSet.getString("result");
            String Interpreter = resultSet.getString("interpreter");


            int Hour = LocalDateTime.now().getHour();
            int Min = LocalDateTime.now().getMinute();
            int Sec = LocalDateTime.now().getSecond();

            PdfReader pdfTemplate = new PdfReader("app/pdf/Template.pdf");
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
            stamper.getAcroFields().setField("Patienthco", Patienthco+" mEq/L");
            stamper.getAcroFields().setField("Patientfio", Patientfio+"%");
            stamper.getAcroFields().setField("Usercomments", Usercomments+"");
            stamper.getAcroFields().setField("Result", Patientresults+"");
            stamper.getAcroFields().setField("Interpreter", Interpreter.toUpperCase()+"");
            stamper.getAcroFields().setField("RecordedDate", RecordedDate + " at " + RecordedTime+"");


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

/*
                Document document;

             document = new Document (Constants.A4, 40, 60, 40, 60);

            imageFile = new File("/app/assets/logo.png");

            String imagee = Image.class.getResource("/app/assets/logo.png").getFile();



            try {

                doc = new PDDocument();
                PDPage page = new PDPage();

                doc.addPage(page);

                PDImageXObject pdImage = PDImageXObject.createFromFile(imagee, doc);

                PDPageContentStream contents = new PDPageContentStream(doc, page);
                PDRectangle mediaBox = page.getMediaBox();

                float startX = (mediaBox.getWidth() - pdImage.getWidth()) / 2;
                float startY = (mediaBox.getHeight() - pdImage.getHeight()) / 2;
                contents.drawImage(pdImage, startX, startY);

                contents.close();

                document.getPDDocument();

            }catch (Exception e){
                System.err.println(e.getMessage());

            }


            Paragraph title = new Paragraph();

            title.addMarkup("*Arterial Blood Gas Information Reader", 20, BaseFont.Helvetica);
            Frame frame = new Frame(title);
            frame.setShape(new Rect());
            frame.setBorder(Color.black, new Stroke());
            frame.setPadding(10, 10, 5, 5);
            frame.setMargin(40, 40, 20, 10);
            document.add(title, VerticalLayoutHint.CENTER);
            document.add(new VerticalSpacer(12));

            Paragraph parag = new Paragraph();
            Paragraph div = new Paragraph();
            div.addText("============================================================\n",14,PDType1Font.HELVETICA_BOLD);
            document.add(div, VerticalLayoutHint.CENTER);

            parag.addText("Patient Name: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + PatientFirstName.getText() + " " + PatientLastName.getText(), 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Patient Age: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Patientage, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Date Recorded: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + RecordedDate + " at " + RecordedTime, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Patient Ph: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Patientph, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Patient pCO: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Patientpco, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Patient HCO(Bicarbonate): ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Patienthco, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Patient FiO: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Patientfio, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Interpreter Results: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Result, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Interpreter's Comments: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Usercomments, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);
            parag.addText("Interpreted By: ", 14, PDType1Font.HELVETICA_BOLD);
            parag.addText("" + Interpreter, 14, PDType1Font.HELVETICA);
            parag.addText("\n", 12, PDType1Font.HELVETICA);

            document.add(parag);
            int Hour = LocalDateTime.now().getHour();
            int Min = LocalDateTime.now().getMinute();
            int Sec = LocalDateTime.now().getSecond();
            FileOutputStream fileOut = new FileOutputStream(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\" + PatientFirstName.getText() + "_" +
                    "" + PatientLastName.getText() + "_" + Hour +"-"+Min+"-"+Sec+".pdf"));

            document.save(fileOut);

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
    */
    }
}




        /*WritableImage nodeshot = UserComment.snapshot(new SnapshotParameters(), null);
        File file = new File("DataResult.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(nodeshot, null), "png", file);
        } catch (IOException e) {

        }

        PDDocument doc    = new PDDocument();
        PDPage page = new PDPage();
        PDImageXObject pdimage;
        PDPageContentStream content;
        try {
            pdimage = PDImageXObject.createFromFile("DataResult.png",doc);
            content = new PDPageContentStream(doc, page);
            content.drawImage(pdimage, 100, 100,450,450);
            content.close();
            doc.addPage(page);
            doc.save(new File("C:\\Users\\"+System.getProperty("user.name")+"\\Desktop\\"+PatientFirstName.getText()+"_"+PatientLastName.getText()+"_"+LocalDate.now()+".pdf"));
            //doc.save(""+PatientFirstName.getText()+"_"+PatientLastName.getText()+"_"+LocalDate.now()+".pdf");
            doc.close();
            file.delete();

            Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
            alertC.setTitle("Exported Successfully!");
            alertC.setHeaderText(null);
            alertC.setContentText("Patient Data as of "+LocalDate.now()+" is successfully exported!");
            alertC.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(ResultController.class.getName()).log(Level.SEVERE, null, ex);
        }






        /* String sf = "C:\\Users\\Steven-PC\\IdeaProjects\\JavaFX_Practice\\src\\app\\view\\jrxml";

        try {
            JasperReport jr = JasperCompileManager.compileReport(sf);
            HashMap<String, Object> para = new HashMap<>();


            JasperPrint jp = JasperFillManager.fillReport(sf)


        } catch (JRException e) {
            e.printStackTrace();
        }

    */





