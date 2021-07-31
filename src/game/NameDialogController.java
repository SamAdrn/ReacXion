package game;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Dialog;

/**
 * This class serves as the controller class for <code>nameDialog.fxml</code>.<br><br>
 * Mainly used to retrieve a <code>String</code> value used to give <code>Player</code>
 * objects with a name.
 *
 * @author Samuel A. Kosasih
 *
 * @see Dialog
 */
public class NameDialogController {

    /**
     * This field refers to the <code>TextField</code> used to retrieve the user's desired name for
     * a <code>Player</code> object.
     */
    @FXML
    private TextField nameTextField;

    /**
     * Retrieves the <code>String</code> value entered in the <code>nameTextField</code> field.
     *
     * @return a <code>String</code> value used to provide <code>Player</code> objects with a name.
     */
    public String processResults() {
        String name = nameTextField.getText().trim();
        if (name.equals("")) {
            return "Unknown";
        }
        return name;
    }

}
