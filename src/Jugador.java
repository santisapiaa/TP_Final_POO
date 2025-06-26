import javax.swing.*;
import java.awt.*;

public class Jugador {
    private int x, y;
    private int cooldownDisparo = 0;
    private boolean escudo = false;
    private boolean disparoTriple = false;
    private int disparoTripleTimer = 0;
    private int disparoRapidoTimer = 0;
    private int powerUpsUsados = 0;
    private int disparoRapidoCooldown = 15;
    private Image imagen;

    public Jugador(int x, int y) {
        this.x = x;
        this.y = y;
        this.imagen = new ImageIcon(getClass().getResource("/assets/nave.png")).getImage();
    }

    public void mover(int dx) {
        x += dx;
        if (x < 0) x = 0;
        if (x > 560) x = 560;
    }

    public void dibujar(Graphics g) {
        int size = escudo ? 48 : 40;
        g.drawImage(imagen, x, y, size, size, null);
    }

    public boolean puedeDisparar() {
        return cooldownDisparo == 0;
    }

    public void resetCooldownDisparo() {
        cooldownDisparo = disparoRapidoCooldown;
    }

    public void updateCooldown() {
        if (cooldownDisparo > 0) cooldownDisparo--;
        if (disparoTripleTimer > 0) {
            disparoTripleTimer--;
            if (disparoTripleTimer == 0) disparoTriple = false;
        }
        if (disparoRapidoTimer > 0) {
            disparoRapidoTimer--;
            if (disparoRapidoTimer == 0) disparoRapidoCooldown = 15;
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
                disparoRapidoCooldown = 5;
                disparoRapidoTimer = 667; // 10 segundos
            }
        }
    }

    public boolean tieneEscudo() { return escudo; }
    public void desactivarEscudo() { escudo = false; }
    public boolean isDisparoTriple() { return disparoTriple; }
    public int getPowerUpsUsados() { return powerUpsUsados; }
}