package uk.ac.soton.comp1206.scene;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utils.Multimedia;

/**
 * The InstructionsScene class displays the instructions screen that holds details about
 * the rules and controls of the game and its game pieces
 */
public class InstructionsScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(InstructionsScene.class);
    /**
     * Create a new instructions screen
     *
     * @param gameWindow the game window
     */
    public InstructionsScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Instructions Scene");
    }

    /**
     * when escape is pressed the user is taken back to the menu
     */
    @Override
    public void initialise() {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> gameWindow.startMenu();
                default -> {}
            }
        });
    }


    /**
     * handles the appearance of the scene
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var InstructionsPane = new StackPane();
        InstructionsPane.setMaxWidth(gameWindow.getWidth());
        InstructionsPane.setMaxHeight(gameWindow.getHeight());
        InstructionsPane.getStyleClass().add("menu-background");
        root.getChildren().add(InstructionsPane);

        var mainPane = new BorderPane();
        InstructionsPane.getChildren().add(mainPane);

        //VBox that holds the contents of the top part of the scene
        var topPart = new VBox();
        BorderPane.setAlignment(topPart, Pos.CENTER);
        topPart.setAlignment(Pos.TOP_CENTER);
        mainPane.setTop(topPart);

        //label that serves as the heading for the instructions
        var instructionsLabel = new Text("Instructions");
        instructionsLabel.getStyleClass().add("heading");
        topPart.getChildren().add(instructionsLabel);

        //text that displays how you lose the game and ways to delay losing
        var instructionsText = new Text("When any row (or column) is filled, it will be cleared! \n" +
                " You will lose one health(you have three points), if you don’t place the current block when time runs out.");
        instructionsText.getStyleClass().add("instructions");
        instructionsText.setTextAlignment(TextAlignment.CENTER);
        topPart.getChildren().add(instructionsText);



        //image that holds the rules nad controls of the game
        ImageView instructionsImage = Multimedia.getImageView("instructions.png");
        mainPane.setCenter(instructionsImage);
        instructionsImage.setPreserveRatio(true);
        instructionsImage.setFitWidth(gameWindow.getHeight());


        //VBox that holds the components of the bottom part of the scene
        var bottomPart = new VBox();
        BorderPane.setAlignment(bottomPart, Pos.CENTER);
        bottomPart.setAlignment(Pos.BOTTOM_CENTER);
        mainPane.setBottom(bottomPart);

        //label that indicates the pieces below it
        var piecesLabel = new Text("Pieces");
        piecesLabel.getStyleClass().add("heading");
        bottomPart.getChildren().add(piecesLabel);


        //grid pane that holds the pieces to be displayed
        GridPane piecesPane = new GridPane();
        bottomPart.getChildren().add(piecesPane);
        piecesPane.setAlignment(Pos.BOTTOM_CENTER);
        piecesPane.setVgap(10);
        piecesPane.setHgap(10);

        //there are 15 pieces  displayed
        for(int i = 0; i<15; i++){
            GamePiece gamePiece = GamePiece.createPiece(i);

            PieceBoard pieceBoard = new PieceBoard(this.gameWindow.getWidth() /14,this.gameWindow.getHeight()/14);

            pieceBoard.setPiece(gamePiece);

            piecesPane.add(pieceBoard, i % 5, i / 5);

        }

    }
}
