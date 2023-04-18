package uk.ac.soton.comp1206.component;

import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.game.Grid;

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

    public void setPiece(GamePiece piece) {
        //clear the grid
        var block = piece.getBlocks();
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                grid.set(x, y, 0);
                if (block[x][y] != 0) {
                    grid.set(x, y, block[x][y]);
                }
            }
        }
    }

    /**
     * display a piece on the piece board
     *
     * @param gamePiece is the piece to be displayed
     */
    public void displayPiece(GamePiece gamePiece) {
        this.grid.clearGrid();
        grid.playPiece(gamePiece, 1, 1);
    }

}
