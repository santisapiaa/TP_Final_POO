import javax.swing.*;
import java.awt.*;

public class EnemigoBasico extends Enemigo {
    public EnemigoBasico(int x, int y, int velocidad) {
        super(x, y, velocidad / 2, 1);
        this.imagen = new ImageIcon(getClass().getResource("/assets/enemy.png")).getImage();
    }
}