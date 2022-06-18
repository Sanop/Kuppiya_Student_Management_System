/**
 * @author : K.S.P.D De Silva <sanodeemantha@gmail.com>
 * @since : 5/15/21
 **/
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("view/DashBoardForm.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Image img = new Image("images/icon/kuppiya.jpg");
        primaryStage.getIcons().add(img);

        primaryStage.setTitle("Dash Board");
        primaryStage.centerOnScreen();
        primaryStage.show();




//        Parent root = FXMLLoader.load(this.getClass().getResource("/view/Customer_Form.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) this.root.getScene().getWindow();
//        stage.setScene(scene);
//        stage.setTitle("Customer Form");
//        stage.centerOnScreen();
    }
}