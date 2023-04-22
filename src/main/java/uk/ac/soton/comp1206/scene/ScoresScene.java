package uk.ac.soton.comp1206.scene;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.ScoreList;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.input.KeyCode.ESCAPE;

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
    private ObservableList<Pair<String, Integer>> observableList;

    private ScoreList localScoreList;

    private SimpleStringProperty player=new SimpleStringProperty();

    //the game that we need get the scores
    private Game game;

    /**
     * Set the game
     */
    public void setGame(Game game) {
        this.game = game;
        player.set(game.getPlayerName());
    }


    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public ScoresScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Scores Scene");
    }

    @Override
    public void initialise() {
        logger.info("Initialising Scores Scene");
        //add keyborad listener,press esc to go back to the main menu
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == ESCAPE) {
                gameWindow.startMenu();
            }
        });
    }

    /**
     * Load the scores from the local file and the game
     */
    public static ArrayList<Pair<String, Integer>> loadScores() {
        //load the scores from the local file,we only need the top 10 scores
        String scoresPath = Objects.requireNonNull(ScoresScene.class.getResource("/history/scores")).getPath();
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
    public static void writeScores(ArrayList<Pair<String, Integer>> pairs) {
        String scoresPath = Objects.requireNonNull(ScoresScene.class.getResource("/history/scores")).getPath();
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
    private static  void writeDefaultScores() {
        var result = new ArrayList<Pair<String, Integer>>();
        result.add(new Pair<>("Player1", 100));
        result.add(new Pair<>("Player2", 100));
        result.add(new Pair<>("Player3", 100));
        result.add(new Pair<>("Player4", 100));
        result.add(new Pair<>("Player5", 100));
        result.add(new Pair<>("Player6", 100));
        result.add(new Pair<>("Player7", 100));
        result.add(new Pair<>("Player8", 100));
        result.add(new Pair<>("Player9", 100));
        result.add(new Pair<>("Player10", 100));
        writeScores(result);
    }


    @Override
    public void build() {
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());

        var scorePane = new StackPane();
        scorePane.setMaxWidth(gameWindow.getWidth());
        scorePane.setMaxHeight(gameWindow.getHeight());
        scorePane.getStyleClass().add("menu-background");
        root.getChildren().add(scorePane);

        var mainPane = new BorderPane();
        scorePane.getChildren().add(mainPane);

        var vbox = new VBox(5);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.TOP_CENTER);
        //game over label
        var gameOverLabel = new Text("Game Over");
        gameOverLabel.getStyleClass().add("bigtitle");
        vbox.getChildren().add(gameOverLabel);

        //add play and score label
        var pair = game.getScores();
        var playLabel = new Text("Player: " + pair.getKey() + "   Score: " + pair.getValue());
        playLabel.getStyleClass().add("level");
        vbox.getChildren().add(playLabel);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        Text local_records = new Text("Local Records");
        local_records.getStyleClass().add("heading");
        Text remote_records = new Text("Remote Records");
        remote_records.getStyleClass().add("heading");
        gridPane.add(local_records, 0, 0);
        gridPane.add(remote_records, 5, 0);


        buildLocalScoreList();
        gridPane.add(localScoreList, 0, 1);

        vbox.getChildren().add(gridPane);
        mainPane.setTop(vbox);
    }

    private void buildLocalScoreList() {
        this.localScoreList = new ScoreList();

        //load the scores from the local file,we only need the top 10 scores
        scores = loadScores();
        observableList = FXCollections.observableArrayList(scores);
        localScores = new SimpleListProperty<>(observableList);

        localScoreList.bindScores(localScores);
        //bind player
        localScoreList.bindPlayer(player);

        scores.add(new Pair<>(player.get(), game.score.get()));
        scores.sort((o1, o2) -> o2.getValue() - o1.getValue());
        if (scores.size() > 10) {
            scores.remove(10);
        }
        //write back
        writeScores(scores);

        localScores.clear();
        localScores.addAll(scores);

    }
}
