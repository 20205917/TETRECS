package uk.ac.soton.comp1206.scene;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.utils.Multimedia;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    /**
     * Create a new menu scene
     *
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
        //add keyboard listener to the scene


        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());

        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);

        HBox hBox = new HBox();

        var titleImage = Multimedia.getImageView("ECSGames.png");
        titleImage.setFitHeight(180);
        titleImage.setFitWidth(180);
        titleImage.setPreserveRatio(true);//we need to preserve the ratio of the image
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(3), titleImage);
        translateTransition.setFromX(0);
        translateTransition.setToX(0);
        translateTransition.setFromY(0);
        translateTransition.setToY(-20);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.play();

        hBox.getChildren().add(titleImage);

        var gameImage = Multimedia.getImageView("TetrECS.png");
        gameImage.setFitHeight(180);
        gameImage.setFitWidth(180);
        gameImage.setX(200);
        gameImage.setPreserveRatio(true);//we need to preserve the ratio of the image
        //Rotate the image
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), gameImage);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(true);
        rotateTransition.play();
        hBox.setSpacing(30);
        hBox.getChildren().add(gameImage);

        mainPane.setTop(hBox);

        var rBox = new VBox();
        rBox.setSpacing(50);
        rBox.setAlignment(javafx.geometry.Pos.CENTER);

        var introButton = new Button("How to Play?");
        introButton.getStyleClass().add("menuItem");
        introButton.setOnAction(event -> {
            startIntro();
        });
        rBox.getChildren().add(introButton);


        //For now, let us just add a button that starts the game. I'm sure you'll do something way better.
        var button = new Button("Play");
        button.getStyleClass().add("menuItem");
        //Bind the button action to the startGame method in the menu
        button.setOnAction(this::startGame);
        rBox.getChildren().add(button);

        var exitButton = new Button("Exit");
        exitButton.getStyleClass().add("menuItem");
        exitButton.setOnAction(event -> {
            close();
        });
        rBox.getChildren().add(exitButton);

        mainPane.setCenter(rBox);
    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
        //play the menu music
        logger.info("Initialising " + this.getClass().getName());

        Multimedia.stopMusic();
        Multimedia.playMusic("menu.mp3");
        //add keyboard listener to the scene
        //esc quit game
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> close();
                case ENTER -> startGame(null);
            }
        });

    }

    /**
     * Handle when the Start Game button is pressed
     *
     * @param event event
     */
    private void startGame(ActionEvent event) {
        gameWindow.startChallenge();
    }

    public void startIntro() {
        gameWindow.startInstructions();
    }

    public void close() {
        System.exit(0);
    }

}
