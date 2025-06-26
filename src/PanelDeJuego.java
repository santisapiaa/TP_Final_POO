import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PanelDeJuego extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Jugador jugador;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Balas> balas;
    private ArrayList<PowerUp> powerUps;
    private boolean izq, der, disparo;
    private int contadorEnemigos = 0;
    private boolean gameOver = false;
    private boolean mostrarEstadisticas = false;
    private String nombreJugador;
    private Dificultad dificultad;
    private int vidas = 3;
    private int enemigosDestruidos = 0;
    private int nivel = 1;
    private Estadisticas estadisticas;
    private JButton botonRestart;
    private JScrollPane tablaEstadisticas;
    private JList<String> listaEstadisticas;

    public PanelDeJuego(String nombre, Dificultad dificultad) {
        this.nombreJugador = nombre;
        this.dificultad = dificultad;
        this.setFocusable(true);
        this.addKeyListener(this);
        jugador = new Jugador(280, 700);
        enemigos = new ArrayList<>();
        balas = new ArrayList<>();
        powerUps = new ArrayList<>();
        timer = new Timer(15, this);
        estadisticas = new Estadisticas();
        botonRestart = new JButton("Reiniciar");
        botonRestart.setBounds(220, 650, 100, 40);
        botonRestart.addActionListener(e -> restartGame());
        botonRestart.setVisible(false);

        // Initialize stats list and scroll pane
        listaEstadisticas = new JList<>();
        listaEstadisticas.setFont(new Font("Arial", Font.PLAIN, 16));
        listaEstadisticas.setBackground(new Color(30, 20, 60));
        listaEstadisticas.setForeground(Color.WHITE);
        tablaEstadisticas = new JScrollPane(listaEstadisticas);
        tablaEstadisticas.setBounds(150, 350, 300, 200);
        tablaEstadisticas.setVisible(false);

        this.setLayout(null);
        this.add(botonRestart);
        this.add(tablaEstadisticas);
    }

    public void start() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (izq) jugador.mover(-5);
            if (der) jugador.mover(5);
            if (disparo) playerShoot();

            updateBullets();
            updateEnemies();
            updatePowerUps();
            checkCollisions();
            repaint();
        }
    }

    private void playerShoot() {
        if (jugador.puedeDisparar()) {
            balas.add(new Balas(jugador.getX() + 16, jugador.getY()));
            if (jugador.isDisparoTriple()) {
                balas.add(new Balas(jugador.getX() + 6, jugador.getY()));
                balas.add(new Balas(jugador.getX() + 26, jugador.getY()));
            }
            jugador.resetCooldownDisparo();
        }
    }

    private void updateBullets() {
        balas.removeIf(b -> b.getY() < 0);
        balas.forEach(Balas::mover);
    }

    private void updateEnemies() {
        contadorEnemigos++;
        if (contadorEnemigos % Math.max(10, dificultad.tasaSpawn - nivel * 2) == 0) {
            Random rand = new Random();
            double spawnChance = rand.nextDouble();
            if (spawnChance < 0.2) {
                enemigos.add(new EnemigoDuro(rand.nextInt(550), 0, dificultad.velocidadEnemigo + nivel));
            } else if (spawnChance < 0.5) {
                enemigos.add(new EnemigoRapido(rand.nextInt(550), 0, dificultad.velocidadEnemigo + nivel));
            } else {
                enemigos.add(new EnemigoBasico(rand.nextInt(550), 0, dificultad.velocidadEnemigo + nivel));
            }
        }
        Iterator<Enemigo> it = enemigos.iterator();
        while (it.hasNext()) {
            Enemigo e = it.next();
            e.mover();
            if (e.getY() > 780) {
                it.remove();
                perderVida();
            }
        }
        jugador.updateCooldown();
    }

    private void updatePowerUps() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.mover();
            if (p.getBounds().intersects(jugador.getBounds())) {
                jugador.activarPowerUp(p);
                it.remove();
            } else if (p.getY() > 800) {
                it.remove();
            }
        }
    }

    private void checkCollisions() {
        Iterator<Enemigo> ei = enemigos.iterator();
        while (ei.hasNext()) {
            Enemigo enemy = ei.next();
            Rectangle er = enemy.getBounds();
            Iterator<Balas> bi = balas.iterator();
            while (bi.hasNext()) {
                Balas b = bi.next();
                if (er.intersects(b.getBounds())) {
                    enemy.golpe();
                    bi.remove();
                    if (enemy.esDestruido()) {
                        ei.remove();
                        enemigosDestruidos++;
                        if (new Random().nextDouble() < 0.1) {
                            powerUps.add(new PowerUp(enemy.getX(), enemy.getY()));
                        }
                        if (enemigosDestruidos % 10 == 0) nivel++;
                    }
                    break;
                }
            }
        }
    }

    private void perderVida() {
        if (jugador.tieneEscudo()) {
            jugador.desactivarEscudo();
        } else {
            vidas--;
            if (vidas <= 0) {
                gameOver = true;
            }
        }
    }

    private void restartGame() {
        if (gameOver || mostrarEstadisticas) {
            if (!estadisticas.stats.isEmpty()) {
                Estadisticas.PlayerStats lastStat = estadisticas.stats.get(estadisticas.stats.size() - 1);
                if (lastStat.nombre.equals(nombreJugador) &&
                        lastStat.statEnemigosDestruidos == enemigosDestruidos &&
                        lastStat.nivelAlcanzado == nivel &&
                        lastStat.powerUpsUsados == jugador.getPowerUpsUsados()) {
                    // No guarda si es duplicado
                } else {
                    estadisticas.saveStats(nombreJugador, enemigosDestruidos, nivel, jugador.getPowerUpsUsados());
                }
            } else {
                estadisticas.saveStats(nombreJugador, enemigosDestruidos, nivel, jugador.getPowerUpsUsados());
            }
            String nuevoNombre = JOptionPane.showInputDialog(null, "Ingresá tu nombre:");
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                this.nombreJugador = nuevoNombre;
            } else {
                this.nombreJugador = "Jugador";
            }
        }
        jugador = new Jugador(280, 700);
        enemigos.clear();
        balas.clear();
        powerUps.clear();
        vidas = 3;
        enemigosDestruidos = 0;
        nivel = 1;
        gameOver = false;
        mostrarEstadisticas = false;
        botonRestart.setVisible(false);
        tablaEstadisticas.setVisible(false);
        timer.start();
        requestFocus();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Fondo azul cielo que se oscurece con cada nivel
        int red = Math.max(135 - 10 * (nivel - 1), 0);
        int green = Math.max(206 - 10 * (nivel - 1), 0);
        int blue = Math.max(235 - 10 * (nivel - 1), 0);
        setBackground(new Color(red, green, blue));

        if (!mostrarEstadisticas) {
            jugador.dibujar(g);
            balas.forEach(b -> b.dibujar(g));
            enemigos.forEach(e -> {
                if (e instanceof EnemigoRapido) {
                    g.drawImage(e.imagen, e.getX(), e.getY(), 30, 30, null);
                } else {
                    e.dibujar(g);
                }
            });
            powerUps.forEach(p -> p.dibujar(g));

            g.setColor(Color.WHITE);
            g.drawString("Vidas: " + vidas, 20, 20);
            g.drawString("Nivel: " + nivel, 20, 40);
            g.drawString("Enemigos: " + enemigosDestruidos, 20, 60);

            if (gameOver) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 36));
                g.drawString("GAME OVER", 180, 350);
                g.setFont(new Font("Arial", Font.PLAIN, 18));
                g.drawString("Presioná ENTER para ver estadísticas", 130, 400);
                botonRestart.setVisible(true);
                tablaEstadisticas.setVisible(false);
            }
        } else {
            // lista de estadisticas
            DefaultListModel<String> model = new DefaultListModel<>();

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Estadísticas de " + nombreJugador, 150,200);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Enemigos destruidos: " + enemigosDestruidos, 150,250);
            g.drawString("Nivel alcanzado: " + nivel, 150,280);
            g.drawString("PowerUps usados: " + jugador.getPowerUpsUsados(), 150,310);

            model.addElement("--- Estadísticas Generales ---");
            for (Estadisticas.PlayerStats stat : estadisticas.stats) {
                model.addElement(String.format("%s: %d enemigos, nivel %d, %d powerups",
                        stat.nombre, stat.statEnemigosDestruidos, stat.nivelAlcanzado, stat.powerUpsUsados));
            }
            listaEstadisticas.setModel(model);
            tablaEstadisticas.setVisible(true);
            botonRestart.setVisible(true);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
            mostrarEstadisticas = true;
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) izq = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) der = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) disparo = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) izq = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) der = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) disparo = false;
    }

    public void keyTyped(KeyEvent e) {}
}