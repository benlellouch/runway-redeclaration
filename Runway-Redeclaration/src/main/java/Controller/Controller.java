package Controller;

import Model.*;

import com.sun.javafx.runtime.VersionInfo;
import Printer.ViewPrinter;
import View.AbstractRunwayView;
import View.SideRunwayView;
import View.TopRunwayView;
import XMLParsing.ModelFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import javax.tools.Tool;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
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
    @FXML
    private ComboBox<String> viewExportBox;
    @FXML
    private ComboBox<String> fileTypeBox;
    @FXML
    private TextField fileName;
    @FXML
    private Button exportViewButton;
    @FXML
    private ComboBox<String> printBox;

    private RevisedRunway revisedRunwayOnDisplay;

    //TODO make the views seperate fxml files so that we don't need this many containers
    @FXML
    private FlowPane topDownViewContainer, sideOnViewContainer, simTopDownViewContainer, simSideOnViewContainer;

    private ObservableList<String> centreLinePosition;
    private ObservableList<String> runwayViews;
    private ObservableList<String> filetype;
    private ObservableList<String> printBoxList;

    public static final StringBuilder notificationsString = new StringBuilder();
    protected static Image tick = new Image("icons/smalltick.png", true);
    private AbstractRunwayView topView;
    private AbstractRunwayView sideView;


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
        fileTypeBox = new ComboBox<>();
        viewExportBox = new ComboBox<>();
        centreLinePosition = generatecentreLinePosition();
        filetype = generateFileTypes();
        runwayViews = FXCollections.observableArrayList("Top View", "Side View");
        printBoxList = FXCollections.observableArrayList();
        printBoxList.setAll("Result", "Top View", "Side View");
        printBox = new ComboBox<>();
        centreLinePosition = generatecentreLinePosition();
        checkForAirports();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        airportMainBox.setItems(airportDefinitionController.getAirportObservableList());
        obstacleBox.setItems(obstacleDefinitionController.getObstacles());
        printBox.setItems(printBoxList);
        centreLinePositionBox.setItems(centreLinePosition);
        if(runwayBox.getValue() == null)
        {
            runwayBox.setDisable(true);
        }
        if(logicalRunwayBox.getValue() == null)
        {
            logicalRunwayBox.setDisable(true);
        }
        viewExportBox.setItems(runwayViews);
        fileTypeBox.setItems(filetype);
        printBox.setItems(printBoxList);
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
    private void openViewExport()
    {
        if (sideView == null || topView == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No Runway View to save.");
            alert.showAndWait();
        }
        else
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/runway_image_export.fxml"));
                loader.setController(this);
                Parent root = loader.load();
                Stage definitionStage = new Stage();
                Scene definitionScene = new Scene(root);
                definitionStage.setTitle("Export Runway View");
                definitionStage.getIcons().add(new Image("icons/icon.png"));
                definitionStage.setScene(definitionScene);
                definitionStage.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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
                    if (o.getName().equals(os.getName()) && o.getHeight() == os.getHeight()) {
                        dup = true;
                        break;
                    }
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
    private void openPrinterView()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PrinterView.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage definitionStage = new Stage();
            Scene definitionScene = new Scene(root);
            definitionStage.setTitle("Print View");
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
    private void print()
    {
        String result = printBox.getValue();
        if(result == null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please select a view to print");
            alert.showAndWait();
        }
        else
        {
            switch (result)
            {
                case "Result":
                    if (revisedRunwayText.getText().isEmpty())
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("No result to print");
                        alert.showAndWait();
                    }
                    else
                    {
                        ViewPrinter.printResult(revisedRunwayText,(Stage) revisedRunwayText.getScene().getWindow());
                    }
                    break;
                case "Top View":
                    if (topView != null)
                    {
                        ViewPrinter.printRunway(topView, (Stage) revisedRunwayText.getScene().getWindow());
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("No runway to print");
                        alert.showAndWait();
                    }

                    break;
                case "Side View":
                    if(sideView != null)
                    {
                        ViewPrinter.printRunway(sideView, (Stage) revisedRunwayText.getScene().getWindow());
                    }
                    else
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("No runway to print");
                        alert.showAndWait();
                    }
                    break;

            }

        }

        Stage stage = (Stage) printBox.getScene().getWindow();
        stage.close();
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
    private void exportView()
    {
        if (fileTypeBox.getValue() == null || viewExportBox.getValue() == null || fileName.getText().trim().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please fill in all inputs");
            alert.showAndWait();
        }else
        {
            String fileType = fileTypeBox.getValue();
            AbstractRunwayView runway = viewExportBox.getValue() == "Top View" ? topView : sideView;
            File file = new File(fileName.getText());


            if (fileType.equals("jpg"))
            {
                try{
                WritableImage wi;

                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.WHITE);

                int imageWidth = (int) runway.getBoundsInLocal().getWidth();
                int imageHeight = (int) runway.getBoundsInLocal().getHeight();

                wi = new WritableImage(imageWidth, imageHeight);
                runway.snapshot(parameters, wi);
                BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
                BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);
                Graphics2D graphics = bufImageRGB.createGraphics();
                graphics.drawImage(bufImageARGB, 0, 0, null);
                ImageIO.write(bufImageRGB, "jpg", file);
                graphics.dispose();
                Stage stage = (Stage) exportViewButton.getScene().getWindow();
                stage.close();
            } catch (IOException ex) { ex.printStackTrace(); }

            }
            else
            {
                //Prompt user to select a file

                try {
                    //Pad the capture area
                    WritableImage writableImage = new WritableImage((int)runway.getWidth() + 20,
                            (int)runway.getHeight() + 20);
                    runway.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    //Write the snapshot to the chosen file
                    ImageIO.write(renderedImage, fileType, file);
                    Stage stage = (Stage) exportViewButton.getScene().getWindow();
                    stage.close();
                } catch (IOException ex) { ex.printStackTrace(); }
            }

        }

        notificationsString.append("Export: successfully exported ")
                .append(viewExportBox.getValue())
                .append(" to ")
                .append(fileName.getText().trim())
                .append(" in the ")
                .append(fileTypeBox.getValue())
                .append(" format").append(" (")
                .append(Time.valueOf(LocalTime.now())).append(")").append("\n");

        synchronized (Controller.notificationsString)
        {
            notificationsString.notify();
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
        e.printStackTrace();
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
    private ObservableList<String> generateFileTypes() {return FXCollections.observableArrayList("jpg", "png", "gif");}

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
                    originalRunway = runway.getLogicalRunway1();
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }

        if (originalRunway == null) {
            return;
        }
        Pane pane = new Pane();

        boolean rotateView = orientationCheckBox.isSelected();

        //TODO  very hacked together needs to change
        TopRunwayView topRunwayView = new TopRunwayView(runway, revisedLRunway, position, obstacle, rotateView);
        topRunwayView.widthProperty().bind(topDownViewContainer.widthProperty());
        topRunwayView.heightProperty().bind(topDownViewContainer.heightProperty());

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

        if (position != null) {
            topRunwayView.renderObstacle();
            sideRunwayView.renderObstacle();
            simSideRunwayView.renderObstacle();
            simTopRunwayView.renderObstacle();
        }

        topView = topRunwayView;
        sideView =sideRunwayView;
    }

}
