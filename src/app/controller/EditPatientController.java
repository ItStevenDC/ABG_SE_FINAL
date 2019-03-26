package app.controller;

import app.helper.DbConnect;
import app.helper.ModelTable;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.awt.*;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EditPatientController implements Initializable {
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
    private JFXTextField updateFNamefield;

    @FXML
    private JFXTextField updateLNamefield;

    @FXML
    private JFXTextField updateAgefield;

    @FXML
    private JFXTextField updatePhfield;

    @FXML
    private JFXTextField updatePcofield;

    @FXML
    private JFXTextField updateHcofield;

    @FXML
    private JFXTextField updateInterpreterfield;

    @FXML
    private TextArea updateCommentArea;

    @FXML
    private JFXTextField updateResultText;

    @FXML
    private Button updateUser;



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

        EditValue();
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

    private void EditValue() {
            table.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    ModelTable ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());

                    updateFNamefield.setText(ml.getFname());
                    updateLNamefield.setText(ml.getLname());
                    updateAgefield.setText(ml.getAge());
                    updatePhfield.setText(ml.getPh());
                    updatePcofield.setText(ml.getPco());
                    updateHcofield.setText(ml.getHco());
                    updateCommentArea.setText(ml.getComments());
                    updateInterpreterfield.setText(ml.getInterpreter());
                    updateResultText.setText(ml.getResults());



                }
            });

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
                        return true;
                    }
                    else if (modelTable.getLname().toLowerCase().contains(lowerCaseFilter))
                    {
                        return true;
                    }
                    else if (modelTable.getDatem().toLowerCase().contains(lowerCaseFilter))
                    {
                        return true;
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
    void DeleteUser(MouseEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK) {
            ModelTable ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());
            loadCellTable();
        }
    }

    private void loadCellTable(){
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_fn.setCellValueFactory(new PropertyValueFactory<>("fname"));
        col_ln.setCellValueFactory(new PropertyValueFactory<>("lname"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_ph.setCellValueFactory(new PropertyValueFactory<>("ph"));
        col_pco.setCellValueFactory(new PropertyValueFactory<>("pco"));
        col_hco.setCellValueFactory(new PropertyValueFactory<>("hco"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("datem"));
        col_interpreter.setCellValueFactory(new PropertyValueFactory<>("interpreter"));

    }

    @FXML
     void updateUser(MouseEvent event) throws IOException {


        try {
            ModelTable ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());
                LocalTime Rtime = LocalTime.now();
                LocalDate Rdate = LocalDate.now();
               String modifiedDate = Rdate.toString();
               String modifiedtime = Rtime.toString();
                DbConnect DbConnect = new DbConnect();
                Connection connection = DbConnect.getConnection();
                String query = "UPDATE PATIENT_TABLE SET fname= ?, lname= ?, age= ?,ph= ?, pco= ?," +
                        " hco= ?,datem= ?, timem= ?, comments= ?, result= ?, interpreter= ? WHERE id='"+ml.getId()+"'";
                PreparedStatement ps = connection.prepareStatement(query);

                ps.setString(1, updateFNamefield.getText());
                ps.setString(2, updateLNamefield.getText());
                ps.setString(3, updateAgefield.getText());
                ps.setString(4, updatePhfield.getText());
                ps.setString(5, updatePcofield.getText());
                ps.setString(6, updateHcofield.getText());
                ps.setString(7, modifiedDate);
                ps.setString(8, modifiedtime);
                ps.setString(9, updateCommentArea.getText());
                ps.setString(10, updateResultText.getText());
                ps.setString(11,updateInterpreterfield.getText());


                Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
                alertC.setTitle("Confirmation Dialog");
                alertC.setHeaderText(null);
                alertC.setContentText("Are you sure you want to edit the data?");
                Optional<ButtonType> action = alertC.showAndWait();
                if(action.get() == ButtonType.OK) {
                    ps.execute();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("User updated successfully.");
                    alert.setHeaderText(null);
                    alert.setContentText("The patient "+updateFNamefield.getText()+" "+updateLNamefield.getText()+" has updated their data.");
                    alert.showAndWait();


                    ps.close();
                    refreshTable();
                    clearfields();
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadCellTable();
    }
    private void clearfields(){

        updateFNamefield.clear();
        updateLNamefield.clear();
        updateAgefield.clear();
        updatePhfield.clear();
        updatePcofield.clear();
        updateHcofield.clear();
        updateInterpreterfield.clear();
        updateCommentArea.clear();
        updateResultText.clear();
    }

    private void refreshTable(){
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

    }
    }



