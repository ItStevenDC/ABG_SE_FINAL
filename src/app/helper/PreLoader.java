package app.helper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.controller.DocumentController;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PreLoader extends Preloader {

    private Stage preloaderStage;
    private Scene scene;

    public PreLoader() {

    }


    @Override
    public void init() throws Exception {

        Parent root1 = FXMLLoader.load(getClass().getResource("splashScreen.fxml"));
        scene = new Scene(root1);
        //scene = new Scene(root1, 700, 400, Color.TRANSPARENT);
        //root1.setStyle("-fx-background-color: transparent;");

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.preloaderStage = primaryStage;



        // preloaderStage.initStyle(StageStyle.TRANSPARENT);

        // Set preloader scene and show stage.
        preloaderStage.setScene(scene);
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.show();



    }

    @Override
    public void handleApplicationNotification(Preloader.PreloaderNotification info) {

        if (info instanceof ProgressNotification) {
            DocumentController.label.setText("Loading "+((ProgressNotification) info).getProgress()*100 + "%");
            System.out.println("Value@ :" + ((ProgressNotification) info).getProgress());
            DocumentController.statProgressBar.setProgress(((ProgressNotification) info).getProgress());
        }



    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification info) {

        StateChangeNotification.Type type = info.getType();
        switch (type) {

            case BEFORE_START:
                // Called after MyApplication#init and before MyApplication#start is called.
                System.out.println("BEFORE_START");
                preloaderStage.hide();
                break;
        }


    }

}
