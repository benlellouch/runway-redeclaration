import Controller.AirportDefinitionController;
import Controller.ObstacleDefinitionController;
import Model.*;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.ComboBoxMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

        
public class MainTest extends ApplicationTest
{

//    @BeforeClass
//    public static void setUpHeadlessMode()
//    {
//            System.setProperty("testfx.robot", "glass");
//            System.setProperty("testfx.headless", "true");
//            System.setProperty("prism.order", "sw");
//            System.setProperty("prism.text", "t2k");
//            System.setProperty("java.awt.headless", "true");
//    }

    @Override
    public void start (Stage stage) throws Exception{
        Parent mainNode = FXMLLoader.load(Main.class.getResource("/MainView.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp() throws TimeoutException {
        clickOn("#noAirportDefinedOK");
        // For some reason, when running this via Monocle, we need to open the window again since it can't
        // see the window open after confirming the initial no airport message.
//        clickOn("File");
//        clickOn("Define New Airport");
//        clickOn("#airportName").write("Heathrow");
        write("Heathrow");
        clickOn("#airportDoneButton");
    }


    @After
    public void tearDown () throws Exception {
        FxToolkit.cleanupStages();
        AirportDefinitionController.getInstance().cleanUp();
        ObstacleDefinitionController.getInstance().cleanUp();
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
        FxAssert.verifyThat("#notificationsLog", (Text s) -> s.getText().contains("Airport: " + "Heathrow" + " has been added"));
    }

    @Test
    public void successfulAirportDef (){
        clickOn("File");
        clickOn("Define New Airport");
        clickOn("#airportName").write("Gatwick");
        clickOn("#airportDoneButton");
        //clickOn("#airportMainBox");
        FxAssert.verifyThat("#airportMainBox", ComboBoxMatchers.hasItems(2));
        FxAssert.verifyThat("#airportMainBox", (ComboBox<Airport> c) -> {
            String val = c.getItems().get(1).getName();
            return val.equals(("Gatwick"));
        });
        FxAssert.verifyThat("#notificationsLog", (Text s) -> s.getText().contains("Airport: " + "Gatwick" + " has been added"));
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

        FxAssert.verifyThat("#runwayBox", (ComboBox<Runway> r) -> {
            String val = r.getItems().get(0).getName();
            return val.equals("09R/27L");
        });

        clickOn("#logicalRunwayBox").clickOn("09R");

        FxAssert.verifyThat("#logicalRunwayBox", (ComboBox<LogicalRunway> l) -> {
            String l1 = l.getItems().get(0).getName();
            String l2 = l.getItems().get(1).getName();
            int size = l.getItems().size();
            return (size == 2) && (l1.equals("09R")) && (l2.equals("27L")) && (!l.isDisabled());
        });

        FxAssert.verifyThat("#notificationsLog", (Text s) -> s.getText().contains("Runway: " + "09R/27L" + " has been added to Airport: Heathrow"));

        try{
            //Thread.sleep(3000);
        }catch (Exception e){

        }
    }

    @Test
    public void successfulObstacleDef (){
        clickOn("File").clickOn("Define New Obstacle");
        clickOn("#obstacleName").write("Boeing");
        clickOn("#obstacleHeight").write("25");
        clickOn("#obstacleDoneButton");
        clickOn("#obstacleBox").clickOn("Boeing(25m)");
        FxAssert.verifyThat("#obstacleBox", (ComboBox<Obstacle> o) -> {
            String val = o.getValue().getName();
            return val.equals("Boeing") && o.getValue().getHeight() == 25;
        });
        FxAssert.verifyThat("#notificationsLog", (Text s) -> s.getText().contains("Obstacle: " + "Boeing" + " has been added"));
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
        clickOn("#centreLinePositionBox").clickOn("N");
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

    // Boundary Tests ////////////////////////////////////////////////////////////////////////////

    @Test
    public void boundaryTest_Obstacle()
    {
        clickOn("File").clickOn("Define New Obstacle");
        clickOn("#obstacleName").write("Boundary Obstacle");
        clickOn("#obstacleHeight").write("1");
        clickOn("#obstacleDoneButton");
        FxAssert.verifyThat("#obstacleBox", (ComboBox<Obstacle> o) -> {
            String val = o.getValue().getName();
            return val.equals("Boundary Obstacle") && o.getValue().getHeight() == 1;
        });
    }

    @Test
    public void boundaryTest_Runway(){
        prepareRunway();
        runwayDefFillAll("0");
        addedNewAirportOnStartUp();
        clickOn("#runwayBox").clickOn("09R/27L");
        FxAssert.verifyThat("#runwayBox", (ComboBox<Runway> r) -> {
            String val = r.getItems().get(0).getName();
            return val.equals("09R/27L");
        });
        clickOn("#logicalRunwayBox").clickOn("09R");
        FxAssert.verifyThat("#logicalRunwayBox", (ComboBox<LogicalRunway> l) -> {
            String l1 = l.getItems().get(0).getName();
            String l2 = l.getItems().get(1).getName();
            int size = l.getItems().size();
            return (size == 2) && (l1.equals("09R")) && (l2.equals("27L")) && (!l.isDisabled());
        });
    }

    // Scenario Tests /////////////////////////////////////////////////////////////////////////////////////////////

    // Scenario 1: Lauren (Runway Technician)
    @Test
    public void scenario1()
    {
        clickOn("File").clickOn("Define New Airport");
        write("Bristol").clickOn("#airportDoneButton");
        clickOn("#airportMainBox").clickOn("Bristol");
        clickOn("File").clickOn("Define New Runway");
        clickOn("#runwayDegree").clickOn("09");
        clickOn("#runwayPosition").clickOn("C");

        clickOn("#todaLeft").write("7500");
        clickOn("#toraLeft").write("7500");
        clickOn("#asdaLeft").write("7500");
        clickOn("#ldaLeft").write("5000;");

        clickOn("#todaRight").write("7500");
        clickOn("#toraRight").write("7500");
        clickOn("#asdaRight").write("-7500");
        clickOn("#ldaRight").write("5000");

        clickOn("#airports").clickOn("Bristol");
        clickOn("#runwayDoneButton");

        alert_dialog_has_header_and_content("Message","Please ensure only positive values are used for measurements");

        clickOn("#asdaRight");
        clearTextField(5);
        write("7500");

        clickOn("#runwayDoneButton");

        alert_dialog_has_header_and_content("Message","Please ensure only numbers are used as inputs for measurements");

        clickOn("#ldaLeft");
        clearTextField(1);

        clickOn("#runwayDoneButton");

        clickOn("File").clickOn("Define New Obstacle");

        clickOn("#obstacleName").write("Jumbo Jet");
        clickOn("#obstacleHeight").write("tall");
        clickOn("#obstacleDoneButton");

        alert_dialog_has_header_and_content("Message","Please input a number for height");

        clickOn("#obstacleHeight");
        clearTextField(4);
        write("30");

        clickOn("#obstacleDoneButton");

        clickOn("#runwayBox").clickOn("09C/27C");
        clickOn("#logicalRunwayBox").clickOn("09C");
        clickOn("#obstacleBox").clickOn("Jumbo Jet(30m)");
    }

    // Scenario 2: Charles (Airfield Operations Manager)
    @Test
    public void scenario2()
    {
        scenario1();

        clickOn("#leftThresholdDistance").write("-50");
        clickOn("#rightThresholdDistance").write("7550");
        clickOn("#centreLinePositionBox").clickOn("N");
        clickOn("#centreLineDistance").write("20");
        clickOn("Calculate");
        clickOn("Side-on view");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        clickOn("Top-down view");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        clickOn("Calculation Breakdown");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        clickOn("Simultaneous View");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    // Scenario 3: Aaron (Runway Technician)
    // (This test cannot currently be implemented since it is designed to test the import XML functionality.)
    // TODO: (Maybe) Refactor to allow for Import XML tests.

    // Scenario 4: James (Airfield Operations Manager)
    // Apart from the error made in this scenario, this test would be identical to that of Scenario 2 (and just as useless since we can't tell if the visualisation is correct through TestFX)
        
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
    public void fail_obstacleZeroHeight(){
        clickOn("File").clickOn("Define New Obstacle");
        clickOn("#obstacleName").write("Pole");
        clickOn("#obstacleHeight").write("0");
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
        clickOn("#centreLinePositionBox").clickOn("L");
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

    public void runwayDefFillAll(String input) {
        clickOn("#todaLeft").write(input);
        clickOn("#toraLeft").write(input);
        clickOn("#asdaLeft").write(input);
        clickOn("#ldaLeft").write(input);

        clickOn("#todaRight").write(input);
        clickOn("#toraRight").write(input);
        clickOn("#asdaRight").write(input);
        clickOn("#ldaRight").write(input);

        clickOn("#airports").clickOn("Heathrow");
        clickOn("#runwayDoneButton");
    }

    public void clearTextField(int size) {
        for (int i = 0; i < size; i++) { push(KeyCode.BACK_SPACE); }
    }
}