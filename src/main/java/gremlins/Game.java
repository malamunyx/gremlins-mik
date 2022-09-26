package gremlins;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;


public class Game {
    private Random rg = new Random();
    private Tile[][] tileMap;
    private double wizard_cooldown;
    private double gremlin_cooldown;
    private Player p;

    public ArrayList<Sprite> sprites = new ArrayList<>();

    public Game(File f, double wizard_cooldown, double gremlin_cooldown) {
        tileMap = loadMap(f);
        this.wizard_cooldown = wizard_cooldown;
        this.gremlin_cooldown = gremlin_cooldown;
    }

    public void draw(App a) { // Map: 36x33
        a.background(211);
        for (Tile[] ta : tileMap) {
            for (Tile t : ta) {
                if (t instanceof Wall) {
                    // If Brickwall and its in original state. (5 state is destroyed/no draw).
                    if ( ((Wall) t).canBreak() && ((Wall) t).getStatus() != 5) {
                        t.draw(a, a.brickwall[((Wall) t).getStatus()], t.x, t.y);
                    } else if (!((Wall) t).canBreak()) {
                        t.draw(a, a.stonewall, t.x, t.y);
                    }
                } else if (t instanceof Exit) {
                    t.draw(a, a.door, t.x, t.y);
                }
            }
        }

//        for (Sprite s : sprites) {
//            if (s instanceof Gremlin) {
//                ((Gremlin) s).update(a, a.gremlin);
//            } else if (s instanceof Fireball) {
//                ((Fireball) s).update(a, a.fireball);
//            }
//
//            // CHECK FOR COLLISIONS WITH PLAYERS, FIREBALLS ETC. SPRITES.
//        }
        updateSprites(a);

//        for (Projectile p : projectiles) {
//            if (p instanceof  Fireball) {
//                ((Fireball) p).update(a, a.fireball);
//            }
//        }

        p.update(a, a.wizard[p.getImgDir()]);

        if (p.pWinLevel()) {
            a.noLoop();
            a.background(0, 255, 0); // change this later.
        }

    }


    public Player getPlayer() {
        return this.p;
    }

    /**
     * Returns tileMap.
     * @return 2-dimensional tile array.
     */
    public Tile[][] getTileMap() {
        return this.tileMap;
    }

    /**
     * Returns tile object of a specific index in tileMap.
     * @param index Grid number reference: index = xPos + (yPos * gridWidth)
     * @return Tile object.
     */
    public Tile getTile(int index) {
        int i = index / 36;
        int j = index % 36;
        return tileMap[i][j];
    }

    /**
     * Reads text file input, returning a 2D tileMap array.
     * @param f Text file input.
     * @return 2D array of tile objects.
     */
    private Tile[][] loadMap(File f) {
        Tile[][] mt = new Tile[33][36];
        try {
            Scanner sc = new Scanner(f);
            Pattern splitter = Pattern.compile("");

            int i = 0;
            while (sc.hasNextLine()) {
                String[] in = splitter.split(sc.nextLine());

                for (int j = 0; j < in.length; ++j) {
                    switch (in[j]) {
                        case "X":
                            mt[i][j] = new Wall(j, i, false);
                            break;
                        case "B":
                            mt[i][j] = new Wall(j, i, true);
                            break;
                        case "W":
                            p = new Player( j*20, i*20, this);
//                            mt[i][j] = p; if we want to make Player extend tile...
                            //sprites.add(p); // every frame, check if each sprite interacts with eachother.
                            break;
                        case "E":
                            mt[i][j] = new Exit(j, i);
                            break;
                        case "G":
                            sprites.add(new Gremlin(j*20, i*20, this));
                            break;
                        case " ":
                            break;
                        default:
                            System.err.println("Invalid tile entry in text file " + i + " " + j);
                    }
                }
                ++i;
            }

            sc.close();
            splitter = null;

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.printStackTrace();
            System.exit(1);
        }
        return mt;
    }

    private void reload(File f) {
        /*
        Iterates through each tile and resets its "broken" boolean status
        into false. Player position is set again back to its original, alongside
        gremlin locations.
         */
    }

    public boolean checkWall(int idx) {
        if (getTile(idx) instanceof Wall) {
            return ((Wall) getTile(idx)).isBroken();
        } else {
            return true;
        }
    }

    public boolean checkBrickWall(int idx) {
        if (getTile(idx) instanceof Wall) {
            return ((Wall) getTile(idx)).canBreak();
        } else {
            return false;
        }
    }

    public void addSprite(Sprite p) {
        sprites.add(p);
    }

    public int getRandomInt(int n) {
        return rg.nextInt(n);
    }

    public void updateSprites(App a) {
        for (int i = sprites.size()-1; i >= 0; --i) {
            Sprite s = sprites.get(i);
            if (s instanceof Fireball) {
                if (((Fireball) s).collided()) {
                    ((Wall)getTile(((Fireball) s).getIndex())).incrementStatus();
                    ((Wall)getTile(((Fireball) s).getIndex())).breakWall();
                    sprites.remove(i);
                }
                ((Fireball) s).update(a, a.fireball);
            } else if (s instanceof Gremlin) {
                ((Gremlin) s).update(a, a.gremlin);
            }
        }
    }
}
