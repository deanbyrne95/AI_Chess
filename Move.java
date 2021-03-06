/*	 Dean Byrne 	*/
/*		x12337831		*/

/*
Move - Table of Contents
====================================================
----------------------------------------------------

1.	Move Class
    1.1.	Class Constructor(s)
    1.2.	Move Methods
        1.2.1.	Get Start
        1.2.2.	Get Landing

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

1.	Move Class

----------------------------------------------------
====================================================
*/

class Move
{
    Square start;
    Square landing;

    /*
    ====================================================
    ----------------------------------------------------

    1.1.	Class Constructor(s)

    ----------------------------------------------------
    ====================================================
    */
    public Move(Square x, Square y)
    {
        start = x;
        landing = y;
    }

    public Move()
    {

    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.	Move Methods

    ----------------------------------------------------
    ====================================================
    */

    /*
    ====================================================
    ----------------------------------------------------

    1.2.1.	Get Start

    ----------------------------------------------------
    ====================================================
    */
    public Square getStart()
    {
        return start;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.2.	Get Landing

    ----------------------------------------------------
    ====================================================
    */
    public Square getLanding()
    {
        return landing;
    }
}
