package inf112.project.RoboRally.actors;

import com.badlogic.gdx.graphics.Color;
import inf112.project.RoboRally.cards.*;
import inf112.project.RoboRally.objects.GridDirection;
import inf112.project.RoboRally.objects.Laser;

import java.util.ArrayList;
import java.util.List;

public class Player implements IPlayer {
    private boolean poweringDownNextTurn;
    private int lives;
    private GridDirection playerDirection;
    private int x,y;
    private int backupX, backupY;
    private String name;
    private IDeck cardsInHand;
    private int numberOfDamageTokensRecieved;
    private IProgramRegister register;
    private int flagsVisited;
    private boolean wasDestroyedThisTurn;
    private Laser laser;
    private Color color;
    private List<Coordinates> pathOfPlayer;
    private ICard lastPlayedCard;

    private static int counter = 0;
    private int priority;
    private boolean cardSelectionConfirmed;
    private boolean powerdDown;

    public Player(String name, int x, int y, Color color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.backupX = this.x;
        this.backupY = this.y;
        this.name = name;
        this.playerDirection = GridDirection.NORTH;
        this.cardsInHand = new Deck();
        this.numberOfDamageTokensRecieved = 0;
        this.lives = 3;
        this.register = new ProgramRegister();
        this.flagsVisited = 0;
        this.wasDestroyedThisTurn = false;
        this.laser = new Laser(1, this);
        this.priority = counter++;
        this.poweringDownNextTurn = false;
        this.powerdDown = false;
        this.cardSelectionConfirmed = false;
    }

