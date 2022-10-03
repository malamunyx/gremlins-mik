package gremlins;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Level {
    private Random rg = new Random();
    int wizardCooldown;
    int enemyCooldown;
    private Player player;

    private HashMap<Integer, Tile> tileMap = new HashMap<>();
    private ArrayList<Sprite> sprites = new ArrayList<>();

    public static Level generateLevel(File currentLevel, double wizardCooldown, double enemyCooldown) {
        return new Level(currentLevel, wizardCooldown, enemyCooldown);
    }

    public Level(File currentLevel, double wizardCooldown, double enemyCooldown) {
        this.wizardCooldown = (int)Math.ceil(wizardCooldown * App.FPS);
        this.enemyCooldown = (int)(enemyCooldown * App.FPS);
        loadLevel(currentLevel);
    }

    /**
     * Calls the app to draw the game level and all its processes of the current level game.
     * @param a App that extends Processing Applet handling all Processing library processes.
     */
    public void draw(App a) {
        a.background(211);
        updateMap(a);
        updateSprites(a);
    }

    /**
     * Calls the app to draw the map tile layout.
     * @param a App that extends Processing Applet handling all Processing library processes.
     */
    public void updateMap(App a) {
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
    }

    /**
     * Returns the Tile object of given map index.
     * @param idx Location index of tile location.
     * @return Tile object in position with associated map index.
     */
    public Tile getTile(int idx) { // EXPLAIN HOW MAP INDEXING WORKS IN DOC
        return this.tileMap.get(idx);
    }

    /**
     * Determines if location is accessible by sprites.
     * @param idx Location index of tile location.
     * @return Boolean value representing tile accessibility.
     */
    public boolean canWalk(int idx) {
        if (tileMap.containsKey(idx) && getTile(idx) instanceof Wall)
            return (((Wall) getTile(idx)).isBroken());
        else
            return true;
    }

    /**
     * Returns Player object associated with the current Game level.
     * @return Player object
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Adds sprite into the Sprite ArrayList.
     * @param s Objects that implement Sprite interface.
     */
    public void addSprite(Sprite s) {
        if (s != null)
            sprites.add(s);
    }

    /**
     * Returns random integer between in range [0, n), utilised mainly for Gremlin directionality.
     * @param n Integer upper bound (Not inclusive).
     * @return Randomised integer.
     */
    public int getRandomInt(int n) {
        return rg.nextInt(n);
    }

    /**
     * Calls the app to draw all sprites. Simultaneously handle sprite collision, and remove any neutralised projectile sprites.
     * @param a App that extends Processing Applet handling all Processing library processes.
     */
    public void updateSprites(App a) {
        // Draw or update sprites.
        for (int i = sprites.size()-1; i >= 0; --i) {
            Sprite s1 = sprites.get(i);

            if (s1 instanceof Gremlin) {
                s1.update(a, a.gremlin);
                if (a.frameCount % (enemyCooldown) == 0)
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
                    if (s1 instanceof Player || s2 instanceof Player) {
                        // calls levelReset, and decrease lives by 1.
                        resetLevel();
                        a.playerDeath();
                    } else {
                        s1.reset();
                        s2.reset();
                    }

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

    /**
     * Returns boolean to determine whether the player object has beat the level.
     * @return Boolean variable whether Player object touched the exit door.
     */
    public boolean playerWin() { // location equivalency
        return getTile(player.getIndex(player.getCentreX(), player.getCentreY())) instanceof Exit;
    }

    /**
     * Reads the level file determined by the JSON config file, creating the Tile and Sprite (Player, Gremlin) objects.
     * @param levelFile Text file containing the level layout.
     * @throws RuntimeException Any violation to map specifications: 36x33, Map bordered by stonewall.
     */
    public void loadLevel(File levelFile) throws RuntimeException {
        try {
            Scanner sc = new Scanner(levelFile);

            int hashIdx = 0;
            int i = 0;
            while (sc.hasNextLine()) {
                String s = sc.nextLine();

                if (s.length() != 36)
                    throw new RuntimeException(String.format("Map Specification violation: vertical Dimensions of map must be 36 Tiles, not %d", s.length()));

                for (int j = 0; j < s.length(); ++j) {

                    if (i == 0 || i == 32) {
                        if (s.charAt(j) != 'X')
                            throw new RuntimeException("Map must be bordered by stonewall");
                    } else if (j == 0 || j == 35) {
                        if (s.charAt(j) != 'X')
                            throw new RuntimeException("Map must be bordered by stonewall");
                    }

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

            if (i > 33)
                throw new RuntimeException("Map Specification violation: vertical Dimensions of map must be 33 Tiles, not " + i);

            if (player == null)
                throw new RuntimeException("Player not detected in level text file layout");

            sc.close();
        } catch (FileNotFoundException e) {
            System.err.printf("File %s not found%n", levelFile.getName());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Getter for sprites array list.
     * @return level sprites arraylist.
     */
    public ArrayList<Sprite> getSprites() {
        return this.sprites;
    }

    /**
     * Resets the level to its original, initialised status, undoing any changes by Player and resetting sprite positions.
     */
    public void resetLevel() {
        for (Tile t : tileMap.values()) {
            if (t instanceof Wall && ((Wall) t).isBroken()) {
                ((Wall) t).unbreakWall();
            }
        }
        for (Sprite s : sprites) { // neutralises Projectiles and resets gremlins to origin.
            if (s instanceof Gremlin)
                ((Gremlin) s).levelReset();
            else
                s.reset();
        }
    }
}
