import javax.swing.*;

public class EnemigoDuro extends Enemigo {
    public EnemigoDuro(int x, int y, int velocidad) {
        super(x, y, velocidad / 3, 2);
        this.imagen = new ImageIcon(getClass().getResource("/assets/enemy2.png")).getImage();
    }
}