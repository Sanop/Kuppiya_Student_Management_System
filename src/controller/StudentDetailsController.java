package controller;

import com.jfoenix.controls.*;
import db.DBConnection;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tm.BatchTM;
import tm.StudentDetailsTM;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @since : 5/15/21
 **/

public class StudentDetailsController {
    public Label lblSid;
    public JFXTextField txtName;
    public JFXTextField txtNic;
    public JFXTextField txtInstitute;
    public JFXTextField txtPayment;
    public ToggleGroup payment;
    public JFXRadioButton rdbPending;
    public JFXRadioButton rdbPaid;
    public JFXComboBox<BatchTM> cmbBatch;
    public JFXButton btnSave;

    public static Connection connection;
    public JFXTextField txtContact;
    public TableView<StudentDetailsTM> tblStudentDetails;
    public JFXListView<StudentDetailsTM> lstPending;
    public AnchorPane root;
    public Label lblAllRegisteredStudent;
    public JFXComboBox<BatchTM> cmbBatch2;
    public Label lblBatchWiseStudent;

    public void initialize(){

        // to fade transition
        FadeTransition ft = new FadeTransition();
        ft.setDuration(Duration.millis(2000));
        ft.setNode(root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        //boolean equals = buttonType.get().equals(ButtonType.YES);

        setDisable(true);
        createDBConnection();
        loadComboBox();
        loadTable();
        loadList();
        setAllRegisteredStudentCount();
        

        tblStudentDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudentDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudentDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudentDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblStudentDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("institute"));
        tblStudentDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("batch"));
        tblStudentDetails.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("payment"));
        tblStudentDetails.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("label"));

        cmbBatch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BatchTM>() {
            @Override
            public void changed(ObservableValue<? extends BatchTM> observable, BatchTM oldValue, BatchTM newValue) {
                BatchTM selectedItem = cmbBatch.getSelectionModel().getSelectedItem();

                if(selectedItem == null){
                    return;
                }
                autoGenerateID(selectedItem.getBatchTitle());

                setDisable(false);
                txtName.requestFocus();
            }
        });

        cmbBatch2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BatchTM>() {
            @Override
            public void changed(ObservableValue<? extends BatchTM> observable, BatchTM oldValue, BatchTM newValue) {
                BatchTM selectedItem = cmbBatch2.getSelectionModel().getSelectedItem();

                if(selectedItem == null){
                    return;
                }

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("select id from student_details where batch = ?");

                    preparedStatement.setObject(1,selectedItem.getBatchTitle());

                    ResultSet resultSet = preparedStatement.executeQuery();

                    int count = 0;

                    while(resultSet.next()){
                        count++;
                    }

                    lblBatchWiseStudent.setText(count+"");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        tblStudentDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentDetailsTM>() {
            @Override
            public void changed(ObservableValue<? extends StudentDetailsTM> observable, StudentDetailsTM oldValue, StudentDetailsTM newValue) {
                StudentDetailsTM selectedItem = tblStudentDetails.getSelectionModel().getSelectedItem();

                if(selectedItem == null){
                    return;
                }



                String label = selectedItem.getLabel();

                if(label.equals("Paid")){
                    rdbPaid.setSelected(true);
                }else{
                    rdbPending.setSelected(true);
                }

                setDisable(false);

                lblSid.setText(selectedItem.getId());
                txtName.setText(selectedItem.getName());
                txtContact.setText(selectedItem.getContact());
                txtNic.setText(selectedItem.getNic());
                txtInstitute.setText(selectedItem.getInstitute());
                txtPayment.setText(selectedItem.getPayment() + "");


                btnSave.setText("Update");

            }
        });

        lstPending.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentDetailsTM>() {
            @Override
            public void changed(ObservableValue<? extends StudentDetailsTM> observable, StudentDetailsTM oldValue, StudentDetailsTM newValue) {
                StudentDetailsTM selectedItem = lstPending.getSelectionModel().getSelectedItem();

                if(selectedItem == null){
                    return;
                }



                String label = selectedItem.getLabel();

                if(label.equals("Paid")){
                    rdbPaid.setSelected(true);
                }else{
                    rdbPending.setSelected(true);
                }

                setDisable(false);

                lblSid.setText(selectedItem.getId());
                txtName.setText(selectedItem.getName());
                txtContact.setText(selectedItem.getContact());
                txtNic.setText(selectedItem.getNic());
                txtInstitute.setText(selectedItem.getInstitute());
                txtPayment.setText(selectedItem.getPayment() + "");


                btnSave.setText("Update");
            }
        });
    }

    private void setAllRegisteredStudentCount() {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select id from student_details");

            int count = 0;
            while(resultSet.next()){
                count++;
            }

            lblAllRegisteredStudent.setText(count+"");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadList() {
        ObservableList<StudentDetailsTM> items = lstPending.getItems();

        items.clear();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from student_details where label = ?");
            preparedStatement.setObject(1,"Pending");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                items.add(new StudentDetailsTM(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7),
                        resultSet.getString(8)));
            }

            lstPending.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void loadTable() {
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from student_details");

            ObservableList<StudentDetailsTM> items = tblStudentDetails.getItems();

            items.clear();

            while(resultSet.next()){
                items.add(new StudentDetailsTM(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7),
                        resultSet.getString(8)));
            }

            tblStudentDetails.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setDisable(boolean flag) {
        txtName.setDisable(flag);
        txtName.clear();
        txtContact.setDisable(flag);
        txtContact.clear();
        txtNic.setDisable(flag);
        txtNic.clear();
        txtInstitute.setDisable(flag);
        txtInstitute.clear();
        txtPayment.setDisable(flag);
        txtPayment.clear();

        btnSave.setDisable(flag);
        btnSave.setText("Save");

        cmbBatch.requestFocus();

    }

    private void autoGenerateID(String batchTitle) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from student_details group by id order by id desc limit 1");

            if(resultSet.next()){
                String oldId = resultSet.getString(1);
                int newID = Integer.parseInt(oldId.substring(11, oldId.length())) + 1;

                if(newID < 10){
                    lblSid.setText(batchTitle+"Stu00"+newID);
                }else if(newID < 100){
                    lblSid.setText(batchTitle+"Stu0"+newID);
                }else{
                    lblSid.setText(batchTitle+"Stu"+newID);
                }
            }else{
                lblSid.setText(batchTitle+"Stu001");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void createDBConnection() {
        connection = DBConnection.getInstance().getConnection();
    }

    private void loadComboBox() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from batch");

            ObservableList<BatchTM> items = cmbBatch.getItems();
            ObservableList<BatchTM> items2 = cmbBatch2.getItems();
            items.clear();
            items2.clear();
            while (resultSet.next()){
                BatchTM batchTM = new BatchTM(resultSet.getString(1),
                        resultSet.getDate(2),
                        resultSet.getDate(3));
                
                items.add(batchTM);
                items2.add(batchTM);
            }
            
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String id = lblSid.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String nic = txtNic.getText();
        String institute = txtInstitute.getText();
        String paymentValue = txtPayment.getText();

        RadioButton radioButton = (RadioButton) payment.getSelectedToggle();
        String label = radioButton.getText();

        if(btnSave.getText().trim().equals("Save")){
            String batchTitle = cmbBatch.getSelectionModel().getSelectedItem().getBatchTitle();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into student_details values(?,?,?,?,?,?,?,?)");
                preparedStatement.setObject(1,id);
                preparedStatement.setObject(2,name);
                preparedStatement.setObject(3,contact);
                preparedStatement.setObject(4,nic);
                preparedStatement.setObject(5,institute);
                preparedStatement.setObject(6,batchTitle);
                preparedStatement.setObject(7,Integer.parseInt(paymentValue));
                preparedStatement.setObject(8,label);

                int i = preparedStatement.executeUpdate();

                if(i != 0 ){
                    new Alert(Alert.AlertType.CONFIRMATION,"Successfully Added......").showAndWait();
                    txtName.clear();
                    txtContact.clear();
                    txtPayment.clear();
                    txtInstitute.clear();
                    txtNic.clear();
                    lblSid.setText("");

                    cmbBatch.getSelectionModel().clearSelection();
                    setDisable(true);

                }
                setAllRegisteredStudentCount();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("update student_details set name = ?,contact = ?, nic = ?,institute = ?,payment = ?,label = ? where id = ?");
                preparedStatement.setObject(1,name);
                preparedStatement.setObject(2,contact);
                preparedStatement.setObject(3,nic);
                preparedStatement.setObject(4,institute);
                preparedStatement.setObject(5,Integer.parseInt(paymentValue));
                preparedStatement.setObject(6,label);
                preparedStatement.setObject(7,id);

                int i = preparedStatement.executeUpdate();

                if( i != 0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Updated").showAndWait();
                }

                txtName.clear();
                txtContact.clear();
                txtPayment.clear();
                txtInstitute.clear();
                txtNic.clear();
                lblSid.setText("");

                setDisable(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        loadTable();
        loadList();
    }

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/DashBoardForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dash Board");
        primaryStage.centerOnScreen();
    }
}
