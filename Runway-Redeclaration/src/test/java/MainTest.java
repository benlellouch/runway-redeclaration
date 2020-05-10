import Controller.AirportDefinitionController;
import Controller.ObstacleDefinitionController;
import Model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.control.ComboBoxMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class MainTest
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

    @Start
    public void start (Stage stage) throws Exception{
        Parent mainNode = FXMLLoader.load(Main.class.getResource("/MainView.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setUp(FxRobot robot) throws TimeoutException {
        robot.clickOn("#noAirportDefinedOK");
        robot.clickOn("#airportName");
        robot.write("Heathrow");
        robot.clickOn("#airportDoneButton");
    }


    @AfterEach
    public void tearDown (FxRobot robot) throws Exception {
        FxToolkit.cleanupStages();
        AirportDefinitionController.getInstance().cleanUp();
        ObstacleDefinitionController.getInstance().cleanUp();
        robot.release(new KeyCode[]{});
        robot.release(new MouseButton[]{});
    }

    // Successful Tests ////////////////////////////////////////////////////////////////////////////
        
    @Test
    @DisplayName("TC5: Airport successfully added on startup")
    public void addedNewAirportOnStartUp (FxRobot robot) {
        robot.clickOn("#airportMainBox");
        robot.clickOn("Heathrow");
        FxAssert.verifyThat("#airportMainBox", (ComboBox<Airport> c) -> {
            String val = c.getValue().getName();
            return val.equals("Heathrow");
        });
        FxAssert.verifyThat("#notificationsLog", (Text s) -> s.getText().contains("Airport: " + "Heathrow" + " has been added"));
    }

    @Test
    @DisplayName("TC6: Successful airport definition")
    public void successfulAirportDef (FxRobot robot){
        robot.clickOn("File");
        robot.clickOn("Define").clickOn("New Airport");
        robot.clickOn("#airportName").write("Gatwick");
        robot.clickOn("#airportDoneButton");
        //robot.clickOn("#airportMainBox");
        FxAssert.verifyThat("#airportMainBox", ComboBoxMatchers.hasItems(2));
        FxAssert.verifyThat("#airportMainBox", (ComboBox<Airport> c) -> {
            String val = c.getItems().get(1).getName();
            return val.equals(("Gatwick"));
        });
        FxAssert.verifyThat("#notificationsLog", (Text s) -> s.getText().contains("Airport: " + "Gatwick" + " has been added"));
    }

    @Test
    @DisplayName("TC7: Successful runway definition")
    public void successfulRunwayDef (FxRobot robot){
        prepareRunway(robot);
        FxAssert.verifyThat("#complementDesignatorText", (Text text) -> text.getText().equals("27L"));
        runwayDefFill(robot,"3660");

        addedNewAirportOnStartUp(robot);
        robot.clickOn("#runwayBox").clickOn("09R/27L");

        FxAssert.verifyThat("#runwayBox", (ComboBox<Runway> r) -> {
            String val = r.getItems().get(0).getName();
            return val.equals("09R/27L");
        });

        robot.clickOn("#logicalRunwayBox").clickOn("09R");

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
    @DisplayName("TC8: Successful obstacle definition")
    public void successfulObstacleDef (FxRobot robot){
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleName").write("Boeing");
        robot.clickOn("#obstacleHeight").write("25");
        robot.clickOn("#obstacleDoneButton");
        robot.clickOn("#obstacleBox").clickOn("Boeing(25m)");
        FxAssert.verifyThat("#obstacleBox", (ComboBox<Obstacle> o) -> {
            String val = o.getValue().getName();
            return val.equals("Boeing") && o.getValue().getHeight() == 25;
        });
        FxAssert.verifyThat("#notificationsLog", (Text s) -> s.getText().contains("Obstacle: " + "Boeing" + " has been added"));
    }

        
    @Test
    @DisplayName("TC9: Correct results calculated and displayed")
    public void correctResultsScenario2(FxRobot robot){
        successfulAirportDef(robot);
        successfulRunwayDef(robot);
        successfulObstacleDef(robot);
        FxAssert.verifyThat("#obstacleBox", ComboBoxMatchers.hasItems(5));

        robot.clickOn("#logicalRunwayBox").clickOn("09R");
        robot.clickOn("#leftThresholdDistance").write("2853");
        robot.clickOn("#rightThresholdDistance").write("500");
        robot.clickOn("#centreLinePositionBox").clickOn("N");
        robot.clickOn("#centreLineDistance").write("20");

        robot.clickOn("Calculate");

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

        FxAssert.verifyThat("#calculationBreakdown", (Label label) -> {
            String val = label.getText();
            String expectedCalcBreakdown = "Runway 09R:\n" +
                    "LDA:\nDistance From Threshold (2853) - RESA (240) - Strip End (60) = 2553\n" +
                    "TORA:\nDistance From Threshold (2853) + Displaced Threshold (307) - Slope Calculation (1250) - Strip End (60) = 1850\n" +
                    "TODA:\nRevised TORA (1850) = 1850\n" +
                    "ASDA:\nRevised TORA (1850) = 1850\n" +
                    "\n" +
                    "Runway 27L:\n" +
                    "LDA:\nOriginal LDA (3660) - Distance From Threshold (500) - Slope Calculation (1250) - Strip End (60) = 1850\n" +
                    "TORA:\nOriginal TORA (3660) - Blast Allowance (300) - Distance From Threshold (500) = 2860\n" +
                    "TODA:\nRevised TORA (2860) + Clearway (0) = 2860\n" +
                    "ASDA:\nRevised TORA (2860) + Stopway (0) = 2860\n\n";

            return val.equals(expectedCalcBreakdown);
        });
    }

    // Boundary Tests ////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("TC10: Lower boundary test on obstacle (1m)")
    public void boundaryTest_lowerObstacle(FxRobot robot)
    {
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleName").write("Crate");
        robot.clickOn("#obstacleHeight").write("1");
        robot.clickOn("#obstacleDoneButton");
        robot.clickOn("#obstacleBox").clickOn("Crate(1m)");
        FxAssert.verifyThat("#obstacleBox", (ComboBox<Obstacle> o) -> {
            String val = o.getValue().getName();
            return val.equals("Crate") && o.getValue().getHeight() == 1;
        });
    }

    @Test
    @DisplayName("TC11: Upper boundary test on obstacle (100m)")
    public void boundaryTest_upperObstacle(FxRobot robot)
    {
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleName").write("Crane");
        robot.clickOn("#obstacleHeight").write("100");
        robot.clickOn("#obstacleDoneButton");
        robot.clickOn("#obstacleBox").clickOn("Crane(100m)");
        FxAssert.verifyThat("#obstacleBox", (ComboBox<Obstacle> o) -> {
            String val = o.getValue().getName();
            return val.equals("Crane") && o.getValue().getHeight() == 100;
        });
    }

    @Test
    @DisplayName("TC12: Lower boundary test on runway (0m)")
    public void boundaryTest_Runway(FxRobot robot){
        prepareRunway(robot);
        runwayDefFillAll(robot,"0");
        addedNewAirportOnStartUp(robot);
        robot.clickOn("#runwayBox").clickOn("09R/27L");
        FxAssert.verifyThat("#runwayBox", (ComboBox<Runway> r) -> {
            String val = r.getItems().get(0).getName();
            return val.equals("09R/27L");
        });
        robot.clickOn("#logicalRunwayBox").clickOn("09R");
        FxAssert.verifyThat("#logicalRunwayBox", (ComboBox<LogicalRunway> l) -> {
            String l1 = l.getItems().get(0).getName();
            String l2 = l.getItems().get(1).getName();
            int size = l.getItems().size();
            return (size == 2) && (l1.equals("09R")) && (l2.equals("27L")) && (!l.isDisabled());
        });
    }

    // Scenario Tests /////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    @DisplayName("TC13: Test replicating Scenario 1 (Lauren - Runway Technician)")
    public void scenario1(FxRobot robot)
    {
        robot.clickOn("File").clickOn("Define").clickOn("New Airport");
        robot.clickOn("#airportName").write("Bristol").clickOn("#airportDoneButton");
        robot.clickOn("#airportMainBox").clickOn("Bristol");
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Runway");
        robot.clickOn("#runwayDegree").clickOn("09");
        robot.clickOn("#runwayPosition").clickOn("C");

        robot.clickOn("#todaLeft").write("7500");
        robot.clickOn("#toraLeft").write("7500");
        robot.clickOn("#asdaLeft").write("7500");
        robot.clickOn("#ldaLeft").write("5000;");

        robot.clickOn("#todaRight").write("7500");
        robot.clickOn("#toraRight").write("7500");
        robot.clickOn("#asdaRight").write("-7500");
        robot.clickOn("#ldaRight").write("5000");

        robot.clickOn("#airports").clickOn("Bristol");
        robot.clickOn("#runwayDoneButton");

        alert_dialog_has_header_and_content(robot, "Message","Please ensure only positive values are used for measurements");

        robot.clickOn("#asdaRight");
        clearTextField(robot,5);
        robot.write("7500");

        robot.clickOn("#runwayDoneButton");

        alert_dialog_has_header_and_content(robot, "Message","Please ensure only numbers are used as inputs for measurements");

        robot.clickOn("#ldaLeft");
        clearTextField(robot,1);

        robot.clickOn("#runwayDoneButton");

        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");

        robot.clickOn("#obstacleName").write("Jumbo Jet");
        robot.clickOn("#obstacleHeight").write("tall");
        robot.clickOn("#obstacleDoneButton");

        alert_dialog_has_header_and_content(robot, "Message","Please input a number for height");

        robot.clickOn("#obstacleHeight");
        clearTextField(robot,4);
        robot.write("30");

        robot.clickOn("#obstacleDoneButton");

        robot.clickOn("#runwayBox").clickOn("09C/27C");
        robot.clickOn("#logicalRunwayBox").clickOn("09C");
        robot.clickOn("#obstacleBox").clickOn("Jumbo Jet(30m)");
    }

    @Test
    @DisplayName("TC14: Test replicating Scenario 2 (Charles - Airfield Operations Manager)")
    public void scenario2(FxRobot robot)
    {
        scenario1(robot);

        robot.clickOn("#leftThresholdDistance").write("-50");
        robot.clickOn("#rightThresholdDistance").write("7550");
        robot.clickOn("#centreLinePositionBox").clickOn("N");
        robot.clickOn("#centreLineDistance").write("20");
        robot.clickOn("Calculate");
        robot.clickOn("Side-on view");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        robot.clickOn("Top-down view");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        robot.clickOn("Calculation Breakdown");

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        robot.clickOn("Simultaneous View");

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
    @DisplayName("TC15: Failure caused by blank airport name")
    public void fail_emptyInputAirport(FxRobot robot){
        robot.clickOn("File");
        robot.clickOn("Define").clickOn("New Airport");
        robot.clickOn("#airportDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please fill all input fields");
    }

    @Test
    @DisplayName("TC16: Failure caused by existing airport name")
    public void fail_airportAlreadyExist(FxRobot robot){
        robot.clickOn("File");
        robot.clickOn("Define").clickOn("New Airport");
        robot.clickOn("#airportName").write("Heathrow");
        robot.clickOn("#airportDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Airport already exists");
    }

    @Test
    @DisplayName("TC17: Failure caused by existing obstacle name")
    public void fail_obstacleAlreadyExist(FxRobot robot){
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleName").write("Barricades");
        robot.clickOn("#obstacleHeight").write("1");
        robot.clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Duplicate alert: Obstacle has not been added");
    }

    @Test
    @DisplayName("TC18: Failure caused by non-numeric obstacle height")
    public void fail_obstacleEmptyInput_stringHeight(FxRobot robot){
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please fill in all input fields");

        robot.clickOn("#obstacleName").write("Barricades");
        robot.clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please fill in all input fields");

        robot.clickOn("#obstacleHeight").write("height");
        robot.clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please input a number for height");
    }

    @Test
    @DisplayName("TC19: Failure caused by negative obstacle height")
    public void fail_obstacleNegativeHeight(FxRobot robot){
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleName").write("Pole");
        robot.clickOn("#obstacleHeight").write("-10");
        robot.clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please put a number greater than zero for Height");
    }

    @Test
    @DisplayName("TC20: Failure caused by an obstacle height of zero")
    public void fail_obstacleZeroHeight(FxRobot robot){
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleName").write("Pole");
        robot.clickOn("#obstacleHeight").write("0");
        robot.clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please put a number greater than zero for Height");
    }

    @Test
    @DisplayName("TC21: Failure caused by large obstacle height (101m)")
    public void fail_obstacleLargeHeight(FxRobot robot){
        robot.clickOn("File").clickOn("Define").moveTo("New Airport").clickOn("New Obstacle");
        robot.clickOn("#obstacleName").write("Pole");
        robot.clickOn("#obstacleHeight").write("101");
        robot.clickOn("#obstacleDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please put a number less than one hundred for Height");
    }

    @Test
    @DisplayName("TC22: Failure caused by blank runway definition fields")
    public void fail_emptyInputRunwayDef(FxRobot robot){
        prepareRunway(robot);
        robot.clickOn("#runwayDoneButton");
        alert_dialog_has_header_and_content(robot, "Message","Please ensure all inputs have been filled in");
    }

    @Test
    @DisplayName("TC23: Failure caused by negative runway dimension")
    public void fail_negativeInputRunwayDef(FxRobot robot){
        prepareRunway(robot);
        runwayDefFill(robot,"-1");
        alert_dialog_has_header_and_content(robot, "Message","Please ensure only positive values are used for measurements");
    }

    @Test
    @DisplayName("TC24: Failure caused by non-numeric runway dimension")
    public void fail_stringInputRunwayDef(FxRobot robot){
        prepareRunway(robot);
        runwayDefFill(robot,"String");
        alert_dialog_has_header_and_content(robot, "Message","Please ensure only numbers are used as inputs for measurements");
    }

    @Test
    @DisplayName("TC25: Failure caused by empty calculation fields")
    public void fail_emptyInputCalulation (FxRobot robot){
        robot.clickOn("Calculate");
        alert_dialog_has_header_and_content(robot,"Message","Please fill in all inputs");
    }

    @Test
    @DisplayName("TC26: Failure caused by non-numeric thresholds")
    public void fail_stringInputCalculation(FxRobot robot){
        successfulAirportDef(robot);
        successfulRunwayDef(robot);
        successfulObstacleDef(robot);

        robot.clickOn("#logicalRunwayBox").clickOn("09R");
        robot.clickOn("#leftThresholdDistance").write("2853");
        robot.clickOn("#rightThresholdDistance").write("string 500");
        robot.clickOn("#centreLinePositionBox").clickOn("N");
        robot.clickOn("#centreLineDistance").write("20");

        robot.clickOn("Calculate");
        alert_dialog_has_header_and_content(robot,"Message","Please input numbers for distances");
    }
        
    //Helper Methods ----------------------------------------------------------------------//
        
    /*Helper Method to retrieve Java FX GUI components */
    public  <T extends Node> T find(FxRobot robot, final String query){
        return (T) robot.lookup(query).queryAll().iterator().next();
    }

    public void prepareRunway(FxRobot robot){
        robot.clickOn("File");
        robot.clickOn("Define").moveTo("New Airport").clickOn("New Runway");
        robot.clickOn("#runwayDegree");
        robot.clickOn("09");
        robot.clickOn("#runwayPosition");
        robot.clickOn("R");
    }

    public void alert_dialog_has_header_and_content(FxRobot robot, final String expectedHeader, final String expectedContent) {
        final javafx.stage.Stage actualAlertDialog = getTopModalStage(robot);
        assertNotNull(actualAlertDialog);

        final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
        assertEquals(expectedHeader, dialogPane.getHeaderText());
        assertEquals(expectedContent, dialogPane.getContentText());
        robot.clickOn("OK");
    }

    private javafx.stage.Stage getTopModalStage(FxRobot robot) {
        // Get a list of windows but ordered from top[0] to bottom[n] ones.
        // It is needed to get the first found modal window.
        final List<Window> allWindows = new ArrayList<>(robot.robotContext().getWindowFinder().listWindows());
        Collections.reverse(allWindows);

        return (javafx.stage.Stage) allWindows
                .stream()
                .filter(window -> window instanceof javafx.stage.Stage)
                .filter(window -> ((javafx.stage.Stage) window).getModality() == Modality.APPLICATION_MODAL)
                .findFirst()
                .orElse(null);
    }

    public void runwayDefFill(FxRobot robot, String todaLeftInput) {
        robot.clickOn("#todaLeft").write(todaLeftInput);
        robot.clickOn("#toraLeft").write("3660");
        robot.clickOn("#asdaLeft").write("3660");
        robot.clickOn("#ldaLeft").write("3353");

        robot.clickOn("#todaRight").write("3660");
        robot.clickOn("#toraRight").write("3660");
        robot.clickOn("#asdaRight").write("3660");
        robot.clickOn("#ldaRight").write("3660");

        robot.clickOn("#airports").clickOn("Heathrow");
        robot.clickOn("#runwayDoneButton");
    }

    public void runwayDefFillAll(FxRobot robot, String input) {
        robot.clickOn("#todaLeft").write(input);
        robot.clickOn("#toraLeft").write(input);
        robot.clickOn("#asdaLeft").write(input);
        robot.clickOn("#ldaLeft").write(input);

        robot.clickOn("#todaRight").write(input);
        robot.clickOn("#toraRight").write(input);
        robot.clickOn("#asdaRight").write(input);
        robot.clickOn("#ldaRight").write(input);

        robot.clickOn("#airports").clickOn("Heathrow");
        robot.clickOn("#runwayDoneButton");
    }

    public void clearTextField(FxRobot robot, int size) {
        for (int i = 0; i < size; i++) { robot.push(KeyCode.BACK_SPACE); }
    }
}