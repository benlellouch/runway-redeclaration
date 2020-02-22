package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Benjamin Lellouch
 * The Controller will handle interactions between the back-end contained in the Model
 * package and the front-end View and Resources packages.
 */

public class Controller implements Initializable {

    // Injected Parameters for Airport Definition Window
    @FXML
    private TextField airportName;
    @FXML
    private Button airportDoneButton;

    // Injected Parameters for Obstacle Definition Window
    @FXML
    private TextField obstacleName;
    @FXML
    private TextField obstacleHeight;
    @FXML
    private Button obstacleDoneButton;

    // Injected Parameters for Runway Definition Window
    @FXML
    private TextField runwayDesignatorLeft;
    @FXML
    private TextField runwayDesignatorRight;
    @FXML
    private TextField todaLeft;
    @FXML
    private TextField todaRight;
    @FXML
    private TextField toraLeft;
    @FXML
    private TextField toraRight;
    @FXML
    private TextField ldaLeft;
    @FXML
    private TextField ldaRight;
    @FXML
    private TextField asdaLeft;
    @FXML
    private TextField asdaRight;
    @FXML
    private Button runwayDoneButton;


    /**
     * These are Dummy Airport Lists
     */
    //TODO remove
    @FXML
    private ComboBox<Airport> airports;
    private ObservableList<Airport> airportObservableList;




    /**
     *  Controller Constructor
     */
    public Controller()
    {
        airports = new ComboBox<>();
        airportObservableList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        airports.setItems(airportObservableList);
    }

    /**
     *  Handles the opening of the Airport Definition window when the
     *  "Define new airport" menu item in the menu bar is pressed.
     */
    @FXML
    private void openAirportDefinition()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AirportDefinition.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setScene(definitionScene);
            definitionStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *  Handles the opening of the Runway Definition window when the
     *  "Define new runway" menu item in the menu bar is pressed.
     */
    @FXML
    private void openRunwayDefinition()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RunwayDefinition.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setScene(definitionScene);
            definitionStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *  Handles the opening of the Obstacle Definition window when the
     *  "Define new obstacle" menu item in the menu bar is pressed.
     */
    @FXML
    private void openObstacleDefinition()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObstacleDefinition.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setScene(definitionScene);
            definitionStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Reads the Textfields in AirportDefinition.fxml,
     * creates a new Airport and adds it the
     * list of Airports stored in this Controller
     */
    @FXML
    private void defineAirport()
    {
        //TODO create new Airport and add it to a List of Airports
        //TODO add Error pop-up when fields are empty
        String newAirportName = airportName.getText().replaceAll("\\s", "");
        if(!newAirportName.isEmpty())
        {
            Stage stage = (Stage) airportDoneButton.getScene().getWindow();
            stage.close();
        }
        else
        {
            System.out.println("Please input a name for the Airport");
        }
    }

    /**
     * Reads the Textfields and ComboBox in RunwayDefinition.fxml
     *  and creates a new Runway and adds it to its
     *  respective Airport
     */
    @FXML
    private void defineRunway()
    {
        //TODO create new Runway and add them to their Respective Airport
        //TODO add Error pop-up when fields are empty
        Stage stage = (Stage) runwayDoneButton.getScene().getWindow();
        stage.close();
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
        Stage stage = (Stage) obstacleDoneButton.getScene().getWindow();
        stage.close();
    }



}

/**
 * This is a dummy Airport class
 */
//TODO remove class when Airport Model is Defined
class Airport
{
    String name;

    public Airport(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
