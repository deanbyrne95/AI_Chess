package pieces;

import javax.swing.*;

public class Queen extends ChessPiece {

    public Queen(int x, int y, boolean white) {
        super(x, y, white);
    }

    @Override
    public boolean isValidMove(JPanel board, int newX, int newY) {
        return super.canMove(board, newX, newY);
    }
}
