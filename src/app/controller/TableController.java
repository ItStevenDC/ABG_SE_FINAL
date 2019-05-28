package app.controller;

import app.helper.ModelTable;
import app.helper.DbConnect;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ibex.nestedvm.util.Seekable;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class TableController implements Initializable {

    @FXML
    private JFXTextField searchField;

    @FXML
    private TableView<ModelTable> table;

    @FXML
    private TableColumn<ModelTable, String> col_id;

    @FXML
    private TableColumn<ModelTable, String> col_fn;

    @FXML
    private TableColumn<ModelTable, String> col_ln;

    @FXML
    private TableColumn<ModelTable, String> col_age;

    @FXML
    private TableColumn<ModelTable, String> col_ph;

    @FXML
    private TableColumn<ModelTable, String> col_pco;

    @FXML
    private TableColumn<ModelTable, String> col_hco;

    @FXML
    private TableColumn<ModelTable, String> col_date;


    @FXML
    private TableColumn<ModelTable, String> col_interpreter;

    @FXML
    private JFXRadioButton rbFname;

    @FXML
    private JFXRadioButton rbLname;

    @FXML
    private JFXRadioButton rbDate;

    @FXML
    private JFXRadioButton rbInter;

    @FXML
            private Button exportXL;


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



    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {




        Connection connection = DbConnect.getInstance().getConnection();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM PATIENT_TABLE");

                while (resultSet.next()) {

                    oblist.add(new ModelTable(resultSet.getString("id"),
                            resultSet.getString("fname"),
                            resultSet.getString("lname"),
                            resultSet.getString("age"),
                            resultSet.getString("ph"),
                            resultSet.getString("pco"),
                            resultSet.getString("hco"),
                            resultSet.getString("datem"),
                            resultSet.getString("comments"),
                            resultSet.getString("interpreter"),
                            resultSet.getString("result")));

                }



        } catch (SQLException e) {

            e.printStackTrace();
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_fn.setCellValueFactory(new PropertyValueFactory<>("fname"));
        col_ln.setCellValueFactory(new PropertyValueFactory<>("lname"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_ph.setCellValueFactory(new PropertyValueFactory<>("ph"));
        col_pco.setCellValueFactory(new PropertyValueFactory<>("pco"));
        col_hco.setCellValueFactory(new PropertyValueFactory<>("hco"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("datem"));
        col_interpreter.setCellValueFactory(new PropertyValueFactory<>("interpreter"));




        table.setItems(oblist);





    }

    @FXML
     void Filter(KeyEvent event) {
        FilteredList<ModelTable> filteredData = new FilteredList<>(oblist, e -> true);
        searchField.setOnKeyReleased(e ->{
            searchField.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filteredData.setPredicate((Predicate<? super ModelTable >) modelTable->{

                    if (newValue == null || newValue.isEmpty())
                    {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if (modelTable.getId().contains(newValue))
                    {

                        return true;
                    }
                    else if (modelTable.getFname().toLowerCase().contains(lowerCaseFilter))
                    {
                        if (rbFname.isSelected()){
                            return true;
                        }

                    }
                    else if (modelTable.getLname().toLowerCase().contains(lowerCaseFilter))
                    {
                        if (rbLname.isSelected()){
                            return true;
                        }

                    }
                    else if (modelTable.getDatem().toLowerCase().contains(lowerCaseFilter))
                    {
                        if (rbDate.isSelected()){
                            return true;
                        }

                    }

                    else if (modelTable.getInterpreter().toLowerCase().contains(lowerCaseFilter))
                    {
                        if (rbInter.isSelected()){
                            return true;
                        }

                    }

                    return false;
                });
            });
            SortedList<ModelTable> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);

        });
    }

    @FXML
    void backPage(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Home.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    @FXML
    void editPage(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/EditPatient.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }


    public void exportPage(MouseEvent event) {
        LocalDate date = LocalDate.now();

        exportXL.setOnAction( e -> {

            try {
                DbConnect DbConnect = new DbConnect();

                Connection connection = DbConnect.getConnection();

                Statement statement = connection.createStatement();
                String query = "SELECT * FROM PATIENT_TABLE";
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet resultSet = ps.executeQuery();


                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet("Patient_Data_"+date+"");
                XSSFRow header = sheet.createRow(0);
                header.createCell(0).setCellValue("ID");
                header.createCell(1).setCellValue("First Name");
                header.createCell(2).setCellValue("Last Name");
                header.createCell(3).setCellValue("Age");
                header.createCell(4).setCellValue("Ph");
                header.createCell(5).setCellValue("pCO2");
                header.createCell(6).setCellValue("HCO3");
                header.createCell(7).setCellValue("Result");
                header.createCell(8).setCellValue("Interpreted By:");

                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
                sheet.autoSizeColumn(5);
                sheet.autoSizeColumn(6);
                sheet.setColumnWidth(7, 256*60);
                sheet.autoSizeColumn(8);

                sheet.setZoom(120);


                int index = 1;
                while (resultSet.next()) {

                    XSSFRow row = sheet.createRow(index);

                    row.createCell(0).setCellValue(resultSet.getString("id"));
                    row.createCell(1).setCellValue(resultSet.getString("fname"));
                    row.createCell(2).setCellValue(resultSet.getString("lname"));
                    row.createCell(3).setCellValue(resultSet.getString("age"));
                    row.createCell(4).setCellValue(resultSet.getString("ph"));
                    row.createCell(5).setCellValue(resultSet.getString("pco"));
                    row.createCell(6).setCellValue(resultSet.getString("hco"));
                    row.createCell(7).setCellValue(resultSet.getString("result"));
                    row.createCell(8).setCellValue(resultSet.getString("interpreter"));
                    index++;

                }

                FileOutputStream fileOut = new FileOutputStream(new File("C:\\Users\\"+System.getProperty("user.name")+"\\Desktop\\Patient_Data_"+date+".xlsx"));


                wb.write(fileOut);
                fileOut.close();

                Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
                alertC.setTitle("Exported Successfully!");
                alertC.setHeaderText(null);
                alertC.setContentText("Patient Data as of "+date+" is successfully exported!");
                alertC.showAndWait();

                ps.close();
                resultSet.close();

            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            }


        });

    }

    //Boolean function to double check if Patient data will be deleted!
    int Conf = 0;

    @FXML
    public void tableTruncate (MouseEvent event) throws SQLException {
        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();
        String query = "DELETE FROM patient_table WHERE fio > 0"; //Patient Data to Delete All
     //   String query = "TRUNCATE TABLE patient_table";
        PreparedStatement ps = connection.prepareStatement(query);
        Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
        alertC.setTitle("Confirmation Dialog");
        alertC.setHeaderText(null);
        alertC.setContentText("By clicking this button you are DELETING all the data! Are you sure you want to proceed? ");
        Optional<ButtonType> action = alertC.showAndWait();
        if (action.get() == ButtonType.OK) {

            Confirm();
            if (Conf == 1) {

              //  ps.setInt(1, 0);
                int del = ps.executeUpdate();
                RefreshTable();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Patient Data Deleted Successfully!!");
                alert.setHeaderText(null);
                alert.setContentText("Number of Deleted Data: " + del);
                alert.showAndWait();

                ps.close();
            }
            else if (Conf == 0)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Reminder!");
                alert.setHeaderText(null);
                alert.setContentText("Please export your data first before trying to delete!");
                alert.showAndWait();
            }
        }
    }

    public void Confirm () {
        Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
        alertC.setTitle("Confirmation Dialog");
        alertC.setHeaderText(null);
        alertC.setContentText("Are you sure you want to delete all the data? ");
        Optional<ButtonType> action = alertC.showAndWait();
        if (action.get() == ButtonType.OK) {
            Conf = 1;
            System.out.println("TABLE DELETED!!!");
        } else
        {
            Conf = 0;
            System.out.println("Table Delete Function Cancelled!");
        }
    }

    private void RefreshTable() {

        oblist.clear();

        Connection connection = DbConnect.getInstance().getConnection();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM PATIENT_TABLE");

            while (resultSet.next()) {

                oblist.add(new ModelTable(resultSet.getString("id"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getString("age"),
                        resultSet.getString("ph"),
                        resultSet.getString("pco"),
                        resultSet.getString("hco"),
                        resultSet.getString("datem"),
                        resultSet.getString("comments"),
                        resultSet.getString("interpreter"),
                        resultSet.getString("result")));


            }

        } catch (SQLException e) {

            e.printStackTrace();

        }



        table.setItems(oblist);
    }

}
