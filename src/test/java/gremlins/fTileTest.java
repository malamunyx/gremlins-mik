package gremlins;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class fTileTest {
    @Test
    public void instantiatedPowerupCannotEffectPlayer() {
        Powerup p = Tile.powerupFactory(0, 0);
        assertThat(p.canEffectPlayer()).isFalse();
    }

    /* Wall testing */
    @Test
    public void unbreakUnbrokenWall() {
        Wall w = Tile.brickWallFactory(0, 0);
        w.unbreakWall();
        assertThat(w.isBroken()).isFalse();
    }
}
