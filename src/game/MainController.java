package game;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class serves as the controller class for <code>mainWindow.fxml</code>.<br><br>
 * Handles most of the application logic, from the creation of some UI components, to giving functionalities to all of
 * them. <br><br>
 * In order for this class to function, a <code>Player</code> object must first be selected, highlighting the reason why
 * the <code>selectNewPlayer()</code> method is called early in the program. Once a <code>Player</code> has been
 * selected, the class serves only to the <code>selectedPlayer</code>, until a new <code>Player</code> is selected from
 * the <code>selectNewPlayer()</code> method by clicking on <code>selectButton</code>. All updates will be handled by
 * calling methods from a <code>PlayerManager</code> object stored in the class.<br><br>
 * Time-related functionalities are served using the <code>Timer</code> class, and <code>SubTimer</code> inner class
 * which extends <code>TimerTask</code>.<br><br>
 * <b>Note that any changes made using the <code>PlayerManager</code> field may not be saved if the application crashes</b>
 *
 * @author Samuel A. Kosasih
 * @see Player
 * @see PlayerManager
 * @see Timer
 * @see TimerTask
 */
public class MainController {

    /**
     * This field refers to the <code>VBox</code> located at the center position of our <code>mainPane</code>
     */
    @FXML
    private VBox centerPane;

    /**
     * This field refers to the <code>Button</code> located in our <code>ToolBar</code>.<br><br>
     * Functions as a launcher to our <code>playerDialog.fxml</code> dialog to select new players.
     */
    @FXML
    private Button selectButton;

    /**
     * This field refers to the <code>Button</code> located in our <code>ToolBar</code>.<br><br>
     * Handles the exit operation of the application.
     */
    @FXML
    private Button exitButton;

    /**
     * This field refers to the <code>Label</code> located at the <code>bottom</code> position of our
     * <code>mainPane</code><br><br>
     * Functions as an indicator to which <code>Player</code> object is currently being
     * selected.
     */
    @FXML
    private Label statusLabel;

    /**
     * This field stores time-related UI displays as a <code>VBox</code> layout.
     */
    private VBox timeBox;

    /**
     * This field is a <code>Label</code> used to communicate instructions to the users.
     */
    private Label instructionsLabel;

    /**
     * This field is a <code>Label</code> used to display the user's result reaction time in each game.
     */
    private Label timeLabel;

    /**
     * This field is a <code>Label</code> used to indicate the position where the user must click to play the game.
     */
    private Label clickHereLabel;

    /**
     * This field is a <code>Button</code> used to start a game at the user's command.
     */
    private Button startButton;

    /**
     * This field is a <code>Rectangle</code> graphic which alternates between red and green, indicating when the user
     * should click on the <code>clickArea</code>.
     */
    private Rectangle lights;

    /**
     * This field is a <code>Rectangle</code> graphic indicating the area where the user should click in order to
     * submit their reaction times.
     */
    private Rectangle clickArea;

    /**
     * This field is a <code>Timer</code> used to handle time-related functionalities, such as setting the countdown
     * for the <code>lights</code> to turn from red to green.
     */
    private Timer timer;

    /**
     * This field is a <code>DecimalFormat</code> variable used to format <code>Double</code> variables to be displayed
     * with three (3) decimal places.
     */
    private final DecimalFormat df = new DecimalFormat("#.###");

    /**
     * This field is a <code>PlayerManager</code>, used to handle <code>Player</code>-related functionalities.
     */
    private PlayerManager playerManager;

    /**
     * This field is a <code>Player</code> object variable used to store the currently-selected <code>Player</code>.
     */
    private Player selectedPlayer;

    /**
     * This field is a <code>Boolean</code> used to indicate whether the UI components has been initialized.
     */
    private boolean initialized = false;

    /**
     * This field is an <code>EventHandler</code> of type <code>KeyEvent</code> to handle an event at
     * which the user presses the ENTER key.
     */
    private EventHandler<KeyEvent> handleEnter;

    /**
     * This field is an <code>EventHandler</code> of type <code>KeyEvent</code> to handle an event at
     * which the user presses the ENTER key too early and fails.
     */
    private EventHandler<KeyEvent> handleEnterFail;

