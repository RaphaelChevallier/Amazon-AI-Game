package ygraphs.ai.smart_fox.games;

import java.util.ArrayList;

//This class is what builds the tree nodes with heuristics attached to add to the big minmax tree
public class TreeNodes {
    private int heuristic;
    private Arrow arrow;
    private Queen queen;
    private ArrayList<TreeNodes> children = new ArrayList<>();
    protected GConstraints GConstraints;

    //constructor for tree nodes for search tree
    public TreeNodes(GConstraints board, Queen q, Arrow A) {
        GConstraints = board;
        queen = q;
        arrow = A;
    }

    public TreeNodes(GConstraints board, Queen q, Arrow A, int val) {
        GConstraints = board;
        queen = q;
        arrow = A;
        heuristic = val;
    }
    //getting postions
    public TreeNodes(GConstraints board) {
        GConstraints = board;
    }
    public Arrow getArrow() {
        return arrow;
    }
    public Queen getQueen() {
        return queen;
    }
    public ArrayList<TreeNodes> getChildren() {
        return children;
    }
    public void setChildren(ArrayList<TreeNodes> children){
        this.children = children;
    }

    public int getValue() {
        return heuristic;
    }
    public void setValue(int V) {
        heuristic = V;
    }
    //children given to TreeNodes corresponding to heuristic
    public ArrayList<TreeNodes> addNodes(boolean ourMove) {
        ArrayList<TreeNodes> expanded = getSuccessors(GConstraints, ourMove);
        for (TreeNodes S : expanded) {
            children.add(S);
        }
        return children;
    }

    //assigns all possible nodes after a state is given from the game board
    public ArrayList<TreeNodes> getSuccessors(GConstraints state, boolean us) {
        int count = 0;
        ArrayList<TreeNodes> children = new ArrayList<>();
        Queen[] queensArray;
        if(us) {
            queensArray = state.findFriend();
        } else{
            queensArray = state.findFoe();
        }
        for(int i = 0; i < queensArray.length; i++) {
            ArrayList<Queen> currentQueenMoves = new ArrayList<>();
            // Make a deep copy of all queen moves at specific queen
            for(Queen q : state.getValidMoves(queensArray[i])) {
                q.previousRow = queensArray[i].row;
                q.previousCol = queensArray[i].col;
                currentQueenMoves.add(q);
            }
            for(int j = 0; j < currentQueenMoves.size(); j++) {
                GConstraints tempBoard = state.transfer();
                if(us) {
                    tempBoard.friend[i].row = currentQueenMoves.get(j).row;
                    tempBoard.friend[i].col = currentQueenMoves.get(j).col;
                    tempBoard.friend[i].previousRow = currentQueenMoves.get(j).previousRow;
                    tempBoard.friend[i].previousCol = currentQueenMoves.get(j).previousCol;
                }
                else {
                    tempBoard.foe[i].row = currentQueenMoves.get(j).row;
                    tempBoard.foe[i].col = currentQueenMoves.get(j).col;
                    tempBoard.foe[i].previousRow = currentQueenMoves.get(j).previousRow;
                    tempBoard.foe[i].previousCol = currentQueenMoves.get(j).previousCol;
                }
                tempBoard.updateEndTurn();

                //will iterate through every arrow shot possible for each queen on the board
                ArrayList<Arrow> legalArrowMoves = new ArrayList<>();
                for(Arrow a : tempBoard.arrowMoves(currentQueenMoves.get(j).row, currentQueenMoves.get(j).col)) {
                    legalArrowMoves.add(a.clone());
                }
                for(int k = 0; k < legalArrowMoves.size(); k++) {
                    // Create a new state where this SearchTreeNode will exist
                    GConstraints newState = tempBoard.transfer();
                    // Add this arrow shot to the new state
                    newState.addArrow(legalArrowMoves.get(k));
                    newState.updateEndTurn();
                    // Make new SearchTreeNode
                    TreeNodes S = new TreeNodes(newState, currentQueenMoves.get(j), legalArrowMoves.get(k), 0);
                    children.add(S);
                    count++;
                }
            }
        }
        return children;
    }
}

