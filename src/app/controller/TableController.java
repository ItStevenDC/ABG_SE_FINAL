package app.controller;

import app.helper.ModelTable;
import app.helper.DbConnect;
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
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
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
    void editPage(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/Main.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }


}
