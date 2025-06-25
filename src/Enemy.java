import javax.swing.*;
import java.awt.*;

public class Enemy {
    private int x, y, speed;
    private Image imagen;

    public Enemy(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.imagen = new ImageIcon(getClass().getResource("/assets/enemy.png")).getImage();
    }

    public void move() { y += speed; }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, 30, 30);
        g.drawImage(imagen, x, y, 30, 30, null);
    }

    public int getY() { return y; }

    public int getX() { return x; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }
}