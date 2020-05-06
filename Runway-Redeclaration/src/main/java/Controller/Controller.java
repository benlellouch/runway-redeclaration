package Controller;

import Model.*;
import XMLParsing.ModelFactory;
import View.SideRunwayView;
import View.TopRunwayView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * @author Benjamin Lellouch
 * The Controller will handle interactions between the back-end contained in the Model
 * package and the front-end View and Resources packages.
 */

public class Controller implements Initializable {


    //A singleton controller is assigned to every definition window
     AirportDefinitionController airportDefinitionController = AirportDefinitionController.getInstance();
     ObstacleDefinitionController obstacleDefinitionController= ObstacleDefinitionController.getInstance();
     RunwayDefinitionController runwayDefinitionController = RunwayDefinitionController.getInstance(this);


    @FXML
    private TextField leftThresholdDistance;
    @FXML
    private TextField rightThresholdDistance;
    @FXML
    private TextField centreLineDistance;
    @FXML
    private Text revisedRunwayText;
    @FXML
    private Text oldRunwayText;
    @FXML
    private Label calculationBreakdown;
    @FXML
    private Text notificationsLog;
    @FXML
    private ComboBox<Airport> airportMainBox;
    @FXML
    private ComboBox<Obstacle> obstacleBox;
    @FXML
    private ComboBox<Runway> runwayBox;
    @FXML
    private ComboBox<LogicalRunway> logicalRunwayBox;
    @FXML
    private ComboBox<String> centreLinePositionBox;
    @FXML
    private Button noAirportDefinedOK;
    @FXML
    private CheckBox orientationCheckBox;

    private RevisedRunway revisedRunwayOnDisplay;

    //TODO make the views seperate fxml files so that we don't need this many containers
    @FXML
    private FlowPane topDownViewContainer, sideOnViewContainer, simTopDownViewContainer, simSideOnViewContainer;

    private ObservableList<String> centreLinePosition;
    protected static final StringBuilder notificationsString = new StringBuilder();
    protected static Image tick = new Image("icons/smalltick.png", true);


    /**
     *  Controller Constructor
     */
    public Controller()
    {
        obstacleBox = new ComboBox<>();
        airportMainBox = new ComboBox<>();
        runwayBox = new ComboBox<>();
        logicalRunwayBox = new ComboBox<>();
        centreLinePositionBox = new ComboBox<>();
        centreLinePosition = generatecentreLinePosition();
        checkForAirports();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        airportMainBox.setItems(airportDefinitionController.getAirportObservableList());
        obstacleBox.setItems(obstacleDefinitionController.getObstacles());
        centreLinePositionBox.setItems(centreLinePosition);
        runwayBox.setDisable(true);
        logicalRunwayBox.setDisable(true);
        NotificationThread notificationThread = new NotificationThread();
        Thread thread = new Thread(notificationThread);
        thread.setDaemon(true);
        thread.start();
    }

    class NotificationThread implements Runnable
    {

