package gremlins;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Game {
    private Tile[][] tileMap;
    private File levelFile;
    private Player p;
    public Game(File f) {
        tileMap = loadMap(f);
        levelFile = f;
    }

    public void draw(App a) { // Map: 36x33
        a.background(211);
        for (int i = 0; i < 33; ++i) {
            for (Tile t : tileMap[i]) {
                if (t instanceof Wall) {
                    if ( ((Wall) t).canBreak() ) {
                        t.draw(a, a.brickwall, t.x, t.y);
                    } else {
                        t.draw(a, a.stonewall, t.x, t.y);
                    }
//                } else if (t instanceof Player) {
//                    t.draw(a, a.wizard[0], t.x, t.y);
                }
            }
        }

        p.draw(a, this, a.wizard[0]);

//        if (p.lose()) {
//            tileMap = reload(levelFile);
//        }
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

    public void UpdateTile(int i, int j, int iNew, int jNew) {

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
//                            mt[i][j] = p;
                            break;
                        case "E":
                            break;
                        case "G":
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
        System.gc();
        return mt;
    }

    private Tile[][] reload(File f) {
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
//                            p.xSetPos(j);
//                            p.ySetPos(i);
                            break;
                        case "E":
                            break;
                        case "G":
                            //SEt gremlin position back to start;
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

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.printStackTrace();
            System.exit(1);
        }
        System.gc();
        return mt;
    }
}
