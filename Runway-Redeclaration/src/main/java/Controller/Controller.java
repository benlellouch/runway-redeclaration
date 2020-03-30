package Controller;

import Model.*;
import View.SideOnView;
import View.TopDownView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    @FXML
    private CheckBox orientationCheckBox;

    private RevisedRunway revisedRunwayOnDisplay;

    //TODO make the views seperate fxml files so that we don't need this many containers
    @FXML
    private FlowPane topDownViewContainer, sideOnViewContainer, simTopDownViewContainer, simSideOnViewContainer;


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

        LogicalRunway lRunway09R = new LogicalRunway("08R",3660,3660,3660,3353);
        LogicalRunway lRunway27L = new LogicalRunway("26L",3660,3660,3660,3660);
        Runway runway09R27L = new Runway(lRunway09R,lRunway27L);

        LogicalRunway lRunway09L = new LogicalRunway("09L", 3902,3902,3902,3595);
        LogicalRunway lRunway27R = new LogicalRunway("27R", 3884,3962,3884,3884);
        Runway runway09L27R = new Runway(lRunway09L,lRunway27R);
        Airport airport = new Airport("heathrow");

        airport.addRunway(runway09L27R);
        airport.addRunway(runway09R27L);

        airportObservableList.add(airport);

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

//    @FXML
//    private void openImportFile(){
//
//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showOpenDialog(null);
//        if(file != null) {
//            System.out.println(file.getAbsolutePath());
//            ModelFactory mf = new ModelFactory(file);
//
//            for(Airport a : mf.getAirports()){
//                boolean duplicate = false;
//                for(Airport as : airportObservableList){
//                    if (a.getName().equals(as.getName()))
//                        duplicate = true;
//                }
//                if(!duplicate)
//                    airportObservableList.add(a);
//            }
//
//            for(Obstacle o : mf.getObstacles()){
//                boolean dup = false;
//                for(Obstacle os : obstacles){
//                    if(o.getName().equals(os.getName()) && o.getHeight()==os.getHeight())
//                        dup = true;
//                }
//                if(!dup)
//                    obstacles.add(o);
//            }
//
//            airportObservableList.addAll(mf.getAirports());
//            obstacles.addAll(mf.getObstacles());
//        }
//    }

    /**
     * Reads the Textfields in AirportDefinition.fxml,
     * creates a new Airport and adds it the
     * list of Airports stored in this Controller
     */
    @FXML
    private void defineAirport()
    {
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
            if(!(runwayDegree.getValue().trim().equalsIgnoreCase("Degree"))&&!runwayPosition.getValue().trim().equalsIgnoreCase("Position")&&!airports.getValue().toString().trim().equalsIgnoreCase("Choose Airport")&&!todaLeft.getText().trim().isEmpty()&&!todaRight.getText().trim().isEmpty()&&!toraLeft.getText().trim().isEmpty()&&!toraRight.getText().trim().isEmpty()&&!asdaLeft.getText().trim().isEmpty()&&!asdaRight.getText().trim().isEmpty()&&!ldaLeft.getText().trim().isEmpty()&&!ldaRight.getText().trim().isEmpty()){
                if(!(Integer.parseInt(todaLeft.getText().trim())<0)&&!(Integer.parseInt(todaRight.getText().trim())<0)&&!(Integer.parseInt(toraLeft.getText().trim())<0)&&!(Integer.parseInt(toraRight.getText().trim())<0)&&!(Integer.parseInt(asdaLeft.getText().trim())<0)&&!(Integer.parseInt(asdaRight.getText().trim())<0)&&!(Integer.parseInt(ldaLeft.getText().trim())<0)&&!(Integer.parseInt(ldaRight.getText().trim())<0)){
                    Airport airport = airports.getValue();

                    String designatorLeft = runwayDegree.getValue() + runwayPosition.getValue();
                    String designatorRight = complementDesignatorText.getText();

                    int todaLeft = Integer.parseInt(this.todaLeft.getText().trim());
                    int todaRight = Integer.parseInt(this.todaRight.getText().trim());

                    int toraLeft = Integer.parseInt(this.toraLeft.getText().trim());
                    int toraRight = Integer.parseInt(this.toraRight.getText().trim());

                    int asdaLeft = Integer.parseInt(this.asdaLeft.getText().trim());
                    int asdaRight = Integer.parseInt(this.asdaRight.getText().trim());

                    int ldaLeft = Integer.parseInt(this.ldaLeft.getText().trim());
                    int ldaRight = Integer.parseInt(this.ldaRight.getText().trim());
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

        if(airportMainBox.getValue().toString().trim().equalsIgnoreCase("Airport")||obstacleBox.getValue().toString().trim().equalsIgnoreCase("Obstacle")||runwayBox.getValue().toString().trim().equalsIgnoreCase("Runway")||logicalRunwayBox.getValue().toString().trim().equalsIgnoreCase("Logical Runway")||leftThresholdDistance.getText().trim().isEmpty()||rightThresholdDistance.getText().trim().isEmpty()||leftRightBox.getValue().trim().equalsIgnoreCase("L/R")||centreLineDistance.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please fill in all inputs");
            alert.showAndWait();
        }
        else{
        int leftTHRDistance = Integer.parseInt(leftThresholdDistance.getText().trim());
        int rightTHRDistance = Integer.parseInt(rightThresholdDistance.getText().trim());
        int centerLineDistance = Integer.parseInt(centreLineDistance.getText().trim());
        if(centerLineDistance>=0){
        Position positionOfObstacle = new Position(centerLineDistance,leftTHRDistance,rightTHRDistance);
        RevisedRunway revisedRunway = new RevisedRunway(runwayToRevise,obstacleOnRunway,positionOfObstacle);
        revisedRunwayText.setText(revisedRunway.getResults());
        oldRunwayText.setText(runwayToRevise.getResults());
        calculationBreakdown.setText(revisedRunway.getCalcBreakdown());
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

    @FXML
    private void updateLogicalRunwayView()
    {
        try {
            if (revisedRunwayOnDisplay.getName().equals(runwayBox.getValue().getName())) {
                calculateRevisedRunway();
            }
        }
        catch (NullPointerException e)
        {
            System.out.println("No runway currently displayed.");
        }

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
        TopDownView topDownView = new TopDownView(runway, revisedLRunway, position, obstacle, rotateView);
        topDownView.widthProperty().bind(topDownViewContainer.widthProperty());
        topDownView.heightProperty().bind(topDownViewContainer.heightProperty());





        // Add everything to top down view tab
        pane.getChildren().addAll(topDownView );
        topDownViewContainer.getChildren().add(pane);

        TopDownView simTopDownView = new TopDownView(runway, revisedLRunway, position, obstacle, rotateView);
        simTopDownView.widthProperty().bind(simTopDownViewContainer.widthProperty());
        simTopDownView.heightProperty().bind(simTopDownViewContainer.heightProperty());
        simTopDownViewContainer.getChildren().add(simTopDownView);

        SideOnView sideOnView = new SideOnView(runway, revisedLRunway, position, obstacle, rotateView);
        sideOnView.widthProperty().bind(sideOnViewContainer.widthProperty());
        sideOnView.heightProperty().bind(sideOnViewContainer.heightProperty());
        sideOnViewContainer.getChildren().add(sideOnView);

        SideOnView simSideOnView = new SideOnView(runway, revisedLRunway, position, obstacle, rotateView);
        simSideOnView.widthProperty().bind(simTopDownViewContainer.widthProperty());
        simSideOnView.heightProperty().bind(simTopDownViewContainer.heightProperty());
        simSideOnViewContainer.getChildren().add(simSideOnView);



        // Draw side on view


        if (position != null) {
            topDownView.drawObstacle();
            sideOnView.drawObstacle();
            simSideOnView.drawObstacle();
            simTopDownView.drawObstacle();
        }

    }

}
