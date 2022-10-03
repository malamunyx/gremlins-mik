package gremlins;

import org.junit.jupiter.api.Test;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

public class AppClassTest {
    // For unit testing purposes, we test this instantiated App object.
    private App app = new App();

    /* Load Level testing */
    @Test
    public void LoadFirstLevelIsNotNull() {
        int level = 1;
        Level l1 = app.loadLevel(level);
        assertThat(l1).isNotNull().isInstanceOf(Level.class);
    }

    @Test
    public void LoadSecondLevelIsNotNull() {
        int level = 2;
        Level l2 = app.loadLevel(level);
        assertThat(l2).isNotNull().isInstanceOf(Level.class);
    }

    @Test
    public void LoadThirdLevelThrowsIndexOutOfBoundsException() {
        assertThatThrownBy(() -> {
            int level = 3;
            Level l1 = app.loadLevel(level);
        }).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Level number is beyond level range");
    }

    @Test
    public void LoadZeroLevelThrowsIndexOutOfBoundsException() {
        assertThatThrownBy(() -> {
            int level = 0;
            Level l1 = app.loadLevel(level);
        }).isInstanceOf(IndexOutOfBoundsException.class).hasMessage("Level number is beyond level range");
    }

    /* Load Current Player Testing */
    @Test
    public void LoadCurrentPlayerLevelOne() {
        Level l1 = app.loadLevel(1);
        assertThat(app.LoadCurrentPlayer(l1)).isNotNull().isInstanceOf(Player.class);
    }

    @Test
    public void LoadCurrentPlayerNullLevelThrowsNullPtrException() {
        assertThatThrownBy(() -> {
            Level ln = null;
            Player cp = app.LoadCurrentPlayer(ln);
        }).isInstanceOf(NullPointerException.class).hasMessage("Level cannot be null for Player retrieval");
    }

    /* Loading Lives testing */
    @Test
    public void LoadLivesFromJSONConfigIsThree() {
        File f = new File(app.configPath);
        assertThat(app.loadLives(f)).isEqualTo(3);
    }

    @Test
    public void LoadLivesFromNullThrowsNullPointerException() {
        File f = null;
        assertThatThrownBy(() -> {
            app.loadLives(f);
        }).isInstanceOf(NullPointerException.class).hasMessage("Config file parameter is null");
    }

    /* Get Max Level testing */
    @Test
    public void getMaxLevelFromJSONConfigIsTwo() {
        File f = new File(app.configPath);
        assertThat(app.LoadMaxLevel(f)).isEqualTo(2);
    }

    @Test
    public void getMaxLevelFromNullThrowsNullPointerException() {
        File f = null;
        assertThatThrownBy(() -> {
            app.LoadMaxLevel(f);
        }).isInstanceOf(NullPointerException.class).hasMessage("Config file parameter is null");
    }

    /* Get Image Testing */
    /*
     * GetImage fails to return valid PImage variables for appclass...
     *
     * ISSUE WITH TESTING GETIMAGE: loadImage() from Java Processing Library
     * In most cases, load all images in setup() to preload them at the start of the program.
     * Loading images inside draw() will reduce the speed of a program.
     * Images cannot be loaded outside setup() unless they're inside a function that's
     * called after setup() has already run.
     */

    @Test
    public void GetImageNullFilename() {
        assertThatThrownBy(() -> {
            app.getImage(null);
        }).isInstanceOf(NullPointerException.class).hasMessage("Null parameter is invalid");
    }

    /* Game Setup Testing */
    @Test
    public void StartGameSetup() {
        app.gameSetup();
        JSONObject conf = loadJSONObject(new File(app.configPath));
        assertThat(app.getLives()).isEqualTo(conf.getInt("lives"));
        assertThat(app.getMaxLevel()).isEqualTo(conf.getJSONArray("levels").size());
        assertThat(app.getLevel()).isEqualTo(1); // By default start level at 1.
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
}
