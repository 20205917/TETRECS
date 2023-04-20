package uk.ac.soton.comp1206.scene;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The Scores Scene is used for displaying the scores of the players
 */

public class ScoresScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(ScoresScene.class);
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
        localScores = new SimpleListProperty<>();

        //get the scores from the local file and the game
        scores = loadScores();

        //get a pair of player and score from the game
        var gameScores = game.getScores();
        scores.add(gameScores);
        //sort by score and only get the top 10 scores
        scores.sort((o1, o2) -> o2.getValue() - o1.getValue());
        if (scores.size() > 10)
            scores = new ArrayList<>(scores.subList(0, 10));

        //set the scores to the list
        localScores.setAll(scores);

        scoresList = localScores.get();
        //set to UI component


    }

    /**
     * Load the scores from the local file and the game
     */
    public ArrayList<Pair<String, Integer>> loadScores() {
        //load the scores from the local file,we only need the top 10 scores
        String scoresPath = Objects.requireNonNull(this.getClass().getResource("/history/scores")).getPath();
        logger.info("Loading scores from the local file: " + scoresPath);
        var result = new ArrayList<Pair<String, Integer>>();
        try {
            var bufferedReader = new BufferedReader(new FileReader(scoresPath));
            //the line is player,score
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                var split = line.split(",");
                result.add(new Pair<>(split[0], Integer.parseInt(split[1])));
            }
            bufferedReader.close();
        } catch (IOException e) {
            logger.error("Unable to load scores from the local file");
            e.printStackTrace();
        }
        //if result size ==0 ,we need to write a default list of scores to the file
        if (result.size() == 0) {
            writeDefaultScores();
            return loadScores();//recursive
        }
        return result;
    }

    /**
     * writeScores
     * write an ordered list of scores into a file, using the sameformat as above.
     * lf the scores file does not exist, write a default list of scores to the file.
     */
    public void writeScores(ArrayList<Pair<String, Integer>> pairs) {
        String scoresPath = Objects.requireNonNull(this.getClass().getResource("/history/scores")).getPath();
        //write the scores to the local file,clear the old scores
        logger.info("Writing scores to the local file: " + scoresPath);
        try {
            var fileWriter = new BufferedWriter(new FileWriter(scoresPath));
            for (var pair : pairs) {
                fileWriter.write(pair.getKey() + "," + pair.getValue() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Unable to write scores to the local file");
            e.printStackTrace();
        }
    }

    /**
     * writeDefaultScores to the file
     */
    private void writeDefaultScores() {
        var result = new ArrayList<Pair<String, Integer>>();
        result.add(new Pair<>("Player1", 1000));
        result.add(new Pair<>("Player2", 1000));
        result.add(new Pair<>("Player3", 1000));
        result.add(new Pair<>("Player4", 1000));
        result.add(new Pair<>("Player5", 1000));
        result.add(new Pair<>("Player6", 1000));
        result.add(new Pair<>("Player7", 1000));
        result.add(new Pair<>("Player8", 1000));
        result.add(new Pair<>("Player9", 1000));
        result.add(new Pair<>("Player10",1000));
        writeScores(result);
    }


    @Override
    public void build() {
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());


    }
}
