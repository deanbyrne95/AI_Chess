/*	 Dean Byrne 	*/
/*		x12337831		*/

//Java Imports

import java.util.*;

/*
AI - Table of Contents
====================================================
----------------------------------------------------

1.	AI Class
    1.1.	Class Constructor
    1.2.    Class Methods
        1.2.1.  Random Move
        1.2.2.  Next Best Move*
        1.2.3.  Two Levels Deep Move*


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

1.	AI Class

----------------------------------------------------
====================================================
*/
public class AI
{
    Random rand;

    /*
    ====================================================
    ----------------------------------------------------

    1.1.	AI Constructor(s)

    ----------------------------------------------------
    ====================================================
    */
    public AI()
    {
        rand = new Random();
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.	AI Methods

    ----------------------------------------------------
    ====================================================
    */

    /*
    ====================================================
    ----------------------------------------------------

    1.2.1.	Random Move

    ----------------------------------------------------
    ====================================================
    */
    public Move randomMove(Stack possibilities)
    {
        int moveID = rand.nextInt(possibilities.size());
        System.out.println("AI randomly chose next move: " + moveID);
        for (int i = 1; i < (possibilities.size() - moveID); i++)
        {
            possibilities.pop();
        }
        Move selectedMove = (Move) possibilities.pop();
        return selectedMove;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.2.	Next Best Move*

    ----------------------------------------------------
    ====================================================
    */
    public Move nextBestMove(Stack possibilities)
    {
        Move selectedMove = (Move) possibilities.pop();
        /*
            For this we need to come up with a solution where, when
            the pieces are being moved, they're being smarter rather than
            randomly generated, i.e. when a piece is able to be taken

            For this, we need to find all the pieces on the board
            (their location and names) so the AI can juggle through which ones
            are closer and/or worth the taking.

            It will be similar to the Two-Levels-Deep AI, as it'll only go one
            level deep, to simulate moves and their potential risks/benefits.
        */


        return selectedMove;
    }

    /*
    ====================================================
    ----------------------------------------------------

    1.2.3.	Two Levels Deep Move*

    ----------------------------------------------------
    ====================================================
    */
    public Move twoLevelsDeepMove(Stack possibilities)
    {
        Move selectedMove = (Move) possibilities.pop();


        return selectedMove;
    }
}