    public void resetPathOfPlayer() {
        pathOfPlayer = new ArrayList<>();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getBackupX() {
        return backupX;
    }

    public int getBackupY() {
        return backupY;
    }

    @Override
    public void setThisPointAsNewBackup() {
        this.backupX=this.x;
        this.backupY=this.y;
        System.out.println("Player " + this.getName() +
                " has moved their Archive location to" +
                "\nx: " + backupX +
                "\ny: " + backupY);
    }

    @Override
    public int getFlagsVisited() {
        return flagsVisited;
    }

    @Override
    public void addNewFlagVisited() {
        flagsVisited++;
    }

    @Override
    public int getPriority() {
        return  this.priority;
    }

    @Override
    public GridDirection getPlayerDirection() {
        return playerDirection;
    }

    @Override
    public List<Coordinates> getPathOfPlayer() {
        return pathOfPlayer;
    }

    @Override
    public void movePlayer(ICard card) {
        if (card == null) {
            throw new IllegalArgumentException();
        }
        lastPlayedCard = card;
        GridDirection playersCurrentDirection = this.playerDirection;
        Action cardCommand = card.getCardCommand();
        switch (cardCommand) {
            case ROTATE_RIGHT: rotateRight(); pathOfPlayer = new ArrayList<>(); break;
            case ROTATE_LEFT: rotateLeft(); pathOfPlayer = new ArrayList<>(); break;
            case U_TURN: uTurn(); pathOfPlayer = new ArrayList<>(); break;
            case FORWARD_1: pathOfPlayer =  moveInDirection(playersCurrentDirection, 1); break;
            case FORWARD_2: pathOfPlayer = moveInDirection(playersCurrentDirection, 2); break;
            case FORWARD_3: pathOfPlayer =  moveInDirection(playersCurrentDirection, 3); break;
            case BACKWARDS: pathOfPlayer =  moveInDirection(opposite(), 1); break;
        }
    }

    @Override
    public void movePlayer(GridDirection direction) {
        if (direction == GridDirection.NORTH) {
            pathOfPlayer = moveInDirection(direction, 1);
        } else if (direction == GridDirection.WEST) {
            pathOfPlayer = moveInDirection(direction, 1);
        } else if (direction == GridDirection.SOUTH) {
            pathOfPlayer = moveInDirection(direction, 1);
        } else {
            pathOfPlayer = moveInDirection(direction, 1);
        }
    }

    @Override
    public boolean equals(Object obj) {
        Player player = (Player) obj;
        return (x == player.getX() && y == player.getY());
    }

    @Override
    public int getPlayerDamage() {
        return this.numberOfDamageTokensRecieved;
    }

    @Override
    public void takeOneDamage() {
        this.numberOfDamageTokensRecieved += 1;
        System.out.println(this.name + " has " + numberOfDamageTokensRecieved
        + " damage tokens, and has " + lives + " lives remaining.");
        this.assessCurrentDamage();
    }

    @Override
    public void setPathOfPlayer(List<Coordinates> pathOfPlayer) {
        this.pathOfPlayer = pathOfPlayer;
    }

    @Override
    public void removeOneDamage() {
        if (this.numberOfDamageTokensRecieved > 0) {
            this.numberOfDamageTokensRecieved -= 1;
            this.assessCurrentDamage();
        }
    }

    @Override
    public void assessCurrentDamage() {
        int currentDamageTaken = this.numberOfDamageTokensRecieved;
        switch (currentDamageTaken) {
            case 5: this.lockNRegistersAndUnlockMRegisters(1, 4);
                    break;
            case 6: this.lockNRegistersAndUnlockMRegisters(2, 3);
                    break;
            case 7: this.lockNRegistersAndUnlockMRegisters(3, 2);
                    break;
            case 8: this.lockNRegistersAndUnlockMRegisters(4, 1);
                    break;
            case 9: this.lockNRegistersAndUnlockMRegisters(5, 0);
                    break;
            default: this.unlockNRegisters(5); break;
        }
    }

    /**
     * Handles the locking and unlocking of registers when the player is damaged.
     *
     * @param numberOfRegistersToLock
     *                              The number of registers that should be locked.
     * @param numberOfRegistersToUnlock
     *                              The number of registers that should be unlocked.
     *
     * @throws IllegalArgumentException
     *       if numberOfRegistersToLock == null,
     *       or numberOfRegistersToUnLock == null,
     *       or numberOfRegistersToLock < 0,
     *       or numberOfRegistersToUnLock < 0,
     *       or numberOfRegistersToLock > register.getSize(),
     *       or numberOfRegistersToUnLock > register.getSize().
     */
    private void lockNRegistersAndUnlockMRegisters(Integer numberOfRegistersToLock,
                                                  Integer numberOfRegistersToUnlock) {
        if (numberOfRegistersToLock == null
                || numberOfRegistersToLock < 0
                || numberOfRegistersToLock > this.register.getNumberOfRegisterSlots()) {
            throw new IllegalArgumentException();
        }
        if (numberOfRegistersToUnlock == null
                || numberOfRegistersToUnlock < 0
                || numberOfRegistersToUnlock > this.register.getNumberOfRegisterSlots()) {
            throw new IllegalArgumentException();
        }
        this.lockNRegisters(numberOfRegistersToLock);
        this.unlockNRegisters(numberOfRegistersToUnlock);
    }


    @Override
    public void unlockNRegisters(Integer numberOfSlotsToUnlock) {
        if (numberOfSlotsToUnlock == null
                || numberOfSlotsToUnlock < 0
                || numberOfSlotsToUnlock > this.register.getNumberOfRegisterSlots()) {
            throw new IllegalArgumentException();
        }
        for (int slotNumber = 0; slotNumber < numberOfSlotsToUnlock; slotNumber++) {
            this.register.unlockRegisterSlotNumberN(slotNumber);
        }
    }

    @Override
    public void destroyPlayer() {
        this.lives -= 1;
        this.numberOfDamageTokensRecieved = 0; // Reset damage
        this.assessCurrentDamage(); // unlock registers
        this.wasDestroyedThisTurn = true;
    }

    @Override
    public void lockNRegisters(Integer numberOfSlotsToLock) {
        if (numberOfSlotsToLock == null
                || numberOfSlotsToLock < 0
                || numberOfSlotsToLock > this.register.getNumberOfRegisterSlots()) {
            throw new IllegalArgumentException();
        }
        int registerSlot = (this.register.getNumberOfRegisterSlots() - 1);
        while (numberOfSlotsToLock > 0) {
            this.register.lockRegisterSlotNumber(registerSlot);
            registerSlot--;
            numberOfSlotsToLock--;
        }
    }

    @Override
    public void addCardsToPlayersHand(List<ICard> cards) {
        if (cards == null) {
            throw new IllegalArgumentException();
        }
        this.cardsInHand.addCollectionOfCardsToDeck(cards);
    }

    @Override
    public void addListOfCardsToProgramRegister(List<ICard> cards) {
        if (cards == null || cards.size() > this.register.getNumberOfRegisterSlots()) {
            throw new IllegalArgumentException();
        }
        this.register.addCollectionOfCardsToRegister(cards);
    }

    @Override
    public void addADeckOfCardsToTheProgramRegister(IDeck deck) {
        if (deck == null || deck.getSize() > this.register.getNumberOfRegisterSlots()) {
            throw new IllegalArgumentException();
        }
        this.register.addADeckOfCardsToTheRegister(deck);
    }

    @Override
    public boolean addACardToProgramRegister(ICard card) {
        if (card == null) {
            throw new IllegalArgumentException();
        }
        return this.register.addCardToCurrentRegisterSlot(card);
    }

    @Override
    public ICard removeACardFromProgramRegisterAtSlotNumber(Integer slotNumber) {
        if (slotNumber == null
                || slotNumber < 0
                || slotNumber >= this.getCurrentRegisterSlotNumber()) {
            throw new IllegalArgumentException();
        }
        return this.register.removeCardFromRegisterSlot(slotNumber);
    }

    @Override
    public int getCurrentRegisterSlotNumber() {
        return this.register.getCurrentRegisterSlot();
    }

    @Override
    public void removeRemainingCardsInHand() {
        this.cardsInHand.removeAllCardsFromDeck();
    }

    @Override
    public ICard revealProgramCardForRegisterNumber(Integer registerNumber) {
        if (registerNumber == null
                || registerNumber < 0
                || registerNumber >= this.register.getNumberOfRegisterSlots()) {
            throw new IllegalArgumentException();
        }
        return this.register.getCardInSlotNumber(registerNumber);
    }

    @Override
    public IDeck clearRegister() {
        return this.register.removeAllUnlockedCardsFromTheRegister();
    }

    @Override
    public int getNumberOfUnlockedRegisterSlots() {
        return this.register.numberOfUnlockedRegisterSlots();
    }

    @Override
    public int getNumberOfUnlockedCardsInTheProgramRegister() {
        return this.register.getCurrentRegisterSlot();
    }

    @Override
    public boolean wasDestroyedThisTurn() {
        return this.wasDestroyedThisTurn;
    }

    @Override
    public Coordinates respawnAtLastArchiveMarker() {
        //this.x=backupX;
        //this.y=backupY;
        wasDestroyedThisTurn = false;
        takeOneDamage(); // Take two damage
        takeOneDamage();
        System.out.println("New laser location is x:" + laser.getX()
                + ", y:" + laser.getY() + " dir: " + laser.getDirection());
        return new Coordinates(backupX, backupY);
    }

    @Override
    public IDeck getCardsInHand() {
        return cardsInHand;
    }

    private GridDirection opposite() {
        return playerDirection.invert();
    }

    @Override
    public List<Coordinates> moveInDirection(GridDirection direction, int steps) {
        List<Coordinates> coordinates = new ArrayList<>();
        Coordinates startPos = new Coordinates(x, y);
        switch (direction) {
            case NORTH:
                for (int i = 0; i < steps; i++) {
                    y = y + 1;
                    coordinates.add(new Coordinates(x,y));
                }
                break;
            case WEST:
                for (int i = 0; i < steps; i++) {
                    x = x - 1;
                    coordinates.add(new Coordinates(x,y));
                }
                break;
            case EAST:
                for (int i = 0; i < steps; i++) {
                    x = x + 1;
                    coordinates.add(new Coordinates(x,y));
                }
                break;
            case SOUTH:
                for (int i = 0; i < steps; i++) {
                    y = y - 1;
                    coordinates.add(new Coordinates(x,y));
                }
                break;
        }
        this.x = startPos.getX();
        this.y = startPos.getY();
        return coordinates;
    }

    @Override
    public void uTurn() {
        this.playerDirection = playerDirection.invert();
    }

    @Override
    public String getTexture() {
        return "assets/player_one.png";
    }

    @Override
    public void rotateLeft() { this.playerDirection = playerDirection.rotateLeft(); }

    @Override
    public void rotateRight() { this.playerDirection = playerDirection.rotateRight(); }

    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getNumberOfLivesRemaining() {
        return this.lives;
    }

    @Override
    public boolean hasLifeLeft() {
        return this.lives > 0;
    }

    @Override
    public Laser getLaser() {
        return laser;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setCoordinates(Coordinates validPositionForPlayer) {
        this.x = validPositionForPlayer.getX();
        this.y = validPositionForPlayer.getY();
    }

    public Coordinates getCoordinates() {
        return new Coordinates(getX(), getY());
    }

    public int numberOfCardsInUnlockedRegister() {
        return register.numberOfCardsInUnlockedRegister();
    }

    public boolean registerIsFull() {
        return register.registerIsFull();
    }

    @Override
    public boolean poweringDownNextTurn() {
        return poweringDownNextTurn;
    }



    @Override
    public void removeAllDamageTokens() {
        this.numberOfDamageTokensRecieved = 0;
    }

    @Override
    public void reversePowerDownStatus() {
        this.poweringDownNextTurn = !this.poweringDownNextTurn;
    }

    @Override
    public boolean cardSelectionConfirmed() {
        return cardSelectionConfirmed;
    }

    public void setCardSelectionConfirmedStatus(boolean status) {
        cardSelectionConfirmed = status;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n"
                + "Lives: " + lives + "\n"
                + "Damage received: " + numberOfDamageTokensRecieved + "\n"
                + "Health remaining: " + (9-numberOfDamageTokensRecieved) + "\n"
                + "Direction: " + playerDirection + "\n"
                + "Flags found: " + flagsVisited + "\n"
                + "Powering down next turn: " + poweringDownNextTurn + "\n"
                + "Powered down: " + powerdDown;
    }

    public ICard getLastPlayedCard() {
        return lastPlayedCard;
    }

    @Override
    public void powerDown() {
        this.powerdDown = true;
    }

    @Override
    public boolean isPoweredDown() {
        return powerdDown;
    }

    @Override
    public void powerUp() {
        this.powerdDown = false;
    }


}

