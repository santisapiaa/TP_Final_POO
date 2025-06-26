import java.awt.*;

public class Balas implements IDibujable{
    private int x, y;

    public Balas(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void mover() { y -= 7; }

    public void dibujar(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 8, 16);
    }

    public int getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 8, 16);
    }
}