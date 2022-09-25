package gremlins;

import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;



public class Gremlin implements Sprite{
    private static final int speed = 2;
    private final Random rg = new Random(); // rg for random generator

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

    private Character getRandomDir() {
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
        return choices.get(rg.nextInt(choices.size()));
    }

    private int up() { // returns the target y.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n-36)) {
            n-= 36;
        }
        return (n / 36)*20;
    }

    private int down() { // returns the target y.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n+36)) {
            n+= 36;
        }
        return (n / 36)*20;
    }

    private int left() { // returns the target y.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n-1)) {
            n-= 1;
        }
        return (n % 36)*20;
    }

    private int right() { // returns the target y.
        int n = getIndex(xPx, yPx);
        while (currentGame.checkWall(n+1)) {
            n+= 1;
        }
        return (n % 36)*20;
    }
}
