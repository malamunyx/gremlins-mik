package gremlins;

import processing.core.PImage;

public class Player implements Sprite {
    private static final int speed = 2;
    private Game currentGame;
    private int xPx;
    private int yPx;
    private int xOrigin; // THIS IS DUE FOR REMOVAL WHEN RESET WORKS
    private int yOrigin;

    private int tar_x;
    private int tar_y;
    private int xDir = 0;
    private int yDir = 0;
    private int xVel = 0;
    private int yVel = 0;

    private char dir = 'L';

    public Player(int xPx, int yPx, Game g) {
        this.currentGame = g;
        this.xPx = xPx;
        this.yPx = yPx;
        this.xOrigin = xPx;
        this.yOrigin = yPx;
        this.tar_x = xPx;
        this.tar_y = yPx;
    }

    @Override
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

        // Wall collisions
        if (xVel < 0 && !canMove(getIndex(xPx, yPx) - 1))
            xStop();
        if (xVel > 0 && !canMove(getIndex(xPx, yPx) + 1))
            xStop();
        if (yVel < 0 && !canMove(getIndex(xPx, yPx) - 36))
            yStop();
        if (yVel > 0 && !canMove(getIndex(xPx, yPx) + 36))
            yStop();

        if (xPx == tar_x && yPx == tar_y) {
            // Stop on square
            xVel = 0;
            yVel = 0;
            if (xDir != 0) {
                tar_x += xDir * App.SPRITESIZE;
                xVel += xDir * speed;
            } else if (yDir != 0) {
                tar_y += yDir * App.SPRITESIZE;
                yVel += yDir * speed;
            }
        } else {
            // Move towards target.
            xPx += xVel;
            yPx += yVel;
        }
    }

    @Override
    public boolean spriteCollision(Sprite s) {
        if (s instanceof Gremlin || s instanceof Slime) {
            int xDist = Math.abs(s.getCentreX() - this.getCentreX());
            int yDist = Math.abs(s.getCentreY() - this.getCentreY());
            return (xDist < 20 && yDist < 20);
        } else {
            return false;
        }
    }

//    @Override getdirnum might just be an abstract class.....
    public int getDirNum() throws Error {
        switch (dir) {
            case 'L':
                return 0;
            case 'R':
                return 1;
            case 'U':
                return 2;
            case 'D':
                return 3;
            default:
                throw new Error(String.format("Direction %c is not a valid direction", dir));
        }
    }

    public void left() {
        dir = 'L';
        if (canMove(getIndex(tar_x, tar_y) - 1)) {
            yStop();
            xDir = -1;
        }
    }

    public void right() {
        dir = 'R';
        if (canMove(getIndex(tar_x, tar_y) + 1)) {
            yStop();
            xDir = 1;
        }
    }

    public void up() {
        dir = 'U';
        if (canMove(getIndex(tar_x, tar_y) - 36)) {
            xStop();
            yDir = -1;
        }
    }

    public void down() {
        dir = 'D';
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

    public void fire() {
        currentGame.addSprite(Sprite.fireballFactory(xPx, yPx, dir, currentGame));
    }

    public boolean canMove(int idx) {
        return currentGame.canWalk(idx);
    }

    @Override
    public int getIndex(int x, int y) {
        return (x / 20) + (y / 20) * 36;
    }

    @Override
    public void reset() {
        this.xPx = xOrigin;
        this.yPx = yOrigin;
        this.tar_x = xPx;
        this.tar_y = yPx;
        xStop();
        yStop();
    }

    @Override
    public int getCentreX() {
        return this.xPx + xOffset;
    }

    @Override
    public int getCentreY() {
        return this.yPx + yOffset;
    }
}
