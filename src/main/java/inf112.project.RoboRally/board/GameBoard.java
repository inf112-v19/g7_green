package inf112.project.RoboRally.board;

import inf112.project.RoboRally.actors.Player;
import inf112.project.RoboRally.objects.*;
import inf112.project.RoboRally.objects.IObjects;

public class GameBoard {
    private int rows;
    private int colums;
    private IObjects [][] board;

    public GameBoard(String level) {
        int counter = extractDimensions(level);
        board = new IObjects[colums][rows];
        for (int y = colums-1; y >= 0; y--) {
            for (int x = 0; x < rows; x++) {
                board[y][x] = factory(level.charAt(counter));
                counter++;
            }
        }
    }

    private int extractDimensions(String level) {
        this.colums = Integer.parseInt(level.substring(0,level.indexOf("C")));
        this.rows = Integer.parseInt(level.substring(level.indexOf("C")+1,level.indexOf("R")));
        return level.indexOf("R")+1; // returns an integer containing the index of the start of board
    }

    private IObjects factory(char c) {
        switch (c) {
            case 'r':
                return new ConveyorBelt(1,GridDirection.EAST);
            case 'u':
                return new ConveyorBelt(1,GridDirection.NORTH);
            case 'd':
                return new ConveyorBelt(1,GridDirection.SOUTH);
            case 'l':
                return new ConveyorBelt(1,GridDirection.WEST);
            case 'f':
                return new Flag();
            case '.':
                return new Floor();
            case 'c':
                return new RotationCog(Rotation.LEFT);
            case 'C':
                return new RotationCog(Rotation.RIGHT);
            default:
                return new Floor();
        }
    }

    /**
     *
     * @return number of rows in this board
     */
    public int getRows() {
        return rows;
    }

    /**
     *
     * @return number of colums of this board
     */
    public int getColums() {
        return colums;
    }

    /**
     * The content of the board at location x,y
     * @param x coordinate
     * @param y coordinate
     * @return the iObject at location x,y of the board
     */
    public IObjects getObject(int x, int y) {
        return board[y][x];
    }

    private boolean locationInsideBoard(int x, int y){
        return x >= 0 && x < rows && y >= 0 && y < colums;
    }

    public boolean moveValid(int x, int y) {
        return locationInsideBoard(x, y);
    }
    
    public void getObjectType(int x, int y) {
        IObjects object = getObject(x,y);
        System.out.println(object.getClass());
    }

}
