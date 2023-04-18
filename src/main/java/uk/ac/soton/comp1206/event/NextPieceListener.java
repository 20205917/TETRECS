package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.game.GamePiece;

/**
 * Create your own NextPieceListener interface which has a nextPiece method which
 * takes the next GamePiece as a parameter
 */
public interface NextPieceListener {
    public void nextPiece(GamePiece nextCurrentPiece,GamePiece nextFollowingPiece);
}
