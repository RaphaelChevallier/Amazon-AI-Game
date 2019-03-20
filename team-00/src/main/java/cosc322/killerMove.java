/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosc322;

import java.util.ArrayList;

/**
 *
 * @author Tyrel
 */
//The goal is to take the moves from the alpha beta prune and define killer moves within them
public class killerMove {//killer move heuristic
    int numberKillerMoves;
    int ply;
    ArrayList killerMoves;
    int move;
    TreeNodes potentialKiller;
    
    public killerMove(){
        
    }
    //pass in a list of moves from the alpha beta prune, and determine whether each is a killer move
    public TreeNodes killerPrune( ArrayList<TreeNodes> moves){//should replace with data structure for list of moves
  
        for(TreeNodes s: moves){
            potentialKiller = s;
        ;
            for(TreeNodes k: moves){
                if(k == potentialKiller){
                    return k;
                }
            }
        }
        return null;//if no killer moves are found;
    }
        public ArrayList<TreeNodes> killerInsert(ArrayList<TreeNodes> moves, TreeNodes killerMove){
        
        if(killerMoves.isEmpty()){//initializes killer move list
            killerMoves = moves;
        }
        moves.add(0, killerMove);//add the killer move to the list and shift other moves  to the right
        if(moves.size()> 6){
            moves.remove(moves.size() -1);//if the set is too large trim off the last killer move in the list
            
        }
        
//        killerMoves[ply][0] = move;
//        for(int numberKillerMoves = 0; numberKillerMoves < killerMoves[ply].length; numberKillerMoves--){
//            int killer = killerMoves[ply][numberKillerMoves];
//            for(int i = 0; i < movesCount; i++){
//                if(moves.get(i) == killerMove){
//                    
//                }
//            }
//        }
    }
    
}
