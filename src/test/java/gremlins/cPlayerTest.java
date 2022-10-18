package gremlins;

import org.junit.jupiter.api.Test;
import processing.data.JSONObject;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static processing.core.PApplet.loadJSONObject;

public class cPlayerTest {
    App a = new App();
    private JSONObject conf = App.loadJSONObject(new File(a.configPath));
    private String fname = conf.getJSONArray("levels").getJSONObject(0).getString("layout");
    private double wz_cd = conf.getJSONArray("levels").getJSONObject(0).getDouble("wizard_cooldown");
    private double en_cd = conf.getJSONArray("levels").getJSONObject(0).getDouble("enemy_cooldown");

    /* GetDirNum Testing */
    @Test
    public void GetDirNumRight() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().right();
        assertThat(l.getPlayer().getDirNum()).isEqualTo(1);
    }

    @Test
    public void GetDirNumUp() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().up();
        assertThat(l.getPlayer().getDirNum()).isEqualTo(2);
    }

    @Test
    public void GetDirNumDown() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().down();
        assertThat(l.getPlayer().getDirNum()).isEqualTo(3);
    }

    @Test
    public void GetDirNumInvalidDir() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        char invalid = 'Q';
        l.getPlayer().setDir(invalid);
        assertThatThrownBy(() -> {
            l.getPlayer().getDirNum();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("Direction %c is not a valid direction", invalid));

    }

    /* Player Movement */
    @Test
    public void testPlayerMoveLeft() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().left();
        assertThat(l.getPlayer().getxDir()).isEqualTo(-1);
        assertThat(l.getPlayer().getyDir()).isEqualTo(0);
        assertThat(l.getPlayer().getDir()).isEqualTo('L');
    }

    @Test
    public void testPlayerMoveRight() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().right();
        assertThat(l.getPlayer().getxDir()).isEqualTo(1);
        assertThat(l.getPlayer().getyDir()).isEqualTo(0);
        assertThat(l.getPlayer().getDir()).isEqualTo('R');
    }

    @Test
    public void testPlayerMoveUp() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().SetPosition(20,40);
        l.getPlayer().up();
        assertThat(l.getPlayer().getxDir()).isEqualTo(0);
        assertThat(l.getPlayer().getyDir()).isEqualTo(-1);
        assertThat(l.getPlayer().getDir()).isEqualTo('U');
    }

    @Test
    public void testPlayerMoveDown() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().SetPosition(60,20);
        l.getPlayer().down();
        assertThat(l.getPlayer().getxDir()).isEqualTo(0);
        assertThat(l.getPlayer().getyDir()).isEqualTo(1);
        assertThat(l.getPlayer().getDir()).isEqualTo('D');
    }

    @Test
    public void testPlayerMoveLeftObstruction() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().SetPosition(20,20);
        l.getPlayer().left();
        assertThat(l.getPlayer().getxDir()).isEqualTo(0);
        assertThat(l.getPlayer().getyDir()).isEqualTo(0);
        assertThat(l.getPlayer().getDir()).isEqualTo('L');
    }

    @Test
    public void testPlayerMoveRightObstruction() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().SetPosition(60,20);
        l.getPlayer().right();
        assertThat(l.getPlayer().getxDir()).isEqualTo(0);
        assertThat(l.getPlayer().getyDir()).isEqualTo(0);
        assertThat(l.getPlayer().getDir()).isEqualTo('R');
    }

    @Test
    public void testPlayerMoveUpObstruction() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().up();
        assertThat(l.getPlayer().getxDir()).isEqualTo(0);
        assertThat(l.getPlayer().getyDir()).isEqualTo(0);
        assertThat(l.getPlayer().getDir()).isEqualTo('U');
    }

    @Test
    public void testPlayerMoveDownObstruction() {
        Level l = Level.generateLevel(new File(fname), wz_cd, en_cd);
        l.getPlayer().down();
        assertThat(l.getPlayer().getxDir()).isEqualTo(0);
        assertThat(l.getPlayer().getyDir()).isEqualTo(0);
        assertThat(l.getPlayer().getDir()).isEqualTo('D');
    }


}
