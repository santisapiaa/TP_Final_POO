import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<PowerUp> powerUps;
    private boolean left, right, shooting;
    private int enemySpawnCounter = 0;
    private boolean gameOver = false;
    private boolean showStats = false;
    private String playerName;
    private Difficulty difficulty;
    private int vidas = 3;
    private int enemigosDestruidos = 0;
    private int nivel = 1;
    private StatsManager statsManager;
    private JButton restartButton;

    public GamePanel(String nombre, Difficulty difficulty) {
        this.playerName = nombre;
        this.difficulty = difficulty;
        this.setFocusable(true);
        this.addKeyListener(this);
        player = new Player(280, 700);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        powerUps = new ArrayList<>();
        timer = new Timer(15, this);
        statsManager = new StatsManager();
        restartButton = new JButton("Reiniciar");
        restartButton.setBounds(220, 450, 100, 40);
        restartButton.addActionListener(e -> restartGame());
        restartButton.setVisible(false);
        this.setLayout(null);
        this.add(restartButton);
    }

    public void start() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (left) player.move(-5);
            if (right) player.move(5);
            if (shooting) playerShoot();

            updateBullets();
            updateEnemies();
            updatePowerUps();
            checkCollisions();
            repaint();
        }
    }

    private void playerShoot() {
        if (player.canShoot()) {
            bullets.add(new Bullet(player.getX() + 16, player.getY()));
            if (player.isDisparoTriple()) {
                bullets.add(new Bullet(player.getX() + 6, player.getY()));
                bullets.add(new Bullet(player.getX() + 26, player.getY()));
            }
            player.resetShootCooldown();
        }
    }

    private void updateBullets() {
        bullets.removeIf(b -> b.getY() < 0);
        bullets.forEach(Bullet::move);
    }

    private void updateEnemies() {
        enemySpawnCounter++;
        if (enemySpawnCounter % Math.max(10, difficulty.spawnRate - nivel * 2) == 0) {
            if (new Random().nextDouble() < 0.2) {
                enemies.add(new ToughEnemy(new Random().nextInt(550), 0, difficulty.enemySpeed + nivel));
            } else {
                enemies.add(new BasicEnemy(new Random().nextInt(550), 0, difficulty.enemySpeed + nivel));
            }
        }
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.move();
            if (e.getY() > 780) {
                it.remove();
                perderVida();
            }
        }
        player.updateCooldown();
    }

    private void updatePowerUps() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.move();
            if (p.getBounds().intersects(player.getBounds())) {
                player.activarPowerUp(p);
                it.remove();
            } else if (p.getY() > 800) {
                it.remove();
            }
        }
    }

    private void checkCollisions() {
        Iterator<Enemy> ei = enemies.iterator();
        while (ei.hasNext()) {
            Enemy enemy = ei.next();
            Rectangle er = enemy.getBounds();
            Iterator<Bullet> bi = bullets.iterator();
            while (bi.hasNext()) {
                Bullet b = bi.next();
                if (er.intersects(b.getBounds())) {
                    enemy.hit();
                    bi.remove();
                    if (enemy.isDestroyed()) {
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
        if (player.tieneEscudo()) {
            player.desactivarEscudo();
        } else {
            vidas--;
            if (vidas <= 0) {
                gameOver = true;
            }
        }
    }

    private void restartGame() {
        if (gameOver || showStats) {
            // Verifica si hay estadísticas y si el último registro es idéntico para evitar duplicados
            if (!statsManager.stats.isEmpty()) {
                StatsManager.PlayerStats lastStat = statsManager.stats.get(statsManager.stats.size() - 1);
                if (lastStat.name.equals(playerName) &&
                        lastStat.enemiesDestroyed == enemigosDestruidos &&
                        lastStat.levelReached == nivel &&
                        lastStat.powerUpsUsed == player.getPowerUpsUsados()) {
                    // No guarda si es duplicado
                } else {
                    statsManager.saveStats(playerName, enemigosDestruidos, nivel, player.getPowerUpsUsados());
                }
            } else {
                statsManager.saveStats(playerName, enemigosDestruidos, nivel, player.getPowerUpsUsados());
            }
            // Solicita nuevo nombre
            String nuevoNombre = JOptionPane.showInputDialog(null, "Ingresá tu nombre:");
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                this.playerName = nuevoNombre;
            } else {
                this.playerName = "Jugador"; // Nombre por defecto si cancelan
            }
        }
        player = new Player(280, 700);
        enemies.clear();
        bullets.clear();
        powerUps.clear();
        vidas = 3;
        enemigosDestruidos = 0;
        nivel = 1;
        gameOver = false;
        showStats = false;
        restartButton.setVisible(false);
        timer.start();
        requestFocus();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(30 + 10 * nivel % 100, 20, 60 + 10 * nivel % 100));

        if (!showStats) {
            player.draw(g);
            bullets.forEach(b -> b.draw(g));
            enemies.forEach(e -> e.draw(g));
            powerUps.forEach(p -> p.draw(g));

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
                restartButton.setVisible(true);
            }
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Estadísticas de " + playerName, 150, 200);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Enemigos destruidos: " + enemigosDestruidos, 150, 250);
            g.drawString("Nivel alcanzado: " + nivel, 150, 280);
            g.drawString("PowerUps usados: " + player.getPowerUpsUsados(), 150, 310);
            statsManager.drawStats(g, 350);
            restartButton.setVisible(true);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
            showStats = true;
            restartButton.setVisible(true);
            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = false;
    }

    public void keyTyped(KeyEvent e) {}
}