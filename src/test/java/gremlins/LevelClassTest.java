package gremlins;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import processing.data.JSONObject;

import java.io.File;

public class LevelClassTest {
    // For unit testing purposes
    private App a = new App();
    private int levelIdx = 0;
    private JSONObject conf = App.loadJSONObject(new File(a.configPath));
    private String fname = conf.getJSONArray("levels").getJSONObject(levelIdx).getString("layout");
    private double wz_cd = conf.getJSONArray("levels").getJSONObject(levelIdx).getDouble("wizard_cooldown");
    private double en_cd = conf.getJSONArray("levels").getJSONObject(levelIdx).getDouble("enemy_cooldown");


    /* Level Factory testing */
    @Test
    public void GenerateLevelReturnsObject() {
        assertThat(Level.generateLevel(new File(fname), wz_cd, en_cd))
                .isInstanceOf(Level.class)
                .isNotNull();
    }

    /* Get Tile testing */
    @Test
    public void getTileStoneWall() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.getTile(0))
                .isNotNull()
                .isInstanceOf(Wall.class);
        assertThat(((Wall) lt.getTile(0))
                .canBreak())
                .isFalse();
    }

    @Test
    public void getTileBrickWall() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.getTile(40))
                .isNotNull()
                .isInstanceOf(Wall.class);
        assertThat(((Wall) lt.getTile(40))
                .canBreak())
                .isTrue();
    }

    @Test
    public void getTileExitIndex() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.getTile(1147))
                .isNotNull()
                .isInstanceOf(Exit.class);
    }

    @Test
    public void getEmptyTileIsNull() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.getTile(39))
                .isNull();
    }

    /* Can Walk Tile Testing */
    @Test
    public void canWalkEmptyTileIsTrue() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.canWalk(37))
                .isTrue();
    }

    @Test
    public void canWalkUnbrokenBrickWallIsFalse() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.canWalk(40))
                .isFalse();
    }

    @Test
    public void canWalkBrokenBrickWallIsTrue() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        ((Wall) lt.getTile(40)).breakWall();
        assertThat(lt.canWalk(40))
                .isTrue();
    }

    @Test
    public void canWalkStoneWallIsFalse() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.canWalk(0))
                .isFalse();
    }

    @Test
    public void canWalkExitTileIsTrue() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.canWalk(1147))
                .isTrue();
    }

    /* Get Player Testing */
    @Test
    public void GetPlayerReturnsPlayerObject() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        assertThat(lt.getPlayer())
                .isNotNull()
                .isInstanceOf(Player.class);

    }

    /* Add Sprite Testing */
    @Test
    public void AddSpriteRealObjectIncrementsSize() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        Sprite slimeTest = new Slime(20, 20, 'D', lt);
        int OriginalSpriteArraySize = lt.getSprites().size();
        lt.addSprite(slimeTest);
        assertThat(lt.getSprites().size()).isEqualTo(OriginalSpriteArraySize + 1);
    }

    @Test
    public void AddSpriteNullObjectKeepsOriginalSize() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        Sprite slimeTest = null;
        int OriginalSpriteArraySize = lt.getSprites().size();
        lt.addSprite(slimeTest);
        assertThat(lt.getSprites().size()).isEqualTo(OriginalSpriteArraySize);
    }

    /* Random Int Generator Testing */
    @Test
    public void GetRandomNumberIsWithinBounds() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        int bound = 5;
        assertThat(Level.rg.nextInt(bound)).isGreaterThanOrEqualTo(0).isLessThan(bound);
    }

    /* Player Win Testing */
    @Test
    public void PlayerNotInExitTile() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        Player p = lt.getPlayer();
        // LOADED LEVEL 1, PLAYER IN OPPOSITE CORNER
        assertThat(p.getIndex(p.getCentreX(), p.getCentreY())).isNotEqualTo(1147);
        assertThat(lt.playerWin()).isFalse();
    }

    @Test
    public void PlayerInExitTile() {
        Level lt = Level.generateLevel(new File(fname), wz_cd, en_cd);
        Player p = lt.getPlayer();
        int moveX = (1147 % 36) * 20;
        int moveY = (1147 / 36) * 20;
        p.SetPosition(moveX, moveY);
        // LOADED LEVEL 1, PLAYER IN OPPOSITE CORNER
        assertThat(p.getIndex(p.getCentreX(), p.getCentreY())).isEqualTo(1147);
        assertThat(lt.playerWin()).isTrue();
    }

    /* Update Sprite Testing --> Maybe? Too hard? */

    /* Load Level Testing */

    /* Reset Level Testing */

}
