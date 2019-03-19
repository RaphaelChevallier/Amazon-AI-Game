package cosc322;

import java.util.ArrayList;

public class MinMaxTree {
    private TreeNodes root;
    private int depth;
    public int eval;
    private int movesLeft;
    private MinimumDistance minDist = new MinimumDistance();
    private ArrayList<TreeNodes> frontier = new ArrayList<>();

    //constructor. Tree composed of made nodes in class
    public MinMaxTree(TreeNodes node) {
        this.root = node;
        this.depth = 0;
        this.eval = 0;
    }

    public void getDepth() {
        TreeNodes n = this.root;
        int tempDepth = 0;
        while (n != null) {
            if (!n.getChildren().isEmpty()) {
                n = n.getChildren().get(0);
            } else {
                break;
            }
            tempDepth++;
        }
        this.depth = tempDepth;
    }

    private int Pruning(TreeNodes N, int D, int alpha, int beta, boolean maxPlayer) {
        if (D == 0 || N.getChildren().size() == 0) {
            eval++;
            minDist.calculate(N.GConstraints);
            N.setValue(minDist.ours - minDist.ours);
            int val = N.getValue();
            return val;
        }

        if (maxPlayer) {
            int V = Integer.MIN_VALUE;
            for (TreeNodes S : N.getChildren()) {
                V = Math.max(V, Pruning(S, D - 1, alpha, beta, false));
                alpha = Math.max(alpha, V);
                if (beta <= alpha) {
                    break;
                }
            }
            N.setValue(V);
            return V;

        } else {
            int V = Integer.MAX_VALUE;
            for (TreeNodes S : N.getChildren()) {
                V = Math.min(V, Pruning(S, D - 1, alpha, beta, true));
                beta = Math.min(beta, V);
                if (beta <= alpha)
                    break;
            }
            N.setValue(V);
            return V;
        }
    }

    //adding successor if odd or even so we know if its a foe or friend
    public void grow() {
        ArrayList<TreeNodes> newFrontier = new ArrayList<>();
        // See if we're at the very first root node
        if (depth != 0) {
            if (depth % 2 == 0) {
                for (TreeNodes S : frontier)
                    newFrontier.addAll(S.addNodes(true));
            } else {
                for (TreeNodes S : frontier)
                    newFrontier.addAll(S.addNodes(false));
            }
        } else {
            newFrontier.addAll(root.addNodes(true));
        }

        // Clear the old frontier, and add in the new SearchTreeNodes to use
        frontier.clear();
        for (TreeNodes S : newFrontier) {
            // deepCopy in order to keep Object relationships
            TreeNodes newNode = new TreeNodes(S.GConstraints.transfer());
            frontier.add(newNode);
        }
        depth++;
    }

    //how far we'll go with pruning
    public void performAlphaBeta() {
        eval = 0;
        getDepth();
        Pruning(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }

    //performs the actual pruning and taking off unwanted nodes
    public void reduce() {
        int avg = 0;
        // Get the average value from all SearchTreeNodes in the frontier
        for (TreeNodes S : frontier) {
            minDist.calculate(S.GConstraints);
            S.setValue(minDist.ours);
            avg += S.getValue();
        }
        if (frontier.size() != 0)
            avg = avg / frontier.size();

        // If the nodes are below the average value, "cut" them
        ArrayList<TreeNodes> toRemove = new ArrayList<>();
        for (TreeNodes S : frontier) {
            if (S.getValue() < avg) {
                toRemove.add(S);
            }
        }

        // "cuts" the SearchTreeNodes that were below the average value
        for (TreeNodes S : toRemove) {
            frontier.remove(S);
        }
    }
    //this will actually initiate the move of the queen at its position to a new one
    public void rootMove(Queen qCurrentPos, Arrow a) {
        movesLeft++;
        // Adds an arrow to the board
        root.GConstraints.addArrow(a);

        // Check whether or not the queen is the opponent's, or ours
        if (qCurrentPos.isOpponent) {
            for (Queen Q : root.GConstraints.foe) {
                if (Q.row == qCurrentPos.previousRow && Q.col == qCurrentPos.previousCol) {
                    Q.moveQueen(qCurrentPos.row, qCurrentPos.col);
                }

            }
        } else {
            for (Queen Q : root.GConstraints.friend) {
                if (Q.row == qCurrentPos.previousRow && Q.col == qCurrentPos.previousCol) {
                    Q.moveQueen(qCurrentPos.row, qCurrentPos.col);
                }
            }
        }
        root.GConstraints.updateEndTurn();
        this.clear();
    }
    //based off results we will make a move
    public TreeNodes Move() {
        if(movesLeft <= 20) {
            this.grow();
            this.reduce();
        }
        else if(movesLeft > 20 && movesLeft <= 30) {
            this.grow();
            this.reduce();
            this.grow();
        }
        else if(movesLeft > 30 && movesLeft <= 45) {
            this.grow();
            this.reduce();
            this.grow();
            this.reduce();
        }
        else if(movesLeft > 45 && movesLeft <= 60) {
            this.grow();
            this.reduce();
            this.grow();
            this.reduce();
            this.grow();
            this.reduce();
        }
        else if(movesLeft > 60) {
            this.grow();
            this.grow();
            this.reduce();
            this.grow();
            this.reduce();
            this.grow();
            this.reduce();
        }
        this.performAlphaBeta();
        TreeNodes bestMove = this.getMoveAfterAlphaBeta();
        this.rootMove(bestMove.getQueen(), bestMove.getArrow());
        return bestMove;
    }
    private TreeNodes getMoveAfterAlphaBeta() {
        int max = Integer.MIN_VALUE;
        TreeNodes best = null;
        ArrayList<TreeNodes> currentBest = new ArrayList<>(); // just to initialize currentBest
        for (TreeNodes S : root.getChildren()) {
            if (max < S.getValue()) {
                max = S.getValue();
                currentBest.add(S);
            }
        }
        if (currentBest.size() > 1) {
            best = currentBest.get((currentBest.size() - 1));
        } else {
            try {
                best = currentBest.get(0);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Reached the goal state");
            }
        }
        return best;
    }
    private void clear() {
        depth = 0;
        frontier.clear();
        root.getChildren().clear();
    }
}
