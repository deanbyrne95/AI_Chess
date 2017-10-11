/*     Dean Byrne   	*/
/*      x12337831		*/

//Java Imports

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Stack;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BoxLayout;

/*
ChessProject - Table of Contents
====================================================
----------------------------------------------------

1.	ChessProject Class
    1.1.	Class Constructor
    1.2.	Game Logic Methods
        1.2.1.  Piece Present
        1.2.2.	Check Black Opponent
        1.2.3.	Check White Opponent
        1.2.4.  Switch Turn
        1.2.5.  Game Over
        1.2.6.  Check King Near (x, y)
        1.2.7.  Check King Near (Square)
        1.2.8.  Get Square Piece Name
    1.3.  A.I Logic Methods*
        1.3.1.	Find White Pieces*
        1.3.2.	Get White Attacking Pieces*
        1.3.3.  Get All Pieces Moves*
            1.3.3.1.	Get Pawn Moves*
            1.3.3.2.	Get Knight Moves*
            1.3.3.3.	Get Bishop Moves*
            1.3.3.4.	Get Rook Moves*
            1.3.3.5.	Get Queen Moves*
            1.3.3.6.	Get King Moves*
        1.3.4.	Colour Squares*
        1.3.5.  Reset Borders
        1.3.6.  Get Landing Squares*
        1.3.7.  Make AI Move*
    1.4.	Game Movements
        1.4.1.	Black Pieces
            1.4.1.1.	Black Pawn
            1.4.1.2.	Black Knight
            1.4.1.3.	Black Bishop
            1.4.1.4.	Black Rook
            1.4.1.5.	Black Queen
            1.4.1.6.	Black King
        1.4.2.	White Pieces
            1.4.2.1.	White Pawn
            1.4.2.2.	White Knight
            1.4.2.3.	White Bishop
            1.4.2.4.	White Rook
            1.4.2.5.	White Queen
            1.4.2.6.	White King
        1.4.3.	Valid Moves
        1.4.4.  Game Over
    1.5.	Main Method

* Work In Progress (Non-Functional)

----------------------------------------------------
====================================================
*/

/*

This is the ChessProject for the Introduction to Artificial Intelligence module
The progression of the project is broken down to complete different sections per week.
The first objective is to get every piece working as it does in Chess.
The second objective is to implement the rules of the game, i.e. Player Turns
The third objective is to implement Check and Checkmate
The fourth objective is to create an A.I that the user can then play against.

In this version, the game is up to date with the second objective - allowing player turns.
*/

/*
====================================================
----------------------------------------------------

1.	ChessProject Class

----------------------------------------------------
====================================================
*/
//Suppress Warnings
@SuppressWarnings("unchecked")
public class ChessProject extends JFrame implements MouseListener, MouseMotionListener
{
    //Local Variables
    JLayeredPane layeredPane;
    JPanel chessBoard;
    JPanel panels;
    JLabel chessPiece;
    JLabel pieces;

    int adjustmentX;
    int adjustmentY;
    int startX;
    int startY;
    int initialX;
    int initialY;

    String pieceTaken;

    Boolean draggingPiece;
    Boolean whiteTurn;
    Boolean gameOver;
    Boolean aiWins;

    AI ai;

