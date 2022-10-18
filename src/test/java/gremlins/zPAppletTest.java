package gremlins;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

public class zPAppletTest {
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
        na.exit();
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

        na.delay(1000);
        assertThat(na.getLevel()).isEqualTo(2);
    }

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

//    @Test
    
//    public void
}
