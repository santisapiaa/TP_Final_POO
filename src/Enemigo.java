import java.awt.*;

public abstract class Enemigo {
    protected int x, y, velocidad;
    protected Image imagen;
    protected int vida;

    public Enemigo(int x, int y, int velocidad, int vida) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.vida = vida;
    }

    public void mover() { y += velocidad; }

    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, 40, 40, null);
    }

    public int getY() { return y; }
    public int getX() { return x; }
    public Rectangle getBounds() { return new Rectangle(x, y, 40, 40); }
    public void golpe() { vida--; }
    public boolean esDestruido() { return vida <= 0; }
}