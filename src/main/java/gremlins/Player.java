package gremlins;

import processing.core.PImage;

public class Player extends LiveEntity implements Sprite {
    private static final int speed = 2;
    private int charge;
    private int xDir = 0;
    private int yDir = 0;

//    private char dir = 'L';

    public Player(int xPx, int yPx, Level g) {
        super(xPx, yPx, g);
        this.charge = g.wizardCooldown;
        this.dir = 'L';
    }

    /**
     * Calls the app to draw the Player mana cooldown bar whilst updating its attributes every frame.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable stored in App class.
     */
    @Override
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);
        a.rect(600, 680, ((float)charge / currentLevel.wizardCooldown)*100, 5);

        // RECHARGE COOLDOWN
        if (charge < currentLevel.wizardCooldown)
            ++charge;

        // Wall collisions
        if (xVel < 0 && !canMove(getIndex(xPx, yPx) - 1))
            leftStop();
        if (xVel > 0 && !canMove(getIndex(xPx, yPx) + 1))
            rightStop();
        if (yVel < 0 && !canMove(getIndex(xPx, yPx) - 36))
            upStop();
        if (yVel > 0 && !canMove(getIndex(xPx, yPx) + 36))
            downStop();

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

    /**
     * Boolean checker that returns whether collision with Gremlin or Slime object occurs.
     * @param s Objects implementing Sprite interface.
     * @return True whenever Sprites instances of specific objects have x and y distances less than sprite size (20 pixels).
     */
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

    /**
     * Returns location index increment/decrement number based on Player directionality.
     * [Left: -1] [Right: +1] [Up: -36(Map width)] [Down: +36 (Map width)]
     * @return Integer location index difference.
     * @throws IllegalArgumentException Whenever any char parameter does not represent a direction.
     */
    @Override
    public int getDirNum() throws IllegalArgumentException {
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
                throw new IllegalArgumentException(String.format("Direction %c is not a valid direction", dir));
        }
    }

    /**
     * Changes player direction to 'L'. If it can move left, primes player for movement next frame.
     */
    public void left() {
        dir = 'L';
        if (canMove(getIndex(xTarget, yTarget) - 1)) {
            yDir = 0;
            xDir = -1;
        }
    }

    /**
     * Changes player direction to 'R'. If it can move right, primes player for movement next frame.
     */
    public void right() {
        dir = 'R';
        if (canMove(getIndex(xTarget, yTarget) + 1)) {
            yDir = 0;
            xDir = 1;
        }
    }

    /**
     * Changes player direction to 'U'. If it can move up, primes player for movement next frame.
     */
    public void up() {
        dir = 'U';
        if (canMove(getIndex(xTarget, yTarget) - 36)) {
            xDir = 0;
            yDir = -1;
        }
    }

    /**
     * Changes player direction to 'D'. If it can move down, primes player for movement next frame.
     */
    public void down() {
        dir = 'D';
        if (canMove(getIndex(xTarget, yTarget) + 36)) {
            xDir = 0;
            yDir = 1;
        }
    }

    /**
     * Sets x movement primer to 0 if moving left, keeping player movement until it is positioned in an exact tile.
     */
    public void leftStop() {
        if (xDir == -1)
            xDir = 0;
    }

    /**
     * Sets x movement primer to 0 if moving right, keeping player movement until it is positioned in an exact tile.
     */
    public void rightStop() {
        if (xDir == 1)
            xDir = 0;
    }

    /**
     * Sets y movement primer to 0 if moving up, keeping player movement until it is positioned in an exact tile.
     */
    public void upStop() {
        if (yDir == -1)
            yDir = 0;
    }

    /**
     * Sets y movement primer to 0 if moving down, keeping player movement until it is positioned in an exact tile.
     */
    public void downStop() {
        if (yDir == 1)
            yDir = 0;
    }

    /**
     * Shoot a fireball projectile, instantiating and storing a projectile in the currentLevel sprites ArrayList.
     * Checks Player cooldown prior to Fireball object instantiation.
     */
    @Override
    public void fire() {
        if (charge == currentLevel.wizardCooldown) {
            currentLevel.addSprite(Sprite.fireballFactory(xPx, yPx, dir, currentLevel));
            charge = 0;
        }
    }

    /**
     * Gets position index of a given x and y pixel position.
     * @param xPx Horizontal pixel position.
     * @param yPx Vertical pixel position.
     * @return Integer location index.
     */
    @Override
    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + (yPx / 20) * 36;
    }

    /**
     * Respawn player to original location, resetting itself to original level start attributes.
     */
    @Override
    public void reset() {
        SetPosition(xOrigin, yOrigin);
        xDir = 0;
        yDir = 0;
    }

    /**
     * Return the horizontal pixel position of the Sprite centre.
     * @return Integer x plane pixel position.
     */
    @Override
    public int getCentreX() {
        return this.xPx + Sprite.Offset;
    }

    /**
     * Return the vertical pixel position of the Sprite centre.
     * @return Integer y plane pixel position.
     */
    @Override
    public int getCentreY() {
        return this.yPx + Sprite.Offset;
    }

    /**
     * Sets position of Player object to desired pixel position.
     * xTarget and yTarget resets to Player's respective pixel position.
     * @param xPx x pixel position.
     * @param yPx y pixel position.
     */
    public void SetPosition(int xPx, int yPx) {
        this.xPx = xPx;
        this.yPx = yPx;
        this.xTarget = xPx;
        this.yTarget = yPx;
    }
}
