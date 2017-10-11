/*	 Dean Byrne 	*/
/*		x12337831		*/

/*
Sqaure - Table of Contents
====================================================
----------------------------------------------------

1.	Square Class
    1.1.	Class Constructor(s)
    1.2.	Square Methods
        1.2.1.	Get X Co-Ordinate
        1.2.2.	Get Y Co-Ordinate
        1.2.3.	Get Piece Name

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

1.	Square Class

----------------------------------------------------
====================================================
*/
class Square
{
    public int xCoOrdinate;
    public int yCoOrdinate;
    public String pieceName;

    /*
    ====================================================
    ----------------------------------------------------

    1.1.	Class Constructor(s)

    ----------------------------------------------------
    ====================================================
    */

    public Square(int x, int y)
    {
        xCoOrdinate = x;
        yCoOrdinate = y;
        pieceName = "";
    }

    public Square(int x, int y, String name)
    {
        xCoOrdinate = x;
        yCoOrdinate = y;
        pieceName = name;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.	Square Methods

    ----------------------------------------------------
    ====================================================
    */

    /*
    ====================================================
    ----------------------------------------------------

    1.2.1.	Get X Co-Ordinate

    ----------------------------------------------------
    ====================================================
    */
    public int getXCoOrdinate()
    {
        return xCoOrdinate;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.2.	Get Y Co-Ordinate

    ----------------------------------------------------
    ====================================================
    */
    public int getYCoOrdinate()
    {
        return yCoOrdinate;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.3.	Get Piece Name

    ----------------------------------------------------
    ====================================================
    */
    public String getPieceName()
    {
        return pieceName;
    }
}
