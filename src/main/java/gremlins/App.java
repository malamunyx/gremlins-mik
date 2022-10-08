package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.*;


public class App extends PApplet {
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int POWERUPTIME = 10;

    public static final int FPS = 60;

    public String configPath;

    // Images
    public PImage[] brickwall = new PImage[5];
    public PImage stonewall;
    public PImage gremlin;
    public PImage slime;

    public PImage[] wizard = new PImage[4];
    public PImage fireball;
    public PImage speedboots;
    public PImage door;
    public PImage podium;

    private int lives;
    private int level;
    private int maxLevel;
    private Level currentLevel;
    private Player currentPlayer;

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
    @Override
    public void setup() {
        frameRate(FPS);

        // Load images during setup
        this.door = getImage("door.png");
        this.podium = getImage("podium.png");
        this.stonewall = getImage("stonewall.png");
        this.brickwall[0] = getImage("brickwall.png");
        this.brickwall[1] = getImage("brickwall_destroyed0.png");
        this.brickwall[2] = getImage("brickwall_destroyed1.png");
        this.brickwall[3] = getImage("brickwall_destroyed2.png");
        this.brickwall[4] = getImage("brickwall_destroyed3.png");

        this.gremlin = getImage("gremlin.png");
        this.slime = getImage("slime.png");

        this.wizard[0] = getImage("wizard0.png");
        this.wizard[1] = getImage("wizard1.png");
        this.wizard[2] = getImage("wizard2.png");
        this.wizard[3] = getImage("wizard3.png");
        this.fireball = getImage("fireball.png");

        this.speedboots = getImage("speedboots.png");

        // Always start at first level, hence first index of JSONArray.
        gameSetup();
    }

