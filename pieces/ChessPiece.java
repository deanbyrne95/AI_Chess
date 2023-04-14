package pieces;

import javax.swing.*;
import java.awt.*;

import static constants.Constants.SQUARE_SIZE;

public abstract class ChessPiece extends JLabel {
    private final String name;
    private final boolean white;
    private final Icon icon;
    private int initialX;
    private int initialY;

    public ChessPiece(int x, int y, boolean white) {
        this.name = this.getClass().getSimpleName();
        super.setLocation(x, y);
        this.white = white;
        this.icon = new ImageIcon(String.format("pieces/%s%s.png", this.isWhite() ? "W_" : "B_", this.getName()));
        super.setHorizontalAlignment(CENTER);
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public void setPosition(int x, int y) {
        this.setInitialX(x / SQUARE_SIZE);
        this.setInitialY(y / SQUARE_SIZE);
    }

    public void setInitialX(int initialX) {
        this.initialX = initialX;
    }

    public void setInitialY(int initialY) {
        this.initialY = initialY;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return String.format("%s %s", this.white ? "White" : "Black", this.name);
    }

    public boolean isWhite() {
        return this.white;
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
    }

    public abstract boolean isValidMove(JPanel board, int newX, int newY);

    protected boolean canMove(JPanel board, int x, int y) {
        if (this.outOfBounds(x, y) || this.notMoved(x, y)) return false;
        // This code can allow a piece to move anywhere on the board
        return (this.isSquareEmpty(board, x, y) || (!isSquareEmpty(board, x, y) && this.isOpposingColour(board, x, y)))
                && !this.isBlocked(board, x, y);
    }

    protected boolean isSquareEmpty(JPanel board, int x, int y) {
        Component c = board.findComponentAt(x, y);
        return c instanceof JPanel;
    }

    protected boolean isBlocked(JPanel board, int x, int y) {
        return false;
    }

    protected boolean isOpposingColour(JPanel board, int x, int y) {
        ChessPiece piece = (ChessPiece) board.findComponentAt(x, y);
        return piece.isWhite() != this.isWhite();
    }

    protected boolean notMoved(int x, int y) {
        return (this.notEquals(this.getInitialX(), x) && this.notEquals(this.getInitialY(), y));
    }

    protected boolean outOfBounds(int x, int y) {
        return this.outsideBounds(x) || this.outsideBounds(y);
    }

    protected abstract boolean canMoveX(int from, int to, int numberSquares);

    protected abstract boolean canMoveY(int from, int to, int numberSquares);

    protected int differenceBetween(int s, int e) {
        return e - s;
    }

    protected boolean checkVertical(JPanel board, int y) {
        for (int tempY = this.getInitialY() + (this.getInitialY() < y ? 1 : -1); (this.getInitialY() > y) ? tempY >= y : tempY <= y; tempY += (this.getInitialY() < y ? 1 : -1)) {
            System.out.printf("Checking if square at [X: %d, Y: %d] is empty%n", this.getInitialX(), tempY);
            if (tempY != y && !isSquareEmpty(board, (this.getInitialX() * SQUARE_SIZE), (tempY * SQUARE_SIZE))) {
                // We need to check if there's a piece of an opposing colour only if it is the landing square
                return false;
            } else {
                if (!isSquareEmpty(board, (this.getInitialX() * SQUARE_SIZE), (tempY * SQUARE_SIZE)) && isOpposingColour(board, (this.getInitialX() * SQUARE_SIZE), (tempY * SQUARE_SIZE))) {
                    return true;
                }
            }
        }
        return true;
    }

    protected boolean checkHorizontal(JPanel board, int x) {
        for (int tempX = this.getInitialX() + (this.getInitialX() < x ? 1 : -1); (this.getInitialX() > x) ? tempX >= x : tempX <= x; tempX += (this.getInitialX() < x ? 1 : -1)) {
            System.out.printf("Checking if square at [X: %d, Y: %d] is empty%n", tempX, this.getInitialY());
            if (tempX != x && !isSquareEmpty(board, (tempX * SQUARE_SIZE), (this.getInitialY() * SQUARE_SIZE))) {
                // We need to check if there's a piece of an opposing colour only if it is the landing square
                return false;
            } else {
                if (!isSquareEmpty(board, (tempX * SQUARE_SIZE), (this.getInitialY() * SQUARE_SIZE)) && isOpposingColour(board, (tempX * SQUARE_SIZE), (this.getInitialY() * SQUARE_SIZE))) {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean outsideBounds(int n) {
        int s = n / SQUARE_SIZE;
        return s < 0 || s > 7;
    }

    private boolean notEquals(int i, int newI) {
        return i != (newI / SQUARE_SIZE);
    }
}
