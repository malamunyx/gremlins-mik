package gremlins;

import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Gremlin implements Sprite{
    private final int xOrigin; // ORIGIN, xPx and yPx returns to this, dir ='\0' everytime player loses.
    private final int yOrigin;
    private static final int speed = 1;
    //private final Random rg = new Random(); // rg for random generator

    private final Game currentGame;
    private int xPx;
    private int yPx;
    private int tar_x;
    private int tar_y;
    char dir;

    private int xVel;
    private int yVel;

    public Gremlin(int xPx, int yPx, Game g) {
        this.currentGame = g;
        this.xOrigin = xPx;
        this.yOrigin = yPx;
        this.xPx =xPx;
        this.yPx = yPx;
        this.tar_x = xPx;
        this.tar_y = yPx;
        this.dir = '\0'; // at the start, null char
    }

    public void update(App a, PImage img) {
        a.image(img, xPx, yPx);
        a.text(dir, xPx, yPx);
        a.fill(0, 10, 10);

        // Get Random dir.
        if (xPx == tar_x && yPx == tar_y) {
            xVel = 0;
            yVel = 0;
            dir = getRandomDir();

            if (dir == 'U') {
                tar_y = up();
                yVel = -1;
            } else if (dir == 'D') {
                tar_y = down();
                yVel = 1;
            } else if (dir == 'L') {
                tar_x = left();
                xVel = -1;
            } else if (dir == 'R') {
                tar_x = right();
                xVel = 1;
            }

        } else {
            xPx += xVel * speed;
            yPx += yVel * speed;
        }
    }

    public int getIndex(int xPx, int yPx) {
        return (xPx / 20) + 36 * (yPx / 20);
    }

    private Character getRandomDir() { // Create Char[] {'L', 'R', 'U', 'D"}
        ArrayList<Character> choices = new ArrayList<>();
        if (currentGame.checkWall(getIndex(xPx, yPx) - 1)) {
            choices.add('L');
        }
        if (currentGame.checkWall(getIndex(xPx, yPx) + 1)) {
            choices.add('R');
        }
        if (currentGame.checkWall(getIndex(xPx, yPx) - 36)) {
            choices.add('U');
        }
        if (currentGame.checkWall(getIndex(xPx, yPx) + 36)) {
            choices.add('D');
        }

        // If it is not surrounded by walls. remove opposite direction.
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

    private int up() { // returns for target y.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n-36)) {
            n-= 36;
        }
        return (n / 36)*20;
    }

    private int down() { // returns for target y.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n+36)) {
            n+= 36;
        }
        return (n / 36)*20;
    }

    private int left() { // returns for target x.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n-1)) {
            n-= 1;
        }
        return (n % 36)*20;
    }

    private int right() { // returns for target x.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n+1)) {
            n+= 1;
        }
        return (n % 36)*20;
    }

    private int getDirInList(ArrayList<Character> dirArray, Character c) throws RuntimeException {
        for (int i = 0; i < dirArray.size(); ++i) {
            if (dirArray.get(i).equals(c))
                    return i;
        }
        throw new RuntimeException("Invalid char");
    }
}
