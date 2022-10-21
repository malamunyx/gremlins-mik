package gremlins;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

public class gPAppletTest {
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
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        JSONObject conf = loadJSONObject(new File(na.configPath));
        assertThat(na.getLives()).isEqualTo(conf.getInt("lives"));
        assertThat(na.getMaxLevel()).isEqualTo(conf.getJSONArray("levels").size());
        assertThat(na.getLevel()).isEqualTo(1); // By default, start level at 1.
    }

    /* End Game Testing [INTEGRATION TESTING] */
    @Test
    public void GameOverScreenWhenZeroLives() {
        App na = new App();


        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(500);

        // Die 3x
        na.playerDeath();
        na.playerDeath();
        na.playerDeath();

        na.delay(1000);

        assertThat(na.getLives()).isEqualTo(0);
    }

    // Pretty unstable test...
    @Test
    public void GameWinScreenWhenAllLevelsCompleted() {
        int moveX = (1147 % 36) * 20;
        int moveY = (1147 / 36) * 20;
        App na = new App();
        na.configPath = "TestConfig/singleLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(500);
        na.getCurrentLevel().getPlayer().SetPosition(moveX, moveY);

        na.delay(1000);
        assertThat(na.getLevel()).isEqualTo(na.getMaxLevel() + 1);
    }

    /* Level Up */
    @Test
    public void LevelUp() {
        int moveX = (1147 % 36) * 20;
        int moveY = (1147 / 36) * 20;
        App na = new App();

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(500);

        na.getCurrentLevel().getPlayer().SetPosition(moveX, moveY);
        assertThat(na.getCurrentLevel().playerWin()).isTrue();

        na.delay(500);
        assertThat(na.getLevel()).isEqualTo(2);
    }

    /* Firing Iceball */
    @Test
    public void EmptyMapIceballUp() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.getCurrentLevel().getPlayer().setDir('U');
        na.getCurrentLevel().getPlayer().fireIceball();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Iceball.class);

        na.delay(300);
    }

    @Test
    public void EmptyMapIceballDown() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.getCurrentLevel().getPlayer().setDir('D');
        na.getCurrentLevel().getPlayer().fireIceball();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Iceball.class);

        na.delay(300);
    }

    @Test
    public void EmptyMapIceballLeft() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.getCurrentLevel().getPlayer().setDir('L');
        na.getCurrentLevel().getPlayer().fireIceball();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Iceball.class);

        na.delay(300);
    }

    @Test
    public void EmptyMapIceballRight() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.getCurrentLevel().getPlayer().setDir('R');
        na.getCurrentLevel().getPlayer().fireIceball();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Iceball.class);

        na.delay(300);
    }

    /* Firing Fireball */
    @Test
    public void EmptyMapFireballUp() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.getCurrentLevel().getPlayer().SetPosition(40, 180);
        na.keyCode = PApplet.UP;
        na.keyPressed();
        na.delay(100);
        na.keyReleased();
        na.getCurrentLevel().getPlayer().fire();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Fireball.class);
        na.delay(300);
    }

    @Test
    public void EmptyMapFireballDown() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.keyCode = PApplet.DOWN;
        na.keyPressed();
        na.delay(100);
        na.keyReleased();
        na.getCurrentLevel().getPlayer().fire();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Fireball.class);

        na.delay(300);
    }

    @Test
    public void EmptyMapFireballLeft() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.getCurrentLevel().getPlayer().SetPosition(180, 40);
        na.keyCode = PApplet.LEFT;
        na.keyPressed();
        na.delay(100);
        na.keyReleased();
        na.getCurrentLevel().getPlayer().fire();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Fireball.class);

        na.delay(300);
    }

    @Test
    public void EmptyMapFireballRight() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.keyCode = PApplet.RIGHT;
        na.keyPressed();
        na.delay(100);
        na.keyReleased();
        na.getCurrentLevel().getPlayer().fire();
        assertThat(na.getCurrentLevel().getSprites()).hasAtLeastOneElementOfType(Fireball.class);

        na.delay(300);
    }

    /* Powerup integration sketch testing */
    @Test
    public void EmptyMapTestPowerup() {
        App na = new App();
        na.configPath = "TestConfig/EmptyTestLevelConfig.json";

        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();
        na.delay(200);
        na.getCurrentLevel().addSprite(Sprite.fireballFactory(100, 100, 'D', na.getCurrentLevel()));
        na.getCurrentLevel().addSprite(Sprite.slimeFactory(100, 100, 'D', na.getCurrentLevel()));
        na.getCurrentLevel().addSprite(Sprite.iceballFactory(200, 200, 'D', na.getCurrentLevel()));
        na.getCurrentLevel().addSprite(Sprite.gremlinFactory(200, 200, na.getCurrentLevel()));
        na.getCurrentLevel().getPlayer().SetPosition(60, 40);
        ((Powerup)na.getCurrentLevel().getTile(75)).setCanEffect(true);
        na.delay(300);
        assertThat(na.getCurrentLevel().getPlayer().getPlayerSpeed()).isEqualTo(4);
    }

    @Test
    public void playerShootBrickwall() {
        App na = new App();
        na.loop();
        PApplet.runSketch(new String[]{"App"}, na);
        na.setup();

        na.delay(300);
        na.getCurrentLevel().getPlayer().setDir('R');
        na.getCurrentLevel().getPlayer().fire();
        assertThat(na.getCurrentLevel().getTile(40)).isInstanceOf(Wall.class);
        na.delay(700);
        assertThat(((Wall)na.getCurrentLevel().getTile(40)).isBroken()).isTrue();
    }


}
