package gremlins;

import processing.core.PImage;

public class Powerup extends Tile {
    private int spawnDelay;
    private boolean canEffect;
    public Powerup(int xPx, int yPx) {
        super(xPx, yPx);
        spawnDelay = App.FPS * getRandomInterval(5, 10);
        canEffect = false;
    }

    @Override
    public void draw(App a, PImage img) {
        a.image(img, xPx, yPx);

        if (spawnDelay != 0) {
            --spawnDelay;
        } else {
            a.image(a.speedboots, xPx, yPx);
            canEffect = true;
        }
    }

    public int getRandomInterval(int origin, int bound) {
        int t;
        do {
            t = Level.rg.nextInt(bound);
        } while (t < origin);

        return t;
    }

    public boolean canEffectPlayer() {
        return canEffect;
    }

    public void intervalInitiate() {
        spawnDelay = getRandomInterval(15, 30) * App.FPS;
        canEffect = false;
    }
}
