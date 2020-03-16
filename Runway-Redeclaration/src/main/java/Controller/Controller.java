package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
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
    private ComboBox<Airport> airports;
    @FXML
    private ComboBox<Airport> airportMainBox;
    @FXML
    private ComboBox<String> runwayDegree;
    @FXML
    private ComboBox<String> runwayPosition;
    @FXML
    private ComboBox<Obstacle> obstacleBox;
    @FXML
    private ComboBox<Runway> runwayBox;
    @FXML
    private ComboBox<LogicalRunway> logicalRunwayBox;
    @FXML
    private ComboBox<String> leftRightBox;
    @FXML
    private Text complementDesignatorText;

    @FXML
    private Button noAirportDefinedOK;


    private ObservableList<Airport> airportObservableList;
    private ObservableList<String> runwayDegreeList;
    private ObservableList <String> runwayPositionList;
    private HashMap<String,String> oppositeDegreeMap;
    private HashMap<String,String> oppositePositionMap;
    private ObservableList<Obstacle> obstacles;
    private ObservableList<String> leftRight;




    /**
     *  Controller Constructor
     */
    public Controller()
    {
        airports = new ComboBox<>();
        runwayDegree = new ComboBox<>();
        runwayPosition = new ComboBox<>();
        obstacleBox = new ComboBox<>();
        airportMainBox = new ComboBox<>();
        leftRightBox = new ComboBox<>();
        airportObservableList = FXCollections.observableArrayList();
        obstacles = FXCollections.observableArrayList();
        populateObstacleList();
        runwayDegreeList = generateDegreeList();
        runwayPositionList = generatePositionList();
        oppositeDegreeMap = generateOppositeDegreeMap();
        oppositePositionMap = generateOppositePositionMap();
        leftRight = generateLeftRight();
        checkForAirports();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        airports.setItems(airportObservableList);
        airportMainBox.setItems(airportObservableList);
        runwayDegree.setItems(runwayDegreeList);
        runwayPosition.setItems(runwayPositionList);
        obstacleBox.setItems(obstacles);
        leftRightBox.setItems(leftRight);
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
            Stage stage = (Stage) noAirportDefinedOK.getScene().getWindow();
            stage.close();

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
        String newAirportName = airportName.getText().replaceAll("\\s", "");
        airportObservableList.add(new Airport(newAirportName));
        if(!newAirportName.isEmpty())
        {   if(!airportObservableList.isEmpty()){
            for (int i=0;i<airportObservableList.size()-1;i++){
                if(airportObservableList.get(i).getName().equalsIgnoreCase(newAirportName)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Airport already exists");
                    alert.showAndWait();
                    airportObservableList.remove(airportObservableList.size()-1);
                }
            }
            //airportObservableList.add(new Airport(newAirportName));
            Stage stage = (Stage) airportDoneButton.getScene().getWindow();
            stage.close();
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
     * Reads the Textfields and ComboBox in RunwayDefinition.fxml
     *  and creates a new Runway and adds it to its
     *  respective Airport
     */
    @FXML
    private void defineRunway()
    {
        //TODO add Error pop-up when fields are empty or malformed
        try {
            if(!(runwayDegree.getValue().trim().equalsIgnoreCase("Degree"))&&!runwayPosition.getValue().trim().equalsIgnoreCase("Position")&&!airports.getValue().toString().trim().equalsIgnoreCase("Choose Airport")&&!todaLeft.getText().isEmpty()&&!todaRight.getText().isEmpty()&&!toraLeft.getText().isEmpty()&&!toraRight.getText().isEmpty()&&!asdaLeft.getText().isEmpty()&&!asdaRight.getText().isEmpty()&&!ldaLeft.getText().isEmpty()&&!ldaRight.getText().isEmpty()){
                if(!(Integer.parseInt(todaLeft.getText())<0)&&!(Integer.parseInt(todaRight.getText())<0)&&!(Integer.parseInt(toraLeft.getText())<0)&&!(Integer.parseInt(toraRight.getText())<0)&&!(Integer.parseInt(asdaLeft.getText())<0)&&!(Integer.parseInt(asdaRight.getText())<0)&&!(Integer.parseInt(ldaLeft.getText())<0)&&!(Integer.parseInt(ldaRight.getText())<0)){
                    Airport airport = airports.getValue();

                    String designatorLeft = runwayDegree.getValue() + runwayPosition.getValue();
                    String designatorRight = complementDesignatorText.getText();

                    int todaLeft = Integer.parseInt(this.todaLeft.getText());
                    int todaRight = Integer.parseInt(this.todaRight.getText());

                    int toraLeft = Integer.parseInt(this.toraLeft.getText());
                    int toraRight = Integer.parseInt(this.toraRight.getText());

                    int asdaLeft = Integer.parseInt(this.asdaLeft.getText());
                    int asdaRight = Integer.parseInt(this.asdaRight.getText());

                    int ldaLeft = Integer.parseInt(this.ldaLeft.getText());
                    int ldaRight = Integer.parseInt(this.ldaRight.getText());
                    if(todaLeft<toraLeft||todaLeft<ldaLeft||todaRight<toraRight||todaRight<ldaRight){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("TODA values have to be greater than or equal to TORA and LDA");
                        alert.showAndWait();
                    }else if (toraLeft<ldaLeft||toraRight<ldaRight){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("TORA has to be greater than or equal to LDA");
                        alert.showAndWait();
                    }else if(asdaLeft<toraLeft||asdaLeft<ldaLeft||asdaRight<toraRight||asdaRight<ldaRight){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("ASDA has to be greater than TORA and LDA");
                        alert.showAndWait();
                    }else {

                        LogicalRunway logicalRunway1 = new LogicalRunway(designatorLeft, toraLeft, todaLeft, asdaLeft, ldaLeft);
                        LogicalRunway logicalRunway2 = new LogicalRunway(designatorRight, toraRight, todaRight, asdaRight, ldaRight);
                        Runway run = new Runway(logicalRunway1, logicalRunway2);
                        airport.addRunway(run);
                        for (int i = 0; i < airport.getRunways().size() - 1; i++) {
                            if (airport.getRunways().get(i).getName().equals(run.getName())) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText("Logical runway duplicate alert");
                                alert.showAndWait();
                                airport.getRunways().remove(run);
                            }
                        }
                        updateRunwayBox();

                        Stage stage = (Stage) runwayDoneButton.getScene().getWindow();
                        stage.close();
                    }
                    }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Please ensure only positive values are used for measurements");
                    alert.showAndWait();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please ensure all inputs have been filled in");
                alert.showAndWait();
            }


        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please ensure all inputs have been filled in");
            alert.showAndWait();
        } catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please ensure only numbers are used as inputs for measurements");
            alert.showAndWait();
        }

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

            } else {
                int newObstacleHeight = Integer.parseInt(obstacleHeight.getText().trim());
                Obstacle newObstacleCreated = new Obstacle(newObstacleName,newObstacleHeight);
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
                Stage stage = (Stage) obstacleDoneButton.getScene().getWindow();
                stage.close();


            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please input a number for height");
            alert.showAndWait();
        }
    }

    /**
     * Fills in the obstacle list with a few predefined obstacles and their heights
     */
    private void populateObstacleList()
    {
        obstacles.add(new Obstacle("Broken Down Rover Vehicle",2));
        obstacles.add(new Obstacle("Barricades",1));
        obstacles.add(new Obstacle("Lighting Pole",5));
        obstacles.add(new Obstacle("Broken Down Aircraft",19));
    }

    /**
     * Dynamically updates the designator of the right runway
     */
    @FXML
    private void updateDesignator()
    {
        String newDegree = getOppositeDegree(runwayDegree.getValue());
        String newPosition =  getOppositePosition(runwayPosition.getValue());

        if (newDegree == null)
        {
            complementDesignatorText.setText(newPosition);
        }
        else if ( newPosition == null)
        {
            complementDesignatorText.setText(newDegree);
        }
        else
        {
            complementDesignatorText.setText( newDegree + newPosition);
        }
    }

    @FXML
    private void updateRunwayBox()
    {
        Airport airport = airportMainBox.getValue();
        if(airport != null)
        {
            ObservableList<Runway> runwayObservableList = FXCollections.observableArrayList();
            runwayObservableList.addAll(airport.getRunways());
            runwayBox.setItems(runwayObservableList);
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
        }
    }

    @FXML
    private void calculateRevisedRunway()
    {   try{
        Runway runwayToRevise = runwayBox.getValue();
        Obstacle obstacleOnRunway = obstacleBox.getValue();

        if(airportMainBox.getValue().toString().trim().equalsIgnoreCase("Airport")||obstacleBox.getValue().toString().trim().equalsIgnoreCase("Obstacle")||runwayBox.getValue().toString().trim().equalsIgnoreCase("Runway")||logicalRunwayBox.getValue().toString().trim().equalsIgnoreCase("Logical Runway")||leftThresholdDistance.getText().isEmpty()||rightThresholdDistance.getText().isEmpty()||leftRightBox.getValue().trim().equalsIgnoreCase("L/R")||centreLineDistance.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please fill in all inputs");
            alert.showAndWait();
        }
        else{
        int leftTHRDistance = Integer.parseInt(leftThresholdDistance.getText());
        int rightTHRDistance = Integer.parseInt(rightThresholdDistance.getText());
        int centerLineDistance = Integer.parseInt(centreLineDistance.getText());
        if(centerLineDistance>=0){
        Position positionOfObstacle = new Position(0,leftTHRDistance,rightTHRDistance);
        RevisedRunway revisedRunway = new RevisedRunway(runwayToRevise,obstacleOnRunway,positionOfObstacle);
        revisedRunwayText.setText(revisedRunway.getResults());
        oldRunwayText.setText(runwayToRevise.getResults());
        calculationBreakdown.setText(revisedRunway.getCalcBreakdown());}
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


    /**
     *
     * @param degree takes the degree of the runway designator
     * @return the opposite degree
     */
    private String getOppositeDegree(String degree)
    {
        return oppositeDegreeMap.get(degree);
    }

    /**
     *
     * @param position takes the position of the runway position
     * @return the opposite position
     */
    private String getOppositePosition(String position)
    {
        return oppositePositionMap.get(position);
    }



    private ObservableList<String> generateDegreeList()
    {
        return FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36");
    }



    private ObservableList<String> generatePositionList()
    {
        return FXCollections.observableArrayList("L","R","C");
    }

    private ObservableList<String> generateLeftRight()
    {
        return FXCollections.observableArrayList("L","R");
    }


    private HashMap<String,String> generateOppositePositionMap()
    {
        HashMap<String,String> oppositeMap = new HashMap<>();
        oppositeMap.put("L","R");
        oppositeMap.put("R", "L");
        oppositeMap.put("C", "C");
        return oppositeMap;
    }


    private HashMap<String,String> generateOppositeDegreeMap()
    {
        HashMap<String,String> oppositeMap = new HashMap<>();

        for (int i = 18; i <= 36; i++)
        {
            int mod = (i % 18 == 0) ? 18 : i % 18;
            oppositeMap.put((mod < 10) ? "0" + mod : String.valueOf(mod), String.valueOf(i));
            oppositeMap.put(String.valueOf(i), (mod < 10) ? "0" + mod : String.valueOf(mod));
        }

        return oppositeMap;
    }

    private void checkForAirports()
    {
        if(airportObservableList.isEmpty())
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/noAirportDefinedPopUp.fxml"));
                loader.setController(this);
                Parent root = loader.load();
                Stage definitionStage = new Stage();
                Scene definitionScene = new Scene(root);
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

}
