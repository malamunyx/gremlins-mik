package gremlins;

abstract class Tile {
    protected int x;
    protected int y;
    /*
    There will be 2 2 walls (one brick, one stonewall, and a door).
    */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
