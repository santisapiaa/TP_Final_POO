import javax.swing.*;
import java.awt.*;

public class Player {
    private int x, y;
    private int shootCooldown = 0;
    private boolean escudo = false;
    private boolean disparoTriple = false;
    private int powerUpsUsados = 0;
    private int rapidFireCooldown = 15;
    private Image imagen;


    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.imagen = new ImageIcon(getClass().getResource("/assets/nave.png")).getImage();
    }

    public void move(int dx) {
        x += dx;
        if (x < 0) x = 0;
        if (x > 570) x = 570;
    }

    public void draw(Graphics g) {
        g.setColor(escudo ? Color.GREEN : Color.CYAN);
        g.fillRect(x, y, 30, 20);
        g.drawImage(imagen, x, y, 30, 20, null);
    }

    public boolean canShoot() {
        return shootCooldown == 0;
    }

    public void resetShootCooldown() {
        shootCooldown = rapidFireCooldown;
    }

    public void updateCooldown() {
        if (shootCooldown > 0) shootCooldown--;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 20);
    }

    public void activarPowerUp(PowerUp p) {
        powerUpsUsados++;
        switch (p.getTipo()) {
            case ESCUDO -> escudo = true;
            case DISPARO_TRIPLE -> disparoTriple = true;
            case DISPARO_RAPIDO -> rapidFireCooldown = 5;
        }
    }

    public boolean tieneEscudo() { return escudo; }
    public void desactivarEscudo() { escudo = false; }
    public boolean isDisparoTriple() { return disparoTriple; }
    public int getPowerUpsUsados() { return powerUpsUsados; }
}