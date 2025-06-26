import javax.swing.*;
import java.awt.*;

public abstract class Enemy {
    protected int x, y, speed;
    protected Image imagen;
    protected int health;

    public Enemy(int x, int y, int speed, int health) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
    }

    public void move() { y += speed; }

    public void draw(Graphics g) {
        g.drawImage(imagen, x, y, 40, 40, null);
    }

    public int getY() { return y; }
    public int getX() { return x; }
    public Rectangle getBounds() { return new Rectangle(x, y, 40, 40); }
    public void hit() { health--; }
    public boolean isDestroyed() { return health <= 0; }
}