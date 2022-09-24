package gremlins;

import processing.core.PImage;

public class Player implements Sprite { // make player extend Tile??????
    private final Game currentGame;
    private int xPx;
    private int yPx;

    private int xVel;
    private int yVel;

    //private boolean xMoving = false; // is xMoving and yMoving irrelevant???
    //private boolean yMoving = false; // xDir and yDir does the same for all xDir and yDir != 0;

    private int tar_x;
    private int tar_y;
    private int xDir = 0;
    private int yDir = 0;

    private int imgDir;



    private static final int speed = 2;

    public Player(int xPx, int yPx, Game g) {
        this.currentGame = g;
        this.xPx = xPx;
        this.yPx = yPx;
        this.tar_x = xPx;
        this.tar_y = yPx;
        this.xVel = 0;
        this.yVel = 0;
        this.imgDir = 1;
    }

    public void draw(App a, Game g, PImage img) {
        a.image(img, xPx, yPx);

        // can we move? i.e. it's in a square.
        // check for collisions, especially if tile is door etc.
        // Ideally valid for stationary
        if (xVel > 0 && currentGame.getTile(getIndex(xPx, yPx)+1) instanceof Wall)
            xStop();

        if (xVel < 0 && currentGame.getTile(getIndex(xPx, yPx)-1) instanceof Wall)
            xStop();

        if (yVel > 0 && currentGame.getTile(getIndex(xPx, yPx)+36) instanceof Wall)
            yStop();

        if (yVel < 0 && currentGame.getTile(getIndex(xPx, yPx)-36) instanceof Wall)
            yStop();

        if (xPx == tar_x && yPx == tar_y) {
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
            xPx += xVel;
            yPx += yVel;
        }
    }

    public void left() {
        imgDir = 0;
        if (canMove(getIndex(tar_x, tar_y) - 1)) {
            yStop();
            xDir = -1;
        }
    }

    public void right() {
        imgDir = 1;
        if (canMove(getIndex(tar_x, tar_y) + 1)) {
            yStop();
            xDir = 1;
        }
    }

    public void up() {
        imgDir = 2;
        if (canMove(getIndex(tar_x, tar_y) - 36)) {
            xStop();
            yDir = -1;
        }
    }

    public void down() {
        imgDir = 3;
        if (canMove(getIndex(tar_x, tar_y) + 36)) {
            xStop();
            yDir = 1;
        }
    }

    public void xStop() {
        xDir = 0;
    }

    public void yStop() {
        yDir = 0;
    }

    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }

    public boolean canMove(int index) {
        if (currentGame.getTile(index) instanceof Wall) {
            return ((Wall) currentGame.getTile(index)).isBroken();
        } else {
            return true;
        }
    }

    public int getImgDir() {
        return this.imgDir;
    }

    public boolean pWinLevel() {
        return currentGame.getTile(getIndex(xPx, yPx)) instanceof Exit;
    }
}