    /*
    ====================================================
    ----------------------------------------------------

    1.1.	Class Constructor

    ----------------------------------------------------
    ====================================================
    */
    public ChessProject()
    {
        Dimension boardSize = new Dimension(600, 600);

        //  Use a Layered Pane for this application
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a Chess Board to the Layered Pane
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        //Chess Board colours
        Color darkBrown = new Color(117, 76, 36);
        Color lightBrown = new Color(166, 124, 82);

        //Add colour to the Chess Board
        for (int i = 0; i < 64; i++)
        {
            JPanel square = new JPanel(new BorderLayout());
            chessBoard.add(square);

            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground(i % 2 == 0 ? lightBrown : darkBrown);
            else
                square.setBackground(i % 2 == 0 ? darkBrown : lightBrown);
        }

        //Add pieces to Chess Board
        for (int i = 8; i < 16; i++)
        {
            pieces = new JLabel(new ImageIcon("WhitePawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(0);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(1);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(6);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteBishop.png"));
        panels = (JPanel) chessBoard.getComponent(2);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteBishop.png"));
        panels = (JPanel) chessBoard.getComponent(5);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
        panels = (JPanel) chessBoard.getComponent(3);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKing.png"));
        panels = (JPanel) chessBoard.getComponent(4);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(7);
        panels.add(pieces);
        for (int i = 48; i < 56; i++)
        {
            pieces = new JLabel(new ImageIcon("BlackPawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(56);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(57);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(62);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackBishop.png"));
        panels = (JPanel) chessBoard.getComponent(58);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackBishop.png"));
        panels = (JPanel) chessBoard.getComponent(61);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackQueen.png"));
        panels = (JPanel) chessBoard.getComponent(59);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKing.png"));
        panels = (JPanel) chessBoard.getComponent(60);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(63);
        panels.add(pieces);

        //Set the Game Turn
        whiteTurn = true;

        gameOver = false;

        ai = new AI();
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.	Game Logic Methods

    ----------------------------------------------------
    ====================================================
    */

    /*

    This is the area where we'll code the rules of the Chess Game.
    It will check if pieces are in the way, which pieces are opponents, check
    what squares they can move to and highlight them.

    */

    /*
    ====================================================
    ----------------------------------------------------

    1.2.1.	Piece Present

    ----------------------------------------------------
    ====================================================
    */
    //This method checks if there is a piece present on a particular square.
    private Boolean piecePresent(int x, int y)
    {
        Component c = chessBoard.findComponentAt(x, y);
        if (c instanceof JPanel)
        {
            return false;
        } else
        {
            return true;
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.2.	Check Black Opponent

    ----------------------------------------------------
    ====================================================
    */
    //This is a method to check if a piece is a White piece.
    private Boolean checkBlackOpponent(int newX, int newY)
    {
        Boolean opponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (((tmp1.contains("White"))))
        {
            if (tmp1.contains("King"))
            {
                opponent = true;
                GameOver("Black");
            } else
            {
                opponent = true;
            }
        } else
        {
            opponent = false;
        }
        return opponent;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.3.	Check White Opponent

    ----------------------------------------------------
    ====================================================
    */
    //This is a method to check if a piece is a Black piece.
    private Boolean checkWhiteOpponent(int newX, int newY)
    {
        Boolean opponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (((tmp1.contains("Black"))))
        {
            if (tmp1.contains("King"))
            {
                opponent = true;
                GameOver("White");
            } else
            {
                opponent = true;
            }
        } else
        {
            opponent = false;
        }
        return opponent;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.4.	Switch Turn

    ----------------------------------------------------
    ====================================================
    */
    private void SwitchTurn(Boolean bool)
    {
        if (whiteTurn)
        {
            whiteTurn = !bool;
            System.out.println("Black Turn");
        } else
        {
            whiteTurn = bool;
            System.out.println("White Turn");
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.5.	Game Over

    ----------------------------------------------------
    ====================================================
    */
    private void GameOver(String winColour)
    {
        gameOver = true;
        if (winColour.contains("White"))
        {
            JOptionPane.showMessageDialog(null, "White won the game!");
            System.exit(0);
        } else
        {
            JOptionPane.showMessageDialog(null, "Black won the game!");
            System.exit(0);
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.6.	Check King Near (x, y)

    ----------------------------------------------------
    ====================================================
    */
    private Boolean checkKingNear(int x, int y)
    {
        if (
                ((piecePresent(x, y + 75)) && getSquarePieceName(x, y + 75).contains("King")) ||
                        ((piecePresent(x, y - 75)) && getSquarePieceName(x, y - 75).contains("King")) ||
                        ((piecePresent(x + 75, y)) && getSquarePieceName(x + 75, y).contains("King")) ||
                        ((piecePresent(x - 75, y)) && getSquarePieceName(x - 75, y).contains("King")) ||
                        ((piecePresent(x + 75, y + 75)) && getSquarePieceName(x + 75, y + 75).contains("King")) ||
                        ((piecePresent(x + 75, y - 75)) && getSquarePieceName(x + 75, y - 75).contains("King")) ||
                        ((piecePresent(x - 75, y + 75)) && getSquarePieceName(x - 75, y + 75).contains("King")) ||
                        ((piecePresent(x - 75, y - 75)) && getSquarePieceName(x - 75, y - 75).contains("King"))
                )
        {
            return true;
        } else
        {
            return false;
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.6.	Check King Near (x, y)

    ----------------------------------------------------
    ====================================================
    */
    private Boolean checkKingNear(Square s)
    {
        int x = s.getXCoOrdinate();
        int y = s.getYCoOrdinate();
        if (
                ((piecePresent(x, y + 75)) && getSquarePieceName(x, y + 75).contains("King")) ||
                        ((piecePresent(x, y - 75)) && getSquarePieceName(x, y - 75).contains("King")) ||
                        ((piecePresent(x + 75, y)) && getSquarePieceName(x + 75, y).contains("King")) ||
                        ((piecePresent(x - 75, y)) && getSquarePieceName(x - 75, y).contains("King")) ||
                        ((piecePresent(x + 75, y + 75)) && getSquarePieceName(x + 75, y + 75).contains("King")) ||
                        ((piecePresent(x + 75, y - 75)) && getSquarePieceName(x + 75, y - 75).contains("King")) ||
                        ((piecePresent(x - 75, y + 75)) && getSquarePieceName(x - 75, y + 75).contains("King")) ||
                        ((piecePresent(x - 75, y - 75)) && getSquarePieceName(x - 75, y - 75).contains("King"))
                )
        {
            return true;
        } else
        {
            return false;
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.8.	Get Square Piece Name

    ----------------------------------------------------
    ====================================================
    */
    private String getSquarePieceName(int x, int y)
    {
        Component c = chessBoard.findComponentAt(x, y);
        if ((c instanceof JLabel))
        {
            JLabel awaitingPiece = (JLabel) c;
            String tmp1 = awaitingPiece.getIcon().toString();
            return tmp1;
        } else
        {
            return "";
        }
    }

    private void printStack(Stack input)
    {
        Move move;
        Square start, landing;
        while (!input.empty())
        {
            move = (Move) input.pop();
            start = (Square) move.getStart();
            landing = (Square) move.getLanding();
            System.out.println("The possible move that was found is : (" + start.getXCoOrdinate() + " , " + start.getYCoOrdinate() + "), landing at (" + landing.getXCoOrdinate() + " , " + landing.getYCoOrdinate() + ")");
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.	A.I Logic Methods

    ----------------------------------------------------
    ====================================================
    */

    /*
    ====================================================
    ----------------------------------------------------

    1.3.1.	Find White Pieces

    ----------------------------------------------------
    ====================================================
    */

    //Find positions off all White pieces on Board.
    private Stack findWhitePieces()
    {
        Stack squares = new Stack();
        String icon;
        int x;
        int y;
        String pieceName;

        for (int i = 0; i < 600; i += 75)
        {
            for (int j = 0; j < 600; j += 75)
            {
                y = i / 75;
                x = j / 75;
                Component temp = chessBoard.findComponentAt(j, i);
                if (temp instanceof JLabel)
                {
                    chessPiece = (JLabel) temp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    if (pieceName.contains("White"))
                    {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }
                }
            }
        }
        return squares;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.2.	Get White Attacking Squares

    ----------------------------------------------------
    ====================================================
    */

    //Find positions off all White pieces on Board.
    private Stack getWhiteAttackingSquares(Stack pieces)
    {
        Stack piece = new Stack();

        while (!pieces.empty())
        {
            Square s = (Square) pieces.pop();
            String tmpString = s.getPieceName();
            if (tmpString.contains("Pawn"))
            {
                Stack tempP = getPawnMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempP.empty())
                {
                    Square tempPawn = (Square) tempP.pop();
                    piece.push(tempPawn);
                }
            } else if (tmpString.contains("Knight"))
            {
                Stack tempK = getKnightMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempK.empty())
                {
                    Square tempKnight = (Square) tempK.pop();
                    piece.push(tempKnight);
                }
            } else if (tmpString.contains("Bishop"))
            {
                Stack tempB = getBishopMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempB.empty())
                {
                    Square tempBishop = (Square) tempB.pop();
                    piece.push(tempBishop);
                }
            } else if (tmpString.contains("Rook"))
            {
                Stack tempR = getRookMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempR.empty())
                {
                    Square tempRook = (Square) tempR.pop();
                    piece.push(tempRook);
                }
            } else if (tmpString.contains("Queen"))
            {
                Stack tempQ = getQueenMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempQ.empty())
                {
                    Square tempQueen = (Square) tempQ.pop();
                    piece.push(tempQueen);
                }
            } else if (tmpString.contains("King"))
            {
                Stack tempK = getKingMoves(s.getXCoOrdinate(), s.getYCoOrdinate(), s.getPieceName());
                while (!tempK.empty())
                {
                    Square tempKing = (Square) tempK.pop();
                    piece.push(tempKing);
                }
            }
        }
        return piece;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.3.	Get All Piece Moves

    ----------------------------------------------------
    ====================================================
    */

    /*
    ====================================================
    ----------------------------------------------------

    1.3.3.1.	Get Pawn Moves

    ----------------------------------------------------
    ====================================================
    */
    //Shows all the moves the Pawn can make
    private Stack getPawnMoves(int x, int y, String piece)
    {
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

        for (int i = 0; i < 4; i++)
        {
            Square temp = (Square) moves.pop();
            Move tempMove = new Move(startingSquare, temp);

            //  Keep within bounds
            if ((temp.getXCoOrdinate() < 0) || (temp.getXCoOrdinate() > 7) || (temp.getYCoOrdinate() < 0) || (temp.getYCoOrdinate() > 7))
            {

            }
            //  Moving 1 Square Up
            else if (startingSquare.getYCoOrdinate() == 1)
            {
                if ((temp.getXCoOrdinate() > startingSquare.getXCoOrdinate() || temp.getXCoOrdinate() < startingSquare.getXCoOrdinate()))
                {
                    if (piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                    {
                        if (piece.contains("White"))
                        {
                            if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                            {
                                pawnMove.push(tempMove);
                            }
                        }
                    }
                } else
                {
                    if (!(piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20)))))
                    {
                        if (temp.getYCoOrdinate() - startingSquare.getYCoOrdinate() == 1)
                        {
                            pawnMove.push(tempMove);

                        } else if (!(piecePresent(((temp.getXCoOrdinate() * 75) + 20), ((((temp.getYCoOrdinate() - 1) * 75) + 20)))))
                        {
                            pawnMove.push(tempMove);
                        }
                    }

                }
            } else
            {
                if ((temp.getXCoOrdinate() > startingSquare.getXCoOrdinate() || temp.getXCoOrdinate() < startingSquare.getXCoOrdinate()))
                {
                    if (piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                    {
                        if (piece.contains("White"))
                        {
                            if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                            {
                                pawnMove.push(tempMove);
                            }
                        }
                    }
                } else
                {
                    if (!(piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20)))) && (temp.getYCoOrdinate() - startingSquare.getYCoOrdinate()) == 1)
                    {
                        pawnMove.push(tempMove);
                    }
                }
            }
        }
        return pawnMove;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.3.2.	Get Knight Moves

    ----------------------------------------------------
    ====================================================
    */
    //Shows all the moves the Knight can make
    private Stack getKnightMoves(int x, int y, String piece)
    {
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

        for (int i = 0; i < 8; i++)
        {
            Square temp = (Square) moves.pop();

            Move tempMove = new Move(startingSquare, temp);

            if ((temp.getXCoOrdinate() < 0) || (temp.getXCoOrdinate() > 7) || (temp.getYCoOrdinate() < 0) || (temp.getYCoOrdinate() > 7))
            {

            } else if (piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
            {
                if (piece.contains("White"))
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                    {
                        attacking.push(temp);
                    } else
                    {
                        System.out.println("It's our own piece!");
                    }
                }
            } else
            {
                if (checkBlackOpponent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    attacking.push(temp);
                }
            }
        }

        colourSquares(attacking);
        return attacking;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.3.3.	Get Bishop Moves

    ----------------------------------------------------
    ====================================================
    */
    //Shows all the moves the Bishop can make
    private Stack getBishopMoves(int x, int y, String piece)
    {
        Square startingSquare = new Square(x, y, piece);

        Stack moves = new Stack();
        Move validMove1, validMove2, validMove3, validMove4;

        for (int i = 1; i < 8; i++)
        {
            int tempX = x + i;
            int tempY = y + i;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove1 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove1);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove1);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        for (int k = 1; k < 8; k++)
        {
            int tempX = x + k;
            int tempY = y - k;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove2 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove2);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove2);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        for (int l = 1; l < 8; l++)
        {
            int tempX = x - l;
            int tempY = y + l;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove3 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove3);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove3);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        for (int n = 1; n < 8; n++)
        {
            int tempX = x - n;
            int tempY = y - n;
            if (!(tempX > 7 || tempX < 0 || tempY > 7 || tempY < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove4 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove4);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove4);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        colourSquares(moves);
        return moves;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.3.4.	Get Rook Moves

    ----------------------------------------------------
    ====================================================
    */
    //Shows all the moves the Rook can make
    private Stack getRookMoves(int x, int y, String piece)
    {
        Square startingSquare = new Square(x, y, piece);

        Stack moves = new Stack();
        Move validMove1, validMove2, validMove3, validMove4;

        //Rook Movement - East
        for (int i = 1; i < 8; i++)
        {
            int tempX = x + i;
            int tempY = y;
            if (!(tempX > 7 || tempX < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove1 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove1);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove1);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        //Rook Movement - West
        for (int j = 1; j < 8; j++)
        {
            int tempX = x - j;
            int tempY = y;
            if (!(tempX > 7 || tempX < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove2 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove2);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove2);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        //Rook Movement - North
        for (int k = 1; k < 8; k++)
        {
            int tempX = x;
            int tempY = y + k;
            if (!(tempY > 7 || tempY < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove3 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove3);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove3);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        //Rook Movement - South
        for (int l = 1; l < 8; l++)
        {
            int tempX = x;
            int tempY = y - l;
            if (!(tempY > 7 || tempY < 0))
            {
                Square temp = new Square(tempX, tempY, piece);
                validMove4 = new Move(startingSquare, temp);
                if (!piecePresent(((temp.getXCoOrdinate() * 75) + 20), (((temp.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove4);
                } else
                {
                    if (checkWhiteOpponent(((temp.getXCoOrdinate() * 75) + 20), ((temp.getYCoOrdinate() * 75) + 20)))
                    {
                        moves.push(validMove4);
                        break;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        colourSquares(moves);
        return moves;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.3.5.	Get Queen Moves

    ----------------------------------------------------
    ====================================================
    */
    //Shows all the moves the Queen can make
    private Stack getQueenMoves(int x, int y, String piece)
    {
        Stack completeMoves = new Stack();
        Stack tempMoves = new Stack();

        Move temp;

        tempMoves = getRookMoves(x, y, piece);
        while (!tempMoves.empty())
        {
            temp = (Move) tempMoves.pop();
            completeMoves.push(temp);
        }

        tempMoves = getBishopMoves(x, y, piece);
        while (!tempMoves.empty())
        {
            temp = (Move) tempMoves.pop();
            completeMoves.push(temp);
        }

        return completeMoves;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.3.6.	Get King Moves

    ----------------------------------------------------
    ====================================================
    */
    //Shows all the moves the King can make
    private Stack getKingMoves(int x, int y, String piece)
    {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validMove1, validMove2, validMove3;

        int tempX1 = x + 1;
        int tempX2 = x - 1;
        int tempY1 = y + 1;
        int tempY2 = y - 1;

        if (!(tempX1 > 7))
        {
            Square temp1 = new Square(tempX1, y, piece);
            Square temp2 = new Square(tempX1, tempY1, piece);
            Square temp3 = new Square(tempX1, tempY2, piece);

            if (checkKingNear(temp1))
            {
                validMove1 = new Move(startingSquare, temp1);
                if (!piecePresent(((temp1.getXCoOrdinate() * 75) + 20), (((temp1.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove1);
                } else
                {
                    if (checkWhiteOpponent(((temp1.getXCoOrdinate() * 75) + 20), (((temp1.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove1);
                    }
                }
            }
            if (!(tempY1 > 7))
            {
                if (checkKingNear(temp2))
                {
                    validMove2 = new Move(startingSquare, temp2);
                    if (!piecePresent(((temp2.getXCoOrdinate() * 75) + 20), (((temp2.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove2);
                    } else
                    {
                        if (checkWhiteOpponent(((temp2.getXCoOrdinate() * 75) + 20), (((temp2.getYCoOrdinate() * 75) + 20))))
                        {
                            moves.push(validMove2);
                        }
                    }
                }
            }
            if (!(tempY2 < 0))
            {
                if (checkKingNear(temp3))
                {
                    validMove3 = new Move(startingSquare, temp3);
                    if (!piecePresent(((temp3.getXCoOrdinate() * 75) + 20), (((temp3.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove3);
                    } else
                    {
                        if (checkWhiteOpponent(((temp3.getXCoOrdinate() * 75) + 20), (((temp3.getYCoOrdinate() * 75) + 20))))
                        {
                            moves.push(validMove3);
                        }
                    }
                }
            }
        }
        if (!(tempX2 < 0))
        {
            Square temp4 = new Square(tempX2, y, piece);
            Square temp5 = new Square(tempX2, tempY1, piece);
            Square temp6 = new Square(tempX2, tempY2, piece);
            if (checkKingNear(temp4))
            {
                validMove1 = new Move(startingSquare, temp4);
                if (!piecePresent(((temp4.getXCoOrdinate() * 75) + 20), (((temp4.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove1);
                } else
                {
                    if (checkWhiteOpponent(((temp4.getXCoOrdinate() * 75) + 20), (((temp4.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove1);
                    }
                }
            }
            if (!(tempY1 > 7))
            {
                if (checkKingNear(temp5))
                {
                    validMove2 = new Move(startingSquare, temp5);
                    if (!piecePresent(((temp5.getXCoOrdinate() * 75) + 20), (((temp5.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove2);
                    } else
                    {
                        if (checkWhiteOpponent(((temp5.getXCoOrdinate() * 75) + 20), (((temp5.getYCoOrdinate() * 75) + 20))))
                        {
                            moves.push(validMove2);
                        }
                    }
                }
            }
            if (!(tempY2 < 0))
            {
                if (checkKingNear(temp6))
                {
                    validMove3 = new Move(startingSquare, temp6);
                    if (!piecePresent(((temp6.getXCoOrdinate() * 75) + 20), (((temp6.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove3);
                    } else
                    {
                        if (checkWhiteOpponent(((temp6.getXCoOrdinate() * 75) + 20), (((temp6.getYCoOrdinate() * 75) + 20))))
                        {
                            moves.push(validMove3);
                        }
                    }
                }
            }
        }
        Square temp7 = new Square(x, tempY1, piece);
        Square temp8 = new Square(x, tempY2, piece);
        if (!(tempY1 > 7))
        {
            if (checkKingNear(temp7))
            {
                validMove2 = new Move(startingSquare, temp7);
                if (!piecePresent(((temp7.getXCoOrdinate() * 75) + 20), (((temp7.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove2);
                } else
                {
                    if (checkWhiteOpponent(((temp7.getXCoOrdinate() * 75) + 20), (((temp7.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove2);
                    }
                }
            }
        }
        if (!(tempY2 < 0))
        {
            if (checkKingNear(temp8))
            {
                validMove3 = new Move(startingSquare, temp8);
                if (!piecePresent(((temp8.getXCoOrdinate() * 75) + 20), (((temp8.getYCoOrdinate() * 75) + 20))))
                {
                    moves.push(validMove3);
                } else
                {
                    if (checkWhiteOpponent(((temp8.getXCoOrdinate() * 75) + 20), (((temp8.getYCoOrdinate() * 75) + 20))))
                    {
                        moves.push(validMove3);
                    }
                }
            }
        }
        return moves;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.4.	Colour Squares

    ----------------------------------------------------
    ====================================================
    */
    //Method to colour a number of squares
    private void colourSquares(Stack squares)
    {
        Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
        while (!squares.empty())
        {
            Square s = (Square) squares.pop();
            int location = s.getXCoOrdinate() + ((s.getYCoOrdinate()) * 8);
            JPanel panel = (JPanel) chessBoard.getComponent(location);
            panel.setBorder(greenBorder);
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.5.	Reset Borders

    ----------------------------------------------------
    ====================================================
    */
    //Method to colour a number of squares
    private void resetBorders()
    {
        Border empty = BorderFactory.createEmptyBorder();
        for (int i = 0; i < 64; i++)
        {
            JPanel tmppanel = (JPanel) chessBoard.getComponent(i);
            tmppanel.setBorder(empty);
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.6.	Get Landing Squares

    ----------------------------------------------------
    ====================================================
    */
    //Method to colour a number of squares
    private void getLandingSquares(Stack square)
    {
        Move temp;
        Square landingSquare;
        Stack squares = new Stack();
        while (!(square.empty()))
        {
            temp = (Move) square.pop();
            landingSquare = (Square) temp.getLanding();
            squares.push(landingSquare);
        }
        colourSquares(squares);
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.3.7.	Make AI Move

    ----------------------------------------------------
    ====================================================
    */
    //Method to make AI move pieces
    private void makeAIMove()
    {
        layeredPane.validate();
        layeredPane.repaint();
        Stack whitePieces = findWhitePieces();
        Stack completeMoves = new Stack();
        Move move;
        Stack temp = new Stack();

        while (!whitePieces.empty())
        {
            Square sq = (Square) whitePieces.pop();
            String whiteSqName = sq.getPieceName();
            Stack tempMoves = new Stack();

            if (whiteSqName.contains("Pawn"))
            {
                tempMoves = getPawnMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Knight"))
            {
                tempMoves = getKnightMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Bishop"))
            {
                tempMoves = getBishopMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Rook"))
            {
                tempMoves = getRookMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("Queen"))
            {
                tempMoves = getQueenMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            } else if (whiteSqName.contains("King"))
            {
                tempMoves = getKingMoves(sq.getXCoOrdinate(), sq.getYCoOrdinate(), sq.getPieceName());
            }

            while (!(tempMoves.empty()))
            {
                move = (Move) tempMoves.pop();
                completeMoves.push(move);
            }
        }

        temp = (Stack) completeMoves.clone();
        getLandingSquares(temp);
        printStack(temp);

        if (completeMoves.size() == 0)
        {
            //Stalemate
        } else
        {
            System.out.println("=============================================================");
            Stack testMove = new Stack();
            while (!(completeMoves.empty()))
            {
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
            int startX1 = (startingPoint.getXCoOrdinate() * 75) + 20;
            int startY1 = (startingPoint.getYCoOrdinate() * 75) + 20;
            int landingX1 = (landingPoint.getXCoOrdinate() * 75) + 20;
            int landingY1 = (landingPoint.getYCoOrdinate() * 75) + 20;
            System.out.println("===== Move " + startingPoint.getPieceName() + " (" + startingPoint.getXCoOrdinate() + ", " + startingPoint.getYCoOrdinate() + " to (" + landingPoint.getXCoOrdinate() + ", " + landingPoint.getYCoOrdinate() + ")");

            Component c = (JLabel) chessBoard.findComponentAt(startX1, startY1);
            Container parent = c.getParent();
            parent.remove(c);
            int panelID = (startingPoint.getYCoOrdinate() * 8) + startingPoint.getXCoOrdinate();
            panels = (JPanel) chessBoard.getComponent(panelID);
            panels.setBorder(redBorder);
            parent.validate();

            Component l = chessBoard.findComponentAt(landingX1, landingY1);
            if (l instanceof JLabel)
            {
                Container parentLanding = l.getParent();
                JLabel awaitingPieceName = (JLabel) l;
                String pieceCaptured = awaitingPieceName.getIcon().toString();
                if (pieceCaptured.contains("King"))
                {
                    aiWins = true;
                }
                parentLanding.remove(l);
                parentLanding.validate();
                pieces = new JLabel(new ImageIcon(startingPoint.getPieceName() + ".png"));
                int landingPanelID = (landingPoint.getYCoOrdinate() * 8) + landingPoint.getXCoOrdinate();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();

                if (aiWins)
                {
                    JOptionPane.showMessageDialog(null, "AI has won!");
                    System.exit(0);
                }
            } else
            {
                pieces = new JLabel(new ImageIcon(startingPoint.getPieceName() + ".png"));
                int landingPanelID = (landingPoint.getYCoOrdinate() * 8) + landingPoint.getXCoOrdinate();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();
            }
            SwitchTurn(true);
        }
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.4.	Game Movements

    ----------------------------------------------------
    ====================================================
    */

    /*

    This method is called when we press the Mouse.
    We need to find out what piece we selected.
    We may also not have selected a piece!

    */
    public void mousePressed(MouseEvent e)
    {
        //Only accent a left mouse click
        if (!SwingUtilities.isLeftMouseButton(e))
        {
            return;
        } else
        {
            chessPiece = null;
            Component c = chessBoard.findComponentAt(e.getX(), e.getY());
            if (c instanceof JPanel)
                return;

            Point parentLocation = c.getParent().getLocation();
            adjustmentX = parentLocation.x - e.getX();
            adjustmentY = parentLocation.y - e.getY();
            chessPiece = (JLabel) c;
            initialX = e.getX();
            initialY = e.getY();
            startX = (e.getX() / 75);
            startY = (e.getY() / 75);
            chessPiece.setLocation(e.getX() + adjustmentX, e.getY() + adjustmentY);
            chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
            layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);

        }
    }

    //Piece will follow the cursor when the mouse click is held over a piece
    public void mouseDragged(MouseEvent me)
    {
        if (chessPiece == null) return;

        chessPiece.setLocation(me.getX() + adjustmentX, me.getY() + adjustmentY);
    }

    /*

    This method is used when the Mouse is released
    We need to make sure the move is valid before putting the piece back on the board.

    */
    public void mouseReleased(MouseEvent e)
    {
        if (chessPiece == null) return;

        chessPiece.setVisible(false);
        Boolean success = false;
        Boolean progression = false;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        String temp = chessPiece.getIcon().toString();
        String pieceName = temp.substring(0, (temp.length() - 4));
        Boolean validMove = false;

        int landingX = (e.getX() / 75);
        int landingY = (e.getY() / 75);
        int xMovement = Math.abs((e.getX() / 75) - startX);
        int yMovement = Math.abs((e.getY() / 75) - startY);
        System.out.println("----------------------------------------------");
        System.out.println("The piece that is being moved is : " + pieceName);
        System.out.println("The starting coordinates are : " + "( " + startX + "," + startY + ")");
        System.out.println("The xMovement is : " + xMovement);
        System.out.println("The yMovement is : " + yMovement);
        System.out.println("The landing coordinates are : " + "( " + landingX + "," + landingY + ")");
        System.out.println("----------------------------------------------");

        if (!gameOver)
        {
            if (whiteTurn != true)
            {
                /*
                ====================================================
                ----------------------------------------------------

                1.4.1.	Black Pieces

                ----------------------------------------------------
                ====================================================
                */

                /*
                ====================================================
                ----------------------------------------------------

                1.4.1.1.	Black Pawn

                ----------------------------------------------------
                ====================================================
                */
                if (pieceName.equals("BlackPawn"))
                {
                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    }
                    //If pawn is at the starting point
                    else
                    {
                        if (startY == 6)
                        {
                            //Pawn can move 1 or 2 squares forward but not backwards
                            if (((yMovement == 1) || (yMovement == 2)) && (startY > landingY) && (xMovement == 0))
                            {
                                if (yMovement == 2)
                                {
                                    if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), (e.getY() + 75))))
                                    {
                                        validMove = true;
                                    }
                                } else
                                {
                                    if (!piecePresent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    }
                                }
                            }
                            //Pawn can take a piece, if present 1 square diagonally, if it's an enemy
                            else if ((yMovement == 1) && (startY > landingY) && xMovement == 1)
                            {
                                //Check if a piece is present in the square
                                if (piecePresent(e.getX(), e.getY()))
                                {
                                    //If there is, check if it's an opponent
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                        //If Pawn isn't in Starting Position
                        else
                        {
                            if (((yMovement == 1) && (startY > landingY) && (xMovement == 0)))
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                    if (landingY == 0)
                                    {
                                        progression = true;
                                    }
                                }
                            } else if ((yMovement == 1) && (startY > landingY) && xMovement == 1)
                            {
                                //Check if a piece is present in the square
                                if (piecePresent(e.getX(), e.getY()))
                                {
                                    //If there is, check if it's an opponent
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                        if (landingY == 0)
                                        {
                                            progression = true;
                                        }
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.1.2.	Black Knight

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("BlackKnight"))
                {
                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else
                    {
                        if ((yMovement == 1) && (xMovement == 2))
                        {
                            if (!piecePresent(e.getX(), e.getY()))
                            {
                                validMove = true;
                            } else
                            {
                                if (checkBlackOpponent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                }
                            }
                        } else if ((yMovement == 2) && (xMovement == 1))
                        {
                            if (!piecePresent(e.getX(), e.getY()))
                            {
                                validMove = true;
                            } else
                            {
                                if (checkBlackOpponent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                }
                            }
                        } else
                        {
                            validMove = false;
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.1.3.	Black Bishop

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("BlackBishop"))
                {
                    Boolean pieceInTheWay = false;
                    int distanceX = Math.abs(startX - landingX);
                    int distanceY = Math.abs(startY - landingY);

                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else if ((landingX == startX) || (landingY == startY))
                    {
                        validMove = false;
                    } else
                    {
                        if (distanceX == distanceY)
                        {
                            //Direction: South-West
                            if ((startX - landingX < 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-West
                            else if ((startX - landingX < 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-East
                            else if ((startX - landingX > 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: South-East
                            else if ((startX - landingX > 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.1.4.	Black Rook

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("BlackRook"))
                {
                    Boolean pieceInTheWay = false;
                    int distanceX = Math.abs(startX - landingX);
                    int distanceY = Math.abs(startY - landingY);

                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else
                    {
                        //Rook can only move along the X-axis or the Y-axis
                        if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0)))
                        {
                            //Rook moving along the X-axis
                            if (distanceX != 0)
                            {
                                //Moving right on the X-Axis
                                if (startX - landingX > 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX - (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving left on the X-Axis
                                else if (startX - landingX < 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX + (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }
                            //Rook moving along the Y-Axis
                            else if (distanceY != 0)
                            {
                                //Moving up on the Y-Axis
                                if (startY - landingY > 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY - (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving down on the Y-Axis
                                if (startY - landingY < 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY + (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.1.5.	Black Queen

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("BlackQueen"))
                {
                    Boolean pieceInTheWay = false;
                    int distanceX = Math.abs(startX - landingX);
                    int distanceY = Math.abs(startY - landingY);

                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    }
                    //Moves like Rook and Bishop
                    else
                    {
                        if (distanceX == distanceY)
                        {
                            //Direction: South-West
                            if ((startX - landingX < 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-West
                            else if ((startX - landingX < 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-East
                            else if ((startX - landingX > 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: South-East
                            else if ((startX - landingX > 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    }
                                }
                            }
                        }
                        //Rook can only move along the X-axis or the Y-axis
                        else if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0)))
                        {
                            //Rook moving along the X-axis
                            if (distanceX != 0)
                            {
                                //Moving right on the X-Axis
                                if (startX - landingX > 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX - (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving left on the X-Axis
                                else if (startX - landingX < 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX + (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }
                            //Rook moving along the Y-Axis
                            else if (distanceY != 0)
                            {
                                //Moving up on the Y-Axis
                                if (startY - landingY > 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY - (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving down on the Y-Axis
                                if (startY - landingY < 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY + (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.1.6.	Black King

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("BlackKing"))
                {
                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else
                    {
                        //King can move 1 square in any direction
                        if ((yMovement <= 1) && (xMovement <= 1))
                        {
                            if (!(checkKingNear(e.getX(), e.getY())))
                            {
                                //Check if a piece is present in the square
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }
            } else
            {
                /*
                ====================================================
                ----------------------------------------------------

                1.4.2.	White Pieces

                ----------------------------------------------------
                ====================================================
                */

                /*
                ====================================================
                ----------------------------------------------------

                1.4.2.1.	White Pawn

                ----------------------------------------------------
                ====================================================
                */
                if (pieceName.equals("WhitePawn"))
                {
                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    }
                    //If pawn is at the starting point
                    else
                    {
                        if (startY == 1)
                        {
                            //Pawn can move 1 or 2 squares forward but not backwards
                            if (((yMovement == 1) || (yMovement == 2)) && (startY < landingY) && (xMovement == 0))
                            {
                                if (yMovement == 2)
                                {
                                    if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), (e.getY() - 75))))
                                    {
                                        validMove = true;
                                    }
                                } else
                                {
                                    if (!piecePresent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    }
                                }
                            }
                            //Pawn can take a piece, if present 1 square diagonally, if it's an enemy
                            else if ((yMovement == 1) && (startY < landingY) && xMovement == 1)
                            {
                                //Check if a piece is present in the square
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = false;
                                } else
                                {
                                    if (checkWhiteOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                        //If Pawn isn't in Starting Position
                        else
                        {
                            if (((yMovement == 1) && (startY < landingY) && (xMovement == 0)))
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                    if (landingY == 7)
                                    {
                                        success = true;
                                    }
                                }
                            } else if ((yMovement == 1) && (startY < landingY) && xMovement == 1)
                            {
                                //Check if a piece is present in the square
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = false;
                                } else
                                {
                                    if (checkWhiteOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                        if (landingY == 7)
                                        {
                                            success = true;
                                        }
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.2.2.	White Knight

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("WhiteKnight"))
                {
                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else
                    {
                        //Knight moves up 1 square on Y-axis and 2 squares, either way, on the X-axis.
                        if ((yMovement == 1) && (xMovement == 2))
                        {
                            if (!piecePresent(e.getX(), e.getY()))
                            {
                                validMove = true;
                            } else
                            {
                                if (checkWhiteOpponent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                }
                            }
                        }
                        //Knight moves up 2 squares on Y-axis and 1 square, either way, on the X-axis.
                        else if ((yMovement == 2) && (xMovement == 1))
                        {
                            if (!piecePresent(e.getX(), e.getY()))
                            {
                                validMove = true;
                            } else
                            {
                                if (checkWhiteOpponent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                }
                            }
                        } else
                        {
                            validMove = false;
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.2.3.	White Bishop

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("WhiteBishop"))
                {
                    Boolean pieceInTheWay = false;
                    int distanceX = Math.abs(startX - landingX);
                    int distanceY = Math.abs(startY - landingY);

                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else if ((landingX == startX) || (landingY == startY))
                    {
                        validMove = false;
                    } else
                    {
                        if (distanceX == distanceY)
                        {
                            //Direction: South-West
                            if ((startX - landingX < 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-West
                            else if ((startX - landingX < 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-East
                            else if ((startX - landingX > 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: South-East
                            else if ((startX - landingX > 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkWhiteOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.2.4.	White Rook

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("WhiteRook"))
                {
                    Boolean pieceInTheWay = false;
                    int distanceX = Math.abs(startX - landingX);
                    int distanceY = Math.abs(startY - landingY);

                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else
                    {
                        //Rook can only move along the X-axis or the Y-axis
                        if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0)))
                        {
                            //Rook moving along the X-axis
                            if (distanceX != 0)
                            {
                                //Moving right on the X-Axis
                                if (startX - landingX > 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX - (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving left on the X-Axis
                                else if (startX - landingX < 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX + (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }
                            //Rook moving along the Y-Axis
                            else if (distanceY != 0)
                            {
                                //Moving up on the Y-Axis
                                if (startY - landingY > 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY - (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving down on the Y-Axis
                                if (startY - landingY < 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY + (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkWhiteOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.2.5.	White Queen

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("WhiteQueen"))
                {
                    Boolean pieceInTheWay = false;
                    int distanceX = Math.abs(startX - landingX);
                    int distanceY = Math.abs(startY - landingY);

                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    }
                    //Moves like Rook and Bishop
                    else
                    {
                        if (distanceX == distanceY)
                        {
                            //Direction: South-West
                            if ((startX - landingX < 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-West
                            else if ((startX - landingX < 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX + (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: North-East
                            else if ((startX - landingX > 0) && (startY - landingY > 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY - (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }
                            //Direction: South-East
                            else if ((startX - landingX > 0) && (startY - landingY < 0))
                            {
                                for (int i = 0; i < distanceX; i++)
                                {
                                    if (piecePresent((initialX - (75 * i)), (initialY + (75 * i))))
                                    {
                                        pieceInTheWay = true;
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkWhiteOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    }
                                }
                            }
                        }
                        //Rook can only move along the X-axis or the Y-axis
                        else if (((distanceX != 0) && (distanceY == 0)) || ((distanceX == 0) & (distanceY != 0)))
                        {
                            //Rook moving along the X-axis
                            if (distanceX != 0)
                            {
                                //Moving right on the X-Axis
                                if (startX - landingX > 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX - (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving left on the X-Axis
                                else if (startX - landingX < 0)
                                {
                                    for (int i = 0; i < distanceX; i++)
                                    {
                                        if (piecePresent(initialX + (75 * i), e.getY()))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }
                            //Rook moving along the Y-Axis
                            else if (distanceY != 0)
                            {
                                //Moving up on the Y-Axis
                                if (startY - landingY > 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY - (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                                //Moving down on the Y-Axis
                                if (startY - landingY < 0)
                                {
                                    for (int i = 0; i < distanceY; i++)
                                    {
                                        if (piecePresent(e.getX(), initialY + (75 * i)))
                                        {
                                            pieceInTheWay = true;
                                            break;
                                        } else
                                        {
                                            pieceInTheWay = false;
                                        }
                                    }
                                }
                            }

                            if (pieceInTheWay)
                            {
                                validMove = false;
                            } else
                            {
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkWhiteOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                ====================================================
                ----------------------------------------------------

                1.4.2.6.	White King

                ----------------------------------------------------
                ====================================================
                */
                else if (pieceName.equals("WhiteKing"))
                {
                    if ((landingX < 0) || (landingX > 7) || (landingY < 0) || landingY > 7)
                    {
                        validMove = false;
                    } else
                    {
                        //King can move 1 square in any direction
                        if ((yMovement <= 1) && (xMovement <= 1))
                        {
                            if (!(checkKingNear(e.getX(), e.getY())))
                            {
                                //Check if a piece is present in the square
                                if (!piecePresent(e.getX(), e.getY()))
                                {
                                    validMove = true;
                                } else
                                {
                                    if (checkBlackOpponent(e.getX(), e.getY()))
                                    {
                                        validMove = true;
                                    } else
                                    {
                                        validMove = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        ====================================================
        ----------------------------------------------------

        1.4.3.	Valid Moves

        ----------------------------------------------------
        ====================================================
        */
        if (!validMove)
        {
            int location = 0;
            if (startY == 0)
            {
                location = startX;
            } else
            {
                location = (startY * 8) + startX;
            }
            String pieceLocation = pieceName + ".png";
            pieces = new JLabel(new ImageIcon(pieceLocation));
            panels = (JPanel) chessBoard.getComponent(location);
            panels.add(pieces);
        } else
        {
            if (progression)
            {
                int location = 0 + (e.getX() / 75);
                if (c instanceof JLabel)
                {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                } else
                {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }
                SwitchTurn(true);
            } else if (success)
            {
                int location = 56 + (e.getX() / 75);
                if (c instanceof JLabel)
                {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                } else
                {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }
                SwitchTurn(true);
            } else
            {
                if (c instanceof JLabel)
                {
                    Container parent = c.getParent();
                    parent.remove(0);
                    parent.add(chessPiece);
                } else
                {
                    Container parent = (Container) c;
                    parent.add(chessPiece);
                }
                chessPiece.setVisible(true);
                SwitchTurn(true);
            }
            makeAIMove();
        }
    }

    //Empty mouse methods
    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseMoved(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.5.	Main Method

    ----------------------------------------------------
    ====================================================
    */

    /*

    Main method that gets the ball moving.

    */

    public static void main(String[] args)
    {
        ChessProject frame = new ChessProject();
        frame.setTitle("ChessProject - x12337831");
        ImageIcon img = new ImageIcon("BlackQueen.png");
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
