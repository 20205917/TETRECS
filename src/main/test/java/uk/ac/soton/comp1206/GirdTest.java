package uk.ac.soton.comp1206;


import org.junit.jupiter.api.Test;
import uk.ac.soton.comp1206.game.Grid;

public class GirdTest {

    @Test
    public void testGrid() {
        Grid grid = new Grid(10, 20);
        System.out.println(grid);
    }

}
