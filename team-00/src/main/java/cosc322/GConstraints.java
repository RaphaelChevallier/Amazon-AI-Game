package cosc322;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ansaerturk
 */
public class GConstraint {

    Tile[][] gameboard = new Tile[10][10];
    Queen[] opponent;
    Queen[] ally;
    boolean opposition;
    ArrayList<Arrow> arrows;
    ArrayList<Queen> Qmove;

    //Constructor starting the game and board map
    //1st move
    public GConstraint(boolean go) {
        if(go) {

            //there can be a smarter way to implement this grid layout
            gameboard = new Tile[][] {
                    { null, null, null, new Queen(0, 3, true), null, null, new Queen(0, 6, true), null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { new Queen(3, 0, true), null, null, null, null, null, null, null, null, new Queen(3, 9, true) },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { new Queen(6, 0, false), null, null, null, null, null, null, null, null, new Queen(6, 9, false) },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, new Queen(9, 3, false), null, null, new Queen(9, 6, false), null, null, null } };


            opponent = new Queen[] { (Queen) gameboard[0][3], (Queen) gameboard[0][6], (Queen) gameboard[3][0], (Queen) gameboard[3][9] };
            ally = new Queen[] { (Queen) gameboard[6][0], (Queen) gameboard[6][9], (Queen) gameboard[9][3], (Queen) gameboard[9][6] };

        }

        else {
            gameboard = new Tile[][] {
                    { null, null, null, new Queen(0, 3, false), null, null, new Queen(0, 6, false), null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { new Queen(3, 0, false), null, null, null, null, null, null, null, null, new Queen(3, 9, false) },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { new Queen(6, 0, true), null, null, null, null, null, null, null, null, new Queen(6, 9, true) },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null, null, null },
                    { null, null, null, new Queen(9, 3, true), null, null, new Queen(9, 6, true), null, null, null } };

            ally = new Queen[] { (Queen) gameboard[0][3], (Queen) gameboard[0][6], (Queen) gameboard[3][0], (Queen) gameboard[3][9] };
            opponent = new Queen[] { (Queen) gameboard[6][0], (Queen) gameboard[6][9], (Queen) gameboard[9][3],	(Queen) gameboard[9][6] };
        }

        //list to keep track of arrows queens
        arrows = new ArrayList<>();
        Qmove = new ArrayList<>();
        updatedQmove();
    }



    public void updatedQmove() {
        Qmove.clear();
        for(Queen Q: ally) {
            Qmove.addAll(getmoves(Q));
        }
    }

    //Implement getmoves here
}
