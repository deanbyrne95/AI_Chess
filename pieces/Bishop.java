package pieces;

import javax.swing.*;

public class Bishop extends ChessPiece {

    public Bishop(int x, int y, boolean white) {
        super(x, y, white);
    }

    @Override
    public boolean isValidMove(JPanel board, int newX, int newY) {
        return this.canMove(board, newX, newY);
    }

    @Override
    protected boolean canMove(JPanel board, int x, int y) {
        if (Math.abs(x) != Math.abs(y)) return false;
        return super.isBlocked(board, x, y);
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
