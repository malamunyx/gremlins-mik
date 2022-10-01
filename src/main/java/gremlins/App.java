package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.io.*;


public class App extends PApplet {
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;

    public static final int FPS = 60;

    public String configPath;

    // Images
    public PImage[] brickwall = new PImage[5];
    public PImage stonewall;
    public PImage gremlin;
    public PImage slime;

    public PImage[] wizard = new PImage[4];
    public PImage fireball;
    public PImage door;

    private int lives;
    private int level;
    private int maxLevel;
    private Game currentGame;
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

        // Always start at first level, hence first index of JSONArray.
        gameSetup();
    }

    public void playerDeath() {
        --lives;
    }

    private void gameSetup() { // maybe turn into one big readJSON method.....
        File config = new File(configPath);

        level = 0;
        maxLevel = getMaxLevel(config);
        lives = loadLives(config);
        currentGame = loadGame(level);
        currentPlayer = currentGame.getPlayer();
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
        if (keyCode == UP || keyCode == DOWN)
            currentPlayer.yStop();
        else if (keyCode == LEFT || keyCode == RIGHT)
            currentPlayer.xStop();
    }


    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        fill(0);
        textAlign(CENTER, CENTER);

        if (lives > 0) {
            currentGame.draw(this);
            textSize(12);
            text((int)frameRate, 25, 10);

            textSize(18);
            text("Lives: ", 90, 685);

            text(String.format("Level %d/%d", level+1, maxLevel), (float)WIDTH/2, 685);

            for (int i = 0; i < lives; ++i) {
                image(wizard[0], 120 + i * 25, 680);
            }

            if (currentGame.playerWin()) {
                if (++level < maxLevel) { // Level up
                    currentGame = loadGame(level);
                    currentPlayer = currentGame.getPlayer();
                } else { // Game win
                    noLoop();
                    endGameScreen("YOU WIN", 0, 255, 0);
                }
            }
        } else {
            noLoop();
            endGameScreen("YOU LOSE", 255, 0, 0);
        }
    }

    private void endGameScreen(String text, int r, int g, int b) {
        background(r, g, b);
        textSize(50);
        text(text, (float)WIDTH/2, (float)HEIGHT/2);
    }

    private Game loadGame(int level) {
        //JSON Handling
        JSONObject conf = loadJSONObject(new File(this.configPath));
        JSONArray levels = conf.getJSONArray("levels");

        File currentLevel = new File(levels.getJSONObject(level).getString("layout"));
        double wizardCooldown = levels.getJSONObject(level).getDouble("wizard_cooldown");
        double enemyCooldown = levels.getJSONObject(level).getDouble("enemy_cooldown");

        return Game.generateGame(currentLevel, wizardCooldown, enemyCooldown);

    }

    private Player getCurrentPlayer(Game currentGame) {
        return currentGame.getPlayer();
    }


    private int loadLives(File f) {
        JSONObject conf = loadJSONObject(new File(this.configPath));
        return conf.getInt("lives");
    }
    private int getMaxLevel(File f) {
        JSONObject conf = loadJSONObject(new File(this.configPath));
        JSONArray levels = conf.getJSONArray("levels");
        return levels.size();
    }

    private PImage getImage(String filename) {
        try {
            return loadImage(URLDecoder.decode(this.getClass().getResource(filename).getPath(), StandardCharsets.UTF_8));
        } catch (NullPointerException e) {
            System.err.printf("Could not locate resource file path for %s\n", filename);
            return null;
        }
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
