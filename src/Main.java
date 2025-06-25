import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String nombre = JOptionPane.showInputDialog(null, "Ingresá tu nombre:");

        String[] dificultades = {"Fácil", "Medio", "Difícil"};
        String dificultadSeleccionada = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona la dificultad:",
                "Dificultad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                dificultades,
                dificultades[0]
        );

        Difficulty difficulty = switch (dificultadSeleccionada) {
            case "Medio" -> Difficulty.MEDIO;
            case "Difícil" -> Difficulty.DIFICIL;
            default -> Difficulty.FACIL;
        };

        JFrame frame = new JFrame("Galaga");
        GamePanel panel = new GamePanel(nombre, difficulty);
        frame.add(panel);
        frame.setSize(600, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        panel.start();
    }
}