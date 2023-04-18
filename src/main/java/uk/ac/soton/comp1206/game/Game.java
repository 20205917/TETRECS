package uk.ac.soton.comp1206.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;

import java.util.HashSet;
import java.util.Random;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    /**
     * This will keep track of the current piece
     */
    protected GamePiece currentPiece;


    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     *
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols, rows);
    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();
    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");
        //When the game is initialised, spawn a new GamePiece and set it as the currentPiece
        currentPiece = spawnPiece();
    }

    /**
     * Handle what should happen when a particular block is clicked
     *
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        //try to place the current piece at this position
        if (grid.canPlayPiece(currentPiece, x, y)) {
            //If the grid can play the piece, place it
            grid.playPiece(currentPiece, x, y);
            nextPiece();
        }

    }

    /**
     * Get the grid model inside this game representing the game state of the board
     *
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
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
     * spawnPiece
     * Create a new random GamePiece by calling GamePiece.createPiece with a
     * random number between 0 and the number of pieces
     * Tip: Create a Random object, and call nextInt to generate a random numberbetween 0 and a given number
     *
     * @return a new GamePiece
     */
    public GamePiece spawnPiece() {
        var random = new Random();
        var randomPiece = random.nextInt(GamePiece.PIECES);

        logger.info("Spawning piece: " + randomPiece);

        return GamePiece.createPiece(randomPiece);
    }

    /**
     * Replace the current piece with a new piece
     */
    public void nextPiece() {
        currentPiece = spawnPiece();
    }

    /**
     * afterPiece
     * To be called after playing a piece
     * This should clear any full vertical/horizontal lines that have been made
     * You will need to iterate through vertical and horizontal lines, keeping
     * track of how many lines are to be cleared and which blocks will need to be reset
     * Any horizontal and vertical lines that have just been made should be cleared(including intersecting lines - multiple lines may be cleared at once).
     */
    public void afterPiece() {
        var hashSet = new HashSet<GameBlockCoordinate>();
        //Iterate through the grid
        //we first iterate through the rows,find a row that is full,then we iterate through the columns to find the blocks that are full
        //we store the coordinates of the blocks that are full in a hashset
        //after we have iterated through the columns,we clear the row and reset the blocks that are full

        for (int i = 0; i < rows; i++) {
            var fullRow = true;
            for (int j = 0; j < cols; j++) {
                if (grid.get(i, j) == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                for (int j = 0; j < cols; j++) {
                    hashSet.add(new GameBlockCoordinate(i, j));
                }
            }
        }

        for (int i = 0; i < cols; i++) {
            var fullCol = true;
            for (int j = 0; j < rows; j++) {
                if (grid.get(j, i) == 0) {
                    fullCol = false;
                    break;
                }
            }
            if (fullCol) {
                for (int j = 0; j < rows; j++) {
                    hashSet.add(new GameBlockCoordinate(j, i));
                }
            }
        }

        //clear the row and reset the blocks that are full
        for (var coordinate : hashSet) {
            grid.set(coordinate.getX(), coordinate.getY(), 0);
        }


    }


}
