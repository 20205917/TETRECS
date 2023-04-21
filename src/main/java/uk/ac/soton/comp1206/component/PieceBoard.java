package uk.ac.soton.comp1206.component;

import uk.ac.soton.comp1206.game.GamePiece;

/**
 * This can be used to display an upcoming piece in a 3x3 grid, which we will update
 * using a NextPieceListener later (see below)
 * It should have a method for setting a piece to display
 * Add it to the ChallengeScene
 */
public class PieceBoard extends GameBoard {
    private static final int N = 3;


    public PieceBoard(double width, double height) {
        super(N, N, width, height);
    }

    /**
     * display a piece on the piece board
     *
     * @param piece is the piece to be displayed
     */
    public void setPiece(GamePiece piece) {
        this.grid.clearGrid();
        grid.playPiece(piece, 1, 1);
    }

}
