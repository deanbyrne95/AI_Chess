package pieces;

import records.Position;

import javax.swing.*;

import static constants.Constants.*;

public class Pawn extends ChessPiece {

    public Pawn(int x, int y, boolean white) {
        super(x, y, white);
    }

    @Override
    public boolean isValidMove(JPanel board, int newX, int newY) {
        return this.canMove(board, newX, newY);
    }

    @Override
    protected boolean canMove(JPanel board, int x, int y) {
        if (super.outOfBounds(x, y) || !super.hasMoved(x, y)) return false;

        int startY = this.isWhite() ? START_WHITE : START_BLACK;
        var newP = new Position(x / SQUARE_SIZE, y / SQUARE_SIZE);
        int maxX = super.isSquareEmpty(board, x, y) ? 0 : super.isOpposingColour(board, x, y) ? (this.isWhite() ? -1 : 1) : 0;
        int maxY = this.getInitialY() == startY ? (this.isWhite() ? -2 : 2) : (this.isWhite() ? -1 : 1);

        return this.canMoveY(this.getInitialY(), newP.y(), maxY) && this.canMoveX(this.getInitialX(), newP.x(), maxX);
    }

    @Override
    protected boolean canMoveX(int from, int to, int numberSquares) {
        return this.differenceBetween(from, to) == numberSquares;
    }

    @Override
    protected boolean canMoveY(int from, int to, int numberSquares) {
        if (this.isWhite())
            return this.differenceBetween(from, to) < 0 && this.differenceBetween(from, to) >= numberSquares;
        return this.differenceBetween(from, to) > 0 && this.differenceBetween(from, to) <= numberSquares;
    }
}
