package uk.ac.soton.comp1206;


import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp1206.scene.ScoresScene;

import java.util.ArrayList;

public class GameTest {

    @Test
    public void testReadFile() {
        ScoresScene scoresScene = new ScoresScene(null);
        ArrayList<Pair<String, Integer>> pairs = scoresScene.loadScores();
        //print the scores
        for (Pair<String, Integer> pair : pairs) {
            System.out.println(pair.getKey() + " " + pair.getValue());
        }
    }
    @Test void testWriteFile(){
        ScoresScene scoresScene = new ScoresScene(null);
        ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("test", 100));
        scoresScene.writeScores(pairs);
    }


}
