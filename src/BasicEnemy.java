import javax.swing.*;
import java.awt.*;

public class BasicEnemy extends Enemy {
    public BasicEnemy(int x, int y, int speed) {
        super(x, y, speed, 1);
        this.imagen = new ImageIcon(getClass().getResource("/assets/enemy.png")).getImage();
    }
}