        @Override
        public void run() {
            while (true)
            {
                try {
                    synchronized (notificationsString)
                    {
                        notificationsString.wait();
                        notificationsLog.setText(notificationsString.toString());
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
            Stage stage;
            if ((stage = (Stage) noAirportDefinedOK.getScene().getWindow()) != null)
            {
                stage.close();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AirportDefinition.fxml"));
            loader.setController(airportDefinitionController);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            definitionStage.setTitle("Define New Airport");
            definitionStage.getIcons().add(new Image("icons/icon.png"));
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
            loader.setController(runwayDefinitionController);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setScene(definitionScene);
            definitionStage.setTitle("Define New Runway");
            definitionStage.getIcons().add(new Image("icons/icon.png"));
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
            loader.setController(obstacleDefinitionController);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setTitle("Define New Obstacle");
            definitionStage.getIcons().add(new Image("icons/icon.png"));
            definitionStage.setScene(definitionScene);
            definitionStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @FXML
    private void openImportFile(){

        ObservableList<Airport> airportObservableList = airportDefinitionController.getAirportObservableList();
        ObservableList<Obstacle> obstacles = obstacleDefinitionController.getObstacles();

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            System.out.println(file.getAbsolutePath());
            ModelFactory mf = new ModelFactory(file);

            for(Airport a : mf.getAirports()){
                boolean duplicate = false;
                for(Airport as : airportObservableList){
                    if (a.getName().equals(as.getName())) {
                        duplicate = true;
                        break;
                    }
                }
                if(!duplicate)
                {
                    airportObservableList.add(a);
                    notificationsString.append("Airport: ").append(a.getName()).append(" has been added").append(" (").append(Time.valueOf(LocalTime.now())).append(")").append("\n");
                    synchronized (Controller.notificationsString)
                    {
                        notificationsString.notify();
                    }
                }

            }

            for(Obstacle o : mf.getObstacles()){
                boolean dup = false;
                for(Obstacle os : obstacles){
                    if(o.getName().equals(os.getName()) && o.getHeight()==os.getHeight())
                        dup = true;
                }
                if(!dup)
                {
                    obstacles.add(o);
                    notificationsString.append("Obstacle: ").append(o.getName()).append(" has been added").append(" (").append(Time.valueOf(LocalTime.now())).append(")").append("\n");
                    synchronized (Controller.notificationsString)
                    {
                        notificationsString.notify();
                    }
                }
            }
            
        }
    }


    @FXML
    protected void updateRunwayBox()
    {
        Airport airport = airportMainBox.getValue();

        if(airport != null)
        {
            ObservableList<Runway> runwayObservableList = FXCollections.observableArrayList();
            runwayObservableList.addAll(airport.getRunways());
            runwayBox.setItems(runwayObservableList);
            runwayBox.setDisable(false);
        }
    }

    @FXML
    private void updateLogicalRunwayBox()
    {
        Runway runway = runwayBox.getValue();

        if(runway != null)
        {
            ObservableList<LogicalRunway> logicalRunwayObservableList = FXCollections.observableArrayList();
            logicalRunwayObservableList.addAll(runway.getLogicalRunway1(), runway.getLogicalRunway2());
            logicalRunwayBox.setItems(logicalRunwayObservableList);
            logicalRunwayBox.setDisable(false);
        }
    }

    @FXML
    private void calculateRevisedRunway()
    {
        try{
        Runway runwayToRevise = runwayBox.getValue();
        Obstacle obstacleOnRunway = obstacleBox.getValue();
        Notifications revisedNotification;

        if(airportMainBox.getValue().toString().trim().equalsIgnoreCase("Airport")
                || obstacleBox.getValue().toString().trim().equalsIgnoreCase("Obstacle")
                || runwayBox.getValue().toString().trim().equalsIgnoreCase("Runway")
                || logicalRunwayBox.getValue().toString().trim().equalsIgnoreCase("Logical Runway")
                || leftThresholdDistance.getText().trim().isEmpty()
                || rightThresholdDistance.getText().trim().isEmpty()
                || centreLinePositionBox.getValue().trim().equalsIgnoreCase("N/S")
                || centreLineDistance.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please fill in all inputs");
            alert.showAndWait();
        }
        else{
        int leftTHRDistance = Integer.parseInt(leftThresholdDistance.getText().trim());
        int rightTHRDistance = Integer.parseInt(rightThresholdDistance.getText().trim());
        int centerLineDistance = Integer.parseInt(centreLineDistance.getText().trim());
        if(centerLineDistance>=0){
        Position positionOfObstacle = new Position((centreLinePositionBox.getValue().equals("N") ? centerLineDistance : -centerLineDistance),leftTHRDistance,rightTHRDistance);
        RevisedRunway revisedRunway = new RevisedRunway(runwayToRevise,obstacleOnRunway,positionOfObstacle);
        revisedRunwayText.setText(revisedRunway.getResults());
        oldRunwayText.setText(runwayToRevise.getResults());
        calculationBreakdown.setText(revisedRunway.getCalcBreakdown());
            revisedNotification =
                    Notifications.create()
                            .title("Runway Revised")
                            .text("Runway: " + runwayToRevise.getName() + " at Airport: " + airportMainBox.getValue().toString().trim() + " has been revised")
                            .hideAfter(Duration.seconds(3))
                            .graphic(new ImageView(tick))
                            .position(Pos.BOTTOM_RIGHT);
            revisedNotification.show();

//            notificationsString.append("Runway: ").append(runwayToRevise.getName()).append(" at Airport: " + airportMainBox.getValue().toString().trim() + " has been revised").append(" (" + Time.valueOf(LocalTime.now()) + ")").append("\n");
//            notificationsLog.setText(notificationsString.toString());
        revisedRunwayOnDisplay = revisedRunway;
        drawRunway(revisedRunway,obstacleOnRunway,positionOfObstacle);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please input positive numbers for center line distances");
            alert.showAndWait();
        }
        }
    }catch (NullPointerException e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Please fill in all inputs");
        alert.showAndWait();
    }catch (NumberFormatException ex){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Please input numbers for distances");
        alert.showAndWait();
    }


    }

    private void checkForAirports()
    {
        if(airportDefinitionController.getAirportObservableList().isEmpty())
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/noAirportDefinedPopUp.fxml"));
                loader.setController(this);
                Parent root = loader.load();
                Stage definitionStage = new Stage();
                Scene definitionScene = new Scene(root);
                definitionStage.setTitle("No Airports Defined");
                definitionStage.getIcons().add(new Image("icons/icon.png"));
                definitionStage.setScene(definitionScene);
                definitionStage.requestFocus();
                definitionStage.show();
                definitionStage.setAlwaysOnTop(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void updateLogicalRunwayView()
    {
        try {
            if (revisedRunwayOnDisplay.getName().equals(runwayBox.getValue().getName()) && logicalRunwayBox.getValue() != null) {
                calculateRevisedRunway();
            }
        }
        catch (NullPointerException e)
        {
            System.out.println("No runway currently displayed.");
        }

    }

    private ObservableList<String> generatecentreLinePosition()
    {
        return FXCollections.observableArrayList("N","S");
    }

    public void drawRunway(RevisedRunway revisedRunway, Obstacle obstacle, Position position) {
        topDownViewContainer.getChildren().clear();
        sideOnViewContainer.getChildren().clear();
        simSideOnViewContainer.getChildren().clear();
        simTopDownViewContainer.getChildren().clear();


        Runway runway = runwayBox.getSelectionModel().getSelectedItem();
        LogicalRunway originalRunway = logicalRunwayBox.getSelectionModel().getSelectedItem();
        LogicalRunway revisedLRunway = originalRunway.getName().equals(revisedRunway.getLogicalRunway1().getName()) ? revisedRunway.getLogicalRunway1() : revisedRunway.getLogicalRunway2();

        try {
            if (runway == null) {
                if (runwayBox.getItems().size() > 0) {
                    runway = runwayBox.getItems().get(0);
                    // Default to the left virtual runway
                    originalRunway = runway.getLogicalRunway1();
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // There is no runway yet
            return;
        }

        if (originalRunway == null) {
            return;
        }

        Pane pane = new Pane();

        String designator_String = originalRunway.getName();
        Integer designator = Integer.parseInt(designator_String.replaceAll("[^\\d.]", ""));

        // Calculate bearing of runway
        double bearing;
        if (designator <= 18) {
            bearing = designator * 10;
        } else {
            bearing = (designator - 18) * 10;
        }

        // Rotate compass accordingly


        // Draw static elements: measuring line, take-off direction, compass

        boolean rotateView = orientationCheckBox.isSelected();

        //TODO  very hacked together needs to change
        TopRunwayView topRunwayView = new TopRunwayView(runway, revisedLRunway, position, obstacle, rotateView);
        topRunwayView.widthProperty().bind(topDownViewContainer.widthProperty());
        topRunwayView.heightProperty().bind(topDownViewContainer.heightProperty());





        // Add everything to top down view tab
        pane.getChildren().addAll(topRunwayView);
        topDownViewContainer.getChildren().add(pane);

        TopRunwayView simTopRunwayView = new TopRunwayView(runway, revisedLRunway, position, obstacle, rotateView);
        simTopRunwayView.widthProperty().bind(simTopDownViewContainer.widthProperty());
        simTopRunwayView.heightProperty().bind(simTopDownViewContainer.heightProperty());
        simTopDownViewContainer.getChildren().add(simTopRunwayView);

        SideRunwayView sideRunwayView = new SideRunwayView(runway, revisedLRunway, position, obstacle);
        sideRunwayView.widthProperty().bind(sideOnViewContainer.widthProperty());
        sideRunwayView.heightProperty().bind(sideOnViewContainer.heightProperty());
        sideOnViewContainer.getChildren().add(sideRunwayView);

        SideRunwayView simSideRunwayView = new SideRunwayView(runway, revisedLRunway, position, obstacle);
        simSideRunwayView.widthProperty().bind(simTopDownViewContainer.widthProperty());
        simSideRunwayView.heightProperty().bind(simTopDownViewContainer.heightProperty());
        simSideOnViewContainer.getChildren().add(simSideRunwayView);



        // Draw side on view


        if (position != null) {
            topRunwayView.renderObstacle();
            sideRunwayView.renderObstacle();
            simSideRunwayView.renderObstacle();
            simTopRunwayView.renderObstacle();
        }

    }

}
