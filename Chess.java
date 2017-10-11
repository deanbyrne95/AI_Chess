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

    private void printStack(Stack input){
        Move move;
        Square start, landing;
        while(!input.empty()){
            move = (Move)input.pop();
            start = (Square)move.getStart();
            landing = (Square)move.getLanding();
            System.out.println("The possible move that was found is : (" + start.getXCoOrdinate() + " , " +
                    start.getYCoOrdinate() + "), landing at (" + landing.getXCoOrdinate() + " , " +
                    landing.getYCoOrdinate()+")");
        }
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

    }

    public void mouseReleased(MouseEvent e)
    {

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