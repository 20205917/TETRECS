package uk.ac.soton.comp1206.scene;

import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;

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

        //Add the piece board to the right of the screen
        var pieceBoard = getPieceBoard();
        mainPane.setRight(pieceBoard);

        var board = new GameBoard(game.getGrid(), gameWindow.getWidth() / 2.0, gameWindow.getWidth() / 2.0);
        mainPane.setCenter(board);

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClicked);
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
     * Setup the game object and model
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
        //add keyboard listener
        //esc to go back to menu
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    gameWindow.startMenu();
                    break;
                default:
                    break;
            }
        });

        game.start();
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

    public PieceBoard getPieceBoard() {
        return new PieceBoard(gameWindow.getWidth() / 4.0, gameWindow.getWidth() / 4.0);
    }

}
