package gremlins;

import processing.core.PImage;

import java.util.ArrayList;

public class Gremlin implements Sprite {
    // must revamp gremlin movement
    private static final int speed = 1;
    private Game currentGame;
    private int xPx;
    private int yPx;
    private int xOrigin; // THIS IS DUE FOR REMOVAL WHEN RESET WORKS
    private int yOrigin;

    private int xTarget;
    private int yTarget;
//    private int xDir = 0;
//    private int yDir = 0;
    private int xVel = 0;
    private int yVel = 0;

    private char dir = '\0';
    private boolean stopped = true;

    public Gremlin(int xPx, int yPx, Game g) {
        this.currentGame = g;
        this.xPx = xPx;
        this.yPx = yPx;
        this.xOrigin = xPx;
        this.yOrigin = yPx;
        this.xTarget = xPx;
        this.yTarget = yPx;
    }

    public void fire() {
        currentGame.addSprite(Sprite.slimeFactory(xPx, yPx, dir, currentGame));
    }

    @Override
    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);
//        a.text(dir, xPx, yPx);
//        a.fill(0, 10, 10);


        if (!stopped && !currentGame.canWalk(getIndex(xPx, yPx) + getDirNum()))
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

    public void stop() {
        xVel = 0;
        yVel = 0;
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
        int sIdx;
        do {
            sIdx = currentGame.getRandomInt(33 * 36);
            xPx = (sIdx % 36) * 20;
            yPx = (sIdx / 36) * 20;
        } while (!currentGame.canWalk(sIdx) || !greaterTenRadius(currentGame.getPlayer()));

        this.dir = '\0';
        stopped = true;
        this.xVel = 0;
        this.yVel = 0;
        this.xTarget = xPx;
        this.yTarget = yPx;
    }

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

    public boolean greaterTenRadius(Player p) {
        int xTileDist = Math.abs(p.getCentreX() - this.getCentreX()) / App.SPRITESIZE;
        int yTileDist = Math.abs(p.getCentreY() - this.getCentreY()) / App.SPRITESIZE;
        return (xTileDist > 10 || yTileDist > 10);
    }

    private Character getRandomDir() {
        ArrayList<Character> choices = new ArrayList<>();
        if (currentGame.canWalk(getIndex(xPx, yPx) - 1))
            choices.add('L');
        if (currentGame.canWalk(getIndex(xPx, yPx) + 1))
            choices.add('R');
        if (currentGame.canWalk(getIndex(xPx, yPx) - 36))
            choices.add('U');
        if (currentGame.canWalk(getIndex(xPx, yPx) + 36))
            choices.add('D');

        // If it is not surrounded by walls, remove opposite direction
        if (choices.size() > 1) {
            if (dir == 'L')
                choices.remove(getDirInList(choices, 'R'));
            else if (dir == 'R')
                choices.remove(getDirInList(choices, 'L'));
            else if (dir == 'U')
                choices.remove(getDirInList(choices, 'D'));
            else if (dir == 'D')
                choices.remove(getDirInList(choices, 'U'));
        }
        return choices.get(currentGame.getRandomInt(choices.size()));
    }

    @Override
    public int getCentreX() {
        return this.xPx + xOffset;
    }

    @Override
    public int getCentreY() {
        return this.yPx + yOffset;
    }

    private int getDirInList(ArrayList<Character> dirArray, Character c) throws Error {
        for (int i = 0; i < dirArray.size(); ++i) {
            if (dirArray.get(i).equals(c))
                return i;
        }
        throw new Error("Invalid char in CharacterArray");
    }
}
