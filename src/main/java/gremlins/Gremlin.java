package gremlins;

import processing.core.PImage;

import java.util.ArrayList;

public class Gremlin implements Sprite {
    private static final int speed = 1;
    private Level currentLevel;
    private int xPx;
    private int yPx;
    private int xOrigin;
    private int yOrigin;

    private int xTarget;
    private int yTarget;
    private int xVel = 0;
    private int yVel = 0;

    private char dir = '\0';
    private boolean stopped = true;

    public Gremlin(int xPx, int yPx, Level g) {
        this.currentLevel = g;
        this.xPx = xPx;
        this.yPx = yPx;
        this.xOrigin = xPx;
        this.yOrigin = yPx;
        this.xTarget = xPx;
        this.yTarget = yPx;
    }

    /**
     * Calls the app to draw the Gremlin whilst updating its attributes every frame.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable stored in App class.
     */
    @Override
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

        if (!stopped && !canMove(getIndex(xPx, yPx) + getDirNum()))
            stopped = true;

        if (xPx == xTarget && yPx == yTarget) {
            xVel = 0;
            yVel = 0;

            if (stopped) {
                dir = getRandomDir();
                stopped = !stopped;
            }

            if (dir == 'U') {
                yVel = -1 * speed;
                yTarget += yVel * App.SPRITESIZE;
            } else if (dir == 'D') {
                yVel = 1 * speed;
                yTarget += yVel * App.SPRITESIZE;
            } else if (dir == 'L') {
                xVel = -1 * speed;
                xTarget += xVel * App.SPRITESIZE;
            } else if (dir == 'R') {
                xVel = 1 * speed;
                xTarget += xVel * App.SPRITESIZE;
            }
        } else {
            xPx += xVel;
            yPx += yVel;
        }

    }

    /**
     * Boolean checker that returns whether collision with Player or Fireball object occurs.
     * @param s Objects implementing Sprite interface.
     * @return True whenever Sprites instances of specific objects have x and y distances less than sprite size (20 pixels).
     */
    @Override
    public boolean spriteCollision(Sprite s) {
        if (s instanceof Player || s instanceof Fireball) {
            int xDist = Math.abs(s.getCentreX() - this.getCentreX());
            int yDist = Math.abs(s.getCentreY() - this.getCentreY());
            return (xDist < App.SPRITESIZE && yDist < App.SPRITESIZE);
        } else {
            return false;
        }
    }

    //    @Override maybe part of livenentity class?

    /**
     * Returns location index increment/decrement number based on Gremlin directionality.
     * [Left: -1] [Right: +1] [Up: -36(Map width)] [Down: +36 (Map width)]
     * @return Integer location index difference.
     */
    public int getDirNum() {
        if (dir == 'L')
            return -1;
        else if (dir == 'R')
            return 1;
        else if (dir == 'U')
            return -36;
        else if (dir == 'D')
            return 36;
        else
            return 0;
    }

    /**
     * Returns whether Gremlin is able to move to a location index.
     * @param idx Location index.
     * @return Boolean determining whether no Wall exists, or if it does, it is broken.
     */
    public boolean canMove(int idx) {
        return currentLevel.canWalk(idx);
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
     * Respawns Gremlin to random location, resetting itself to original level start attributes.
     */
    @Override
    public void reset() {
        int sIdx;
        do {
            sIdx = currentLevel.getRandomInt(33 * 36);
            xPx = (sIdx % 36) * 20;
            yPx = (sIdx / 36) * 20;
        } while (!currentLevel.canWalk(sIdx) || !greaterTenRadius(currentLevel.getPlayer()));

        this.dir = '\0';
        stopped = true;
        this.xVel = 0;
        this.yVel = 0;
        this.xTarget = xPx;
        this.yTarget = yPx;
    }

    /**
     * Respawns Gremlin to original spawn location, resetting itself to original level start attributes.
     */
    public void levelReset() {
        this.dir = '\0';
        stopped = true;
        this.xPx = xOrigin;
        this.yPx = yOrigin;
        this.xVel = 0;
        this.yVel = 0;
        this.xTarget = xPx;
        this.yTarget = yPx;

    }

    /**
     * Shoot a Slime projectile, instantiating and storing a projectile in the currentLevel sprites ArrayList.
     */
    public void fire() {
        currentLevel.addSprite(Sprite.slimeFactory(xPx, yPx, dir, currentLevel));
    }

    /**
     * Returns whether Gremlin position relative to Player is greater than 10 tiles radius.
     * @param p Player object referenced by the current Level object.
     * @return True whether horizontal or vertical distance is greater than 10 tiles.
     */
    public boolean greaterTenRadius(Player p) {
        int xTileDist = Math.abs(p.getCentreX() - this.getCentreX()) / App.SPRITESIZE;
        int yTileDist = Math.abs(p.getCentreY() - this.getCentreY()) / App.SPRITESIZE;
        return (xTileDist > 10 || yTileDist > 10);
    }

    /**
     * Checks for unbroken walls around current tile location in search for valid location.
     * @return Random direction char ['U', 'D', 'L', 'R']
     */
    private Character getRandomDir() {
        ArrayList<Character> choices = new ArrayList<>();
        if (currentLevel.canWalk(getIndex(xPx, yPx) - 1))
            choices.add('L');
        if (currentLevel.canWalk(getIndex(xPx, yPx) + 1))
            choices.add('R');
        if (currentLevel.canWalk(getIndex(xPx, yPx) - 36))
            choices.add('U');
        if (currentLevel.canWalk(getIndex(xPx, yPx) + 36))
            choices.add('D');

        // If it is not surrounded by walls, remove opposite direction
        if (choices.size() > 1) {
            if (dir == 'L')
                choices.remove(getIdxInList(choices, 'R'));
            else if (dir == 'R')
                choices.remove(getIdxInList(choices, 'L'));
            else if (dir == 'U')
                choices.remove(getIdxInList(choices, 'D'));
            else if (dir == 'D')
                choices.remove(getIdxInList(choices, 'U'));
        }
        return choices.get(currentLevel.getRandomInt(choices.size()));
    }

    /**
     * Return the horizontal pixel position of the Sprite centre.
     * @return Integer x plane pixel position.
     */
    @Override
    public int getCentreX() {
        return this.xPx + Sprite.xOffset;
    }

    /**
     * Return the vertical pixel position of the Sprite centre.
     * @return Integer y plane pixel position.
     */
    @Override
    public int getCentreY() {
        return this.yPx + Sprite.yOffset;
    }

    /**
     * Iterates through an ArrayList parameter searching for given char parameter, returning the index of desired character.
     * @param dirArray Character arraylist, used in getRandomDir function.
     * @param c Direction character to search.
     * @return Integer index of direction character.
     * @throws IllegalStateException If character is not found. In getRandomDir(), during an obstruction, opposite always contained in dirArray.
     */
    private int getIdxInList(ArrayList<Character> dirArray, Character c) throws IllegalStateException {
        for (int i = 0; i < dirArray.size(); ++i) {
            if (dirArray.get(i).equals(c))
                return i;
        }
        throw new IllegalStateException("Invalid char in CharacterArray. Direction obstruction ensures presence of opposite directions");
    }
}
