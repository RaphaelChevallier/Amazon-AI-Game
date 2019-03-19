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
    
    public GConstraints(Queen[] foe, Queen[] friend, ArrayList<Arrow> arrow){
        this.foe = foe;
        this.friend = friend;
        this.arrows = arrow;
        updateEndTurn();
    }
    public Queen[] findFoe(){
        return this.foe;
    }
    public Queen[] findFriend(){
        return this.friend;
    }

    public void updatedQmove() {
        Qmove.clear();
        for(Queen Q: friend) {
            Qmove.addAll(getValidMoves(Q));
        }
    }
    public void updateEndTurn(){
        clearBoard();
        for(Queen q: friend){
            if(friend != null){
                gameboard[q.row][q.col] = q;
                
            }
        }
        for(Queen q: foe){
            if(foe!=null){
                gameboard[q.row][q.col]=q;
            }
        }
        for(Arrow arrow: arrows){
            if(arrow!=null){
                gameboard[arrow.row][arrow.col] = arrow;
            }
        }
    }
    //cloning board and game states for space efficiency
    public GConstraints transfer(){
        Queen[] tempFriend = new Queen[4];
        Queen[] tempFoe = new Queen[4];
        ArrayList<Arrow> tempArrows = new ArrayList<>();
        for(int i = 0; i < tempFoe.length; i ++){
            tempFriend[i] = friend[i].clone();
            tempFoe[i] = foe[i].clone().clone();
        }
        if(arrows != null){
            for(Arrow temp: arrows){
                if(temp != null){
                    tempArrows.add(new Arrow(temp.row, temp.col));
                }
            }
        }
        GConstraints constraints = new GConstraints(tempFriend, tempFoe, tempArrows);
        return constraints;
    }
    public void clearBoard(){
        for(int i = 0; i < 10; i++ ){
            for(int j = 0; j < 10; j++){
            gameboard[i][j] = null;
        }
        }
    }

    //Check moves Queen is able to implement, returns best move

    public ArrayList<Queen> getValidMoves(Queen queen) {
        ArrayList<Queen> validMoves = new ArrayList<>();
        int cRow = queen.row;
        int cCol = queen.col;

        for(int i = 1; cCol - i >= 0; i++) {
            if(gameboard[cRow][cCol-i] == null) {
                validMoves.add(new Queen(cRow, cCol-i));
            }
            else {
                break;
            }
        }


        for(int i = 1; cCol + i <= 9; i++) {
            if(gameboard[cRow][cCol+i] == null) {
                validMoves.add(new Queen(cRow, cCol+i));
            }
            else {
                break;
            }
        }

        for(int i = 1; cRow - i >= 0; i++) {
            if(gameboard[cRow-i][cCol] == null) {
                validMoves.add(new Queen(cRow-i, cCol));
            }
            else {
                break;
            }
        }

        for(int i = 1; cRow + i <= 9; i++) {
            if(gameboard[cRow+i][cCol] == null) {
                validMoves.add(new Queen(cRow+i, cCol));
            }
            else {
                break;
            }
        }

        for(int i = 1; cRow - i >= 0 && cCol - i >= 0; i++) {
            if(gameboard[cRow-i][cCol-i] == null) {
                validMoves.add(new Queen(cRow-i, cCol-i));
            }
            else {
                break;
            }
        }

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

    }
    public ArrayList<Arrow> arrowMoves(int row, int col){
        ArrayList<Arrow> allowedMoves = new ArrayList<>();
        int currentRow = row;
        int currentColumn = col;
        //left
        for(int i = 1; currentColumn - i>= 0; i++){
            if(gameboard[currentRow][currentColumn-i] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow, currentColumn-i));
            }
        }
        //right
        for(int i = 1; currentColumn + i<= 9; i++){
            if(gameboard[currentRow][currentColumn+i] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow, currentColumn+i));
            }
        }
        //up
        for(int i = 1; currentRow - i>= 0; i++){
            if(gameboard[currentRow-i][currentColumn] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow-i, currentColumn));
            }
        }
        //down
        for(int i = 1; currentRow + i<= 9; i++){
            if(gameboard[currentRow+i][currentColumn] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow + i, currentColumn));
            }
        }
        //left and up
        for(int i = 1; currentColumn - i>= 0 && currentRow - i >=0; i++){
            if(gameboard[currentRow - i][currentColumn-i] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow - i, currentColumn-i));
            }
        }
        //left and down
         for(int i = 1; currentColumn - i>= 0 && currentRow + i <=9; i++){
            if(gameboard[currentRow + i][currentColumn-i] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow + i, currentColumn-i));
            }
        }
         //right and down
           for(int i = 1; currentColumn + i<=9 && currentRow + i <=9; i++){
            if(gameboard[currentRow + i][currentColumn + i] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow + i, currentColumn + i));
            }
        }
           //right and up
             for(int i = 1; currentColumn + i <= 9 && currentRow - i >=0 ; i++){
            if(gameboard[currentRow - i][currentColumn + i] !=null){
                break;
            }else{
                allowedMoves.add(new Arrow(currentRow - i, currentColumn+i));
            }
        }
            return allowedMoves;
        
    }
    public void addArrow (Arrow arrow){
        arrows.add(arrow);
        updateEndTurn();
    }

    //potential queens that can move from enemy side
    public void enemyPotentialAction() {
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
        }}
       public boolean goalTest(){
           if(enemyAction == false || Qmove.isEmpty()){
               if(enemyAction == false){
                   System.out.println("We win!");
               }
               else{
                   System.out.println("We lose!");
               }
               return true;
           }
           else{
               return false;
           }
       }
     // end of enemyPotentialAction
    public void printBoard(){
        String board = "";
        String line = "\nx--- --- --- --- --- --- --- --- --- ---x";
        for(int i = 0; i < 10; i++){
            board += line + "\n";
            for(int j = 0; j < 10; j++){
                board+= "| ";
            
            if (gameboard[i][j] == null) board += "  ";
            else if (gameboard[i][j] instanceof Queen){
                if (gameboard[i][j] == findFoe()[0] || gameboard[i][j] == findFoe()[1] || gameboard[i][j] == findFoe()[2]  || gameboard[i][j] == findFoe()[3]){
                    board += "B ";
                    
                }
                else board += "W ";
                
            }else board += "a ";
                
            
        }
        board +="|";
    }
    board += line;
    System.out.println(board);

}
}