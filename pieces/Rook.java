package pieces;

import javax.swing.*;

public class Rook extends ChessPiece {

    public Rook(int x, int y, boolean white) {
        super(x, y, white);
    }

    @Override
    public boolean isValidMove(JPanel board, int newX, int newY) {
        return this.canMove(board, newX, newY);
    }

    @Override
    protected boolean canMove(JPanel board, int x, int y) {
        if (super.outOfBounds(x, y) || super.notMoved(x, y)) return false;

        return super.canMove(board, x, y);
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
