package gremlins;

import processing.core.PImage;

public class Player {
    Game currentGame;
    private int x;
    private int y;

    private int xVel;
    private int yVel;

    //private boolean xMoving = false; // is xMoving and yMoving irrelevant???
    //private boolean yMoving = false; // xDir and yDir does the same for all xDir and yDir != 0;

    private int tar_x;
    private int tar_y;
    private int xDir = 0;
    private int yDir = 0;



    private static final int speed = 2;

    public Player(int x, int y, Game g) {
        this.currentGame = g;
        this.x = x;
        this.y = y;
        this.tar_x = x;
        this.tar_y = y;
        this.xVel = 0;
        this.yVel = 0;
    }

//    public int getPosHashIdx(int x, int y) {
//        return (x / 20) + 36 * (y / 20);
//    }
//    public int getHashIdx() {
//        return (this.x / 20) + 36 * (this.y / 20);
//    }
    public void draw(App a, Game g, PImage img) {
        a.image(img, x, y);

        // can we move? i.e. it's in a square.
        // check for collisions, especially if tile is door etc.
        // Ideally valid for stationary
        if (xVel > 0 && currentGame.getTile(getIndex(x,y)+1) instanceof Wall)
            xStop();

        if (xVel < 0 && currentGame.getTile(getIndex(x,y)-1) instanceof Wall)
            xStop();

        if (yVel > 0 && currentGame.getTile(getIndex(x,y)+36) instanceof Wall)
            yStop();

        if (yVel < 0 && currentGame.getTile(getIndex(x,y)-36) instanceof Wall)
            yStop();

        if (x == tar_x && y == tar_y) {
            xVel = 0;
            yVel = 0;
            if (xDir != 0) {
                tar_x += xDir * 20;
                xVel = xDir * speed;
            } else if (yDir != 0) {
                tar_y += yDir * 20;
                yVel = yDir * speed;
            }
        } else { // move towards target
            x += xVel;
            y += yVel;
        }
    }

    public void left() {
        if (!(currentGame.getTile(getIndex(tar_x, tar_y) - 1) instanceof Wall)) {
            xDir = -1;
        }
    }

    public void right() {
        if (!(currentGame.getTile(getIndex(tar_x, tar_y) + 1) instanceof Wall)) {
            xDir = 1;
        }
    }

    public void up() {
        if (!(currentGame.getTile(getIndex(tar_x, tar_y) - 36) instanceof Wall)) {
            yDir = -1;
        }
    }

    public void down() {
        if (!(currentGame.getTile(getIndex(tar_x, tar_y) + 36) instanceof Wall)) {
            yDir = 1;
        }
    }


    public void xStop() {
        //xMoving = false;
        xDir = 0;
    }

    public void yStop() {
        //yMoving = false;
        yDir = 0;
    }

    public int getIndex(int x, int y) {
        return (x / 20) + 36 * (y / 20);
    }

//    public boolean lose() {
//        return xCell == 1 && yCell == 1;
//    }
}
