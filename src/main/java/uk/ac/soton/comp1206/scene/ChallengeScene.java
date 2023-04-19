package uk.ac.soton.comp1206.scene;

import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utils.Multimedia;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;

    //holds the current piece
    protected PieceBoard currentPiece;
    //holds the next piece
    protected PieceBoard followingPiece;

    protected GameBoard board;
    //keep the track of Position
    protected int XPosition;
    protected int YPosition;
    /**
     * Create a new Single Player challenge scene
     *
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
    }

    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        setupGame();

        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());

        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("menu-background");
        root.getChildren().add(challengePane);


        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);


        //Add the state panel to the left of the screen
        var statePanel = addStatePanel();
        statePanel.setPadding(new Insets(10, 10, 10, 10));
        mainPane.setLeft(statePanel);


        var vBox = new VBox();
        vBox.setSpacing(30);

        //displays the current piece in a pieceBoard object
        currentPiece = new PieceBoard(gameWindow.getWidth()/6.0,gameWindow.getHeight()/6.0);
        currentPiece.getStyleClass().add("gameBox");
        vBox.getChildren().add(currentPiece);

        //displays the next piece in a pieceBoard object
        followingPiece = new PieceBoard(gameWindow.getWidth()/11.0,gameWindow.getHeight()/11.0);
        followingPiece.getStyleClass().add("gameBox");
        vBox.getChildren().add(followingPiece);

        mainPane.setRight(vBox);


        board = new GameBoard(game.getGrid(), gameWindow.getWidth() / 2.0, gameWindow.getWidth() / 2.0);
        mainPane.setCenter(board);
        board.getStyleClass().add("gameBox1");



        //Handle block on gameBoard grid being clicked
        board.setOnBlockClick(this::blockClicked);

        //Handle rotation on block when right-clicked
        board.setOnRightClick((gameBlock)-> rightRotate());

        //Handle rotation on block when pieceBoard grid is clicked
        currentPiece.setOnBlockClick((gameBlock)-> rightRotate());
    }

    /**
     * Handle when a block is clicked
     *
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }


    /**
     * reset the next currentPiece and next followingPiece on the pieceBoard
     */
    protected void nextPiece(GamePiece nextCurrentPiece,GamePiece nextFollowingPiece) {
        this.currentPiece.setPiece(nextCurrentPiece);
        this.followingPiece.setPiece(nextFollowingPiece);
    }

    /**
     * rightRotate the block
     */
    public void rightRotate() {
        game.rotateCurrentPiece(1);
        currentPiece.setPiece(game.getCurrentPiece());
        Multimedia.playAudio("rotate.wav");
    }

    /**
     * leftRotate the block
     */
    public void leftRotate() {
        game.rotateCurrentPiece(3);
        currentPiece.setPiece(game.getCurrentPiece());
        Multimedia.playAudio("rotate.wav");
    }

    /**
     * swaps the current piece with the following one and reset
     */
    public void swapPiece(){
        game.swapCurrentPiece();
        currentPiece.setPiece(game.getCurrentPiece());
        followingPiece.setPiece(game.getFollowingPiece());
        Multimedia.playAudio("transition.wav");
    }

    /**
     * Set up the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5);

    }


    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");

        //listens to when the next piece is fetched and calls the respective in class method
        this.game.setNextPieceListener(this::nextPiece);

        //listens to when a key is pressed and calls the respective in class method
        scene.setOnKeyPressed(this::keyPressed);

        game.start();
    }

    /**
     * handles the events that occur when a key is pressed
     * this method enables keyboard support
     *
     * @param event have the type of the pressed key
     */
    public void keyPressed(KeyEvent event){

        switch (event.getCode()) {
            //when escape is pressed the game is stopped and the player is brought
            //back to the menu
            case ESCAPE->{
                quit();
                //game.stop();
                gameWindow.startMenu();
            }
            //when space or r is pressed the pieces swapPiece
            case SPACE,R -> swapPiece();

            //when enter or x is pressed the block is placed
            case ENTER,X -> blockClicked(board.getBlock(XPosition,YPosition));

            //when right arrow or D is pressed the block is moved one block to the right
            case RIGHT,D -> {
                if(XPosition < game.getCols() - 1 ){
                    XPosition++;
                }
            }

            //when left arrow or A is pressed , the block is moved one block to the left
            case LEFT,A -> {
                if(XPosition > 0){
                    XPosition--;
                }
            }

            //when up arrow or W is pressed , the block is moved one block up
            case UP,W -> {
                if(YPosition > 0){
                    YPosition--;
                }
            }

            //when downwards arrow or S is pressed ,  the block is moved one block down
            case DOWN,S -> {
                if(YPosition < game.getRows() - 1 ){
                    YPosition++;
                }
            }
            //when the open bracket or q is pressed the block rotates to the left
            case OPEN_BRACKET,Q,Z->leftRotate();

            //when the close bracket or e is pressed the block rotates to the right
            case CLOSE_BRACKET,E,C->rightRotate();
        }
    }

    private void quit() {
        //before we quit, we need to reset the game
        game = null;//this will allow the garbage collector to clean up the game object
    }

    /**
     * Add a state panel to the scene
     * Add Ul elements to show the score, level, multiplier and lives in the ChallengeScene bybinding to the game properties.
     * Tip: Use the .asString method on an Integer Property to get it as a bindable string!
     */
    public VBox addStatePanel() {

        var vbox = new VBox();


        var scoreText = new Text();
        scoreText.textProperty().bindBidirectional(game.score.asObject(), new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return "Score: " + integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                return null;
            }
        });
        scoreText.getStyleClass().add("score");
        vbox.getChildren().add(scoreText);

        var livesText = new Text();
        livesText.textProperty().bindBidirectional(game.lives.asObject(), new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return "Lives: " + integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                return null;
            }
        });
        livesText.getStyleClass().add("lives");
        vbox.getChildren().add(livesText);

        var levelText = new Text();
        levelText.textProperty().bindBidirectional(game.level.asObject(), new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return "Level: " + integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                return null;
            }
        });
        levelText.getStyleClass().add("level");
        vbox.getChildren().add(levelText);


        var multiplierText = new Text();
        multiplierText.textProperty().bindBidirectional(game.multiplier.asObject(), new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return "Multiplier: " + integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                return null;
            }
        });
        multiplierText.getStyleClass().add("level");
        vbox.getChildren().add(multiplierText);

        return vbox;
    }

}
