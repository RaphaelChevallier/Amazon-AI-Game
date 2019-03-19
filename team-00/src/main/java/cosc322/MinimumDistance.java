/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosc322;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author ansaerturk
 */
public class MinimumDistance {

    int theirs = 0;
    int ours = 0;
    Tile[][] gameboard = null;
    GConstraints gc = null;
    Queen[] Qs = new Queen[8];

    //Constructor
    public MinimumDistance() {
        gameboard = null;
        gc = null;
        Qs = new Queen[8];
        theirs = 0;
        ours = 0;
    }
//End of constructor


    public void calculate(GConstraints g) {
        gc = g;
        gameboard = gc.gameboard;


        for (int i = 0; i < 8; i++) {
            if (i < 4) {
                Qs[i] = g.findFriend()[i];
            } else {
                Qs[i] = g.findFoe()[i-4];
            }
        }
        ours = 0;
        theirs = 0;


        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                if (gc.gameboard[i][j] == null) {
                    mindistQ(i, j);
                    //find the queen at the minimum distance
                }
            } }}


    public void mindistQ(int row, int col) {

        boolean[][] probed = new boolean[10][10];
        probed[row][col] = true;
        boolean found = false;
        Queue<Tile> q = new LinkedList<>();
        q = totalQmove(q, row, col, probed);


        while (!found) {

            Queue<Tile> tQ = new LinkedList<>();
            int ind = q.size();

            if (ind == 0) {
                found = true;
                break;  }

            for (int i = 0; i < ind; i++) {
                Tile tile = (Tile) q.poll();
                if ((gameboard[tile.row][tile.col] != null) && ((gameboard[tile.row][tile.col] instanceof Queen))) {

                    found = true;
                    boolean QEnemy = ((Queen) gameboard[tile.row][tile.col]).isOpponent;
                    boolean opposed = false;

                    for (Tile t : q){

                        if ((gameboard[t.row][t.col] != null) && (gameboard[t.row][t.col] instanceof Queen) && !(((Queen) gameboard[t.row][t.col]).isOpponent == QEnemy))
                            opposed = true;  }

                    if (opposed) {
                        break;
                    }

                    if (((Queen) gameboard[tile.row][tile.col]).isOpponent) {
                        theirs++;
                    }
                    else {
                        ours++; }

                    break;
                }

                else {
                    probed[tile.row][tile.col] = true;  }

                if (gameboard[tile.row][tile.col] == null) {
                    tQ = totalQmove(tQ, tile.row, tile.col, probed);
                }
            }
            q = tQ;

        }   }


    public Queue<Tile> totalQmove(Queue<Tile> tilequeue, int nrow, int ncol, boolean[][] probed) {

        for (int i = 1; ncol - i >= 0; i++) {
            Tile ntile = new Tile(nrow, ncol - i);
            if (probed[nrow][(ncol - i)] == false) {
                tilequeue.add(ntile);   }
            if (gameboard[nrow][(ncol - i)] != null) {
                break; }
        }

        for (int i = 1; (nrow - i >= 0) && (ncol - i >= 0); i++) {
            Tile ntile = new Tile(nrow - i, ncol - i);
            if (probed[(nrow - i)][(ncol - i)] == false) {
                tilequeue.add(ntile); }
            if (gameboard[(nrow - i)][(ncol - i)] != null) {
                break;
            }   }

        for (int i = 1; nrow - i >= 0; i++) {
            Tile ntile = new Tile(nrow - i, ncol);
            if (probed[(nrow - i)][ncol] == false) {
                tilequeue.add(ntile);
            }
            if (gameboard[(nrow - i)][ncol] != null) {
                break;
            }   }

        for (int i = 1; (nrow - i >= 0) && (ncol + i <= 9); i++) {
            Tile ntile = new Tile(nrow - i, ncol + i);
            if (probed[(nrow - i)][(ncol + i)] == false) {
                tilequeue.add(ntile);
            }
            if (gameboard[(nrow - i)][(ncol + i)] != null) {
                break;
            }   }

        for (int i = 1; ncol + i <= 9; i++) {
            Tile ntile = new Tile(nrow, ncol + i);
            if (probed[nrow][(ncol + i)] == false) {
                tilequeue.add(ntile);
            }
            if (gameboard[nrow][(ncol + i)] != null) {
                break;
            }   }

        for (int i = 1; (nrow + i <= 9) && (ncol + i <= 9); i++) {
            Tile ntile = new Tile(nrow + i, ncol + i);
            if (probed[(nrow + i)][(ncol + i)] == false) {
                tilequeue.add(ntile);
            }
            if (gameboard[(nrow + i)][(ncol + i)] != null) {
                break;
            }   }

        for (int i = 1; nrow + i <= 9; i++) {
            Tile ntile = new Tile(nrow + i, ncol);
            if (probed[(nrow + i)][ncol] == false) {
                tilequeue.add(ntile);
            }
            if (gameboard[(nrow + i)][ncol] != null) {
                break;
            }   }

        for (int i = 1; (nrow + i <= 9) && (ncol - i >= 0); i++) {
            Tile ntile = new Tile(nrow + i, ncol - i);
            if (probed[(nrow + i)][(ncol - i)] == false) {
                tilequeue.add(ntile);
            }
            if (gameboard[(nrow + i)][(ncol - i)] != null) {
                break;
            }   }

        return tilequeue;   }


}