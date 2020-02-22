import Controller.Controller;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends  Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Runway Re-Declaration");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        loader.setControllerFactory(c ->
                new Controller()
        );

        Parent root = loader.load();
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class,args);
    }
}
