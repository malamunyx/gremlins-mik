package gremlins;

public interface Projectile {
    boolean checkWallCollision();
    boolean isNeutralised();
    void stop();
}
