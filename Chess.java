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

public class Chess extends JFrame implements MouseListener, MouseMotionListener
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

    public Chess()
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
            pieces = new JLabel(new ImageIcon("Pieces/WhitePawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("Pieces/WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(0);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(1);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(6);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/WhiteBishop.png"));
        panels = (JPanel) chessBoard.getComponent(2);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/WhiteBishop.png"));
        panels = (JPanel) chessBoard.getComponent(5);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/WhiteQueen.png"));
        panels = (JPanel) chessBoard.getComponent(3);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/WhiteKing.png"));
        panels = (JPanel) chessBoard.getComponent(4);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(7);
        panels.add(pieces);
        for (int i = 48; i < 56; i++)
        {
            pieces = new JLabel(new ImageIcon("Pieces/BlackPawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("Pieces/BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(56);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(57);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(62);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/BlackBishop.png"));
        panels = (JPanel) chessBoard.getComponent(58);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/BlackBishop.png"));
        panels = (JPanel) chessBoard.getComponent(61);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/BlackQueen.png"));
        panels = (JPanel) chessBoard.getComponent(59);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/BlackKing.png"));
        panels = (JPanel) chessBoard.getComponent(60);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("Pieces/BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(63);
        panels.add(pieces);
    }

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

    private Boolean checkKingNear(int x, int y)
    {
        if (((piecePresent(x, y + 75)) && getSquarePieceName(x, y + 75).contains("King")) ||
                ((piecePresent(x, y - 75)) && getSquarePieceName(x, y - 75).contains("King")) ||
                ((piecePresent(x + 75, y)) && getSquarePieceName(x + 75, y).contains("King")) ||
                ((piecePresent(x - 75, y)) && getSquarePieceName(x - 75, y).contains("King")) ||
                ((piecePresent(x + 75, y + 75)) && getSquarePieceName(x + 75, y + 75).contains("King")) ||
                ((piecePresent(x + 75, y - 75)) && getSquarePieceName(x + 75, y - 75).contains("King")) ||
                ((piecePresent(x - 75, y + 75)) && getSquarePieceName(x - 75, y + 75).contains("King")) ||
                ((piecePresent(x - 75, y - 75)) && getSquarePieceName(x - 75, y - 75).contains("King")))
        {
            return true;
        } else
        {
            return false;
        }
    }

    private Boolean checkKingNear(Square s)
    {
        int x = s.getXCoOrdinate();
        int y = s.getYCoOrdinate();
        if (((piecePresent(x, y + 75)) && getSquarePieceName(x, y + 75).contains("King")) ||
                ((piecePresent(x, y - 75)) && getSquarePieceName(x, y - 75).contains("King")) ||
                ((piecePresent(x + 75, y)) && getSquarePieceName(x + 75, y).contains("King")) ||
                ((piecePresent(x - 75, y)) && getSquarePieceName(x - 75, y).contains("King")) ||
                ((piecePresent(x + 75, y + 75)) && getSquarePieceName(x + 75, y + 75).contains("King")) ||
                ((piecePresent(x + 75, y - 75)) && getSquarePieceName(x + 75, y - 75).contains("King")) ||
                ((piecePresent(x - 75, y + 75)) && getSquarePieceName(x - 75, y + 75).contains("King")) ||
                ((piecePresent(x - 75, y - 75)) && getSquarePieceName(x - 75, y - 75).contains("King")))
        {
            return true;
        } else
        {
            return false;
        }
    }

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
            System.out.println("The possible move that was found is : (" + start.getXCoOrdinate() + " , " +
                    start.getYCoOrdinate() + "), landing at (" + landing.getXCoOrdinate() + " , " +
                    landing.getYCoOrdinate() + ")");
        }
    }

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

    //Method to colour a number of squares
    private void resetBorders()
    {
        Border empty = BorderFactory.createEmptyBorder();
        for (int i = 0; i < 64; i++)
        {
            JPanel tmp_panel = (JPanel) chessBoard.getComponent(i);
            tmp_panel.setBorder(empty);
        }
    }

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

    public void mouseDragged(MouseEvent me)
    {
        if (chessPiece == null) return;

        chessPiece.setLocation(me.getX() + adjustmentX, me.getY() + adjustmentY);
    }

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
        if( !validMove )
        {
            int location = 0;
            if(startY == 0)
            {
                location = startX;
            }
            else
            {
                location  = ( startY * 8 ) + startX;
            }
            String pieceLocation = pieceName+".png";
            pieces = new JLabel( new ImageIcon( pieceLocation ) );
            panels = ( JPanel )chessBoard.getComponent( location );
            panels.add( pieces );
        }
        else
        {
            if(progression)
            {
                int location = 0 + ( e.getX() / 75 );
                if ( c instanceof JLabel )
                {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel( new ImageIcon( "BlackQueen.png" ) );
                    parent = ( JPanel )chessBoard.getComponent( location );
                    parent.add( pieces );
                }
                else
                {
                    Container parent = ( Container ) c;
                    pieces = new JLabel( new ImageIcon( "BlackQueen.png" ) );
                    parent = (JPanel)chessBoard.getComponent(location);
                    parent.add(pieces);
                }
                SwitchTurn(true);
            }
            else if( success )
            {
                int location = 56 + ( e.getX() / 75 );
                if ( c instanceof JLabel ) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel( new ImageIcon( "WhiteQueen.png" ) );
                    parent = ( JPanel )chessBoard.getComponent( location );
                    parent.add( pieces );
                }
                else
                {
                    Container parent = ( Container ) c;
                    pieces = new JLabel( new ImageIcon( "WhiteQueen.png" ) );
                    parent = (JPanel)chessBoard.getComponent(location);
                    parent.add(pieces);
                }
                SwitchTurn(true);
            }
            else
            {
                if (c instanceof JLabel)
                {
                    Container parent = c.getParent();
                    parent.remove(0);
                    parent.add( chessPiece );
                }
                else
                {
                    Container parent = (Container)c;
                    parent.add( chessPiece );
                }
                chessPiece.setVisible(true);
                SwitchTurn(true);
            }

        }
    }

    //Empty mouse methods
    public void mouseMoved(MouseEvent e)
    {

    }

    public void mouseClicked(MouseEvent e)
    {

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public static void main(String[] args)
    {
        Chess frame = new Chess();
        frame.setTitle("https://github.com/deanbyrne95/AI_Chess");
        ImageIcon img = new ImageIcon("BlackQueen.png");
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}