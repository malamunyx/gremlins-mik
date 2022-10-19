package gremlins;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

public class eGremlinTest {
//    @Test
//    public void GetIdxInListCharNotFoundThrowsIllegalStateException() {
//        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
//        Gremlin g = Sprite.gremlinFactory(0,0, l);
//
//    }

    /* GetDirNum testing */
    @Test
    public void GetDirNumDirIsNullPointer() {
        char c = '\0';
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(0, 0, l);
        assertThat(g.getDirNum()).isEqualTo(0);
    }

    /* Gremlin freezing and frozen testing */
    @Test
    public void GremlinFrozenTesting() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(0,0, l);
        g.freeze();
        assertThat(g.isFrozen()).isTrue();
    }

    @Test
    public void GremlinUnfreezeTesting() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(0,0, l);
        g.freeze();

        g.unfreeze();
        assertThat(g.isFrozen()).isFalse();
    }

    /* GreaterTenRadius testing */
    @Test
    public void PlayerGreaterTenRadius() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Player p = l.getPlayer();
        Gremlin g = Sprite.gremlinFactory(40,260, l);
        assertThat(g.greaterTenRadius(p)).isTrue();
        // 40, 40 + 20 * (10 + 1)
    }

    @Test
    public void PlayerNotGreaterTenRadius() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Player p = l.getPlayer();
        Gremlin g = Sprite.gremlinFactory(40,80, l);
        assertThat(g.greaterTenRadius(p)).isFalse();
        // 40, 40 + 40;
    }

    @Test
    public void TestGremlinResetGreaterTenRadius() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Player p = l.getPlayer();
        Gremlin g = Sprite.gremlinFactory(40,80, l);
        g.reset();
        assertThat(g.greaterTenRadius(p)).isTrue();
    }

    @Test
    public void TestGremlinResetBrickFullMap() {
        Level l = Level.generateLevel(new File("TestLevels/BrickFull.txt"), 3, 3);
        Player p = l.getPlayer();
        Gremlin g = Sprite.gremlinFactory(40,40, l);
        g.reset();
        assertThat(g.greaterTenRadius(p)).isTrue();
    }

    /* Reset and levelReset testing */
    @Test
    public void TestGremlinLevelResetOriginalLocation() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(40,80, l);
        g.freeze();
        g.levelReset();
        assertThat(g.xPx).isEqualTo(g.xOrigin);
        assertThat(g.yPx).isEqualTo(g.yOrigin);
        assertThat(g.isFrozen()).isFalse();
    }

    /* GetRandomDir testing */
    @Test
    public void TestGetRandomDirNoPossibleDir() {
        Level l = Level.generateLevel(new File("TestLevels/GremlinDirTester.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(20,20, l);
        assertThat(g.getRandomDir()).isEqualTo('\0');
    }

    @Test
    public void TestGetRandomDirLeftObstruction() {
        Level l = Level.generateLevel(new File("TestLevels/GremlinDirTester.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(60,40, l);
        g.setDir('L');
        assertThat(g.getRandomDir()).isNotEqualTo('R');
    }

    @Test
    public void TestGetRandomDirRightObstruction() {
        Level l = Level.generateLevel(new File("TestLevels/GremlinDirTester.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(80,20, l);
        g.setDir('R');
        assertThat(g.getRandomDir()).isNotEqualTo('L');
    }

    @Test
    public void TestGetRandomDirUpObstruction() {
        Level l = Level.generateLevel(new File("TestLevels/GremlinDirTester.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(80,20, l);
        g.setDir('U');
        assertThat(g.getRandomDir()).isNotEqualTo('D');
    }

    @Test
    public void TestGetRandomDirDownObstruction() {
        Level l = Level.generateLevel(new File("TestLevels/GremlinDirTester.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(80,620, l);
        g.setDir('D');
        assertThat(g.getRandomDir()).isNotEqualTo('U');
    }

    /* Collision Checker */
    @Test
    public void CollisionCheckerPlayerSamePositionReturnsTrue() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(40,40, l);
        assertThat(g.spriteCollision(l.getPlayer())).isTrue();
    }

    @Test
    public void CollisionCheckerPlayerDiffPositionReturnsFalse() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(40,80, l);
        assertThat(g.spriteCollision(l.getPlayer())).isFalse();
    }

    @Test
    public void CollisionCheckerFireballSamePositionReturnsTrue() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(40,40, l);
        Fireball f = Sprite.fireballFactory(40, 40, 'U', l);
        assertThat(g.spriteCollision(f)).isTrue();
    }

    @Test
    public void CollisionCheckerFireballDIffPositionReturnsFalse() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Gremlin g = Sprite.gremlinFactory(40,40, l);
        Fireball f = Sprite.fireballFactory(40, 80, 'U', l);
        assertThat(g.spriteCollision(f)).isFalse();
    }



}
