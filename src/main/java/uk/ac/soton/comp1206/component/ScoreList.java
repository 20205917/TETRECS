package uk.ac.soton.comp1206.component;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

/**
 * This is a list of scores，which is used to display the scores of the players
 * when the game is over，and it extends the VBox object
 * it can display the scores of the players in real time
 */
public class ScoreList extends VBox {
    private SimpleListProperty<Pair<String, Integer>> scores;

    private SimpleStringProperty player;

    public ScoreList() {
        this.scores = new SimpleListProperty<>();
        this.player = new SimpleStringProperty();

        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("scorelist");
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setSpacing(10);

        //updates score list when scores are updated
        this.scores.addListener((ListChangeListener<Pair<String, Integer>>) change -> {
                    reveal();
                }
        );
    }

    private void reveal() {
        this.getChildren().clear();
        for (Pair<String, Integer> score : scores) {
            Text text = new Text(score.getKey() + " : " + score.getValue());
            if (player.get() != null && player.get().equals(score.getKey())) {
                text.getStyleClass().add("myscore");
            } else {
                text.getStyleClass().add("scoreitem");
            }
            this.getChildren().add(text);
        }
    }


    public void bindScores(ObservableValue<ObservableList<Pair<String, Integer>>> scores ) {
        this.scores.bind(scores);
    }


    public void bindPlayer(ObservableStringValue player) {
        this.player.bind(player);
    }
}
