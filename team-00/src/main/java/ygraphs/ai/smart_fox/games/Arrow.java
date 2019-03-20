package ygraphs.ai.smart_fox.games;

public class Arrow extends Tile implements Cloneable {

    public Arrow(int i, int j) {
        super(i, j);
    }

    public Arrow clone(){
        Arrow aCloned = new Arrow(row,col);

        return aCloned;
    }

    public int getColPos(){
        return super.col;
    }

    public int getRowPos(){
        return super.row;
    }

    public int[] combinedMove(int row, int col){
        int[] cmove = new int[2];
        cmove[0] = row;
        cmove[1] = col;

        return cmove; 

    }

}
