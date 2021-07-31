package game;

import java.io.Serializable;

/**
 * This class represents a <code>Player</code> object.<br><br>
 * Used to store the player's name, their best times, and the total number of attempts made.
 * Provides mutators and accessors to the fields stored in the class.
 *
 * @author Samuel A. Kosasih
 */
public class Player implements Serializable {

    /**
     * This field stores the player's name as a <code>String</code>.
     */
    private String name;

    /**
     * This field stores the player's best time as a <code>Double</code>.
     */
    private Double bestTime;

    /**
     * This field stores the player's total number of attempts as an <code>Integer</code>.
     */
    private int attempts;

    /**
     * Default Constructor.
     *
     * @param name the player's name as a <code>String</code>
     */
    public Player(String name) {
        this.name = name;
        this.bestTime = null;
        attempts = 0;
    }

    /**
     * Retrieves the player's name.
     *
     * @return the player's name as a <code>String</code> value
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name with a new given name.
     *
     * @param name the new given name. Represented as a <code>String</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the player's best time.
     *
     * @return the player's best time as a <code>Double</code> value
     */
    public Double getBestTime() {
        return bestTime;
    }

    /**
     * Refreshes the player's best time with a new potentially faster time.
     *
     * @param time the new time achieved by the user
     * @return <code>true</code> if the new time is the user's first attempt, or if the new time is
     * better than the value stored in <code>bestTime</code>. Otherwise, it will return <code>false</code>.
     */
    public boolean refreshBestTime(double time) {
        increaseAttempts();
        if (this.bestTime == null) {
            this.bestTime = time;
            return true;
        }
        if (time < this.bestTime) {
            this.bestTime = time;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the number of attempts made by the user.
     *
     * @return the number of attempts as an <code>Integer</code> value.
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Increases the number of attempts made by the user. <br><br>
     * Usually called whenever the user has completed a game.
     */
    private void increaseAttempts() {
        attempts++;
    }

    /**
     * Provides a <code>String</code> representation of the <code>Player</code> object.
     *
     * @return the <code>Player</code>'s name as it's <code>String</code> representation
     */
    @Override
    public String toString() {
        return name;
    }
}
