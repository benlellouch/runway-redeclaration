package Controller;

import Model.Airport;
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

public class AirportDefinitionController {

    private static AirportDefinitionController SINGLE_INSTANCE = null;
    // Injected Parameters for Airport Definition Window
    @FXML
    private TextField airportName;
    @FXML
    private Button airportDoneButton;

    private ObservableList<Airport> airportObservableList;


    /**
     * Private Constructor for this Class as it is a singleton
     */
    private AirportDefinitionController()
    {
        airportObservableList = FXCollections.observableArrayList();
    }

    /**
     * @return Return's the unique instance of this class
     */
    public static AirportDefinitionController getInstance()
    {
        if (SINGLE_INSTANCE == null) {
            synchronized(AirportDefinitionController.class)
            {
                SINGLE_INSTANCE = new AirportDefinitionController();
            }
        }

        return SINGLE_INSTANCE;
    }

    /**
     * Reads the Textfields in AirportDefinition.fxml,
     * creates a new Airport and adds it the
     * list of Airports stored in this Controller
     */
    @FXML
    private void defineAirport()
    {
        Notifications airportAddedNotification;
        String newAirportName = airportName.getText().trim().replaceAll("\\s", "");

        if(!newAirportName.isEmpty())
        {   airportObservableList.add(new Airport(newAirportName));
            if(!airportObservableList.isEmpty()){
                for (int i=0;i<airportObservableList.size()-1;i++){
                    if(airportObservableList.get(i).getName().equalsIgnoreCase(newAirportName)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Airport already exists");
                        alert.showAndWait();
                        airportObservableList.remove(airportObservableList.size()-1);
                    }
                }
                Stage stage = (Stage) airportDoneButton.getScene().getWindow();
                stage.close();
                airportAddedNotification =
                        Notifications.create()
                                .title("Airport Added")
                                .text("Airport: " + newAirportName + " has been added")
                                .hideAfter(Duration.seconds(3))
                                .graphic(new ImageView(Controller.tick))
                                .position(Pos.BOTTOM_RIGHT);
                airportAddedNotification.show();
                Controller.notificationsString.append("Airport: ").append(newAirportName).append(" has been added").append(" (" + Time.valueOf(LocalTime.now()) + ")").append("\n");
                synchronized (Controller.notificationsString)
                {
                    Controller.notificationsString.notify();
                }
            }else{
                airportObservableList.add(new Airport(newAirportName));
                Stage stage = (Stage) airportDoneButton.getScene().getWindow();
                stage.close();

            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Please fill all input fields");

            alert.showAndWait();
        }


    }

    /**
     * @return the list containing all the defined airports
     */
    public ObservableList<Airport> getAirportObservableList() {
        return airportObservableList;
    }

    /**
     * Used for TestFX testing
     */
    public void cleanUp () { airportObservableList.clear(); }
}
