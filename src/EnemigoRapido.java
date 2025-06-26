import javax.swing.*;

public class EnemigoRapido extends Enemigo {
    public EnemigoRapido(int x, int y, int velocidad) {
        super(x, y, velocidad, 1);
        this.imagen = new ImageIcon(getClass().getResource("/assets/enemy3.png")).getImage();
    }
}