package uk.ac.soton.comp1206.scene;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import java.util.ArrayList;

/**
 * The Scores Scene is used for displaying the scores of the players
 */

public class ScoresScene extends BaseScene {

    //for bind
    private SimpleListProperty<Pair<String, Integer>> localScores;

    //for data source
    private ArrayList<Pair<String, Integer>> scores;

    //for show
    private ObservableList<Pair<String, Integer>> scoresList;

    //the game that we need get the scores
    private Game game;

    /**
     * Set the game
     */
    public void setGame(Game game) {
        this.game = game;
    }


    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public ScoresScene(GameWindow gameWindow) {
        super(gameWindow);
    }

    @Override
    public void initialise() {


    }

    @Override
    public void build() {
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());


    }
}
