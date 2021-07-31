package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The entry point of the application. Run the <code>main()</code> method to start the application.
 *
 * @author Samuel A. Kosasih
 */
public class Main extends Application {

    /**
     * The main entry point for this JavaFX application.
     *
     * @param stage the primary stage for this application, onto which the application scene can be set.
     *
     * @see Application
     * @see Stage
     * @see Scene
     * @see Parent
     * @see FXMLLoader
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        stage.setTitle("ReacXion");
        stage.setScene(new Scene(root, 500, 500));
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        stage.setOnHidden(windowEvent -> controller.handleShutDown());
        stage.show();
    }

    /**
     * Launches the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
