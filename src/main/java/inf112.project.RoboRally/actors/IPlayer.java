package inf112.project.RoboRally.actors;

import inf112.project.RoboRally.cards.ICard;
import inf112.project.RoboRally.cards.IDeck;
import inf112.project.RoboRally.objects.GridDirection;

import java.util.List;

public interface IPlayer {

    /**
     * @return x-coordinate of player
     */
    int getX();

    /**
     * @return y-coordinate of player
     */
    int getY();

    /**
     * @return The direction the player currently faces.
     */
    GridDirection getPlayerDirection();

    /**
     * Move the player as instructed on the given program card.
     *
     * @param card
     *             The card containing the instructions which are to be executed.
     */
    void movePlayer(ICard card);


    /**
     * Get the amount of damage the player has taken.
     *
     * @return The number of damage tokens.
     */
    int getPlayerDamage();


    /**
     * Deal one point of damage to this player.
     * Increments the number of damage tokens.
     */
    void takeOneDamage();


    /**
     * This player repairs one point of damage.
     * Decrements the number of damage tokens.
     */
    void removeOneDamage();


    /**
     * Check the amount of damage the player has sustained,
     * and trigger effects accordingly.
     */
    void assessCurrentDamage();


    /**
     *
     *
     *
     *
     */
    void destroyPlayer();


    /**
     * Lock N registers of the players register.
     *
     * @param numberOfRegisters
     *                          The number of registers to be locked.
     */
    void lockNRegisters(int numberOfRegisters);


    /**
     * Takes a list of cards, and adds them to the players hand.
     *
     * @param cards
     *              The cards to be added.
     */
    void receiveCards(List<ICard> cards);


    /**
     *
     *
     *
     */
    void addListOfCardsToProgramRegister(List<ICard> cards);


    /**
     * Player discards their hand.
     */
    void removeRemainingCardsInHand();


    /**
     * Reveal the card in the players chosen registry slot.<br>
     * The card is not removed from the registry.
     *
     * @param registerNumber
     *                      The register slot whose contents are to be revealed.
     * @return A reference to the card residing in the register slot, if any.<br>
     *
     * @throws IllegalArgumentException
     *        if the slotNumber is negative (slotNumber < 0),<br>
     *        slotNumber is too high (slotNumber > register.getSize()),<br>
     *        or if slotNumber is null (slotNumber = null).
     */
    ICard revealProgramCardForRegisterNumber(Integer registerNumber);


    /**
     * Remove all unlocked cards from this players registry.<br>
     * Cards in locked slots, remain in their positions.
     */
    void clearRegister();


    /**
     *
     *
     *
     * @return true if the player was destroyed<br>
     *         false otherwise.
     */
    boolean wasDestroyedThisTurn();


    /**
     *
     *
     *
     *
     */
    void respawnAtLastArchiveMarker();


    /**
     * Get the cards currently held by the player.
     *
     * @return The players deck of cards.
     */
    IDeck getCardsInHand();


    /**
     * Get the name of the player.
     *
     * @return The players current name.
     */
    String getName();
    
    /**
     * A method for setting backup point to the given position
     */
    void setThisPointAsNewBackup();
    
    /**
     * A method for obtaining the number of flags that have been visited by the IPlayer
     * @return The number of flags visited
     */
    int getFlagsVisited();
    
    /**
     * Add a flag to the number of visited flags
     */
    void addNewFlagVisited();
    
    /**
     * A method for moving the player one step in a given direction
     * @param direction The direction to be moved
     */
    void movePlayer(GridDirection direction);
    
    /**
     * A method for rotating the player to the left (counter-clockwise)
     */
    void rotateLeft();
    
    /**
     * A method for rotating the player to the right (clockwise)
     */
    void rotateRight();
    
    /**
     * A method for rotating the player to the opposite direction
     */
    void uTurn();

    /**
     * For use in drawing the player
     * @return Texture location and name in string form ex. assets/textureName.png
     */
    String getTexture();
}
