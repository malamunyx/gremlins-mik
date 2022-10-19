package gremlins;

import org.junit.jupiter.api.Test;
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

}
