package pieces;

import javax.swing.*;
import java.awt.*;

import static constants.Constants.SQUARE_SIZE;

public abstract class ChessPiece extends JLabel {
    private int initialX;
    private int initialY;
    private final String name;
    private final boolean white;
    private final Icon icon;

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
        this.setInitialX(x);
        this.setInitialY(y);
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
        // This code can allow a piece to move anywhere on the board
        return this.hasMoved(x, y)
                && (this.isSquareEmpty(board, x, y) || (!isSquareEmpty(board, x, y) && this.isOpposingColour(board, x, y)))
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
        if (piece.isWhite() != this.isWhite()) {
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.printf("\t\t\tCaptured %s%n", piece.getFullName());
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            return true;
        }
        return false;
    }

    protected boolean hasMoved(int x, int y) {
        return this.withinBounds(x, y) && (this.notEquals(this.getInitialX(), x) || this.notEquals(this.getInitialY(), y));
    }

    protected boolean withinBounds(int x, int y) {
        return this.inBounds(x) && this.inBounds(y);
    }

    protected int differenceBetween(int s, int e) {
        return e - s;
    }

    private boolean inBounds(int n) {
        int s = n / SQUARE_SIZE;
        return s >= 0 && s <= 7;
    }

    private boolean notEquals(int i, int newI) {
        return i != (newI / SQUARE_SIZE);
    }
}
