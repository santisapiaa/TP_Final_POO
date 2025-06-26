import javax.swing.*;
import java.awt.*;

public class Player {
    private int x, y;
    private int shootCooldown = 0;
    private boolean escudo = false;
    private boolean disparoTriple = false;
    private int disparoTripleTimer = 0;
    private int rapidFireTimer = 0;
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
        if (x > 560) x = 560;
    }

    public void draw(Graphics g) {
        int size = escudo ? 48 : 40;
        g.drawImage(imagen, x, y, size, size, null);
    }

    public boolean canShoot() {
        return shootCooldown == 0;
    }

    public void resetShootCooldown() {
        shootCooldown = rapidFireCooldown;
    }

    public void updateCooldown() {
        if (shootCooldown > 0) shootCooldown--;
        if (disparoTripleTimer > 0) {
            disparoTripleTimer--;
            if (disparoTripleTimer == 0) disparoTriple = false;
        }
        if (rapidFireTimer > 0) {
            rapidFireTimer--;
            if (rapidFireTimer == 0) rapidFireCooldown = 15;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Rectangle getBounds() {
        int size = escudo ? 48 : 40;
        return new Rectangle(x, y, size, size);
    }

    public void activarPowerUp(PowerUp p) {
        powerUpsUsados++;
        switch (p.getTipo()) {
            case ESCUDO -> escudo = true;
            case DISPARO_TRIPLE -> {
                disparoTriple = true;
                disparoTripleTimer = 667; // 10 segundos a 15ms por frame
            }
            case DISPARO_RAPIDO -> {
                rapidFireCooldown = 5;
                rapidFireTimer = 667; // 10 segundos
            }
        }
    }

    public boolean tieneEscudo() { return escudo; }
    public void desactivarEscudo() { escudo = false; }
    public boolean isDisparoTriple() { return disparoTriple; }
    public int getPowerUpsUsados() { return powerUpsUsados; }
}