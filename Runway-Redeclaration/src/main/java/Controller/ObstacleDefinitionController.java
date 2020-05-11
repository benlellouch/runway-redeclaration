package Controller;

import Model.Obstacle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.Time;
import java.time.LocalTime;

public class ObstacleDefinitionController {

private static ObstacleDefinitionController SINGLE_INSTANCE = null;

    // Injected Parameters for Obstacle Definition Window
    @FXML
    private TextField obstacleName;
    @FXML
    private TextField obstacleHeight;
    @FXML
    private Button obstacleDoneButton;

    private ObservableList<Obstacle> obstacles;


    private ObstacleDefinitionController()
    {
        obstacles = FXCollections.observableArrayList();
        populateObstacleList();
    }

public static ObstacleDefinitionController getInstance()
{
    if (SINGLE_INSTANCE == null) {
        synchronized (ObstacleDefinitionController.class)
        {
            SINGLE_INSTANCE = new ObstacleDefinitionController();
        }
    }

    return SINGLE_INSTANCE;
}

    /**
     * Fills in the obstacle list with a few predefined obstacles and their heights
     */
    private void populateObstacleList()
    {
        obstacles.add(new Obstacle("Broken Down Rover Vehicle",2,1));
        obstacles.add(new Obstacle("Barricades",1,1));
        obstacles.add(new Obstacle("Lighting Pole",5,1));
        obstacles.add(new Obstacle("Broken Down Aircraft",19,1));
    }

    /**
     * Reads the Textfields in ObstacleDefinition.fxml,
     * creates a new Obstacle and adds it to a list
     * of Obstacles stored in this Controller
     */
    @FXML
    private void defineObstacle()
    {
        //TODO create new Obstacle and add it to a List of Obstacles
        //TODO add Error pop-up when fields are empty
        try {
            Notifications obstacleDefinedNotification;
            String newObstacleName = obstacleName.getText().trim();

            if (newObstacleName.isEmpty() || obstacleHeight.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Input field empty");
                alert.setContentText("Please fill in all input fields");
                alert.showAndWait();

            } else if (Integer.parseInt(obstacleHeight.getText().trim()) < 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please put a number greater than zero for Height");
                alert.showAndWait();

            } else if (Integer.parseInt(obstacleHeight.getText().trim()) > 100) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please put a number less than one hundred for Height");
                alert.showAndWait();
            }
            else {
                int newObstacleHeight = Integer.parseInt(obstacleHeight.getText().trim());
                Obstacle newObstacleCreated = new Obstacle(newObstacleName,newObstacleHeight,1);
                int x = obstacles.size();
                obstacles.add(newObstacleCreated);

                for (int i=0;i<obstacles.size()-1;i++){
                    if(obstacles.get(i).getName().equalsIgnoreCase(newObstacleName)&&obstacles.get(i).getHeight()==newObstacleHeight){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Duplicate alert: Obstacle has not been added");
                        alert.showAndWait();
                        obstacles.remove(obstacles.size()-1);
                    }
                }
                //obstacles.add(newObstacleCreated);
                if(x<obstacles.size()){
                Stage stage = (Stage) obstacleDoneButton.getScene().getWindow();
                stage.close();
                obstacleDefinedNotification =
                        Notifications.create()
                                .title("Obstacle Added")
                                .text("Obstacle: " + newObstacleCreated.getName() + " " + newObstacleCreated.getHeight() + "m has been added")
                                .hideAfter(Duration.seconds(3))
                                .graphic(new ImageView(Controller.tick))
                                .position(Pos.BOTTOM_RIGHT);
                obstacleDefinedNotification.show();
                Controller.notificationsString.append("Obstacle: ").append(newObstacleCreated.getName()).append(" has been added").append(" (").append(Time.valueOf(LocalTime.now())).append(")").append("\n");
                synchronized (Controller.notificationsString)
                {
                    Controller.notificationsString.notify();
                }
            }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please input a number for height");
            alert.showAndWait();
        }
    }

    public ObservableList<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * Used for TestFX testing
     */
    public void cleanUp(){obstacles.clear(); populateObstacleList();}
}
