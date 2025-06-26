import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class PowerUp implements IDibujable{
    public enum Tipo { ESCUDO, DISPARO_RAPIDO, DISPARO_TRIPLE }

    private int x, y;
    private final Tipo tipo;
    private Image imagen;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
        this.tipo = Tipo.values()[new Random().nextInt(3)];

        switch (tipo) {
            case ESCUDO -> imagen = new ImageIcon(getClass().getResource("/assets/powerup_escudo.png")).getImage();
            case DISPARO_RAPIDO -> imagen = new ImageIcon(getClass().getResource("/assets/powerup_rapido.png")).getImage();
            case DISPARO_TRIPLE -> imagen = new ImageIcon(getClass().getResource("/assets/powerup_triple.png")).getImage();
        }
    }

    public void mover() {
        y += 2;
    }

    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, 30, 30, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 30);
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getY() {
        return y;
    }
}