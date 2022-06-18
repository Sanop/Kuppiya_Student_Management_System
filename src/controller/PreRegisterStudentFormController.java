package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.BatchTM;
import tm.PreRegisterStudentTM;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @Since : 5/28/21
 * @language : java
 **/
public class PreRegisterStudentFormController {
    public AnchorPane root;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtInstitute;
    public JFXComboBox<BatchTM> cmbBatch;
    public TableView<PreRegisterStudentTM> tblPreRegisterStudent;
    public static Connection connection;
    public JFXTextField txtNic;
    public JFXButton btnSaveAsRegistered;
    public JFXTextField txtPayment;
    public RadioButton rbtnPay;
    public ToggleGroup payment;
    public RadioButton rbtnPending;
    public Label lblCount;

    public void initialize(){
        createDBConnection();
        loadComboBox();
        loadTable();
        setCount();
        txtNic.setVisible(false);
        txtPayment.setVisible(false);
        rbtnPending.setVisible(false);
        rbtnPay.setVisible(false);
        btnSaveAsRegistered.setVisible(false);
        btnSaveAsRegistered.setVisible(false);

        tblPreRegisterStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblPreRegisterStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblPreRegisterStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblPreRegisterStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("institute"));
        tblPreRegisterStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("batch"));
        tblPreRegisterStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        tblPreRegisterStudent.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("checkOut"));
    }

    private void createDBConnection() {
        connection = DBConnection.getInstance().getConnection();
    }

    private void loadComboBox() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from batch");

            ObservableList<BatchTM> items = cmbBatch.getItems();
            items.clear();
            while (resultSet.next()){
                BatchTM batchTM = new BatchTM(resultSet.getString(1),
                        resultSet.getDate(2),
                        resultSet.getDate(3));

                items.add(batchTM);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String contact = txtContact.getText();
        String institute = txtInstitute.getText();
        String batch = cmbBatch.getSelectionModel().getSelectedItem().getBatchTitle();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into pre_registered_student (name,contact,institute,batch) values(?,?,?,?)");
            preparedStatement.setObject(1,name);
            preparedStatement.setObject(2,contact);
            preparedStatement.setObject(3,institute);
            preparedStatement.setObject(4,batch);

            int i = preparedStatement.executeUpdate();

            if(i != 0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"Success...").showAndWait();
                txtName.clear();
                txtContact.clear();
                txtInstitute.clear();
                loadTable();
                setCount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/DashBoardForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dash Board");
        primaryStage.centerOnScreen();
    }

    public void loadTable(){
        ObservableList<PreRegisterStudentTM> items = tblPreRegisterStudent.getItems();
        items.clear();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from pre_registered_student");

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String contact = resultSet.getString(3);
                String institute = resultSet.getString(4);
                String batch = resultSet.getString(5);

                Button btnCheckIn = new Button("Check In");
                Button btnCheckOut = new Button("Check Out");

                btnCheckIn.setStyle("-fx-background-color: #009432 ; -fx-text-fill: white");

                btnCheckOut.setStyle("-fx-background-color: red ; -fx-text-fill: white");
                btnCheckIn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        btnSaveAsRegistered.setVisible(true);
                        txtNic.setVisible(true);
                        txtNic.requestFocus();
                        txtPayment.setVisible(true);
                        rbtnPay.setVisible(true);
                        rbtnPending.setVisible(true);

                        txtName.setDisable(true);
                        txtName.clear();
                        txtContact.setDisable(true);
                        txtContact.clear();
                        txtInstitute.setDisable(true);
                        txtInstitute.clear();
                        cmbBatch.setDisable(true);
                    }
                });

                btnCheckOut.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        deletePreRegisterStudent();
                    }
                });


                items.add(new PreRegisterStudentTM("Temp" + id,name,contact,institute,batch,btnCheckIn,btnCheckOut));
            }

            //G9D5FO46
            tblPreRegisterStudent.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveStudentAsRegisterStudent() {

        String id = null;
        PreRegisterStudentTM selectedItem = tblPreRegisterStudent.getSelectionModel().getSelectedItem();
        String batchTitle = selectedItem.getBatch();


        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from student_details where id like 'Kuppiya17%' order by id desc limit 1");

            if(resultSet.next()){
                String oldId = resultSet.getString(1);
                int newID = Integer.parseInt(oldId.substring(12, oldId.length())) + 1;

                if(newID < 10){
                    id = batchTitle+"Stu00"+newID;
                }else if(newID < 100){
                    id = batchTitle+"Stu0"+newID;
                }else{
                    id = batchTitle+"Stu"+newID;
                }
            }else{
                id = batchTitle+"Stu001";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        String name = selectedItem.getName();
        String contact = selectedItem.getContact();
        String nic = txtNic.getText();
        String institute = selectedItem.getInstitute();
        String payment = txtPayment.getText();
        RadioButton radioButton = (RadioButton) this.payment.getSelectedToggle();
        String label = radioButton.getText();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into student_details values(?,?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,name);
            preparedStatement.setObject(3,contact);
            preparedStatement.setObject(4,nic);
            preparedStatement.setObject(5,institute);
            preparedStatement.setObject(6,batchTitle);
            preparedStatement.setObject(7,Integer.parseInt(payment));
            preparedStatement.setObject(8,label);

            int i = preparedStatement.executeUpdate();

            if(i != 0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"Success... New ID is "+id+"").showAndWait();
                deletePreRegisterStudent();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePreRegisterStudent() {
        PreRegisterStudentTM selectedItem = tblPreRegisterStudent.getSelectionModel().getSelectedItem();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from pre_registered_student where contact = ?");
            preparedStatement.setObject(1,selectedItem.getContact());

            preparedStatement.executeUpdate();

            loadTable();
            setCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtNic.clear(); 
        txtNic.setVisible(false);
        txtPayment.clear();
        txtPayment.setVisible(false);
        rbtnPay.setVisible(false);
        rbtnPending.setVisible(false);
        btnSaveAsRegistered.setVisible(false);
        txtName.setDisable(false);
        txtContact.setDisable(false);
        txtInstitute.setDisable(false);
        cmbBatch.setDisable(false);
    }

    public void btnSaveAsRegisteredOnAction(ActionEvent actionEvent) {
        saveStudentAsRegisterStudent();
    }

    public void btnRemoveAllOnAction(ActionEvent actionEvent) {
        try {

            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete all data in table", ButtonType.YES, ButtonType.NO).showAndWait();

            if(buttonType.get().equals(ButtonType.YES)){
                Statement statement = connection.createStatement();

                statement.executeUpdate("truncate table pre_registered_student");
            }

            loadTable();
            setCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCount(){
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from pre_registered_student");

            int count = 0;

            while (resultSet.next()){
                count++;
            }

            lblCount.setText(count+"");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n");
    }
}
