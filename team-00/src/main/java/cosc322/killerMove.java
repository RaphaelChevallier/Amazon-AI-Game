/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosc322;

/**
 *
 * @author Tyrel
 */
//The goal is to take the moves from the alpha beta prune and define killer moves within them
public class killerMove {//killer move heuristic
    int numberKillerMoves;
    int ply;
    int killerMoves[][];
    int move;
    
    public killerMove(int ply, int numberOfMoves){//ply is distance from root and numberofmoves is killer move total count
        killerMoves = new int[ply][numberOfMoves];
        this.ply = ply;
        
    }
    //pass in a list of moves from the alpha beta prune, and determine whether each is a killer move
    public void killerPrune(int move){//should replace with data structure for list of moves
        for(int i = killerMoves[ply].length - 2; i >= 0; i--){
            killerMoves[ply][i + 1] = killerMoves[ply][i];
        }
        killerMoves[ply][0] = move;
    }
    
}
