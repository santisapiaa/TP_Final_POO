import java.io.*;
import java.util.*;
import java.awt.*;
import java.util.List;

public class StatsManager {
    private static final String STATS_FILE = "stats.dat";

    // Hacer PlayerStats public static para que sea accesible desde otras clases
    public static class PlayerStats implements Serializable {
        String name;
        int enemiesDestroyed;
        int levelReached;
        int powerUpsUsed;

        PlayerStats(String name, int enemiesDestroyed, int levelReached, int powerUpsUsed) {
            this.name = name;
            this.enemiesDestroyed = enemiesDestroyed;
            this.levelReached = levelReached;
            this.powerUpsUsed = powerUpsUsed;
        }
    }

    protected List<PlayerStats> stats;

    public StatsManager() {
        stats = new ArrayList<>();
        loadStats();
    }

    public void saveStats(String name, int enemiesDestroyed, int levelReached, int powerUpsUsed) {
        // Verifica si el último registro es idéntico para evitar duplicados
        if (!stats.isEmpty() && stats.get(stats.size() - 1).name.equals(name) &&
                stats.get(stats.size() - 1).enemiesDestroyed == enemiesDestroyed &&
                stats.get(stats.size() - 1).levelReached == levelReached &&
                stats.get(stats.size() - 1).powerUpsUsed == powerUpsUsed) {
            return; // No guarda si es duplicado
        }
        stats.add(new PlayerStats(name, enemiesDestroyed, levelReached, powerUpsUsed));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STATS_FILE))) {
            oos.writeObject(stats);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadStats() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STATS_FILE))) {
            stats = (List<PlayerStats>) ois.readObject();
        } catch (FileNotFoundException e) {
            stats = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void drawStats(Graphics g, int yOffset) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Récords de todos los jugadores:", 150, yOffset);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        int y = yOffset + 30;
        for (PlayerStats stat : stats) {
            g.drawString(stat.name + ": " + stat.enemiesDestroyed + " enemigos, nivel " + stat.levelReached + ", " + stat.powerUpsUsed + " power-ups", 150, y);
            y += 25;
        }
    }
}