import java.awt.*;

public class Bullet {
    private int x, y;
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() { y -= 7; }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 5, 10);
    }

    public int getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 5, 10);
    }
}