package gremlins;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

public class dProjectileTest {

    @Test
    public void InvalidDirThrowsIllegalArgumentException() {
        char dir = 'Q';
        assertThatThrownBy(() -> {
            Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
            Fireball fb = Sprite.fireballFactory(0, 0, dir, l);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("Invalid char %c for projectile direction", dir));
    }

    @Test
    public void ProjectileResetNeutralisesAndStops() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'U', l);
        fb.reset();
        assertThat(fb.xPx).isEqualTo(0);
        assertThat(fb.yPx).isEqualTo(0);
        assertThat(fb.neutralised).isTrue();
    }

    /* Slime Projectile Collision Testing */
    @Test
    public void SlimeCollideFireball() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'R', l);
        Slime s = Sprite.slimeFactory(19, 0, 'L', l);
        assertThat(s.spriteCollision(fb)).isTrue();
    }

    @Test
    public void SlimeDoesNotCollideFireball() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'R', l);
        Slime s = Sprite.slimeFactory(21, 0, 'L', l);
        assertThat(s.spriteCollision(fb)).isFalse();
    }

    @Test
    public void SlimeCollidePlayer() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Slime s = Sprite.slimeFactory(21, 21, 'L', l);
        assertThat(s.spriteCollision(l.getPlayer())).isTrue();
    }

    @Test
    public void SlimeDoesNotCollidePlayer() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Slime s = Sprite.slimeFactory(20, 20, 'L', l);
        assertThat(s.spriteCollision(l.getPlayer())).isFalse();
    }

    @Test
    public void SlimeCollideItselfReturnsFalse() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Slime s = Sprite.slimeFactory(20, 20, 'L', l);
        assertThat(s.spriteCollision(s)).isFalse();
    }

    /* Fireball Projectile Collision Testing */
    @Test
    public void FireballCollideSlime() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'R', l);
        Slime s = Sprite.slimeFactory(19, 19, 'L', l);
        assertThat(fb.spriteCollision(s)).isTrue();
    }

    @Test
    public void FireballDoesNotCollideSlime() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'R', l);
        Slime s = Sprite.slimeFactory(20, 20, 'L', l);
        assertThat(fb.spriteCollision(s)).isFalse();
    }

    @Test
    public void FireballCollideGremlin() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'R', l);
        Gremlin g = Sprite.gremlinFactory(19, 19, l);
        assertThat(fb.spriteCollision(g)).isTrue();
    }

    @Test
    public void FireballDoesNotCollideGremlin() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'R', l);
        Gremlin g = Sprite.gremlinFactory(20, 20, l);
        assertThat(fb.spriteCollision(g)).isFalse();
    }

    @Test
    public void FireballCollideItselfReturnFalse() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Fireball fb = Sprite.fireballFactory(0, 0, 'R', l);
        assertThat(fb.spriteCollision(fb)).isFalse();
    }

    /* Iceball Projectile Collision Testing */
    @Test
    public void IceballCollideSlime() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Iceball ib = Sprite.iceballFactory(0, 0, 'R', l);
        Slime s = Sprite.slimeFactory(19, 19, 'L', l);
        assertThat(ib.spriteCollision(s)).isTrue();
    }

    @Test
    public void IceballDoesNotCollideSlime() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Iceball ib = Sprite.iceballFactory(0, 0, 'R', l);
        Slime s = Sprite.slimeFactory(20, 20, 'L', l);
        assertThat(ib.spriteCollision(s)).isFalse();
    }

    @Test
    public void IceballCollideGremlin() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Iceball ib = Sprite.iceballFactory(0, 0, 'R', l);
        Gremlin g = Sprite.gremlinFactory(19, 19, l);
        assertThat(ib.spriteCollision(g)).isTrue();
    }

    @Test
    public void IceballDoesNotCollideGremlin() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Iceball ib = Sprite.iceballFactory(0, 0, 'R', l);
        Gremlin g = Sprite.gremlinFactory(20, 20, l);
        assertThat(ib.spriteCollision(g)).isFalse();
    }

    @Test
    public void IceballCollideItselfReturnFalse() {
        Level l = Level.generateLevel(new File("TestLevels/Empty.txt"), 3, 3);
        Iceball ib = Sprite.iceballFactory(0, 0, 'R', l);
        assertThat(ib.spriteCollision(ib)).isFalse();
    }


}
