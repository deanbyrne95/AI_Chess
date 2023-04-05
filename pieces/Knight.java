package pieces;

import javax.swing.*;

public class Knight extends ChessPiece {

    public Knight(int x, int y, boolean white) {
        super(x, y, white);
    }

    @Override
    public boolean isValidMove(JPanel board, int newX, int newY) {
        return super.canMove(board, newX, newY);
    }

    @Override
    protected boolean canMoveX(int from, int to, int numberSquares) {
        return false;
    }

    @Override
    protected boolean canMoveY(int from, int to, int numberSquares) {
        return false;
    }
}
