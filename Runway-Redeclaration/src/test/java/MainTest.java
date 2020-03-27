import Model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.ComboBoxMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

        
public class MainTest extends ApplicationTest
{
    @Override
    public void start (Stage stage) throws Exception{
        Parent mainNode = FXMLLoader.load(Main.class.getResource("/MainView.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp() throws Exception {
        clickOn("#noAirportDefinedOK");
        // For some reason, when running this via Monocle, we need to open the window again since it can't
        // see the window open after confirming the initial no airport message.
        clickOn("File");
        clickOn("Define New Airport");
        clickOn("#airportName").write("Heathrow");
        clickOn("#airportDoneButton");
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

        // Successful Tests ////////////////////////////////////////////////////////////////////////////
        
    @Test
    public void addedNewAirportOnStartUp () {
        clickOn("#airportMainBox");
        clickOn("Heathrow");
        FxAssert.verifyThat("#airportMainBox", (ComboBox<Airport> c) -> {
            String val = c.getValue().getName();
            return val.equals("Heathrow");
        });
    }

    @Test
    public void successfulAirportDef (){
        clickOn("File");
        clickOn("Define New Airport");
        clickOn("#airportName").write("Some other Airport");
        clickOn("#airportDoneButton");
        //clickOn("#airportMainBox");
        FxAssert.verifyThat("#airportMainBox", ComboBoxMatchers.hasItems(2));
    }

    public void prepareRunway(){
        clickOn("File");
        clickOn("Define New Runway");
        clickOn("#runwayDegree");
        clickOn("09");
        clickOn("#runwayPosition");
        clickOn("R");
    }

    @Test
    public void successfulRunwayDef (){
        prepareRunway();
        FxAssert.verifyThat("#complementDesignatorText", (Text text) -> text.getText().equals("27L"));
        runwayDefFill("3660");

        addedNewAirportOnStartUp();
        clickOn("#runwayBox").clickOn("09R/27L");
        clickOn("#logicalRunwayBox").clickOn("09R");

        try{
            //Thread.sleep(3000);
        }catch (Exception e){

        }
    }

    @Test
    public void successfulObstacleDef (){
        clickOn("File").clickOn("Define New Obstacle");
        clickOn("#obstacleName").write("Obstacle Scenario 2");
        clickOn("#obstacleHeight").write("25");
        clickOn("#obstacleDoneButton");
        clickOn("#obstacleBox").clickOn("Obstacle Scenario 2(25m)");
        FxAssert.verifyThat("#obstacleBox", (ComboBox<Obstacle> o) -> {
            String val = o.getValue().getName();
            return val.equals("Obstacle Scenario 2") && o.getValue().getHeight() == 25;
        });
    }

        
    @Test
    public void correctResultsScenario2(){
        successfulAirportDef();
        successfulRunwayDef();
        successfulObstacleDef();
        FxAssert.verifyThat("#obstacleBox", ComboBoxMatchers.hasItems(5));

        clickOn("#logicalRunwayBox").clickOn("09R");
        clickOn("#leftThresholdDistance").write("2853");
        clickOn("#rightThresholdDistance").write("500");
        clickOn("#leftRightBox").clickOn("L");
        clickOn("#centreLineDistance").write("20");

        clickOn("Calculate");

        FxAssert.verifyThat("#revisedRunwayText", (Text text) -> {
            String val = text.getText();
            String expectedResults = "Runway 09R:\n" +
                    "LDA: 2553\n" +
                    "TORA: 1850\n" +
                    "TODA: 1850\n" +
                    "ASDA: 1850\n\n" +
                    "Runway 27L:\n" +
                    "LDA: 1850\n" +
                    "TORA: 2860\n" +
                    "TODA: 2860\n" +
                    "ASDA: 2860\n\n";
            return val.equals(expectedResults);
        });
    }

        
        // Tests Giving Undesired Outputs /////////////////////////////////////////////////////////////////////////////
        
    @Test
    public void fail_emptyInputAirport(){
        clickOn("File");
        clickOn("Define New Airport");
        clickOn("#airportDoneButton");
        alert_dialog_has_header_and_content("Message","Please fill all input fields");
    }

    @Test
    public void fail_airportAlreadyExist(){
        clickOn("File");
        clickOn("Define New Airport");
        clickOn("#airportName").write("Heathrow");
        clickOn("#airportDoneButton");
        alert_dialog_has_header_and_content("Message","Airport already exists");
    }

    @Test
    public void fail_obstacleAlreadyExist(){
        clickOn("File").clickOn("Define New Obstacle");
        clickOn("#obstacleName").write("Barricades");
        clickOn("#obstacleHeight").write("1");
        clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content("Message","Duplicate alert: Obstacle has not been added");
    }

    @Test
    public void fail_obstacleEmptyInput_stringHeight(){
        clickOn("File").clickOn("Define New Obstacle");
        clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content("Message","Please fill in all input fields");

        clickOn("#obstacleName").write("Barricades");
        clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content("Message","Please fill in all input fields");

        clickOn("#obstacleHeight").write("height");
        clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content("Message","Please input a number for height");
    }

    @Test
    public void fail_obstacleNegativeHeight(){
        clickOn("File").clickOn("Define New Obstacle");
        clickOn("#obstacleName").write("Pole");
        clickOn("#obstacleHeight").write("-10");
        clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content("Message","Please put a number greater than zero for Height");
    }

    @Test
    public void fail_emptyInputRunwayDef(){
        prepareRunway();
        clickOn("#runwayDoneButton");
        alert_dialog_has_header_and_content("Message","Please ensure all inputs have been filled in");
    }

    @Test
    public void fail_negativeInputRunwayDef(){
        prepareRunway();
        runwayDefFill("-1");
        alert_dialog_has_header_and_content("Message","Please ensure only positive values are used for measurements");
    }

    @Test
    public void fail_stringInputRunwayDef(){
        prepareRunway();
        runwayDefFill("String");
        alert_dialog_has_header_and_content("Message","Please ensure only numbers are used as inputs for measurements");
    }

    @Test
    public void fail_emptyInputCalulation (){
        clickOn("Calculate");
        alert_dialog_has_header_and_content("Message","Please fill in all inputs");
    }

    @Test
    public void fail_stringInputCalculation(){
        successfulAirportDef();
        successfulRunwayDef();
        successfulObstacleDef();

        clickOn("#logicalRunwayBox").clickOn("09R");
        clickOn("#leftThresholdDistance").write("2853");
        clickOn("#rightThresholdDistance").write("string 500");
        clickOn("#leftRightBox").clickOn("L");
        clickOn("#centreLineDistance").write("20");

        clickOn("Calculate");
        alert_dialog_has_header_and_content("Message","Please input numbers for distances");
    }

        
        
        //Helper Methods ----------------------------------------------------------------------//
        
    /*Helper Method to retrieve Java FX GUI components */
    public  <T extends Node> T find(final String query){
        return (T) lookup(query).queryAll().iterator().next();
    }

    public void alert_dialog_has_header_and_content(final String expectedHeader, final String expectedContent) {
        final javafx.stage.Stage actualAlertDialog = getTopModalStage();
        assertNotNull(actualAlertDialog);

        final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
        assertEquals(expectedHeader, dialogPane.getHeaderText());
        assertEquals(expectedContent, dialogPane.getContentText());
        clickOn("OK");
    }

    private javafx.stage.Stage getTopModalStage() {
        // Get a list of windows but ordered from top[0] to bottom[n] ones.
        // It is needed to get the first found modal window.
        final List<Window> allWindows = new ArrayList<>(robotContext().getWindowFinder().listWindows());
        Collections.reverse(allWindows);

        return (javafx.stage.Stage) allWindows
                .stream()
                .filter(window -> window instanceof javafx.stage.Stage)
                .filter(window -> ((javafx.stage.Stage) window).getModality() == Modality.APPLICATION_MODAL)
                .findFirst()
                .orElse(null);
    }

    public void runwayDefFill(String todaLeftInput) {
        clickOn("#todaLeft").write(todaLeftInput);
        clickOn("#toraLeft").write("3660");
        clickOn("#asdaLeft").write("3660");
        clickOn("#ldaLeft").write("3353");

        clickOn("#todaRight").write("3660");
        clickOn("#toraRight").write("3660");
        clickOn("#asdaRight").write("3660");
        clickOn("#ldaRight").write("3660");

        clickOn("#airports").clickOn("Heathrow");
        clickOn("#runwayDoneButton");
    }

}