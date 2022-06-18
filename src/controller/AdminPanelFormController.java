package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.BatchTitleTM;

import java.io.IOException;
import java.sql.*;

/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @Since : 6/22/21
 **/
public class AdminPanelFormController {
    public JFXTextField txtTitle;
    public JFXDatePicker dtpStartingDate;
    public JFXDatePicker dtpEndDate;
    public AnchorPane root;
    public ListView<BatchTitleTM> lstTitles;


    public void initialize(){
        loadList();
    }

    public void btnSubmitOnAction(ActionEvent actionEvent) {

        String title = txtTitle.getText();

        String startingDate = dtpStartingDate.getValue().toString();

        String endDate = dtpEndDate.getValue().toString();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into batch values(?,?,?)");

            preparedStatement.setObject(1,title);
            preparedStatement.setObject(2,startingDate);
            preparedStatement.setObject(3,endDate);

            int i = preparedStatement.executeUpdate();

            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Success").showAndWait();
            }else {
                new Alert(Alert.AlertType.ERROR,"Error").showAndWait();
            }

            txtTitle.clear();
            dtpEndDate.setValue(null);
            dtpStartingDate.setValue(null);

            loadList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void loadList(){

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select batchTitle from batch");

            ObservableList<BatchTitleTM> items = lstTitles.getItems();

            items.clear();

            while(resultSet.next()){

                items.add(new BatchTitleTM(resultSet.getString(1)));

            }

            lstTitles.refresh();
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
}
