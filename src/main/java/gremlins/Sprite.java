package gremlins;

public interface Sprite {
    int xOffset = 10;
    int yOffset = 10;
    int getCentreX();
    int getCentreY();
    boolean intersects(Sprite s);

    void reset(Game g);
}
