package pieces;

import javax.swing.*;

import static constants.Constants.SQUARE_SIZE;

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
        var landX = x / SQUARE_SIZE;
        var landY = y / SQUARE_SIZE;
        if (this.getInitialY() != landY && this.getInitialX() != landX) return false;

        return super.canMove(board, x, y) && !super.isBlocked(board, landX, landY);
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
