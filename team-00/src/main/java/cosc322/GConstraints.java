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

     protected void addArrow(Arrow arrownew) {
        arrows.add(arrownew);
        updatemove();
    } 

    private void resetboard() {
        for(int i = 0; i <= 9; i++) {
            for(int j = 0; j <= 9; j++) {
                gameboard[i][j] = null;
            }
        }
    } 

  //update after each move
    protected void updatemove() {
        resetboard();

        for(Queen q: ally) {
        	if(ally != null){
        		gameboard[q.row][q.col] = q;
        	}
        }

        for(Queen q: opponent) {
        	if(opponent != null) {
                gameboard[q.row][q.col] = q;
        	}
        }

        for(Arrow arrow: arrows) {
            if(arrow != null) {
                gameboard[arrow.row][arrow.col] = arrow;
            } } }


    //decides winner and loser
    protected boolean finalresult() {
        if (opposition == false || Qmove.size() == 0) {
            if (opposition == false) {
                System.out.println("Opponent Lost");
            } else {
                System.out.println("Opponent Won");
            }
            return true;
        }
        else {
            return false;
        } }
     


    public void outputboard() {
        String layout = "";
        String ln = "\nx--- --- --- --- --- --- --- --- --- ---x";
        for (int i = 0; i < 10; i++) {
            layout += ln + "\n";
            for (int j = 0; j < 10; j++) {
                layout += "| ";
                if (gameboard[i][j] == null) layout += "  ";
                else if (gameboard[i][j] instanceof Queen) {
                    if (gameboard[i][j] == enemyfind()[0] || gameboard[i][j] == enemyfind()[1] ||
                            gameboard[i][j] == enemyfind()[2] || gameboard[i][j] == enemyfind()[3]) {
                        layout += "B ";
                    } else layout += "W ";
                } else layout += "a ";
            }
            layout += "|";
        }
        layout += ln;
        System.out.println(layout);
    } 
}

