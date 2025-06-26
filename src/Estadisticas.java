import java.io.*;
import java.util.*;
import java.awt.*;
import java.util.List;

public class Estadisticas{
    private static final String STATS_FILE = "stats.dat";

    public static class PlayerStats implements Serializable {
        String nombre;
        int statEnemigosDestruidos;
        int nivelAlcanzado;
        int powerUpsUsados;

        PlayerStats(String nombre, int statEnemigosDestruidos, int nivelAlcanzado, int powerUpsUsados) {
            this.nombre = nombre;
            this.statEnemigosDestruidos = statEnemigosDestruidos;
            this.nivelAlcanzado = nivelAlcanzado;
            this.powerUpsUsados = powerUpsUsados;
        }
    }

    protected List<PlayerStats> stats;

    public Estadisticas() {
        stats = new ArrayList<>();
        loadStats();
    }

    public void saveStats(String nombre, int statEnemigosDestruidos, int nivelAlcanzado, int powerUpsUsados) {
        // Verifica si el último registro es idéntico para evitar duplicados
        if (!stats.isEmpty() && stats.get(stats.size() - 1).nombre.equals(nombre) &&
                stats.get(stats.size() - 1).statEnemigosDestruidos == statEnemigosDestruidos &&
                stats.get(stats.size() - 1).nivelAlcanzado == nivelAlcanzado &&
                stats.get(stats.size() - 1).powerUpsUsados == powerUpsUsados) {
            return; // No guarda si es duplicado
        }
        stats.add(new PlayerStats(nombre, statEnemigosDestruidos, nivelAlcanzado, powerUpsUsados));
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
}