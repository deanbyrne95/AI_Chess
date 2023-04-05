package pieces;

import javax.swing.*;

public class Rook extends ChessPiece {

    public Rook(int x, int y, boolean white) {
        super(x, y, white);
    }

    @Override
    public boolean isValidMove(JPanel board, int newX, int newY) {
        return super.canMove(board, newX, newY);
    }
}