    /**
     * Sets app settings and variables to initial game start settings (As identified by config.json).
     * Level always start at initial index of parsed JSON array.
     * Lives are determined by config.json file.
     * sets currentGame to the determined level and its related player.
     */
    public void gameSetup() { // maybe turn into one big readJSON method.....
        File config = new File(configPath);

        level = 1;
        maxLevel = LoadMaxLevel(config);
        lives = loadLives(config);
        currentLevel = loadLevel(level);
        currentPlayer = LoadCurrentPlayer(currentLevel);
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(){
        if (!looping) { // restart game.
            gameSetup();
            loop();
        }

        if (keyCode == UP)
            currentPlayer.up();
        else if (keyCode == DOWN)
            currentPlayer.down();
        else if (keyCode == LEFT)
            currentPlayer.left();
        else if (keyCode == RIGHT)
            currentPlayer.right();

        if (keyCode == 32) {
            currentPlayer.fire();
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased(){
        if (keyCode == UP)
            currentPlayer.upStop();
        else if (keyCode == DOWN)
            currentPlayer.downStop();
        else if (keyCode == LEFT)
            currentPlayer.leftStop();
        else if (keyCode == RIGHT)
            currentPlayer.rightStop();
    }


    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        fill(0);
        textAlign(CENTER, CENTER);

        if (level > maxLevel) { // Condition for Game win
                noLoop();
                endGameScreen("YOU WIN", 0, 255, 0);
        }
        else if (lives <= 0){ // Condition for Game lost.
            noLoop();
            endGameScreen("YOU LOSE", 255, 0, 0);
        }
        else { // Normal conditions
            currentLevel.draw(this);
            textSize(12);
            text((int)frameRate, 25, 10);

            textSize(18);
            text("Lives: ", 90, 685);

            text(String.format("Level %d/%d", level, maxLevel), (float)WIDTH/2, 685);

            for (int i = 0; i < lives; ++i) {
                image(wizard[0], 120 + i * 25, 680);
            }

            if (currentLevel.playerWin()) {
                ++level;
                if (level <= maxLevel) { // Level up
                    setGameLevel(level);
                }
            }
        }
    }

    /**
     * Displays a screen with desired text and RGB values for background colour.
     * @param text String for end game message to be displayed.
     * @param r RGB value for red.
     * @param g RGB value for green.
     * @param b RGB value for blue.
     */
    public void endGameScreen(String text, int r, int g, int b) {
        background(r, g, b);
        textSize(50);
        text(text, (float)WIDTH/2, (float)HEIGHT/2);
    }

    /**
     * Returns Game object of the associated level.
     * @param level Integer level number to be parsed into JSON array (level is decremented for Array indexing purposes).
     * @return Game object representing the associated level.
     * @throws IndexOutOfBoundsException Indication that level index is out of bounds.
     */
    public Level loadLevel(int level) throws IndexOutOfBoundsException {
        --level; // For indexing purposes.

        //JSON Handling
        JSONObject conf = loadJSONObject(new File(this.configPath));
        JSONArray levels = conf.getJSONArray("levels");

        if (level >= levels.size() || level < 0)
            throw new IndexOutOfBoundsException("Level number is beyond level range");

        File currentLevel = new File(levels.getJSONObject(level).getString("layout"));
        double wizardCooldown = levels.getJSONObject(level).getDouble("wizard_cooldown");
        double enemyCooldown = levels.getJSONObject(level).getDouble("enemy_cooldown");

        return Level.generateLevel(currentLevel, wizardCooldown, enemyCooldown);
    }

    /**
     * Returns the Player object for its related game (level).
     * @param currentLevel the current (level) Game class that is loaded.
     * @return Player object of the associated level Game class that is loaded by the level text file.
     */
    public Player LoadCurrentPlayer(Level currentLevel) {
        if (currentLevel == null) {
            throw new NullPointerException("Level cannot be null for Player retrieval");
        }
        return currentLevel.getPlayer();
    }

    /**Decrements the current player's life count. */
    public void playerDeath() {
        --lives;
    }

    /**
     * Returns the number of lives given to the player when the game is initialised.
     * @param f JSON config file.
     * @return Integer representing the number of lives player gets when game is started.
     * @throws NullPointerException Indication that config File object reference is null.
     */
    public int loadLives(File f) throws NullPointerException {
        if (f == null)
            throw new NullPointerException("Config file parameter is null");

        JSONObject conf = loadJSONObject(new File(this.configPath));
        return conf.getInt("lives");
    }

    /**
     * Returns the size of levels JSON array, with its size representing the final level.
     * @return Integer representing final level, and the number of levels.
     * @param f JSON config file.
     * @throws NullPointerException Indication that config File object reference is null.
     */
    public int LoadMaxLevel(File f) throws NullPointerException {
        if (f == null)
            throw new NullPointerException("Config file parameter is null");

        JSONObject conf = loadJSONObject(new File(this.configPath));
        JSONArray levels = conf.getJSONArray("levels");
        return levels.size();
    }

    /**
     * Returns Processing PImage variable of the associated image resource.
     * @param filename Image filename in the resources folder.
     * @return PImage variable representing images for the Processing library to handle.
     * @throws RuntimeException When image file is unable to be located in resources folder.
     * @throws NullPointerException When file parameter is null.
     */
    public PImage getImage(String filename) throws RuntimeException, NullPointerException {
        if (filename == null) {
            throw new NullPointerException("Null parameter is invalid");
        }
        try {
            return loadImage(URLDecoder.decode(this.getClass().getResource(filename).getPath(), StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException | NullPointerException e) {
            throw new RuntimeException("Could not locate resource file path for " + filename);
        }
    }

    /**
     * Getter method for amount of lives
     * @return Number of lives available.
     */
    public int getLives() {
        return lives;
    }
    /**
     * Getter method for current Level
     * @return Current Level
     */
    public int getLevel() {
        return level;
    }
    /**
     * Getter method for max Level
     * @return Number of levels, maximum level.
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Getter method for currentLevel object.
     * @return Reference to Level object associated with the current level number.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets level to desired level, changing currentLevel and currentPlayer objects.
     * @param level Level number.
     */
    public void setGameLevel(int level) {
        if (level > this.getMaxLevel() || level <= 0)
            throw new IndexOutOfBoundsException("Level parameter out of bounds");

        this.level = level;
        this.currentLevel = loadLevel(level);
        this.currentPlayer = LoadCurrentPlayer(currentLevel);
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
