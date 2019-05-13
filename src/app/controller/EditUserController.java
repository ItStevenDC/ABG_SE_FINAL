package app.controller;

import app.helper.DbConnect;
import app.helper.ModelTable;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.sun.org.apache.xpath.internal.operations.Mod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import sun.rmi.runtime.Log;

import java.awt.*;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserController implements Initializable {
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
    private TableColumn<ModelTable, String> col_mail;

    @FXML
    private TableColumn<ModelTable, String> col_gen;

    @FXML
    private TableColumn<ModelTable, String> col_admin;


    @FXML
    private JFXTextField updateFNamefield;

    @FXML
    private JFXTextField updateLNamefield;

    @FXML
    private JFXTextField updateGenderfield;

    @FXML
    private JFXTextField updateEmailfield;

    @FXML
    private JFXPasswordField updateOldfield;

    @FXML
    private JFXPasswordField updateNewfield;

    @FXML
    private Button updateUser;

    @FXML
    private JFXRadioButton rbAdmin;

    @FXML
    private JFXRadioButton rbSAdmin;

    @FXML
    private JFXToggleButton isAdmin;

    @FXML
    private JFXToggleButton isSuperAdmin;

    @FXML
    private Text patientInterp;

    String temp = null;



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
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM USERS_TABLE");

            while (resultSet.next()) {

                oblist.add(new ModelTable(resultSet.getString("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getString("gender"),
                        resultSet.getString("role"),
                        resultSet.getString("password")));

                          }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_fn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col_ln.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        col_mail.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_gen.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col_admin.setCellValueFactory(new PropertyValueFactory<>("role"));



        table.setItems(oblist);

    }

    private void EditValue() {

            table.setOnMouseClicked(event -> {
                ModelTable ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());
                String admin = ml.getRole();
                updateFNamefield.setText(ml.getFirstname());
                updateLNamefield.setText(ml.getLastname());
                updateEmailfield.setText(ml.getEmail());
                updateGenderfield.setText(ml.getGender());


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
                                    else if (modelTable.getFirstname().toLowerCase().contains(lowerCaseFilter))
                                    {

                                            return true;


                                    }
                                    else if (modelTable.getLastname().toLowerCase().contains(lowerCaseFilter))
                                    {

                                            return true;


                                    }
                                    else if (modelTable.getEmail().toLowerCase().contains(lowerCaseFilter))
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
        Parent root = FXMLLoader.load(getClass().getResource("/app/view/AdminHome.fxml"));

        Node node = (Node) event.getSource();

        Stage stage = (Stage) node.getScene().getWindow();

        stage.setScene(new Scene(root));

    }

    @FXML
    void DeleteUser(MouseEvent event) throws IOException, SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK) {
            DeleteUserDB();

            clearfields();
        }
    }

    private void loadCellTable(){
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_fn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col_ln.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        col_mail.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_gen.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col_admin.setCellValueFactory(new PropertyValueFactory<>("role"));


    }



    @FXML
    void updateUser(MouseEvent event) throws IOException {

        if (!updateNewfield.getText().isEmpty() && !updateOldfield.getText().isEmpty()) {


            updateDatawPW();

        } else if (!updateNewfield.getText().isEmpty() && updateOldfield.getText().isEmpty()) {
            passwordAlert();
        } else if (!updateOldfield.getText().isEmpty() && updateNewfield.getText().isEmpty()) {
            passwordAlert();
        } else if (updateOldfield.getText().isEmpty() && updateNewfield.getText().isEmpty()){

            updateData();
        }
    }



    public void updateData(){

        try {
            if (!updateNewfield.getText().isEmpty() && !updateOldfield.getText().isEmpty()) {
                updateDatawPW();
            } else {
                ModelTable ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());
                DbConnect DbConnect = new DbConnect();
                Connection connection = DbConnect.getConnection();
                if (validateMail()) {
                    String query = "UPDATE USERS_TABLE SET firstname= ?, lastname= ?, email= ?,gender= ? WHERE id='" + ml.getId() + "'";
                    PreparedStatement ps = connection.prepareStatement(query);

                    ps.setString(1, updateFNamefield.getText());
                    ps.setString(2, updateLNamefield.getText());
                    ps.setString(3, updateEmailfield.getText());
                    ps.setString(4, updateGenderfield.getText());


                    Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
                    alertC.setTitle("Confirmation Dialog");
                    alertC.setHeaderText(null);
                    alertC.setContentText("Are you sure you want to edit the data?");
                    Optional<ButtonType> action = alertC.showAndWait();
                    if (action.get() == ButtonType.OK) {
                        ps.execute();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("User updated successfully.");
                        alert.setHeaderText(null);
                        alert.setContentText("The User " + updateFNamefield.getText() + " " + updateLNamefield.getText() + " has updated their data.");
                        alert.showAndWait();


                        ps.close();
                        RefreshTable();

                        clearfields();
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadCellTable();
    }

    public void updateDatawPW(){
        try {
            ModelTable ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());
            temp = ml.getPassword();
            String validate = temp;
            String npass = updateNewfield.getText();
            if (!updateOldfield.getText().equals(temp)){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Incorrect Password!");
                alert.setHeaderText(null);
                alert.setContentText("It seems that you have the wrong password!");
                alert.showAndWait();

            }else if (updateOldfield.getText().equals(temp)){

                ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());
                DbConnect DbConnect = new DbConnect();
                Connection connection = DbConnect.getConnection();

                    String query = "UPDATE USERS_TABLE SET firstname= ?, lastname= ?, email= ?,gender= ?, password=? WHERE id='" + ml.getId() + "'";
                    PreparedStatement ps = connection.prepareStatement(query);

                    ps.setString(1, updateFNamefield.getText());
                    ps.setString(2, updateLNamefield.getText());
                    ps.setString(3, updateEmailfield.getText());
                    ps.setString(4, updateGenderfield.getText());
                    ps.setString(5, npass);


                    Alert alertC = new Alert(Alert.AlertType.CONFIRMATION);
                    alertC.setTitle("Confirmation Dialog");
                    alertC.setHeaderText(null);
                    alertC.setContentText("Are you sure you want to edit the data?");
                    Optional<ButtonType> action = alertC.showAndWait();
                    if (action.get() == ButtonType.OK) {
                        ps.execute();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("User updated successfully.");
                        alert.setHeaderText(null);
                        alert.setContentText("The User " + updateFNamefield.getText() + " " + updateLNamefield.getText() + " has updated their data!");
                        alert.showAndWait();


                        ps.close();
                        RefreshTable();

                        clearfields();
                    }
                }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadCellTable();
    }

    private void clearfields(){

        updateFNamefield.clear();
        updateLNamefield.clear();
        updateEmailfield.clear();
        updateGenderfield.clear();
        updateNewfield.clear();
        updateOldfield.clear();
    }



    private void DeleteUserDB() throws SQLException {
        ModelTable ml = table.getItems().get(table.getSelectionModel().getSelectedIndex());
        DbConnect DbConnect = new DbConnect();
        Connection connection = DbConnect.getConnection();

        String query = "DELETE FROM USERS_TABLE WHERE  id ='"+ml.getId()+"'";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.executeUpdate();

        RefreshTable();


        ps.close();

    }

    private void RefreshTable() {

        oblist.clear();

        Connection connection = DbConnect.getInstance().getConnection();

        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM USERS_TABLE");

            while (resultSet.next()) {

                oblist.add(new ModelTable(resultSet.getString("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getString("gender"),
                        resultSet.getString("role"),
                        resultSet.getString("password")));


            }

        } catch (SQLException e) {

            e.printStackTrace();
        }



        table.setItems(oblist);
    }

    private void passwordAlert() {
        Notifications notificationBuilder = Notifications.create()
                .title ("Error Updating Data!")
                .text ("Please make sure you to all the text fields are correct!")
                .graphic(null)
                .hideAfter(Duration.seconds(5))
                .position(Pos.CENTER)
                .onAction(new EventHandler<javafx.event.ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        System.out.println("Error Password");
                    }
                });
        notificationBuilder.showConfirm();
    }

    private boolean validateMail() {

        Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");

        Matcher m =  p.matcher(updateEmailfield.getText());

        if (m.find() && m.group().equals(updateEmailfield.getText())){
            return true;



        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Invalid Mail Detected!");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email!");
            alert.showAndWait();

            return false;
        }

    }


}



