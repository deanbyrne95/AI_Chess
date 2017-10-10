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
		chessBoard.setLayout( new GridLayout(8, 8) );
		chessBoard.setPreferredSize( boardSize );
		chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

		//Chess Board colours
		Color darkBrown = new Color(117,76,36);
		Color lightBrown = new Color(166,124,82);

		//Add colour to the Chess Board
		for (int i = 0; i < 64; i++)
		{
			JPanel square = new JPanel( new BorderLayout() );
			chessBoard.add( square );

			int row = (i / 8) % 2;
			if (row == 0)
				square.setBackground( i % 2 == 0 ? lightBrown : darkBrown );
			else
				square.setBackground( i % 2 == 0 ? darkBrown : lightBrown );
		}
	}

	public void mousePressed(MouseEvent e)
	{

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
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo( null );
		frame.setVisible(true);
	}
}