    /**
     * This field stores a <code>Long</code> value to store the system's time at the point the
     * <code>run()</code> method is called and the <code>lights</code> turn green.
     */
    private long start;

    /**
     * Initializes most of the UI components to provide the user with interactions.<br><br>
     * This method is first called when the <code>Main</code> class loads the <code>mainWindow.fxml</code>
     * file into the <code>Scene</code>. It first instructs the user to select a <code>Player</code>, then
     * builds the UI components to be displayed start-up or for later use.
     *
     * @see Main
     */
    public void initialize() {
        playerManager = new PlayerManager();

        selectNewPlayer();

        lights = new Rectangle();
        lights.setWidth(350);
        lights.setHeight(150);

        StackPane clickPane = new StackPane();

        clickHereLabel = new Label("Click here (or press ENTER)");
        clickHereLabel.setFont(new Font("Arial", 18));
        clickHereLabel.setVisible(false);

        clickArea = new Rectangle();
        clickArea.setWidth(350);
        clickArea.setHeight(150);
        clickArea.setFill(Color.TRANSPARENT);
        clickArea.setStroke(Color.BLACK);

        timeBox = new VBox(5);
        timeBox.setAlignment(Pos.CENTER);

        timeLabel = new Label();
        timeLabel.setFont(new Font("Arial", 50));

        timeBox.getChildren().addAll(timeLabel, new Label("(in seconds)"));

        clickPane.getChildren().addAll(clickHereLabel, clickArea, timeBox);

        instructionsLabel = new Label();
        instructionsLabel.setFont(new Font("Arial", 18
        ));

        startButton = new Button();

        centerPane.setSpacing(10);
        centerPane.getChildren().addAll(lights, instructionsLabel, startButton, clickPane);

        selectButton.setOnAction(actionEvent -> selectNewPlayer());

        exitButton.setOnAction(actionEvent -> handleShutDown());

        initialized = true;

        timer = new Timer(true);

        handleEnter = keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                handleClick();
            }
        };

        handleEnterFail = keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                fail();
            }
        };

        before();
    }

    /**
     * Builds and prepares the UI for a game.<br><br>
     * Here, the <code>startButton</code> will start the game at the user's command, initiating
     * the <code>timer</code>'s schedule.
     */
    private void before() {
        lights.setFill(Color.ORANGERED);
        clickArea.setFill(Color.TRANSPARENT);
        instructionsLabel.setText("Click on the area below when the lights turn green");
        startButton.setText("Okay!");
        timeBox.setVisible(false);
        startButton.setOnAction(actionEvent -> {
            timer = new Timer(true);
            int delay = getRandInt();
            timer.schedule(new StartGame(), delay * 1000L);
            selectButton.setDisable(true);
            exitButton.setDisable(true);
            clickHereLabel.setVisible(true);
            instructionsLabel.setText("Get Ready...");
            startButton.setVisible(false);
            clickArea.setDisable(false);
            clickArea.requestFocus();
            clickArea.addEventHandler(KeyEvent.KEY_PRESSED, handleEnterFail);
            clickArea.setOnMouseClicked(mouseEvent -> fail());
        });
    }

    /**
     * Builds the UI to display an indication that the user has failed the game.<br><br>
     * This method is only called whenever the user interacts with the <code>clickArea</code> too early.
     * It is programmed specifically to prevent users from spamming clicks, which ruins the competitive
     * nature of the game.
     */
    private void fail() {
        timer.cancel();
        selectButton.setDisable(false);
        exitButton.setDisable(false);
        instructionsLabel.setText("Don't click before the light turns green!");
        clickHereLabel.setVisible(false);
        startButton.setVisible(true);
        startButton.setText("Okay :(");
        startButton.setOnAction(actionEvent -> before());
        clickArea.setFill(Color.BLACK);
        clickArea.removeEventHandler(KeyEvent.KEY_PRESSED, handleEnterFail);
        clickArea.setOnMouseClicked(mouseEvent -> {});
        System.out.println("Fail method called");
    }

    /**
     * Builds the UI to display the game results.<br><br>
     * Here, the <code>startButton</code> will call the <code>before()</code> method to revert the UI
     * back, preparing itself for a game.
     */
    private void after() {
        startButton.setText("Try Again?");
        startButton.setVisible(true);
        startButton.requestFocus();
        startButton.setOnAction(actionEvent -> before());
        clickArea.setFill(Color.WHITE);
        clickArea.removeEventHandler(KeyEvent.KEY_PRESSED, handleEnterFail);
        clickArea.removeEventHandler(KeyEvent.KEY_PRESSED, handleEnter);
        clickArea.setOnMouseClicked(mouseEvent -> {});
        System.out.println("After method called");
    }

    /**
     * Generates an <code>Integer</code> value used to set the <code>timer</code> delay.
     *
     * @return an <code>Integer</code> value of range 2-10 inclusive
     */
    private int getRandInt() {
        return new Random().nextInt(11 - 2) + 2;
    }

    /**
     * Launches the <code>playerDialog.fxml</code> file to allow the user to select a new <code>Player</code>.
     *
     * @see Dialog
     */
    private void selectNewPlayer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Select a player");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            e.printStackTrace();
        }
        PlayerDialogController controller = loader.getController();
        controller.initialize(playerManager);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No player selected");
        alert.setHeaderText("Please select a player.");
        alert.setContentText("We cannot proceed without selecting a player.");

        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                Player p = controller.processResults();
                if (p != null) {
                    selectedPlayer = p;
                    statusLabel.setText("Selected Player: " + p.getName());
                    if (initialized) {
                        before();
                    }
                    break;
                } else {
                    alert.showAndWait();
                }
            } else {
                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the exit event of the application.<br><br>
     * Saves <code>Player</code> progress to the class' <code>PlayerManager</code>.
     */
    public void handleShutDown() {
        if (!playerManager.save()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setContentText("Database not saved. Data may be lost. Please contact developer.");
            alert.showAndWait();
        }
        System.out.println("Application Exited");
        Platform.exit();
    }

    /**
     * Displays an <code>Alert</code> to indicate that a <code>Dialog</code> has failed to be shown.
     *
     * @see Alert
     */
    private void showDialogError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setContentText("Failed to load dialog. Please contact developer.");
        alert.show();
    }

    /**
     * Handles all the operations to be run when the user interacts with <code>clickArea</code>.
     */
    public void handleClick() {
        long timeElapsed = System.nanoTime() - start;
        double converted = timeElapsed / 1000000000.0;
        timeLabel.setText(df.format(converted));
        timeBox.setVisible(true);
        clickHereLabel.setVisible(false);
        selectButton.setDisable(false);
        exitButton.setDisable(false);
        if (selectedPlayer.refreshBestTime(converted)) {
            instructionsLabel.setText("Congratulations. New Best Time: " + df.format(selectedPlayer.getBestTime()));
        } else {
            instructionsLabel.setText("Well done. (Best Time: " + df.format(selectedPlayer.getBestTime()) + ")");
        }
        after();

    }

    /**
     * This class is a private inner class containing the code to handle game actions.<br><br>
     * The <code>run()</code> method implemented from the <code>TimerTask</code> class will be executed
     * immediately upon the duration of the <code>timer</code>'s delay.
     *
     * @author Samuel A. Kosasih
     * @see TimerTask
     */
    private class StartGame extends TimerTask {

        /**
         * The actions to be performed by this timer task as the <code>timer</code>'s delay
         * duration has elapsed.
         */
        @Override
        public void run() {
            Platform.runLater(() -> {
                lights.setFill(Color.GREEN);
                instructionsLabel.setText("CLICK NOW");
                start = System.nanoTime();
                clickArea.requestFocus();
                clickArea.removeEventHandler(KeyEvent.KEY_PRESSED, handleEnterFail);
                clickArea.addEventHandler(KeyEvent.KEY_PRESSED, handleEnter);
                clickArea.setOnMouseClicked(mouseEvent -> handleClick());
            });
        }


    }

}


