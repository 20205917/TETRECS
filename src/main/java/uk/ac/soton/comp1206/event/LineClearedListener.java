package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.component.GameBlockCoordinate;

import java.util.HashSet;

/**
 * Handles the clearing of a line when it gets full
 * it passes all the coordinates of the blocks that need to be cleared
 */
public interface LineClearedListener {
    /**
     * @param blockCoordinates is a set include are the x and y coordinates of a block
     */
    void lineCleared(HashSet<GameBlockCoordinate> blockCoordinates);
}
