package at.htlsteyr.minesweeper;

public class Bombs {


    private final boolean bombs;
    private int x;
    private int y;

    public Bombs(int x, int y, boolean bombs) {
        this.x = x;
        this.y = y;
        this.bombs = bombs;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getBombs(){
        return bombs;
    }
}
