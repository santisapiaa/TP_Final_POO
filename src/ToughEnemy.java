import javax.swing.*;
import java.awt.*;

public class ToughEnemy extends Enemy {
    public ToughEnemy(int x, int y, int speed) {
        super(x, y, speed / 2, 2);
        this.imagen = new ImageIcon(getClass().getResource("/assets/enemy2.png")).getImage();
    }
}