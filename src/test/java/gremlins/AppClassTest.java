package gremlins;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

public class AppClassTest {
    // For unit testing purposes, we test this instantiated App object.

    /* Load Level testing */
    @Test
    public void LoadFirstLevelIsNotNull() {
        App app = new App();
        int level = 1;
        Level l1 = app.loadLevel(level);
        assertThat(l1).isNotNull().isInstanceOf(Level.class);
    }

    @Test
    public void LoadSecondLevelIsNotNull() {
        App app = new App();
        int level = 2;
        Level l2 = app.loadLevel(level);
        assertThat(l2).isNotNull().isInstanceOf(Level.class);
    }

    @Test
    public void LoadThirdLevelThrowsIndexOutOfBoundsException() {
        assertThatThrownBy(() -> {
            App app = new App();
            int level = 3;
            app.loadLevel(level);
        }).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Level number is beyond level range");
    }

    @Test
    public void LoadZeroLevelThrowsIndexOutOfBoundsException() {
        assertThatThrownBy(() -> {
            App app = new App();
            int level = 0;
            app.loadLevel(level);
        }).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Level number is beyond level range");
    }

    /* Load Current Player Testing */
    @Test
    public void LoadCurrentPlayerLevelOne() {
        App app = new App();
        Level l1 = app.loadLevel(1);
        assertThat(app.LoadCurrentPlayer(l1)).isNotNull().isInstanceOf(Player.class);
    }

    @Test
    public void LoadCurrentPlayerNullLevelThrowsNullPtrException() {
        assertThatThrownBy(() -> {
            App app = new App();
            app.LoadCurrentPlayer(null);
        }).isInstanceOf(NullPointerException.class).hasMessage("Level cannot be null for Player retrieval");
    }

    /* Loading Lives testing */
    @Test
    public void LoadLivesFromJSONConfigIsThree() {
        App app = new App();
        File f = new File(app.configPath);
        assertThat(app.loadLives(f)).isEqualTo(3);
    }

    @Test
    public void LoadLivesFromNullThrowsNullPointerException() {
        assertThatThrownBy(() -> {
            App app = new App();
            app.loadLives(null);
        }).isInstanceOf(NullPointerException.class).hasMessage("Config file parameter is null");
    }

    /* Set Game Level Testing */
    @Test
    public void SetGameLevelThreeThrowsIndexOutOfBoundsException() {
        App app = new App();
        app.gameSetup();

        assertThatThrownBy(() -> {
            app.setGameLevel(3);
        }).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Level parameter out of bounds");

        JSONObject conf = loadJSONObject(new File(app.configPath));
        assertThat(app.getMaxLevel()).isEqualTo(conf.getJSONArray("levels").size());
    }

    @Test
    public void SetGameLevelZeroThrowsIndexOutOfBoundsException() {
        App app = new App();
        app.gameSetup();

        assertThatThrownBy(() -> {
            app.setGameLevel(0);
        }).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Level parameter out of bounds");

        JSONObject conf = loadJSONObject(new File(app.configPath));
        assertThat(app.getMaxLevel()).isEqualTo(conf.getJSONArray("levels").size());
    }

    @Test
    public void SetGameLevelTwo() {
        App app = new App();
        app.gameSetup();
        app.setGameLevel(2);
        assertThat(app.getLevel()).isEqualTo(2);
    }


    /* Get Max Level testing */
    @Test
    public void getMaxLevelFromJSONConfigIsTwo() {
        App app = new App();
        File f = new File(app.configPath);
        assertThat(app.LoadMaxLevel(f)).isEqualTo(2);
    }

    @Test
    public void getMaxLevelFromNullThrowsNullPointerException() {
        assertThatThrownBy(() -> {
            App app = new App();
            app.LoadMaxLevel(null);
        }).isInstanceOf(NullPointerException.class).hasMessage("Config file parameter is null");
    }

    /* Get Image Testing */
    /*
     * GetImage fails to return valid PImage variables for app class...
     * GetImage returns Null, throws NullPointerException for loadImage(), which we catch...
     */

    @Test
    public void GetImageNullFilename() {
        assertThatThrownBy(() -> {
            App app = new App();
            app.getImage(null);
        }).isInstanceOf(NullPointerException.class).hasMessage("Null parameter is invalid");
    }

    @Test
    public void GetImageInvalidFilenameThrowsRunTimeException() {
        String fname = "Invalid.png";
        assertThatThrownBy(() -> {
            App app = new App();
            app.getImage(fname);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Could not locate resource file path for " + fname);
    }

    /* Game Setup Testing */
    @Test
    public void StartGameSetup() {
        App app = new App();
        app.gameSetup();
        JSONObject conf = loadJSONObject(new File(app.configPath));
        assertThat(app.getLives()).isEqualTo(conf.getInt("lives"));
        assertThat(app.getMaxLevel()).isEqualTo(conf.getJSONArray("levels").size());
        assertThat(app.getLevel()).isEqualTo(1); // By default, start level at 1.
    }

    /* Player Death Testing */
    @Test
    public void PlayerDeathDecrementsLives() {
        App na = new App();
        na.gameSetup();
        int originalLives = na.getLives();
        na.playerDeath();
        assertThat(na.getLives()).isEqualTo(originalLives - 1);
    }

    /* PApplet Setup Function Testing */

    /*
        Integration testing, asserts that the App.setup() function
        Does not throw exceptions, loads all images fine, and its loaded
        attributes are of expected value.
     */
    @Test
    public void AppSetupInitialises() {
        App na = new App();
        na.loop();
        PApplet.runSketch(new String[] { "App" }, na);
        na.setup();
        JSONObject conf = loadJSONObject(new File(na.configPath));
        assertThat(na.getLives()).isEqualTo(conf.getInt("lives"));
        assertThat(na.getMaxLevel()).isEqualTo(conf.getJSONArray("levels").size());
        assertThat(na.getLevel()).isEqualTo(1); // By default, start level at 1.
        na.exit();
    }

    /* End Game Testing [INTEGRATION TESTING] */
    @Test
    public void GameOverScreenWhenZeroLives() {
        App na = new App();


        na.loop();
        PApplet.runSketch(new String[] { "App" }, na);
        na.setup();
        na.delay(500);

        // Die 3x
        na.playerDeath();
        na.playerDeath();
        na.playerDeath();

        na.delay(1000);

        assertThat(na.getLives()).isEqualTo(0);
    }

    @Test
    public void GameWinScreenWhenAllLevelsCompleted() {
        int moveX = (1147 % 36) * 20;
        int moveY = (1147 / 36) * 20;
        App na = new App();

        na.loop();
        PApplet.runSketch(new String[] { "App" }, na);
        na.setup();
        na.setGameLevel(2); // SET TO LEVEL 2.
        na.delay(800);

        na.getCurrentLevel().getPlayer().SetPosition(moveX, moveY);

        na.delay(1000);
        assertThat(na.getLevel()).isEqualTo(3);
    }

    /* Level Up */
    @Test
    public void LevelUp() {
        int moveX = (1147 % 36) * 20;
        int moveY = (1147 / 36) * 20;
        App na = new App();

        na.loop();
        PApplet.runSketch(new String[] { "App" }, na);
        na.setup();
        na.delay(500);

        na.getCurrentLevel().getPlayer().SetPosition(moveX, moveY);
        assertThat(na.getCurrentLevel().playerWin()).isTrue();

        na.delay(1000);
        assertThat(na.getLevel()).isEqualTo(2);
    }
}
