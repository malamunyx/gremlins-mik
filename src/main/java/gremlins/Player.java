package gremlins;

// THIS IS WHAT WE USE TO MOVE RELEASE AND STOP AT A GRID
//        if (!xMoving && (x % 20 == 0)) {
//            xVel = 0;
//        }
//        if (!yMoving && (y % 20 == 0)) {
//            yVel = 0;
//        }
////        this.moveHash = getHashIndex();
//        x += xVel;
//        y +=

// CONSIDER USING THE HASHCODE TO DETERMINE.
// HASHCODE: for int x, y,   hashcode = x/20 + width*(y/20)
// x/20 and y/20 are integer divisions (Floor division)

import processing.core.PImage;

public class Player {
    private int x;
    private int y;

    private int xVel;
    private int yVel;

    private boolean xMoving = false;
    private boolean yMoving = false;

    private int moveHashIdx;

    private static final int speed = 2;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.moveHashIdx = this.getHashIndex();
    }

    public int getHashIndex() {
        return (x / 20) + 36 * (y / 20);
    }
    public int getHashIndex(int x, int y) {
        return (x / 20) + 36 * (y / 20);
    }

    public void draw(PImage img, App app) {
        app.image(img, x, y);

        // check for collisions, especially if tile is door etc.
        if (xVel > 0 && app.mapTiles.containsKey(this.getHashIndex() + 1))
            xMoving = false;

        if (xVel < 0 && app.mapTiles.containsKey(this.getHashIndex() - 1))
            xMoving = false;

        if (yVel > 0 && app.mapTiles.containsKey(this.getHashIndex() + 36))
            yMoving = false;

        if (yVel < 0 && app.mapTiles.containsKey(this.getHashIndex() - 36))
            yMoving = false;




        if (!xMoving && (x % 20 == 0)) {
            xVel = 0;
        }
        if (!yMoving && (y % 20 == 0)) {
            yVel = 0;
        }

        x += xVel;
        y += yVel;
    }

    public void left(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()-1)) {
            xMoving = true;
            xVel = -speed;
        }
    }

    public void right(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()+1)) {
            xMoving = true;
            xVel = speed;
        }
    }

    public void up(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()-36)) {
            yMoving = true;
            yVel = -speed;
        }

    }

    public void down(App app) {
        if (!app.mapTiles.containsKey(this.getHashIndex()+36)) {
            yMoving = true;
            yVel = speed;
        }
    }

    public void xStop() {xMoving = false;}

    public void yStop() {yMoving = false;}
}
