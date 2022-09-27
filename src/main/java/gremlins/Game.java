package gremlins;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Random rg = new Random();
    double wizardCooldown;
    double enemyCooldown;
    private Player player;

    private HashMap<Integer, Tile> tileMap = new HashMap<>();
    private ArrayList<Sprite> sprites = new ArrayList<>();

    public static Game generateGame(File currentLevel, double wizardCooldown, double enemyCooldown) {
        return new Game(currentLevel, wizardCooldown, enemyCooldown);
    }

    public Game(File currentLevel, double wizardCooldown, double enemyCooldown) {
        loadMap(currentLevel);
        this.wizardCooldown = wizardCooldown;
        this.enemyCooldown = enemyCooldown;
    }

    public void draw(App a) {
        a.background(211);

        // draw map -> turn into own method?
        for (Tile t : tileMap.values()) {
            if (t instanceof Wall) {
                if (((Wall) t).canBreak() && ((Wall) t).getStatus() != 5)
                    t.draw(a, a.brickwall[((Wall) t).getStatus()]);
                else if (!((Wall) t).canBreak())
                    t.draw(a, a.stonewall);
            } else if (t instanceof Exit) {
                t.draw(a, a.door);
            }
        }
        updateSprites(a);

        if (playerWin()) {
            a.noLoop();
            a.background(0, 255, 0);
        }

    }

    public Tile getTile(int idx) {
        return this.tileMap.get(idx);
    }

    /**
     * Determines if location is accessible by sprites
     * @param idx hash index of tile location
     * @return boolean value representing tile accessibility
     */
    public boolean canWalk(int idx) {
        if (getTile(idx) instanceof Wall)
            return (((Wall) getTile(idx)).isBroken());
        else
            return true;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    public int getRandomInt(int n) {
        return rg.nextInt(n);
    }

    private void updateSprites(App a) {
        // Draw or update sprites.
        for (int i = sprites.size()-1; i >= 0; --i) {
            Sprite s1 = sprites.get(i);

            if (s1 instanceof Gremlin) {
                s1.update(a, a.gremlin);
                if (a.frameCount % (enemyCooldown * 60) == 0)
                    ((Gremlin) s1).fire();
            }
            else if (s1 instanceof Player)
                s1.update(a, a.wizard[player.getDirNum()]);
            else if (s1 instanceof Fireball)
                s1.update(a, a.fireball);
            else if (s1 instanceof Slime) {
                s1.update(a, a.slime);
            }
        }

        // check collisions
        for (int i = sprites.size()-1; i >= 0; --i) {
            Sprite s1 = sprites.get(i);

            for (int j = sprites.size()-1; j >= 0; --j) {
                Sprite s2 = sprites.get(j);
                if (i != j && s1.spriteCollision(s2)) {
                    s1.reset();
                    s2.reset();
                }
            }
        }

        // check for sprites to delete (neutralised projectiles)
        for (int i = sprites.size()-1; i >= 0; --i) {
            Sprite s1 = sprites.get(i);
            if (s1 instanceof Projectile) {
                if (((Projectile) s1).isNeutralised())
                    sprites.remove(i);
            }
        }
    }

    public boolean playerWin() {
        return getTile(player.getIndex(player.getCentreX(), player.getCentreY())) instanceof Exit;
    }

    private void loadMap(File levelFile) {
        try {
            Scanner sc = new Scanner(levelFile);

            int hashIdx = 0;
            int i = 0;
            while (sc.hasNextLine()) {
                String s = sc.nextLine();

                for (int j = 0; j < s.length(); ++j) {
                    switch (s.charAt(j)) {
                        case 'X':
                            tileMap.put(hashIdx, Tile.stoneWallFactory(j*20, i*20));
                            break;
                        case 'B':
                            tileMap.put(hashIdx, Tile.brickWallFactory(j*20, i*20));
                            break;
                        case 'W':
                            player = Sprite.playerFactory(j*20, i*20, this);
                            addSprite(player);
                            break;
                        case 'E':
                            tileMap.put(hashIdx, Tile.exitTileFactory(j*20, i*20));
                            break;
                        case 'G':
                           addSprite(Sprite.gremlinFactory(j*20, i*20, this));
                            break;
                        case ' ':
                            break;
                        default:
                            System.err.printf("Invalid tile %c in textfile. [%d][%d]", s.charAt(j), i, j);
                    }
                    ++hashIdx;
                }
                ++i;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.printf("File %s not found%n", levelFile.getName());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
