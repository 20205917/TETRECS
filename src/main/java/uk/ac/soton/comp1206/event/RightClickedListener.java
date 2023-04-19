package uk.ac.soton.comp1206.event;


import uk.ac.soton.comp1206.component.GameBlock;

/**
 * handle the event when a block in a GameBoard is right clicked.
 */

public interface RightClickedListener {
    /**
     * Handle a block clicked event
     */
    public void rightClicked(GameBlock gameBlock);
}
