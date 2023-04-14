package pieces;

import records.Position;

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
        if (super.outOfBounds(x, y) || super.notMoved(x, y)) return false;

        var newP = new Position((x / SQUARE_SIZE), (y / SQUARE_SIZE));
        var maxX = Math.abs(this.differenceBetween(this.getInitialY(), newP.y())) > 0 ? 0 : 7;
        var maxY = Math.abs(this.differenceBetween(this.getInitialX(), newP.x())) > 0 ? 0 : 7;

        return this.isBlocked(board, newP.x(), newP.y()) && this.canMoveY(this.getInitialY(), newP.y(), maxY) && this.canMoveX(this.getInitialX(), newP.x(), maxX);
    }

    @Override
    protected boolean isBlocked(JPanel board, int x, int y) {
        if (this.getInitialX() != x) return super.checkHorizontal(board, x);
        else return super.checkVertical(board, y);
    }

    @Override
    protected boolean canMoveX(int from, int to, int numberSquares) {
        return Math.abs(this.differenceBetween(from, to)) <= numberSquares;
    }

    @Override
    protected boolean canMoveY(int from, int to, int numberSquares) {
        return Math.abs(this.differenceBetween(from, to)) <= numberSquares;
    }
}
