package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class serves as a manager class to manage <code>Player</code> objects stored in
 * <code>PlayerDatabase</code>.<br><br>
 * Constructing the object will first scan for an object file (.ser) stored within the
 * project files. If found, then it will read existing data from the file.<br><br>
 * Also provides means to save any changes made to the database.
 *
 * @author Samuel A. Kosasih
 *
 * @see Player
 * @see PlayerDatabase
 */
public class PlayerManager implements Serializable {

    /**
     * This field stores a <code>PlayerDatabase</code> object used within the class.<br><br>
     * Access currently saved <code>Player</code> objects using this field.
     */
    private PlayerDatabase database;

    /**
     * This field stores a <code>File</code> object used for file handling purposes.<br><br>
     * Refers to the file name <code>players.ser</code> stored within the project files.
     */
    private final File file = new File("players.ser");

    /**
     * Default Constructor.<br><br>
     * Reads from an object file referred by the file name <code>players.ser</code> stored
     * within the project files. If not found, it will proceed with a new <code>PlayerDatabase</code>.
     *
     * @see PlayerDatabase
     */
    public PlayerManager() {
        if (!read()) {
            this.database = new PlayerDatabase();
            addPlayer("Anonymous");
        }
    }

    /**
     * Creates and adds a new <code>Player</code> with the given name to the database.
     *
     * @param newName the name to be given to the new <code>Player</code> as a <code>String</code>
     * @return <code>true</code> if there are no duplicate <code>Player</code>s with the same name,
     * and the <code>Player</code> object is successfully created and saved. Otherwise, it will
     * return <code>false</code>.
     *
     * @see Player
     */
    public boolean addPlayer(String newName) {
        for (Player p : database.playerList) {
            if (p.getName().equals(newName)) {
                return false;
            }
        }
        return database.playerList.add(new Player(newName));
    }

    /**
     * Removes the <code>Player</code> object with the given name from the database.
     *
     * @param playerName the name of the <code>Player</code> to be removed as a <code>String</code>
     * @return <code>true</code> if the <code>Player</code> with the <code>playerName</code> is
     * found and successfully deleted. Otherwise, it will return <code>false</code>.
     *
     * @see Player
     */
    public boolean removePlayer(String playerName) {
        for (Player p : database.playerList) {
            if (p.getName().equals(playerName)) {
                return database.playerList.remove(p);
            }
        }
        return false;
    }

    /**
     * Retrieves the <code>Player</code> with the given name from the database.
     *
     * @param playerName the given name of the <code>Player</code> to be retrieved as a
     *                   <code>String</code>
     * @return the <code>Player</code> object with <code>playerName</code>. If <code>Player</code>
     * is not found, then it will return <code>null</code> instead.
     */
    public Player getPlayer(String playerName) {
        for (Player p : database.playerList) {
            if (p.getName().equals(playerName)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Provides an <code>Iterator</code> to iterate through the database.
     *
     * @return an <code>Iterator</code> of type <code>Player</code> from <code>PlayerDatabase</code>
     *
     * @see Iterator
     * @see Player
     */
    public Iterator<Player> iterator() {
        return database.iterator();
    }

    /**
     * Saves any changes made to the database within the session.<br><br>
     * This method will write the <code>PlayerDatabase</code> object to a file referred by
     * the field <code>file</code>.
     *
     * @return <code>true</code> if the session is successfully saved. Otherwise it will return <code>false</code>
     *
     * @see PlayerDatabase
     * @see ObjectOutputStream
     * @see FileOutputStream
     */
    public boolean save() {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(database);
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    /**
     * Reads existing data to the <code>PlayerDatabase</code> from a file referred by the field
     * <code>file</code> for the current session.
     *
     * @return <code>true</code> if file is found and data is successfully read. Otherwise, it will return
     * <code>false</code>.
     *
     * @see PlayerDatabase
     * @see ObjectInputStream
     * @see FileInputStream
     */
    private boolean read() {
        if (file.exists()) {
            try (ObjectInputStream input =
                         new ObjectInputStream(new FileInputStream(file))) {
                this.database = (PlayerDatabase) input.readObject();
                return true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Data not read. Application is still safe to run without loaded data.");
                return false;
            }
        }
        return false;
    }

    /**
     * This private static inner class serves as the data model used only by the <code>PlayerManager</code> class
     * to store <code>Player</code> objects.<br><br>
     * Uses a <code>List</code> implementation <code>ArrayList</code> of type <code>Player</code> as a
     * data structure. Efficiency will deteriorate as more <code>Player</code> objects are stored.
     *
     * @author Samuel A. Kosasih
     *
     * @see Player
     */
    private static class PlayerDatabase implements Serializable {

        /**
         * This field stores a <code>List</code> of type <code>Player</code>.<br><br>
         * Serves as the class' data structure to store <code>Player</code> objects.
         *
         * @see List
         */
        private final List<Player> playerList;

        /**
         * Default Constructor. <br><br>
         * Instantiates a new <code>ArrayList</code> of type <code>Player</code> to store
         * <code>Player</code> objects
         *
         * @see ArrayList
         */
        public PlayerDatabase() {
            this.playerList = new ArrayList<>();
        }

        /**
         * Provides an <code>Iterator</code> to iterate through <code>playerList</code>.
         *
         * @return an <code>Iterator</code> of type <code>Player</code>
         *
         * @see Iterator
         * @see Player
         */
        public Iterator<Player> iterator() {
            return playerList.iterator();
        }

    }

}
