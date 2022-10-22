package gremlins;

import org.junit.jupiter.api.Test;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;

public class bLevelTest {
    // For Level unit testing purposes. Level index will be 0.
    App a = new App();
    private JSONObject conf = App.loadJSONObject(new File(a.configPath));
    private String fname = conf.getJSONArray("levels").getJSONObject(0).getString("layout");
    private double wz_cd = conf.getJSONArray("levels").getJSONObject(0).getDouble("wizard_cooldown");
    private double en_cd = conf.getJSONArray("levels").getJSONObject(0).getDouble("enemy_cooldown");

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
        p.setPosition(moveX, moveY);
        // LOADED LEVEL 1, PLAYER IN OPPOSITE CORNER
        assertThat(p.getIndex(p.getCentreX(), p.getCentreY())).isEqualTo(1147);
        assertThat(lt.playerWin()).isTrue();
    }

    /* Load Level Testing */
    @Test
    public void LevelLoadValidFileNoThrowable() {
        String configPath = "TestLevels/levelClassTestConfig.txt";
        Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
        // Test if Powerup is located.
        assertThat(lt.getTile(73))
                .isInstanceOf(Powerup.class);
    }

    @Test
    public void LoadInvalidHorizontalDimensionThrowsRuntimeException() {
        String configPath = "TestLevels/InvalidHorizontalDimension.txt";
        assertThatThrownBy(() -> {
            Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Map Specification violation: horizontal Dimensions of map must be 36 Tiles, not 37");
    }

    @Test
    public void LoadInvalidVerticalDimensionThrowsRuntimeException() {
        String configPath = "TestLevels/InvalidVerticalDimension.txt";
        assertThatThrownBy(() -> {
            Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Map Specification violation: vertical Dimensions of map must be 33 Tiles, not 34");
    }

    @Test
    public void LoadInvalidPlayerMissingThrowsRuntimeException() {
        String configPath = "TestLevels/InvalidPlayerMissing.txt";
        assertThatThrownBy(() -> {
            Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Player not detected in level text file layout");
    }

    @Test
    public void LoadFileNotFoundThrowsFileNotFoundException() {
        assertThatThrownBy(() -> {
            String configPath = "TestLevels/thisShouldNotExist.txt";
            Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("File thisShouldNotExist.txt not found");
    }

    @Test
    public void LoadFileInvalidTileContained() {
        String configPath = "TestLevels/InvalidTileContained.txt";
        Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
    }

    @Test
    public void LoadInvalidBorderBottomThrowsRuntimeException() {
        String configPath = "TestLevels/InvalidBorderBottom.txt";
        assertThatThrownBy(() -> {
            Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Map must be bordered by stonewall");
    }

    @Test
    public void LoadInvalidBorderSideThrowsRuntimeException() {
        String configPath = "TestLevels/InvalidBorderSide.txt";
        assertThatThrownBy(() -> {
            Level lt = Level.generateLevel(new File(configPath), wz_cd, en_cd);
        }).isInstanceOf(RuntimeException.class)
                .hasMessage("Map must be bordered by stonewall");
    }

    /* Reset Level Testing */
    @Test
    public void TestLevelReset() {
        Level lt = Level.generateLevel(new File("TestLevels/BrickFull.txt"), wz_cd, en_cd);
        Gremlin g = Sprite.gremlinFactory(40, 40, lt);
        Slime s = Sprite.slimeFactory(40, 40, 'R', lt);
        Wall bw = (Wall)lt.getTile(73);
        g.setDir('R');
        lt.addSprite(g);
        lt.addSprite(s);
        bw.breakWall();
        assertThat(bw.isBroken()).isTrue();

        lt.resetLevel();
        assertThat(g.xPx).isEqualTo(g.xOrigin);
        assertThat(g.yPx).isEqualTo(g.yOrigin);
        assertThat(g.getDirNum()).isEqualTo(0);
        assertThat(s.isNeutralised()).isTrue();
        assertThat(bw.isBroken()).isFalse();
    }

    /* Player-Powerup interaction testing */
    @Test
    public void playerReceivePowerupReturnsTrueUponOverlap() {
        Level lt = Level.generateLevel(new File("TestLevels/Empty.txt"), wz_cd, en_cd);
        Powerup p = (Powerup)lt.getTile(75);
        p.setCanEffect(true);
        lt.getPlayer().setPosition(60, 40);
        assertThat(lt.playerReceivePowerup()).isTrue();
    }

    @Test
    public void playerReceivePowerupReturnsFalseWhenTileCannotEffect() {
        Level lt = Level.generateLevel(new File("TestLevels/Empty.txt"), wz_cd, en_cd);
        Powerup p = (Powerup)lt.getTile(75);
        lt.getPlayer().setPosition(60, 40);
        assertThat(lt.playerReceivePowerup()).isFalse();
    }

    @Test
    public void playerReceivePowerupReturnsFalseWhenPLayerHasPowerup() {
        Level lt = Level.generateLevel(new File("TestLevels/Empty.txt"), wz_cd, en_cd);
        Powerup p = (Powerup)lt.getTile(75);
        p.setCanEffect(true);
        lt.getPlayer().setPowerupActive(true);

        lt.getPlayer().setPosition(60, 40);
        assertThat(lt.playerReceivePowerup()).isFalse();
    }

    @Test
    public void playerReceivePowerupReturnsFalseNoOverlap() {
        Level lt = Level.generateLevel(new File("TestLevels/Empty.txt"), wz_cd, en_cd);
        Powerup p = (Powerup)lt.getTile(75);
        assertThat(lt.playerReceivePowerup()).isFalse();
    }



    @Test
    public void playerPowerupEffectiveWhenUpdatePowerup() {
        Level lt = Level.generateLevel(new File("TestLevels/Empty.txt"), wz_cd, en_cd);
        Player p = lt.getPlayer();
        Powerup pu = (Powerup)lt.getTile(75);

        pu.setCanEffect(true);

        p.setPosition(60, 40);
        assertThat(lt.playerReceivePowerup()).isTrue();

        lt.updatePowerup();
        assertThat(lt.getPlayer().hasPowerup()).isTrue();
    }

}


/* Update Sprite Testing --> Maybe? Too hard? PApplet */
//    /* Reset Level Testing */


