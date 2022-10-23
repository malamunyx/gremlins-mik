package gremlins;

import processing.core.PImage;

import java.util.ArrayList;

public class Gremlin extends LiveEntity {
    private static final int SPEED = 1;
    private int localSpeed;
    private boolean stopped = true;
    private boolean frozen = false;

    private int freezeCountdown = App.FPS * App.FREEZETIME;

    /**
     * Gremlin Class constructor.
     * @param xPx Integer pixel position on the x-axis.
     * @param yPx Integer pixel position on the y-axis.
     * @param l Associated level object.
     * @return  Gremlin object.
     */
    public Gremlin(int xPx, int yPx, Level l) {
        super(xPx, yPx, l);
        this.localSpeed = SPEED;
        this.dir = '\0';
    }

    /**
     * Calls the app to draw the Gremlin whilst updating its attributes every frame.
     * @param a App that extends Processing Applet handling all Processing library processes.
     * @param img PImage variable stored in App class.
     */
    @Override
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);

        if (!stopped && !canMove(getIndex(xPx, yPx) + getDirNum())) {
            stopped = true;
        }

        if (frozen) {
            --freezeCountdown;
        }

        if (freezeCountdown == 0) {
            freezeCountdown = App.FPS * App.FREEZETIME;
            unfreeze();
        }

        if (xPx == xTarget && yPx == yTarget) {
            xVel = 0;
            yVel = 0;

            if (stopped) {
                dir = getRandomDir();
                stopped = !stopped;
            }

            if (dir == 'U') {
                yVel = -1 * localSpeed;
                yTarget += yVel * App.SPRITESIZE;
            } else if (dir == 'D') {
                yVel = 1 * localSpeed;
                yTarget += yVel * App.SPRITESIZE;
            } else if (dir == 'L') {
                xVel = -1 * localSpeed;
                xTarget += xVel * App.SPRITESIZE;
            } else if (dir == 'R') {
                xVel = 1 * localSpeed;
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

    /**
     * Returns location index increment/decrement number based on Gremlin directionality.
     * [Left: -1] [Right: +1] [Up: -36(Map width)] [Down: +36 (Map width)]
     * @return Integer location index difference.
     */
    @Override
    public int getDirNum() {
        if (dir == 'L') {
            return -1;
        } else if (dir == 'R') {
            return 1;
        } else if (dir == 'U') {
            return -36;
        } else if (dir == 'D') {
            return 36;
        } else {
            return 0;
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
     * Respawns Gremlin to random location, resetting itself to original level start attributes.
     */
    @Override
    public void reset() {
        /*
         * Unfreeze() is in reset() as it can incentivise players
         * to not spam fireballs with Iceball projectiles.
         */

        int sIdx;
        do {
            sIdx = Level.rg.nextInt(33 * 36);
            xPx = (sIdx % 36) * 20;
            yPx = (sIdx / 36) * 20;
        } while (!currentLevel.canWalk(sIdx) || !greaterTenRadius(currentLevel.getPlayer()));

        this.dir = '\0';
        stopped = true;
        this.xVel = 0;
        this.yVel = 0;
        this.xTarget = xPx;
        this.yTarget = yPx;
        unfreeze();
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
        unfreeze();
    }

    /**
     * Shoot a Slime projectile, instantiating and storing a projectile in the currentLevel sprites ArrayList.
     */
    @Override
    public void fire() {
        currentLevel.addSprite(Sprite.slimeFactory(xPx, yPx, dir, currentLevel));
    }

    /**
     * Return the horizontal pixel position of the Sprite centre.
     * @return Integer x plane pixel position.
     */
    @Override
    public int getCentreX() {
        return this.xPx + Sprite.OFFSET;
    }

    /**
     * Return the vertical pixel position of the Sprite centre.
     * @return Integer y plane pixel position.
     */
    @Override
    public int getCentreY() {
        return this.yPx + Sprite.OFFSET;
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
    public char getRandomDir() {
        ArrayList<Character> choices = new ArrayList<>();
        if (currentLevel.canWalk(getIndex(xPx, yPx) - 1))
            choices.add('L');
        if (currentLevel.canWalk(getIndex(xPx, yPx) + 1))
            choices.add('R');
        if (currentLevel.canWalk(getIndex(xPx, yPx) - 36))
            choices.add('U');
        if (currentLevel.canWalk(getIndex(xPx, yPx) + 36))
            choices.add('D');

        if (choices.isEmpty()) {
            return '\0';
        } else if (choices.size() > 1) { // If it is dead end, remove opposite direction
            if (dir == 'L') {
                choices.remove(getIdxInList(choices, 'R'));
            } else if (dir == 'R') {
                choices.remove(getIdxInList(choices, 'L'));
            } else if (dir == 'U') {
                choices.remove(getIdxInList(choices, 'D'));
            } else if (dir == 'D') {
                choices.remove(getIdxInList(choices, 'U'));
            }
        }
        return choices.get(Level.rg.nextInt(choices.size()));
    }

    /**
     * Freezes the gremlin object.
     * localSpeed becomes zero and frozen boolean becomes true.
     */
    public void freeze() {
        localSpeed = 0;
        frozen = true;
    }

    /**
     * Undo any changes from freezing method.
     * localSpeed becomes intended speed and frozen boolean becomes false.
     */
    public void unfreeze() {
        localSpeed = SPEED;
        frozen = false;
    }

    /**
     * Getter for frozen boolean variable.
     * @return true whenever gremlin is frozen.
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Sets direction character, used for testing purposes.
     * @param c Character.
     */
    public void setDir(char c) {
        this.dir = c;
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
