package gremlins;

import processing.core.PImage;

public class Player implements Sprite {
    private static final int speed = 2;
    private Game currentGame;
    private int charge;
    private int xPx;
    private int yPx;
    private int xOrigin; // THIS IS DUE FOR REMOVAL WHEN RESET WORKS
    private int yOrigin;

    private int xTarget;
    private int yTarget;
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
        this.xTarget = xPx;
        this.yTarget = yPx;
        this.charge = g.wizardCooldown;
    }

    @Override
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);
        a.rect(600, 680, ((float)charge / currentGame.wizardCooldown)*100, 5);

        // RECHARGE COOLDOWN
        if (charge < currentGame.wizardCooldown)
            ++charge;

        // Wall collisions
        if (xVel < 0 && !canMove(getIndex(xPx, yPx) - 1))
            xStop();
        if (xVel > 0 && !canMove(getIndex(xPx, yPx) + 1))
            xStop();
        if (yVel < 0 && !canMove(getIndex(xPx, yPx) - 36))
            yStop();
        if (yVel > 0 && !canMove(getIndex(xPx, yPx) + 36))
            yStop();

        if (xPx == xTarget && yPx == yTarget) {
            // Stop on square
            xVel = 0;
            yVel = 0;
            if (xDir != 0) {
                xTarget += xDir * App.SPRITESIZE;
                xVel += xDir * speed;
            } else if (yDir != 0) {
                yTarget += yDir * App.SPRITESIZE;
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
            return (xDist < App.SPRITESIZE && yDist < App.SPRITESIZE);
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
        if (canMove(getIndex(xTarget, yTarget) - 1)) {
            yStop();
            xDir = -1;
        }
    }

    public void right() {
        dir = 'R';
        if (canMove(getIndex(xTarget, yTarget) + 1)) {
            yStop();
            xDir = 1;
        }
    }

    public void up() {
        dir = 'U';
        if (canMove(getIndex(xTarget, yTarget) - 36)) {
            xStop();
            yDir = -1;
        }
    }

    public void down() {
        dir = 'D';
        if (canMove(getIndex(xTarget, yTarget) + 36)) {
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
        if (charge == currentGame.wizardCooldown) {
            currentGame.addSprite(Sprite.fireballFactory(xPx, yPx, dir, currentGame));
            charge = 0;
        }
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
        this.xTarget = xPx;
        this.yTarget = yPx;
        xStop();
        yStop();
    }

    @Override
    public int getCentreX() {
        return this.xPx + Sprite.xOffset;
    }

    @Override
    public int getCentreY() {
        return this.yPx + Sprite.yOffset;
    }
}
