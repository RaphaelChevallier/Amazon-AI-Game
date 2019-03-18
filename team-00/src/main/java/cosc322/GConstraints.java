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
public class GConstraints {
    private boolean enemyAction;
    Tile[][] gameboard = new Tile[10][10];
    Queen[] friend;
    Queen[] foe;
    boolean opposition;
    ArrayList<Arrow> arrows;
    ArrayList<Queen> Qmove;

    //Constructor starting the game and board map
    //1st move
    public GConstraints(boolean go) {
        

            //gameboard layout
            gameboard = new Tile[10][10];
            for(int i = 0; i < gameboard.length; i++){
                for(int j = 0; j < gameboard[0].length; j++ ){
                    gameboard[i][j] = null;
                }
            }
            gameboard[0][3] = new Queen(0,3, true);
            gameboard[0][6] = new Queen(0,6, true);
            gameboard[3][0] = new Queen(3,0, true);
            gameboard[3][9] = new Queen(3,9, true);
            gameboard[6][0] = new Queen(6,0, true);
            gameboard[6][9] = new Queen(6,9, true);
            gameboard[9][6] = new Queen(9,6, true);
            gameboard[9][3] = new Queen(9,3, true);
            if(go) {
            foe = new Queen[] { (Queen) gameboard[0][3], (Queen) gameboard[0][6], (Queen) gameboard[3][0], (Queen) gameboard[3][9] };
            friend = new Queen[] { (Queen) gameboard[6][0], (Queen) gameboard[6][9], (Queen) gameboard[9][3], (Queen) gameboard[9][6] };
        }
        else {

            friend = new Queen[] { (Queen) gameboard[0][3], (Queen) gameboard[0][6], (Queen) gameboard[3][0], (Queen) gameboard[3][9] };
            foe= new Queen[] { (Queen) gameboard[6][0], (Queen) gameboard[6][9], (Queen) gameboard[9][3],	(Queen) gameboard[9][6] };
        }

        //list to keep track of arrows queens
        arrows = new ArrayList<>();
        Qmove = new ArrayList<>();
        updatedQmove();
    }



    public void updatedQmove() {
        Qmove.clear();
        for(Queen Q: friend) {
            Qmove.addAll(getValidMoves(Q));
        }
    }

    //Check moves Queen is able to implement, returns best move

    public ArrayList<Queen> getValidMoves(Queen queen) { //getLegalMoves
        ArrayList<Queen> validMoves = new ArrayList<>();
        int cRow = queen.row;
        int cCol = queen.col;

        /*
            Horizontal Movement Check
         */

        // valid moves left
        for(int i = 1; cCol - i >= 0; i++) {
            if(gameboard[cRow][cCol-i] == null) {
                validMoves.add(new Queen(cRow, cCol-i));
            }
            else {
                break;
            }
        }

        // valid moves right
        for(int i = 1; cCol + i <= 9; i++) {
            if(gameboard[cRow][cCol+i] == null) {
                validMoves.add(new Queen(cRow, cCol+i));
            }
            else {
                break;
            }
        }


        /*
            Vertical Movements Check
         */

        // valid moves up
        for(int i = 1; cRow - i >= 0; i++) {
            if(gameboard[cRow-i][cCol] == null) {
                validMoves.add(new Queen(cRow-i, cCol));
            }
            else {
                break;
            }
        }

        // valid moves down
        for(int i = 1; cRow + i <= 9; i++) {
            if(gameboard[cRow+i][cCol] == null) {
                validMoves.add(new Queen(cRow+i, cCol));
            }
            else {
                break;
            }
        }


        /*
            Diagonal Movement Check
         */


        // valid moves diagonally left upwards
        for(int i = 1; cRow - i >= 0 && cCol - i >= 0; i++) {
            if(gameboard[cRow-i][cCol-i] == null) {
                validMoves.add(new Queen(cRow-i, cCol-i));
            }
            else {
                break;
            }
        }

        // valid moves diagonally left downwards
        for(int i = 1; cRow + i <= 9 && cCol - i >= 0; i++) {
            if(gameboard[cRow+i][cCol-i] == null) {
                validMoves.add(new Queen(cRow+i, cCol-i));
            }
            else {
                break;
            }
        }

        // valid moves diagonally right upwards
        for(int i = 1; cRow - i >= 0 && cCol + i <= 9; i++) {
            if(gameboard[cRow-i][cCol+i] == null) {
                validMoves.add(new Queen(cRow-i, cCol+i));
            }
            else {
                break;
            }
        }

        // valid moves diagonally right downwards
        for(int i = 1; cRow + i <= 9 && cCol + i <= 9; i++) {
            if(gameboard[cRow+i][cCol+i] == null) {
                validMoves.add(new Queen(cRow+i, cCol+i));
            }
            else {
                break;
            }
        }

        return validMoves;

    } // end getValidMoves

    //potential queens that can move from enemy side
    public void enemyPotentialAction() { //canEnemyMove
        for(Queen q: foe) {
            int firstRow = q.row;
            int firstCol = q.col;

            if(firstRow - 1 >= 0 && gameboard[firstRow - 1][firstCol] == null){
                enemyAction = true;
                break;
            }

            if(firstRow + 1 <= 9 && gameboard[firstRow + 1][firstCol] == null){
                enemyAction = true;
                break;
            }

            if(firstCol - 1 >= 0 && gameboard[firstRow][firstCol - 1] == null){
                enemyAction = true;
                break;
            }
            if(firstCol + 1 <= 9 && gameboard[firstRow][firstCol + 1] == null){
                enemyAction = true;
                break;
            }

            if((firstRow - 1 >= 0 && firstCol - 1 >= 0) && gameboard[firstRow - 1][firstCol - 1] == null){
                enemyAction = true;
                break;
            }

            if((firstRow + 1 <= 9 && firstCol - 1 >= 0) && gameboard[firstRow + 1][firstCol - 1] == null){
                enemyAction = true;
                break;
            }

            if((firstRow + 1 <= 9 && firstCol + 1 <= 9) && gameboard[firstRow + 1][firstCol + 1] == null){
                enemyAction = true;
                break;
            }
            if((firstRow - 1 >= 0 && firstCol + 1 <= 9) && gameboard[firstRow - 1][firstCol + 1] == null){
                enemyAction = true;
                break;
            }
        }
    } // end of enemyPotentialAction


}
