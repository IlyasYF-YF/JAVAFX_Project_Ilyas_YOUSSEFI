package javafxapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.gestion.util.HibernateUtil;

public class JavaFXApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(
            getClass().getResource("produit-view.fxml") 
        );

        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Gestion du Catalogue — Restaurant");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}