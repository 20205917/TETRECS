package uk.ac.soton.comp1206.game;

import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;
import uk.ac.soton.comp1206.event.GameLoopListener;
import uk.ac.soton.comp1206.event.LineClearedListener;
import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.utils.Multimedia;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     * This will keep track of the following piece
     */
    protected GamePiece followingPiece;

    private ScheduledExecutorService executor;

    //future that will be used to schedule the game loop
    private ScheduledFuture<?> future;


    /*
       listeners that handle the next piece, the lines cleared, the game loop
        and the termination of the game
     */
    private NextPieceListener nextPieceListener;
    private LineClearedListener lineClearedListener;
    private GameLoopListener gameLooplistener;

    /**
     * Add bindable properties for the score, level,
     * lives and multiplier to the Game class,with appropriate accessor methods.
     * These should default to 0 score, level 0 , 3 lives and 1 x multiplier respectively.
     */
    public SimpleIntegerProperty score;

    public SimpleIntegerProperty level;

    public SimpleIntegerProperty lives;

    public SimpleIntegerProperty multiplier;


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

        //Initialise the properties
        this.score = new SimpleIntegerProperty(0);
        this.level = new SimpleIntegerProperty(0);
        this.lives = new SimpleIntegerProperty(3);
        this.multiplier = new SimpleIntegerProperty(1);
        this.followingPiece = spawnPiece();
        this.executor = new ScheduledThreadPoolExecutor(1);
        this.future = this.executor.schedule(this::gameLoop, getTimerDelay(), TimeUnit.MILLISECONDS);

    }

    /**
     * lmplement a Timer or ScheduledExecutorService inside the Game class which calls agameLoop method
     * This should be started when the game starts and repeat at the interval specified by
     * the getTimerDelay function
     * When gameLoop fires (the timer reaches 0): lose a life, the current piece isdiscarded and the timer restarts.The multiplier is set back to 1.
     * The timer should be reset when a piece is played, to the new timer delay (whichmay have changed)
     */
    public void gameLoop() {
        //when gameLoop fires
        //lose a life
        this.lives.set(this.lives.get() - 1);
        //if lives touch 0, the game is over
        if (this.lives.get() == 0) {
            this.executor.shutdown();
        }
        nextPiece();
        //the multiplier is set back to 1
        this.multiplier.set(1);
        long delay=getTimerDelay();
        //the timer restarts
        this.future = this.executor.schedule(this::gameLoop,delay , TimeUnit.MILLISECONDS);
        this.gameLooplistener.loop(delay);
    }

    public void setNextPieceListener(NextPieceListener nextPieceListener) {
        this.nextPieceListener = nextPieceListener;
    }

    public void setLineClearedListener(LineClearedListener lineClearedListener) {
        this.lineClearedListener = lineClearedListener;
    }

    public void setGameLooplistener(GameLoopListener gameLooplistener) {
        this.gameLooplistener = gameLooplistener;
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
        this.gameLooplistener.loop(getTimerDelay());
        //When the game is initialised, spawn a new GamePiece and set it as the currentPiece
        Multimedia.stopMusic();
        Multimedia.playMusic("game.wav");

        //Initializes two Piece
        nextPiece();
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
            //Generate the next
            nextPiece();
            //clear the row and reset the blocks that are full
            afterPiece();
            Multimedia.playAudio("place.wav");
        } else {// placement of the piece failed
            logger.info("Cannot place piece!");
            Multimedia.playAudio("fail.wav");
        }
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
     * Replace the current piece with a following Piece
     * Replace the following Piece with a new piece
     */
    public void nextPiece() {
        currentPiece = followingPiece;
        //creat a next piece
        followingPiece = spawnPiece();

        //reset two Pieces
        if (nextPieceListener != null) {
            nextPieceListener.nextPiece(currentPiece, followingPiece);
        }
        logger.info("The next piece is: {}", followingPiece);
    }

    /**
     * rotateCurrentPiece method rotate the next piece
     */
    public void rotateCurrentPiece(int times) {
        currentPiece.rotate(times);
    }

    /**
     * swaps the current piece with the next one and vice versa
     */
    public void swapCurrentPiece() {
        GamePiece piece = getCurrentPiece();
        currentPiece = getFollowingPiece();
        followingPiece = piece;
        logger.info("Swapping Pieces");
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

        int clearedLine = 0;
        for (int i = 0; i < rows; i++) {
            var fullRow = true;
            for (int j = 0; j < cols; j++) {
                if (grid.get(i, j) == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                clearedLine++;
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
                clearedLine++;
                for (int j = 0; j < rows; j++) {
                    hashSet.add(new GameBlockCoordinate(j, i));
                }
            }
        }

        //clear the row and reset the blocks that are full
        for (var coordinate : hashSet) {
            grid.set(coordinate.getX(), coordinate.getY(), 0);
        }

        //Trigger fade out animation
        if (lineClearedListener != null) {
            lineClearedListener.lineCleared(hashSet);
        }

        score(clearedLine, hashSet.size());

        //The multiplier is increased by 1 if the next piece also clears lines. It is increased after the score for the cleared set of lines is applied
        //The multiplier is reset to 1 when a piece is placed that doesn't clear any lines
        if (clearedLine == 0) {
            multiplier.set(1);
        } else {
            multiplier.set(multiplier.get() + 1);
        }

        //The level should increase per 1000 points (at the start, you begin at level 0.After
        //1000 points, you reach level 1.At 3000 points you would be level 3)
        level.set(score.get() / 1000);

        //reset the scheduler
        this.future.cancel(false);
        this.future = this.executor.schedule(this::gameLoop,getTimerDelay() , TimeUnit.MILLISECONDS);
        this.gameLooplistener.loop(getTimerDelay());
    }

    /**
     * score method which takes the number of lines and numberof blocks
     * and call it in afterPiece. It should add a score based on the following formula:
     * number of lines * number of grid blocks cleared * 10 * the current multiplier
     * lf no lines are cleared, no score is added
     * For example, if a piece was added that cleared 2 intersecting lines,
     * 2 lines would becleared and 9 blocks would be cleared (because 1 block appears
     * in two lines but iscounted only once) - this would be 180 points with a 1 times multiplier
     * (comparedto 200 points if 2 non-intersectina lines were cleared at the same time)
     *
     * @param lines  number of lines cleared
     * @param blocks number of blocks cleared
     */
    public void score(int lines, int blocks) {
        if (lines == 0) {
            return;
        }
        var score = lines * blocks * 10 * multiplier.get();
        this.score.set(this.score.get() + score);
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
     * Get the following piece of the game
     *
     * @return teh following piece
     */
    public GamePiece getFollowingPiece() {
        return followingPiece;
    }

    /**
     * Get the current piece of the game
     *
     * @return the current piece
     */
    public GamePiece getCurrentPiece() {
        return currentPiece;
    }


    /**
     * Calculate the delay at the maximum of either 2500 milliseconds or 12000 - 500*the current level
     * So it'll start at 12000, then drop to 11500, then 11000 and keep on going
     * until itreaches 2500 at which point it won't drop any lower
     */
    public long getTimerDelay() {
        var delay = 12000 - 500 * level.get();
        if (delay < 2500) {
            delay = 2500;
        }
        return delay;
    }


}
