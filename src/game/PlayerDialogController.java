package game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Optional;

/**
 * This class serves as the controller class for <code>playerDialog.fxml</code>.<br><br>
 * Handles the application logic for users to handle the <code>Player</code> database, that allows them
 * to select from their created <code>Player</code>s, delete them, or edit their names. These options
 * appear together as a <code>Dialog</code>, that the user must handle first before moving on with the
 * main window.
 *
 * @see Dialog
 */
public class PlayerDialogController {

    /**
     * This refers to the main <code>DialogPane</code> of the <code>playerDialog.fxml</code> file.
     */
    @FXML
    private DialogPane dialogPane;

    /**
     * This field refers to the <code>ListView</code> of <code>Player</code>s located at the left
     * position of the main <code>BorderPane</code>.
     */
    @FXML
    private ListView<Player> playerListView;

    /**
     * This field refers to the <code>Label</code> used to display the <code>Player</code>'s name.
     */
    @FXML
    private Label playerNameLabel;

    /**
     * This field refers to the hidden <code>TextField</code> that only appears when the user
     * decides to edit a <code>Player</code>'s name.
     */
    @FXML
    private TextField playerNameTextField;

    /**
     * This field refers to the <code>Label</code> used to display the <code>Player</code>'s best
     * achieved time.
     */
    @FXML
    private Label timeLabel;

    /**
     * This field refers to the <code>Label</code> used to display the total number of attempts
     * made by a <code>Player</code>.
     */
    @FXML
    private Label attemptsLabel;

    /**
     * This field refers to the <code>Button</code> used to enable edit mode for the
     * <code>Player</code>'s name.
     */
    @FXML
    private Button editButton;

    /**
     * This field refers to the <code>Button</code> used to save the edits made by the user on edit mode.
     */
    @FXML
    private Button doneButton;

    /**
     * This field refers to the <code>Button</code> used to delete <code>Player</code>s.
     */
    @FXML
    private Button deleteButton;

    /**
     * This field refers to the <code>Button</code> used to cancel edit mode and unsave any changes.
     */
    @FXML
    private Button cancelButton;

    /**
     * This field stores a <code>PlayerManager</code> type variable that allows the data handling
     * and storage of <code>Player</code> objects.
     */
    private PlayerManager pm;

    /**
     * This field is a <code>DecimalFormat</code> variable used to format <code>Double</code> variables to be displayed
     * with three (3) decimal places.
     */
    private final DecimalFormat df = new DecimalFormat("#.###");

    /**
     * This field is a <code>Player</code> object variable used to store the currently-selected <code>Player</code>.
     */
    private Player selectedPlayer;

