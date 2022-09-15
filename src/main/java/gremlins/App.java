package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.event.KeyEvent;

import java.util.*; // Random, HashSet
//import java.util.Random;
import java.io.*;


public class App extends PApplet {

    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;

    public static final int FPS = 60;

    public static final Random randomGenerator = new Random();

    public String configPath;
    
    public PImage brickwall;
    public PImage stonewall;
    public PImage wizard;

    /*
     * HashSet of tile objects.
     * If brickwall collides with fireball
     *  -> Delete from mapTile, free object (Destroy object, free memory).
     *  (ASK: does a removal from hashset clear mem?)
     */
    private final HashSet<Tile> mapTiles = new HashSet<>();

    private Player p;

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
        this.stonewall = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        this.brickwall = loadImage(this.getClass().getResource("brickwall.png").getPath().replace("%20", ""));
        //this.gremlin = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        //this.slime = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        //this.fireball = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        this.wizard = loadImage(this.getClass().getResource("wizard0.png").getPath().replace("%20", ""));

        // JSON Handling
        JSONObject conf = loadJSONObject(new File(this.configPath));
        JSONArray Levels = conf.getJSONArray("levels"); // Level array.
        // get file
        File currentLevel = new File(Levels.getJSONObject(0).getString("layout"));
        loadLevel(currentLevel);
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if (key == 37)
            p.left();
        else if (key == 38)
            p.up();
        else if (key == 39)
            p.right();
        else if (key == 40)
            p.down();
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if (key == 37 || key == 39)
            p.xStop();
        else if (key == 38 || key == 40)
            p.yStop();
    }


    /**
     * Draw all elements in the game by current frame. 
	 */
    public void draw() {
        background(211); // clears the screen each frame
        for (Tile t : mapTiles) {
//            if (t instanceof Exit) {// If it is a door.
//                // image for door.
            if (t instanceof Wall) {
                if ( ((Wall) t).isBreakable() )
                    image(brickwall, t.x, t.y);
                else
                    image(stonewall, t.x, t.y);
            }
        }

        p.draw(this);
    }

    /*
     * private function to LOAD THE FILE
     * create the player object (With the position)
     */
    private void loadLevel(File f) {
        // if map previously loaded, clear/turn null call garbage collection.
        if (!mapTiles.isEmpty()){
            mapTiles.clear();
//            System.gc(); // clear the data. <---- GARBAGE COLLECTOR
        }

        try {
            Scanner sc = new Scanner(f);

            int posY = 0; // y-pixel variable
            while (sc.hasNextLine()) {
                String[] tileRow = sc.nextLine().split("");

                if (tileRow.length > 36 || posY > 33 * 20) {
                    System.err.println("Invalid file format");
                    System.exit(1);
                }

                for (int i = 0; i < tileRow.length; ++i) {
                    switch (tileRow[i]) {
                        case "X": // Stone wall
                            mapTiles.add(new Wall(i*20, posY, false));
                            break;
                        case "B": // brick wall
                            mapTiles.add(new Wall(i*20, posY, true));
                            break;
                        case "E": // Exit door
                            //
                            break;
                        case "W": // Start/Wizard (maybe return positions);
                            p = new Player(wizard, i*20, posY);
                            break;
                        case " ":
                            break;
                    }
                }

                posY += 20;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("No file found");
            System.exit(1);
        }
        System.gc(); // clear the data. <---- GARBAGE COLLECTOR
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
