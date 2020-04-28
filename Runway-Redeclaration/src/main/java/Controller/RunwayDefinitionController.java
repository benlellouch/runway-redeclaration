package Controller;

import Model.Airport;
import Model.LogicalRunway;
import Model.Runway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ResourceBundle;

public class RunwayDefinitionController implements Initializable {

    private static RunwayDefinitionController SINGLE_INSTANCE = null;
    private Controller mainController;

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
    private ComboBox<String> runwayDegree;
    @FXML
    private ComboBox<String> runwayPosition;
    @FXML
    private ComboBox<Airport> airports;
    @FXML
    private Text complementDesignatorText;

    private ObservableList<String> runwayDegreeList;
    private ObservableList <String> runwayPositionList;
    private HashMap<String,String> oppositeDegreeMap;
    private HashMap<String,String> oppositePositionMap;

    private RunwayDefinitionController (Controller mainController)
    {
        this.mainController = mainController;
        runwayPosition = new ComboBox<>();
        runwayDegree = new ComboBox<>();
        airports = new ComboBox<>();
        runwayDegreeList = generateDegreeList();
        runwayPositionList = generatePositionList();
        oppositeDegreeMap = generateOppositeDegreeMap();
        oppositePositionMap = generateOppositePositionMap();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        runwayDegree.setItems(runwayDegreeList);
        runwayPosition.setItems(runwayPositionList);
        airports.setItems(mainController.airportDefinitionController.getAirportObservableList());
    }


    public static RunwayDefinitionController getInstance(Controller mainController)
    {
        if (SINGLE_INSTANCE == null) {
            synchronized (RunwayDefinitionController.class)
            {
                SINGLE_INSTANCE = new RunwayDefinitionController(mainController);
            }
        }

        return SINGLE_INSTANCE;
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
            Notifications runwayAddedNotification;
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
                        mainController.updateRunwayBox();

                        Stage stage = (Stage) runwayDoneButton.getScene().getWindow();
                        stage.close();
                        Controller.notificationsString.append("Runway: ").append(run.getName()).append(" has been added to Airport: ").append(airport.getName()).append(" (").append(Time.valueOf(LocalTime.now())).append(")").append("\n");
                        synchronized (Controller.notificationsString)
                        {
                            Controller.notificationsString.notify();
                        }
                        runwayAddedNotification =
                                Notifications.create()
                                        .title("Runway Added")
                                        .text("Runway: " + run.getName() + " has been added to Airport: " +airport.getName())
                                        .hideAfter(Duration.seconds(3))
                                        .graphic(new ImageView(Controller.tick))
                                        .position(Pos.BOTTOM_RIGHT);
                        runwayAddedNotification.show();
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
                "16","17","18");
    }


    private ObservableList<String> generatePositionList()
    {
        return FXCollections.observableArrayList("L","R","C");
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
}
