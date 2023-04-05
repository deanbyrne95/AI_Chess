import pieces.*;
import records.Position;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Stack;
import java.util.stream.IntStream;

import static constants.Constants.*;

/**
 * This is the ChessProject for the Introduction to Artificial Intelligence module.
 * <p>The progression of the project is broken down to complete different sections per week:</p>
 * <ul>
 * <li>The first objective is to get every piece working as it does in Chess.</li>
 * <li>The second objective is to implement the rules of the game, i.e. Player Turns</li>
 * <li>The third objective is to implement Check and Checkmate</li>
 * <li>The fourth objective is to create an A.I that the user can then play against.</li>
 * <ul>
 *
 * @author x12337831
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
class Chess extends JFrame implements MouseListener, MouseMotionListener {
    //Local Variables
    private final JLayeredPane layeredPane;
    private final JPanel chessBoard;
    private JPanel panels;
    private ChessPiece chessPiece;
    private int adjustmentX;
    private int adjustmentY;
    private int startX;
    private int startY;
    private boolean whiteTurn;


    Chess() {
        Dimension boardSize = new Dimension(Math.multiplyExact(GRID_SIZE, SQUARE_SIZE), Math.multiplyExact(GRID_SIZE, SQUARE_SIZE));

        //  Use a Layered Pane for this application
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a Chess Board to the Layered Pane
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        //Chess Board colours
        this.initialiseChessboard();

        //Add pieces to Chess Board
        this.initialisePieces((START_BLACK * GRID_SIZE), START_BLACK, false);
        this.initialisePieces((START_WHITE * GRID_SIZE), START_WHITE, true);

        //Set the Game Turn
        this.switchTurn();

//        ai = new AI();

        super.setTitle("https://github.com/deanbyrne95/AI_Chess");
        super.setIconImage(new ImageIcon("pieces/B_Queen.png").getImage());
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.pack();
        super.setResizable(false);
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    private void initialiseChessboard() {
        Color darkBrown = new Color(117, 76, 36);
        Color lightBrown = new Color(166, 124, 82);

        //Add colour to the Chess Board
        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel(new BorderLayout());
            square.setBackground(((i / GRID_SIZE) % 2 == 0) ? (i % 2 == 0 ? lightBrown : darkBrown) : (i % 2 == 0 ? darkBrown : lightBrown));
            chessBoard.add(square);
        }
    }

    private void initialisePieces(int x, int y, boolean isWhite) {
        IntStream.range(x, x + GRID_SIZE).forEach(i -> {
            ChessPiece piece = new Pawn(i, y, isWhite);
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(piece);
        });
        IntStream.range(isWhite ? x + GRID_SIZE : x - GRID_SIZE, isWhite ? x + 16 : x).forEach(i -> {
            switch (i) {
                case 0, 7, 56, 63 -> chessPiece = new Rook(i, y, isWhite);
                case 1, 6, 57, 62 -> chessPiece = new Knight(i, y, isWhite);
                case 2, 5, 58, 61 -> chessPiece = new Bishop(i, y, isWhite);
                case 3, 59 -> chessPiece = new Queen(i, y, isWhite);
                case 4, 60 -> chessPiece = new King(i, y, isWhite);
            }
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(chessPiece);
        });
    }


    //This method checks if there is a piece present on a particular square.
    private boolean piecePresent(int x, int y) {
        Component c = chessBoard.findComponentAt(x, y);
        return !(c instanceof JPanel);
    }

    //This is a method to check if a piece is a White piece.
    private boolean checkBlackOpponent(int newX, int newY) {
        return checkOpponent(newX, newY, "W_", "B_");
    }

    //This is a method to check if a piece is a Black piece.
    private boolean checkWhiteOpponent(int newX, int newY) {
        return checkOpponent(newX, newY, "B_", "W_");
    }

    private boolean checkOpponent(int newX, int newY, String b, String w) {
        boolean opponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (tmp1.contains(b)) {
            if (tmp1.contains("King")) {
                opponent = true;
                gameOver(w);
            } else {
                opponent = true;
            }
        } else {
            opponent = false;
        }
        return opponent;
    }


    private void switchTurn() {
        if (whiteTurn) {
            whiteTurn = false;
            System.out.println("==============================================");
            System.out.println("\t\t\tB L A C K\tT U R N");
            System.out.println("==============================================");
        } else {
            whiteTurn = true;
            System.out.println("==============================================");
            System.out.println("\t\t\tW H I T E\tT U R N");
            System.out.println("==============================================");
        }
    }


    private void gameOver(String winColour) {
        JOptionPane.showMessageDialog(null, winColour.contains("W_") ? "White won the game!" : "Black won the game!");
        System.exit(0);
    }


    private boolean checkKingNear(int x, int y) {
        return ((piecePresent(x, y + SQUARE_SIZE)) && getSquarePieceName(x, y + SQUARE_SIZE).contains("King")) || ((piecePresent(x, y - SQUARE_SIZE)) && getSquarePieceName(x, y - SQUARE_SIZE).contains("King")) || ((piecePresent(x + SQUARE_SIZE, y)) && getSquarePieceName(x + SQUARE_SIZE, y).contains("King")) || ((piecePresent(x - SQUARE_SIZE, y)) && getSquarePieceName(x - SQUARE_SIZE, y).contains("King")) || ((piecePresent(x + SQUARE_SIZE, y + SQUARE_SIZE)) && getSquarePieceName(x + SQUARE_SIZE, y + SQUARE_SIZE).contains("King")) || ((piecePresent(x + SQUARE_SIZE, y - SQUARE_SIZE)) && getSquarePieceName(x + SQUARE_SIZE, y - SQUARE_SIZE).contains("King")) || ((piecePresent(x - SQUARE_SIZE, y + SQUARE_SIZE)) && getSquarePieceName(x - SQUARE_SIZE, y + SQUARE_SIZE).contains("King")) || ((piecePresent(x - SQUARE_SIZE, y - SQUARE_SIZE)) && getSquarePieceName(x - SQUARE_SIZE, y - SQUARE_SIZE).contains("King"));
    }


    private boolean checkKingNear(Square s) {
        int x = s.getXCoOrdinate();
        int y = s.getYCoOrdinate();
        return this.checkKingNear(x, y);
    }


    private String getSquarePieceName(int x, int y) {
        Component c = chessBoard.findComponentAt(x, y);
        if ((c instanceof JLabel awaitingPiece)) {
            return awaitingPiece.getIcon().toString();
        } else {
            return "";
        }
    }

    private void printStack(Stack input) {
        Move move;
        Square start, landing;
        while (!input.empty()) {
            move = (Move) input.pop();
            start = move.getStart();
            landing = move.getLanding();
            System.out.println("The possible move that was found is : (" + start.getXCoOrdinate() + " , " + start.getYCoOrdinate() + "), landing at (" + landing.getXCoOrdinate() + " , " + landing.getYCoOrdinate() + ")");
        }
    }


    //Find positions off all White pieces on Board.
    private Stack findWhitePieces() {
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;

        for (int i = 0; i < 600; i += SQUARE_SIZE) {
            for (int j = 0; j < 600; j += SQUARE_SIZE) {
                y = i / SQUARE_SIZE;
                x = j / SQUARE_SIZE;
                Component temp = chessBoard.findComponentAt(j, i);
                if (temp instanceof JLabel) {
                    chessPiece = (ChessPiece) temp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    if (pieceName.contains("W_")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }
                }
            }
        }
        return squares;
    }


    //Find positions off all White pieces on Board.
    private Stack getWhiteAttackingSquares(Stack pieces) {
        Stack piece = new Stack();

        while (!pieces.empty()) {
            Square s = (Square) pieces.pop();
            String tmpString = s.getPieceName();
            if (tmpString.contains("Pawn")) {
                Stack tempP = getPawnMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempP.empty()) {
                    Square tempPawn = (Square) tempP.pop();
                    piece.push(tempPawn);
                }
            } else if (tmpString.contains("Knight")) {
                Stack tempK = getKnightMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempK.empty()) {
                    Square tempKnight = (Square) tempK.pop();
                    piece.push(tempKnight);
                }
            } else if (tmpString.contains("Bishop")) {
                Stack tempB = getBishopMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempB.empty()) {
                    Square tempBishop = (Square) tempB.pop();
                    piece.push(tempBishop);
                }
            } else if (tmpString.contains("Rook")) {
                Stack tempR = getRookMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempR.empty()) {
                    Square tempRook = (Square) tempR.pop();
                    piece.push(tempRook);
                }
            } else if (tmpString.contains("Queen")) {
                Stack tempQ = getQueenMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempQ.empty()) {
                    Square tempQueen = (Square) tempQ.pop();
                    piece.push(tempQueen);
                }
            } else if (tmpString.contains("King")) {
                Stack tempK = getKingMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempK.empty()) {
                    Square tempKing = (Square) tempK.pop();
                    piece.push(tempKing);
                }
            }
        }
        return piece;
    }

    //Shows all the moves the Pawn can make
    private Stack getPawnMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Stack pawnMove = new Stack();

        Square s = new Square(x, y + 1, piece);
        moves.push(s);
        Square s1 = new Square(x + 1, y + 1, piece);
        moves.push(s1);
        Square s2 = new Square(x - 1, y + 1, piece);
        moves.push(s2);
        Square s3 = new Square(x, y + 2, piece);
        moves.push(s3);

        for (int i = 0; i < 4; i++) {
            Square temp = (Square) moves.pop();
            Move tempMove = new Move(startingSquare, temp);

            //  Keep within bounds
            if ((temp.getXCoOrdinate() < 0) || (temp.getXCoOrdinate() > 7) || (temp.getYCoOrdinate() < 0) || (temp.getYCoOrdinate() > 7)) {
                System.out.println("Out of bounds");
            }
            //  Moving 1 Square Up
            else if (startingSquare.getYCoOrdinate() == 1) {
                if ((temp.getXCoOrdinate() > startingSquare.getXCoOrdinate() || temp.getXCoOrdinate() < startingSquare.getXCoOrdinate())) {
                    if (piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        if (piece.contains("W_")) {
                            if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                                pawnMove.push(tempMove);
                            }
                        }
                    }
                } else {
                    if (!(piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))))) {
                        if (temp.getYCoOrdinate() - startingSquare.getYCoOrdinate() == 1) {
                            pawnMove.push(tempMove);

                        } else if (!(piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((((temp.getYCoOrdinate() - 1) * SQUARE_SIZE) + 20))))) {
                            pawnMove.push(tempMove);
                        }
                    }

                }
            } else {
                if ((temp.getXCoOrdinate() > startingSquare.getXCoOrdinate() || temp.getXCoOrdinate() < startingSquare.getXCoOrdinate())) {
                    if (piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        if (piece.contains("W_")) {
                            if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                                pawnMove.push(tempMove);
                            }
                        }
                    }
                } else {
                    if (!(piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) && (temp.getYCoOrdinate() - startingSquare.getYCoOrdinate()) == 1) {
                        pawnMove.push(tempMove);
                    }
                }
            }
        }
        return pawnMove;
    }


    //Shows all the moves the Knight can make
    private Stack getKnightMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);

        Stack moves = new Stack();
        Stack attacking = new Stack();

        Square s = new Square(x + 1, y + 2);
        moves.push(s);
        Square s1 = new Square(x + 1, y - 2);
        moves.push(s1);
        Square s2 = new Square(x - 1, y + 2);
        moves.push(s2);
        Square s3 = new Square(x - 1, y - 2);
        moves.push(s3);
        Square s4 = new Square(x + 2, y + 1);
        moves.push(s4);
        Square s5 = new Square(x + 2, y - 1);
        moves.push(s5);
        Square s6 = new Square(x - 2, y + 1);
        moves.push(s6);
        Square s7 = new Square(x - 2, y - 1);
        moves.push(s7);

        for (int i = 0; i < GRID_SIZE; i++) {
            Square temp = (Square) moves.pop();

            Move tempMove = new Move(startingSquare, temp);

            if ((temp.getXCoOrdinate() < 0) || (temp.getXCoOrdinate() > 7) || (temp.getYCoOrdinate() < 0) || (temp.getYCoOrdinate() > 7)) {
                System.out.println("Out of bounds");
            } else if (piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                if (piece.contains("W_")) {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        attacking.push(temp);
                    } else {
                        System.out.println("It's our own piece!");
                    }
                }
            } else {
                if (checkBlackOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    attacking.push(temp);
                }
            }
        }

        colourSquares(attacking);
        return attacking;
    }


    //Shows all the moves the Bishop can make
    private Stack getBishopMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);

        Stack moves = new Stack();
        Move validMove1, validMove2, validMove3, validMove4;

        for (int i = 1; i < GRID_SIZE; i++) {
            int tempX = x + i;
            int tempY = y + i;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove1 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove1);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove1);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        for (int k = 1; k < GRID_SIZE; k++) {
            int tempX = x + k;
            int tempY = y - k;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove2 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove2);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove2);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        for (int l = 1; l < GRID_SIZE; l++) {
            int tempX = x - l;
            int tempY = y + l;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove3 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove3);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove3);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        for (int n = 1; n < GRID_SIZE; n++) {
            int tempX = x - n;
            int tempY = y - n;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove4 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove4);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove4);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        colourSquares(moves);
        return moves;
    }


    //Shows all the moves the Rook can make
    private Stack getRookMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);

        Stack moves = new Stack();
        Move validMove1, validMove2, validMove3, validMove4;

        //Rook Movement - East
        for (int i = 1; i < GRID_SIZE; i++) {
            int tempX = x + i;
            int tempY = y;
            if (!(tempX > 7 || tempX < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove1 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove1);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove1);
                    }
                    break;
                }
            }
        }
        //Rook Movement - West
        for (int j = 1; j < GRID_SIZE; j++) {
            int tempX = x - j;
            int tempY = y;
            if (!(tempX > 7 || tempX < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove2 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove2);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove2);
                    }
                    break;
                }
            }
        }
        //Rook Movement - North
        for (int k = 1; k < GRID_SIZE; k++) {
            int tempX = x;
            int tempY = y + k;
            if (!(tempY > 7 || tempY < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove3 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove3);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove3);
                    }
                    break;
                }
            }
        }
        //Rook Movement - South
        for (int l = 1; l < GRID_SIZE; l++) {
            int tempX = x;
            int tempY = y - l;
            if (!(tempY > 7 || tempY < 0)) {
                Square temp = new Square(tempX, tempY, piece);
                validMove4 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove4);
                } else {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * SQUARE_SIZE) + 20), ((temp.getYCoOrdinate() * SQUARE_SIZE) + 20))) {
                        moves.push(validMove4);
                    }
                    break;
                }
            }
        }
        colourSquares(moves);
        return moves;
    }


    //Shows all the moves the Queen can make
    private Stack getQueenMoves(int x, int y, String piece) {
        Stack completeMoves = new Stack();
        Stack tempMoves;

        Move temp;

        tempMoves = getRookMoves(x, y, piece);
        while (!tempMoves.empty()) {
            temp = (Move) tempMoves.pop();
            completeMoves.push(temp);
        }

        tempMoves = getBishopMoves(x, y, piece);
        while (!tempMoves.empty()) {
            temp = (Move) tempMoves.pop();
            completeMoves.push(temp);
        }

        return completeMoves;
    }


    //Shows all the moves the King can make
    private Stack getKingMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validMove1, validMove2, validMove3;

        int tempX1 = x + 1;
        int tempX2 = x - 1;
        int tempY1 = y + 1;
        int tempY2 = y - 1;

        if (!(tempX1 > 7)) {
            Square temp1 = new Square(tempX1, y, piece);
            Square temp2 = new Square(tempX1, tempY1, piece);
            Square temp3 = new Square(tempX1, tempY2, piece);

            if (checkKingNear(temp1)) {
                validMove1 = new Move(startingSquare, temp1);
                if (!piecePresent(((temp1.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp1.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove1);
                } else {
                    if (checkWhiteOpponent(((temp1.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp1.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove1);
                    }
                }
            }
            if (!(tempY1 > 7)) {
                if (checkKingNear(temp2)) {
                    validMove2 = new Move(startingSquare, temp2);
                    if (!piecePresent(((temp2.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp2.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove2);
                    } else {
                        if (checkWhiteOpponent(((temp2.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp2.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                            moves.push(validMove2);
                        }
                    }
                }
            }
            if (!(tempY2 < 0)) {
                if (checkKingNear(temp3)) {
                    validMove3 = new Move(startingSquare, temp3);
                    if (!piecePresent(((temp3.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp3.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove3);
                    } else {
                        if (checkWhiteOpponent(((temp3.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp3.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                            moves.push(validMove3);
                        }
                    }
                }
            }
        }
        if (!(tempX2 < 0)) {
            Square temp4 = new Square(tempX2, y, piece);
            Square temp5 = new Square(tempX2, tempY1, piece);
            Square temp6 = new Square(tempX2, tempY2, piece);
            if (checkKingNear(temp4)) {
                validMove1 = new Move(startingSquare, temp4);
                if (!piecePresent(((temp4.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp4.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove1);
                } else {
                    if (checkWhiteOpponent(((temp4.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp4.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove1);
                    }
                }
            }
            if (!(tempY1 > 7)) {
                if (checkKingNear(temp5)) {
                    validMove2 = new Move(startingSquare, temp5);
                    if (!piecePresent(((temp5.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp5.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove2);
                    } else {
                        if (checkWhiteOpponent(((temp5.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp5.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                            moves.push(validMove2);
                        }
                    }
                }
            }
            if (!(tempY2 < 0)) {
                if (checkKingNear(temp6)) {
                    validMove3 = new Move(startingSquare, temp6);
                    if (!piecePresent(((temp6.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp6.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove3);
                    } else {
                        if (checkWhiteOpponent(((temp6.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp6.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                            moves.push(validMove3);
                        }
                    }
                }
            }
        }
        Square temp7 = new Square(x, tempY1, piece);
        Square temp8 = new Square(x, tempY2, piece);
        if (!(tempY1 > 7)) {
            if (checkKingNear(temp7)) {
                validMove2 = new Move(startingSquare, temp7);
                if (!piecePresent(((temp7.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp7.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove2);
                } else {
                    if (checkWhiteOpponent(((temp7.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp7.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove2);
                    }
                }
            }
        }
        if (!(tempY2 < 0)) {
            if (checkKingNear(temp8)) {
                validMove3 = new Move(startingSquare, temp8);
                if (!piecePresent(((temp8.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp8.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                    moves.push(validMove3);
                } else {
                    if (checkWhiteOpponent(((temp8.getXCoOrdinate() * SQUARE_SIZE) + 20), (((temp8.getYCoOrdinate() * SQUARE_SIZE) + 20)))) {
                        moves.push(validMove3);
                    }
                }
            }
        }
        return moves;
    }


    //Method to colour a number of squares
    private void colourSquares(Stack squares) {
        Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
        while (!squares.empty()) {
            Square s = (Square) squares.pop();
            int location = s.getXCoOrdinate() + ((s.getYCoOrdinate()) * GRID_SIZE);
            JPanel panel = (JPanel) chessBoard.getComponent(location);
            panel.setBorder(greenBorder);
        }
    }


    //Method to colour a number of squares
    private void resetBorders() {
        Border empty = BorderFactory.createEmptyBorder();
        for (int i = 0; i < 64; i++) {
            JPanel tmppanel = (JPanel) chessBoard.getComponent(i);
            tmppanel.setBorder(empty);
        }
    }


    //Method to colour a number of squares
    private void getLandingSquares(Stack square) {
        Move temp;
        Square landingSquare;
        Stack squares = new Stack();
        while (!(square.empty())) {
            temp = (Move) square.pop();
            landingSquare = (Square) temp.getLanding();
            squares.push(landingSquare);
        }
        colourSquares(squares);
    }


    //Method to make AI move pieces
    /*private void makeAIMove() {
        layeredPane.validate();
        layeredPane.repaint();
        Stack whitePieces = findWhitePieces();
        Stack completeMoves = new Stack();
        Move move;
        Stack temp = new Stack();

        while (!whitePieces.empty()) {
            Square sq = (Square) whitePieces.pop();
            String whiteSqName = sq.getPieceName();
            Stack tempMoves = new Stack();

            if (whiteSqName.contains("Pawn")) {
                tempMoves = getPawnMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Knight")) {
                tempMoves = getKnightMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Bishop")) {
                tempMoves = getBishopMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Rook")) {
                tempMoves = getRookMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Queen")) {
                tempMoves = getQueenMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("King")) {
                tempMoves = getKingMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            }

            while (!(tempMoves.empty())) {
                move = (Move) tempMoves.pop();
                completeMoves.push(move);
            }
        }

        temp = (Stack) completeMoves.clone();
        getLandingSquares(temp);
        printStack(temp);

        if (completeMoves.size() == 0) {
            //Stalemate
        } else {
            System.out.println("=============================================================");
            Stack testMove = new Stack();
            while (!(completeMoves.empty())) {
                Move tempMove = (Move) completeMoves.pop();
                Square startSquare = (Square) tempMove.getStart();
                Square landingSquare = (Square) tempMove.getLanding();
                System.out.println("The " + startSquare.getPieceName() + " can move from (" + startSquare.getXCoOrdinate() + ", " + startSquare.getYCoOrdinate() + " to (" + landingSquare.getXCoOrdinate() + ", " + landingSquare.getYCoOrdinate() + ")");
                testMove.push(tempMove);
            }
            System.out.println("=============================================================");
            Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
            Move selectedMove = ai.randomMove(testMove);
            Square startingPoint = (Square) selectedMove.getStart();
            Square landingPoint = (Square) selectedMove.getLanding();
            int startX1 = (startingPoint.getXCoOrdinate() * SQUARE_SIZE) + 20;
            int startY1 = (startingPoint.getYCoOrdinate() * SQUARE_SIZE) + 20;
            int landingX1 = (landingPoint.getXCoOrdinate() * SQUARE_SIZE) + 20;
            int landingY1 = (landingPoint.getYCoOrdinate() * SQUARE_SIZE) + 20;
            System.out.println("===== Move " + startingPoint.getPieceName() + " (" + startingPoint.getXCoOrdinate() + ", " + startingPoint.getYCoOrdinate() + " to (" + landingPoint.getXCoOrdinate() + ", " + landingPoint.getYCoOrdinate() + ")");

            Component c = (JLabel) chessBoard.findComponentAt(startX1, startY1);
            Container parent = c.getParent();
            parent.remove(c);
            int panelID = (startingPoint.getYCoOrdinate() * GRID_SIZE) + startingPoint.getXCoOrdinate();
            panels = (JPanel) chessBoard.getComponent(panelID);
            panels.setBorder(redBorder);
            parent.validate();

            Component l = chessBoard.findComponentAt(landingX1, landingY1);
            if (l instanceof JLabel) {
                Container parentLanding = l.getParent();
                JLabel awaitingPieceName = (JLabel) l;
                String pieceCaptured = awaitingPieceName.getIcon().toString();
                if (pieceCaptured.contains("King")) {
                    aiWins = true;
                }
                parentLanding.remove(l);
                parentLanding.validate();
                pieces = new JLabel(new ImageIcon(startingPoint.getPieceName() + ".png"));
                int landingPanelID = (landingPoint.getYCoOrdinate() * GRID_SIZE) + landingPoint.getXCoOrdinate();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();

                if (aiWins) {
                    JOptionPane.showMessageDialog(null, "AI has won!");
                    System.exit(0);
                }
            } else {
                pieces = new JLabel(new ImageIcon(startingPoint.getPieceName() + ".png"));
                int landingPanelID = (landingPoint.getYCoOrdinate() * GRID_SIZE) + landingPoint.getXCoOrdinate();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();
            }
            switchTurn();
        }
    }*/


    /*

    This method is called when we press the Mouse.
    We need to find out what piece we selected.
    We may also not have selected a piece!

    */
    public void mousePressed(MouseEvent e) {
        //Only accent a left mouse click
        if (!SwingUtilities.isLeftMouseButton(e)) {
            System.out.println("Non-click detected");
        } else {
            chessPiece = null;
            Component c = chessBoard.findComponentAt(e.getX(), e.getY());
            if (c instanceof JPanel) return;
            if ((whiteTurn && !((ChessPiece) c).isWhite()) || !whiteTurn && ((ChessPiece) c).isWhite()) return;

            chessPiece = (ChessPiece) c;
            Point parentLocation = c.getParent().getLocation();
            adjustmentX = parentLocation.x - e.getX();
            adjustmentY = parentLocation.y - e.getY();
            chessPiece.setPosition(e.getX(), e.getY());
            chessPiece.setLocation(e.getX() + adjustmentX, e.getY() + adjustmentY);
            chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
            layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);

        }
    }

    //Piece will follow the cursor when the mouse click is held over a piece
    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;

        chessPiece.setLocation(me.getX() + adjustmentX, me.getY() + adjustmentY);
    }

    /*

    This method is used when the Mouse is released
    We need to make sure the move is valid before putting the piece back on the board.

    */
    public void mouseReleased(MouseEvent e) {
        if (chessPiece == null) return;

        chessPiece.setVisible(false);
        boolean success = false;
        boolean progression = false;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());

        var moveP = new Position(((e.getX() / SQUARE_SIZE) - chessPiece.getInitialX()), ((e.getY() / SQUARE_SIZE) - chessPiece.getInitialY()));
        var landP = new Position((e.getX() / SQUARE_SIZE), (e.getY() / SQUARE_SIZE));

        if (chessPiece.isValidMove(chessBoard, e.getX(), e.getY())) {
            this.printMove(moveP, landP);
            Container parent;
            if (c instanceof JLabel) {
                parent = c.getParent();
                this.printCapturedPiece(chessPiece, (ChessPiece) parent.getComponent(0));
                parent.remove(0);
            } else {
                parent = (Container) c;
            }
            chessPiece.setLocation(landP.x(), landP.y());
            chessPiece.setVisible(true);
            parent.add(chessPiece);
            this.switchTurn();
        } else {
            var i = (chessPiece.getInitialY() * GRID_SIZE) + chessPiece.getInitialX();
            panels = (JPanel) chessBoard.getComponent(i);
            chessPiece.setVisible(true);
            panels.add(chessPiece);
        }

        /*if (!gameOver) {
            if (!whiteTurn) {
                switch (pieceName) {
                    case "Black Pawn" -> {
                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        }
                        //If pawn is at the starting point
                        else {
                            if (startY == START_BLACK) {
                                //Pawn can move 1 or 2 squares forward but not backwards
                                if (((yMovement == 1) || (yMovement == 2)) && (startY > landingY) && (xMovement == 0)) {
                                    if (yMovement == 2) {
                                        if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), (e.getY() + SQUARE_SIZE)))) {
                                            validMove = true;
                                        }
                                    } else {
                                        if (!piecePresent(e.getX(), e.getY())) {
                                            validMove = true;
                                        }
                                    }
                                }
                                //Pawn can take a piece, if present 1 square diagonally, if it's an enemy
                                else if ((yMovement == 1) && (startY > landingY) && xMovement == 1) {
                                    //Check if a piece is present in the square
                                    if (piecePresent(e.getX(), e.getY())) {
                                        //If there is, check if it's an opponent
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                            //If Pawn isn't in Starting Position
                            else {
                                if (((yMovement == 1) && (startY > landingY) && (xMovement == 0))) {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                        if (landingY == 0) {
                                            progression = true;
                                        }
                                    }
                                } else if ((yMovement == 1) && (startY > landingY) && xMovement == 1) {
                                    //Check if a piece is present in the square
                                    if (piecePresent(e.getX(), e.getY())) {
                                        //If there is, check if it's an opponent
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                            if (landingY == 0) {
                                                progression = true;
                                            }
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "Black Knight" -> {
                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        } else {
                            if ((yMovement == 1) && (xMovement == 2)) {
                                if (!piecePresent(e.getX(), e.getY())) {
                                    validMove = true;
                                } else {
                                    if (checkBlackOpponent(e.getX(), e.getY())) {
                                        validMove = true;
                                    }
                                }
                            } else if ((yMovement == 2) && (xMovement == 1)) {
                                if (!piecePresent(e.getX(), e.getY())) {
                                    validMove = true;
                                } else {
                                    if (checkBlackOpponent(e.getX(), e.getY())) {
                                        validMove = true;
                                    }
                                }
                            } else {
                                validMove = false;
                            }
                        }
                    }
                    case "Black Bishop" -> {
                        boolean pieceInTheWay = false;
                        int distanceX = Math.abs(startX - landingX);
                        int distanceY = Math.abs(startY - landingY);

                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            System.out.println("Out of bounds");
                        } else if ((landingX == startX) || (landingY == startY)) {
                            System.out.println("Out of bounds");
                        } else {
                            if (distanceX == distanceY) {
                                //Direction: South-West
                                if ((startX - landingX < 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-West
                                else if ((startX - landingX < 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-East
                                else if ((startX - landingX > 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: South-East
                                else if ((startX - landingX > 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }

                                if (!pieceInTheWay) {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "Black Rook" -> {
                        boolean pieceInTheWay = false;
                        int distanceX = Math.abs(startX - landingX);
                        int distanceY = Math.abs(startY - landingY);

                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        } else {
                            //Rook can only move along the X-axis or the Y-axis
                            if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0))) {
                                //Rook moving along the X-axis
                                if (distanceX != 0) {
                                    //Moving right on the X-Axis
                                    if (startX - landingX > 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX - (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving left on the X-Axis
                                    else if (startX - landingX < 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX + (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }
                                //Rook moving along the Y-Axis
                                else if (distanceY != 0) {
                                    //Moving up on the Y-Axis
                                    if (startY - landingY > 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY - (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving down on the Y-Axis
                                    if (startY - landingY < 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY + (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }

                                if (pieceInTheWay) {
                                    validMove = false;
                                } else {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "Black Queen" -> {
                        boolean pieceInTheWay = false;
                        int distanceX = Math.abs(startX - landingX);
                        int distanceY = Math.abs(startY - landingY);

                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        }
                        //Moves like Rook and Bishop
                        else {
                            if (distanceX == distanceY) {
                                //Direction: South-West
                                if ((startX - landingX < 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-West
                                else if ((startX - landingX < 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-East
                                else if ((startX - landingX > 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: South-East
                                else if ((startX - landingX > 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }

                                if (pieceInTheWay) {
                                    validMove = false;
                                } else {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        }
                                    }
                                }
                            }
                            //Rook can only move along the X-axis or the Y-axis
                            else if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0))) {
                                //Rook moving along the X-axis
                                if (distanceX != 0) {
                                    //Moving right on the X-Axis
                                    if (startX - landingX > 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX - (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving left on the X-Axis
                                    else if (startX - landingX < 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX + (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }
                                //Rook moving along the Y-Axis
                                else if (distanceY != 0) {
                                    //Moving up on the Y-Axis
                                    if (startY - landingY > 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY - (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving down on the Y-Axis
                                    if (startY - landingY < 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY + (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }

                                if (pieceInTheWay) {
                                    validMove = false;
                                } else {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "Black King" -> {
                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        } else {
                            //King can move 1 square in any direction
                            if ((yMovement <= 1) && (xMovement <= 1)) {
                                if (!(checkKingNear(e.getX(), e.getY()))) {
                                    //Check if a piece is present in the square
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                switch (pieceName) {
                    case "White Pawn" -> {
                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        }
                        //If pawn is at the starting point
                        else {
                            if (startY == START_WHITE) {
                                //Pawn can move 1 or 2 squares forward but not backwards
                                if (((yMovement == -1) || (yMovement == -2)) && (startY > landingY) && (xMovement == 0)) {
                                    if (yMovement == -2) {
                                        if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), (e.getY() - SQUARE_SIZE)))) {
                                            validMove = true;
                                        }
                                    } else {
                                        if (!piecePresent(e.getX(), e.getY())) {
                                            validMove = true;
                                        }
                                    }
                                }
                                //Pawn can take a piece, if present 1 square diagonally, if it's an enemy
                                else if ((yMovement == 1) && (startY < landingY) && xMovement == 1) {
                                    //Check if a piece is present in the square
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = false;
                                    } else {
                                        if (checkWhiteOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                            //If Pawn isn't in Starting Position
                            else {
                                if (((yMovement == 1) && (startY < landingY) && (xMovement == 0))) {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                        if (landingY == 7) {
                                            success = true;
                                        }
                                    }
                                } else if ((yMovement == 1) && (startY < landingY) && xMovement == 1) {
                                    //Check if a piece is present in the square
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = false;
                                    } else {
                                        if (checkWhiteOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                            if (landingY == 7) {
                                                success = true;
                                            }
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "White Knight" -> {
                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        } else {
                            //Knight moves up 1 square on Y-axis and 2 squares, either way, on the X-axis.
                            if ((yMovement == 1) && (xMovement == 2)) {
                                if (!piecePresent(e.getX(), e.getY())) {
                                    validMove = true;
                                } else {
                                    if (checkWhiteOpponent(e.getX(), e.getY())) {
                                        validMove = true;
                                    }
                                }
                            }
                            //Knight moves up 2 squares on Y-axis and 1 square, either way, on the X-axis.
                            else if ((yMovement == 2) && (xMovement == 1)) {
                                if (!piecePresent(e.getX(), e.getY())) {
                                    validMove = true;
                                } else {
                                    if (checkWhiteOpponent(e.getX(), e.getY())) {
                                        validMove = true;
                                    }
                                }
                            } else {
                                validMove = false;
                            }
                        }
                    }
                    case "White Bishop" -> {
                        boolean pieceInTheWay = false;
                        int distanceX = Math.abs(startX - landingX);
                        int distanceY = Math.abs(startY - landingY);

                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        } else if ((landingX == startX) || (landingY == startY)) {
                            validMove = false;
                        } else {
                            if (distanceX == distanceY) {
                                //Direction: South-West
                                if ((startX - landingX < 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-West
                                else if ((startX - landingX < 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-East
                                else if ((startX - landingX > 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: South-East
                                else if ((startX - landingX > 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }

                                if (pieceInTheWay) {
                                    validMove = false;
                                } else {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkWhiteOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "White Rook" -> {
                        boolean pieceInTheWay = false;
                        int distanceX = Math.abs(startX - landingX);
                        int distanceY = Math.abs(startY - landingY);

                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        } else {
                            //Rook can only move along the X-axis or the Y-axis
                            if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0))) {
                                //Rook moving along the X-axis
                                if (distanceX != 0) {
                                    //Moving right on the X-Axis
                                    if (startX - landingX > 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX - (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving left on the X-Axis
                                    else if (startX - landingX < 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX + (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }
                                //Rook moving along the Y-Axis
                                else if (distanceY != 0) {
                                    //Moving up on the Y-Axis
                                    if (startY - landingY > 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY - (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving down on the Y-Axis
                                    if (startY - landingY < 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY + (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }

                                if (pieceInTheWay) {
                                    validMove = false;
                                } else {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkWhiteOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "White Queen" -> {
                        boolean pieceInTheWay = false;
                        int distanceX = Math.abs(startX - landingX);
                        int distanceY = Math.abs(startY - landingY);

                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        }
                        //Moves like Rook and Bishop
                        else {
                            if (distanceX == distanceY) {
                                //Direction: South-West
                                if ((startX - landingX < 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-West
                                else if ((startX - landingX < 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX + (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: North-East
                                else if ((startX - landingX > 0) && (startY - landingY > 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY - (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }
                                //Direction: South-East
                                else if ((startX - landingX > 0) && (startY - landingY < 0)) {
                                    for (int i = 0; i < distanceX; i++) {
                                        if (piecePresent((initialX - (SQUARE_SIZE * i)), (initialY + (SQUARE_SIZE * i)))) {
                                            pieceInTheWay = true;
                                        }
                                    }
                                }

                                if (pieceInTheWay) {
                                    validMove = false;
                                } else {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkWhiteOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        }
                                    }
                                }
                            }
                            //Rook can only move along the X-axis or the Y-axis
                            else if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0))) {
                                //Rook moving along the X-axis
                                if (distanceX != 0) {
                                    //Moving right on the X-Axis
                                    if (startX - landingX > 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX - (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving left on the X-Axis
                                    else if (startX - landingX < 0) {
                                        for (int i = 0; i < distanceX; i++) {
                                            if (piecePresent(initialX + (SQUARE_SIZE * i), e.getY())) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }
                                //Rook moving along the Y-Axis
                                else if (distanceY != 0) {
                                    //Moving up on the Y-Axis
                                    if (startY - landingY > 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY - (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                    //Moving down on the Y-Axis
                                    if (startY - landingY < 0) {
                                        for (int i = 0; i < distanceY; i++) {
                                            if (piecePresent(e.getX(), initialY + (SQUARE_SIZE * i))) {
                                                pieceInTheWay = true;
                                                break;
                                            } else {
                                                pieceInTheWay = false;
                                            }
                                        }
                                    }
                                }

                                if (pieceInTheWay) {
                                    validMove = false;
                                } else {
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkWhiteOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case "White King" -> {
                        if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7) {
                            validMove = false;
                        } else {
                            //King can move 1 square in any direction
                            if ((yMovement <= 1) && (xMovement <= 1)) {
                                if (!(checkKingNear(e.getX(), e.getY()))) {
                                    //Check if a piece is present in the square
                                    if (!piecePresent(e.getX(), e.getY())) {
                                        validMove = true;
                                    } else {
                                        if (checkBlackOpponent(e.getX(), e.getY())) {
                                            validMove = true;
                                        } else {
                                            validMove = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }*/


        /*if (!validMove) {
            int location = 0;
            if (startY == 0) {
                location = startX;
            } else {
                location = (startY * GRID_SIZE) + startX;
            }
            panels = (JPanel) chessBoard.getComponent(location);
            panels.add(chessPiece);
        } else {
            if (progression) {
                int location = (e.getX() / SQUARE_SIZE);
                Container parent;
                if (c instanceof JLabel) {
                    parent = c.getParent();
                    parent.remove(0);
                } else {
                    parent = (Container) c;
                }
//                pieces = new JLabel(new ImageIcon("BlackQueen.png"));
                parent = (JPanel) chessBoard.getComponent(location);
//                parent.add(pieces);
                switchTurn();
            } else if (success) {
                int location = 56 + (e.getX() / SQUARE_SIZE);
                Container parent;
                if (c instanceof JLabel) {
                    parent = c.getParent();
                    parent.remove(0);
                }
                parent = (JPanel) chessBoard.getComponent(location);
                parent.add(chessPiece);
                switchTurn();
            } else {
                Container parent;
                if (c instanceof JLabel) {
                    parent = c.getParent();
                    parent.remove(0);
                } else {
                    parent = (Container) c;
                }
                parent.add(chessPiece);
                chessPiece.setVisible(true);
                switchTurn();
            }
//            makeAIMove();
        }*/
    }

    private void printCapturedPiece(ChessPiece captorPiece, ChessPiece capturedPiece) {
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        System.out.printf("\t\t%s captured %s%n", captorPiece.getFullName(), capturedPiece.getFullName());
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    }

    private void printMove(Position moveP, Position landP) {
        System.out.println("----------------------------------------------");
        System.out.printf("Piece:\t\t\t\t\t\t%s%n", chessPiece.getFullName());
        System.out.printf("Starting coordinates:\t\t[%d, %d]%n", chessPiece.getInitialX(), chessPiece.getInitialY());
        System.out.printf("Movement:\t\t\t\t\t[%d, %d]%n", moveP.x(), moveP.y());
        System.out.printf("Landing coordinates:\t\t[%d, %d]%n", landP.x(), landP.y());
        System.out.println("----------------------------------------------");
    }

    //Empty mouse methods
    public void mouseClicked(MouseEvent e) {
        System.out.printf("Piece present:\t\t(%s)%n", this.piecePresent(e.getX(), e.getY()));
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args) {
        new Chess();
    }
}
