package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/UserStart.fxml"));
        java.net.URL url = ClassLoader.getSystemResource("app/assets/168611.png");
        Image icon = new Image(getClass().getResourceAsStream("/app/assets/168611.png"));
        Scene scene = new Scene(root);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Arterial Blood Gas Information Reader");

        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