    /**
     * Initializes mostly the <code>playerListView</code> to display <code>Player</code>s to the user.<br>
     * <br><b>This method must be called first before displaying the <code>Dialog</code></b>, to import a
     * <code>PlayerManager</code> object being used by the main application to read all
     * <code>Player</code> data. <br><br>
     * Populates the <code>playerListView</code> by calling <code>populatePlayerList()</code> method,
     * and adds the functionality to display <code>Player</code> data whenever the user selects one
     * from <code>playerListView</code>. If the user is not currently selecting any <code>Player</code>,
     * then the <code>deleteButton</code> and the <code>editButton</code> will be disabled.
     *
     * @param playerManager the <code>PlayerManager</code> object from the main application
     *                      to be stored as a class field.
     *
     * @see PlayerManager
     */
    public void initialize(PlayerManager playerManager) {
        pm = playerManager;

        populatePlayerList();

        playerListView.getSelectionModel().selectedItemProperty().addListener((observableValue, player, p1) -> {
            if (p1 != null) {
                Player p = playerListView.getSelectionModel().getSelectedItem();
                playerNameLabel.setText(p.getName());
                timeLabel.setText(p.getBestTime() == null ? "N/A" : df.format(p.getBestTime()));
                attemptsLabel.setText(p.getAttempts() + "");
                editButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                playerNameLabel.setText("N/A");
                timeLabel.setText("N/A");
                attemptsLabel.setText("N/A");
                editButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });
        playerListView.getSelectionModel().selectFirst();
    }

    /**
     * Handles application logic to make edits to a <code>Player</code>.<br><br>
     * Will show the <code>playerTextField</code> to allow the user to enter a new desired
     * name for the <code>Player</code>.
     * The <code>deleteButton</code> and the <code>editButton</code> itself will disappear upon
     * method call, and will instead be replaced by the <code>cancelButton</code> and the
     * <code>doneButton</code>.
     */
    @FXML
    private void handleEdit() {
        selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
        playerListView.setDisable(true);
        editButton.setVisible(false);
        playerNameLabel.setVisible(false);
        deleteButton.setVisible(false);
        playerNameTextField.setText(selectedPlayer.getName());
        playerNameTextField.setVisible(true);
        cancelButton.setVisible(true);
        doneButton.setVisible(true);
    }

    /**
     * Handles application logic to quit from edit mode.<br><br>
     * The <code>playerTextField</code>, <code>doneButton</code> and the <code>cancelButton</code>
     * itself will disappear upon method call, and will instead be replaced by the
     * <code>editButton</code> and the <code>deleteButton</code>, as it was in normal view. <br><br>
     * <b>Any changes made in edit mode will not be saved.</b>
     */
    @FXML
    private void handleCancel() {
        playerListView.setDisable(false);
        editButton.setVisible(true);
        playerNameLabel.setVisible(true);
        deleteButton.setVisible(true);
        playerNameTextField.setVisible(false);
        doneButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    /**
     * Handles the application logic to save any changes made in edit mode.<br><br>
     * Will first retrieve the new name from <code>playerNameTextField</code> and checks whether
     * the name is being used by any other <code>Player</code>s being stored in the class'
     * <code>PlayerManager</code> object. <br><br>
     * If a duplicate is found, an <code>Alert</code> will be shown indicating that the user must
     * select a different name. <br><br>
     * If not, or if the new name equals the original name, then the method will proceed to save
     * the changes and revert back to normal view.
     *
     * @see Alert
     */
    @FXML
    private void handleDone() {
        String newName = playerNameTextField.getText();
        boolean proceed;
        if (newName.equals(selectedPlayer.getName())) {
            proceed = true;
        } else {
            proceed = true;
            for (Player player : playerListView.getItems()) {
                if (player.getName().equals(newName)) {
                    proceed = false;
                    break;
                }
            }
        }
        if (proceed) {
            selectedPlayer.setName(newName);
            populatePlayerList();
            playerListView.getSelectionModel().select(selectedPlayer);
            playerListView.setDisable(false);
            editButton.setVisible(true);
            playerNameLabel.setVisible(true);
            deleteButton.setVisible(true);
            playerNameTextField.setVisible(false);
            doneButton.setVisible(false);
            cancelButton.setVisible(false);
            cancelButton.fire();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Name change unsuccessful");
            alert.setHeaderText("Name duplicate found");
            alert.setContentText("An existing player with the chosen name is found. " +
                    "Please select another name");
        }
    }

    /**
     * Handles application logic to delete a <code>Player</code>.<br><br>
     * Will first retrieve the selected <code>Player</code> and asks the user to confirm
     * deletion through an <code>Alert</code>. <br><br>
     * If the user confirms, then it will call the <code>PlayerManager</code>'s
     * <code>removePlayer(String playerName)</code> method to remove it from
     * the database.
     *
     * @see PlayerManager
     * @see Alert
     */
    @FXML
    private void handleDelete() {
        selectedPlayer = playerListView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Player");
        alert.setHeaderText("Please confirm!");
        alert.setContentText("Are you sure you want to delete " + selectedPlayer.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            if (pm.removePlayer(selectedPlayer.getName())) {
                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setTitle("Removal Successful");
                done.setHeaderText("Removed " + selectedPlayer.getName());
                done.setContentText("You have removed " + selectedPlayer.getName() + ".");
                populatePlayerList();
                playerListView.getSelectionModel().selectFirst();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Removal Unsuccessful");
                error.setHeaderText("Player not found.");
                error.setContentText("The player: " + selectedPlayer.getName() + " is not found." +
                        "Perhaps it has already been deleted. If that is not the case, please contact" +
                        "developer.");
            }
        }
    }

    /**
     * Handles application logic to add a new <code>Player</code>.<br><br>
     * Loads the <code>nameDialog.fxml</code> file and displays a <code>Dialog</code>
     * to allow the user to enter a name to be given to the new <code>Player</code> object.
     * <br><br>
     * If the new name is not currently being used by any other <code>Player</code>s in the
     * <code>PlayerManager</code> database, then it will proceed to add the new <code>Player</code>
     * by calling the <code>PlayerManager</code>'s <code>addPlayer(String newName)</code> method.
     * <br><br>
     * Otherwise, an <code>Alert</code> will be displayed to indicate that a duplicate <code>Player</code>
     * object is found, and will ask the user to choose another name.
     *
     * @see PlayerManager
     * @see Dialog
     * @see Alert
     */
    @FXML
    private void handleAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(dialogPane.getScene().getWindow());
        dialog.setTitle("Select a player");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nameDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            showDialogError();
            e.printStackTrace();
        }
        NameDialogController controller = loader.getController();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        while (true) {
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                String name = controller.processResults();
                if (pm.addPlayer(name)) {
                    playerListView.getSelectionModel().select(pm.getPlayer(name));
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Could not add new player");
                    alert.setHeaderText("Name duplicate found");
                    alert.setContentText("An existing player with the chosen name is found. Please select another name");
                }
            } else {
                break;
            }
        }
        populatePlayerList();
    }

    /**
     * Populates the <code>playerListView</code> with <code>Player</code> objects.<br><br>
     * The <code>Player</code> objects are read from the <code>PlayerManager</code> object
     * imported at the <code>initialize()</code> method.
     *
     * @see PlayerManager
     */
    private void populatePlayerList() {
        ObservableList<Player> list = FXCollections.observableArrayList();
        Iterator<Player> it = pm.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        playerListView.setItems(list);
    }

    /**
     * Retrieves the selected <code>Player</code> object from <code>playerListView</code>.
     *
     * @return the selected <code>Player</code> that will be updated at every game.
     */
    public Player processResults() {
        return playerListView.getSelectionModel().getSelectedItem();
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

}
