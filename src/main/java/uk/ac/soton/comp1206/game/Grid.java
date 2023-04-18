package uk.ac.soton.comp1206.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The Grid is a model which holds the state of a game board. It is made up of a set of Integer values arranged in a 2D
 * arrow, with rows and columns.
 * <p>
 * Each value inside the Grid is an IntegerProperty can be bound to enable modification and display of the contents of
 * the grid.
 * <p>
 * The Grid contains functions related to modifying the model, for example, placing a piece inside the grid.
 * <p>
 * The Grid should be linked to a GameBoard for it's display.
 */
public class Grid {

    /**
     * The number of columns in this grid
     */
    private final int cols;

    /**
     * The number of rows in this grid
     */
    private final int rows;

    /**
     * The grid is a 2D arrow with rows and columns of SimpleIntegerProperties.
     */
    private final SimpleIntegerProperty[][] grid;

    /**
     * Create a new Grid with the specified number of columns and rows and initialise them
     *
     * @param cols number of columns
     * @param rows number of rows
     */
    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create the grid itself
        grid = new SimpleIntegerProperty[cols][rows];

        //Add a SimpleIntegerProperty to every block in the grid
        for (var y = 0; y < rows; y++) {
            for (var x = 0; x < cols; x++) {
                grid[x][y] = new SimpleIntegerProperty(0);
            }
        }
    }

    /**
     * Get the Integer property contained inside the grid at a given row and column index. Can be used for binding.
     *
     * @param x column
     * @param y row
     * @return the IntegerProperty at the given x and y in this grid
     */
    public IntegerProperty getGridProperty(int x, int y) {
        return grid[x][y];
    }

    /**
     * Update the value at the given x and y index within the grid
     *
     * @param x     column
     * @param y     row
     * @param value the new value
     */
    public void set(int x, int y, int value) {
        grid[x][y].set(value);
    }

    /**
     * Get the value represented at the given x and y index within the grid
     *
     * @param x column
     * @param y row
     * @return the value
     */
    public int get(int x, int y) {
        try {
            //Get the value held in the property at the x and y index provided
            return grid[x][y].get();
        } catch (ArrayIndexOutOfBoundsException e) {
            //No such index
            return -1;
        }
    }

    /**
     * Get the number of columns in this game
     *
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     *
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }


    /**
     * canPlayPiece method
     * Which takes a GamePiece with a given x and y of the grid will return true orfalse if that piece can be played
     * Tip: iterate through the piece blocks, if the piece has a block at a given x and y (a value of 1),
     * check if the corresponding x and y on the grid is empty there(a value of 0)
     *
     * @param piece GamePiece
     * @param x     column
     * @param y     row
     * @return whether the piece can be played
     */
    public boolean canPlayPiece(GamePiece piece, int x, int y) {
        //Our x y ranges from 0,0 to 4,4, and we need to make sure that our piece doesn't exist in those places
        var blocks = piece.getBlocks();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (blocks[i][j] == 1) {
                    //get the value of the grid at the given x and y
                    var gridValue = get(x + i - 1, y + j - 1);
                    //if the grid value is not 0, it means that outbounds or there is a piece
                    if (gridValue != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * playPiece method
     * Which takes a GamePiece with a given x and y of the grid will place the piece on the grid
     * Tip: iterate through the piece blocks, if the piece has a block at a given x and y (a value of 1),
     * set the corresponding x and y on the grid to the piece number
     *
     * @param piece GamePiece
     * @param x     column
     * @param y     row
     */
    public void playPiece(GamePiece piece, int x, int y) {
        int[][] blocks = piece.getBlocks();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (blocks[i][j] != 0) {
                    //set the value of the grid at the given x and y to the piece number
                    set(x + i - 1, y + j - 1, piece.getValue());
                }
            }
        }
    }
    /**
     * Set Grid to empty
     */
    public void clearGrid() {
        for (int y = 0; y < this.rows; y++) {
            for (int x = 0; x < this.cols; x++)
                this.grid[x][y].set(0);
        }
    }

}
